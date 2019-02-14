package pudpongsai.thanaporn.th.ac.su.reg.pregnant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.NoteDetail;

public class WaterActivity extends AppCompatActivity {

    int countSmall,countSmall2,countSmall3,totalWater;
    TextView txtCountSmall,txtCountSmall2,txtCountSmall3,txtWaterStatus,txtWaterCal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        txtCountSmall = (TextView)findViewById(R.id.txtCountSmall);
        txtCountSmall2 = (TextView)findViewById(R.id.txtCountSmall2);
        txtCountSmall3 = (TextView)findViewById(R.id.txtCountSmall3);
        txtWaterStatus = (TextView)findViewById(R.id.txtWaterStatus);
        txtWaterCal = (TextView)findViewById(R.id.txtWaterCal);

        txtCountSmall.setText("0");
        txtCountSmall2.setText("0");
        txtCountSmall3.setText("0");
        countSmall = 0;
        countSmall2 = 0;
        countSmall3 = 0;
        totalWater = 0;
    }

    public void OnclickCompute(View view){
        Button btnMain = (Button)view;

        switch (btnMain.getId()){
            case R.id.btnDisSmall:
                countSmall = countSmall-1;
                countSmall =((countSmall < 0)? 0 : countSmall);
                txtCountSmall.setText(""+countSmall);
                break;

            case R.id.btnPlusSmall:
                countSmall = countSmall+1;
                txtCountSmall.setText(""+countSmall);
                break;

            case R.id.btnDisSmall2:
                countSmall2 = countSmall2-1;
                countSmall2 =((countSmall2 < 0)? 0 : countSmall2);
                txtCountSmall2.setText(""+countSmall2);
                break;

            case R.id.btnPlusSmall2:
                countSmall2 = countSmall2+1;
                txtCountSmall2.setText(""+countSmall2);
                break;

            case R.id.btnDisSmall3:
                countSmall3 = countSmall3-1;
                countSmall3 =((countSmall3 < 0)? 0 : countSmall3);
                txtCountSmall3.setText(""+countSmall3);
                break;

            case R.id.btnPlusSmall3:
                countSmall3 = countSmall3+1;
                txtCountSmall3.setText(""+countSmall3);
                break;
        }
        CalTotalWater();

    }
    public void CalTotalWater(){
        totalWater = 0;
        totalWater += countSmall*125;
        totalWater += countSmall2*250;
        totalWater += countSmall3*550;
        txtWaterCal.setText(""+totalWater+" มิลลิลิตร ");
        CalStatusWater();
    }


    public void CalStatusWater(){

        if (totalWater < 1400){
            txtWaterStatus.setText("คุณดื่มน้ำน้อยเกินไป");

        }else if (totalWater >= 1500 && totalWater <= 2000){
            txtWaterStatus.setText("คุณดื่มน้ำเพียงพอ");

        }else {
            txtWaterStatus.setText("คุณดื่มน้ำมากเกินไป");

        }

    }
    public void OnclickSaveWater(View view){
        startActivity(new Intent(WaterActivity.this,MainActivity.class));

    }

}
