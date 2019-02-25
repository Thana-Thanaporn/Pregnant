package pudpongsai.thanaporn.th.ac.su.reg.pregnant.Details;



public class EventDetail {
    private String topic;
    private String place;
    private String detail;
    private long date;

    public EventDetail(String topic, String place, String detail, long date) {
        this.topic = topic;
        this.place = place;
        this.detail = detail;
        this.date = date;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
