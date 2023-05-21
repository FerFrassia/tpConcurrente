public class ConjuntoSinLocks<T> extends Conjunto<T> {

    private ARList<T> atomicList;

    public ConjuntoSinLocks() {
        atomicList = new ARList<T>();
    }

    @Override
    public boolean agregar(T elemento) {
        ARNode<T> node = new ARNode<T>(elemento);
        while (true) {
            ARNode<T> last = atomicList.tail.get();
            ARNode<T> next = last.next.get();
            if (last == atomicList.tail.get()) {
                if (next == null) {
                    if (last.next.compareAndSet(next, node)) {
                        return atomicList.tail.compareAndSet(last, node);
                    }
                } else {
                    atomicList.tail.compareAndSet(last, next);
                }
            }
        }
    }

    @Override
    public boolean pertenece(T elemento) {
        ARNode<T> currentNode = atomicList.head.get().next.get();
        while (currentNode != null) {
            if (currentNode.item.equals(elemento)) {
                return true;
            }
            currentNode = currentNode.next.get();
        }
        return false;
    }

    @Override
    public boolean quitar(T elemento) {
        try {
            quitarSinLocks(elemento);
            return true;
        } catch (EmptyException e) {
            return false;
        }
    }

    public boolean quitarSinLocks(T elemento) throws EmptyException {
        while (true) {
            ARNode<T> first = atomicList.head.get();
            ARNode<T> last = atomicList.tail.get();
            ARNode<T> next = first.next.get();
            if (first == atomicList.head.get()) {
                if (first == last) {
                    if (next == null) {
                        throw new EmptyException();
                    }
                    atomicList.tail.compareAndSet(last, next);
                } else {
                    if (atomicList.head.compareAndSet(first, next)) {
                        return true;
                    }
                }
            }
        }
    }

    @Override
    public void print() {
        System.out.print("[");
        ARNode<T> currentNode = atomicList.head.get();
        while (currentNode != null) {
            System.out.print(currentNode.item);
            if (currentNode.next.get() != null) {
                System.out.print(" -> ");
            }
            currentNode = currentNode.next.get();
        }
        System.out.println("]");
    }

    @Override
    public boolean esEquivalenteA(T[] arr) {
        return todosLosDelConjuntoEstanEnA(arr) && todosLosDeAEstanEnElConjunto(arr);
    }

    private boolean todosLosDelConjuntoEstanEnA(T[] arr) {
        ARNode<T> currentNode = atomicList.head.get().next.get();
        while (currentNode != null) {
            boolean estaPresente = false;
            for (T object: arr) {
                if (currentNode.item.equals(object)) {
                    estaPresente = true;
                    break;
                }
            }
            if (!estaPresente) {
                System.out.println(currentNode.item.toString() + " no está presente en el arreglo ");
                return false;
            }
            currentNode = currentNode.next.get();
        }
        return true;
    }

    private boolean todosLosDeAEstanEnElConjunto(T[] arr) {
        for (T object: arr) {
            if (!pertenece(object)) {
                System.out.print(object.toString() + " no está presente en el conjunto ");
                return false;
            }
        }
        return true;
    }

    @Override
    public void vaciar() {
        atomicList = new ARList<T>();
    }

}

