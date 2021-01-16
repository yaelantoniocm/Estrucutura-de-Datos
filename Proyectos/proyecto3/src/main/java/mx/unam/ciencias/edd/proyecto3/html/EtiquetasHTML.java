package mx.unam.ciencias.edd.proyecto3.html;

import mx.unam.ciencias.edd.*;

/* Clase que sirve para generar las etiquetas HTML utilizadas en el proyecto. */
public class EtiquetasHTML {

    public String getDoctypeHTML() {
        return "<!DOCTYPE html>\n";
    }

    public String getTitle(String titulo) {
        return "<title>" + titulo + "</title>\n";
    }

    public String getHead(String titulo) {
        return "<head><meta charset='UTF-8'>" + getTitle(titulo) + "</head>\n";
    }

    public String getHead(String titulo, String css) {
        return "<head><meta charset='UTF-8'>" + getCSS(css) + getTitle(titulo) + "</head>\n";

    }

    public String getP(String texto) {
        return "<p>" + texto + "</p>\n";
    }

    public String getHeader(String header) {
        return "<header><h1>" + header  + "</h1></header>\n";
    }

    public String getOpenTag(String tag, String extra) {
        return "<" + tag + " " + extra + " >";
    }

    public String getOpenTag(String tag) {
        return "<" + tag + ">\n";
    }

    public String getCloseTag(String tag) {
        return "</" + tag + ">\n";
    }

    public String getCSS(String css) {
        return "<link rel='stylesheet' type='text/css' href='" + css + "'>";
    }
}
