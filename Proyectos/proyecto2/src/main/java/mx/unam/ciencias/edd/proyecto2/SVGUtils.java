package mx.unam.ciencias.edd.proyecto2;

/**
 * Clase para crear elementos en SVG.
 */
public class SVGUtils {

	/* Utils */
	private int longitudNumero(int n) {
		int i = 1;
		while(n >= 10) {
			n /= 10;
			i++;
		}
		return i;
	}

	public String texto(String texto, double x, double y, String extra) {
		return "<text x='"+ x +"' y='"+ y +"' font-size='20' "+ extra +">"+ texto +"</text>\n";
	}

	public String numero(int n, double x, double y, String extra) {
		return "<text x='"+ x +"' y='"+ y +"' font-size='20' "+ extra +">"+ n +"</text>\n";
	}

	public String rectangulo(double base, double altura, double x, double y) {
		return "<rect x='"+ x +"' y='"+ y +"' width='"+ base +"' height='"+ altura +"' stroke='black' stroke-width='1' fill='white'/>\n";
	}
	public String rectanguloConTexto(String texto, double x, double y) {
		return rectangulo(texto.length()*10+10, 25, x, y) + texto(texto, x+5, y+20, "");
	}

	public String rectanguloConNumero(int n, double x, double y, int padding, int altura) {
		return rectangulo(longitudNumero(n)*10+padding*2, altura, x, y) + numero(n, x+padding, y+20,"");
	}

	public String circulo(double radio, double x, double y, String color) {
		String color_s = "";
		if(!color.equals(""))
			color_s="fill='"+ color +"'";
		return "<circle cx='"+ x +"' cy='"+ y +"' r='"+ radio +"' "+ color_s +" stroke='black' stroke-width='1'/>\n";
	}
	public String circuloConNumero(int n, double x, double y, int radio, String color, String colorLetra) {
		return circulo(radio, x, y, color) + numero(n, x, y+5, "text-anchor='middle' fill='"+ colorLetra +"'");
	}

	public String linea(double x1, double y1, double x2, double y2) {
		return "<line x1='"+ x1 +"' y1='"+ y1 +"' x2='"+ x2 +"' y2='"+ y2 +"' stroke='black' stroke-width='2'/>\n";
	}
	public String dobleFlecha(double x, double y) {
		return texto("↔", x, y, "font-weight='bold'");
	}
	public String flechaDerecha(double x, double y) {
		return texto("→", x, y, "font-weight='bold'");
	}
}