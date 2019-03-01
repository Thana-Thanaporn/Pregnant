package pudpongsai.thanaporn.th.ac.su.reg.pregnant.NoteMenuActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class NoteActivity extends AppCompatActivity {

    TextView weekAndDay,txtNote,txtWeight,txtKick;
    RoundedImageView pic1,pic2;
    String week , day , imgPath1, imgPath2;
    LinearLayout layoutMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_note);

        layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
        layoutMain.getForeground().setAlpha( 0);

        weekAndDay = (TextView) findViewById(R.id.weekAndDay);
        txtNote = (TextView) findViewById(R.id.txtNote);
        txtWeight = (TextView) findViewById(R.id.txtWeight);
        txtKick = (TextView) findViewById(R.id.txtKick);

        pic1 = (RoundedImageView) findViewById(R.id.pic1);
        pic2 = (RoundedImageView) findViewById(R.id.pic2);

        weekAndDay.setText("สัปดาห์ที่ " + UserDetail.selecNote[0] + " วันที่ " + UserDetail.selecNote[1]);
        week = UserDetail.selecNote[0];
        day = UserDetail.selecNote[1];
        UserDetail.selecNote = new String[]{"",""};


        String url = "https://pregnantmother-e8d1f.firebaseio.com/users/"+UserDetail.username
                +"/notes/"+week+"/"+day+".json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(!s.equals("null")) {
                    try {
                        JSONObject obj = new JSONObject(s);

                        txtNote.setText(obj.getString("note").toString());
                        txtWeight.setText(obj.getString("weight").toString() + " กิโลกรัม");
                        txtKick.setText(obj.getString("kick").toString() + " ครั้ง");
                        imgPath1 = obj.getString("pic1").toString();
                        imgPath2 = obj.getString("pic2").toString();

                        StorageReference storageReference = FirebaseStorage.getInstance("gs://pregnantmother-e8d1f.appspot.com").getReference();

                        if (!imgPath1.equals("")){
                            StorageReference referenceImg1 = storageReference.child(imgPath1.substring(1,imgPath1.length()));
                            Glide.with(NoteActivity.this)
                                    .using(new FirebaseImageLoader())
                                    .load(referenceImg1)
                                    .into(pic1);
                        }
                        if (!imgPath2.equals("")){
                            StorageReference referenceImg2 = storageReference.child(imgPath2.substring(1,imgPath2.length()));
                            Glide.with(NoteActivity.this)
                                    .using(new FirebaseImageLoader())
                                    .load(referenceImg2)
                                    .into(pic2);
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

        RequestQueue rQueue = Volley.newRequestQueue(NoteActivity.this);
        rQueue.add(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void EditNote(){
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
        int margin = (int) (widthDevice*0.95);
        int height = (int) (heightDevice*0.18);

        final PopupWindow popupEditNote = new PopupWindow(popupEditNoteView,margin,height,true);
        popupEditNote.setOutsideTouchable(true);
        popupEditNote.showAtLocation(popupEditNoteView,Gravity.CENTER,0,0);

        btnNoteEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNoteEdit.setBackgroundResource(R.drawable.box_radius_btn_actcive);
                popupEditNote.dismiss();
                UserDetail.selecNote[0] = week;
                UserDetail.selecNote[1] = day;
                startActivity(new Intent(NoteActivity.this,NotePregnantActivity.class));
            }
        });
        btnNoteDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNoteDel.setBackgroundResource(R.drawable.box_radius_btn_actcive);
                popupEditNote.dismiss();
                DatabaseReference referenceDel = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+UserDetail.username
                                +"/notes/"+week);
                referenceDel.child(day).setValue(null);
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
        View popupWeightView = weightInflater.inflate(R.layout.popup_thx_note,null);

        int widthDevice = getWindowManager().getDefaultDisplay().getWidth();
        int heightDevice = getWindowManager().getDefaultDisplay().getHeight();
        int margin = (int) (widthDevice*0.8);
        int height = (int) (heightDevice*0.35);

        final PopupWindow popup = new PopupWindow(popupWeightView,margin,height,true);
        popup.setOutsideTouchable(true);
        popup.showAtLocation(popupWeightView, Gravity.CENTER,0,0);
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
                startActivity(new Intent(NoteActivity.this,NoteMotherActivity.class));

            }
        }, 3 *1000);
    }

    public void onClickBack(View v){
        onBackPressed();
    }
    public void onClickEdit(View v){
        EditNote();
    }
}
