import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

class ManejadorArchivos {

    private String funcion;
    private File file;
    private File fileB;
    private String rutaSalida;
    private String extension;

    ManejadorArchivos(String pFuncion, File pFile, File pFileBinario, String pRutaSalida, String pExtension) {
        funcion = pFuncion;
        file = pFile;
        fileB = pFileBinario;
        rutaSalida = pRutaSalida;
        extension = pExtension;
    }

    void leerArchivo() {
        try {
            if (funcion.equals("-c")) {
                BitInputStream bitInputStream1 = new BitInputStream(new FileInputStream(file));
                BitInputStream bitInputStream2 = new BitInputStream(new FileInputStream(file));

                if (!extension.equals("huf")) {
                    comprimir(bitInputStream1, bitInputStream2, file.getPath(), rutaSalida);
                } else {
                    System.out.println("Sucedió un error, el archivo ya está comprimido. Favor revisarlo.");
                    System.exit(-3);
                }
            } else if (funcion.equals("-d")) {
                BitInputStream bitInputStream1 = new BitInputStream(new FileInputStream(file));
                BitInputStream bitInputStream2 = new BitInputStream(new FileInputStream(fileB));

                descomprimir(bitInputStream1, bitInputStream2, rutaSalida);
            } else {
                System.out.println("ERROR, no se reconoció la instrucción. Intente de nuevo");
                System.exit(-2);
            }
        } catch (IOException exception) {
            System.out.println("Hubo un error al intentar abrir el archivo");
            System.exit(-1);
        }
    }

    /**
     * Método que se encarga de la compresión. Entre sus acciones están: crear el árbold de Huffman y su tabla asociada,
     * Crear el archivo de salida, guardar la tabla y guardar los datos.
     * @param file1 el archivo que queremos comprimir.
     * @param file2 una copia auxiliar del archivo que queremos comprimir.
     * @param tipo Se refiere a si el usuario desea un nombre para el archivo de salida.
     *             Si así fuera, tipo posee ese nombre.
     *             Si así no fuera, tipo posee el nombre original.
     * @param nombreDestino opcional. Es el nombre del archivo de salida.
     */
    private void comprimir(BitInputStream file1, BitInputStream file2, String tipo, String nombreDestino) {
        System.out.println("Comprimiendo archivo...\n");
        Lista<Arbol<Par<Character, Integer>>> l = new Lista<>();
        int tamArchivo = 0;
        System.out.println("Creando lista...");
        while (file1.hasNextBit()) {
            char caracterLeido = (char) file1.read();
            if (buscar(caracterLeido, l)) {
                Arbol<Par<Character, Integer>> elem = null;
                for (int x = 0; x < l.numElementos(); x++) {
                    if (l.recuperar(x).getRaiz().getPrimero() == caracterLeido) {
                        elem = l.recuperar(x);
                        x = l.numElementos() + 50;
                    }
                }
                assert elem != null;
                elem.getRaiz().modificarSegundo(elem.getRaiz().getSegundo() + 1);
            } else {
                Arbol<Par<Character, Integer>> a = new Arbol<>(new Par(caracterLeido, 1));
                l.agregar(a);
            }
            tamArchivo++;
        }
        System.out.println("Lista de caracteres creada correctamente!\n");

        System.out.println("Creando árbol...");
        Arbol<Par<Character, Integer>> arbolHuffman = crearArbolHuffman(l);
        System.out.println("Arbol de Huffman creado correctamente!\n");

        System.out.println("Creando tabla...");
        Lista<Par<Character, String>> tabla = crearTabla(arbolHuffman, "", new Lista<>(), 0);
        System.out.println("Tabla creada correctamente!\n");

        try {
            System.out.println("Creando comprimido...");
            BitOutputStream archivoSalida = new BitOutputStream(new FileOutputStream(nombreDestino));
            BitOutputStream archivoTabla = new BitOutputStream(new FileOutputStream(nombreDestino + "Tabla.huf"));

            System.out.println("Escribiendo en archivo comprimido...");

            //Escribe el tamaño de la tabla
            String numE = "" + tabla.numElementos();
            for (int x = 0; x < numE.length(); x++) {
                archivoTabla.write(numE.charAt(x));
            }
            archivoTabla.write('\n');

            //Escribe el tamaño del archivo
            String tamA = "" + tamArchivo;
            for (int x = 0; x < tamA.length(); x++) {
                archivoTabla.write(tamA.charAt(x));
            }
            archivoTabla.write('\n');

            //Escribe la tabla
            Elemento<Par<Character, String>> it1 = tabla.primero;
            while (it1 != null) {
                char aux = it1.objeto.getPrimero();
                archivoTabla.write(aux); // escribe el caracter
                archivoTabla.write(' ');
                String aux2 = obtenerFrecuencia(aux, arbolHuffman);
                for (int x = 0; x < aux2.length(); x++) {
                    archivoTabla.write(aux2.charAt(x)); // escribe la frecuencia
                }
                archivoTabla.write(':');
                archivoTabla.write(' ');

                for (int x = 0; x < it1.objeto.getSegundo().length(); x++) { //escribe su codigo
                    archivoTabla.write(it1.objeto.getSegundo().charAt(x));
                }
                archivoTabla.write('\n');
                it1 = it1.siguiente;
            }

            file2.setBitMode(false);
            int contador = 0;
            while (file2.hasNextBit()) {
                int readByte = file2.read();
                Par<Character, String> par = null;
                for (int x = 0; x < tabla.numElementos(); x++) {
                    if (tabla.recuperar(x).getPrimero() == (char) readByte) {
                        par = tabla.recuperar(x);
                        x = tabla.numElementos() + 31;
                    }
                }
                assert par != null;
                for (int x = 0; x < par.getSegundo().length(); x++) {
                    char bit = par.getSegundo().charAt(x);
                    contador++;
                    if (bit == '0') {
                        archivoSalida.writeBit(0);
                    } else {
                        archivoSalida.writeBit(1);
                    }
                }
            }
            while (contador % 8 != 0) {
                archivoSalida.writeBit(0);
                contador++;
            }
            System.out.println("El archivo se escribió correctamente!\nCompresión finalizada!");
            System.exit(0);
        } catch (IOException exception) {
            System.out.println("Hubo un error al crear el archivo comprimido");
            System.exit(-3);
        }
    }

    /**
     * Método que se encarga de la descompresión del archivo. Tiene una función inversa al método anterior.
     * Entre sus acciones están: recrear el árbol de Huffman a partir de los datos en el archivo,
     * crear el archivo de salida, y guardar los datos originales.
     * @param file el archivo que queremos descomprimir. Tiene que ser un archivo anteriormente comprimido, osea, con extension .huf
     * @param nombreDestino opcional. Posee el nombre que el archivo de salida llevará.
     */
    private void descomprimir(BitInputStream file, BitInputStream file1, String nombreDestino) {
        System.out.println("Descomprimiendo...");

        StringBuilder basura = new StringBuilder();
        boolean continuar = true;
        while (continuar) {
            int bytes = file.read();
            if (bytes != '\n') {
                basura.append((char) bytes);
            } else {
                continuar = false;
            }
        }
        int cantidadCaracteres = Integer.parseInt(basura.toString());

        basura = new StringBuilder();
        continuar = true;
        while (continuar) {
            int bytes = file.read();
            if (bytes != '\n') {
                basura.append((char) bytes);
            } else {
                continuar = false;
            }
        }
        int tamArchivo = Integer.parseInt(basura.toString());

        System.out.println("Reconstruyendo tabla...");
        Lista<Par<Character,String>> tabla = new Lista<>();
        while (tabla.numElementos() != cantidadCaracteres) {
            StringBuilder codigo = new StringBuilder();
            char caracter = (char) file.read();
            int var = file.read();
            while (var != ':') {
                var = file.read();
            }
            var = file.read();
            var = file.read();
            while (var != '\n') {
                codigo.append((char) var);
                var = file.read();
            }
            Par<Character,String> par = new Par<>(caracter, codigo.toString());
            tabla.agregar(par);
        }
        System.out.println("Tabla reconstruida!\n");

        System.out.println("Reconstruyendo arbol...");
        Arbol<Character> arbolHuffman = reconstruirArbol(tabla);
        System.out.println("Arbol reconstruido!\n");

        try {
            BitOutputStream nuevoBitOutputStream;
            System.out.println("Creando archivo descomprimido...");
            nuevoBitOutputStream = new BitOutputStream(new FileOutputStream(nombreDestino + ".txt"));

            System.out.println("Recorriendo arbol y escribiendo archivo...");
            file.setBitMode(true);
            int contador = 0;
            while (contador < tamArchivo) {
                Arbol<Character> it1 = arbolHuffman;
                do {
                    if (file1.hasNextBit()) {
                        String codigo = "";
                        codigo += file1.readBit();
                        codigo += file1.readBit();
                        codigo += file1.readBit();
                        int codigoInt = Integer.parseInt(codigo, 2);
                        it1 = it1.getHijos()[codigoInt];
                    } else {
                        System.out.println("Sucedió un error inesperado.");
                        System.exit(-3);
                    }
                } while (!it1.esHoja());
                nuevoBitOutputStream.write(it1.getRaiz());
                contador++;
            }
            System.out.println("El archivo se descomprimó correctamente.");
            System.exit(0);
        } catch (IOException exception) {
            System.out.println("Hubo un error al crear el archivo descomprimido");
            System.exit(-3);
        }
    }

    /**
     * Método que permite averiguar si el caracter 'c' se encuentra en la lista l.
     * @param c El caracter que queremos buscar.
     * @param l La lista en donde queremos buscar.
     * @return true si el elemento se encuentra; false si sucede lo contrario.
     */
    private boolean buscar(char c, Lista<Arbol<Par<Character,Integer>>> l) {
        Elemento<Arbol<Par<Character,Integer>>> it1 = l.primero;
        while (it1 != null) {
            if (it1.objeto.getRaiz().getPrimero() == c) {
                return true;
            } else {
                it1 = it1.siguiente;
            }
        }
        return false;
    }

    private String obtenerFrecuencia(char c, Arbol<Par<Character,Integer>> a) {
        Queue<Arbol<Par<Character,Integer>>> cola = new ArrayDeque<>();
        cola.add(a);
        while (!cola.isEmpty()) {
            Arbol<Par<Character, Integer>> it = cola.poll();
            if (!it.esHoja()) {
                Arbol[] hijos = it.getHijos();
                for (int x = 0; x < it.getCantHijos(); x++) {
                    cola.add(hijos[x]);
                }
            } else {
                if (it.getRaiz().getPrimero() == c) {
                    return it.getRaiz().getSegundo().toString();
                }
            }
        }
        return "";
    }

    /**
     * Método que permite crear el árbol de Huffman correspondiente a los elementos (caracteres y frecuencias) que se
     * encuentran en la lista l.
     * @param l la lista de la cual queremos crear el árbol.
     * @return el arbol de Huffman completamente terminado.
     */
    private Arbol<Par<Character,Integer>> crearArbolHuffman(Lista<Arbol<Par<Character,Integer>>> l) {
        while (l.primero.siguiente != null) {
            l = this.burbuja(l);
            int contador = 0, frecuencias = 0, cantHijos = 0;
            Arbol[] hijosArray = new Arbol[8];
            while (contador < 8 && l.numElementos() != 0) {
                hijosArray[contador] = l.primero.objeto;
                frecuencias += l.primero.objeto.getRaiz().getSegundo();
                l.BorrarPrimero();
                cantHijos++;
                contador++;
            }
            Arbol<Par<Character,Integer>> nuevoArbol = new Arbol<>(new Par('_', frecuencias), hijosArray, cantHijos);
            l.agregar(nuevoArbol);
        }
        return l.primero.objeto;
    }

    /**
     * Aplica el algoritmo burbuja para ordenar los elementos de la lista de menor a mayor, según la frecuencia con la que
     * aparece cada elemento.
     * @param l la lista que queremos ordenar.
     * @return lista ya ordenada de menor a mayor.
     */
    private Lista<Arbol<Par<Character,Integer>>> burbuja(Lista<Arbol<Par<Character,Integer>>> l) {
        Elemento<Arbol<Par<Character,Integer>>> it1 = l.ultimo;
        while (it1 != l.primero) {
            Elemento<Arbol<Par<Character,Integer>>> it2 = l.primero;
            int contador = 0;
            while (it2.siguiente != null) {
                if (it2.objeto.getRaiz().getSegundo() > it2.siguiente.objeto.getRaiz().getSegundo()) {
                    l.intercambiar(contador,contador+1);
                }
                contador++;
                it2 = it2.siguiente;
            }
            it1 = it1.anterior;
        }
        return l;
    }

    /**
     * Método auxiliar de la descompresión. Recibe los datos que se encontraban en el archivo comprimido y recrea el arbol de Huffman,
     * siguiendo sus codigos.
     * @param lista una lista de pares. Cada par será de tipo (char,String), donde char representa el caracter y String su codigo asociado.
     * @return Arbol de Huffman reconstruido.
     */
    private Arbol<Character> reconstruirArbol(Lista<Par<Character,String>> lista) {
        Arbol<Character> arbol = new Arbol<>((char) 255, new Arbol[8], 0);
        for (int x = 0; x < lista.numElementos(); x++) {
            Par<Character, String> it = lista.recuperar(x);
            arbol.addHijo(it.getPrimero(), it.getSegundo(), arbol);
        }
        return arbol;
    }

    /**
     * Crea una 'tabla' a apartir de un arbol de Huffman en especifico. Esta tabla será una lista de pares,
     * donde cada par es de tipo char y String. El char representa el caracter y el String su codigo asociado.
     * Esta tabla será la que se guarde en el archivo comprimido. Sin esta, el archivo no se podría descomprimir.
     * @param arbol el Arbol de Huffman del cual queremos hacer la tabla.
     * @param listaAnterior al igual que el parametro anterior, es auxiliar. Se ocupa conocer la secuencia del nodo anterior.
     * @return lista con todos los caracteres identificados con sus respectivos codigos.
     */
    private Lista<Par<Character,String>> crearTabla(Arbol<Par<Character, Integer>> arbol, String hilera,
                                                    Lista<Par<Character, String>> listaAnterior, int id) {
        Lista<Par<Character,String>> lista = listaAnterior;
        if (arbol.esHoja()) {
            lista.agregar(new Par<>(arbol.getRaiz().getPrimero(), hilera));
        } else {
            Arbol[] hijos = arbol.getHijos();
            for (int x = 0; x < 8; x++) {
                if (hijos[x] != null) {
                    String hileraTemp = Integer.toBinaryString(x);
                    if (hileraTemp.length()%3 == 1) {
                        hileraTemp = "00" + hileraTemp;
                    } else if (hileraTemp.length()%3 == 2){
                        hileraTemp = "0" + hileraTemp;
                    }
                    lista = crearTabla(hijos[x], hilera + hileraTemp, lista, x);
                } else {
                    break;
                }
            }
        }
        return lista;
    }
}