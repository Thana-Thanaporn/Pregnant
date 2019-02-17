package pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Adapters.TelAdapter;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.TelDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NoteMenuActivity.NoteMotherActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NoteMenuActivity.NotePregnantActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.PregnantUitli;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class TelActivity extends AppCompatActivity {

    public Context mcontext = TelActivity.this;

    LinearLayout layoutMain;
    ListView listTel;

    ArrayList<TelDetail> arrTel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_tel);

        layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
        layoutMain.getForeground().setAlpha( 0);
        listTel = (ListView) findViewById(R.id.listTel);

        DatabaseReference TelReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"
                        + UserDetail.username + "/tels");

        TelReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                arrTel.clear();

                for(DataSnapshot ds : snapshot.getChildren()) {
                    arrTel.add(new TelDetail(
                            ds.getKey(),
                            ds.child("tel").getValue().toString())
                    );
                }

                TelAdapter telAdapter = new TelAdapter(arrTel,mcontext,layoutMain);
                listTel.setAdapter(telAdapter);
                telAdapter.notifyDataSetChanged();
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
    public  void onClickNewTel(View v) {
        PregnantUitli pregnantUitli = new PregnantUitli();
        pregnantUitli.popupTel(layoutMain, mcontext, new TelDetail("", ""));
    }
}
