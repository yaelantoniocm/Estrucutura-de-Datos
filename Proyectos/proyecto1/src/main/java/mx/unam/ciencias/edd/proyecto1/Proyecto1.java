package mx.unam.ciencias.edd.proyecto1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import mx.unam.ciencias.edd.Lista;

/*Proyecto 1: Debemos Implementar un ordenador lexicográfico (una versión más sencilla del
programa sort en Unix) que funcione con uno o más archivos de texto o la
entrada estándar y que imprima su salida en la salida estándar.
*/


public class Proyecto1 {

    //Variable estatica final para representar la ruta para guardar la bandera -o
    static String ruta;
    
    // Variables estaticas finales para representar nuestras banderas
    static final String CREA_TEXTO = "-o";
    static final String REVERSA = "-r";
    
    // Variables estaticas finales para representar los errores en las banderas.
    static final String EXCEPT_TEXTO = "¡Ocurrio un problema con el archivo!";
    static final String EXCEPT_ENTRADA_ESTANDAR = "¡Ocurrio un problema con la entrada!";
    
    static boolean activR = false;  //<- Es para ver si la bandera de -r se activo
    static boolean activO = false;  //<- Es para ver si la bandera de -o se activo
    
    //Lineas del texto.
    static Lista<Comparador> lineas_en_parrafos = new Lista<>();

    //Esta lista se encargara de guardar el texto
    static Lista<String> listaTexto = new Lista<>();


    public static void main(String[] args) {

        String entradaNor;
        banderasActiva(args);
        boolean entradaEst = lectorDeTextosArchivos(args, activR, activO);
        
        if(!entradaEst) {
            for(String archivos : listaTexto)
                try(BufferedReader bfr = new BufferedReader(new FileReader(archivos))) {
                    while((entradaNor = bfr.readLine()) != null)
                        //Usando lineas_en_parrafos se va a apasar las lineas del txt que se paso en la consola. 
                        lineas_en_parrafos.agrega(new Comparador(entradaNor));  
                }catch(IOException io) {
                    System.out.println(EXCEPT_TEXTO);
                    System.exit(-1);
                }
        }else  //En entrada estandar.
            try(BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in))) {    
                if(activO) { 
                    try{
                        File file = new File(ruta);
                        if(!file.exists())
                            file.createNewFile();
                        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                        while ((entradaNor = bfr.readLine()) != null) {
                            bw.write(entradaNor);
                        	bw.newLine();
                        }
                        bw.close();
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }else
                    while((entradaNor = bfr.readLine()) != null)
                        /*Usando lineas_en_parrafos se va a apasar las lineas que se paso en la consola, 
                        *pero ahora en la entrada estandar 
                        */
                        lineas_en_parrafos.agrega(new Comparador(entradaNor)); 
             }catch(IOException io) {
                System.out.println(EXCEPT_ENTRADA_ESTANDAR);
                System.exit(-1);
            }
            
            //Al poner la bandera de COLA_A_CABEZA (-r), se imprimira lo que esta hasta abajo, hasta arriba.
        for(Comparador cad : activR ? Lista.mergeSort(lineas_en_parrafos).reversa() : Lista.mergeSort(lineas_en_parrafos))
            System.out.println(cad);
    }
     
     /**
     * Este método nos dira si esta leyendo un archivo o leyendo el texto con la entrada estandar.
     * @param args Lo que recibimos en la consola, ya sea -r o -o
     * @param activR Nos mandara un booleno (true o false) si en la consola se nos mando la bandera -r.
     * @param activO Nos mandara un booleno (true o false) si en la consola se nos mando la bandera -o.
     */
    private static boolean lectorDeTextosArchivos(String[] args, boolean activR, boolean activO) {
        return (args.length == 0 || (args.length == 1 && activR) || (args.length >= 1 && activO)) ? true : false;
    }

     /*Metodo que recibira argumentos de la consola y nos indicará si recibio -r o -o;
     * los guardara en una lista si el argumento posee algun archivo.
     */
    private static void banderasActiva(String[] args) {

        for(int i=0;i<args.length;i++) {
        	if(args[i].equals(REVERSA)) activR = true;
            if(args[i].equals(CREA_TEXTO)) {  
            
               	activO = true;
            	ruta = args[i+1];
            }
            if(!args[i].equals(REVERSA) && !args[i].equals(CREA_TEXTO)) listaTexto.agrega(args[i]);
        }
    }
    //TODO CORRIO BIEN CON "mvn clean && mvn package && cat hombres.txt | java -jar target/proyecto1.jar"
}