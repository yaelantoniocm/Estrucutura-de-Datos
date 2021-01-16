package mx.unam.ciencias.edd.proyecto3.html;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.*;
import mx.unam.ciencias.edd.proyecto3.svg.*;

import java.io.IOException;
import java.io.File;
import java.util.Iterator;

/* Clase que genera el archivo index.html. */
public class GeneradorIndex {

    private String html;
    private Diccionario<String, Diccionario<String, Integer>> archivos_palabras;
    private EtiquetasHTML htmlUtil;
    private String directorio;

    public GeneradorIndex(Diccionario<String, Diccionario<String, Integer>> archivos_palabras, String directorio) {
        this.html = "";
        this.archivos_palabras = archivos_palabras;
        this.htmlUtil = new EtiquetasHTML();
        this.directorio = directorio;
    }

    private void generarLinks() {
        html += htmlUtil.getOpenTag("ul");
        File archivo_actual;
        Iterator<String> iteradorLLave = archivos_palabras.iteradorLlaves();

        while (iteradorLLave.hasNext()) {
            String llave = iteradorLLave.next();
            archivo_actual = new File(llave);

            html += htmlUtil.getOpenTag("li");
            html += htmlUtil.getOpenTag("a", "href='"+ archivo_actual.getName() +".html'");
            html += archivo_actual.getName() + ": " + archivos_palabras.get(llave).getElementos() + " palabras.";
            html += htmlUtil.getCloseTag("a");
            html += htmlUtil.getCloseTag("li");
        }

        html += htmlUtil.getCloseTag("ul");
    }

    private void generarGrafica() {
        Grafica<String> g = new Grafica<String>();
        EstructurasDatosSVG edSVG = new EstructurasDatosSVG();
        File archivo_actual;
        Diccionario<String, Conjunto<String>> conjuntos = new Diccionario<String, Conjunto<String>>(archivos_palabras.getElementos());
        Conjunto<String> actual;
        Iterator<String> iteradorLLave = archivos_palabras.iteradorLlaves();

        while (iteradorLLave.hasNext()) {
            String llave = iteradorLLave.next();
            archivo_actual = new File(llave);

            g.agrega(archivo_actual.getName());
            actual = new Conjunto<String>(archivos_palabras.get(llave).getElementos());

            Iterator<String> iteradorLlaveAux = archivos_palabras.iteradorLlaves();

            while (iteradorLlaveAux.hasNext()) {
                String palabra = iteradorLlaveAux.next();

                if (palabra.length() >= 5)
                    actual.agrega(palabra);
            }
                
            conjuntos.agrega(archivo_actual.getName(), actual);
        }

        for (String v : g)
            for (String vi : g)
                if (!v.equals(vi))
                    if (conjuntos.get(v).interseccion(conjuntos.get(vi)).getElementos() > 6 && !g.sonVecinos(v, vi))
                        g.conecta(v, vi);

        html += edSVG.grafica(g);
    }

    private void generarCSS() {
        html += htmlUtil.getOpenTag("style");
        html += "#grafica{margin:0 auto;width: 1000px;}";
        html += htmlUtil.getCloseTag("style");
    }

    private void generarBody() {
        html += htmlUtil.getOpenTag("body");
        html += htmlUtil.getHeader("Proyecto 3. Estructuras de datos.");
        html += htmlUtil.getOpenTag("section");
        html += htmlUtil.getOpenTag("div");

        generarLinks();

        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getP("Gráfica donde cada archivo es un vértice, y dos archivos son vecinos si ambos contienen al menos 1 palabara en común, con al menos 7 caracteres cada una.");
        html += htmlUtil.getOpenTag("div", "id='grafica'");

        generarGrafica();

        html += htmlUtil.getCloseTag("div");
        html += htmlUtil.getCloseTag("section");
        html += htmlUtil.getCloseTag("body");
    }

    public void generarHTML() {
        html += htmlUtil.getDoctypeHTML();
        html += htmlUtil.getOpenTag("html");
        html += htmlUtil.getHead("Índice de archivos");

        generarCSS();
        generarBody();
        
        html += htmlUtil.getCloseTag("html");
    }

    public String getHTML() {
        return html;
    }
}
