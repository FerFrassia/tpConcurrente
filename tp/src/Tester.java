import java.util.concurrent.CountDownLatch;

public class Tester {

    boolean print_tiempo_tests = true;
    int cantidad_elementos = 10000;

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
        unHiloAgregandoCantidadElementosNumeros(c, mensajeDeSalida);
        unHiloSacandoCantidadElementosNumeros(c, mensajeDeSalida);
        dosHilosAgregandoCantidadElementosNumeros(c, mensajeDeSalida);
        dosHilosQuitandoCantidadElementosNumeros(c, mensajeDeSalida);
        cuatroHilosAgregandoCantidadElementosNumeros(c, mensajeDeSalida);
        cuatroHilosQuitandoCantidadElementosNumeros(c, mensajeDeSalida);
        testearHilosAgregandoYQuitandoCuatroYSeis(c, mensajeDeSalida);
    }

    public void unHiloAgregandoCantidadElementosNumeros(Conjunto<Integer> c, String mensajeDeSalida) {
        System.out.print(mensajeDeSalida + "unHiloAgregando" + cantidad_elementos  +"Numeros: ");
        long startTime = System.currentTimeMillis();
        Integer[] intArray = new Integer[cantidad_elementos];
        for (Integer i = 0; i < cantidad_elementos; i = i + 1) {
            c.agregar(i);
            intArray[i] = i;
        }
        System.out.println(c.esEquivalenteA(intArray) ? "OK" : "NOT OK");
        print_tiempo_tardado(startTime);
        c.vaciar();
    }

    public void unHiloSacandoCantidadElementosNumeros(Conjunto<Integer> c, String mensajeDeSalida) {
        System.out.print(mensajeDeSalida + "unHiloSacando" + cantidad_elementos  +"Numeros: ");
        long startTime = System.currentTimeMillis();
        Integer[] intArray = new Integer[cantidad_elementos];
        for (Integer i = 0; i < cantidad_elementos; i = i + 1) {
            c.agregar(i);
            intArray[i] = i;
        }

        if (c.esEquivalenteA(intArray)) {
            for (Integer i = 0; i < cantidad_elementos; i = i + 1) {
                c.quitar(i);
            }

            intArray = new Integer[]{};
            System.out.println(c.esEquivalenteA(intArray) ? "OK" : "NOT OK");
        } else {
            System.out.println("NOT OK");
        }
        print_tiempo_tardado(startTime);
        c.vaciar();
    }

    //Agregamos cantidad_elementos números y creamos un hilo que agrega los pares y otro que agrega los impares
    public void dosHilosAgregandoCantidadElementosNumeros(Conjunto<Integer> c, String mensajeDeSalida) {
        System.out.print(mensajeDeSalida + "dosHilosAgregando" + cantidad_elementos  +"Numeros: ");
        long startTime = System.currentTimeMillis();
        int latchGroupCount = 2;
        CountDownLatch latch = new CountDownLatch(latchGroupCount);
        Thread t1 = new Thread(new HiloAgregadorDePares(c, 0, cantidad_elementos-1, latch), "T1");
        Thread t2 = new Thread(new HiloAgregadorDeImpares(c, 1, cantidad_elementos-1, latch), "T2");

        t1.start();
        t2.start();

        Integer[] intArray = new Integer[cantidad_elementos];
        for(Integer i=0; i < intArray.length; i++) {
            intArray[i] = i;
        }
        chequearTerminacion(latch, c, intArray);
        print_tiempo_tardado(startTime);

    }

    //Agregamos cantidad_elementos números y creamos dos hilos que agrega los pares y dos que agregan los impares:
    public void cuatroHilosAgregandoCantidadElementosNumeros(Conjunto<Integer> c, String mensajeDeSalida) {
        System.out.print(mensajeDeSalida + "cuatroHilosAgregando" + cantidad_elementos  +"Numeros: ");
        long startTime = System.currentTimeMillis();
        int latchGroupCount = 4;
        CountDownLatch latch = new CountDownLatch(latchGroupCount);
        Thread t1 = new Thread(new HiloAgregadorDePares(c, 0, (cantidad_elementos/2-1), latch), "T1");
        Thread t2 = new Thread(new HiloAgregadorDeImpares(c, 1, (cantidad_elementos/2-1), latch), "T2");
        Thread t3 = new Thread(new HiloAgregadorDePares(c, (cantidad_elementos/2), (cantidad_elementos-1), latch), "T3");
        Thread t4 = new Thread(new HiloAgregadorDeImpares(c, (cantidad_elementos/2+1), (cantidad_elementos-1), latch), "T4");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        Integer[] intArray = new Integer[cantidad_elementos];
        for(Integer i=0; i < intArray.length; i++) {
            intArray[i] = i;
        }
        chequearTerminacion(latch, c, intArray);
        print_tiempo_tardado(startTime);
        c.vaciar();
    }

    //En un conjunto con cantidad_elementos números creamos un hilo que quita los pares y otro que quita los impares
    public void dosHilosQuitandoCantidadElementosNumeros(Conjunto<Integer> c, String mensajeDeSalida) {
        System.out.print(mensajeDeSalida + "dosHilosQuitando" + cantidad_elementos  +"Numeros: ");
        long startTime = System.currentTimeMillis();
        int latchGroupCount = 2;
        CountDownLatch latch = new CountDownLatch(latchGroupCount);
        Thread t1 = new Thread(new HiloQuitadorDePares(c, 0, cantidad_elementos-1, latch), "T1");
        Thread t2 = new Thread(new HiloQuitadorDeImpares(c, 1, cantidad_elementos-1, latch), "T2");

        t1.start();
        t2.start();

        Integer[] intArray = new Integer[0];
        chequearTerminacion(latch, c, intArray);
        print_tiempo_tardado(startTime);
    }

    //En un conjunto con cantidad_elementos números creamos dos hilos que quitan los pares y otros dos que quitan los impares
    public void cuatroHilosQuitandoCantidadElementosNumeros(Conjunto<Integer> c, String mensajeDeSalida) {
        System.out.print(mensajeDeSalida + "cuatroHilosQuitando" + cantidad_elementos  +"Numeros: ");
        long startTime = System.currentTimeMillis();
        int latchGroupCount = 4;
        CountDownLatch latch = new CountDownLatch(latchGroupCount);
        Thread t1 = new Thread(new HiloQuitadorDePares(c, 0, (cantidad_elementos/2-1), latch), "T1");
        Thread t2 = new Thread(new HiloQuitadorDeImpares(c, 1, (cantidad_elementos/2-1), latch), "T2");
        Thread t3 = new Thread(new HiloQuitadorDePares(c, (cantidad_elementos/2), (cantidad_elementos-1), latch), "T3");
        Thread t4 = new Thread(new HiloQuitadorDeImpares(c, (cantidad_elementos/2+1), (cantidad_elementos-1), latch), "T4");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        Integer[] intArray = new Integer[0];
        chequearTerminacion(latch, c, intArray);
        print_tiempo_tardado(startTime);
        c.vaciar();
    }

    public void testearHilosAgregandoYQuitandoCuatroYSeis(Conjunto<Integer> c, String mensajeDeSalida) {
        //agregamos del 1 al 10 para tener un conjunto inicial donde podamos quitar elementos del medio
        Integer[] intArray = new Integer[10];
        long startTime = System.currentTimeMillis();
        for(int i=1; i<=10; i++) {
            c.agregar(i);
            intArray[i-1] = i;
        }

        if (c.esEquivalenteA(intArray)) {
            hilosAgregandoYQuitandoCuatroYSeis(c, mensajeDeSalida);
            print_tiempo_tardado(startTime);
        } else {
            System.out.println("ERROR: no se pudo hacer el test");
        }
    }

    //Hilos que agregan y quitan 4 y 6 muchas veces.
    public void hilosAgregandoYQuitandoCuatroYSeis(Conjunto<Integer> c, String mensajeDeSalida) {
        System.out.print(mensajeDeSalida + "hilosAgregandoYQuitandoCuatroYSeis: ");

        int latchGroupCount = 4;
        CountDownLatch latch = new CountDownLatch(latchGroupCount);
        Thread t1 = new Thread(new HiloAgregadorIterativamente(c, 4, cantidad_elementos, latch), "T1");
        Thread t2 = new Thread(new HiloAgregadorIterativamente(c, 6, cantidad_elementos, latch), "T2");
        Thread t3 = new Thread(new HiloQuitadorIterativamente(c, 4, cantidad_elementos, latch), "T3");
        Thread t4 = new Thread(new HiloQuitadorIterativamente(c, 6, cantidad_elementos, latch), "T4");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            latch.await();
            if(c.pertenece(5)){
                System.out.println("OK");
            } else {
                System.out.println("NOT OK, el 5 no está presente: ");
                c.print();
            }
        } catch (InterruptedException e) {
            System.out.println("NOT OK: " + e.toString());
        }
    }

    public void chequearTerminacion(CountDownLatch latch, Conjunto<Integer> c, Integer[] intArray) {
        try {
            latch.await();
            if(c.esEquivalenteA(intArray)){
                System.out.println("OK");
            } else {
                System.out.println("NOT OK");
            }
        } catch (InterruptedException e) {
            System.out.println("EXCEPTION:  " + e.toString());
        }
    }

    private void print_tiempo_tardado(long startTime) {
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        if(print_tiempo_tests) System.out.println(" ---Tiempo de test: " + elapsedTime + " milisegundos---");
    }


}
