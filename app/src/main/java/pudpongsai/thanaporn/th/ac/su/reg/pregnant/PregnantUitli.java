package pudpongsai.thanaporn.th.ac.su.reg.pregnant;

import java.util.Calendar;

import pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details.UserDetail;



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
}
