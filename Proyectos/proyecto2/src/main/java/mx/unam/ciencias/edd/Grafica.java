package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */    
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext(); //super.hasNext
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().get();
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La lista de vecinos del vértice. */
        public Lista<Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            vecinos = new Lista<>();
            this.elemento = elemento;
            this.color = Color.NINGUNO;
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getElementos();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        this.vertices = new Lista<>();
        this.aristas = 0;
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getElementos(); 
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null) throw new IllegalArgumentException();
        for(Vertice v : vertices) 
            if (v.elemento.equals(elemento)) throw new IllegalArgumentException();
        vertices.agrega(new Vertice(elemento));


    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        Vertice vertice1_a = (Vertice) vertice(a);
        Vertice vertice2_b = (Vertice) vertice(b);
        if (vertice1_a == vertice2_b || vertice1_a.vecinos.contiene(vertice2_b) && vertice2_b.vecinos.contiene(vertice1_a))
            throw new IllegalArgumentException();
        vertice1_a.vecinos.agrega(vertice2_b);
        vertice2_b.vecinos.agrega(vertice1_a);
        aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        Vertice vertice1_a = (Vertice) vertice(a);
        Vertice vertice2_b = (Vertice) vertice(b);
        if (!vertice1_a.vecinos.contiene(vertice2_b) && !vertice2_b.vecinos.contiene(vertice1_a))
            throw new IllegalArgumentException();
        vertice1_a.vecinos.elimina(vertice2_b);
        vertice2_b.vecinos.elimina(vertice1_a);
        aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for (Vertice vertice1 : vertices )
            if (vertice1.elemento.equals(elemento))
                return true;
        return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        if (!contiene(elemento)) throw new NoSuchElementException();
        
        Vertice v1 = (Vertice)vertice(elemento);
        for (Vertice vecino : vertices)
            for (Vertice vI : vecino.vecinos)
                if (vI.equals(v1)) {
                    vecino.vecinos.elimina(v1);
                    aristas--;
                }

        vertices.elimina(v1);      
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        if (!this.contiene(a) || !this.contiene(b))
            throw new NoSuchElementException();
        Vertice vertice1_a = (Vertice) vertice(a);
        Vertice vertice2_b = (Vertice) vertice(b);
        return vertice1_a.vecinos.contiene(vertice2_b) && vertice2_b.vecinos.contiene(vertice1_a);
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        for (Vertice v : vertices)
            if (v.elemento.equals(elemento))
                return v;
        throw new NoSuchElementException();
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if (vertice == null || vertice.getClass() != Vertice.class)
            throw new IllegalArgumentException("Vértice inválido");
        Vertice v = (Vertice)vertice;
        v.color = color;
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        for(Vertice v : vertices){
            if(v.vecinos.esVacia())
                return false;
        }
        return true;
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for (Vertice v : vertices)
            accion.actua(v);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        Cola<Grafica<T>.Vertice> cola = new Cola<>();
        recorrido(elemento, accion, cola);
    }
    private void recorrido(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Grafica<T>.Vertice> meteSaca) {
        if(!this.contiene(elemento)) throw new NoSuchElementException();
        Vertice v = (Vertice)vertice(elemento);
        meteSaca.mete(v);
        while(!meteSaca.esVacia()) {
            Vertice vT = meteSaca.saca();
            setColor(vT,Color.ROJO);
            accion.actua(vT);
            for (Vertice ve : vT.vecinos)
                if (ve.color != Color.ROJO) {
                    setColor(ve,Color.ROJO);
                    meteSaca.mete(ve);
                }
        }
        paraCadaVertice(vertice -> setColor(vertice,Color.NINGUNO));
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        Pila<Grafica<T>.Vertice> pila = new Pila<>();
        recorrido(elemento, accion, pila);

    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return vertices.esVacia();  //super.esVacio() esVacia()
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        vertices.limpia();
        aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        Lista<T> lista = new Lista<T>();

        for(Vertice rojo : vertices)
            rojo.color = Color.ROJO;

        String srt = "{";
        String aristas = "{";

        for(Vertice vertice : vertices) {
            srt += vertice.elemento + ", ";
            for(Vertice adyacentes : vertice.vecinos) {
                if(adyacentes.color == Color.ROJO)
                    aristas += "(" + vertice.elemento + ", " + adyacentes.elemento + "), ";
                vertice.color = Color.NEGRO;
            }
            lista.agrega(vertice.elemento);
        }
        for(Vertice nulo : vertices)
            nulo.color = Color.NINGUNO;

        return srt + "}, " + aristas + "}";
    }   


    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la gráfica es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        if((getElementos() != grafica.getElementos()) || (aristas != grafica.aristas))
            return false;
        for(Vertice v : vertices) {
            v.color = Color.ROJO;
            if(!grafica.contiene(v.elemento))
                return false;
        }
        for(Vertice v : vertices) {
            for(Vertice y : v.vecinos){
                if(y.color == Color.ROJO)
                    if(!grafica.sonVecinos(y.get(),v.elemento))
                        return false;
            }
            v.color = Color.NEGRO;
        }
        return true;
    }

    private Vertice castingVertice(VerticeGrafica<T> v) {
        return (Vertice)v;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
