package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.proyecto3.html.*;

public class Palabra implements Comparable<Palabra> {

    private String palabra;
    private int conteo;

    public Palabra(String palabra) {
        this.palabra = palabra;
    }

    public Palabra(String palabra, int conteo) {
        this.palabra = palabra;
        this.conteo = conteo;
    }

    public String getPalabra() {
        return this.palabra;
    }

    public int getConteo() {
        return this.conteo;
    }

    public void setConteo(int conteo) {
        this.conteo = conteo;
    }

    public void sumarConteo(int suma) {
        this.conteo += suma;
    }

    @Override public int compareTo(Palabra otra) {
        if (this.conteo < otra.conteo)
            return -1;
        if (this.conteo > otra.conteo)
            return 1;

        return 0;
    }

    @Override public String toString() {
        return palabra;
    }

    public boolean equals(Palabra p) {
        return p.palabra.equals(this.palabra) && p.conteo == this.conteo;
    }
}
