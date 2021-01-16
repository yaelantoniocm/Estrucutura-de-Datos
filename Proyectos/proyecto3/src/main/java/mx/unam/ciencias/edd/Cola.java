package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
        
        if(esVacia()) return "";

        String l = "";

        Nodo aux = cabeza;
        while(aux != null) {
            l += aux.elemento.toString() + ",";
            aux = aux.siguiente;
        }
        return l;

    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        
        if(elemento == null) throw new IllegalArgumentException();
        Nodo nodo = new Nodo(elemento);
        if(rabo == null) {
            rabo = cabeza = nodo;
        } else{
            rabo.siguiente = nodo;
            rabo = rabo.siguiente;
        }

    }
}
