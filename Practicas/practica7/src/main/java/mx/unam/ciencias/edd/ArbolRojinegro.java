package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            if(this.color == Color.ROJO)
                return "R{" + elemento.toString() + "}";
            return "N{" + elemento.toString() + "}";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") 
            	VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            return color == vertice.color && super.equals(objeto);
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }
    
    /*private VerticeRojinegro verticeRojinegro(VerticeArbolBinario<T> vertice){
    	return (VerticeRojinegro)vertice;
    }*/

    private VerticeRojinegro verticeRojinegro(VerticeArbolBinario<T> vertice) {
    	VerticeRojinegro v = (VerticeRojinegro)vertice;
    	return v;
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        VerticeRojinegro v = verticeRojinegro(vertice);
        return v.color;
    }


    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
	    super.agrega(elemento);
	    VerticeRojinegro v = verticeRojinegro(getUltimoVerticeAgregado());
	 	v.color = Color.ROJO;
	    rebalancea(v);        	
    }

   private void rebalancea(VerticeRojinegro v) {

   		VerticeRojinegro padre, tio, abuelo;

    	//----------Caso 1----------

    	if(!v.hayPadre()) {
    		v.color = Color.NEGRO;
    		return;

    	}
    	//----------Caso 2----------

    	padre = verticeRojinegro(v.padre);
    	if(padre.color == Color.NEGRO)
    		return;

    	abuelo = verticeRojinegro(padre.padre);
    	
    	//----------Caso 3----------

    	if(esIzquierdo(padre))
    		tio = verticeRojinegro(abuelo.derecho);
    	else
    		tio = verticeRojinegro(abuelo.izquierdo);
    	
    	if(tio != null && tio.color == Color.ROJO) {
    		padre.color = Color.NEGRO;
    		tio.color = Color.NEGRO;
    		abuelo.color = Color.ROJO;
    		rebalancea(abuelo);
    		return;
    	}

    	//----------Caso 4----------

    	if(esIzquierdo(v) && !esIzquierdo(padre)) {
    		super.giraDerecha(padre);
    		padre =verticeRojinegro(abuelo.derecho);
    		abuelo = verticeRojinegro(padre.padre);
    		v = verticeRojinegro(padre.derecho);
    	}else if(esIzquierdo(padre) && !esIzquierdo(v)) {
    		super.giraIzquierda(padre);
    		padre = verticeRojinegro(abuelo.izquierdo);
    		abuelo = verticeRojinegro(padre.padre);
    		v = verticeRojinegro(padre.izquierdo);
    	}

    	//----------Caso 5----------

    	padre.color = Color.NEGRO;
    	abuelo.color = Color.ROJO;
    	if(esIzquierdo(v))
    		super.giraDerecha(abuelo);
    	else
    		super.giraIzquierda(abuelo);
    }

    private boolean esIzquierdo(VerticeRojinegro vertice){
        return !vertice.hayPadre() ? false : vertice.padre.izquierdo == vertice;
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {

    	VerticeRojinegro vertice = verticeRojinegro(super.busca(elemento));
    	VerticeRojinegro hijo, fantasma = null;

    	if(vertice == null)
    		return;

    	if(vertice.hayIzquierdo()) {
    		VerticeRojinegro aux = vertice;
    		vertice = verticeRojinegro(maximoSubArbol(vertice.izquierdo));
    		aux.elemento = vertice.elemento;
    	}

    	if(esHoja(vertice)) {
    		fantasma = verticeRojinegro(nuevoVertice(null));
    		fantasma.color = Color.NEGRO;
    		fantasma.padre = vertice;
    		vertice.izquierdo = fantasma;
    	}

    	hijo = getHijo(vertice);
    	subirHijo(vertice);

    	if(esNegro(vertice) && esNegro(hijo)) {
    		hijo.color = Color.NEGRO;
    		rebalanceaE(hijo);
    	}else
    		hijo.color = Color.NEGRO;

    	mataFantasma(fantasma);

    	elementos--;
    	
    }

    private Vertice maximoSubArbol(Vertice v) {
        return !v.hayDerecho() ? v : maximoSubArbol(v.derecho);
    }

    private boolean esHoja(VerticeRojinegro vertice) {
    	return (vertice.derecho == null && vertice.izquierdo == null) ? true : false;
    }

    private void mataFantasma(VerticeRojinegro fantasma) {
    	if(fantasma != null)
    		if(esRaiz(fantasma))
    			raiz = ultimoAgregado = fantasma = null;
    		else
    			if(esIzquierdo(fantasma))
    				fantasma.padre.izquierdo = null;
    			else
    				fantasma.padre.derecho = null;
    }

    private boolean esRaiz(VerticeRojinegro vertice) {
    	return vertice.padre == null ? true : false;
    }

    private void rebalanceaE(VerticeRojinegro v) {

    	VerticeRojinegro padre, hermano, hijoI, hijoD, aux;

    	//----------Caso 1----------

    	if(!v.hayPadre()) {
    		v.color = Color.NEGRO;
    		raiz = v;
    		return;
    	}

    	padre = verticeRojinegro(v.padre);
    	hermano = getHermano(v);

    	//----------Caso 2----------

    	if(!esNegro(hermano)) {

    		padre.color = Color.ROJO;
    		hermano.color = Color.NEGRO;

    		if(esIzquierdo(v))
    			super.giraIzquierda(padre);
    		else
    			super.giraDerecha(padre);
    		
    		padre = verticeRojinegro(v.padre);
    		hermano = getHermano(v);
    	}

    	hijoI = verticeRojinegro(hermano.izquierdo);
    	hijoD = verticeRojinegro(hermano.derecho);

    	//----------Caso 3----------

    	if(esNegro(padre) && esNegro(hermano) && esNegro(hijoD) && esNegro(hijoI)) {
    		hermano.color = Color.ROJO;
    		rebalanceaE(padre);
    		return;
    	}

    	//----------Caso 4----------

    	if(esNegro(hermano) && esNegro(hijoI) && esNegro(hijoD) && !esNegro(padre)) {
    		hermano.color = Color.ROJO;
    		padre.color = Color.NEGRO;
    		return;
    	}

    	//----------Caso 5----------

    	if(sonBicoloreados(hijoD, hijoI) && sonCruzados(v, hijoI, hijoD)) { 
    	
    		if(!esNegro(hijoI))
    			hijoI.color = Color.NEGRO;
    		else
    			hijoD.color = Color.NEGRO;

    		hermano .color = Color.ROJO;

    		if(esIzquierdo(v))
    			super.giraDerecha(hermano);
    		else
    			super.giraIzquierda(hermano);
    		
    		hermano = getHermano(v);
    		hijoI = verticeRojinegro(hermano.izquierdo);
    		hijoD = verticeRojinegro(hermano.derecho);
    	}

    	//----------Caso 6----------
    	
    	hermano.color = padre.color;
    	padre.color = Color.NEGRO;
    	if(esIzquierdo(v))
    		hijoD.color = Color.NEGRO;
    	else
    		hijoI.color = Color.NEGRO;
    	if(esIzquierdo(v))
    		super.giraIzquierda(padre);
    	else
    		super.giraDerecha(padre);    	
    }

    private void eliminaFantasma(VerticeRojinegro eliminar){
    	if(eliminar.elemento == null)
    		eliminaHoja(eliminar);
    }

    private void eliminaHoja(VerticeRojinegro eliminar) {
    	if(raiz == eliminar) {
    		raiz = null;
    		ultimoAgregado = null;
    	}else if(esIzquierdo(eliminar))
    		eliminar.padre.izquierdo = null;
    	else
    		eliminar.padre.derecho = null;
    }

    private void subirHijo(VerticeRojinegro vertice) {

    	if(vertice.hayIzquierdo())
    		if(vertice == raiz) {
    			raiz = vertice.izquierdo;
    			raiz.padre = null;
    		}else {
    			vertice.izquierdo.padre = vertice.padre;
    			if(esIzquierdo(vertice))
    				vertice.padre.izquierdo = vertice.izquierdo;
    			else
    				vertice.padre.derecho = vertice.izquierdo;
    		}else if(vertice == raiz) {
    			raiz = raiz.derecho;
    			raiz.padre = null;
    		}else {
    			vertice.derecho.padre = vertice.padre;
    			if(esIzquierdo(vertice))
    				vertice.padre.izquierdo = vertice.derecho;
    			else
    				vertice.padre.derecho = vertice.derecho;
    		}

    }

    private void eliminaSinHijoIzq(VerticeRojinegro eliminar) {
       if(raiz == eliminar) {
            raiz = raiz.derecho;
            eliminar.derecho.padre = null;
        }else {
            eliminar.derecho.padre = eliminar.padre;
            if(esIzquierdo(eliminar))
                eliminar.padre.izquierdo = eliminar.derecho;
            else
                eliminar.padre.derecho = eliminar.derecho;
        }
        elementos--;
    }

    private void eliminaSinHijoDer(VerticeRojinegro eliminar) {
       if(raiz == eliminar) {
            raiz = raiz.izquierdo;
            eliminar.izquierdo.padre = null;
        }else {
            eliminar.izquierdo.padre = eliminar.padre;
            if(esIzquierdo(eliminar))
                eliminar.padre.izquierdo = eliminar.izquierdo;
            else
                eliminar.padre.derecho = eliminar.izquierdo;
        }
        elementos--;
    }

    private VerticeRojinegro getHermano(VerticeRojinegro vertice) {
    	return esIzquierdo(vertice) ? verticeRojinegro(vertice.padre.derecho) : verticeRojinegro(vertice.padre.izquierdo);
    }

    private VerticeRojinegro getHijo(VerticeRojinegro vertice) {
    	return (vertice.hayIzquierdo()) ? verticeRojinegro(vertice.izquierdo) : verticeRojinegro(vertice.derecho);
    }

    private boolean sonCruzados(VerticeRojinegro v, VerticeRojinegro hijoIzq, VerticeRojinegro hijoDer){
    	return esNegro(hijoIzq) && !esIzquierdo(v) || esNegro(hijoDer) && esIzquierdo(v);
    }

    private boolean sonBicoloreados(VerticeRojinegro v1, VerticeRojinegro v2){
    	return esNegro(v1) ^ esNegro(v2);
    }

    private boolean esNegro(VerticeRojinegro vertice){
    	return vertice == null || vertice.color == Color.NEGRO;
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
