import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class QueueTandem {

    // Capacidade e servidores de cada fila
    private final int k1_, c1_;
    private final int k2_, c2_;

    // Distribuições de tempo
    private final int dist_arrival_i_, dist_arrival_e_; // chegada externa
    private final int dist1_exit_i_, dist1_exit_e_;     // atendimento fila1
    private final int dist2_exit_i_, dist2_exit_e_;     // atendimento fila2

    private static int random_count_ = 100000;
    private static double global_time_ = 0.0;

    // Status e contadores
    private int queue1_status_ = 0;
    private int queue2_status_ = 0;
    private int loss1_ = 0;
    private int loss2_ = 0;

    private final Double[] q1_times_;
    private final Double[] q2_times_;

    private static final Gerador lcg =
            new Gerador(96, 6000, 74644981083.0, 258.0);
    private static final ArrayList<Event> scheduler = new ArrayList<>();

    public QueueTandem(
            int k1, int c1,
            int k2, int c2,
            int dist_arrival_i, int dist_arrival_e,
            int dist1_exit_i, int dist1_exit_e,
            int dist2_exit_i, int dist2_exit_e) {

        this.k1_ = k1;  this.c1_ = c1;
        this.k2_ = k2;  this.c2_ = c2;
        this.dist_arrival_i_ = dist_arrival_i;
        this.dist_arrival_e_ = dist_arrival_e;
        this.dist1_exit_i_ = dist1_exit_i;
        this.dist1_exit_e_ = dist1_exit_e;
        this.dist2_exit_i_ = dist2_exit_i;
        this.dist2_exit_e_ = dist2_exit_e;

        q1_times_ = new Double[k1_ + 1];
        q2_times_ = new Double[k2_ + 1];
        for (int i = 0; i <= k1_; i++) q1_times_[i] = 0.0;
        for (int i = 0; i <= k2_; i++) q2_times_[i] = 0.0;
    }

    // -------- Simulação principal ----------
    public void Simulation() {
        // primeira chegada
        scheduleArrival(2.0);

        while (random_count_ > 0 && !scheduler.isEmpty()) {
            Collections.sort(scheduler, Comparator.comparingDouble(Event::getTime_scheduled));
            Event ev = scheduler.remove(0);
            double elapsed = ev.getTime_scheduled() - global_time_;
            timeAdd(elapsed);

            switch (ev.getType()) {
                case "A": arrival(); break;
                case "P": passage(); break;
                case "E": exit(); break;
            }
        }

        // Resultados
        System.out.println("--- Fila 1 ---");
        for (double t : q1_times_) System.out.println(t);
        System.out.println("Perdas F1: " + loss1_);

        System.out.println("--- Fila 2 ---");
        for (double t : q2_times_) System.out.println(t);
        System.out.println("Perdas F2: " + loss2_);

        System.out.println("Tempo total simulado: " + global_time_);
    }

    // -------- Eventos ----------
    private void arrival() {
        if (queue1_status_ < k1_) {
            queue1_status_++;
            if (queue1_status_ <= c1_) schedulePassage();
        } else {
            loss1_++;
        }
        scheduleArrival(global_time_);
    }

    private void passage() {
        // saída da fila1
        queue1_status_--;
        if (queue1_status_ >= c1_) schedulePassage();

        // tentativa de entrada na fila2
        if (queue2_status_ < k2_) {
            queue2_status_++;
            if (queue2_status_ <= c2_) scheduleExit();
        } else {
            loss2_++;
        }
    }

    private void exit() {
        queue2_status_--;
        if (queue2_status_ >= c2_) scheduleExit();
    }

    // -------- Agendadores ----------
    private void scheduleArrival(double baseTime) {
        double dt = dist_arrival_i_
                + (dist_arrival_e_ - dist_arrival_i_) * lcg.next();
        random_count_--;
        scheduler.add(new Event("A", baseTime + dt));
    }

    private void schedulePassage() {
        double dt = dist1_exit_i_ + (dist1_exit_e_ - dist1_exit_i_) * lcg.next();
        random_count_--;
        scheduler.add(new Event("P", global_time_ + dt));
    }

    private void scheduleExit() {
        double dt = dist2_exit_i_ + (dist2_exit_e_ - dist2_exit_i_) * lcg.next();
        random_count_--;
        scheduler.add(new Event("E", global_time_ + dt));
    }

    // -------- Atualização de tempo ----------
    private void timeAdd(double elapsed) {
        q1_times_[queue1_status_] += elapsed;
        q2_times_[queue2_status_] += elapsed;
        global_time_ += elapsed;
    }
}
