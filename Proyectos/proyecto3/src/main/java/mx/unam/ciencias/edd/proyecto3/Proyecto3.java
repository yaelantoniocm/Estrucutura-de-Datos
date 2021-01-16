package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.excepciones.*;
import mx.unam.ciencias.edd.proyecto3.html.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.Normalizer;
import java.text.Normalizer.Form;


/* Proyecto 3 */
public class Proyecto3 {

    public static void mostrarError(String error) {
        System.err.println(error);
        System.exit(-1);
    }

    public static void proyecto3(String[] args) throws ExcepcionDirectorioNoValido, ExcepcionArchivoNoValido, IOException {
        BufferedReader br = null;
        BufferedWriter bw = null;
        Diccionario<String, Diccionario<String, Integer>> archivos_palabras;
        Diccionario<String, Integer> palabras;
        Lista<String> archivos = new Lista<String>();
        String linea_actual = "", texto, directorio = null;
        String[] palabras_actual = {"h"};
        GeneradorArchivoHTML generador;
        File archivo_actual;
        Grafica<String> archivos_g = new Grafica<String>();

        for (int i = 0; i < args.length; i++)
            if (args[i].equals("-o")) {
                if (args.length > i + 1)
                    directorio = args[++i];
            } else {
                archivo_actual = new File(args[i]);
                try {
                    archivos_g.agrega(archivo_actual.getName());
                } catch (IllegalArgumentException e) {
                    mostrarError("Hay dos archivos que se llaman igual.");
                }
                archivos.agrega(args[i]);
            }

        if (directorio == null)
            throw new ExcepcionDirectorioNoValido("No fue agregado un directorio de destino");

        File directorio_file = new File(directorio);

        if (!directorio_file.exists())
            directorio_file.mkdirs();

        if (!directorio_file.isDirectory())
            throw new ExcepcionDirectorioNoValido(directorio + " no es un directorio.");

        if (archivos.esVacia())
            throw new ExcepcionArchivoNoValido("No fueron agregados archivos donde leer palabras");

        archivos_palabras = new Diccionario<String, Diccionario<String, Integer>>(archivos.getLongitud());

        for (String archivo : archivos) {
            try {
                br = new BufferedReader(new FileReader(archivo));
                archivos_palabras.agrega(archivo, new Diccionario<String, Integer>());
                palabras = archivos_palabras.get(archivo);
                String s = "";

                while ((linea_actual = br.readLine()) != null) {
                    if (linea_actual.trim().length() > 0) {
                        s += Normalizer.normalize(linea_actual, Form.NFD);
                        s = s.toLowerCase();
                        s = s.trim();
                        s = s.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                        s = s.replaceAll("[*\\p{L}\\p{Nd}]-", " ");
                        s = s.replaceAll("\\p{Punct}", "");
                        s += " ";
                    }
                }

                if (s.length() < 1) 
                    ExcepcionArchivoNoValido.excepcion("No se introdujo texto al archivo " + archivo);

                palabras_actual = s.split(" ");

                for (String palabra: palabras_actual) {
                    if (palabra.length() > 0)
                        if (palabras.contiene(palabra))
                            palabras.agrega(palabra, palabras.get(palabra) + 1);
                        else
                            palabras.agrega(palabra, 1);
                }

                try {
                    archivo_actual = new File(archivo);
                    archivo_actual = new File(directorio + "/" + archivo_actual.getName() + ".html");
                    bw = new BufferedWriter(new FileWriter(archivo_actual));
                    generador = new GeneradorArchivoHTML(archivo, palabras);
                    generador.generarHTML();
                    bw.write(generador.getHTML());
                    bw.close();
                } catch (IOException e) {
                    throw new IOException("Sucedió un error al momento de crear el archivo" + archivo + ".html");
                }

            } catch (IOException e) {
                System.out.println(e);
                throw new ExcepcionArchivoNoValido(e.getMessage());
            } finally {
                try {
                    if (br != null)
                        br.close();
                } catch (IOException ex) {
                    throw new ExcepcionArchivoNoValido("Hubo un problema con el archivo " + archivo);
                }
            }
        }
        try {
            archivo_actual = new File(directorio + "/index.html");
            bw = new BufferedWriter(new FileWriter(archivo_actual));
            GeneradorIndex generadorI = new GeneradorIndex(archivos_palabras, directorio);
            generadorI.generarHTML();
            bw.write(generadorI.getHTML());
            bw.close();
        } catch (IOException e) {
            throw new IOException("Sucedió un error al momento de crear el archivo index.html");
        }
    }

    public static void main(String[] args) {
        try {
            proyecto3(args);
        } catch (IOException | ExcepcionArchivoNoValido | ExcepcionDirectorioNoValido e) {
            mostrarError(e.getMessage());
        }
    }
}
