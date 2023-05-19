public class ConjuntoConLocksFinos<T> extends Conjunto<T> {

    @Override
    public boolean agregar(T elemento) {
    
        Node<T> newNode = new Node<T>(elemento);
        int key = elemento.hashCode();

        Node<T> previousNode = list.head; //El centinela
        Node<T> currentNode = null;

        try {
            previousNode.lock();

            currentNode = list.head.next; //El primer elemento de la lista
            if(currentNode != null) currentNode.lock();

            
            while(currentNode != null && currentNode.key < key) { //Busco el lugar donde insertar el nuevo nodo
                previousNode.unlock();
                
                previousNode = currentNode;
                currentNode = currentNode.next;

                if(currentNode != null) currentNode.lock();
            }

            if(currentNode != null && key == currentNode.key) return false; //Si ya existe el elemento, no lo agrego

            newNode.next = currentNode; //Inserto el nuevo nodo
            previousNode.next = newNode;
            return true;

        } finally {
            previousNode.unlock();
            if(currentNode != null) currentNode.unlock();
        }
    }


    @Override
    public boolean pertenece(T elemento) {
        Node<T> previousNode = list.head;
        Node<T> currentNode = list.head.next; 
        int key = elemento.hashCode();

        try {

            if(currentNode == null) return false;

            previousNode.lock();
            currentNode.lock();
            
            while(currentNode != null && currentNode.key < key) {
                previousNode.unlock();
                
                previousNode = currentNode;
                currentNode = currentNode.next;

                if(currentNode != null) currentNode.lock();
            }

            return currentNode != null && key == currentNode.key;

        } finally {
            previousNode.unlock();
            if(currentNode != null) currentNode.unlock();
        }
    }

    @Override
    public boolean quitar(T elemento) {
        int key = elemento.hashCode();

        Node<T> previousNode = list.head; 
        Node<T> currentNode = list.head.next; 

        try {

            previousNode.lock();
            if(currentNode != null) currentNode.lock();
            
            while(currentNode != null && currentNode.key <= key) { 
               if(currentNode.item == elemento){
                    previousNode.next = currentNode.next;
                    return true;
               }
                previousNode.unlock();
                
                previousNode = currentNode;
                currentNode = currentNode.next;

                if(currentNode != null) currentNode.lock();
            }
            
            return false;
        } finally {
            previousNode.unlock();
            if(currentNode != null) currentNode.unlock();
        }
    }

}
