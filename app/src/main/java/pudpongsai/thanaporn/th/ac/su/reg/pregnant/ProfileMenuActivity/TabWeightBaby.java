package pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
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

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class TabWeightBaby extends Activity {

    GraphView graph;
    TextView txtTodayWeight;
    String weight, hieght;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_weight_baby);

        graph = (GraphView) findViewById(R.id.graph);
        txtTodayWeight = (TextView) findViewById(R.id.txtTodayWeight);


        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"+ UserDetail.username+"/babyweight");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                graph.removeAllSeries();
                DataPoint[] points = new DataPoint[36];

                String Last = "";
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Last = ds.getValue().toString();
                    int index = Integer.parseInt(ds.getKey().trim());
                    points[index-8] = new DataPoint(index, Double.parseDouble(ds.getValue().toString()));

                }
                getmyWeight(points);
                txtTodayWeight.setText(": "+Last+" กรัม");


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    public void getmyWeight(DataPoint [] dataPoints){

        for (int i = 0 ; i < dataPoints.length ; i++){
            if (dataPoints[i] == null){
                dataPoints[i] = new DataPoint(i+8, -100);
            }
        }

        showGraphData(dataPoints);
    }

    private DataPoint [] GraphData(double []  weightbaby) {
        DataPoint[] points = new DataPoint[36];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(i+8, weightbaby[i]);
        }
        return points;
    }
//    [1.5,2.5,4.5,7.5,14.5,24,45,75,105,145,195,245,305,365,435,506,605,670,1320,1470,1660,1890,2100,2290,2500,2690,2880,3090,3470,3610,3750,3870,3980,4060]
//    [1,2,4,7,14,23,43,70,100,140,190,240,300,360,430,501,600,660,860,990,1150,1310,1460,1630,1810,2010,2430,2650,2870,3030,3170,3280,3360,3410]
//    [0.5,1.5,3.5,6.5,13.5,22,41,65,95,135,185,235,295,355,425,494,595,650,670,680,770,890,1030,1180,1310,1480,1670,2190,2310,2510,2680,2750,2800,2830]

    private void showGraphData(DataPoint [] dataPoints) {
        DataPoint[] pointsHigh,pointsLow,pointsCenter;

        pointsHigh = GraphData(new double[]{17,22,37,47,60,75,95,119,148,183,225,275,333,401,480,
                570,672,787,915,1057,1212,1381,1561,1753,1955,2164,2379,2597,2815,3030,3238,3437,3621,3789,3959,4131});
        pointsCenter = GraphData(new double[]{15,20,35,45,58,73,93,117,146,181,223,273,331,399,478,568,670,785,
                913,1055,1210,1379,1559,1751,1953,2162,2377,2595,2813,3028,3236,3435,3619,3787,3957,4129});
        pointsLow = GraphData(new double[]{13,18,33,43,56,71,91,115,144,179,221,271,329,397,476,
                566,668,783,911,1053,1208,1377,1557,1749,1951,2160,2375,2593,2811,3026,3234,3433,3617,3785,3955,4127});


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
        graph.getViewport().setMinX(8);
        graph.getViewport().setMaxX(17);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(20);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);


    }
}
