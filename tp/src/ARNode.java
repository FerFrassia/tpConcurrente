import java.util.concurrent.atomic.AtomicReference;

public class ARNode<T> {

    public T item;
    public AtomicReference<ARNode<T>> next;
    public int key;

    public ARNode(T item) {
        this.item = item;
        next = new AtomicReference<ARNode<T>>(null);
        if(item != null) this.key = item.hashCode();
        else this.key = -1;
    }
}
