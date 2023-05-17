public class ConjuntoSinLocks<T> implements Conjunto<T> {

    List list;

    public ConjuntoSinLocks() {
        list = new List();
    }

    @Override
    public boolean agregar(T elemento) {
        //TO DO: borrar esto y hacer la implementacion posta
        Node newNode = new Node(elemento);
        if (list.head == null) {
            list.head = newNode;
        } else {
            Node currentNode = list.head;
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        return true;
    }

    @Override
    public boolean pertenece(T elemento) {
        return false;
    }

    @Override
    public boolean quitar(T elemento) {
        return false;
    }

    public void print() {
        System.out.print("Sin Locks: ");
        System.out.print("[");
        Node currentNode = list.head;
        while (currentNode != null) {
            System.out.print(currentNode.item);
            if (currentNode.next != null) {
                System.out.print(" -> ");
            }
            currentNode = currentNode.next;
        }
        System.out.println("]");
    }

}

