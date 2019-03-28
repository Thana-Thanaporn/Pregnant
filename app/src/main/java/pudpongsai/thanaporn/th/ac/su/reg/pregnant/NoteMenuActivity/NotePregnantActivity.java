package pudpongsai.thanaporn.th.ac.su.reg.pregnant.NoteMenuActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class NotePregnantActivity extends AppCompatActivity {

    TextView txtWeek, txtCountKick;
    EditText edtNote, edtWeightNote;
    Button btnDisKick, btnPlusKick, btnSaveNewNote;
    ImageView imgAddNote1,imgAddNote2;
    PopupWindow popupEditNote;
    String weightNote, note,countKickStr;

    LinearLayout layoutMain;

    int countKick ;
    boolean [] checkImg = {false,false,false,false};

    Bitmap bitmapImg1 , bitmapImg2;

    String week , day;
    String imgPath1, imgPath2;
    boolean checkClickFav,checkThxNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_note_pregnant);


        if (ContextCompat.checkSelfPermission(NotePregnantActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(NotePregnantActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(NotePregnantActivity.this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NotePregnantActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA},1);
        }


        layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
        layoutMain.getForeground().setAlpha( 0);
        checkClickFav = false;
        checkThxNote= false;
        countKick = 0;
        imgPath1 = "";
        imgPath2 = "";

        txtWeek = (TextView)findViewById(R.id.txtWeek);
        txtCountKick = (TextView)findViewById(R.id.txtCountKick);
        edtNote = (EditText) findViewById(R.id.edtNote);
        edtWeightNote = (EditText) findViewById(R.id.edtWeightNote);
        btnDisKick = (Button) findViewById(R.id.btnDisKick);
        btnPlusKick = (Button) findViewById(R.id.btnPlusKick);
        btnSaveNewNote = (Button) findViewById(R.id.btnSaveNewNote);

        imgAddNote1 = (ImageView) findViewById(R.id.imgAddNote1);
        imgAddNote2 = (ImageView) findViewById(R.id.imgAddNote2);


        if (UserDetail.selecNote[0].equals("")){
            txtWeek.setText("สัปดาห์ที่ " + UserDetail.weekPregnant + " วันที่ " + UserDetail.dayPregnant);
            week = UserDetail.weekPregnant;
            day = UserDetail.dayPregnant;
        }else {
            txtWeek.setText("สัปดาห์ที่ " + UserDetail.selecNote[0] + " วันที่ " + UserDetail.selecNote[1]);
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

                            edtNote.setText(obj.getString("note").toString());
                            edtWeightNote.setText(obj.getString("weight").toString());
                            countKick = Integer.parseInt(obj.getString("kick").toString());
                            txtCountKick.setText(""+countKick);
                            imgPath1 = obj.getString("pic1").toString();
                            imgPath2 = obj.getString("pic2").toString();

                            StorageReference storageReference = FirebaseStorage.getInstance("gs://pregnantmother-e8d1f.appspot.com").getReference();

                            if (!imgPath1.equals("")){
                                StorageReference referenceImg1 = storageReference.child(imgPath1.substring(1,imgPath1.length()));
                                Glide.with(NotePregnantActivity.this)
                                        .using(new FirebaseImageLoader())
                                        .load(referenceImg1)
                                        .into(imgAddNote1);
                            }
                            if (!imgPath2.equals("")){
                                StorageReference referenceImg2 = storageReference.child(imgPath2.substring(1,imgPath2.length()));
                                Glide.with(NotePregnantActivity.this)
                                        .using(new FirebaseImageLoader())
                                        .load(referenceImg2)
                                        .into(imgAddNote2);
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

            RequestQueue rQueue = Volley.newRequestQueue(NotePregnantActivity.this);
            rQueue.add(request);
        }


        txtCountKick.setText("0");


        imgAddNote1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkImg[0] = true;
                selectImgOrTakeImg();
            }
        });

        imgAddNote2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkImg[1] = true;
                selectImgOrTakeImg();
            }
        });

        btnSaveNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightNote = edtWeightNote.getText().toString();
                if (weightNote.equals("")){
                    edtWeightNote.setError("กรุณาเรอกน้ำหนัก");
                }else {
                    OnclickFav();
                }

            }
        });


    }

    public  void SavetoFirebase(String fav){

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String date = df.format(Calendar.getInstance().getTime());


        note = edtNote.getText().toString();
        countKickStr = txtCountKick.getText().toString();

        DatabaseReference PregnantNoteReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+UserDetail.username+"/notes/"
                        +week+"/"+day);
        DatabaseReference weightNoteReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+UserDetail.username);

        Map<String, String> map = new HashMap<String, String>();
        map.put("date", date);
        map.put("weight", weightNote);
        map.put("note", note);
        map.put("kick", countKickStr);
        map.put("pic1", (imgPath1.equals("")?"":imgPath1));
        map.put("pic2", (imgPath2.equals("")?"":imgPath2));
        map.put("fav", fav);


        weightNoteReference.child("weight").child(week).child(day).setValue(weightNote);


            if (checkImg[2]){
                map.put("pic1", saveImg(PregnantNoteReference,bitmapImg1,1));
            }
            if (checkImg[3]){
                map.put("pic2", saveImg(PregnantNoteReference,bitmapImg2,2));
            }

        PregnantNoteReference.setValue(map);


    }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void OnclickFav(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                layoutMain.getForeground().setAlpha( 220);
            }

        LayoutInflater weightInflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupFavView = weightInflater.inflate(R.layout.popup_fav,null);

        final Button btnFavYes = popupFavView.findViewById(R.id.btnFavYes);
        final Button btnFavNo = popupFavView.findViewById(R.id.btnFavNo);
        btnFavYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFavYes.setBackgroundResource(R.drawable.box_radius_btn_actcive);
                thxNote();
                SavetoFirebase("yes");
                checkClickFav =true;

            }
        });
        btnFavNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFavNo.setBackgroundResource(R.drawable.box_radius_btn_actcive);
                thxNote();
                SavetoFirebase("no");
                checkClickFav =true;
            }
        });

        int widthDevice = getWindowManager().getDefaultDisplay().getWidth();
        int heightDevice = getWindowManager().getDefaultDisplay().getHeight();
        int margin = (int) (widthDevice*0.8);
        int height = (int) (heightDevice*0.25);

        popupEditNote = new PopupWindow(popupFavView,margin,height,true);
        popupEditNote.setOutsideTouchable(true);
        popupEditNote.showAtLocation(popupFavView,Gravity.CENTER,0,0);

        popupEditNote.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!checkClickFav){
                    thxNote();
                    SavetoFirebase("no");
                }

            }
        });


    }

    public void thxNote(){
        LayoutInflater weightInflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupWeightView = weightInflater.inflate(R.layout.popup_thx_note,null);

        int widthDevice = getWindowManager().getDefaultDisplay().getWidth();
        int heightDevice = getWindowManager().getDefaultDisplay().getHeight();
        int margin = (int) (widthDevice*0.8);
        int height = (int) (heightDevice*0.35);

        final PopupWindow popup = new PopupWindow(popupWeightView,margin,height,true);
        popup.showAtLocation(popupWeightView,Gravity.CENTER,0,0);
        popup.setOutsideTouchable(true);
        new Handler().postDelayed(new Runnable(){
            public void run() {
                if (!checkThxNote){
                    startActivity(new Intent(NotePregnantActivity.this , NoteMotherActivity.class));
                }

            }
        }, 2 *1000);

        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                checkThxNote = true;
                popupEditNote.dismiss();
                startActivity(new Intent(NotePregnantActivity.this , NoteMotherActivity.class));
            }
        });
    }

    public String saveImg( DatabaseReference PregnantNoteReference ,Bitmap bitmapSelect,int num){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapSelect.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataPic = baos.toByteArray();

        String id = UUID.randomUUID().toString();

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://pregnantmother-e8d1f.appspot.com");
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/"+UserDetail.username+"/notes/week_"+
                week+"day_"+day + "_"+id+".jpg");
        UploadTask uploadTask = imagesRef.putBytes(dataPic);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

       return imagesRef.getPath();

    }

    private void selectImgOrTakeImg() {
        final int REQUEST_CAMERA = 0;
        final int SELECT_FILE = 1;

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder( NotePregnantActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmapImg = null;

        if(resultCode == RESULT_OK && null != data){
            switch(requestCode) {
                case 0:
                    Bundle extras = data.getExtras();
                    bitmapImg = (Bitmap) extras.get("data");
                    break;
                case 1:
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    bitmapImg = BitmapFactory.decodeFile(picturePath);
                    break;

            }

            if (checkImg[0]){
                bitmapImg1 = Bitmap.createScaledBitmap(bitmapImg, 130,130,true);
                imgAddNote1.setImageBitmap(bitmapImg);
                imgAddNote1.setScaleType(ImageView.ScaleType.FIT_XY);
                checkImg[0] = false;
                checkImg[2] = true;
            }

            if (checkImg[1]){
                bitmapImg2 = Bitmap.createScaledBitmap(bitmapImg, 130,130,true);
                imgAddNote2.setImageBitmap(bitmapImg);
                imgAddNote2.setScaleType(ImageView.ScaleType.FIT_XY);
                checkImg[1] = false;
                checkImg[3] = true;
            }



        }


    }


    public void OnclickCompute(View view){
        Button btnMain = (Button)view;

        switch (btnMain.getId()){
            case R.id.btnDisKick:
                countKick = countKick-1;
                countKick =((countKick < 0)? 0 : countKick);
                txtCountKick.setText(""+countKick);
                break;

            case R.id.btnPlusKick:
                countKick = countKick+1;
                txtCountKick.setText(""+countKick);
                break;

        }

    }
    public void onClickBack(View v){
        onBackPressed();
    }
}
