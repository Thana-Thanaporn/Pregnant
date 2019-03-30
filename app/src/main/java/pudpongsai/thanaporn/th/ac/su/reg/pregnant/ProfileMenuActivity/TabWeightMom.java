package pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.DecimalFormat;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class TabWeightMom extends Activity {

    GraphView graph;
    TextView txtTodayWeight,txtBeforeWeight;
    String weight, hieght;
    Double myWeight = 0.0;
    Double oldWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_weight_mom);

        graph = (GraphView) findViewById(R.id.graph);
        txtTodayWeight = (TextView) findViewById(R.id.txtTodayWeight);
        txtBeforeWeight = (TextView) findViewById(R.id.txtBeforeWeight);


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

                DataSnapshot dsWeight = snapshot.child("weight");
                DataPoint[] points = new DataPoint[44];

                String weekLast = "";
                for(DataSnapshot ds : dsWeight.getChildren()) {
                    weekLast = ds.getKey();
                    int index = Integer.parseInt(ds.getKey());
                    points[index] = new DataPoint(index, getWeekWeight(ds));

                }
                getNowWeight(dsWeight.child(weekLast));
                getmyWeight(points);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

    }

    public void getNowWeight(DataSnapshot dataSnapshot){
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            Double data = Double.parseDouble(ds.getValue().toString());
            myWeight = data;
        }
        txtTodayWeight.setText(": "+myWeight+" กิโลกรัม");
    }

    public double getWeekWeight(DataSnapshot dataSnapshot){
        int count = 0;
        int sum = 0;
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            sum  += Double.parseDouble(ds.getValue().toString());
            count += 1;
        }
        return sum/count;
    }

    public void getmyWeight(DataPoint [] dataPoints){

        for (int i = 0 ; i < dataPoints.length ; i++){
            if (dataPoints[i] == null){
                dataPoints[i] = new DataPoint(i, 0);
            }
        }

        showGraphData(dataPoints);
    }

    public double calculatorBMI(double weight , double hieght) {
        double calBMI = (weight / (Math.pow(hieght * 0.01, 2)));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(calBMI));
    }

    private DataPoint [] GraphData(double []  weightBMI) {
        DataPoint[] points = new DataPoint[44];
        for (int i = 0; i < points.length; i++) {

            if (i < 15)
                points[i] = new DataPoint(i, oldWeight + (i*(weightBMI[0])));
            else if (i > 14 && i < 29)
                points[i] = new DataPoint(i, points[i-1].getY()+(weightBMI[1]));
            else
                points[i] = new DataPoint(i, points[i-1].getY()+(weightBMI[2]));
        }
        return points;
    }



    private void showGraphData(DataPoint [] dataPoints) {
        DataPoint[] pointsHigh,pointsLow,pointsCenter;
        Double BMI = calculatorBMI(oldWeight, Double.parseDouble(hieght));
        Log.d("check BMI", String.valueOf(BMI));
        if (BMI < 18.5){
            pointsHigh = GraphData(new double[]{0.4,0.58,0.8});
            pointsLow = GraphData(new double[]{0.1,0.44,0.4});
            pointsCenter = GraphData(new double[]{0.2,0.51,0.6});
        }else if (BMI >= 18.5 && BMI <= 24.9){
            pointsHigh = GraphData(new double[]{0.3,0.5,0.8});
            pointsLow = GraphData(new double[]{0.1,0.3,0.3});
            pointsCenter = GraphData(new double[]{0.2,0.4,0.5});
        }else if (BMI >= 25 && BMI <= 29.9){
            pointsHigh = GraphData(new double[]{0.1,0.33,0.5});
            pointsLow = GraphData(new double[]{0.02,0.23,0.3});
            pointsCenter = GraphData(new double[]{0.06,0.28,0.4});
        }else {
            pointsHigh = GraphData(new double[]{0.1,0.27,0.5});
            pointsLow = GraphData(new double[]{0.02,0.17,0.1});
            pointsCenter = GraphData(new double[]{0.06,0.22,0.3});
        }


        PointsGraphSeries<DataPoint> pointsGraphSeries = new PointsGraphSeries<>(dataPoints);
        pointsGraphSeries.setShape(PointsGraphSeries.Shape.POINT);
        pointsGraphSeries.setSize(10);
        pointsGraphSeries.setColor(Color.parseColor("#f9b7b7"));
        LineGraphSeries<DataPoint> lineGraphSeries = new LineGraphSeries<>(pointsHigh);
        lineGraphSeries.setColor(Color.parseColor("#FF0000"));
        LineGraphSeries<DataPoint> lineGraphSeries1 = new LineGraphSeries<>(pointsLow);
        lineGraphSeries1.setColor(Color.parseColor("#005A8C"));
        LineGraphSeries<DataPoint> lineGraphSeries2 = new LineGraphSeries<>(pointsCenter);
        lineGraphSeries2.setColor(Color.parseColor("#8CC63F"));
        graph.getViewport().setScalable(true);
        graph.addSeries(lineGraphSeries);
        graph.addSeries(lineGraphSeries1);
        graph.addSeries(lineGraphSeries2);
        graph.addSeries(pointsGraphSeries);


        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(myWeight-2);
        graph.getViewport().setMaxY(myWeight+5);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);


    }
}
