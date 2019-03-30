package pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Adapters.TotalAdapter;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.TotalWeekDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class TotalNoteActivity extends AppCompatActivity {
    Context mcontext = TotalNoteActivity.this;
    ListView listTotalNote;

    static  int  countselect;
    TotalAdapter totalAdapter;

    ArrayList<Bitmap> arrPic = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_total_note);
        countselect = 0;
        UserDetail.totalNote.clear();
        listTotalNote = (ListView) findViewById(R.id.listTotalNote);

        totalAdapter = new TotalAdapter(UserDetail.totalNote,mcontext);
        listTotalNote.setAdapter(totalAdapter);

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+ UserDetail.username+"/notes");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(final DataSnapshot ds : snapshot.getChildren()) {

                    int count = 0 ;
                    for(final DataSnapshot dsday : ds.getChildren()) {
                        count +=1;
                    }

                    UserDetail.totalNote.add(new TotalWeekDetail(count,ds.getKey(),false));
                    totalAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });


    }
    public static void countSelectNote(){
        countselect = 0;
        ArrayList<TotalWeekDetail> arr = UserDetail.totalNote;
        for (int i = 0; i < arr.size(); i++){
            if(arr.get(i).isStatus())
                countselect += arr.get(i).getTotalNote();
        }


    }
    public boolean checkStatus(String week){
        for (TotalWeekDetail weekDetail : UserDetail.totalNote) {
            if (weekDetail.getWeek().equals(week)) {
                return weekDetail.isStatus();
            }
        }
        return false;
    }


    public void loadData(View view){
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+ UserDetail.username+"/notes");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(final DataSnapshot ds : snapshot.getChildren()) {

                    if(checkStatus(ds.getKey())){
                        for(final DataSnapshot dataSnapshot : ds.getChildren()) {
                            final Map<String, String> map = (Map) dataSnapshot.getValue();
                            String imgPath = map.get("pic1").toString();

                            StorageReference storageReference = FirebaseStorage.getInstance("gs://pregnantmother-e8d1f.appspot.com").getReference()
                                    .child(imgPath);

                            Glide.with(TotalNoteActivity.this)
                                    .using(new FirebaseImageLoader())
                                    .load(storageReference)
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                            arrPic.add(resource);
                                            arrPic.add(createText(map,ds.getKey(),dataSnapshot.getKey()));


                                            if (arrPic.size() == countselect*2)
                                                printPdf();
                                        }
                                        @Override
                                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                            super.onLoadFailed(e, errorDrawable);
                                        }
                                    });
                            }
                        }

                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    public Bitmap createText(Map<String, String> map , String week, String day){

        String[] showtext = new String[5];
        int[] texthight = new int[5];
        showtext[0] = "สัปดาห์ที่ "+week + "  วันที่ " + day;
        showtext[1] = "บันทึก : " +  map.get("note");
        showtext[2] = "น้ำหนัก : " + map.get("weight");
        showtext[3] = "การดิ้น : " +  map.get("kick");
        showtext[4] = "วันที่บันทึก : " +  map.get("date");
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(24);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.LEFT);
        Typeface type = ResourcesCompat.getFont(TotalNoteActivity.this, R.font.kanit_medium);
        paint.setTypeface(type);

        Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setTextSize(20);
        paint1.setColor(Color.BLACK);
        paint1.setTextAlign(Paint.Align.LEFT);
        Typeface type1 = ResourcesCompat.getFont(TotalNoteActivity.this, R.font.kanit_light);
        paint.setTypeface(type1);
        float baseline = -paint.ascent(); // ascent() is negative
        int height =0;
        for (int i =0 ; i< showtext.length ; i++){
            Rect bounds = new Rect();
             paint1.getTextBounds(showtext[i], 0, showtext[i].length(), bounds);
            if (i == 0){
                texthight[0] = (int) (baseline);
            }else {
                texthight[i] = (int) (bounds.height() + texthight[i-1] +7.5f);
            }

            height = texthight[i];
        }

////        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(500, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(showtext[0], 0, texthight[0], paint);
        canvas.drawText(showtext[1], 0, texthight[1], paint1);
        canvas.drawText(showtext[2], 0, texthight[2], paint1);
        canvas.drawText(showtext[3], 0, texthight[3], paint1);
        canvas.drawText(showtext[4], 0, texthight[4], paint1);

        return  image;
    }


    public void printPdf(){
        String dest = Environment.getExternalStorageDirectory()
                + "/YourNote.pdf";
        if (new File(dest).exists()) {
            new File(dest).delete();
        }


        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
            writer.setPdfVersion(PdfWriter.VERSION_1_7);

            document.open();

            int height = 680 ;
            for (int i = arrPic.size() -1 ; i >= 0;i--) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                arrPic.get(i).compress(Bitmap.CompressFormat.PNG, 100 , stream);
                Image myImg = Image.getInstance(stream.toByteArray());

                if (height < arrPic.get(i).getHeight()){
                    document.newPage();
                    height = 680;
                }
                myImg.setAbsolutePosition(40,height);
                height -= arrPic.get(i).getHeight()+20;
                document.add(myImg);
            }

            document.close();
            Toast.makeText(this,"This is a Toast", Toast.LENGTH_LONG).show();
            

            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(Toast.LENGTH_LONG); // As I am using LENGTH_LONG in Toast
                        finish();
                        startActivity(getIntent());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            thread.start();

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
