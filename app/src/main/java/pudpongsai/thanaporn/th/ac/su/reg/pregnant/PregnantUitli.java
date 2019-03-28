package pudpongsai.thanaporn.th.ac.su.reg.pregnant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Adapters.NotiAdapter;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Adapters.SosAdapter;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.CalendarMenuActivity.AddDateActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.CalendarMenuActivity.CalendarActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.EventDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.TelDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.LoginMenuActivity.LoginActivity;
import pudpongsai.thanaporn.th.ac.su.reg.pregnant.LoginMenuActivity.RegisterActivity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class PregnantUitli {

    public static  void  checkNoti(final TextView textView , final ListView listView , final Context context){
        Calendar now  = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY,0);
        now.set(Calendar.MINUTE,0);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MILLISECOND,0);

        DatabaseReference  referenceNoti = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"
                        + UserDetail.username+"/calendars/"+now.getTimeInMillis());

        referenceNoti.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                UserDetail.arrNoti.clear();

                for(DataSnapshot dsDay : snapshot.getChildren()) {
                    Map<String,String> map = (Map) dsDay.getValue();
                    UserDetail.arrNoti.add(
                            new EventDetail(map.get("topic"),
                                    map.get("place"),
                                    map.get("detail"),
                                    Long.parseLong(dsDay.getKey())));
                }

                if (UserDetail.arrNoti.size() != 0){
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("" + UserDetail.arrNoti.size());
                }


                if (listView != null){
                    NotiAdapter notiAdapter = new NotiAdapter(UserDetail.arrNoti,context);
                    listView.setAdapter(notiAdapter);
                    notiAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });


    }

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
//        UserDetail.weekPregnant = ""+week;
//        UserDetail.dayPregnant = ""+day;
        UserDetail.dayPregnant = ""+3;
        UserDetail.weekPregnant = ""+4;

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
        int margin = (int) (widthDevice*0.95);
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
                String word = "ลบเบอร์โทรศัพท์เรียบร้อยแล้ว";
                popupDel(layout,context,word);


            }
        });
        popupEditTel.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layout.getForeground().setAlpha( 0);
            }
        });

    }

    public static void popupDel(final LinearLayout layout, final Context context, String word){
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
        txtDetail.setText(word);

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
    public static void popupEditDelEvent(final LinearLayout layout, final Context context , final EventDetail eventDetail,final long day){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layout.getForeground().setAlpha( 220);
        }

        LayoutInflater EditNoteInflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupEditNoteView = EditNoteInflater.inflate(R.layout.popup_edit_del_note,null);

        final Button btnEdit = popupEditNoteView.findViewById(R.id.btnNoteEdit);
        final Button btnDel = popupEditNoteView.findViewById(R.id.btnNoteDel);

        int widthDevice = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        int heightDevice = ((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
        int wight = (int) (widthDevice*0.95);
        int height = (int) (heightDevice*0.18);

        final PopupWindow popupEditDel = new PopupWindow(popupEditNoteView,wight,height,true);
        popupEditDel.setOutsideTouchable(true);
        popupEditDel.showAtLocation(popupEditNoteView,Gravity.CENTER,0,0);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEdit.setBackgroundResource(R.drawable.box_radius_btn_actcive);
                popupEditDel.dismiss();
                Intent mIntent = new Intent(context, AddDateActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putLong("Date", day);
                mBundle.putLong("Time", eventDetail.getDate());
                mIntent.putExtras(mBundle);
                context.startActivity(mIntent);

            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDel.setBackgroundResource(R.drawable.box_radius_btn_actcive);
                DatabaseReference saveTelReference = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"
                                + UserDetail.username+"/calendars/"+day);
                saveTelReference.child(String.valueOf(eventDetail.getDate())).setValue(null);
                popupEditDel.dismiss();
                layout.getForeground().setAlpha( 0);
                String word = "ลบบันทึกเรียบร้อยแล้ว";
                popupDel(layout,context,word);


            }
        });
        popupEditDel.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layout.getForeground().setAlpha( 0);
            }
        });

    }
    public static void popupSaveDate(final RelativeLayout layout, final Context context){
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
        txtDetail.setText("เพิ่มลงในปฎิทินเรียบร้อยแล้ว");

        final PopupWindow popup = new PopupWindow(popupDelView,margin,height,true);
        popup.setOutsideTouchable(true);
        popup.showAtLocation(popupDelView,Gravity.CENTER,0,0);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layout.getForeground().setAlpha( 0);
                context.startActivity(new Intent(context, CalendarActivity.class));
            }
        });

        new Handler().postDelayed(new Runnable(){
            public void run() {
                layout.getForeground().setAlpha( 0);
                popup.dismiss();
                context.startActivity(new Intent(context, CalendarActivity.class));
            }
        }, 3 *1000);
    }

    public void popupEditOutProfile(final RelativeLayout layout, final Context context ){
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
        int margin = (int) (widthDevice*0.95);
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

    public static void popupSOS(final RelativeLayout layout, final Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layout.getForeground().setAlpha( 220);
        }

        final ArrayList<TelDetail> arrTelDetail = new ArrayList<>();

        LayoutInflater EditNoteInflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = EditNoteInflater.inflate(R.layout.popup_sos,null);

        final ListView listView = popupView.findViewById(R.id.listTel);

        int widthDevice = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        int width = (int) (widthDevice*0.95);

        final PopupWindow popupSOS = new PopupWindow(popupView,width, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupSOS.setOutsideTouchable(true);
        popupSOS.showAtLocation(popupView,Gravity.CENTER,0,0);

        DatabaseReference TelReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://pregnantmother-e8d1f.firebaseio.com/users/"
                        + UserDetail.username + "/tels");

        TelReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                arrTelDetail.clear();

                for(DataSnapshot ds : snapshot.getChildren()) {
                    arrTelDetail.add(new TelDetail(
                            ds.getKey(),
                            ds.child("tel").getValue().toString())
                    );
                }

                SosAdapter sosAdapter = new SosAdapter(arrTelDetail,context);
                listView.setAdapter(sosAdapter);
                sosAdapter.notifyDataSetChanged();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popupSOS.dismiss();
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + arrTelDetail.get(position).getTel()));
                        context.startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });







        popupSOS.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layout.getForeground().setAlpha( 0);
            }
        });

    }

}
