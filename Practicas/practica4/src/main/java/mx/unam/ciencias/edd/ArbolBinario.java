package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * <p>Clase abstracta para árboles binarios genéricos.</p>
 *
 * <p>La clase proporciona las operaciones básicas para árboles binarios, pero
 * deja la implementación de varias en manos de las subclases concretas.</p>
 */
public abstract class ArbolBinario<T> implements Coleccion<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class Vertice implements VerticeArbolBinario<T> {

        /** El elemento del vértice. */
        public T elemento;
        /** El padre del vértice. */
        public Vertice padre;
        /** El izquierdo del vértice. */
        public Vertice izquierdo;
        /** El derecho del vértice. */
        public Vertice derecho;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public Vertice(T elemento) {
            this.elemento = elemento;
        }

        /**
         * Nos dice si el vértice tiene un padre.
         * @return <tt>true</tt> si el vértice tiene padre,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayPadre() {
            return padre != null;
        }

        /**
         * Nos dice si el vértice tiene un izquierdo.
         * @return <tt>true</tt> si el vértice tiene izquierdo,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayIzquierdo() {
            return izquierdo != null; 
        }

        /**
         * Nos dice si el vértice tiene un derecho.
         * @return <tt>true</tt> si el vértice tiene derecho,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayDerecho() {
            return derecho != null;
        }

        /**
         * Regresa el padre del vértice.
         * @return el padre del vértice.
         * @throws NoSuchElementException si el vértice no tiene padre.
         */
        @Override public VerticeArbolBinario<T> padre() {
            if(padre == null) throw new NoSuchElementException();
            return this.padre;
        }

        /**
         * Regresa el izquierdo del vértice.
         * @return el izquierdo del vértice.
         * @throws NoSuchElementException si el vértice no tiene izquierdo.
         */
        @Override public VerticeArbolBinario<T> izquierdo() {
            if(izquierdo == null) throw new NoSuchElementException();
            return this.izquierdo;
        }

        /**
         * Regresa el derecho del vértice.
         * @return el derecho del vértice.
         * @throws NoSuchElementException si el vértice no tiene derecho.
         */
        @Override public VerticeArbolBinario<T> derecho() {
            if(derecho == null) throw new NoSuchElementException();

             return this.derecho;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return altura2(this);
        }

        /*Método auxiliar de altura */
         private int altura2(Vertice vertice) {
             if(vertice == null)
                 return -1;
             return 1 + Math.max(altura2(vertice.izquierdo), altura2(vertice.derecho));
         }

        /**
         * Regresa la profundidad del vértice.
         * @return la profundidad del vértice.
         */
        @Override public int profundidad() {
            return profundidad2(this);
        }

        /*Método auxiliar de profundidad */
         private int profundidad2(Vertice vertice) {
             if(!vertice.hayPadre())
                 return 0;
             return 1 + profundidad2(vertice.padre);
         }

        /**
         * Regresa el elemento al que apunta el vértice.
         * @return el elemento al que apunta el vértice.
         */
        @Override public T get() {
            return elemento;
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>. Las clases que extiendan {@link Vertice} deben
         * sobrecargar el método {@link Vertice#equals}.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link Vertice}, su elemento es igual al elemento de éste
         *         vértice, y los descendientes de ambos son recursivamente
         *         iguales; <code>false</code> en otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") Vertice vertice = (Vertice)objeto;
            if(!elemento.equals(vertice.elemento))
                return false;
            return equalsDer(this, vertice) && equalsIzq(this, vertice);
        }

        private boolean equalsIzq(Vertice v1, Vertice v2){
           if (v1.hayIzquierdo() && v2.hayIzquierdo() && v2.elemento.equals(v1.elemento))
                return v1.izquierdo.equals(v2.izquierdo);
            if(!v1.hayIzquierdo() && !v2.hayIzquierdo() && v2.elemento.equals(v1.elemento))
                return true;
            if (v1.hayIzquierdo() && !v2.hayIzquierdo())
                return false;
            if(!v1.hayIzquierdo() && !v2.hayIzquierdo())
                return false;
            return false;
        }

        private boolean equalsDer(Vertice v3, Vertice v4){
            if (v3.hayDerecho() && v4.hayDerecho() && v4.elemento.equals(v3.elemento))
                return v3.derecho.equals(v4.derecho);
            if(!v3.hayDerecho() && !v4.hayDerecho() && v4.elemento.equals(v3.elemento))
                return true;
            if (v3.hayDerecho() && !v4.hayDerecho())
                return false;
            if(!v3.hayDerecho() && !v4.hayDerecho())
                return false;
            return false;
        }

        /**
         * Regresa una representación en cadena del vértice.
         * @return una representación en cadena del vértice.
         */
        public String toString() {
            return elemento.toString();
        }
    }

    /** La raíz del árbol. */
    protected Vertice raiz;
    /** El número de elementos */
    protected int elementos;

    /**
     * Constructor sin parámetros. Tenemos que definirlo para no perderlo.
     */
    public ArbolBinario() {}

    /**
     * Construye un árbol binario a partir de una colección. El árbol binario
     * tendrá los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario.
     */
    public ArbolBinario(Coleccion<T> coleccion) {
        for (T elemento : coleccion)
        	agrega(elemento);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link Vertice}. Para
     * crear vértices se debe utilizar este método en lugar del operador
     * <code>new</code>, para que las clases herederas de ésta puedan
     * sobrecargarlo y permitir que cada estructura de árbol binario utilice
     * distintos tipos de vértices.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    protected Vertice nuevoVertice(T elemento) {
        return new Vertice(elemento);
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol es la altura de su
     * raíz.
     * @return la altura del árbol.
     */
    public int altura() {
        if(raiz == null)
            return -1;
        return raiz.altura();
    }

    /**
     * Regresa el número de elementos que se han agregado al árbol.
     * @return el número de elementos en el árbol.
     */
    @Override public int getElementos() {
        return elementos;
    }

    /**
     * Nos dice si un elemento está en el árbol binario.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        if(elemento.equals(null))
            return false;
        return busca(elemento) != null;
    }

    /**
     * Busca el vértice de un elemento en el árbol. Si no lo encuentra regresa
     * <tt>null</tt>.
     * @param elemento el elemento para buscar el vértice.
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <tt>null</tt> en otro caso.
     */
    public VerticeArbolBinario<T> busca(T elemento) {
        if(elemento == null)
            return null;
         if(esVacia())
            return null;
         return busca(raiz, elemento);				
    }

     private VerticeArbolBinario<T> busca(Vertice vertice, T elemento){
        Cola<Vertice> v = new Cola<Vertice>();
        v.mete(vertice);
        while(!v.esVacia()) {
            Vertice aux = v.cabeza.elemento;
            if(v.saca().elemento.equals(elemento))
                return aux;
            else {
                if(aux.hayIzquierdo())
                    v.mete(aux.izquierdo);
                if(aux.hayDerecho())
                    v.mete(aux.derecho);
            }
        }
        return null;
    }

    /**
     * Regresa el vértice que contiene la raíz del árbol.
     * @return el vértice que contiene la raíz del árbol.
     * @throws NoSuchElementException si el árbol es vacío.
     */
    public VerticeArbolBinario<T> raiz() {
        if(raiz == null)
        	throw new NoSuchElementException();
        return raiz;
    }

    /**
     * Nos dice si el árbol es vacío.
     * @return <code>true</code> si el árbol es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return raiz == null;
    }

    /**
     * Limpia el árbol de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        raiz = null;
        elementos = 0;
    }

    /**
     * Compara el árbol con un objeto.
     * @param objeto el objeto con el que queremos comparar el árbol.
     * @return <code>true</code> si el objeto recibido es un árbol binario y los
     *         árboles son iguales; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked")
            ArbolBinario<T> arbol = (ArbolBinario<T>)objeto;
        if (raiz == null && arbol.raiz == null)
            return true;
        if (raiz == null && arbol.raiz != null)
            return false;
        if (raiz != null && arbol.raiz == null)
            return false;
        return raiz.equals(arbol.raiz);
    }

    /**
     * Regresa una representación en cadena del árbol.
     * @return una representación en cadena del árbol.
     */
    @Override public String toString() {
        if(raiz==null) return "";
        boolean[] X = new boolean[altura()+1];
        for (int i=0;i<altura()+1;i++)
            X[i] = false;
        return toString(raiz,0,X);
    }



    private String toString(Vertice vertice, int nivel, boolean[] rama) {

        String p = vertice + "\n";
        rama[nivel] = true;
        if (vertice.izquierdo != null && vertice.derecho != null) {
            p += Espacios(nivel, rama);
            p += "├─›";
            p += toString(vertice.izquierdo, nivel + 1, rama);
            p += Espacios(nivel, rama);
            p += "└─»";
            rama[nivel] = false; 
            p += toString(vertice.derecho, nivel + 1, rama);
        }else if (vertice.izquierdo != null) {
            p += Espacios(nivel, rama);
            p += "└─›";
            rama[nivel] = false; 
            p += toString(vertice.izquierdo, nivel + 1, rama);
        }else if (vertice.derecho != null) {
            p += Espacios(nivel, rama);
            p += "└─»";
            rama[nivel] = false; 
            p += toString(vertice.derecho, nivel + 1, rama);
        }
        return p;
    }
    // Ilustra los espacios
    private String Espacios(int n, boolean[] rama) {
        String e = "";
        for (int i = 0; i < n; i++)
            if(rama[i])
                e += "│  "; 
            else
                e += "   "; 
        return e;
    
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * Vertice}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         Vertice}.
     */
    protected Vertice vertice(VerticeArbolBinario<T> vertice) {
        return (Vertice)vertice;
    }

}
