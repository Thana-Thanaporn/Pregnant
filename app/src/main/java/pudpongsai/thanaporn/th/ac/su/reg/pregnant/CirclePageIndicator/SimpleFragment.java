package pudpongsai.thanaporn.th.ac.su.reg.pregnant.CirclePageIndicator;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.HomeActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.R;

public class SimpleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int position = 0;
        final ArrayList<String> imgPath = new ArrayList<>();
        final ArrayList<String> details = new ArrayList<>();

        Bundle bundle = getArguments();

        position = bundle.getInt(SimplePagerAdapter.ARGS_POSITION);

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.circlepageindicator_fragment, container, false);

        final ImageView imgShow = (ImageView) rootView.findViewById(R.id.imgShow);
        final TextView txtShow = (TextView) rootView.findViewById(R.id.detailEx);

        DatabaseReference referenceWeek = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/pregnantDetails/"
                        + UserDetail.oldPregnant+"/"+UserDetail.weekPregnant+"/exercises/"+UserDetail.selectExercises);

        final int finalPosition = position;
        referenceWeek.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, String> map = (Map)snapshot.getValue();
                imgPath.add(map.get("pic1").toString());
                imgPath.add(map.get("pic2").toString());
                imgPath.add(map.get("pic3").toString());

                details.add(map.get("detail1").toString());
                details.add(map.get("detail2").toString());
                details.add(map.get("detail3").toString());
                Picasso.get()
                        .load(imgPath.get(finalPosition % imgPath.size()))
                        .into(imgShow);
                txtShow.setText(details.get(finalPosition % details.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });






        return rootView;
    }
}
