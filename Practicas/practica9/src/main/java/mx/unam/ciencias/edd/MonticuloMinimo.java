package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return indice < elementos;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if(!isValidIndex(indice)) { throw new NoSuchElementException(); }
            return arbol[indice++];
        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
            this.indice = -1;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            return this.elemento.compareTo(adaptador.elemento);
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = nuevoArreglo(100); /* 100 es arbitrario. */
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        arbol = nuevoArreglo(n);
        int i = 0;
        elementos = n;
        for(T e: iterable) {
            arbol[i] = e;
            e.setIndice(i);
            i += 1;
        }
        for(i = (n - 1) / 2; i >= 0; i--) {
            heapifyDown(arbol[i]);
        }
    }

    private void heapifyDown(T elem) {
        if (elem == null) { return; }
        int u = elem.getIndice() * 2 + 1;
        int w = elem.getIndice() * 2 + 2;

        if(!isValidIndex(u) && !isValidIndex(w))
            return;

        int min = w;
        if(isValidIndex(u)) {
            if(isValidIndex(w)) {
                if(arbol[u].compareTo(arbol[w]) < 0) { min = u; }
            } else { min = u; }
        }

        if(this.arbol[min].compareTo(elem) < 0) {
            intercambia(elem, arbol[min]);
            heapifyDown(elem);
        }
    }

    private void heapifyUp(T elem) {
        int e1 = elem.getIndice() - 1;
        e1 = e1 == -1 ? -1 : e1 / 2;
        if(!isValidIndex(e1) || arbol[e1].compareTo(elem) < 0) {
            return;
        }
        intercambia(arbol[e1], elem);
        heapifyUp(elem);
    }

    private boolean isValidIndex(int i) {
        return !(i < 0 || i >= elementos);
    }

    private void intercambia(T elem1, T elem2) {
        int v = elem2.getIndice();
        arbol[elem1.getIndice()] = elem2;
        arbol[elem2.getIndice()] = elem1;
        elem2.setIndice(elem1.getIndice());
        elem1.setIndice(v);
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        if(elementos == arbol.length) {
            T[] newArray = nuevoArreglo(elementos * 2);
            for(int i = 0; i < elementos; i++) 
                newArray[i] = arbol[i];
            arbol = newArray;
        }
        arbol[elementos] = elemento;
        elemento.setIndice(elementos);
        elementos += 1;
        heapifyUp(elemento);
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if(esVacia()) { throw new IllegalStateException(); }
        T elem = arbol[0];
        intercambia(arbol[0], arbol[elementos - 1]);
        elementos -= 1;
        arbol[elementos].setIndice(-1);
        arbol[elementos] = null;
        heapifyDown(arbol[0]);

        return elem;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        int i = elemento.getIndice();
        if(!isValidIndex(i)) { return; }
        intercambia(arbol[i], arbol[elementos - 1]);
        elementos -= 1;
        arbol[elementos] = null;
        elemento.setIndice(-1);
        reordena(arbol[i]);
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        int i = elemento.getIndice();
        if(!isValidIndex(i)) { return false; }
        return arbol[i].compareTo(elemento) == 0;
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean esVacia() {
        return elementos == 0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        for(int i = 0; i < elementos; i++)
            arbol[i] = null;
        elementos = 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        if(elemento == null) { return; }
        int parent = elemento.getIndice() - 1;
        parent = parent == -1 ? -1 : parent / 2;
        if(!isValidIndex(parent) || arbol[parent].compareTo(elemento) <= 0) {
            heapifyDown(elemento);
        } else {
            heapifyUp(elemento);
        }
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if(!isValidIndex(i)) { throw new NoSuchElementException(); }
        return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        String rep = "";
        for(T e: this)
            rep += e.toString() + ", ";
        return rep;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        if(getElementos() != monticulo.getElementos()) { return false;}
        for(int i = 0; i < getElementos(); i++) {
            if(!this.get(i).equals(monticulo.get(i))) { return false; }
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>> Lista<T> heapSort(Coleccion<T> coleccion) {
        Lista<Adaptador<T>> nuevaLista = new Lista<Adaptador<T>>();
        for(T aux : coleccion)
            nuevaLista.agrega(new Adaptador<T>(aux));

        Lista<T> lista = new Lista<T>();

        MonticuloMinimo<Adaptador<T>> minimo = new MonticuloMinimo<Adaptador<T>>(nuevaLista);
        while(!minimo.esVacia()) 
            lista.agrega(minimo.elimina().elemento);
        

        return lista;
    }
}