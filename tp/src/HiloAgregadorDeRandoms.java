import java.util.concurrent.CountDownLatch;
import java.lang.Math;
import java.util.concurrent.ThreadLocalRandom;

public class HiloAgregadorDeRandoms extends Thread {
    Conjunto<Integer> conjunto;

    int veces;
    int cotaInferior;
    int cotaSuperior;
    CountDownLatch latch;


    public HiloAgregadorDeRandoms(Conjunto<Integer> c, int v, int inf, int sup, CountDownLatch l) {
        conjunto = c;
        veces = v;
        cotaInferior = inf;
        cotaSuperior = sup;
        latch = l;
    }

    public void agregarRandoms() {
        for (int i = 1; i <= veces; ++i) {
            int val = cotaSuperior == Integer.MAX_VALUE ? 0 : 1;
            Integer random = ThreadLocalRandom.current().nextInt(cotaInferior, cotaSuperior+val);
            conjunto.agregar(random);
        }
    }

    public void run() {
        agregarRandoms();
        if (latch != null) {latch.countDown();}
    }
}
