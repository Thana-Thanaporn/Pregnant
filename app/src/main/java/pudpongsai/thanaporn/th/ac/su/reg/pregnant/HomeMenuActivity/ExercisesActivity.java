package pudpongsai.thanaporn.th.ac.su.reg.pregnant.HomeMenuActivity;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.CirclePageIndicator.SimplePagerAdapter;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class ExercisesActivity extends AppCompatActivity {
    private CirclePageIndicator mCirclePageIndicator;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_exercises);

        TextView txtEx = (TextView) findViewById(R.id.idEx);
        txtEx.setText("ท่าที่ " + UserDetail.selectExercises);
        mViewPager = (ViewPager) findViewById(R.id.pager);

        mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        final float density = getResources().getDisplayMetrics().density;
        mCirclePageIndicator.setFillColor(Color.GRAY);
        mCirclePageIndicator.setStrokeColor(Color.GRAY);
        mCirclePageIndicator.setStrokeWidth(1);
        mCirclePageIndicator.setRadius(6 * density);

        SimplePagerAdapter adapter =
                new SimplePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);

        mCirclePageIndicator.setViewPager(mViewPager);
    }

    public void onClickBack(View v){
        onBackPressed();
    }
}
