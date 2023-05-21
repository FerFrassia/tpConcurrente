import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.locks.ReentrantLock;

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

            currentNode = previousNode.next.getReference();
            if (currentNode != null && currentNode.key == key) return false; //Si el elemento ya esta en la lista, no se puede agregar 
            else {
                ARNode<T> node = new ARNode<T>(elemento);
                node.next = new AtomicMarkableReference<ARNode<T>>(currentNode, false);
                if (previousNode.next.compareAndSet(currentNode, node, false, false)) return true;  //Si el proximo nodo del anterior es el esperado, se agrega el nuevo nodo
            }
        }
    }

    private ARNodePair<T> encontrar_previo_y_proximo(T elemento){
        ARNode<T> previousNode = atomicList.head.getReference();
        ARNode<T> currentNode = previousNode.next.getReference();
        ARNode<T> nextNode;
        int key = elemento.hashCode();
        boolean[] marked ={false};

        tryAgain: while (true) {
            previousNode = atomicList.head.getReference();
            currentNode = previousNode.next.getReference();
            
            while (true) {
                if (currentNode == null) {
                    return new ARNodePair<T>(previousNode, null);
                }
                nextNode = currentNode.next.get(marked);
                
                while (marked[0]) {
                    boolean snip = previousNode.next.compareAndSet(currentNode, nextNode, false, false);
                    if (!snip)  continue tryAgain;
                    
                    currentNode = nextNode;
                    nextNode = currentNode.next.get(marked);
                }
                
                if (currentNode.key >= key) {
                    return new ARNodePair<T>(previousNode, currentNode);
                }
                
                previousNode = currentNode;
                currentNode = nextNode;
            }
        }

        /*while (currentNode != null && currentNode.key < key) {
            currentNode = previousNode.next.getReference();
            nextNode = currentNode.next.getReference();

            previousNode = currentNode;
            currentNode = nextNode;
        }
        return new ARNodePair<T>(previousNode, currentNode);*/
    }

    @Override
    public boolean pertenece(T elemento) {
        int key = elemento.hashCode();
        ARNode<T> currentNode = atomicList.head.getReference().next.getReference();
        boolean marked = false;
        while (currentNode != null && currentNode.key != key) {
            marked = currentNode.next.isMarked();
            currentNode = currentNode.next.getReference();
        }
        return currentNode != null && currentNode.key == key && !marked;
    }

    @Override
    public boolean quitar(T elemento) {
        int key = elemento.hashCode();
        ARNodePair<T> nodePair;
        ARNode<T> previousNode, currentNode, nextNode;
        boolean snip;
        while (true) {
            nodePair = encontrar_previo_y_proximo(elemento);
            previousNode = nodePair.previousNode;
            currentNode = nodePair.currentNode;


            if(currentNode == null || currentNode.key != key) return false;
            else {
                nextNode = currentNode.next.getReference();
                snip = currentNode.next.compareAndSet(nextNode, nextNode, false, true);
                if(!snip) continue;
                if (previousNode.next.compareAndSet(currentNode, nextNode, false, false)){
                    return true;
                }
                /*if (!snip) continue;
                if(nextNode != null) System.out.println(elemento + ") nextNode: " + nextNode.item);
                if (previousNode.next.compareAndSet(currentNode, nextNode, false, false)) return true;*/
            }
        }
    }

    @Override
    public void print() {
        System.out.print("[");
        ARNode<T> currentNode = atomicList.head.getReference();
        while (currentNode != null) {
            System.out.print(currentNode.item);
            if (currentNode.next.getReference() != null) {
                System.out.print(" -> ");
            }
            currentNode = currentNode.next.getReference();
        }
        System.out.println("]");
    }

    @Override
    public boolean esEquivalenteA(T[] arr) {
        return todosLosDelConjuntoEstanEnA(arr) && todosLosDeAEstanEnElConjunto(arr);
    }

    private boolean todosLosDelConjuntoEstanEnA(T[] arr) {
        ARNode<T> currentNode = atomicList.head.getReference().next.getReference();
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
            currentNode = currentNode.next.getReference();
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

