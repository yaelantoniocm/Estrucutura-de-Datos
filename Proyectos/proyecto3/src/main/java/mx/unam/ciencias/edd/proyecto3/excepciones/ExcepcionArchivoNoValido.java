package mx.unam.ciencias.edd.proyecto3.excepciones;

public class ExcepcionArchivoNoValido extends RuntimeException {

    public ExcepcionArchivoNoValido() {
        super("Archivo no valido.");
    }

    public ExcepcionArchivoNoValido(String err) {
        super(err);
    }

    public static void excepcion(String error) {
    	System.out.println(error);
    	System.exit(-1);
    }

}
