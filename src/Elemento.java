/**
 * Esta clase es un complemento de la clase lista, pues permite realizar los 'enlaces' entre los objetos.
 * El tipo de dato que esta clase usa debe ser igual al tipo de dato de la clase lista.
 * @author Eduardo Biazzetti - B40999.
 */
class Elemento<T> {
    Elemento<T> anterior;
    T objeto;
    Elemento<T> siguiente;

    /**
     * Constructor de la clase Elemento. Inicialmente está aislado, pues siguiente y anterior son nulos.
     * @param nuevoObjeto el objeto de tipo T que esta instancia de elemento va a almacenar (y futuramente enlazar en una lista).
     */
    Elemento(T nuevoObjeto) {
        objeto = nuevoObjeto;
        siguiente = null;
        anterior = null;
    }
}