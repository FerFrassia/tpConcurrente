public class ARNodePair<T> {

    ARNode<T> previousNode;
    ARNode<T> currentNode;

    public ARNodePair(ARNode<T> previousNode, ARNode<T> currentNode) {
        this.previousNode = previousNode;
        this.currentNode = currentNode;
    }
}
