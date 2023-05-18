public class Conjunto<T> {

    List list;

    public Conjunto() {
        list = new List();
    }

    public boolean agregar(T elemento) {
        return true;
    }
    public boolean pertenece(T elemento) {
        return true;
    }
    public boolean quitar(T elemento) {
        return true;
    }

    //Métodos auxiliares
    public void print() {
        System.out.print("Locks Finos: ");
        System.out.print("[");
        Node currentNode = list.head.next;
        while (currentNode != null) {
            System.out.print(currentNode.item);
            if (currentNode.next != null) {
                System.out.print(" -> ");
            }
            currentNode = currentNode.next;
        }
        System.out.println("]");
    }
    public boolean esEquivalenteA(T[] arr) {
        return todosLosDelConjuntoEstanEnA(arr) && todosLosDeAEstanEnElConjunto(arr);
    }

    private boolean todosLosDelConjuntoEstanEnA(T[] arr) {
        Node currentNode = list.head.next;
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
            currentNode = currentNode.next;
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

    public void vaciar() {
        list = new List();
    }
}
