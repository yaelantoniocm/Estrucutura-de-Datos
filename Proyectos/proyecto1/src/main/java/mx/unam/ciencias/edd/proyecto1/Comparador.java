package mx.unam.ciencias.edd.proyecto1;

import java.text.Normalizer;
import java.text.Normalizer.Form;

 /**
 * Clase que recibira un String
 * Esta clase nos servira para comparar todas las cadenas, ignorando
 * mayúsculas, minúsculas y carácteres especiales.
 * para poder sobreescibir el método se implemento {@link Comparable}
 */

public class Comparador implements Comparable<Comparador> {

	String cadena;

	public Comparador(String cadena) {
		this.cadena = cadena;
	}

	//Método que regresara la copia de la cadena.
	@Override public String toString() {
		return cadena;
	}

	//Método que va hacer las comparaciones.
	@Override public int compareTo(Comparador cad) {
		String cadr = Normalizer.normalize(cad.toString() , Form.NFD)
		.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
		.replaceAll("[^a-zA-Z0-9]", "")
		.toLowerCase();
		String cads = Normalizer.normalize(this.cadena, Form.NFD)
		.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
		.replaceAll("[^a-zA-Z0-9]", "")
		.toLowerCase();
		return cads.compareTo(cadr);
		
	}

}