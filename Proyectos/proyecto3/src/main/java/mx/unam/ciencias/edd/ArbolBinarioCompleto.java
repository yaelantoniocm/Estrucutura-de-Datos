package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        public Iterador() {
            cola = new Cola<Vertice>();
            if(esVacia())
                return;
            cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !cola.esVacia();
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            Vertice v = cola.saca();
            if(v.izquierdo != null)
                cola.mete(v.izquierdo);
            if(v.derecho != null)
                cola.mete(v.derecho);
            return v.elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {

        if(elemento == null) throw new IllegalArgumentException();

        Vertice a = nuevoVertice(elemento);
        elementos++;

        if(esVacia())
            raiz = a;
        else {
            Vertice b = BFS();
            if(!b.hayIzquierdo()) {
                b.izquierdo = a;
                a.padre = b;
                return;
            }
            if(!b.hayDerecho()) {
                b.derecho = a;
                a.padre = b;
            }
        }
    }

    /* Método BFS para agrega */
    private Vertice BFS() {
        if(esVacia()) return null;

        Cola<Vertice> a = new Cola<Vertice>();
        a.mete(raiz);

        while(a.cabeza != null) { 
            Vertice b = a.saca();
            if(b.hayIzquierdo())
                a.mete(b.izquierdo);
            if(b.hayDerecho())
                a.mete(b.derecho);
            if(!b.hayIzquierdo() || !b.hayDerecho())
                return b;
        }
        return null;
     }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        
        if(elemento == null) return;

        Vertice aux = (Vertice)busca(elemento);

        if(aux.equals(null)) return;

        elementos--;
        if(elementos == 0 && elemento.equals(raiz.elemento))
            raiz = null;
        else {
            Vertice b = ultimo();
            if(b.padre.izquierdo.equals(b)){
                intercambia(aux, b);
                b.padre.izquierdo = null;
                b.padre = null;
            }else {
                intercambia(aux, b);
                b.padre.derecho = null;
                b.padre = null;
            }
        }
    }

    /* Método auxiliar para elimina*/
    private Vertice ultimo() {

        Cola<Vertice>cola = new Cola<Vertice>();
        cola.mete(raiz);
        Vertice vertice = null;
        while(cola.cabeza != null){
            vertice = cola.saca();
            if(vertice.hayIzquierdo())
                cola.mete(vertice.izquierdo);
            if(vertice.hayDerecho())
                cola.mete(vertice.derecho);
        }
        return vertice;
    }

    /*Método auxiliar intercambia para elimina*/
    private void intercambia(Vertice a, Vertice b) {
        T aux = a.elemento;
        a.elemento = b.elemento;
        b.elemento = aux;
     }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        if(raiz == null)
            return -1;
        else
            return (int)Math.floor(Math.log(elementos) / Math.log(2));
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        if(esVacia()) return;
        
        Cola<Vertice>cola = new Cola<Vertice>();
        cola.mete(raiz);
        while(cola.cabeza != null){
            Vertice v = cola.saca();
            accion.actua(v);
            if(v.hayIzquierdo())
                cola.mete(v.izquierdo);
            if(v.hayDerecho())
                cola.mete(v.derecho);
        }
        return;
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
