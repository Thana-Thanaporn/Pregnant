package pudpongsai.thanaporn.th.ac.su.reg.pregnant;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.NoteDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;

public class NotePregnantActivity extends AppCompatActivity {

    TextView txtWeek, txtCountKick;
    EditText edtNote, edtWeightNote;
    Button btnDisKick, btnPlusKick, btnSaveNewNote;
    ImageView imgAddNote1,imgAddNote2;
    PopupWindow popupWeight;
    String weightNote, note,countKickStr;

    LinearLayout layoutMain;

    int countKick ;
    boolean [] checkImg = {false,false,false,false};

    Bitmap bitmapImg1 , bitmapImg2;


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

        countKick = 0;

        txtWeek = (TextView)findViewById(R.id.txtWeek);
        txtCountKick = (TextView)findViewById(R.id.txtCountKick);
        edtNote = (EditText) findViewById(R.id.edtNote);
        edtWeightNote = (EditText) findViewById(R.id.edtWeightNote);
        btnDisKick = (Button) findViewById(R.id.btnDisKick);
        btnPlusKick = (Button) findViewById(R.id.btnPlusKick);
        btnSaveNewNote = (Button) findViewById(R.id.btnSaveNewNote);

        imgAddNote1 = (ImageView) findViewById(R.id.imgAddNote1);
        imgAddNote2 = (ImageView) findViewById(R.id.imgAddNote2);

        txtWeek.setText("สัปดาห์ที่ " + UserDetail.weekPregnant + " วันที่ " + UserDetail.dayPregnant);
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
                        +UserDetail.weekPregnant+"/"+UserDetail.dayPregnant);
        DatabaseReference weightNoteReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+UserDetail.username);

        Map<String, String> map = new HashMap<String, String>();
        map.put("date", date);
        map.put("weight", weightNote);
        map.put("note", note);
        map.put("kick", countKickStr);
        map.put("pic1", "");
        map.put("pic2", "");
        map.put("fav", fav);


        weightNoteReference.child("weight").child(UserDetail.weekPregnant).child(UserDetail.dayPregnant).setValue(weightNote);


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
            }
        });
        btnFavNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFavNo.setBackgroundResource(R.drawable.box_radius_btn_actcive);
                thxNote();
                SavetoFirebase("no");
            }
        });


        int widthDevice = getWindowManager().getDefaultDisplay().getWidth();
        int heightDevice = getWindowManager().getDefaultDisplay().getHeight();
        int margin = (int) (widthDevice*0.8);
        int height = (int) (heightDevice*0.25);

        popupWeight = new PopupWindow(popupFavView,margin,height,true);
        popupWeight.setOutsideTouchable(true);
        popupWeight.showAtLocation(popupFavView,Gravity.CENTER,0,0);

        popupWeight.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                thxNote();
                SavetoFirebase("no");
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
        popup.setOutsideTouchable(true);
        popup.showAtLocation(popupWeightView,Gravity.CENTER,0,0);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                startActivity(new Intent(NotePregnantActivity.this , NoteMotherActivity.class));
            }
        });

        new Handler().postDelayed(new Runnable(){
            public void run() {
                startActivity(new Intent(NotePregnantActivity.this , NoteMotherActivity.class));

            }
        }, 3 *1000);
    }

    public String saveImg( DatabaseReference PregnantNoteReference ,Bitmap bitmapSelect,int num){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapSelect.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataPic = baos.toByteArray();

        String id = UUID.randomUUID().toString();

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://pregnantmother-e8d1f.appspot.com");
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/"+UserDetail.username+"/notes/week_"+
                UserDetail.weekPregnant+"day_"+UserDetail.dayPregnant + "_"+id+".jpg");
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
}
