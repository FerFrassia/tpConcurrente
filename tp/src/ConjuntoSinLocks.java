public class ConjuntoSinLocks<T> extends Conjunto<T> {

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


}

