package pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.TotalWeekDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class TotalNoteActivity extends AppCompatActivity {
    Context mcontext = TotalNoteActivity.this;
    ListView listTotalNote;
    ImageView pictest;

    ArrayList<TotalWeekDetail> arrTotalNote = new ArrayList<>();
    ArrayList<Bitmap> arrPic = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_note);
        listTotalNote = (ListView) findViewById(R.id.listTotalNote);
        pictest = (ImageView) findViewById(R.id.pictest);

        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+ UserDetail.username+"/notes/"+4);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, String> map = (Map) ds.getValue();
                    String imgPath = map.get("pic1").toString();
                    Log.d("imgPath pdf ", imgPath);

                    StorageReference storageReference = FirebaseStorage.getInstance("gs://pregnantmother-e8d1f.appspot.com").getReference()
                            .child(imgPath);

                    Glide.with(TotalNoteActivity.this)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    Bitmap bimtapImage= resource;
                                    arrPic.add(bimtapImage);
                                    pictest.setImageBitmap(bimtapImage);
                                }
                                @Override
                                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                    super.onLoadFailed(e, errorDrawable);
                                }
                            });

                }
                Log.d("pdf ", String.valueOf(arrPic.size()));
//                printPdf();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });




//        Glide.with(this)
//                .load("https://firebasestorage.googleapis.com/v0/b/pregnantmother-e8d1f.appspot.com/o/images%2Ftoth%2Fnotes%2Fweek_10day_2_a5ed353d-28d8-4533-ae79-72f33bc35377.jpg?alt=media&token=677ebe68-3fe5-4d0d-b44f-82d7123f4ebe")
//                .asBitmap()
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        Bitmap bimtapImage=resource;
//                        pictest.setImageBitmap(bimtapImage);
//                    }
//                    @Override
//                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                        super.onLoadFailed(e, errorDrawable);
//                    }
//                });
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Bitmap theBitmap = Glide.
//                            with(TotalNoteActivity.this).
//                            load("https://i.imgur.com/lU6i0qJ.jpg").
//                            asBitmap().
//                            into(100, 100). // Width and height
//                            get();
//                    pictest.setImageBitmap(theBitmap);
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });



//        String url = "https://pregnantmother-e8d1f.firebaseio.com/users/"+UserDetail.username+"/notes.json";
//        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
//            @Override
//            public void onResponse(String s) {
//                if(!s.equals("null")) {
//                    try {
//                        JSONObject obj = new JSONObject(s);
//                        Iterator i = obj.keys();
//                        String key = "";
//
//                        while(i.hasNext()){
//                            key = i.next().toString();
//                            totalWeek(obj.getJSONObject(key),key);
//                        }
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        },new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                System.out.println("" + volleyError );
//            }
//        });
//
//        RequestQueue rQueue = Volley.newRequestQueue(mcontext);
//        rQueue.add(request);

    }

    private void totalWeek(JSONObject object ,String week) {

        try {
            Iterator i = object.keys();
            String key = "";

            int countNote = 0 ;
            String textPdf ="สัปดาห์ที่ " + key;

            while(i.hasNext()){
                key = i.next().toString();
                JSONObject objDay = object.getJSONObject(key);
                textPdf += "\n"+" วันที่ : " + key
                        + "\n บันทึก : " + objDay.getString("note")
                        + "\n น้ำหนัก : " + objDay.getString("weight");
//                        + "\n การดิ้น : " + objDay.getString("kick")
//                        + "\n วันที่บันทึก : " + objDay.getString("date")
//                        + "\n\n";


                countNote += 1;
            }

            arrTotalNote.add(new TotalWeekDetail(countNote,key,textPdf));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void printPdf(){
        String dest = Environment.getExternalStorageDirectory()
                + "/HelloWorld.pdf";
        if (new File(dest).exists()) {
            new File(dest).delete();
        }

//        String text = "งงงงงงงงงงงงงงงงกกกดำทเทกาสเ่าส";
//        String[] showtext = new String[4];
//        int[] texthight = new int[4];
//        showtext[0] = "สัปดาห์ที่ 4 วันที่ 2";
//        showtext[1] = "สัปดาห์ที่ 4 วันที่ 2";
//        showtext[2] = "สัปดาห์ที่ 4 วันที่ 2";
//        showtext[3] = "สัปดาห์ที่ 4 วันที่ 2";
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setTextSize(24);
//        paint.setColor(Color.BLACK);
//        paint.setTextAlign(Paint.Align.LEFT);
//        Typeface type = ResourcesCompat.getFont(TotalNoteActivity.this, R.font.kanit_medium);
//        paint.setTypeface(type);
//
//        Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint1.setTextSize(20);
//        paint1.setColor(Color.BLACK);
//        paint1.setTextAlign(Paint.Align.LEFT);
//        Typeface type1 = ResourcesCompat.getFont(TotalNoteActivity.this, R.font.kanit_light);
//        paint.setTypeface(type1);
//        float baseline = -paint.ascent(); // ascent() is negative
//        int height =0;
//        for (int i =0 ; i< showtext.length ; i++){
//            Rect bounds = new Rect();
//            paint1.getTextBounds(text, 0, text.length(), bounds);
//            if (i == 0){
//                texthight[0] = (int) (baseline);
//            }else {
//                texthight[i] = (int) (bounds.height() + texthight[i-1] +7.5f);
//            }
//
//            height = texthight[i];
//        }

////        int height = (int) (baseline + paint.descent() + 0.5f);
//        Bitmap image = Bitmap.createBitmap(500, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(image);
//        canvas.drawText(showtext[0], 0, texthight[0], paint);
//        canvas.drawText(showtext[1], 0, texthight[1], paint1);
//        canvas.drawText(showtext[2], 0, texthight[2], paint1);
//        canvas.drawText(showtext[3], 0, texthight[3], paint1);
//
//        pictest.setImageBitmap(image);
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
            writer.setPdfVersion(PdfWriter.VERSION_1_7);



            for (int i =0 ; i< arrPic.size() ;i++) {
                ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
                arrPic.get(i).compress(Bitmap.CompressFormat.PNG, 100, stream3);
                Image maimg = Image.getInstance(stream3.toByteArray());
                document.add(maimg);
            }
//            Image image2 = Image.getInstance( new URL("https://i.imgur.com/lU6i0qJ.jpg"));
//            document.add(image2);

//            URL newurl = new URL("https://i.imgur.com/lU6i0qJ.jpg");
//            Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
//            pictest.setImageBitmap(mIcon_val);
            document.open();
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void onClickBack(View v){
        onBackPressed();
    }
}
