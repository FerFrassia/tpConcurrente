public interface Conjunto<T> {
    public boolean agregar(T elemento);  //Lo hice boolean para que vaya en el mismo sentido que el de la clase pero se puede cambiar
    public boolean pertenece(T elemento);
    public boolean quitar(T elemento); //Lo hice boolean para que vaya en el mismo sentido que el de la clase pero se puede cambiar
    public void print();
}
