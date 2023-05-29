import java.util.concurrent.CountDownLatch;

public class HiloAgregadorDeConsecutivos extends Thread {

    Conjunto<Integer> conjunto;
    Integer desde;
    Integer hasta;
    CountDownLatch latch;


    public HiloAgregadorDeConsecutivos(Conjunto<Integer> c, Integer d, Integer h, CountDownLatch l) {
        conjunto = c;
        desde = d;
        hasta = h;
        latch = l;
    }

    public void agregarConsecutivos() {
        for (Integer i = desde; i <= hasta; ++i) {
            conjunto.agregar(i);
        }
    }

    public void run() {
        agregarConsecutivos();
        if (latch != null) {latch.countDown();}
    }
}
