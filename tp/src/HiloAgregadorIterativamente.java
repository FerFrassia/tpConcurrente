import java.util.concurrent.CountDownLatch;

public class HiloAgregadorIterativamente extends Thread {

    Conjunto<Integer> conjunto;
    Integer numeroAAgregar;
    Integer vecesAAgregar;
    CountDownLatch latch;


    public HiloAgregadorIterativamente(Conjunto<Integer> c, Integer n, Integer v, CountDownLatch l) {
        conjunto = c;
        numeroAAgregar = n;
        vecesAAgregar = v;
        latch = l;
    }

    public void agregar() {
        for (Integer i = 0; i <= vecesAAgregar; i++) {
            conjunto.agregar(numeroAAgregar);
        }
    }

    public void run() {
        agregar();
        if (latch != null) {latch.countDown();}
    }
}
