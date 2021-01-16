package mx.unam.ciencias.edd.proyecto3.svg;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.Palabra;

/* Clase para generar un código SVG de gráfica de pastel. */
public class PastelSVG<T extends Coleccion<Palabra>> {

    private double[] getCoordenadas(double cx, double cy, double radio, double angulo) {
        double[] coordenadas = new double[2];

        angulo = Math.toRadians(angulo);
        coordenadas[0] = cx + radio * Math.cos(angulo);
        coordenadas[1] = cy + radio * Math.sin(angulo);

        return coordenadas;
    }

    private String getPedazo(double angulo1, double angulo2, double cx, double cy, double radio, String color) {
        EtiquetasSVG svgUtil = new EtiquetasSVG();

        double[] co1 = getCoordenadas(cx, cy, radio, angulo1);
        double[] co2 = getCoordenadas(cx, cy, radio, angulo2);

        String extra = "stroke='#fff' stroke-width='2' ";

        return svgUtil.path(co1[0], co1[1], co2[0], co2[1], cx, cy, radio, color, extra);
    }

    public String generarPastel(double cx, double cy, double radio, T col, String[] colores) {
        int total = 0, i = 0;
        double porcentaje, angulo, anguloi = 0, angulof = 0;
        String svg = "";
        String barra = "";
        double aux = 0;
        int max = 0;

        svg += "\n    <svg width='500' height='500' xmlns='http://www.w3.org/2000/svg'>\n";

        for (Palabra palabra : col) {
            total += palabra.getConteo();

            if (palabra.getConteo() > max)
                max = palabra.getConteo();
        }

        barra += "\n    <svg width='" + (max + 49) + "' height='" + (colores.length * 20) +"' xmlns='http://www.w3.org/2000/svg'>";

        for (Palabra palabra : col) {
            porcentaje = palabra.getConteo() * 100.0 / total;
            angulo = porcentaje * 360 / 100;

            anguloi = angulof;
            angulof += angulo;

            svg += getPedazo(anguloi, angulof, cx, cy, radio, colores[i]);
            barra += EtiquetasSVG.barra(porcentaje, aux, colores[i]);

            aux += 20;
            i++;
        }

        svg += "    </svg>\n";
        barra += "\n    </svg>\n";

        return barra + svg;
    }
}
