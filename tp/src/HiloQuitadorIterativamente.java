import java.util.concurrent.CountDownLatch;

public class HiloQuitadorIterativamente extends Thread {

    Conjunto<Integer> conjunto;
    Integer numeroAQuitar;
    Integer vecesAQuitar;
    CountDownLatch latch;


    public HiloQuitadorIterativamente(Conjunto<Integer> c, Integer n, Integer v, CountDownLatch l) {
        conjunto = c;
        numeroAQuitar = n;
        vecesAQuitar = v;
        latch = l;
    }

    public void quitar() {
        for (Integer i = 0; i <= vecesAQuitar; i++) {
            conjunto.quitar(numeroAQuitar);
        }
    }

    public void run() {
        quitar();
        if (latch != null) {latch.countDown();}
    }
}
