package pudpongsai.thanaporn.th.ac.su.reg.pregnant.LoginMenuActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

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
import java.util.UUID;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NoteMenuActivity.NotePregnantActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class RegisterActivity extends AppCompatActivity {
    ImageView btnprofile;
    String deviceId;
    EditText edtUsername,edtEmail,edtPassword,edtName;
    String username,email,password,name,weight;
    Button btnRegis;
    boolean checkImg;
    Bitmap bitmapImg;
    LinearLayout layoutMain;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
        layoutMain.getForeground().setAlpha( 0);
        checkImg = false;

        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        btnprofile = (ImageView)findViewById(R.id.btnprofile);

        edtUsername = (EditText)findViewById(R.id.edtUsername);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        edtName = (EditText)findViewById(R.id.edtName);

        btnRegis = (Button) findViewById(R.id.btnRegis);

        btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImgOrTakeImg();
            }
        });

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFirebase();
            }
        });
        weight = "";
        if (UserDetail.profileMode.equals("edit")){
            getProfileData();
        }
    }

    public void getProfileData(){
        String url = "https://pregnantmother-e8d1f.firebaseio.com/users/"+UserDetail.username
                +"/profile.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(!s.equals("null")) {
                    try {
                        JSONObject obj = new JSONObject(s);

                        edtUsername.setText(UserDetail.username);
                        edtUsername.setFocusable(false);
                        edtEmail.setText(obj.getString("email").toString());
                        edtPassword.setText(obj.getString("password").toString());
                        edtName.setText(obj.getString("firstname").toString());
                        String imgPath = obj.getString("pic").toString();
                        weight = obj.getString("weight").toString();
                        StorageReference storageReference = FirebaseStorage.getInstance("gs://pregnantmother-e8d1f.appspot.com").getReference();

                            StorageReference referenceImg1 = storageReference.child(imgPath.substring(1,imgPath.length()));
                            Glide.with(RegisterActivity.this)
                                    .using(new FirebaseImageLoader())
                                    .load(referenceImg1)
                                    .into(btnprofile);

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

        RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
        rQueue.add(request);
    }

    private void saveToFirebase() {

        username = edtUsername.getText().toString();
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();
        name = edtName.getText().toString();
        if(!checkImg && !UserDetail.profileMode.equals("edit")){
            Toast.makeText(RegisterActivity.this,"กรุณาใส่รูปภาพ",Toast.LENGTH_LONG).show();
        }
        if(username.equals("")){
            edtUsername.setError("can't be blank");
        }
        else if(!username.matches("[A-Za-z0-9]+")){
            edtUsername.setError("only alphabet or number allowed");
        }
        else if(email.equals("")){
            edtEmail.setError("can't be blank");
        }
        else if(password.equals("")){
            edtPassword.setError("can't be blank");
        }
        else if(!password.matches("[A-Za-z0-9]+")){
            edtPassword.setError("only alphabet or number allowed");
        }
        else if(password.length()<8){
            edtPassword.setError("at least 8 characters long");
        }
        else if(name.equals("")){
            edtName.setError("can't be blank");
        }
        else {
            final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
            pd.setMessage("Loading...");
            pd.show();

            String url = "https://pregnantmother-e8d1f.firebaseio.com/users.json";

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {
                    DatabaseReference PregnantNoteReference = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com");

                    if(s.equals("null")) {
                        referenceToDatabase(PregnantNoteReference);
                        saveImg( PregnantNoteReference);
                        UserDetail.username = username;
                    }
                    else {
                        try {
                            JSONObject obj = new JSONObject(s);

                            if (!obj.has(username)) {
                                referenceToDatabase(PregnantNoteReference);
                                saveImg( PregnantNoteReference);
                                UserDetail.username = username;

                            }else if (UserDetail.profileMode.equals("edit")){
                                if (checkImg){
                                    referenceToDatabase(PregnantNoteReference);
                                    saveImg( PregnantNoteReference);
                                }else {
                                    referenceToDatabase(PregnantNoteReference);
                                }

                            }else {
                                edtUsername.setError("username already exists");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    pd.dismiss();
                }

            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError );
                    pd.dismiss();
                }
            });

            RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
            rQueue.add(request);



        }

    }

    private void referenceToDatabase(DatabaseReference PregnantNoteReference) {
        PregnantNoteReference.child("users").child(username).child("profile").child("password").setValue(password);
        PregnantNoteReference.child("users").child(username).child("profile").child("email").setValue(email);
        PregnantNoteReference.child("users").child(username).child("profile").child("firstname").setValue(name);
        PregnantNoteReference.child("users").child(username).child("profile").child("weight").setValue(weight);

        rememberUser();
    }

    private void rememberUser() {

        final DatabaseReference PregnantNoteReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/remembers");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layoutMain.getForeground().setAlpha( 220);
        }

        LayoutInflater rememberInflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupRememberView = rememberInflater.inflate(R.layout.popup_remember,null);

        final Button btnRememberYes = popupRememberView.findViewById(R.id.btnFavYes);
        final Button btnRememberNo = popupRememberView.findViewById(R.id.btnFavNo);

        btnRememberYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PregnantNoteReference.child(deviceId).child("user").setValue(username);
                btnRememberYes.setBackgroundResource(R.drawable.box_radius_btn_actcive);
                startActivity(new Intent(RegisterActivity.this,OldPregnantActivity.class));
            }
        });
        btnRememberNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRememberNo.setBackgroundResource(R.drawable.box_radius_btn_actcive);
                startActivity(new Intent(RegisterActivity.this,OldPregnantActivity.class));
            }
        });

        int widthDevice = getWindowManager().getDefaultDisplay().getWidth();
        int heightDevice = getWindowManager().getDefaultDisplay().getHeight();
        int margin = (int) (widthDevice*0.8);
        int height = (int) (heightDevice*0.25);

        PopupWindow popupWeight = new PopupWindow(popupRememberView,margin,height,true);
        popupWeight.showAtLocation(popupRememberView, Gravity.CENTER,0,0);
    }

    public void saveImg(final DatabaseReference PregnantNoteReference){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapImg.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataPic = baos.toByteArray();

        String id = UUID.randomUUID().toString();

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://pregnantmother-e8d1f.appspot.com");
        StorageReference storageRef = storage.getReference();
        final StorageReference imagesRef = storageRef.child("images/"+username+"/profile_"+id+".jpg");
        UploadTask uploadTask = imagesRef.putBytes(dataPic);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                PregnantNoteReference.child("users").child(username).child("profile").child("pic").setValue(imagesRef.getPath());
            }
        });



    }

    private void selectImgOrTakeImg() {
        final int REQUEST_CAMERA = 0;
        final int SELECT_FILE = 1;

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
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

            checkImg = true;

            bitmapImg = Bitmap.createScaledBitmap(bitmapImg, 130,130,true);
            btnprofile.setImageBitmap(bitmapImg);
            btnprofile.setScaleType(ImageView.ScaleType.FIT_XY);


        }


    }
    public void clickLogin(View v){

        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
    }


}
