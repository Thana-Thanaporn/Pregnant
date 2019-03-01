package pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.LoginMenuActivity.LoginActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.PregnantUitli;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class GraphActivity extends AppCompatActivity {
    Context mcontext = GraphActivity.this;
    DataPoint[] dataPoints = new DataPoint[7];
    GraphView graph;

    TextView txtweekGraph,txtstandardWeight,txtTodayWeight,txtBeforeWeight,txtHieght,txtBMI;
    String weight, hieght;
    Double myWeight , oldWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_graph);

        graph = (GraphView) findViewById(R.id.graph);
        txtweekGraph = (TextView) findViewById(R.id.txtweekGraph);
        txtstandardWeight = (TextView) findViewById(R.id.txtstandardWeight);
        txtTodayWeight = (TextView) findViewById(R.id.txtTodayWeight);
        txtBeforeWeight = (TextView) findViewById(R.id.txtBeforeWeight);
        txtHieght = (TextView) findViewById(R.id.txtHieght);
        txtBMI = (TextView) findViewById(R.id.txtBMI);

        txtweekGraph.setText("สัปดาห์ที่ "+UserDetail.weekPregnant);



        String url = "https://pregnantmother-e8d1f.firebaseio.com/users/"+UserDetail.username+".json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(!s.equals("null")) {
                    try {
                        JSONObject obj = new JSONObject(s);
                        JSONObject weightObj = obj.getJSONObject("weight").getJSONObject(UserDetail.weekPregnant);
                        showGraphData(weightObj);

                        weight = obj.getJSONObject("profile").getString("weight");
                        hieght = obj.getJSONObject("profile").getString("hieght");
                        myWeight = Double.valueOf(weightObj.getString(UserDetail.dayPregnant));
                        oldWeight = Double.valueOf(obj.getJSONObject("profile").getString("weight"));
                        txtTodayWeight.setText(": "+weightObj.getString(UserDetail.dayPregnant)+" กิโลกรัม");
                        txtBeforeWeight.setText(": "+weight+" กิโลกรัม");
                        txtHieght.setText(": "+hieght+" เซนติเมตร");
                        txtBMI.setText(calculatorBMI(Integer.parseInt(weight),Integer.parseInt(hieght)));



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

    private String calculatorBMI(int weight , int hieght) {
        double calBMI =(weight/(Math.pow(hieght*0.01,2)));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double BMI = Double.parseDouble(decimalFormat.format(calBMI));
        calWeightPregnant(BMI);
        if (BMI < 18.5){
            return ": "+BMI +" (ผอม)";
        }else if (BMI >= 18.5 && BMI <= 22.9){
            return ": "+BMI +" (ปกติ)";
        }else if (BMI >= 23 && BMI <= 24.9){
            return ": "+BMI +" (น้ำหนักเกิน ระดับ 1)";
        }
        else if (BMI >= 25 && BMI <= 29.9){
            return ": "+BMI +" (น้ำหนักเกิน ระดับ 2)";
        }else {
            return ": "+BMI + " (น้ำหนักเกิน ระดับ 3)";
        }
    }
    public void calWeightPregnant(double BMI){
        String weightStatus = "";
        if (BMI < 18.5){
            weightStatus = calWeightWeekPregnant(new double[]{0.224,0.5,0.5});
        }else if (BMI >= 18.5 && BMI <= 24.9){
            weightStatus = calWeightWeekPregnant(new double[]{0.178,0.4,0.4});
        }else if (BMI >= 25 && BMI <= 29.9){
            weightStatus = calWeightWeekPregnant(new double[]{0.1,0.3,0.3});
        }else {
            weightStatus = calWeightWeekPregnant(new double[]{0.05,0.2,0.2});
        }

        txtstandardWeight.setText(weightStatus);

    }

    public String calWeightWeekPregnant(double []  weight){
        int week = Integer.parseInt(UserDetail.weekPregnant);
        if (week > 3 && week < 14){ //4-13
            return  calWeightRangePregnant(weight[0],week);
        }else if (week >= 14 && week < 29){//14-28
            return calWeightRangePregnant(weight[1],week);
        }else { //>29
            return calWeightRangePregnant(weight[2],week);
        }
    }

    public String calWeightRangePregnant(double  weight ,int week ){
        weight = oldWeight +(weight * week);
        Log.d("weight","{"+weight+"} - {"+myWeight+"]");
        if ( myWeight < weight){
           return  "น้อยกว่าเกณฑ์";
        }else if (myWeight >= weight && myWeight < (weight+0.2)){
            return "ตามเกณฑ์";
        }else {
            return "มากกว่าเกณฑ์";
        }
    }


    private void showGraphData(JSONObject obj) {
        String[] days = new String[]{"1","2","3","4","5","6","7"};

        for (int i = 0 ; i < days.length ; i++){
            try {
                if (obj.has(days[i])){
                    dataPoints[i] = new DataPoint(i, Double.parseDouble(obj.getString(days[i])));
                }else {
                    dataPoints[i] = new DataPoint(i, 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(dataPoints);
        PointsGraphSeries<DataPoint> pointsGraphSeries = new PointsGraphSeries<>(dataPoints);
        pointsGraphSeries.setShape(PointsGraphSeries.Shape.POINT);
        pointsGraphSeries.setColor(Color.parseColor("#f9b7b7"));
        lineGraphSeries.setColor(Color.parseColor("#f9b7b7"));
        graph.addSeries(pointsGraphSeries);
        graph.addSeries(lineGraphSeries);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"อา.", "จ.", "อ.","พ.","พฤ.","ศ.","ส."});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

    }



    public void onClickBack(View v){
        onBackPressed();
    }
}
