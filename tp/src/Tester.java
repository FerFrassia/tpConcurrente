import java.util.concurrent.CountDownLatch;

public class Tester {

    public void correrTests() {
        System.out.println("Corriendo Tests:");
        correrTestsParaLocksFinos();
        System.out.println();
        correrTestsParaOptimista();
        System.out.println();
        //correrTestsParaSinLocks(); lo comento porque no está implementado todavía
    }

    public void correrTestsParaLocksFinos() {
        ConjuntoConLocksFinos<Integer> c = new ConjuntoConLocksFinos<Integer>();

        System.out.print("[locks finos] ");
        dosHilosAgregando1000Numeros(c);
        
        System.out.print("[locks finos] ");
        dosHilosQuitando1000Numeros(c);

        System.out.print("[locks finos] ");
        cuatroHilosAgregando1000Numeros(c);
        
        System.out.print("[locks finos] ");
        cuatroHilosQuitando1000Numeros(c);   

        System.out.print("[locks finos] ");
        testearHilosAgregandoYQuitandoCuatroYSeis(c);    
    }

    public void correrTestsParaOptimista() {
        ConjuntoOptimista<Integer> c = new ConjuntoOptimista<Integer>();

        System.out.print("[optimista] ");
        dosHilosAgregando1000Numeros(c);
        
        System.out.print("[optimista] ");
        dosHilosQuitando1000Numeros(c);

        System.out.print("[optimista] ");
        cuatroHilosAgregando1000Numeros(c);
        
        System.out.print("[optimista] ");
        cuatroHilosQuitando1000Numeros(c);
        
        System.out.print("[optimista] ");
        testearHilosAgregandoYQuitandoCuatroYSeis(c);  
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

    //En un conjunto con 1000 números creamos un hilo que quita los pares y otro que quita los impares
    public void dosHilosQuitando1000Numeros(Conjunto<Integer> c) {
        System.out.print("dosHilosQuitando1000Numeros: ");
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
    public void cuatroHilosQuitando1000Numeros(Conjunto<Integer> c) {
        System.out.print("cuatroHilosQuitando1000Numeros: ");

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
    }


    public void testearHilosAgregandoYQuitandoCuatroYSeis(Conjunto<Integer> c) {
        //agregamos del 1 al 10 para tener un conjunto inicial donde podamos quitar elementos del medio
        for(int i=1; i<=10; i++) {
            c.agregar(i);
        }

        hilosAgregandoYQuitandoCuatroYSeis(c);
    }

    //Hilos que agregan y quitan 4 y 6 mil veces.
    public void hilosAgregandoYQuitandoCuatroYSeis(Conjunto<Integer> c) {
        System.out.print("hilosAgregandoYQuitandoCuatroYSeis: ");

        int latchGroupCount = 4;
        CountDownLatch latch = new CountDownLatch(latchGroupCount);
        Thread t1 = new Thread(new HiloAgregadorIterativamente(c, 4, 1000, latch), "T1");
        Thread t2 = new Thread(new HiloAgregadorIterativamente(c, 6, 1000, latch), "T2");
        Thread t3 = new Thread(new HiloQuitadorIterativamente(c, 4, 1000, latch), "T3");
        Thread t4 = new Thread(new HiloQuitadorIterativamente(c, 6, 1000, latch), "T4");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            latch.await();
            if(c.pertenece(5)){
                System.out.println("OK");
            } else {
                System.out.println("NOT OK");
            }
        } catch (InterruptedException e) {
            System.out.println("NOT OK " + e.toString());
        }
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
