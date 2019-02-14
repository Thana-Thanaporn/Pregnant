package pudpongsai.thanaporn.th.ac.su.reg.pregnant;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.NoteDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;

public class MainActivity extends AppCompatActivity {

    Button btnWeight,btnWater,btnSaveNote;

    ImageButton btnFeel;

    EditText edtFeel,edtNote;

    TextView weekAndDay;

    NumberPicker npkWeight,npkSubWeight;

    ImageView btnAddImage , nowImg;

    Bitmap bitmapImg;

    RelativeLayout imgBox;

    int countImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED&&
//                ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA)
//                        != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            android.Manifest.permission.CAMERA},1);
//        }
//
//        countImg = 0;
//
//        weekAndDay = (TextView) findViewById(R.id.weekAndDay);
//
//        btnWeight = (Button)findViewById(R.id.btnWeight);
//        btnWater = (Button)findViewById(R.id.btnWater);
//        btnFeel = (ImageButton)findViewById(R.id.btnFeel);
//        btnSaveNote = (Button)findViewById(R.id.btnSaveNote);
//        btnAddImage = (ImageView)findViewById(R.id.btnAddImage);
//
//        edtFeel = (EditText)findViewById(R.id.edtFeel);
//        edtNote = (EditText)findViewById(R.id.edtNote);
//
//        imgBox = (RelativeLayout) findViewById(R.id.imgBox);
//
//        btnWater.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,WaterActivity.class));
//            }
//        });
//
//        btnWater.setText(""+((NoteDetail.totalWater == 0)?"การดื่มน้ำ":NoteDetail.totalWater));
//        btnWeight.setText(""+((NoteDetail.weight == 0 && NoteDetail.subWeight == 0)?"น้ำหนัก":NoteDetail.weight+"."+NoteDetail.subWeight));
//
//        btnAddImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nowImg = btnAddImage;
//               selectImgOrTakeImg();
//            }
//        });
//
//        weekAndDay.setText("สัปดาห์ที่ "+UserDetail.weekPregnant+" วันที่ "+UserDetail.dayPregnant);


    }

    private void selectImgOrTakeImg() {
        final int REQUEST_CAMERA = 0;
        final int SELECT_FILE = 1;

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
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

            bitmapImg = Bitmap.createScaledBitmap(bitmapImg, 130,130,true);
            nowImg.setImageBitmap(bitmapImg);
            nowImg.setScaleType(ImageView.ScaleType.FIT_XY);
            countImg += 1;

            if(countImg < 4) {
                createImgView();
            }

        }


    }

    private void createImgView() {
        final ImageView imageView = new ImageView(MainActivity.this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                400,400
        );

        if(countImg % 2 == 1){
            layoutParams.setMargins(180*3,600 * (countImg/2),0,0);
        }else if(countImg % 2 == 0){
            layoutParams.setMargins(0,600 * (countImg/2),0,0);
        }


        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.drawable.img_profile);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowImg = imageView;
                selectImgOrTakeImg();
            }
        });
        imgBox.addView(imageView);

    }

//
//    public void OnclickWeight(View view){
//        LayoutInflater weightInflater = (LayoutInflater)
//                getSystemService(LAYOUT_INFLATER_SERVICE);
//        View popupWeightView = weightInflater.inflate(R.layout.popup_weight,null);
//
//        npkWeight = popupWeightView.findViewById(R.id.npkWeight);
//
//        npkWeight.setMinValue(40);
//        npkWeight.setMaxValue(100);
//
//        npkSubWeight = popupWeightView.findViewById(R.id.npkSubWeight);
//
//        npkSubWeight.setMinValue(00);
//        npkSubWeight.setMaxValue(90);
//        npkWeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                NoteDetail.weight = newVal;
//                btnWeight.setText(""+((NoteDetail.subWeight == 0)?NoteDetail.weight+". 00":NoteDetail.weight+"."+NoteDetail.subWeight));
//
//            }
//        });
//
//        npkSubWeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                NoteDetail.subWeight = newVal;
//                btnWeight.setText(""+((NoteDetail.weight == 0)?"00."+NoteDetail.subWeight:NoteDetail.weight+"."+NoteDetail.subWeight));
//
//
//            }
//        });
//
//
//        final PopupWindow popupWeight = new PopupWindow(popupWeightView,900,1000,true);
//
//        popupWeight.showAtLocation(popupWeightView,Gravity.CENTER,0,0);
//
//        popupWeightView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                popupWeight.dismiss();
//                return true;
//            }
//        });
//    }
//
//    public void OnclickEmotion(View view){
//        LayoutInflater emotionInflater = (LayoutInflater)
//                getSystemService(LAYOUT_INFLATER_SERVICE);
//        View popupEmotionView = emotionInflater.inflate(R.layout.popup_emotion,null);
//
//        final PopupWindow popupEmotion = new PopupWindow(popupEmotionView,900,1000,true);
//
//        popupEmotion.showAtLocation(popupEmotionView,Gravity.CENTER,0,0);
//
//        popupEmotionView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                popupEmotion.dismiss();
//                return true;
//            }
//        });
//
//        ImageButton btnFeel1 = popupEmotionView.findViewById(R.id.btnFeel1);
//        btnFeel1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NoteDetail.emotion = "อารมณ์ดี";
//                NoteDetail.emotionIcon = R.drawable.btn_feel;
//                btnFeel.setImageResource(NoteDetail.emotionIcon);
//                popupEmotion.dismiss();
//            }
//        });
//
//        ImageButton btnFeel2 = popupEmotionView.findViewById(R.id.btnFeel2);
//        btnFeel2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NoteDetail.emotion = "อารมณ์ดีมาก";
//                NoteDetail.emotionIcon = R.drawable.btn_feel2;
//                btnFeel.setImageResource(NoteDetail.emotionIcon);
//                popupEmotion.dismiss();
//            }
//        });
//
//        ImageButton btnFeel3 = popupEmotionView.findViewById(R.id.btnFeel3);
//        btnFeel3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NoteDetail.emotion = "เฉยๆ";
//                NoteDetail.emotionIcon = R.drawable.btn_feel3;
//                btnFeel.setImageResource(NoteDetail.emotionIcon);
//                popupEmotion.dismiss();
//            }
//        });
//
//        ImageButton btnFeel4 = popupEmotionView.findViewById(R.id.btnFeel4);
//        btnFeel4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NoteDetail.emotion = "โมโห";
//                NoteDetail.emotionIcon = R.drawable.btn_feel4;
//                btnFeel.setImageResource(NoteDetail.emotionIcon);
//                popupEmotion.dismiss();
//            }
//        });
//
//        ImageButton btnFeel5 = popupEmotionView.findViewById(R.id.btnFeel5);
//        btnFeel5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NoteDetail.emotion = "หม่นหมอง";
//                NoteDetail.emotionIcon = R.drawable.btn_feel5;
//                btnFeel.setImageResource(NoteDetail.emotionIcon);
//                popupEmotion.dismiss();
//            }
//        });
//
//        ImageButton btnFeel6 = popupEmotionView.findViewById(R.id.btnFeel6);
//        btnFeel6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NoteDetail.emotion = "อารมณืไม่ดี";
//                NoteDetail.emotionIcon = R.drawable.btn_feel6;
//                btnFeel.setImageResource(NoteDetail.emotionIcon);
//                popupEmotion.dismiss();
//            }
//        });
//
//
//    }

}
