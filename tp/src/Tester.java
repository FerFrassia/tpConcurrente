import java.util.concurrent.CountDownLatch;

public class Tester {

    public void correrTests() {
        System.out.println("Corriendo Tests:");
        correrTestsParaLocksFinos();
        System.out.println();
        correrTestsParaOptimista();
        System.out.println();
        correrTestsParaSinLocks();
    }

    public void correrTestsParaLocksFinos() {
        correrTestsParaConjunto(new ConjuntoConLocksFinos<Integer>(), "[locks finos] ");
    }

    public void correrTestsParaOptimista() {
        correrTestsParaConjunto(new ConjuntoOptimista<Integer>(), "[optimista] ");
    }

    public void correrTestsParaSinLocks() {
        correrTestsParaConjunto(new ConjuntoSinLocks<>(), "[sin locks] ");
    }

    public void correrTestsParaConjunto(Conjunto<Integer> c, String mensajeDeSalida) {
        unHiloAgregando1000Numeros(c, mensajeDeSalida);
        unHiloSacando1000Numeros(c, mensajeDeSalida);
        dosHilosAgregando1000Numeros(c, mensajeDeSalida);
        dosHilosQuitando1000Numeros(c, mensajeDeSalida);
        cuatroHilosAgregando1000Numeros(c, mensajeDeSalida);
        cuatroHilosQuitando1000Numeros(c, mensajeDeSalida);
        testearHilosAgregandoYQuitandoCuatroYSeis(c, mensajeDeSalida);
    }

    public void unHiloAgregando1000Numeros(Conjunto<Integer> c, String mensajeDeSalida) {
        System.out.print(mensajeDeSalida + "unHiloAgregando1000Numeros: ");
        Integer[] intArray = new Integer[1000];
        for (Integer i = 0; i < 1000; i = i + 1) {
            c.agregar(i);
            intArray[i] = i;
        }

        System.out.println(c.esEquivalenteA(intArray) ? "OK" : "NOT OK");
        c.vaciar();
    }

    public void unHiloSacando1000Numeros(Conjunto<Integer> c, String mensajeDeSalida) {
        System.out.print(mensajeDeSalida + "unHiloSacando1000Numeros: ");
        Integer[] intArray = new Integer[1000];
        for (Integer i = 0; i < 1000; i = i + 1) {
            c.agregar(i);
            intArray[i] = i;
        }

        if (c.esEquivalenteA(intArray)) {
            for (Integer i = 0; i < 1000; i = i + 1) {
                c.quitar(i);
            }

            intArray = new Integer[]{};
            System.out.println(c.esEquivalenteA(intArray) ? "OK" : "NOT OK");
        } else {
            System.out.println("NOT OK");
        }

        c.vaciar();
    }

    //Agregamos 1000 números y creamos un hilo que agrega los pares y otro que agrega los impares
    public void dosHilosAgregando1000Numeros(Conjunto<Integer> c, String mensajeDeSalida) {
        System.out.print(mensajeDeSalida + "dosHilosAgregando1000Numeros: ");
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
    public void cuatroHilosAgregando1000Numeros(Conjunto<Integer> c, String mensajeDeSalida) {
        System.out.print(mensajeDeSalida + "cuatroHilosAgregando1000Numeros: ");

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
        c.vaciar();
    }

    //En un conjunto con 1000 números creamos un hilo que quita los pares y otro que quita los impares
    public void dosHilosQuitando1000Numeros(Conjunto<Integer> c, String mensajeDeSalida) {
        System.out.print(mensajeDeSalida + "dosHilosQuitando1000Numeros: ");
        int latchGroupCount = 2;
        CountDownLatch latch = new CountDownLatch(latchGroupCount);
        Thread t1 = new Thread(new HiloQuitadorDePares(c, 0, 999, latch), "T1");
        Thread t2 = new Thread(new HiloQuitadorDeImpares(c, 1, 999, latch), "T2");

        t1.start();
        t2.start();

        Integer[] intArray = new Integer[0];
        chequearTerminacion(latch, c, intArray);
    }

    //En un conjunto con 1000 números creamos dos hilos que quitan los pares y otros dos que quitan los impares
    public void cuatroHilosQuitando1000Numeros(Conjunto<Integer> c, String mensajeDeSalida) {
        System.out.print(mensajeDeSalida + "cuatroHilosQuitando1000Numeros: ");

        int latchGroupCount = 4;
        CountDownLatch latch = new CountDownLatch(latchGroupCount);
        Thread t1 = new Thread(new HiloQuitadorDePares(c, 0, 499, latch), "T1");
        Thread t2 = new Thread(new HiloQuitadorDeImpares(c, 1, 499, latch), "T2");
        Thread t3 = new Thread(new HiloQuitadorDePares(c, 500, 999, latch), "T3");
        Thread t4 = new Thread(new HiloQuitadorDeImpares(c, 501, 999, latch), "T4");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        Integer[] intArray = new Integer[0];
        chequearTerminacion(latch, c, intArray);
        c.vaciar();
    }

    public void testearHilosAgregandoYQuitandoCuatroYSeis(Conjunto<Integer> c, String mensajeDeSalida) {
        //agregamos del 1 al 10 para tener un conjunto inicial donde podamos quitar elementos del medio
        Integer[] intArray = new Integer[10];
        for(int i=1; i<=10; i++) {
            c.agregar(i);
            intArray[i-1] = i;
        }

        if (c.esEquivalenteA(intArray)) {
            hilosAgregandoYQuitandoCuatroYSeis(c, mensajeDeSalida);
        } else {
            System.out.println("ERROR: no se pudo hacer el test");
        }
    }

    //Hilos que agregan y quitan 4 y 6 mil veces.
    public void hilosAgregandoYQuitandoCuatroYSeis(Conjunto<Integer> c, String mensajeDeSalida) {
        System.out.print(mensajeDeSalida + "hilosAgregandoYQuitandoCuatroYSeis: ");

        int latchGroupCount = 4;
        CountDownLatch latch = new CountDownLatch(latchGroupCount);
        Thread t1 = new Thread(new HiloAgregadorIterativamente(c, 4, 2, latch), "T1");
        Thread t2 = new Thread(new HiloAgregadorIterativamente(c, 6, 2, latch), "T2");
        Thread t3 = new Thread(new HiloQuitadorIterativamente(c, 4, 2, latch), "T3");
        Thread t4 = new Thread(new HiloQuitadorIterativamente(c, 6, 2, latch), "T4");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            latch.await();
            if(c.pertenece(5)){
                System.out.println("OK");
            } else {
                System.out.print("NOT OK, el 5 no está presente: ");
                c.print();
            }
        } catch (InterruptedException e) {
            System.out.println("NOT OK: " + e.toString());
        }
    }

    public void chequearTerminacion(CountDownLatch latch, Conjunto<Integer> c, Integer[] intArray) {
        try {
            latch.await();
            System.out.println(c.esEquivalenteA(intArray) ? "OK" : "NOT OK");
        } catch (InterruptedException e) {
            System.out.println("EXCEPTION:  " + e.toString());
        }
    }




}
