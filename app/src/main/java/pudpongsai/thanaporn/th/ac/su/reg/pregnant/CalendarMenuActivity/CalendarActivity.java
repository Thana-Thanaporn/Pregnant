package pudpongsai.thanaporn.th.ac.su.reg.pregnant.CalendarMenuActivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.HomeMenuActivity.HomeActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NoteMenuActivity.NoteMotherActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NotiActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity.ProfileActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class CalendarActivity extends AppCompatActivity {

    Activity mContext = CalendarActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
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
