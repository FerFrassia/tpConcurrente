import java.util.concurrent.atomic.AtomicMarkableReference;

public class ARList<T> {
    public AtomicMarkableReference<ARNode<T>> head;
    //public AtomicMarkableReference<ARNode<T>> tail;

    public ARList() {
        ARNode<T> centinell = new ARNode<T>(null);
        head = new AtomicMarkableReference<ARNode<T>>(centinell, false);
        //tail = new AtomicMarkableReference<ARNode<T>>(centinell, false);
    }
}
