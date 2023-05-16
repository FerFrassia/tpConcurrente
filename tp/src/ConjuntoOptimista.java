public class ConjuntoOptimista<T> implements Conjunto<T> {

    List list;

    public ConjuntoOptimista() {
        list = new List();
    }

    @Override
    public void agregar(T elemento) {
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
    }

    @Override
    public Boolean pertenece(T elemento) {
        return false;
    }

    @Override
    public void quitar(T elemento) {

    }

    @Override
    public void print() {
        System.out.print("Optimista: ");
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
