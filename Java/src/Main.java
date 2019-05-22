import java.io.File;

public class Main {

    /**
     * Metodo principal
     * @param args parametros del programa =>
     *             args[0] = '-d' para descomprimir o '-c' para comprimir
     *             args[1] = ruta del archivo de entrada
     *             args[2] = nombre opcional del archivo de salida
     */
    public static void main(String[] args) {
        System.out.println("Verificación de parámetros...");
        String funcion = "", rutaSalida = "", extension = "";
        File file = null, fileBinario = null;
        if (args.length != 0) {
            if (args.length == 3) {
                funcion = args[0];
                file = new File(args[1]);
                if (funcion.equals("-d")) {
                    fileBinario = new File(args[1].substring(0, args[1].lastIndexOf("Tabla")));
                }
                rutaSalida = args[1].substring(0, args[1].lastIndexOf('\\') + 1) + args[2];
                extension = args[1].substring(args[1].lastIndexOf('.') + 1);
            } else if (args.length == 2) {
                funcion = args[0];
                file = new File(args[1]);
                if (funcion.equals("-c")) {
                    rutaSalida = args[1].substring(0, args[1].length() - 4) + "Comprimido";
                } else {
                    fileBinario = new File(args[1].substring(0, args[1].lastIndexOf("Tabla")));
                    rutaSalida = args[1].substring(0, args[1].length() - 4) + "Descomprimido";
                }
                extension = args[1].substring(args[1].lastIndexOf('.') + 1);
            } else if (args.length == 1) {
                System.out.println("¡ERROR! Menos parámetros de los esperados");
                System.exit(-2);
            } else {
                System.out.println("¡ERROR! Cantidad indebida de parámetros");
                System.exit(-3);
            }
        } else {
            System.out.println("¡ERROR! No se recibió ninguna indicación");
            System.exit(-1);
        }
        System.out.println("Verificación de parámetros completada!\n");

        ManejadorArchivos ma = new ManejadorArchivos(funcion, file, fileBinario, rutaSalida, extension);
        ma.leerArchivo();
    }
}
