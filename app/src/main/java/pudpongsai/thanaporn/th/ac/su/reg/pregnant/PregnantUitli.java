package pudpongsai.thanaporn.th.ac.su.reg.pregnant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.TelDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.LoginMenuActivity.LoginActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.LoginMenuActivity.RegisterActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NoteMenuActivity.NoteActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.NoteMenuActivity.NotePregnantActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.ProfileMenuActivity.GraphActivity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class PregnantUitli {

    public  void checkNowPregnant(String date ,String weekStr , String dayStr){
        long week = Long.parseLong(weekStr);
        long day =  Long.parseLong(dayStr);

        long oldDate = Long.parseLong(date);
        Calendar now = Calendar.getInstance();

        long diff = oldDate - now.getTimeInMillis();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;//20
        long weeks = days /7;// 20/7 = 2 เหลือ 6
        long dayWeek = days %7; //6

        day += Math.abs(dayWeek);
        week += Math.abs(weeks);
        if(day > 7){
            week +=1;
            day -= 7;
        }
        UserDetail.weekPregnant = ""+week;
        UserDetail.dayPregnant = ""+day;
//        UserDetail.dayPregnant = ""+4;
//        UserDetail.weekPregnant = ""+5;

    }

    public void popupTel(final LinearLayout layout, Context context , final TelDetail telDetail){
        int widthDevice = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        int heightDevice = ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layout.getForeground().setAlpha( 220);
        }

        LayoutInflater weightInflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupTelView = weightInflater.inflate(R.layout.popup_tel,null);

        final Button btnSaveTel = popupTelView.findViewById(R.id.btnSaveTel);
        final EditText edtTel = popupTelView.findViewById(R.id.edtTel);
        final EditText edtNameTel = popupTelView.findViewById(R.id.edtNameTel);
        edtTel.setText(telDetail.getTel());
        edtNameTel.setText(telDetail.getNameTel());

        int width = (int) (widthDevice*0.8);
        int height = (int) (heightDevice*0.35);

        final PopupWindow popup = new PopupWindow(popupTelView,width,height,true);
        popup.showAtLocation(popupTelView, Gravity.CENTER,0,0);
        popup.setOutsideTouchable(true);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        btnSaveTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkEditText = true;
                if (edtTel.getText().toString().equals("")){
                    edtTel.setError("กรุณากรอกเบอร์โทรศัทพ์");
                    checkEditText = false;
                }
                if (edtNameTel.getText().toString().equals("")){
                    edtNameTel.setError("กรุณากรอกชื่อเจ้าของเบอร์โทรศัทพ์");
                    checkEditText = false;
                }
                if (checkEditText){
                    String nametel = edtNameTel.getText().toString();
                    DatabaseReference saveTelReference = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"
                                    + UserDetail.username + "/tels");
                    if (!telDetail.getNameTel().equals("")){
                        saveTelReference.child(telDetail.getNameTel()).child("tel").setValue(null);
                    }
                    saveTelReference.child(nametel).child("tel").setValue(edtTel.getText().toString());
                    popup.dismiss();
                    layout.getForeground().setAlpha(0);

                }

            }
        });

    }

    public void popupEditDelTel(final LinearLayout layout, final Context context , final TelDetail telDetail){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layout.getForeground().setAlpha( 220);
        }

        LayoutInflater EditNoteInflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupEditNoteView = EditNoteInflater.inflate(R.layout.popup_edit_del_note,null);

        final Button btnNoteEdit = popupEditNoteView.findViewById(R.id.btnNoteEdit);
        final Button btnNoteDel = popupEditNoteView.findViewById(R.id.btnNoteDel);

        int widthDevice = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        int heightDevice = ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
        int margin = (int) (widthDevice*0.8);
        int height = (int) (heightDevice*0.18);

        final PopupWindow popupEditTel = new PopupWindow(popupEditNoteView,margin,height,true);
        popupEditTel.setOutsideTouchable(true);
        popupEditTel.showAtLocation(popupEditNoteView,Gravity.CENTER,0,0);

        btnNoteEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNoteEdit.setBackgroundResource(R.drawable.box_radius_btn_actcive);
                popupEditTel.dismiss();
                popupTel(layout,context,telDetail);

            }
        });
        btnNoteDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNoteDel.setBackgroundResource(R.drawable.box_radius_btn_actcive);
                DatabaseReference saveTelReference = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"
                                + UserDetail.username + "/tels");
                saveTelReference.child(telDetail.getNameTel()).child("tel").setValue(null);
                popupEditTel.dismiss();
                layout.getForeground().setAlpha( 0);
                popupDelTel(layout,context);


            }
        });
        popupEditTel.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layout.getForeground().setAlpha( 0);
            }
        });

    }

    public void popupDelTel(final LinearLayout layout, final Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layout.getForeground().setAlpha( 220);
        }
        LayoutInflater weightInflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupDelView = weightInflater.inflate(R.layout.popup_thx_note,null);

        int widthDevice = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        int heightDevice = ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
        int margin = (int) (widthDevice*0.8);
        int height = (int) (heightDevice*0.35);
        final TextView txtDetail = popupDelView.findViewById(R.id.txtDetail);
        txtDetail.setText("ลบเบอร์โทรศัพท์เรียบร้อยแล้ว");

        final PopupWindow popup = new PopupWindow(popupDelView,margin,height,true);
        popup.setOutsideTouchable(true);
        popup.showAtLocation(popupDelView,Gravity.CENTER,0,0);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layout.getForeground().setAlpha( 0);
            }
        });

        new Handler().postDelayed(new Runnable(){
            public void run() {
                layout.getForeground().setAlpha( 0);
                popup.dismiss();

            }
        }, 3 *1000);
    }

    public void popupEditOutProfile(final LinearLayout layout, final Context context ){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layout.getForeground().setAlpha( 220);
        }

        LayoutInflater EditNoteInflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupEditNoteView = EditNoteInflater.inflate(R.layout.popup_edit_del_note,null);

        final Button btnNoteEdit = popupEditNoteView.findViewById(R.id.btnNoteEdit);
        final Button btnNoteDel = popupEditNoteView.findViewById(R.id.btnNoteDel);
        btnNoteDel.setText("ออกจากระบบ");
        int widthDevice = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        int heightDevice = ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
        int margin = (int) (widthDevice*0.8);
        int height = (int) (heightDevice*0.18);

        final PopupWindow popupEditTel = new PopupWindow(popupEditNoteView,margin,height,true);
        popupEditTel.setOutsideTouchable(true);
        popupEditTel.showAtLocation(popupEditNoteView,Gravity.CENTER,0,0);

        btnNoteEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNoteEdit.setBackgroundResource(R.drawable.box_radius_btn_actcive);
                popupEditTel.dismiss();
                UserDetail.profileMode = "edit";
                context.startActivity(new Intent(context,RegisterActivity.class));
            }
        });
        btnNoteDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNoteDel.setBackgroundResource(R.drawable.box_radius_btn_actcive);
                popupEditTel.dismiss();
                layout.getForeground().setAlpha( 0);
                UserDetail.isLogout = true;
                context.startActivity(new Intent(context,LoginActivity.class));
            }
        });
        popupEditTel.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layout.getForeground().setAlpha( 0);
            }
        });

    }

}
