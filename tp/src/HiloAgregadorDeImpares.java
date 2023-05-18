import java.util.concurrent.CountDownLatch;

public class HiloAgregadorDeImpares extends Thread {
    Conjunto<Integer> conjunto;
    Integer desde;
    Integer hasta;
    CountDownLatch latch;


    public HiloAgregadorDeImpares(Conjunto<Integer> c, Integer d, Integer h, CountDownLatch l) {
        conjunto = c;
        desde = d;
        hasta = h;
        latch = l;
    }

    public void agregarImpares() {
        for (Integer i = desde; i <= hasta; i = i + 2) {
            conjunto.agregar(i);
        }
    }

    public void run() {
        agregarImpares();
        if (latch != null) {latch.countDown();}
    }
}
