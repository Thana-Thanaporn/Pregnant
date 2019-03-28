package pudpongsai.thanaporn.th.ac.su.reg.pregnant.HomeMenuActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Map;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.CalendarMenuActivity.CalendarActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.EventDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NoteMenuActivity.NoteMotherActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NotiActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.PregnantUitli;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity.ProfileActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class HomeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    Context mContext = HomeActivity.this;
    RelativeLayout layoutMain;

    public static final String API_KEY = "AIzaSyCdDs-rDdEhiiCYIhpnpRSxPOHS53Zh_P0";
    String VIDEO_ID = "";
    int widthDevice ;
    TextView weekChild,longChild,weightChild,detailChild;
    LinearLayout foodChild,recommendChild,exerciseChild;

    TextView notiCount;

    FloatingActionButton home_action, calen_action, note_action, noti_action, pro_action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        UserDetail.isLogout = false;

        notiCount = (TextView) findViewById(R.id.notiCount);
        notiCount.setVisibility(View.GONE);
        widthDevice = getWindowManager().getDefaultDisplay().getWidth();
        foodChild = (LinearLayout) findViewById(R.id.foodChild);
        recommendChild = (LinearLayout) findViewById(R.id.recommendChild);
        exerciseChild = (LinearLayout) findViewById(R.id.exerciseChild);

        weekChild = (TextView) findViewById(R.id.weekChild);
//        longChild = (TextView) findViewById(R.id.longChild);
//        weightChild = (TextView) findViewById(R.id.weightChild);
//        detailChild = (TextView) findViewById(R.id.detailChild);
        layoutMain = (RelativeLayout) findViewById(R.id.layoutMain);
        layoutMain.getForeground().setAlpha( 0);

        weekChild.setText("สัปดาห์ที่ " + UserDetail.weekPregnant);




        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{android.Manifest.permission.CALL_PHONE},1);
        }

        PregnantUitli.checkNoti(notiCount,null,mContext);
        DatabaseReference  referenceWeek = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/pregnantDetails/"
                        +UserDetail.oldPregnant+"/"+UserDetail.weekPregnant);

        referenceWeek.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, String> map = (Map)snapshot.getValue();

                VIDEO_ID = map.get("video").toString();
                YouTubePlayerView youTubePlayerView = findViewById(R.id.video);
                youTubePlayerView.initialize(API_KEY, HomeActivity.this);

//                longChild.setText("ความยาว : " + map.get("long").toString() +" ซม. ");
//                weightChild.setText("น้ำหนัก : " + map.get("weight").toString() +" กรัม ");
//                detailChild.setText( map.get("detail").toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
        referenceWeek.child("foods").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, String> map = (Map)ds.getValue();
                    createImgFood(
                            map.get("name").toString(),
                            map.get("pic").toString()
                    );
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
        referenceWeek.child("exercises").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, String> map = (Map)ds.getValue();
                    createImgExercises(
                            map.get("name").toString(),
                            map.get("main").toString(),
                            ds.getKey()
                    );
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
//        referenceWeek.child("recommends").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                for(DataSnapshot ds : snapshot.getChildren()) {
//                    int no = Integer.parseInt(ds.getKey())+1;
//                    TextView txtDetail = new TextView(mContext);
//                    RelativeLayout.LayoutParams txtDetailparams = new RelativeLayout.LayoutParams(
//                            RelativeLayout.LayoutParams.MATCH_PARENT,
//                            RelativeLayout.LayoutParams.WRAP_CONTENT
//                    );
//                    Typeface type = ResourcesCompat.getFont(mContext, R.font.kanit_light);
//                    txtDetail.setTypeface(type);
//                    txtDetail.setText(no + ". "+ds.getValue().toString());
//                    txtDetail.setLayoutParams(txtDetailparams);
//
//                    recommendChild.addView(txtDetail);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getMessage());
//            }
//        });


    }
    private void createImgExercises(String name , String img , final String key) {
        CardView cardView = new CardView(this);
        CardView.LayoutParams params = new CardView.LayoutParams(
                CardView.LayoutParams.WRAP_CONTENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        int cardMargin = (int) (widthDevice * 0.028);
        params.setMargins(cardMargin, cardMargin, cardMargin, cardMargin);
        cardView.setLayoutParams(params);

        LinearLayout boder = new LinearLayout(this);
        LinearLayout.LayoutParams boderparams = new LinearLayout.LayoutParams(
                (int) (widthDevice * 0.5),
                (int) (widthDevice * 0.4)
        );
        boder.setLayoutParams(boderparams);
        boder.setBackgroundResource(R.drawable.box_radius_frame_pic);
        boder.setOrientation(LinearLayout.VERTICAL);

        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams roundparams = new LinearLayout.LayoutParams(
                (int) (widthDevice * 0.48),
                (int) (widthDevice * 0.33)
        );
        int roundMargin = (int) (widthDevice * 0.01);
        roundparams.setMargins( roundMargin, 0, roundMargin, 0);
        imageView.setLayoutParams(roundparams);
        Picasso.get()
                .load(img)
                .into(imageView);


        TextView txtDetail = new TextView(this);
        RelativeLayout.LayoutParams txtDetailparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        txtDetailparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        Typeface type = ResourcesCompat.getFont(this, R.font.kanit_light);
        txtDetail.setTypeface(type);
        txtDetail.setText(name);
        txtDetail.setGravity(Gravity.CENTER);
        txtDetail.setLayoutParams(txtDetailparams);

        boder.addView(imageView);
        boder.addView(txtDetail);
        cardView.addView(boder);
        exerciseChild.addView(cardView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetail.selectExercises = key;
                startActivity(new Intent(mContext,ExercisesActivity.class));
            }
        });

    }
    private void createImgFood(String name , String img) {
        CardView cardView = new CardView(this);
        CardView.LayoutParams params = new CardView.LayoutParams(
                CardView.LayoutParams.WRAP_CONTENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        int cardMargin = (int) (widthDevice * 0.028);
        params.setMargins(cardMargin, cardMargin, cardMargin, cardMargin);
        cardView.setLayoutParams(params);

        LinearLayout boder = new LinearLayout(this);
        LinearLayout.LayoutParams boderparams = new LinearLayout.LayoutParams(
                (int) (widthDevice * 0.38),
                (int) (widthDevice * 0.45)
        );
        boder.setLayoutParams(boderparams);
        boder.setBackgroundResource(R.drawable.box_radius_frame_pic);
        boder.setOrientation(LinearLayout.VERTICAL);

        RoundedImageView roundedImageView = new RoundedImageView(this);
        LinearLayout.LayoutParams roundparams = new LinearLayout.LayoutParams(
                (int) (widthDevice * 0.35),
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int roundMargin = (int) (widthDevice * 0.015);
        roundparams.setMargins( roundMargin, roundMargin, roundMargin, roundMargin);
        roundparams.gravity = Gravity.CENTER;
        roundedImageView.setLayoutParams(roundparams);
        roundedImageView.setBorderColor(getResources().getColor(R.color.colorBtnOld));
        roundedImageView.setBorderWidth(5);
        roundedImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        roundedImageView.setRadius(6);
        roundedImageView.setSquare(true);
        roundedImageView.setImageResource(R.drawable.kick);

        Picasso.get()
                .load(img)
                .into(roundedImageView);


        TextView txtDetail = new TextView(this);
        RelativeLayout.LayoutParams txtDetailparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        txtDetailparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        Typeface type = ResourcesCompat.getFont(this, R.font.kanit_light);
        txtDetail.setTypeface(type);
        txtDetail.setText(name);
        txtDetail.setGravity(Gravity.CENTER);
        txtDetail.setLayoutParams(txtDetailparams);

        boder.addView(roundedImageView);
        boder.addView(txtDetail);
        cardView.addView(boder);
        foodChild.addView(cardView);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Failured to Initialize!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        /** add listeners to YouTubePlayer instance **/
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);

        /** Start buffering **/
        if (!wasRestored) {
            player.cueVideo(VIDEO_ID);
        }
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onBuffering(boolean arg0) {
        }
        @Override
        public void onPaused() {
        }
        @Override
        public void onPlaying() {
        }
        @Override
        public void onSeekTo(int arg0) {
        }
        @Override
        public void onStopped() {
        }
    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onAdStarted() {
        }
        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }
        @Override
        public void onLoaded(String arg0) {
        }
        @Override
        public void onLoading() {
        }
        @Override
        public void onVideoEnded() {
        }
        @Override
        public void onVideoStarted() {
        }
    };


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

    public void onClickSos(View v){
        PregnantUitli.popupSOS(layoutMain,HomeActivity.this);
    }
}
