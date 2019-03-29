package pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.PregnantUitli;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class GraphActivity extends AppCompatActivity {

    LocalActivityManager mLocalActivityManager;
    public Context mcontext = GraphActivity.this;

    LinearLayout layoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_graph);

        layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
        layoutMain.getForeground().setAlpha( 0);


        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);


        tabHost.setup(mLocalActivityManager);

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1")
                .setIndicator("น้ำหนักของคุณ")
                .setContent(new Intent(this, TabWeightMom.class));

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator("น้ำหนักทารกในครรภ์")
                .setContent(new Intent(this, TabWeightBaby.class));


        tabHost.addTab(tabSpec);
        tabHost.addTab(tabSpec2);

        TextView x = (TextView) tabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        x.setTextSize(18);

        TextView x1 = (TextView) tabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        x1.setTextSize(18);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(!isFinishing());

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocalActivityManager.dispatchResume();
    }

    public  void onClickNewBabyWeight(View v) {
        PregnantUitli.popupBabyWeight(layoutMain, mcontext);
    }


    public void onClickBack(View v){
        onBackPressed();
    }
}
