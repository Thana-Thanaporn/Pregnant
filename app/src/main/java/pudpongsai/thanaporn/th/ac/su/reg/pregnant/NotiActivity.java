package pudpongsai.thanaporn.th.ac.su.reg.pregnant;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.CalendarMenuActivity.CalendarActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.HomeMenuActivity.HomeActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NoteMenuActivity.NoteMotherActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity.ProfileActivity;

public class NotiActivity extends AppCompatActivity {

    Activity mContext = NotiActivity.this;
    TextView notiCount;
    ListView listNoti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_noti);
        notiCount = (TextView) findViewById(R.id.notiCount);
        notiCount.setVisibility(View.GONE);
        listNoti =(ListView) findViewById(R.id.listNoti);
        PregnantUitli.checkNoti(notiCount,listNoti , mContext);
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
