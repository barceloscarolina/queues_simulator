import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.namespace.QName;

public class Queue {

    // Queue Parameters
    private int k_; // capacity
    private int c_; // servers
    // arrivals between 2 and 5 units of time
    private int dist_arrival_i_;
    private int dist_arrival_e_;
    // exits (services) between 3 and 5 units of time
    private int dist_exit_i_;
    private int dist_exit_e_;

    // end of simulation criterea = on using 100.000 pseudo-random numbers
    private static int random_count_ = 100000;

    // inicial status
    private static Double global_time_ = 0.0;
    private int count_losses_ = 0;
    private int queue_status_ = 0;
    private Double[] queue_status_times;

    private static Gerador lcg = new Gerador(96, 6000, 74644981083.0, 258.0);
    private static ArrayList<Event> scheduler = new ArrayList<>();

    public Queue(int k, int c, int dist_arrival_i_, int dist_arrival_e_, int dist_exit_i_, int dist_exit_e_) {

        this.k_ = k;
        this.c_ = c;
        this.dist_arrival_i_ = dist_arrival_i_;
        this.dist_arrival_e_ = dist_arrival_e_;
        this.dist_exit_i_ = dist_exit_i_;
        this.dist_exit_e_ = dist_exit_e_;

        queue_status_times = new Double[k_ + 1];

        // inicialize the array with 0
        {
            for (int i = 0; i < queue_status_times.length; i++) {
                queue_status_times[i] = 0.0;
            }
        }

    }

    public void Simulation() {

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

    public void Arrival(Double elapsed_time) {

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

    public void Exit(Double elapsed_time) {

        timeAdd(elapsed_time);

        queue_status_--;

        if (queue_status_ >= c_) {
            newExitSched();
        }

    }

    public void timeAdd(Double elapsed_time) {

        // add the time in the actual status
        queue_status_times[queue_status_] = queue_status_times[queue_status_] + elapsed_time;

        global_time_ = global_time_ + elapsed_time;

    }

    public void newExitSched() {

        Double random_time = dist_exit_i_ + ((dist_exit_e_ - dist_exit_i_) * lcg.next());
        random_count_--;

        // System.out.println(global_time_);

        Event new_exit = new Event("E", global_time_ + random_time);

        scheduler.add(new_exit);

    }

    public void newArrivalSched() {

        Double random_time = dist_arrival_i_
                + ((dist_arrival_e_ - dist_arrival_i_) * lcg.next());
        random_count_--;

        // System.out.println(global_time_);

        Event new_arrival = new Event("A", global_time_ + random_time);

        scheduler.add(new_arrival);

    }

}
