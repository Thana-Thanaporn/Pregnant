package pudpongsai.thanaporn.th.ac.su.reg.pregnant.LoginMenuActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.HomeMenuActivity.HomeActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.PregnantUitli;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsernameLog,edtPasswordLog;
    String usernameLog,passwordLog;
    String deviceId;
    Button btnLog;
    JSONObject objUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        edtUsernameLog = (EditText) findViewById(R.id.edtUsernameLog);
        edtPasswordLog = (EditText) findViewById(R.id.edtPasswordLog);
        btnLog = (Button) findViewById(R.id.btnLog);

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
        getUsers();

    }

    private void checkRemember() {
        String url = "https://pregnantmother-e8d1f.firebaseio.com/remembers.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(!s.equals("null")) {
                    try {
                        JSONObject obj = new JSONObject(s);
                        if (obj.has(deviceId)){
                            UserDetail.username = obj.getJSONObject(deviceId).getString("user");
                            havePregnant(objUsers.getJSONObject(UserDetail.username).getJSONObject("profile"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError );
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
        rQueue.add(request);
    }

    private void getUsers() {
        String url = "https://pregnantmother-e8d1f.firebaseio.com/users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(!s.equals("null")) {
                    try {
                        objUsers = new JSONObject(s);
                        checkRemember();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError );
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
        rQueue.add(request);
    }

    private void havePregnant(JSONObject obj){
        try {
            if (obj.getString("weight").equals("")){
                startActivity(new Intent(LoginActivity.this,OldPregnantActivity.class));
            }else {
                UserDetail.oldPregnant = obj.getString("oldrange");
                PregnantUitli checkPregnant = new PregnantUitli();
                checkPregnant.checkNowPregnant(
                        obj.getJSONObject("pregnant").getString("time"),
                        obj.getJSONObject("pregnant").getString("week"),
                        obj.getJSONObject("pregnant").getString("day")
                );
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void checkLogin() {
        usernameLog = edtUsernameLog.getText().toString();
        passwordLog = edtPasswordLog.getText().toString();

        if(usernameLog.equals("")){
            edtUsernameLog.setError("can't be blank");
        }
        else if (passwordLog.equals("")){
            edtPasswordLog.setError("can't be blank");
        }
        else{
            final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Loading...");
            pd.show();

            try {
                if (!objUsers.has(usernameLog)) {
                    edtUsernameLog.setError("ไม่พบชื่อ");
                } else if(!objUsers.getJSONObject(usernameLog).getJSONObject("profile").getString("password").equals(passwordLog)) {
                    edtPasswordLog.setError("รหัสผิด");
                } else if(objUsers.getJSONObject(usernameLog).getJSONObject("profile").getString("password").equals(passwordLog)){
                    UserDetail.username = usernameLog;
                    havePregnant(objUsers.getJSONObject(usernameLog).getJSONObject("profile"));
                }
                pd.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void clickRegis(View v){

        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }
}
