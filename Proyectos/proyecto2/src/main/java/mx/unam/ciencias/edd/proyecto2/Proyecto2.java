package mx.unam.ciencias.edd.proyecto2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;

import mx.unam.ciencias.edd.*;

public class Proyecto2 {

	/* Obtiene elementos de un arreglo para meterlos en una lista. */
	public static Lista<Integer> obtenerElementosLista(Lista<Integer> l, String[] elementos) throws NumberFormatException {
		for(String i: elementos) 
			l.agrega(Integer.parseInt(i));
		return l;
	}

	/* Obtiene elementos de un arreglo para volverlos Indexeables y meterlos en una Lista. */
	public static Lista<Indexable<Integer>> obtenerElementosListaIndexable(Lista<Indexable<Integer>> li, String[] elementos) throws NumberFormatException {
		Lista<Integer> l = obtenerElementosLista(new Lista<Integer>(), elementos);
		for(int i:l)
			li.agrega(new Indexable<Integer>(i, i));
		return li;
	}

	/* Obtiene elementos de un arreglo para meterlos en una Pila. */
	public static Pila<Integer> obtenerElementosPila(Pila<Integer> p, String[] elementos) throws NumberFormatException {
		for(String i: elementos) { p.mete(Integer.parseInt(i)); }
		return p;
	}

	/* Obtiene elementos de un arreglo para meterlos en una cola. */
	public static Cola<Integer> obtenerElementosCola(Cola<Integer> c, String[] elementos) throws NumberFormatException {
		for(String i: elementos) 
			c.mete(Integer.parseInt(i));
		return c;
	}


	/* Obtiene elementos de un arreglo para meterlos en un ArbolAVL. */
	public static ArbolAVL<Integer> obtenerElementosArbolAVL(ArbolAVL<Integer> ab, String[] elementos) throws NumberFormatException {
		for(String i: elementos)
			ab.agrega(Integer.parseInt(i));
		return ab;
	}

	/* Obtiene elementos de un arreglo para meterlos en un ArbolRojinegro. */
	public static ArbolRojinegro<Integer> obtenerElementosArbolRN(ArbolRojinegro<Integer> ab, String[] elementos) throws NumberFormatException {
		for(String i: elementos)
			ab.agrega(Integer.parseInt(i));
		return ab;
	}


	/* Obtiene elementos y relaciones de dos arreglos para meterlos en una Grafica. */
	public static Grafica<Integer> obtenerElementosGrafica(Grafica<Integer> g, Lista<Integer> elementos, Lista<String> relaciones) throws NumberFormatException {
		try {
			for(int i: elementos)
			g.agrega(i);

		for(int i=0;i<relaciones.getLongitud();i++)
			g.conecta(Integer.parseInt(relaciones.get(i).substring(0,1)), Integer.parseInt(relaciones.get(i).substring(1,2)));
		
		}
		catch(Exception e){

			System.out.println("No mande elementos repetidos");
			System.exit(-1);
		}

		return g;
	}

	/* Lee los elementos que se le pasaron después del tipo de estructura de datos.*/
	public static void leerElementos(String[] elementos, Lista<String> relaciones, Lista<Integer> elementosG, EstructurasDeDatos estructuraE) throws Exception {

		Lista<Integer> lista = new Lista<Integer>();
		EddSVG edSVG = new EddSVG();

			switch(estructuraE) {
				case Lista:
					lista = obtenerElementosLista(new Lista<Integer>(), elementos);
					System.out.println(edSVG.lista(lista));
					break;
				case Cola:
					Cola<Integer> cola = new Cola<Integer>();
					cola = obtenerElementosCola(cola, elementos);
					System.out.println(edSVG.meteSaca(cola));
					break;
				case Pila:
					Pila<Integer> pila = new Pila<Integer>();
					pila = obtenerElementosPila(pila, elementos);
					System.out.println(edSVG.meteSaca(pila));
					break;
				case ArbolBinario:
					lista = obtenerElementosLista(new Lista<Integer>(), elementos);
					ArbolBinarioOrdenado<Integer> arbolO = new ArbolBinarioOrdenado<Integer>(lista);
					System.out.println(edSVG.arbolBinario(arbolO, estructuraE));
					break;
				case ArbolBinarioCompleto:
					lista = obtenerElementosLista(new Lista<Integer>(), elementos);
					ArbolBinarioCompleto<Integer> arbolC = new ArbolBinarioCompleto<Integer>(lista);
					System.out.println(edSVG.arbolBinario(arbolC, estructuraE));
					break;
				case ArbolRojinegro:
					ArbolRojinegro<Integer> arbolRN = new ArbolRojinegro<Integer>();
					arbolRN = obtenerElementosArbolRN(arbolRN, elementos);
					System.out.println(edSVG.arbolBinario(arbolRN, estructuraE));
					break;
				case ArbolAVL:
					ArbolAVL<Integer> arbolAVL = new ArbolAVL<Integer>();
					arbolAVL = obtenerElementosArbolAVL(arbolAVL, elementos);
					System.out.println(edSVG.arbolBinario(arbolAVL, estructuraE));
					break;
				case Monticulo:
					Lista<Indexable<Integer>> li = obtenerElementosListaIndexable(new Lista<Indexable<Integer>>(), elementos);
					MonticuloMinimo<Indexable<Integer>> monticulo = new MonticuloMinimo<Indexable<Integer>>(li);
					System.out.println(edSVG.monticulo(monticulo));
					break;
				case Grafica:
					Grafica<Integer> g = obtenerElementosGrafica(new Grafica<Integer>(), elementosG, relaciones);
					System.out.println(edSVG.grafica(g));
					break;
			}
	}

	/* Lee un archivo de texto. */
	private static void leeArchivo(String path) throws Exception {

		EstructurasDeDatos estructura = null;
		Lista<Integer> elem = new Lista<Integer>();
		Lista<String> st = new Lista<String>();
		String cad = "";
		Lista<String> relaciones = new Lista<String>();
		Lista<Integer> elementosG = new Lista<Integer>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while((br.readLine()) != null)
   				st.agrega(br.readLine().trim());
   		}catch(Exception e) {
   			System.out.println("Archivo de textos no válido.");
   			System.exit(-1);
   		}

   		try {
			estructura = EstructurasDeDatos.valueOf(st.get(0));
		}catch(IllegalArgumentException e) {
			System.out.println("Estructura no válida.");
			System.exit(-1);
		}

		for(int i=1;i<st.getLongitud();i++)
			cad += "-" + st.get(i).replace(" ","-");
		String[] elems = cad.split("-");

		try {
			for(int i=1;i<elems.length;i++)
				elem.agrega(Integer.parseInt(elems[i]));
		}catch(NumberFormatException nfe) {
			System.out.println("Introducir solamente numeros enteros.");
			System.exit(-1);
		}

		if(estructura == EstructurasDeDatos.Grafica) {
			for(int i=0;i<elem.getLongitud();i++) {
				if(elem.get(i)<10)
					elementosG.agrega(elem.get(i)); 
				else
					relaciones.agrega(Integer.toString(elem.get(i))); 
			}
		}

		String[] elementos = new String[elem.getLongitud()];
		for(int i=0;i<elem.getLongitud();i++)
			elementos[i] = Integer.toString(elem.get(i));

		leerElementos(elementos,relaciones,elementosG,estructura);
		System.exit(-1);
	}


	public static void main(String[] args) throws Exception {

		EstructurasDeDatos estructuraE = null;
		String edd = args[0];
		String[] elementos = new String[args.length-1];
		Lista<String> l = new Lista<String>();
		int[] enteros = new int[args.length-1];
		Lista<Integer> elementosG = new Lista<Integer>();
		Lista<String> relaciones = new Lista<String>();

		if(args.length == 1) leeArchivo(args[0].trim());

		try {
			estructuraE = EstructurasDeDatos.valueOf(edd);
		}catch(IllegalArgumentException e) {
			System.out.println("Estructura inválida.");
		}

		if(args.length<2) {
			System.out.println("Introduce elementos.");
			System.exit(-1);
		} 

		for(int i=1;i<args.length;i++)
			l.agrega(args[i].trim());
		for(int n=0;n<args.length-1;n++)
			elementos[n] = l.get(n);

		if(estructuraE == EstructurasDeDatos.Grafica) {
			for(int n=0;n<args.length-1;n++)
				enteros[n] = Integer.parseInt(l.get(n));
			for(int i=0;i<enteros.length;i++) {
				if(enteros[i]<10)
					elementosG.agrega(enteros[i]); 
				else
					relaciones.agrega(Integer.toString(enteros[i])); 
			}
		}
		leerElementos(elementos,relaciones,elementosG,estructuraE);

	}

}