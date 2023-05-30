import java.util.concurrent.CountDownLatch;
import java.lang.Math;
import java.util.concurrent.ThreadLocalRandom;

public class HiloPerteneceRandoms extends Thread {
    Conjunto<Integer> conjunto;

    int veces;
    int cotaInferior;
    int cotaSuperior;
    CountDownLatch latch;


    public HiloPerteneceRandoms(Conjunto<Integer> c, int v, int inf, int sup, CountDownLatch l) {
        conjunto = c;
        veces = v;
        cotaInferior = inf;
        cotaSuperior = sup;
        latch = l;
    }

    public void perteneceRandoms() {
        for (int i = 1; i <= veces; ++i) {
            int val = cotaSuperior == Integer.MAX_VALUE ? 0 : 1;
            Integer random = ThreadLocalRandom.current().nextInt(cotaInferior, cotaSuperior+val);
            conjunto.pertenece(random);
        }
    }

    public void run() {
        perteneceRandoms();
        if (latch != null) {latch.countDown();}
    }
}
