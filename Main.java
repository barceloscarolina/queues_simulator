public class Main {

    public static void main(String args[]) {

        // Queue Parameters
        int k_ = 5; // capacity
        int c_ = 1; // servers
        // arrivals between 2 and 5 units of time
        int dist_arrival_i_ = 2;
        int dist_arrival_e_ = 5;
        // exits (services) between 3 and 5 units of time
        int dist_exit_i_ = 3;
        int dist_exit_e_ = 5;

        Queue queue_1 = new Queue(k_, c_, dist_arrival_i_, dist_arrival_e_, dist_exit_i_, dist_exit_e_);

        queue_1.Simulation();

    }

}
