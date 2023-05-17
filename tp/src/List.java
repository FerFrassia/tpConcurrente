public class List<T> {

    public Node head;

    public List() {
        this.head = new Node<T>(null); //Creo un nodo centinela. Hice esto para no tener que preguntar si el head es null, pero se puede cambiar
    }
}
