/**
 * Esta clase permite asociar un objeto T con un objeto K.
 * @author Eduardo Biazzetti - B40999.
 */
class Par<T,K> {
    private T primero;
    private K segundo;

    /**
     * Constructor de la clase Par.
     * @param c: Representa el objeto de tipo T del par.
     * @param f: Representa el objeto de tipo K del par.
     */
    Par(T c, K f) {
        primero = c;
        segundo = f;
    } // fin del constructor

    /**
     * Devuelve el objeto de tipo T que posee el par.
     * @return primero.
     */
    T getPrimero() {
        return primero;
    }

    /**
     * Devuelve el objeto de tipo K que posee el par.
     * @return segundo.
     */
    K getSegundo() {
        return segundo;
    }

    /**
     * Modifica el objeto K actual por uno nuevo.
     * @param nuevo el nuevo valor que 'segundo' va a poseer.
     */
    void modificarSegundo(K nuevo) {
        segundo = nuevo;
    }
}