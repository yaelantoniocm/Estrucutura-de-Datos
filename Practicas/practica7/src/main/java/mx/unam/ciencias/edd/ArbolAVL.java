package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
            altura = 0;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            return String.format("%s %d/%d", elemento.toString(), altura, balance(this));
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            return(altura == vertice.altura && super.equals(objeto));
        }
    }

    /* Convierte el vértice a VerticeAVL */
    private VerticeAVL verticeAVL(VerticeArbolBinario<T> vertice) {
        return (VerticeAVL)vertice;
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        rebalanceo(verticeAVL(ultimoAgregado));
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {

    	if(elemento == null) return;

    	VerticeAVL aux = verticeAVL(super.busca(elemento));

    	if(aux == null) return;

    	elementos--;

    	if(elementos == 0) {
    		raiz = null;
    		return;
    	}

    	if(aux.hayIzquierdo() && aux.hayDerecho())
    		aux = verticeAVL(super.intercambiaEliminable(aux));

	    super.eliminaVertice(aux);
	    rebalanceo(aux);
    }

    /* Método auxiliar que nos devuelve la altura de un árbol.*/
    private int getAltura(VerticeArbolBinario<T> vertice) {
        return vertice == null ? -1 : verticeAVL(vertice).altura;
    }

    /* Método auxiliar para cambiar la altura de un vértice. */
    private void cambiaAltura(VerticeAVL vertice) {
        vertice.altura = getAlturaCalculada(vertice);
    }

    /* Método auxiliar que nos dirá la máxima altura entre dos caminos (sub-árbol izquierdo y sub-árbol derecho).*/
    private int getAlturaCalculada(VerticeAVL vertice) {
        return 1 + Math.max(getAltura(verticeAVL(vertice.izquierdo)), getAltura(verticeAVL(vertice.derecho)));
    }

    /* Método auxiliar que nos dice el balance de un vértice. */
    private int balance(VerticeAVL vertice) {
        return getAltura(verticeAVL(vertice.izquierdo)) - getAltura(verticeAVL(vertice.derecho));
    }

    /* Método auxiliar para girar a la izquierda. */
    private void giraIzquierdaAVL(VerticeAVL vertice){
        super.giraIzquierda(vertice);
        cambiaAltura(vertice);
        cambiaAltura(verticeAVL(vertice.padre));
    }

    /* Método auxiliar para girar a la derecha. */
    private void giraDerechaAVL(VerticeAVL vertice){
        super.giraDerecha(vertice);
        cambiaAltura(vertice);
        cambiaAltura(verticeAVL(vertice.padre));
    }

    /* Método que rebalancea el árbol AVL. */
    private void rebalanceo(VerticeAVL vertice) {

        if (vertice == null) return;

        cambiaAltura(vertice);

        if (balance(vertice) == -2) {
            if (balance(verticeAVL(vertice.derecho)) == 1)
                giraDerechaAVL(verticeAVL(vertice.derecho));
            giraIzquierdaAVL(vertice);
        } else if (balance(vertice) == 2) {
            if (balance(verticeAVL(vertice.izquierdo)) == -1)
                giraIzquierdaAVL(verticeAVL(vertice.izquierdo));

            giraDerechaAVL(vertice);
        }
        
        rebalanceo(verticeAVL(vertice.padre));
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}
