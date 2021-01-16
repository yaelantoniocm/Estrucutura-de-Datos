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
public class ArbolRojinegro<T extends Comparable<T>> extends ArbolBinarioOrdenado<T> {

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
            return String.format("%s{%s}", color == Color.ROJO ? "R" : "N", elemento.toString());
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
            @SuppressWarnings("unchecked") VerticeRojinegro vertice = (VerticeRojinegro) objeto;
            return raiz.get().equals(vertice.get()) && verticeRojinegro(raiz).color == vertice.color
                   && equals(verticeRojinegro(raiz.izquierdo), verticeRojinegro(vertice.izquierdo))
                   && equals(verticeRojinegro(raiz.derecho), verticeRojinegro(vertice.derecho));
      
    }
    private boolean equals(VerticeRojinegro a, VerticeRojinegro b) {
            if (a == null && b == null)
                return true;
            //Si es que los hijos son diferentes
            else if (a != null && b == null || a == null && b != null)
                return false;
            return a.get().equals(b.get()) && verticeRojinegro(a).color == b.color
                   && equals(verticeRojinegro(a.izquierdo), verticeRojinegro(b.izquierdo))
                   && equals(verticeRojinegro(a.derecho), verticeRojinegro(b.derecho));
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
        return verticeRojinegro(vertice).color;
    }

    //Método auxiliar para obtener al abuelo, al padre, al tío y al hermano, tambien los ocupe para poder orientarme mejor
    private VerticeRojinegro getPadre(VerticeRojinegro vertice) {         //No ocupe este método por que me causaba problemas pero lo necesitaba para getAbuelo, pero puede funcione en un futuro
        return vertice.padre == null ? null : verticeRojinegro(vertice.padre); 
    }
    private VerticeRojinegro getAbuelo(VerticeRojinegro vertice) { //No lo llego a ocupar pero puede funcionar en proximas practicas
        if (getPadre(vertice) == null)
            return null;
        if (vertice.padre.padre == null)
            return null;
        return verticeRojinegro(vertice.padre.padre);
    }
    private VerticeRojinegro getTio(VerticeRojinegro padre, VerticeRojinegro abuelo) {
        return hayHijoIzq(padre) ? verticeRojinegro(abuelo.derecho) :
               verticeRojinegro(abuelo.izquierdo);
    }
    private VerticeRojinegro getHijo(VerticeRojinegro vertice){
        if(vertice.hayIzquierdo())
            return verticeRojinegro(vertice.izquierdo);
        return verticeRojinegro(vertice.derecho);
    }
    
    private boolean hayHijoIzq(Vertice vertice){
        if(!vertice.hayPadre())
            return false;
        return vertice.padre.izquierdo == vertice;        
    }

    private boolean hayHijoDer(Vertice vertice){
        if(!vertice.hayPadre())
            return false;
        return vertice.padre.derecho == vertice;        
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeRojinegro vertice = verticeRojinegro(ultimoAgregado);
        vertice.color = Color.ROJO;
        rebalanceo1(vertice);
    }

    private void rebalanceo1(VerticeRojinegro vertice) {
        VerticeRojinegro padre, abuelo, tio;
        // Caso 1
        if (!vertice.hayPadre()) {
            raiz = vertice;
            vertice.color = Color.NEGRO;
            return;
        }
        padre = verticeRojinegro(vertice.padre);
        //Caso 2
        if (getColor(padre) == Color.NEGRO)
            return;
        abuelo = verticeRojinegro(padre.padre);
        
        //Caso 3
        tio = getTio(padre, abuelo);
        if (tio != null && tio.color == Color.ROJO) {
            padre.color = tio.color = Color.NEGRO;
            abuelo.color = Color.ROJO;
            rebalanceo1(abuelo);
            return;
        }
        //Caso 4
        if (hayHijoIzq(vertice) ^ hayHijoIzq(padre)) {
            if (hayHijoIzq(padre))
                super.giraIzquierda(padre);
            else
                super.giraDerecha(padre);
            VerticeRojinegro v2aux = vertice;
            vertice = padre;
            padre = v2aux;
            //Preparamos para el caso 5.
        }
        //Caso 5
        padre.color = Color.NEGRO;
        abuelo.color = Color.ROJO;
        if (hayHijoIzq(vertice))
            super.giraDerecha(abuelo);
        else
            super.giraIzquierda(abuelo);
    }
    
    private boolean estanCruzados(VerticeRojinegro vertice, VerticeRojinegro sobrinoIzq, VerticeRojinegro sobrinoDer) {
        return esNegro(sobrinoIzq) && hayHijoDer(vertice) || esNegro(sobrinoDer) && hayHijoIzq(vertice);
    }
    private boolean sonVerticesBicoloreados(VerticeRojinegro a, VerticeRojinegro b) {
        return esNegro(a) ^ esNegro(b); //Se utiliza el "y" exclusivo ya que quiero que evalue las dos aun que la primera sea falsa a diferencia del "&&"
    }
    private VerticeRojinegro getHermano(VerticeRojinegro vertice) {
        if (hayHijoIzq(vertice))
            return verticeRojinegro(vertice.padre.derecho);
        return verticeRojinegro(vertice.padre.izquierdo);
    }
   
    //Métodos auxiliares basados en clase Árbol Binario para ver si es Hoja o Raíz
    private boolean esHoja(Vertice vertice){
        return !vertice.hayDerecho() && !vertice.hayIzquierdo();
    }
    private boolean esRaiz(Vertice vertice){
        return vertice == raiz;
    }
    
    /*Método que se engargara de buscar recursivamente de buscar un elemento, me base en la
    *clase de arbol binario para tomarlo
    */
    private Vertice busca(Vertice vertice, T elemento) { 
        if (vertice == null)
            return null;
        if (vertice.get().equals(elemento))
            return vertice;
        Vertice i = busca(vertice.izquierdo, elemento);
        Vertice d = busca(vertice.derecho, elemento);
        return i != null ? i : d;
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
         VerticeRojinegro vertice = verticeRojinegro(busca(raiz, elemento));
        VerticeRojinegro hijo, ghost = null;

        if (vertice == null)
            return;
        if (vertice.hayIzquierdo()) {
            VerticeRojinegro v1aux = vertice;
            vertice = verticeRojinegro(MaxArbol(vertice.izquierdo));
            
            v1aux.elemento = vertice.elemento;
        }
        
        if (esHoja(vertice)){
            ghost = verticeRojinegro(nuevoVertice(null));
            ghost.color = Color.NEGRO;
            ghost.padre = vertice;
            vertice.izquierdo = ghost;
        }
        hijo = getHijo(vertice);
        moveUp(vertice);

        if(esNegro(vertice) && esNegro(hijo)){
            hijo.color = Color.NEGRO;
            rebalanceo2(hijo);
        }else 
            hijo.color = Color.NEGRO;

        killghost(ghost);

        elementos--;
   	}
   	private void moveUp(VerticeRojinegro vertice){
        if (vertice.hayIzquierdo())
            if (vertice == raiz) {
                raiz = vertice.izquierdo;
                raiz.padre = null;
            } else {
                
                vertice.izquierdo.padre = vertice.padre;
                if (hayHijoIzq(vertice))
                    vertice.padre.izquierdo = vertice.izquierdo;
                else
                    vertice.padre.derecho = vertice.izquierdo;
            }else
            if (vertice == raiz) {
                raiz = raiz.derecho;
                raiz.padre = null;
            } else {
                vertice.derecho.padre = vertice.padre;
                if (hayHijoIzq(vertice))
                    vertice.padre.izquierdo = vertice.derecho;
                else
                    vertice.padre.derecho = vertice.derecho;
            }
    }

    private void killghost(VerticeRojinegro ghost){
        if(ghost != null)
            if(esRaiz(ghost))
                raiz = ultimoAgregado = ghost = null;
            else
                if(hayHijoIzq(ghost))
                    ghost.padre.izquierdo = null;
                else
                    ghost.padre.derecho = null;
    }
    private void rebalanceo2(VerticeRojinegro vertice) {
        VerticeRojinegro padre, hermano, sobrinoIzq, sobrinoDer;
        //Caso 1
        if (vertice.padre == null){
            vertice.color = Color.NEGRO;
            raiz = vertice;
            return;
        }
        padre = verticeRojinegro(vertice.padre);
        hermano = getHermano(vertice);
        //Caso 2
        if (!esNegro(hermano)) {
            hermano.color = Color.NEGRO;
            padre.color = Color.ROJO;
            
            if (hayHijoIzq(vertice))
                super.giraIzquierda(padre);
            else
                super.giraDerecha(padre);
            padre = verticeRojinegro(vertice.padre);
            hermano = getHermano(vertice);
        }
        sobrinoIzq = verticeRojinegro(hermano.izquierdo);
        sobrinoDer = verticeRojinegro(hermano.derecho);
        //Caso 3
        if (esNegro(padre) && esNegro(hermano) && sobrinosNegros(sobrinoIzq, sobrinoDer)) {
            hermano.color = Color.ROJO;
            rebalanceo2(padre);
            return;
        }
        //Caso 4
        if (esNegro(hermano) && sobrinosNegros(sobrinoIzq, sobrinoDer) && !esNegro(padre)) {
            padre.color = Color.NEGRO;
            hermano.color = Color.ROJO;
            return;
        }
        //Caso 5
        if (sonVerticesBicoloreados(sobrinoIzq, sobrinoDer) && estanCruzados(vertice, sobrinoIzq, sobrinoDer)) {
            if(!esNegro(sobrinoIzq))
                sobrinoIzq.color = Color.NEGRO;
            else
                sobrinoDer.color = Color.NEGRO;

            hermano.color = Color.ROJO;

            if(hayHijoIzq(vertice))
                super.giraDerecha(hermano);
            else
                super.giraIzquierda(hermano);
            hermano = getHermano(vertice);
            sobrinoIzq = verticeRojinegro(hermano.izquierdo);
            sobrinoDer = verticeRojinegro(hermano.derecho);
        }
        //Caso 6
        hermano.color = padre.color;
        padre.color = Color.NEGRO;

        if(hayHijoIzq(vertice))
            sobrinoDer.color = Color.NEGRO;
        else
            sobrinoIzq.color = Color.NEGRO;

        if(hayHijoIzq(vertice))
            super.giraIzquierda(padre);
        else
            super.giraDerecha(padre);
    }

    //Metodos auxiliares para saber si es rojo o negro
    private boolean sobrinosNegros(VerticeRojinegro sobrinoIzq, VerticeRojinegro sobrinoDer) {
        return esNegro(sobrinoIzq) && esNegro(sobrinoDer);
    }
    private boolean esNegro(VerticeRojinegro vertice) {
        return vertice == null || vertice.color == Color.NEGRO;
    }
    private boolean esRojo(VerticeRojinegro v) { //Este método no lo llegue a ocupar ya que causaba problemas aun que pienso puede funcionar luego.
        if (v == null)
            return false;
        return v.color != Color.NEGRO;
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

    private Vertice MaxArbol(Vertice v) { //De la clase ArbolBinario Ordenado la tome para ver si es el maximo en Sub-Árbol
        if(!v.hayDerecho())                 
            return v;
        return MaxArbol(v.derecho);
    }
}