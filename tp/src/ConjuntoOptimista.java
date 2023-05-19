public class ConjuntoOptimista<T> extends Conjunto<T> {

    private boolean validar (Node<T> currentNode, Node<T> previousNode){
        Node<T> auxiliarNode = list.head;
        while(auxiliarNode != null && auxiliarNode.key <= previousNode.key){
            if(auxiliarNode == previousNode) return previousNode.next == currentNode;
            auxiliarNode = auxiliarNode.next;
        }
        return false;
    }

    @Override
    public boolean agregar(T elemento) {
        Node<T> newNode = new Node<T>(elemento);
        int key = elemento.hashCode();

        while (true){
            Node<T> previousNode = list.head; 
            Node<T> currentNode = list.head.next; 

            while(currentNode != null && currentNode.key < key) { 
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            previousNode.lock();
            if(currentNode != null) currentNode.lock();

            try {
                if(validar(currentNode, previousNode)){
                    if(currentNode != null && currentNode.key == key) return false;
                    newNode.next = currentNode;
                    previousNode.next = newNode;
                    return true;
                }    
            } finally {
                if(currentNode != null) currentNode.unlock();
                previousNode.unlock();
            }
        }
    }

    @Override
    public boolean pertenece(T elemento) {
        int key = elemento.hashCode();

        while (true){
            Node<T> previousNode = list.head; 
            Node<T> currentNode = list.head.next; 

            while(currentNode != null && currentNode.key < key) { 
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            previousNode.lock();
            if(currentNode != null) currentNode.lock();

            try {
                if(validar(currentNode, previousNode)){
                    return currentNode != null && currentNode.key == key;
                }    
            } finally {
                if(currentNode != null) currentNode.unlock();
                previousNode.unlock();
            }
        }
    }

    @Override
    public boolean quitar(T elemento) {
        int key = elemento.hashCode();

        while (true) {
            Node<T> previousNode = list.head;
            Node<T> currentNode = list.head.next;

            while (currentNode != null && currentNode.key < key) {
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            previousNode.lock();
            if (currentNode != null) currentNode.lock();

            try {
                if (validar(currentNode, previousNode)) {
                    if (currentNode != null && currentNode.key == key) {
                        previousNode.next = currentNode.next;
                        return true;
                    }
                    return false;
                }
            } finally {
                if (currentNode != null) currentNode.unlock();
                previousNode.unlock();
            }
        }
    }

}
