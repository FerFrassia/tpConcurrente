import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class Node<T> {

    public T item;
    public int key;
    public Node<T> next;

    private Lock lock;

    public Node(T item) {
        this.item = item;
        if(item == null) this.key = -1;
        else this.key = item.hashCode();
        this.lock = new ReentrantLock();
    }

    public void lock() {
        this.lock.lock();
    }

    public void unlock() {
        this.lock.unlock();
    }

}