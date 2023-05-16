public interface Conjunto<T> {
    public void agregar(T elemento);
    public Boolean pertenece(T elemento);
    public void quitar(T elemento);
    public void print();
}
