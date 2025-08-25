public class Event {

    private Double time_scheduled;
    private String type; // A - arrival E - exit

    public Event(String type, Double time_scheduled) {

        this.type = type;
        this.time_scheduled = time_scheduled;

    }

    public String getType() {
        return type;
    }

    public Double getTime_scheduled() {
        return time_scheduled;
    }

}