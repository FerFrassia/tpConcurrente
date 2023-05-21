public class ConjuntoSinLocks<T> extends Conjunto<T> {

    private ARList<T> atomicList;

    public ConjuntoSinLocks() {
        atomicList = new ARList<T>();
    }

    @Override
    public boolean agregar(T elemento) {
        int key = elemento.hashCode();
        ARNodePair<T> nodePair;
        ARNode<T> previousNode, currentNode;
        while (true) {
            nodePair = encontrar_previo_y_proximo(elemento);
            previousNode = nodePair.previousNode;
            currentNode = nodePair.currentNode;

            currentNode = previousNode.next.get();
            if (currentNode != null && currentNode.key == key) return false; //Si el elemento ya esta en la lista, no se puede agregar 
            else {
                ARNode<T> node = new ARNode<T>(elemento);
                node.next.set(currentNode);
                if (previousNode.next.compareAndSet(currentNode, node)) return true;  //Si el proximo nodo del anterior es el esperado, se agrega el nuevo nodo
            }
        }
    }

    private ARNodePair<T> encontrar_previo_y_proximo(T elemento){ //Funcion que busca el anterior al elemento buscado
        ARNode<T> previousNode = atomicList.head.get();
        ARNode<T> currentNode = previousNode.next.get();
        ARNode<T> nextNode;
        int key = elemento.hashCode();
        while (currentNode != null && currentNode.key < key) {
            currentNode = previousNode.next.get();
            nextNode = currentNode.next.get();

            previousNode = currentNode;
            currentNode = nextNode;
        }
        return new ARNodePair<T>(previousNode, currentNode);
    }

    @Override
    public boolean pertenece(T elemento) {
        ARNode<T> currentNode = atomicList.head.get().next.get();
        while (currentNode != null && currentNode.key != elemento.hashCode()) {
            currentNode = currentNode.next.get();
        }
        return currentNode != null && currentNode.key == elemento.hashCode();
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
        int key = elemento.hashCode();
        ARNodePair<T> nodePair;
        ARNode<T> previousNode, currentNode;
        while (true) {
            ARNode<T> first = atomicList.head.get();
            if(first == null) throw new EmptyException();

            nodePair = encontrar_previo_y_proximo(elemento);
            previousNode = nodePair.previousNode;
            currentNode = nodePair.currentNode;
            if(currentNode == null || currentNode.key != key) return false;
            else {
                ARNode<T> nextNode = currentNode.next.get();
                if (previousNode.next.compareAndSet(currentNode, nextNode)) return true;
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

