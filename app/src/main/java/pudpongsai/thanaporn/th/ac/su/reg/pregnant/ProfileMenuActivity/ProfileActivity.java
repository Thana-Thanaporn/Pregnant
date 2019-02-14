package pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.CalendarMenuActivity.CalendarActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.HomeMenuActivity.HomeActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NoteMenuActivity.NoteMotherActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NotiActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.PregnantUitli;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class ProfileActivity extends AppCompatActivity {

    Activity mContext = ProfileActivity.this;

    TextView txtUsername,txtName,txtEmail,txtOldPregnant;
    RoundedImageView picProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtOldPregnant = (TextView) findViewById(R.id.txtOldPregnant);

        picProfile = (RoundedImageView) findViewById(R.id.picProfile);


        DatabaseReference referenceProfile = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+ UserDetail.username+"/profile");

        referenceProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Map<String, String> map = (Map) snapshot.getValue();

                txtUsername.setText(UserDetail.username);
                txtName.setText(map.get("firstname").toString());
                txtEmail.setText(map.get("email").toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        referenceProfile.child("pregnant").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                PregnantUitli checkPregnant = new PregnantUitli();
                checkPregnant.checkNowPregnant(
                        snapshot.child("time").getValue().toString(),
                        snapshot.child("week").getValue().toString(),
                        snapshot.child("day").getValue().toString());
                txtOldPregnant.setText(UserDetail.weekPregnant + " สัปดาห์ " + UserDetail.dayPregnant + " วัน");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

    }
    public void onClickProfileMenu(View v){

        switch (v.getId()){
            case R.id.btnGraph:
                startActivity(new Intent(mContext,GraphActivity.class));
                break;
            case R.id.btnAllNote:
                break;
            case R.id.btnTel:
                break;

        }
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
