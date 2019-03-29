package pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details;

public class TotalWeekDetail {
    public int totalNote;
    public String week;
    public boolean status;

    public TotalWeekDetail(int totalNote, String week, boolean status) {
        this.totalNote = totalNote;
        this.week = week;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getTotalNote() {
        return totalNote;
    }

    public void setTotalNote(int totalNote) {
        this.totalNote = totalNote;
    }
}
