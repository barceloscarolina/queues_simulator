import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main {

    // Queue Parameters
    private static int k_ = 5; // capacity
    private static int c_ = 1; // servers
    // arrivals between 2 and 5 units of time
    private static int dist_arrival_i_ = 2;
    private static int dist_arrival_e_ = 5;
    // exits (services) between 3 and 5 units of time
    private static int dist_exit_i_ = 3;
    private static int dist_exit_e_ = 5;

    // end of simulation criterea = on using 100.000 pseudo-random numbers
    private static int random_count_ = 100000;

    // inicial status
    private static Double global_time_ = 0.0;
    private static int count_losses_ = 0;
    private static int queue_status_ = 0;
    private static Double[] queue_status_times = new Double[k_ + 1];

    // inicialize the array with 0
    static {
        for (int i = 0; i < queue_status_times.length; i++) {
            queue_status_times[i] = 0.0;
        }
    }

    private static Gerador lcg = new Gerador(96, 6000, 74644981083.0, 258.0);
    private static ArrayList<Event> scheduler = new ArrayList<>();

    public static void main(String[] args) {

        // first arrival
        Arrival(2.0);

        while (random_count_ > 0) {

            // sort scheduler by time
            Collections.sort(scheduler, Comparator.comparingDouble(Event::getTime_scheduled));

            Double elapsed_time = scheduler.get(0).getTime_scheduled() - global_time_;

            if (scheduler.get(0).getType() == "A") {
                Arrival(elapsed_time);
            } else
                Exit(elapsed_time);

            scheduler.remove(0);

        }

        for (int i = 0; i < queue_status_times.length; i++) {
            System.out.println(queue_status_times[i]);
        }
        System.out.println("global_time " + global_time_);
        System.out.println("count_losses  " + count_losses_);
    }

    public static void Arrival(Double elapsed_time) {

        timeAdd(elapsed_time);

        if (queue_status_ < k_) {
            queue_status_++;
            if (queue_status_ <= c_) {
                newExitSched();
            }

        } else
            count_losses_++;

        newArrivalSched();

    }

    public static void Exit(Double elapsed_time) {

        timeAdd(elapsed_time);

        queue_status_--;

        if (queue_status_ >= c_) {
            newExitSched();
        }

    }

    public static void timeAdd(Double elapsed_time) {

        // add the time in the actual status
        queue_status_times[queue_status_] = queue_status_times[queue_status_] + elapsed_time;

        global_time_ = global_time_ + elapsed_time;

    }

    public static void newExitSched() {

        Double random_time = dist_exit_i_ + ((dist_exit_e_ - dist_exit_i_) * lcg.next());
        random_count_--;

        // System.out.println(global_time_);

        Event new_exit = new Event("E", global_time_ + random_time);

        scheduler.add(new_exit);

    }

    public static void newArrivalSched() {

        Double random_time = dist_arrival_i_
                + ((dist_arrival_e_ - dist_arrival_i_) * lcg.next());
        random_count_--;

        // System.out.println(global_time_);

        Event new_arrival = new Event("A", global_time_ + random_time);

        scheduler.add(new_arrival);

    }

}
