import java.util.concurrent.atomic.AtomicReference;

public class ARList<T> {
    public AtomicReference<ARNode<T>> head;
    public AtomicReference<ARNode<T>> tail;

    public ARList() {
        ARNode<T> centinell = new ARNode<T>(null);
        head = new AtomicReference<ARNode<T>>(centinell);
        tail = new AtomicReference<ARNode<T>>(centinell);
    }
}
