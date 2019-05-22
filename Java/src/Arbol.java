/**
 * Esta clase se usa para representar cada uno de los "sub-árboles" de Huffman.
 * La raíz es un par (o cualquier otro tipo T) con un caracter y su frecuencia de aparición.
 * Inicialmente, las raíces son hojas (nodos sin hijos) pero conforme se corra el programa, se van agregando hijos para
 * formar el árbol de Huffman final.
 *
 * @author Eduardo Biazzetti - B40999.
 */
class Arbol<T> {
    private Arbol[] hijos;
    private T raiz;
    private int cantHijos;

    /** Constructor de la clase Arbol. */
    Arbol(T pRaiz) {
        raiz = pRaiz;
        hijos = null;
        cantHijos = 0;
    }

    Arbol(T pRaiz, Arbol[] array, int pCantidadHijos) {
        raiz = pRaiz;
        hijos = array;
        cantHijos = pCantidadHijos;
    }

    /**
     * Permite 'recuperar' la raíz del árbol.
     * @return raiz.
     */
    T getRaiz() {
        return raiz;
    }

    /**
     * Modifica y establece una nueva raíz en el árbol.
     * @param p: La nueva raíz del árbol. Tiene que ser de tipo T.
     */
    void setRaiz(T p) {
        raiz = p;
    }

    /**
     * Verifica si la raíz del árbol tiene o no tiene hijos.
     * @return true si hijoDerecho e hijoIzquierdo son nulos; en caso contario, devuelve false.
     */
    boolean esHoja() { return cantHijos == 0; }

    int getCantHijos() { return cantHijos; }

    Arbol[] getHijos() { return hijos; }

    void addHijo(char caracter, String codigo, Arbol a) {
        String codigoPadre = codigo.substring(0,3);
        String codigoRestante = codigo.substring(3);
        int numHijo = Integer.parseInt(codigoPadre, 2);
        if (a.hijos[numHijo] == null) {
            a.hijos[numHijo] = new Arbol((char) 255, new Arbol[8], 0);
            a.cantHijos++;
        }

        if (codigoRestante.equals("")) {
            a.hijos[numHijo].setRaiz(caracter);
        } else {
            addHijo(caracter, codigoRestante, a.hijos[numHijo]);
        }
    }
}