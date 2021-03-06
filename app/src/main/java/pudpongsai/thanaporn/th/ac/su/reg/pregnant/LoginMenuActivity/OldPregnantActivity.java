package pudpongsai.thanaporn.th.ac.su.reg.pregnant.LoginMenuActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.HomeMenuActivity.HomeActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class OldPregnantActivity extends AppCompatActivity {

    Activity mContext = OldPregnantActivity.this;

    String weekOld,dayOld, oldRange,weight,high;
    EditText edtWeight,edtHigh,edtWeek,edtDay;

    Button btnOldSave,btnOld1,btnOld2,btnOld3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_old_pregnant);
        oldRange = "";
        edtWeight = (EditText)findViewById(R.id.edtWeight);
        edtHigh = (EditText)findViewById(R.id.edtHigh);
        edtWeek = (EditText)findViewById(R.id.edtWeek);
        edtDay = (EditText)findViewById(R.id.edtDay);

        btnOld1 = (Button) findViewById(R.id.btnOld1);
        btnOld2 = (Button) findViewById(R.id.btnOld2);
        btnOld3 = (Button) findViewById(R.id.btnOld3);

        btnOldSave = (Button) findViewById(R.id.btnOldSave);

        btnOldSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOldPregnant();
            }
        });
        if (UserDetail.profileMode.equals("edit")){
            getProfileData();
        }

    }
    public void getProfileData(){
        String url = "https://pregnantmother-e8d1f.firebaseio.com/users/"+UserDetail.username
                +"/profile.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(!s.equals("null")) {
                    try {
                        JSONObject obj = new JSONObject(s);

                        edtWeight.setText(obj.getString("weight").toString());
                        edtHigh.setText(obj.getString("hieght").toString());
                        edtWeek.setText(obj.getJSONObject("pregnant").getString("week").toString());
                        edtDay.setText(obj.getJSONObject("pregnant").getString("day").toString());

                        switch (obj.getString("oldrange").toString()){
                            case "20 - 30 ปี" :
                                btnActive(btnOld1);
                                break;
                            case "31 - 40 ปี" :
                                btnActive(btnOld2);
                                break;
                            case "41 - 50 ปี" :
                                btnActive(btnOld3);
                                break;

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

        RequestQueue rQueue = Volley.newRequestQueue(OldPregnantActivity.this);
        rQueue.add(request);
    }

    private void saveOldPregnant() {
        boolean checkCondition = true;
        weekOld = edtWeek.getText().toString();
        dayOld = edtDay.getText().toString();
        weight = edtWeight.getText().toString();
        high = edtHigh.getText().toString();

        if(weight.equals("")){
            edtWeight.setError("can't be blank");
            checkCondition = false;
        }
        if(high.equals("")){
            edtHigh.setError("can't be blank");
            checkCondition = false;
        }
        if(oldRange.equals("")){
            oldRange = "20 - 30 ปี";
            checkCondition = false;
        }
        if(weekOld.equals("")){
            weekOld = String.valueOf(4);
        }else {
            int range = Integer.parseInt(weekOld);
            if (!(range >= 1 && range < 43)){
                edtWeek.setError("กรุณากรอกช่วงสัปดาห์ที่ ss - 42 เท่านั้น");
                checkCondition = false;
            }
        }
        if(dayOld.equals("")){
            dayOld = String.valueOf(1);
        }else {
            int range = Integer.parseInt(dayOld);
            if (!(range >= 1 && range < 8)){
                edtDay.setError("กรุณากรอกช่วงวันที่ ss - 7 เท่านั้น");
                checkCondition = false;
            }
        }

        if(checkCondition){
            DatabaseReference PregnantNoteReference = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com");

            Calendar nowDate = Calendar.getInstance();

            PregnantNoteReference.child("users").child(UserDetail.username).child("profile").child("weight").setValue(weight);
            PregnantNoteReference.child("users").child(UserDetail.username).child("profile").child("hieght").setValue(high);
            PregnantNoteReference.child("users").child(UserDetail.username).child("profile").child("oldrange").setValue(oldRange);
            PregnantNoteReference.child("users").child(UserDetail.username).child("profile").child("pregnant")
                    .child("week").setValue(weekOld);
            PregnantNoteReference.child("users").child(UserDetail.username).child("profile").child("pregnant")
                    .child("time").setValue(nowDate.getTimeInMillis());

            PregnantNoteReference.child("users").child(UserDetail.username).child("profile").child("pregnant")
                    .child("day").setValue(dayOld);
            UserDetail.weekPregnant = weekOld;
            UserDetail.dayPregnant = dayOld;
            UserDetail.oldPregnant = oldRange;
            startActivity(new Intent(OldPregnantActivity.this,HomeActivity.class));
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void btnActive(Button btnSelect){

        btnOld1.setTextColor(Color.BLACK);
        btnOld2.setTextColor(Color.BLACK);
        btnOld3.setTextColor(Color.BLACK);

        btnOld1.setBackground(ContextCompat.getDrawable(mContext,R.drawable.box_radius_btn));
        btnOld2.setBackground(ContextCompat.getDrawable(mContext,R.drawable.box_radius_btn));
        btnOld3.setBackground(ContextCompat.getDrawable(mContext,R.drawable.box_radius_btn));

        btnSelect.setBackground(ContextCompat.getDrawable(mContext,R.drawable.box_radius_btn_actcive));
        btnSelect.setTextColor(Color.WHITE);

        oldRange = btnSelect.getText().toString();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void ClickSelectRange(View view){
        switch (view.getId()){
            case R.id.btnOld1 :
                btnActive(btnOld1);
                break;
            case R.id.btnOld2 :
                btnActive(btnOld2);
                break;
            case R.id.btnOld3 :
                btnActive(btnOld3);
                break;

        }
    }
}
