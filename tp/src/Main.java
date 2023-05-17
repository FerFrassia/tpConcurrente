public class Main {
    public static void main(String[] args) {
        ConjuntoConLocksFinos<Integer> c1 = new ConjuntoConLocksFinos<Integer>();
        c1.agregar(1);
        c1.agregar(2);
        c1.agregar(3);
        c1.agregar(4);
        c1.quitar(1);
        c1.quitar(4);
        c1.print();


        ConjuntoOptimista<Integer> c2 = new ConjuntoOptimista<Integer>();
        c2.agregar(4);
        c2.agregar(5);
        c2.agregar(6);
        c2.quitar(5);
        c2.print();

        ConjuntoSinLocks<Integer> c3 = new ConjuntoSinLocks<Integer>();
        c3.agregar(7);
        c3.agregar(8);
        c3.agregar(9);
        c3.print();

    }
}