package pudpongsai.thanaporn.th.ac.su.reg.pregnant.NoteMenuActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.CalendarMenuActivity.CalendarActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Adapters.weekAdapter;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.HomeMenuActivity.HomeActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NotiActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.PregnantUitli;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity.ProfileActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class NoteMotherActivity extends AppCompatActivity {

    Activity mContext = NoteMotherActivity.this;

    RelativeLayout boxPic,favPic;
    int widthDevice ;
    String selectWeek = "";
    int countPicBox,countPicFav;
    Spinner spiSelectWeek;
    int indexNow[] ={0,0};

    RelativeLayout layoutMain;
    TextView notiCount;

    ArrayList<String> week = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_note_mother);

        notiCount = (TextView) findViewById(R.id.notiCount);
        notiCount.setVisibility(View.GONE);
        PregnantUitli.checkNoti(notiCount,null,mContext);

        widthDevice = getWindowManager().getDefaultDisplay().getWidth();
        countPicBox = 0;
        countPicFav =0 ;
        boxPic = (RelativeLayout)findViewById(R.id.boxPic);
        favPic = (RelativeLayout)findViewById(R.id.favPic);
        spiSelectWeek = (Spinner) findViewById(R.id.spiSelectWeek);
        layoutMain = (RelativeLayout) findViewById(R.id.layoutMain);
        layoutMain.getForeground().setAlpha( 0);


        DatabaseReference  referenceWeek = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+UserDetail.username+"/notes");

        referenceWeek.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()) {
                    week.add("สัปดาห์ที่ "+ ds.getKey());
                }
                weekAdapter weekAdt = new weekAdapter(NoteMotherActivity.this, week);
                spiSelectWeek.setAdapter(weekAdt);
                weekAdt.notifyDataSetChanged();
                int index = week.indexOf("สัปดาห์ที่ "+ UserDetail.weekPregnant);

                if (index == -1){
                    spiSelectWeek.setSelection(week.size()-1);
                }else {
                    spiSelectWeek.setSelection(index);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });


        spiSelectWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                String week = spiSelectWeek.getSelectedItem().toString();
                selectWeek = week.substring(11,week.length());
                getData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //optionally do something here
            }
        });



    }

    public void  getData(){


        DatabaseReference  reference1 = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+UserDetail.username+"/notes/"+selectWeek);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                countPicBox = 0;
                countPicFav =0 ;
                boxPic.removeAllViews();
                favPic.removeAllViews();

                for(DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, String> map = (Map) ds.getValue();
                    SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
                    Date newDate = null;
                    String date = "";
                    try {
                        newDate = format.parse(map.get("date").toString());
                        format = new SimpleDateFormat("dd/mm/yyyy");
                        date = format.format(newDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String imgPath = map.get("pic1").toString();
                    String fav = map.get("fav").toString();
                    String weekdetail[] = {selectWeek, ds.getKey()};
                    imgPath = (imgPath.equals("") ? "" : imgPath.substring(1, imgPath.length()));
                    if (fav.equals("yes")) {
                        createImage(weekdetail, imgPath, date, favPic, "fav");
                    } else {
                        createImage(weekdetail, imgPath, date, boxPic, "box");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void OnclickEditNote(final String[] key){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layoutMain.getForeground().setAlpha( 220);
        }

        LayoutInflater EditNoteInflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupEditNoteView = EditNoteInflater.inflate(R.layout.popup_edit_del_note,null);

        final Button btnNoteEdit = popupEditNoteView.findViewById(R.id.btnNoteEdit);
        final Button btnNoteDel = popupEditNoteView.findViewById(R.id.btnNoteDel);

        int widthDevice = getWindowManager().getDefaultDisplay().getWidth();
        int heightDevice = getWindowManager().getDefaultDisplay().getHeight();
        int wieght = (int) (widthDevice*0.95);
        int height = (int) (heightDevice*0.18);

        final PopupWindow popupEditNote = new PopupWindow(popupEditNoteView,wieght,height,true);
        popupEditNote.setOutsideTouchable(true);
        popupEditNote.showAtLocation(popupEditNoteView,Gravity.CENTER,0,0);

        btnNoteEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNoteEdit.setBackgroundResource(R.drawable.box_radius_btn_actcive);
                popupEditNote.dismiss();
                UserDetail.selecNote = key;
                startActivity(new Intent(NoteMotherActivity.this,NotePregnantActivity.class));
            }
        });
        btnNoteDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNoteDel.setBackgroundResource(R.drawable.box_radius_btn_actcive);
                popupEditNote.dismiss();
                DatabaseReference  referenceDel = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+UserDetail.username
                                +"/notes/"+key[0]);
                referenceDel.child(key[1]).setValue(null);
                delNote();

            }
        });
        popupEditNote.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutMain.getForeground().setAlpha( 0);
            }
        });



    }
    public void delNote(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layoutMain.getForeground().setAlpha( 220);
        }
        LayoutInflater weightInflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupDelView = weightInflater.inflate(R.layout.popup_thx_note,null);

        int widthDevice = getWindowManager().getDefaultDisplay().getWidth();
        int heightDevice = getWindowManager().getDefaultDisplay().getHeight();
        int margin = (int) (widthDevice*0.8);
        int height = (int) (heightDevice*0.35);
        final TextView txtDetail = popupDelView.findViewById(R.id.txtDetail);
        txtDetail.setText("ลบบันทึกเรียบร้อยแล้ว");

        final PopupWindow popup = new PopupWindow(popupDelView,margin,height,true);
        popup.setOutsideTouchable(true);
        popup.showAtLocation(popupDelView,Gravity.CENTER,0,0);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutMain.getForeground().setAlpha( 0);
            }
        });

        new Handler().postDelayed(new Runnable(){
            public void run() {
                layoutMain.getForeground().setAlpha( 0);
                popup.dismiss();

            }
        }, 3 *1000);
    }
    public  void createImage(final String key[] , String imgPath , String note , RelativeLayout layout , String status){
        int countpic =0;
        switch (status){
            case "fav":
                countpic = countPicFav ;
                break;
            default:
                countpic = countPicBox ;
                break;
        }

        CardView cardView = new CardView(this);
        CardView.LayoutParams params = new CardView.LayoutParams(
                CardView.LayoutParams.WRAP_CONTENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        int cardMargin = (int) (widthDevice * 0.028);
        int cardSizewidth = (int) (widthDevice * 0.45) + (cardMargin *2);
        int cardSizehiegh = (int) (widthDevice * 0.51) + (cardMargin *2);
        params.setMargins(cardMargin+((countpic%2 == 1)? cardSizewidth: 0), cardMargin+((countpic/2 > 0)? (countpic/2 *(cardSizehiegh )): 0)
                , cardMargin, cardMargin);
        cardView.setLayoutParams(params);

        LinearLayout boder = new LinearLayout(this);
        LinearLayout.LayoutParams boderparams = new LinearLayout.LayoutParams(
                (int) (widthDevice * 0.45),
                (int) (widthDevice * 0.51)
        );
        boder.setLayoutParams(boderparams);
        boder.setBackgroundResource(R.drawable.box_radius_frame_pic);
        boder.setOrientation(LinearLayout.VERTICAL);

        RoundedImageView roundedImageView = new RoundedImageView(this);
        LinearLayout.LayoutParams roundparams = new LinearLayout.LayoutParams(
                (int) (widthDevice * 0.41),
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int roundMargin = (int) (widthDevice * 0.02);
        roundparams.setMargins( roundMargin, roundMargin, roundMargin, roundMargin);
        roundparams.gravity = Gravity.CENTER;
        roundedImageView.setLayoutParams(roundparams);
        roundedImageView.setBorderColor(getResources().getColor(R.color.colorAccent));
        roundedImageView.setBorderWidth(5);
        roundedImageView.setRadius(6);
        roundedImageView.setSquare(true);

        if (imgPath.equals("")){
            roundedImageView.setBackgroundResource(R.drawable.kick);
        }else {
            StorageReference storageReference = FirebaseStorage.getInstance("gs://pregnantmother-e8d1f.appspot.com").getReference()
                    .child(imgPath);

            Glide.with(NoteMotherActivity.this)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(roundedImageView);
        }

        roundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetail.selecNote = key;
                startActivity(new Intent(NoteMotherActivity.this,NoteActivity.class));
            }
        });

        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams relativeLayoutparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        relativeLayout.setLayoutParams(relativeLayoutparams);

        TextView txtDetail = new TextView(this);
        RelativeLayout.LayoutParams txtDetailparams = new RelativeLayout.LayoutParams(
                (int) (widthDevice * 0.45),
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        txtDetail.setText(note);
        txtDetailparams.setMargins( (int) (widthDevice * 0.02), 0, 0, 0);
        txtDetail.setLayoutParams(txtDetailparams);

        ImageView setting = new ImageView(this);
        RelativeLayout.LayoutParams settingparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        settingparams.setMargins( 0, 0, (int) (widthDevice * 0.04), 0);
        settingparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        setting.setImageResource(R.drawable.settingnote);
        setting.setLayoutParams(settingparams);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnclickEditNote(key);
            }
        });

        relativeLayout.addView(txtDetail);
        relativeLayout.addView(setting);
        boder.addView(roundedImageView);
        boder.addView(relativeLayout);
        cardView.addView(boder);
        layout.addView(cardView);

        switch (status){
            case "fav":
                countPicFav += 1;
                break;
            default:
                countPicBox += 1;
                break;
        }
    }

    public  void onClickNewNote(View v){
        startActivity(new Intent(NoteMotherActivity.this,NotePregnantActivity.class));
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
