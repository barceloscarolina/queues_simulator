public class Main {
    public static void main(String[] args) {
        long a = 96;
        long c = 6000;
        long m = 74644981083L;
        long seed = 258;

        Gerador lcg = new Gerador(a, c, m, seed);
        for (int i = 0; i < 1000; i++) {
            long num = lcg.proximo();
            System.out.println(num);
        }
    }
}