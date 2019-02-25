package pudpongsai.thanaporn.th.ac.su.reg.pregnant.CalendarMenuActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.LoginMenuActivity.RegisterActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NoteMenuActivity.NotePregnantActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.PregnantUitli;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class AddDateActivity extends AppCompatActivity {
    Context mContext = AddDateActivity.this;
    EditText edtTopic,edtPlace,edtDetail;
    ImageButton timePicker;
    TextView txtDate;
    RelativeLayout layoutMain;

    String topic,place,detail;
    Calendar myCalendar = Calendar.getInstance();
    long day ,oldTime;

    SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_date);
        layoutMain = (RelativeLayout) findViewById(R.id.layoutMain);
        layoutMain.getForeground().setAlpha( 0);

        edtTopic = (EditText) findViewById(R.id.edtTopic);
        edtPlace = (EditText) findViewById(R.id.edtPlace);
        edtDetail = (EditText) findViewById(R.id.edtDetail);
        timePicker = (ImageButton) findViewById(R.id.timePicker);
        txtDate = (TextView) findViewById(R.id.txtDate);

        if (getIntent().hasExtra("Time")) {
            day = getIntent().getExtras().getLong("Date");
            oldTime = getIntent().getExtras().getLong("Time");
           myCalendar.setTimeInMillis(oldTime);

            String url = "https://pregnantmother-e8d1f.firebaseio.com/users/" + UserDetail.username
                    +"/calendars/"+day+"/"+oldTime+".json";
            Log.d("event time",url);
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {
                    if(!s.equals("null")) {
                        try {
                            JSONObject obj = new JSONObject(s);

                            edtTopic.setText(obj.getString("topic").toString());
                            edtPlace.setText(obj.getString("place").toString());
                            edtDetail.setText(obj.getString("detail").toString());
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

            RequestQueue rQueue = Volley.newRequestQueue(mContext);
            rQueue.add(request);
        }

        txtDate.setText(
                myCalendar.get(Calendar.DAY_OF_MONTH)+"/"
                        +(myCalendar.get(Calendar.MONTH)+1)+"/"
                        +(myCalendar.get(Calendar.YEAR)+543)
                        + "   " +formatTime.format(myCalendar.getTime()));

    }

    public void setDate(View view) {

        new DatePickerDialog (mContext,date,
                myCalendar.get (Calendar.YEAR),myCalendar.get (Calendar.MONTH),
                myCalendar.get (Calendar.DAY_OF_MONTH)).show ();

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setTime();
        }

    };



    public void setTime() {

        new TimePickerDialog(mContext,time,
                myCalendar.get (Calendar.HOUR_OF_DAY),myCalendar.get (Calendar.MINUTE),true).show ();
    }

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener () {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            myCalendar.set (Calendar.HOUR_OF_DAY,hourOfDay);
            myCalendar.set (Calendar.MINUTE,minute);
            txtDate.setText(
                    myCalendar.get(Calendar.DAY_OF_MONTH)+"/"
                            +(myCalendar.get(Calendar.MONTH)+1)+"/"
                            +(myCalendar.get(Calendar.YEAR)+543)
                            + "   " +formatTime.format(myCalendar.getTime()));
        }
    };

    public void saveDate(View view) {
        boolean checkText = true;
        topic = edtTopic.getText().toString();
        place = edtPlace.getText().toString();
        detail = edtDetail.getText().toString();

        if(topic.equals("")){
            edtTopic.setError("can't be blank");
            checkText = false;
        }
        if(place.equals("")){
            edtPlace.setError("can't be blank");
            checkText = false;
        }
        if(detail.equals("")){
            edtDetail.setError("can't be blank");
            checkText = false;
        }
        if(checkText){
            Calendar dayCal = Calendar.getInstance();
            dayCal.setTimeInMillis(myCalendar.getTimeInMillis());
            dayCal.set(Calendar.HOUR_OF_DAY,0);
            dayCal.set(Calendar.MINUTE,0);
            dayCal.set(Calendar.SECOND,0);
            dayCal.set(Calendar.MILLISECOND,0);

            boolean sameday = (day != dayCal.getTimeInMillis() || oldTime != myCalendar.getTimeInMillis());
            DatabaseReference PregnantDateReference = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+UserDetail.username
                            +"/calendars");
            if (sameday){
                PregnantDateReference.child(""+day).child(""+oldTime).setValue(null);
            }
            Map<String,String> map = new HashMap<>();
            map.put("topic", topic);
            map.put("place", place);
            map.put("detail", detail);
            PregnantDateReference.child(""+dayCal.getTimeInMillis()).child(String.valueOf(myCalendar.getTimeInMillis())).setValue(map);
            PregnantUitli.popupSaveDate(layoutMain,mContext);
        }


    }
    public void onClickBack(View v){
        onBackPressed();
    }
}
