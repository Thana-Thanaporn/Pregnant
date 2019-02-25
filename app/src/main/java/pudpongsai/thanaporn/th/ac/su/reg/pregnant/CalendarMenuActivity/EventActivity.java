package pudpongsai.thanaporn.th.ac.su.reg.pregnant.CalendarMenuActivity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Adapters.DateAdapter;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Adapters.EventAdapter;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.EventDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class EventActivity extends AppCompatActivity {
    SimpleDateFormat formatDay = new SimpleDateFormat("dd MMMM",new Locale("th","TH"));
    TextView txtDay;
    ListView listEvent;
    ArrayList<EventDetail> arrEvent = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    LinearLayout layoutMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_event);
        txtDay = (TextView) findViewById(R.id.txtDay);
        listEvent = (ListView) findViewById(R.id.listEvent);
        layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
        layoutMain.getForeground().setAlpha( 0);

        calendar.setTimeInMillis(getIntent().getExtras().getLong("Date"));

        txtDay.setText(formatDay.format(calendar.getTime())+" " + (calendar.get(Calendar.YEAR)+543));



        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+ UserDetail.username+"/calendars/"+calendar.getTimeInMillis());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                arrEvent.clear();
                for(DataSnapshot dsDay : snapshot.getChildren()) {
                    Map<String,String> map = (Map) dsDay.getValue();
                    arrEvent.add(new EventDetail(map.get("topic"),map.get("place"),map.get("detail"),Long.parseLong(dsDay.getKey())));
                }
                EventAdapter eventAdapter = new EventAdapter(arrEvent,EventActivity.this,layoutMain,calendar.getTimeInMillis());
                listEvent.setAdapter(eventAdapter);
                eventAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

    }
    public void onClickBack(View v){
        onBackPressed();
    }
}
