package pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity;


import android.content.Context;
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
import com.cete.dynamicpdf.Document;
import com.cete.dynamicpdf.Font;
import com.cete.dynamicpdf.Page;
import com.cete.dynamicpdf.PageOrientation;
import com.cete.dynamicpdf.PageSize;
import com.cete.dynamicpdf.TextAlign;
import com.cete.dynamicpdf.pageelements.Label;

import org.json.JSONException;
import org.json.JSONObject;

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
        String FILE = Environment.getExternalStorageDirectory()
                + "/HelloWorld.pdf";
        Log.d("file",FILE);

        // Create a document and set it's properties
        Document objDocument = new Document();
        objDocument.setCreator("DynamicPDFHelloWorld.java");
        objDocument.setAuthor("Your Name");
        objDocument.setTitle("Hello World");

        // Create a page to add to the document
        Page objPage = new Page(PageSize.LETTER, PageOrientation.PORTRAIT,
                54.0f);

        // Create a Label to add to the page
//        String strText = arrTotalNote.get(0).getTotalPdf();
        String strText = "Hello World...\nFrom DynamicPDF Generator "
                + "for Java\nDynamicPDF.com";
        Label objLabel = new Label(strText, 0, 0, 504, 100,
                Font.getHelvetica(), 18, TextAlign.CENTER);

        // Add label to page
        objPage.getElements().add(objLabel);

        // Add page to document
        objDocument.getPages().add(objPage);

        try {
            // Outputs the document to file
            objDocument.draw(FILE);
            Toast.makeText(this, "File has been written to :" + FILE,
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this,
                    "Error, unable to write to file\n" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
    public void onClickBack(View v){
        onBackPressed();
    }
}
