package pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details;

public class TotalWeekDetail {
    public int totalNote;
    public String week;
    public String totalPdf;

    public TotalWeekDetail(int totalNote, String week, String totalPdf) {
        this.totalNote = totalNote;
        this.week = week;
        this.totalPdf = totalPdf;
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

    public String getTotalPdf() {
        return totalPdf;
    }

    public void setTotalPdf(String totalPdf) {
        this.totalPdf = totalPdf;
    }
}
