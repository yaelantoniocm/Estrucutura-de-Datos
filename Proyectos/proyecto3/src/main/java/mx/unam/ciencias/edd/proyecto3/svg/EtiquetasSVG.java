package mx.unam.ciencias.edd.proyecto3.svg;


/* Clase para crear elementos en SVG */
public class EtiquetasSVG {

    public String texto(String texto, double x, double y, String extra) {
        return "      <text x='" + x + "' y='" + y + "' font-size='12' " + extra +">" + texto + "</text>\n";
    }

    public String numero(int n, double x, double y, String extra) {
        return "      <text x='" + x + "' y='" + y + "' font-size='20' " + extra + ">" + n + "</text>\n";
    }

    public static String barra(double width, double y, String color) {
        String s = "\n      <g class='bar'>\n";
        s += "        <rect width='" + (width * 5) + "' height='" + 19 + "' y='"+ y +"' fill='" + color + "'></rect>";

        return s;
    }

    public String rectangulo(double base, double altura, double x, double y) {
        return "      <rect x='" + x + "' y='" + y + "' width='" + base + "' height='" + altura + "' stroke='black' stroke-width='1' fill='white'/>\n";
    }

    public String rectangulo(double base, double altura, double x, double y, String extra) {
        return "      <rect x='" + x + "' y='" + y + "' width='" + base + "' height='" + altura + "' stroke='black' stroke-width='1' " + extra + "/>\n";
    }

    public String circulo(double radio, double x, double y, String color) {
        String color_s = "";

        if (!color.equals(""))
            color_s="fill='" + color + "'";

        return "      <circle cx='" + x + "' cy='" + y + "' r='" + radio + "' " + color_s + " stroke='black' stroke-width='1'/>\n";
    }

    public String linea(double x1, double y1, double x2, double y2) {
        return "      <line x1='"+ x1 +"' y1='"+ y1 +"' x2='"+ x2 +"' y2='"+ y2 +"' stroke='black' stroke-width='2'/>\n";
    }

    public String path(double x1, double y1, double x2, double y2, double cx, double cy, double radio, String color, String extra) {
        String path = "      <path ";

        path += "d='M" + cx + ", " + cy + " L" + x1 + "," + y1 + " A" + radio + "," + radio;
        path += " 0 0, 1 " + x2 + "," + y2 + " z' fill='" + color + "' " + extra + ">";

        return path + "</path>\n";
    }

    public String rectanguloConTexto(String texto, double x, double y) {
        return this.rectangulo(texto.length() * 10 + 10, 25, x, y) + this.texto(texto, x + 5, y + 20, "");
    }

    public String rectanguloConNumero(int n, double x, double y, int padding, int altura) {
        return this.rectangulo(this.longitudNumero(n) * 10 + padding * 2, altura, x, y) + this.numero(n, x + padding, y + 20, "");
    }

    public String circuloConNumero(int n, double x, double y, int radio, String color, String colorLetra) {
        return this.circulo(radio, x, y, color) + this.numero(n, x, y + 5, "text-anchor='middle' fill='" + colorLetra + "'");
    }

    public String circuloConTexto(String texto, double x, double y, int radio, String color, String colorLetra) {
        return this.circulo(radio, x, y, color) + this.texto(texto, x, y + 5, "text-anchor='middle' fill='" + colorLetra + "'");
    }

    private int longitudNumero(int n) {
        int i = 1;

        while (n >= 10) {
            n /= 10;
            i++;
        }

        return i;
    }
}
