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
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().elemento;
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* La lista de vecinos del vértice. */
        public Diccionario<T, Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            this.vecinos = new Diccionario<T, Vecino>();
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

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
           this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
             if (distancia > vertice.distancia)
                return 1;
            else if (distancia < vertice.distancia)
                return -1;
            return 0;
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            return this.vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            return vecino.getColor();
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecino.vecinos;
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
    }

    /* Vértices. */
    private Diccionario<T, Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        this.vertices = new Diccionario<T, Vertice>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getElementos(); //getElementos
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
        if (elemento == null)
            throw new IllegalArgumentException();
        if(vertices.contiene(elemento)) {
            throw new IllegalArgumentException();
        }
        vertices.agrega(elemento, new Vertice(elemento));
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
        Vertice verticeA = (Vertice) vertice(a);
        Vertice verticeB = (Vertice) vertice(b);
        if (a.equals(b) || sonVecinos(a, b))
            throw new IllegalArgumentException();
        verticeA.vecinos.agrega(b, new Vecino(verticeB, 1));
        verticeB.vecinos.agrega(a, new Vecino(verticeA, 1));
        aristas++;
    }

    // Es un método encargado de buscar al vecino del vertice.
    private Vecino buscaVecino(Vertice vertice1, Vertice vertice2) {
        if(vertice1.vecinos.contiene(vertice2.get())) {
            return vertice1.vecinos.get(vertice2.get());
        }
        return null;
    }
    
    // Es un método encargado de buscar a un vertice del vertice
    private Vertice buscaVertice(T elemento) {
        return vertices.get(elemento);
    }

    
    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
       if(!contiene(a) || !contiene(b)) { throw new NoSuchElementException(); }
        if(a.equals(b) || sonVecinos(a,b) || peso < 0) { throw new IllegalArgumentException(); }

        Vertice verticeA = (Vertice) vertice(a);
        Vertice verticeB = (Vertice) vertice(b);

        verticeA.vecinos.agrega(b, new Vecino(verticeB, peso));
        verticeB.vecinos.agrega(a, new Vecino(verticeA, peso));
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
        Vertice verticeA = (Vertice) vertice(a);
        Vertice verticeB = (Vertice) vertice(b);
        if (a.equals(b) || !sonVecinos(a, b))
            throw new IllegalArgumentException( );
        if (verticeA == null || verticeB == null) { 
            throw new NoSuchElementException(); 
        }
        Vecino ve_ab = verticeA.vecinos.get(verticeB.get());
        Vecino ve_ba = verticeB.vecinos.get(verticeA.get());
        verticeA.vecinos.elimina(ve_ab.get());
        verticeB.vecinos.elimina(ve_ba.get());
        aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return vertices.contiene(elemento);
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        if (!contiene(elemento))
            throw new NoSuchElementException();
        Vertice vertice = (Vertice) vertice(elemento);
        for (Vertice ver : vertices)
            for (Vecino vec : ver.vecinos)
                if (vec.vecino.equals(vertice)) {
                    ver.vecinos.elimina(vec.get());
                    aristas--;
                }
        vertices.elimina(vertice.get());
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
            throw new NoSuchElementException("a o b no son elementos de la gráfica");
        Vertice verticeA = (Vertice)vertice(a);
        Vertice verticeB = (Vertice)vertice(b);
        return verticeA.vecinos.contiene(verticeB.get());
    }


    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        
        Vertice v = this.buscaVertice(a);
        Vertice u = this.buscaVertice(b);
        if (v == null || u == null) { 
            throw new NoSuchElementException(); 
        }
        if (!this.sonVecinos(a, b)) { 
            throw new IllegalArgumentException(); 
        }
        return this.buscaVecino(v, u).peso;
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
       Vertice fst = (Vertice) vertice(a);
        Vertice scnd = (Vertice) vertice(b);
        
        if(!sonVecinos(a,b)) 
            throw new IllegalArgumentException();
        
        for (Vecino posicionado : fst.vecinos) {
            if (posicionado.vecino.equals(scnd)){
                posicionado.peso = peso;
            }
        }
        for (Vecino aux : scnd.vecinos) {
            if (aux.vecino.equals(fst)){
                aux.peso = peso;
            } 
        } 
    }
    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        return vertices.get(elemento);
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if (vertice == null || vertice.getClass() != Vertice.class &&
             vertice.getClass() != Vecino.class){
            throw new IllegalArgumentException();
        }
        if (vertice.getClass() == Vertice.class) {
            Vertice vert1 = (Vertice) vertice;
            vert1.color = color;
        }
        if (vertice.getClass() == Vecino.class) {
            Vecino vec1 = (Vecino) vertice;
            vec1.vecino.color = color;
        }
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        for (Vertice v : vertices)
            v.color = Color.ROJO;

        Cola<Vertice> cola = new Cola<Vertice>();
        Iterator<T> it = vertices.iteradorLlaves();
        T llave = it.next();
        cola.mete(vertices.get(llave));
        vertices.get(llave).color = Color.NEGRO;

        while(!cola.esVacia()){
            Vertice aux = cola.saca();
            for (Vecino vec: aux.vecinos){
                if(vec.vecino.color == Color.ROJO) {
                    vec.vecino.color = Color.NEGRO;
                    cola.mete(vec.vecino);
                }
            }
        }
        for(Vertice vert1 : this.vertices)
            if(vert1 .color != Color.NEGRO)
                return false;
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
        Vertice v1 = this.buscaVertice(elemento);
        if (v1 == null) { throw new NoSuchElementException(); }
        Cola<Vertice> queue = new Cola<Vertice>();

        this.paraCadaVertice((vert) -> this.setColor(vert, Color.NINGUNO));
        queue.mete(v1); v1.color = Color.NEGRO;

        while(!queue.esVacia()) {
            Vertice aux = queue.saca();
            accion.actua(aux);
            for (Vecino n: aux.vecinos) {
                if(n.vecino.color == Color.NINGUNO) {
                    queue.mete(n.vecino);
                    n.vecino.color = Color.NEGRO;
                }
            }
        }

        this.paraCadaVertice((vert) -> this.setColor(vert, Color.NINGUNO));
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
        Vertice v1 = this.buscaVertice(elemento);
        if (v1 == null) { throw new NoSuchElementException(); }
        Pila<Vertice> stack = new Pila<Vertice>();

        this.paraCadaVertice((vert) -> this.setColor(vert, Color.NINGUNO));
        stack.mete(v1); v1.color = Color.NEGRO;

        Vertice aux;
        while(!stack.esVacia()) {
            aux = stack.saca();
            accion.actua(aux);
            for (Vecino n: aux.vecinos) {
                if(n.vecino.color == Color.NINGUNO) {
                    stack.mete(n.vecino);
                    n.vecino.color = Color.NEGRO;
                }
            }
        }

        this.paraCadaVertice((vert) -> this.setColor(vert, Color.NINGUNO));
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return vertices.esVacia();
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

        String cadena = "{";
        String aristas = "{";

        for(Vertice vert1 : vertices) {
            cadena += vert1.elemento + ", ";
            for(Vecino ady : vert1.vecinos) {
                if(ady.vecino.color==Color.ROJO)
                    aristas += String.format("(" + vert1.get().toString() + ", " + ady.get().toString() + "), ");
                vert1.color = Color.NEGRO;
            }
            lista.agrega(vert1.elemento);
        }
        for(Vertice nulo : vertices)
            nulo.color = Color.NINGUNO;

        return cadena + "}, " + aristas + "}";
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
        for(Vertice vert : vertices) {
            vert.color = Color.ROJO;
            if(!grafica.contiene(vert.elemento)) { 
                return false; 
            }
        }
        for(Vertice vert : vertices) {
            for(Vecino y : vert.vecinos){
                if(y.getColor() == Color.ROJO)
                    if(!grafica.sonVecinos(y.get(),vert.elemento)){ 
                        return false; 
                    }
            }
            vert.color = Color.NEGRO;
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <tt>a</tt> y
     *         <tt>b</tt>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        if(!contiene(origen) || !contiene(destino)) { throw new NoSuchElementException(); }

        Lista<VerticeGrafica<T>> lista = new Lista<VerticeGrafica<T>>();
        Cola<Vertice> cola = new Cola<Vertice>();

        Vertice vo = buscaVertice(origen);
        Vertice vd = buscaVertice(destino);

        if(origen.equals(destino)) {
            lista.agrega(vo);
            return lista;
        }
        for(Vertice v : vertices)
            v.distancia = -1;
    
        vo.distancia = 0;

        cola.mete(vo);

        while(!cola.esVacia()) {
            vo = cola.saca();
            for(Vecino vecino : vo.vecinos)
                if(vecino.vecino.distancia == -1) {
                    vecino.vecino.distancia = vo.distancia +1;
                    cola.mete(vecino.vecino);
                }
        }

        if(vd.distancia == -1) { return lista; }

        lista.agrega(vd);

        while(!vo.elemento.equals(origen))
            for(Vecino vertice : vo.vecinos)
                if(vo.distancia == vertice.vecino.distancia + 1) {
                    lista.agrega(vertice.vecino);
                    vo = vertice.vecino;
                }

        return lista.reversa();
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <tt>origen</tt> y
     *         el vértice <tt>destino</tt>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        if(!contiene(origen) || !contiene(destino)) { throw new NoSuchElementException(); }

        Lista<VerticeGrafica<T>> lista = new Lista<VerticeGrafica<T>>();
        MonticuloMinimo<Vertice> montMin;

        Vertice vertOrg = (Vertice)vertice(origen);
        Vertice vertDest = (Vertice)vertice(destino);

        for(Vertice v : vertices)
            v.distancia = Double.MAX_VALUE;

        vertOrg.distancia = 0;

        montMin = new MonticuloMinimo<Vertice>(vertices, vertices.getElementos());
        double aux;

        while(!montMin.esVacia()) {
            Vertice v = montMin.elimina();
            for(Vecino ve : v.vecinos) {
                aux = ve.peso;
                if(ve.vecino.distancia > (v.distancia + aux)) {
                    ve.vecino.distancia = v.distancia + aux;
                    montMin.reordena(ve.vecino);
                }
            }
        }
        if(vertDest.distancia == Double.MAX_VALUE) { return lista; }
        Vertice v = vertDest;
        lista.agrega(v);

        while(!v.elemento.equals(origen)) {
            for(Vecino ve : v.vecinos) {
                aux = ve.peso;
                if(v.distancia == ve.vecino.distancia + aux) {
                    lista.agrega(ve.vecino);
                    v = ve.vecino;
                }
            }
        }
        return lista.reversa();
    }
}
