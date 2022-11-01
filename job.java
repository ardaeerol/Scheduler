public class job {
    int arrivalTime;
    int id;
    int duration;
    String priority;

    public job(int arrivalTime, int id,String priority, int duration ) {
        this.arrivalTime = arrivalTime;
        this.id = id;
        this.duration = duration;
        this.priority = priority;
    }
}