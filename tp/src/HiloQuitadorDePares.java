import java.util.concurrent.CountDownLatch;

public class HiloQuitadorDePares extends Thread {

    Conjunto<Integer> conjunto;
    Integer desde;
    Integer hasta;
    CountDownLatch latch;


    public HiloQuitadorDePares(Conjunto<Integer> c, Integer d, Integer h, CountDownLatch l) {
        conjunto = c;
        desde = d;
        hasta = h;
        latch = l;
    }

    public void quitarPares() {
        for (Integer i = desde; i <= hasta; i = i + 2) {
            conjunto.quitar(i);
        }
    }

    public void run() {
        quitarPares();
        if (latch != null) {latch.countDown();}
    }
}
