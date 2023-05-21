import java.util.concurrent.atomic.AtomicMarkableReference;

public class ARNode<T> {

    public T item;
    public AtomicMarkableReference<ARNode<T>> next;
    public int key;

    public ARNode(T item) {
        this.item = item;
        next = new AtomicMarkableReference<ARNode<T>>(null, false);
        if(item != null) this.key = item.hashCode();
        else this.key = -1;
    }
}
