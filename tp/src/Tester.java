import java.util.concurrent.CountDownLatch;

public class Tester {

    public void correrTests() {
        System.out.println("Corriendo Tests:");
        correrTestsParaLocksFinos();
        correrTestsParaOptimista();
        //correrTestsParaSinLocks(); lo comento porque no está implementado todavía
    }

    public void correrTestsParaLocksFinos() {
        ConjuntoConLocksFinos<Integer> c = new ConjuntoConLocksFinos<Integer>();

        System.out.print("[locks finos] ");
        dosHilosAgregando1000Numeros(c);
        c.vaciar();

        System.out.print("[locks finos] ");
        cuatroHilosAgregando1000Numeros(c);
        c.vaciar();
    }

    public void correrTestsParaOptimista() {
        ConjuntoOptimista<Integer> c = new ConjuntoOptimista<Integer>();

        System.out.print("[optimista] ");
        dosHilosAgregando1000Numeros(c);
        c.vaciar();

        System.out.print("[optimista] ");
        cuatroHilosAgregando1000Numeros(c);
        c.vaciar();


    }

    public void correrTestsParaSinLocks() {
        System.out.print("[sin locks] ");
        ConjuntoSinLocks<Integer> c = new ConjuntoSinLocks<Integer>();
        dosHilosAgregando1000Numeros(c);
        c.vaciar();
    }

    //Agregamos 1000 números y creamos un hilo que agrega los pares y otro que agrega los impares
    public void dosHilosAgregando1000Numeros(Conjunto<Integer> c) {
        System.out.print("dosHilosAgregando1000Numeros: ");
        int latchGroupCount = 2;
        CountDownLatch latch = new CountDownLatch(latchGroupCount);
        Thread t1 = new Thread(new HiloAgregadorDePares(c, 0, 999, latch), "T1");
        Thread t2 = new Thread(new HiloAgregadorDeImpares(c, 1, 999, latch), "T2");

        t1.start();
        t2.start();

        Integer[] intArray = new Integer[1000];
        for(Integer i=0; i < intArray.length; i++) {
            intArray[i] = i;
        }
        chequearTerminacion(latch, c, intArray);
    }

    //Agregamos 1000 números y creamos dos hilos que agrega los pares y dos que agregan los impares:
    public void cuatroHilosAgregando1000Numeros(Conjunto<Integer> c) {
        System.out.print("cuatroHilosAgregando1000Numeros: ");

        int latchGroupCount = 4;
        CountDownLatch latch = new CountDownLatch(latchGroupCount);
        Thread t1 = new Thread(new HiloAgregadorDePares(c, 0, 499, latch), "T1");
        Thread t2 = new Thread(new HiloAgregadorDeImpares(c, 1, 499, latch), "T2");
        Thread t3 = new Thread(new HiloAgregadorDePares(c, 500, 999, latch), "T3");
        Thread t4 = new Thread(new HiloAgregadorDeImpares(c, 501, 999, latch), "T4");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        Integer[] intArray = new Integer[1000];
        for(Integer i=0; i < intArray.length; i++) {
            intArray[i] = i;
        }
        chequearTerminacion(latch, c, intArray);
    }

    public void chequearTerminacion(CountDownLatch latch, Conjunto<Integer> c, Integer[] intArray) {
        try {
            latch.await();
            if (c.esEquivalenteA(intArray)) {
                System.out.println("OK");
            } else {
                System.out.println("NOT OK");
            }
        } catch (InterruptedException e) {
            System.out.println("NOT OK " + e.toString());
        }
    }




}
