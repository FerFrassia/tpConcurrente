import java.util.concurrent.CountDownLatch;

public class HiloQuitadorDeImpares extends Thread {
    Conjunto<Integer> conjunto;
    Integer desde;
    Integer hasta;
    CountDownLatch latch;


    public HiloQuitadorDeImpares(Conjunto<Integer> c, Integer d, Integer h, CountDownLatch l) {
        conjunto = c;
        desde = d;
        hasta = h;
        latch = l;
    }

    public void quitarImpares() {
        for (Integer i = desde; i <= hasta; i = i + 2) {
            conjunto.quitar(i);
        }
    }

    public void run() {
        quitarImpares();
        if (latch != null) {latch.countDown();}
    }
}
