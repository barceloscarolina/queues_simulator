public class Gerador {

    private long a;
    private long c;
    private Double m;
    private Double semente;

    public Gerador(long a, long c, Double m, Double semente) {
        this.a = a;
        this.c = c;
        this.m = m;
        this.semente = semente;
    }

    public Double next() {
        semente = (a * semente + c) % m;
        return semente / m;
    }

}

/*
 * // Main só do gerador de pseudocodigo
 * 
 * public void main(String[] args) {
 * long a = 96;
 * long c = 6000;
 * long m = 74644981083L;
 * long seed = 258;
 * 
 * Gerador lcg = new Gerador(a, c, m, seed);
 * for (int i = 0; i < 1000; i++) {
 * long num = lcg.proximo();
 * System.out.println(num);
 * }
 * }
 */
