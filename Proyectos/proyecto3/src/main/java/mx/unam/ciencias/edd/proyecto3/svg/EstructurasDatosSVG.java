package mx.unam.ciencias.edd.proyecto3.svg;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.*;

/* Clase para dibujar Estructuras de Datos en SVG. */
public class EstructurasDatosSVG {

    /* Clase auxiliar para dibujar las graficas. */
    private class VerticeCoordenada implements Comparable<VerticeCoordenada> {

        private VerticeGrafica<String> vertice;
        private double x;
        private double y;

        /* Crea el vertice coordenada a partir de un vertice y sus coordenadas. */
        public VerticeCoordenada(VerticeGrafica<String> vertice, double x, double y) {
            this.vertice = vertice;
            this.x = x;
            this.y = y;
        }

        /* Compara entre dos VerticeCoordenada. */
        @Override public int compareTo(VerticeCoordenada vc) {
            return this.vertice.get().compareTo(vc.vertice.get());
        }

        /* Compara dos VerticeCoordeanda para ver si son iguales. */
        public boolean equals(VerticeCoordenada vc) {
            return vc.vertice.get().equals(this.vertice.get());
        }
    }

    private EtiquetasSVG utils;
    private final String xml;

    /* Constructor único que inicializa los valores. */
    public EstructurasDatosSVG() {
        utils = new EtiquetasSVG();
        xml = "";
    }

    /* Realiza el código SVG de un ArbolBinario */
    public String arbolBinario(ArbolBinario<Palabra> ab, EstructurasDeDatos arbol_a) {
        int padding = 4, largoSVG, altoSVG, radio;
        int iniX, iniY;
        String arbol;
        VerticeArbolBinario<Palabra> max;

        if (ab.esVacia())
            return xml;

        max = this.obtenerMaximo(ab.raiz());

        radio = (this.longitudString(max.get().toString()) * 5 + padding * 2) / 2;
        largoSVG = this.obtenerLongitudSVGArbol(ab, radio);
        altoSVG = this.obtenerAlturaSVGArbol(ab, radio);

        iniX = largoSVG / 2;
        iniY = radio * 3;

        arbol = this.obtenerVertices(ab.raiz(), radio, largoSVG / 2, iniX, iniY, arbol_a);
        return xml + "\n    <svg width='" + largoSVG + "' height='" + altoSVG + "' xmlns='http://www.w3.org/2000/svg'>\n" + arbol + "    </svg>\n";
    }

    /* Realiza el svg de una Grafica. */
    public String grafica(Grafica<String> g) {
        String grafica;
        int padding = 5, radio;
        int perimetro;
        String max;
        double radioG;
        double largoSVG, altoSVG;

        max = this.obtenerMaximo(g);

        radio = (this.longitudString(max) * 5 + padding * 2) / 2;
        perimetro = g.getElementos() * radio * 3;
        radioG = perimetro / Math.PI;

        largoSVG = altoSVG = radioG * 2 + radio * 2.0 * 2.0;

        grafica = this.obtenerVertices(g, radioG, radio, largoSVG / 2, altoSVG / 2);
        return xml + "\n    <svg width='" + largoSVG + "' height='" + altoSVG + "' xmlns='http://www.w3.org/2000/svg'>\n" + grafica + "    </svg>\n";
    }

    /* Obtiene la longitud de un número en termino de sus digitos. */
    private int longitudNumero(int n) {
        int i = 1;

        while (n >= 10) {
            n /= 10;
            i++;
        }

        return i;
    }

    private int longitudString(String s) {
        int i = 0, length = s.length();

        while (length > 0) {
            s = s.substring(0, --length);
            i++;
        }

        return i;
    }

    /* Auxiliar de arbolBinario. Obtiene el número más grande del subárbol. */
    private VerticeArbolBinario<Palabra> obtenerMaximo(VerticeArbolBinario<Palabra> vertice) {
        VerticeArbolBinario<Palabra> izq = null, der = null, max;

        if (vertice == null)
            return null;

        if (!vertice.hayIzquierdo() && !vertice.hayDerecho())
            return vertice;
        else {
            if (vertice.hayIzquierdo())
                izq = this.obtenerMaximo(vertice.izquierdo());
            if (vertice.hayDerecho())
                der = this.obtenerMaximo(vertice.derecho());
        }

        if (izq != null && der != null)
            max = ((longitudString(izq.get().toString()) >= (longitudString(der.get().toString()))) ? izq : der);
        else
            if (izq == null)
                max = der;
            else
                max = izq;

        return ((longitudString(vertice.get().toString()) >= longitudString(max.get().toString())) ? vertice : max);
    }

    /* Auxiliar de gráfica. Obtiene el número mas grande de la grafica. */
    private String obtenerMaximo(Grafica<String> g) {
        String max = "";

        for (String i : g) {
            max = i;
            break;
        }
        for (String i : g)
            if (longitudString(max) < longitudString(i))
                max = i;

        return max;
    }

    /* Auxiliar de arbolBinario. Genera la longitud del código SVG. */
    private int obtenerLongitudSVGArbol(ArbolBinario<Palabra> ab, int radio) {
        int numeroHojas = (int) Math.pow(2, ab.altura());

        return (numeroHojas + (numeroHojas / 16) + 2) * (radio * 2);
    }

    /* Auxiliar de arbolBinario. Genera la altura del SVG. */
    private int obtenerAlturaSVGArbol(ArbolBinario<Palabra> ab, int radio) {
        return (ab.altura() + 3) * (radio * 2);
    }

    /* Auxiliar de arbolBinario. Genera el código SVG con los vértices del árbol. */
    private String obtenerVertices(VerticeArbolBinario<Palabra> vertice, int radio, int i, int x, int y, EstructurasDeDatos arbol_a) {
        String arbol = "", color = "white", colorLetra = "black";
        i /= 2;

        if (vertice.hayIzquierdo()) {
            arbol += utils.linea(x, y, x - i, y + radio * 2);
            arbol += obtenerVertices(vertice.izquierdo(), radio, i, x - i, y + radio * 2, arbol_a);
        }
        if (vertice.hayDerecho()) {
            arbol += utils.linea(x, y, x + i, y + radio * 2);
            arbol += obtenerVertices(vertice.derecho(), radio, i, x + i, y + radio * 2, arbol_a);
        }


        if (vertice.toString().charAt(0) == ('R')) {
            color = "red";
            colorLetra = "white";
        }
        if (vertice.toString().charAt(0) == ('N')) {
            color = "black";
            colorLetra = "white";
        }

        arbol += utils.circuloConTexto(vertice.get().toString(), x, y, radio, color, colorLetra);

        if (arbol_a == EstructurasDeDatos.ArbolAVL)
            arbol += utils.texto(vertice.toString().split(" ")[1], x + radio - 10, y - (radio / 2) - 10, "text-anchor='middle'");

        return arbol;
    }

    /* Auxiliar de Gráfica. Genera el código SVG con los vertices y aristas de la grafica. */
    private String obtenerVertices(Grafica<String> g, double radioG, int radio, double x, double y)  {
        String vertices = "", aristas = "", color = "white", colorLetra = "black";
        double angulo = Math.toRadians(360 / g.getElementos());
        double anguloi = 0, xi, yi;
        int i = 0;
        VerticeCoordenada coordenadai;
        VerticeGrafica<String> vi = null;
        VerticeCoordenada[] coordenadas = new VerticeCoordenada[g.getElementos()];
        Arreglos arr = null;

        for (String v : g) {
            xi = radioG * Math.cos(anguloi);
            yi = radioG * Math.sin(anguloi);
            vertices += utils.circuloConTexto(v, x + xi, y + yi, radio, color, colorLetra);

            vi = g.vertice(v);
            coordenadai = new VerticeCoordenada(vi, x + xi, y + yi);
            coordenadas[i] = coordenadai;

            anguloi += angulo;
            i += 1;
        }

        arr.quickSort(coordenadas);

        for (VerticeCoordenada v : coordenadas)
            for (VerticeGrafica<String> vecino : v.vertice.vecinos()) {
                coordenadai = new VerticeCoordenada(vecino, 0, 0);
                coordenadai = coordenadas[arr.busquedaBinaria(coordenadas, coordenadai)];
                aristas += utils.linea(v.x, v.y, coordenadai.x, coordenadai.y);
            }

        return aristas + vertices;
    }
}
