package pudpongsai.thanaporn.th.ac.su.reg.pregnant.CalendarMenuActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Adapters.DateAdapter;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Adapters.TelAdapter;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.EventDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.HomeMenuActivity.HomeActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NoteMenuActivity.NoteMotherActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NotiActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity.ProfileActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class CalendarActivity extends AppCompatActivity {

    Activity mContext = CalendarActivity.this;
    Locale thai = new Locale("th","TH");
    SimpleDateFormat format = new SimpleDateFormat("MMMM ",thai);
    ArrayList<EventDetail> arrEvent = new ArrayList<>();
    ListView listEvent;

    Calendar myCalendar = Calendar.getInstance();

    TextView txtMonth;
    CompactCalendarView compactCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_calendar);

        myCalendar.set(Calendar.HOUR_OF_DAY,0);
        myCalendar.set(Calendar.MINUTE,0);
        myCalendar.set(Calendar.SECOND,0);
        myCalendar.set(Calendar.MILLISECOND,0);
        getAllEvent();
        getDayEvent(new Date(myCalendar.getTimeInMillis()));
        listEvent = (ListView) findViewById(R.id.listEvent);

        txtMonth = (TextView) findViewById(R.id.txtMonth);
        txtMonth.setText(format.format(myCalendar.getTime())+ " , " + (myCalendar.get(Calendar.YEAR)+543));

        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.setFirstDayOfWeek(Calendar.SATURDAY);
        compactCalendarView.setLocale(TimeZone.getDefault(),thai);
        compactCalendarView.setUseThreeLetterAbbreviation(true);


        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                getDayEvent(dateClicked);
                Log.d("calendar", "Day was clicked: " + dateClicked.getTime() + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d("calendar", "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });



    }
    public void getDayEvent(final Date date){
        arrEvent.clear();
        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+ UserDetail.username+"/calendars/"+date.getTime());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot dsDay : snapshot.getChildren()) {
                    Map<String,String> map = (Map) dsDay.getValue();
                   arrEvent.add(new EventDetail(map.get("topic"),map.get("place"),map.get("detail"),Long.parseLong(dsDay.getKey())));
                }
                DateAdapter dateAdapter = new DateAdapter(arrEvent,mContext,date.getTime());
                listEvent.setAdapter(dateAdapter);
                dateAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }


    public void getAllEvent(){
        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+ UserDetail.username+"/calendars");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot dsDay : snapshot.getChildren()) {
                    for(DataSnapshot ds : dsDay.getChildren()) {
                        Event ev = new Event(Color.BLUE, Long.parseLong(ds.getKey()));
                        compactCalendarView.addEvent(ev);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    public void onClickNewDate(View v){
        startActivity(new Intent(mContext,AddDateActivity.class));
    }

    public void onClickMonth(View v){
        switch (v.getId()){
            case R.id.btnPreMonth:
                myCalendar.set(Calendar.MONTH,myCalendar.get(Calendar.MONTH)-1);
                compactCalendarView.scrollLeft();
                break;
            case R.id.btnNextMonth:
                myCalendar.set(Calendar.MONTH,myCalendar.get(Calendar.MONTH)+1);
                compactCalendarView.scrollRight();
                break;
        }
        txtMonth.setText(format.format(myCalendar.getTime())+ " , " + (myCalendar.get(Calendar.YEAR)+543));
    }

    public void onClickButtomMenu(View v){

        switch (v.getId()){
            case R.id.btnHome:
                startActivity(new Intent(mContext,HomeActivity.class));
                break;
            case R.id.btnCalender:
                startActivity(new Intent(mContext,CalendarActivity.class));
                break;
            case R.id.btnMenuNote:
                startActivity(new Intent(mContext,NoteMotherActivity.class));
                break;
            case R.id.btnNoti:
                startActivity(new Intent(mContext,NotiActivity.class));
                break;
            case R.id.btnProfile:
                startActivity(new Intent(mContext,ProfileActivity.class));
                break;
        }
    }
}
