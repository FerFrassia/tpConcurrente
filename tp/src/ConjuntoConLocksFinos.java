public class ConjuntoConLocksFinos<T> implements Conjunto<T> {

    List list;

    public ConjuntoConLocksFinos() {
        list = new List();
        
    }

    @Override
    public boolean agregar(T elemento) {
    
        Node<T> newNode = new Node<T>(elemento);
        int key = elemento.hashCode();

        Node<T> currentNode = list.head.next; //El primer elemento de la lista
        Node<T> previousNode = list.head; //El centinela

        try {

            if(currentNode != null) currentNode.lock();
            previousNode.lock();
            
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
            if(currentNode != null) currentNode.unlock();
            previousNode.unlock();
        }
    }


    @Override
    public boolean pertenece(T elemento) {
        Node<T> currentNode = list.head.next; 
        Node<T> previousNode = list.head;
        int key = elemento.hashCode();

        try {

            if(currentNode == null) return false;

            currentNode.lock();
            previousNode.lock();
            
            while(currentNode != null && currentNode.key < key) {
                previousNode.unlock();
                
                previousNode = currentNode;
                currentNode = currentNode.next;

                if(currentNode != null) currentNode.lock();
            }

            return currentNode != null && key == currentNode.key;

        } finally {
            if(currentNode != null) currentNode.unlock();
            previousNode.unlock();
        }
    }

    @Override
    public boolean quitar(T elemento) {
        int key = elemento.hashCode();

        Node<T> currentNode = list.head.next; 
        Node<T> previousNode = list.head; 

        try {

            if(currentNode != null) currentNode.lock();
            previousNode.lock();
            
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
            if(currentNode != null) currentNode.unlock();
            previousNode.unlock();
        }
    }

    @Override
    public void print() {
        System.out.print("Locks Finos: ");
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
