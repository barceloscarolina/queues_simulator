public class Gerador {

    private long a;
    private long c;
    private long m;
    private long semente;

    public Gerador(long a, long c, long m, long semente) {
        this.a = a;
        this.c = c;
        this.m = m;
        this.semente = semente;
    }

    public long proximo() {
        semente = (a * semente + c) % m;
        return semente;
    }
}
