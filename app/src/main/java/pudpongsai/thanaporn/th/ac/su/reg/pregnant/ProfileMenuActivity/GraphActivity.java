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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.DecimalFormat;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class GraphActivity extends AppCompatActivity {
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


        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+UserDetail.username);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                graph.removeAllSeries();

                DataSnapshot dsProfile = snapshot.child("profile");

                weight = dsProfile.child("weight").getValue().toString();
                hieght = dsProfile.child("hieght").getValue().toString();
                oldWeight = Double.valueOf(weight);
                txtBeforeWeight.setText(": "+weight+" กิโลกรัม");
                txtHieght.setText(": "+hieght+" เซนติเมตร");

                DataSnapshot dsWeight = snapshot.child("weight");
                if (dsWeight.hasChild(UserDetail.weekPregnant)){
                    getWeight(dsWeight.child(UserDetail.weekPregnant));
                }else {
                    String weekLast = "";

                    for(DataSnapshot ds : dsWeight.getChildren()) {
                        weekLast = ds.getKey();
                    }
                    getWeight(dsWeight.child(weekLast));
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

    }
    public void getWeight(DataSnapshot dataSnapshot){

        DataPoint[] dataPoints = new DataPoint[7];

        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            int index = Integer.parseInt(ds.getKey());
            Double data = Double.parseDouble(ds.getValue().toString());

            dataPoints[index-1] = new DataPoint(index,data);
            myWeight = data;
        }

        for (int i = 0 ; i < dataPoints.length ; i++){
            if (dataPoints[i] == null){
                dataPoints[i] = new DataPoint(i, 0);
            }
        }

        showGraphData(dataPoints);

        txtTodayWeight.setText(": "+myWeight+" กิโลกรัม");
        txtBMI.setText(calculatorBMI(Integer.parseInt(weight),Integer.parseInt(hieght)));

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


    private void showGraphData(DataPoint [] dataPoints) {


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
