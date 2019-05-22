/**
 * @author Eduardo Biazzetti - B40999
 */

class Lista<T>{
    Elemento<T> primero;
    Elemento<T> ultimo;

    /**
     * Constructor de la clase lista. Se crea una lista vacia.
     */
    Lista(){
        primero = null;
        ultimo = null;
    }

    /**
     * Agrega un elemento a la lista. Si la lista está vacía, se agrega de primero. En caso contrario, se agrega al final.
     * @param elem el eleemento que queremos agregar.
     */
    void agregar(T elem) {
        Elemento<T> nuevoElem = new Elemento<>(elem);
        if (primero == null) {
            primero = nuevoElem;
            ultimo = nuevoElem;
        } else {
            ultimo.siguiente = nuevoElem;
            nuevoElem.anterior = ultimo;
            ultimo = nuevoElem;
        }
    }

    /**
     * Intercambia los elementos de 2 posiciones de la lista.
     * @param pos1 posicion del elemento 1 que se quiere cambiar
     * @param pos2 posicion del elemento 2 que se quiere cambiar
     */
    void intercambiar(int pos1, int pos2) {
        Elemento<T> elem1 = null;
        Elemento<T> elem2 = null;
        int x = 0;
        Elemento<T> it1 = primero;
        while ((it1 != null) && (elem1 == null || elem2 == null)) {
            if (x == pos1) {
                elem1 = it1;
            }
            if (x == pos2) {
                elem2 = it1;
            }
            x++;
            it1 = it1.siguiente;
        }
        assert elem1 != null;
        T temporal = elem1.objeto;
        assert elem2 != null;
        elem1.objeto = elem2.objeto;
        elem2.objeto = temporal;
    }

    /**
     * Método que permite borrar el primer elemento de la lista.
     */
    void BorrarPrimero() {
        if (primero.siguiente != null) {
            Elemento<T> aBorrar = primero;
            primero = aBorrar.siguiente;
            primero.anterior = null;
        } else {
            primero = null;
            ultimo = null;
        }
    }

    /**
     * Permite recuperar el elemento de una determinada posición.
     * @param pos la posición del elemento.
     * @return el elemento que se encuentra en la posición pos.
     */
    T recuperar(int pos) {
        int x = 0;
        Elemento<T> it1 = primero;
        while ((x < pos) && (it1 != null)) {
            it1 = it1.siguiente;
            x++;
        }
        if (it1 != null) {
            return it1.objeto;
        } else {
            return null;
        }
    }

    /**
     * Cuenta los elementos de la lista y los devuelve.
     * @return la cantidad de elementos de la lista.
     */
    int numElementos() {
        Elemento<T> it1 = primero;
        int contador = 0;
        while (it1 != null) {
            contador++;
            it1 = it1.siguiente;
        }
        return contador;
    }
}