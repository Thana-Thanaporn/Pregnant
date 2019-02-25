package pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity;


import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.TelDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.TotalWeekDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class TotalNoteActivity extends AppCompatActivity {
    Context mcontext = TotalNoteActivity.this;
    ListView listTotalNote;

    ArrayList<TotalWeekDetail> arrTotalNote = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_note);
        listTotalNote = (ListView) findViewById(R.id.listTotalNote);

        String url = "https://pregnantmother-e8d1f.firebaseio.com/users/"+UserDetail.username+"/notes.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(!s.equals("null")) {
                    try {
                        JSONObject obj = new JSONObject(s);
                        Iterator i = obj.keys();
                        String key = "";

                        while(i.hasNext()){
                            key = i.next().toString();
                            totalWeek(obj.getJSONObject(key),key);
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

        RequestQueue rQueue = Volley.newRequestQueue(mcontext);
        rQueue.add(request);
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

    public void printPdf(View view){
        String dest = Environment.getExternalStorageDirectory()
                + "/HelloWorld.pdf";
        if (new File(dest).exists()) {
            new File(dest).delete();
        }
        String fontFilePath = Uri.parse("android.resource://"+getPackageName()+"/font/kanit_regular.ttf").toString();


        if (new File(fontFilePath).exists()) {
            Log.d("ffff","ffffff");
        }
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
            writer.setPdfVersion(PdfWriter.VERSION_1_7);


//            FontFactory.register(System.getProperty("file.separator")+"resources"+System.getProperty("file.separator")+"font"+System.getProperty("file.separator")+"kanit_regular.‌​ttf", "my_bold_font");
//            Font myBoldFont = FontFactory.getFont("my_bold_font");

//            String fontName = "kanit_regular.ttf";
//            InputStream is = mcontext.getAssets().open(fontName);
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            int a = is.read(buffer);
//            BaseFont customFont = BaseFont.createFont(fontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, buffer, buffer);
//            Font banglaFont = new Font(customFont, 12);


//            BaseFont bf = BaseFont.createFont("C:\\Users\\youn2\\Downloads\\THSarabunNew.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            Font font = new Font(bf, 12);
//
//            document.open();
//            document.add(new Paragraph("ดดดดดดด",font));
//
//            document.close();
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
