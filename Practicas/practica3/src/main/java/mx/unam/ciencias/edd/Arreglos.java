package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void quickSort(T[] arreglo, Comparator<T> comparador) {
        int start = 0;
        int end = arreglo.length-1;
        quickSort(arreglo,start,end,comparador);
    }

    private static <T> void quickSort(T[] arreglo, int start, int end, Comparator<T> comparador) {
        if(end <= start) return;

        int x = start + 1; 
        int y = end;

        while (x < y)
            if (comparador.compare(arreglo[x],arreglo[start])>0 && comparador.compare(arreglo[y],arreglo[start]) <= 0)
                intercambiar(arreglo, x++, y--);
            else if (comparador.compare(arreglo[x],arreglo[start]) <= 0)
                x++;
            else
                y--;

        if(comparador.compare(arreglo[x],arreglo[start]) > 0)
            x--;

        intercambiar(arreglo, x, start);
        quickSort(arreglo, start, x-1, comparador);
        quickSort(arreglo, x+1, end, comparador);      
    }

    private static <T> void intercambiar(T[] a, int x, int y){
        T aux = a[x];
        a[x] = a[y];
        a[y] = aux;
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void selectionSort(T[] arreglo, Comparator<T> comparador) {
        for(int i = 0; i < arreglo.length; i++){
            int x = i;
            for(int j = i+1; j < arreglo.length; j++)
                if(comparador.compare(arreglo[j], arreglo[x]) < 0)
                    x = j;
                intercambiar(arreglo, i ,x);
            
        }
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        int start = 0;
        int end = arreglo.length-1;

        while (start <= end) {
            int middle = start + (end-start) / 2;
            if(comparador.compare(elemento,arreglo[middle])==0)
                return middle;

            if(comparador.compare(elemento,arreglo[middle])<0)
                end = middle-1;

            if(comparador.compare(elemento,arreglo[middle])>0)
                start = middle+1;

           
        }   
    
        return -1;  
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
