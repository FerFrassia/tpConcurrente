import java.util.concurrent.CountDownLatch;

public class HiloAgregadorDePares extends Thread {

    Conjunto<Integer> conjunto;
    Integer desde;
    Integer hasta;
    CountDownLatch latch;


    public HiloAgregadorDePares(Conjunto<Integer> c, Integer d, Integer h, CountDownLatch l) {
        conjunto = c;
        desde = d;
        hasta = h;
        latch = l;
    }

    public void agregarPares() {
        for (Integer i = desde; i <= hasta; i = i + 2) {
            conjunto.agregar(i);
        }
    }

    public void run() {
        agregarPares();
        if (latch != null) {latch.countDown();}
    }
}
