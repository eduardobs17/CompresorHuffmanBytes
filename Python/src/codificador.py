from collections import Counter
import heapq
import pickle, json


# Definicion de constantes
CANTIDAD_HIJOS = 8
LLAVE_FIN = "FIN"
CODIGO_BINARIO = {
    0:"000",
    1:"001",
    2:"010",
    3:"011",
    4:"100",
    5:"101",
    6:"110",
    7:"111"
} 
# Nodos para la construccion del arbol
class nodo():
    # Constructor de la clase
    def __init__(self):
        self.proba = 0
        self.termino = None
        self.lista_hijos = None
    
    # Metodos para poder comparar nodos
    def __lt__(self, other):
        return self.proba < other.proba

    def __le__(self, other):
        return self.proba <= other.proba

    def __eq__(self, other):
        return self.proba == other.proba

    def __ne__(self, other):
        return not self.proba == other.proba

    def __gt__(self, other):
        return self.proba > other.proba

    def __ge__(self, other):
        return self.proba >= other.proba
    
    # Sobrecarga del metodo string para imprimir como arbol
    def imprimir_estructura(self,margen=0):
        cadena = margen*"\t"+" |\n"
        cadena += margen*"\t"+" |\n"
        cadena += margen*"\t" +"(" + str(self.proba) + ", " + str(self.termino) + ")\n"
        if self.lista_hijos is not None:
            for hijo in self.lista_hijos:
                if hijo is not None:
                    cadena += hijo.imprimir_estructura(margen+1)
                else:
                    cadena +=  (margen+1)*"\t"+" |\n"
                    cadena += (margen+1)*"\t"+" |\n"
                    cadena += (margen+1)*"\t" + "None\n"
        return cadena


# obtener contenido ordenado por probabilidad
def obtener_contenido_ordenado(dic):
    lista_arbol = []
    for llave,valor in dic.items():
        # Se insertan en la lista y priorizados
        n = nodo()
        n.termino = llave
        n.proba = valor
        heapq.heappush(lista_arbol,n)
    return lista_arbol

# Metodo que permite crear el diccionario de frecuencias de cada termino
def obtener_dic_frecuencias(lista_terminos):
    dic_frecuencias = {}
    total = len(lista_terminos) + 1
    conjunto = Counter(lista_terminos)
    for termino, cuenta in conjunto.items():
        dic_frecuencias[termino] = cuenta/total
    # Se agrega el fin de archivo
    dic_frecuencias[LLAVE_FIN] = 1/total
    return dic_frecuencias

# Metodo que crea el arbol
def crear_arbol(lista_priorizada):
    lista_hijos = [] # lista de valores de hijos
    tope = None # item que se desencola
    nuevo_nodo = nodo() # el nuevo item que se va a insertar
    suma_temp = 0 # para ir acumulando los valores
    # Comienza a correr el algoritmo hasta que solo quede la raiz en la lista
    while len(lista_priorizada) > 1:
        # Se van sacando de 8 en 8 de tope de la lista
        # se va sumando la frecuencia y armando la lista de hijos
        # la raiz va a tener proba 1
        for indice in range (0, CANTIDAD_HIJOS):
            try:
                # Se intenta sacar si aun hay disponibles
                tope = heapq.heappop(lista_priorizada)
                suma_temp += tope.proba
            except:
                # Ya no es posible tener 8 hijos entonces se agrega None
                tope = None
            # Se mete en la lista de hijos para el nuevo nodo
            lista_hijos.append(tope)
        # Se crea el nuevo nodo
        nuevo_nodo.proba = suma_temp
        nuevo_nodo.lista_hijos = []
        nuevo_nodo.lista_hijos = lista_hijos
        # Se inserta en la lista para la proxima iteracion
        heapq.heappush(lista_priorizada,nuevo_nodo)
        # Se limpian los valores para la proxima iteracion
        lista_hijos = []
        suma_temp = 0
        nuevo_nodo = nodo()
        # Finalmente se retorna la raiz con proba 1 y con el arbol listo
    return lista_priorizada[0]


# metodo para obtener el diccionario de codificacion para segun el arbol
def obtener_diccionario_codificacion(raiz):
    # Se define una cola para el recorrido en profundidad primero
    cola = []
    # Se define el diccionario de codificacion
    codigo = {}
    # Constado de los bits
    contador = 0
    # Se guarda la codificacion del nivel superior para concaternar con el siguiente
    ultima_codificacion = ""
    # Se inserta el nodo raiz
    cola.append((raiz,ultima_codificacion))
    while len(cola) > 0:
        # Se saca el elemento de la cola
        nodo,ultima_codificacion = cola.pop()
        # Si tiene hijos se recogen generando los codigos
        if nodo.lista_hijos is not None:
            # Se recorre cada hijo que no sea None
            for hijo in nodo.lista_hijos:
                if hijo is not None:
                    # Se registra el codigo en el diccionario
                    if hijo.termino is not None:
                        codigo[hijo.termino] = ultima_codificacion + CODIGO_BINARIO[contador]
                    # Si hay mas descendencia se encola
                    if hijo.lista_hijos is not None:
                        cola.append((hijo,CODIGO_BINARIO[contador]))                        

                # Se incrementa el contador
                contador += 1
        # Se reinicia el contador para la proxima iteracion
        contador = 0
    # Finalmente se retorna la codificacion
    return codigo

def impirmir_dic_codificacion(dic):
    # Se muestra la codificacion
    print("\t%-11s %15s"%(11*"-",15*"-"))
    print("\t%-10s | %14s"%("Termino:","codificacion"))
    print("\t%-11s %15s"%(11*"-",15*"-"))
    for llave,valor in dic.items():
        print("\t%-10s | %14s"%(llave,valor))
    print()


# metodo que devuelve el entero de la codificacion
def obtener_bytes_codificacion(lista_terminos,dic_codificacion):
    resultado = ""
    # Se obtiene el codigo de cada termino
    for termino in lista_terminos:
        resultado += dic_codificacion[termino]
    # Se agrega 1 al inicio para conservar ceros y el fin de archivo
    resultado = '1' + resultado + dic_codificacion[LLAVE_FIN]
    # Se ajusta para que sea multiplo de 8
    resultado = resultado + (len(resultado) % 8 * "0")
    # Finalmente se convierte a entero ya que esta en base 2
    return int(resultado,2)

# Metodo que permite almacenar el archivo codificado y el diccionario
def almacenar_compresion(bytes,nombre_archivo,dic):
    # Para el archivo
    archivo_salida = open(nombre_archivo,'wb')
    pickle.dump(bytes,archivo_salida)
    archivo_salida.close()
    # Para el diccionario como un json
    archivo_tabla = open(nombre_archivo+".dic",'w')
    json.dump(dic,archivo_tabla)
    archivo_tabla.close()

# metodo que realiza la lectura del contenido de un archivo y devuelve una lista de termino
def obtener_contenido_archivo(nombre_archivo):
    return open(nombre_archivo,"r").read().split(" ")    

# metodo principal
def correr():
    NOMBRE_ARCHIVO = "animals.txt"
    # Se lee el archivo y se obtiene la lista de terminos
    terminos = obtener_contenido_archivo(NOMBRE_ARCHIVO)
    # Se obtienen las frecuencias de cada termino
    dic =obtener_dic_frecuencias(terminos)
    # Se obtiene la lista ordenada por prioridad para el algoritmo
    lista =  obtener_contenido_ordenado(dic)
    # Se obtiene el arbol de costo minimo
    arbol = crear_arbol(lista)
    # Se genera la tabla de codificacion
    dic = obtener_diccionario_codificacion(arbol)
    # Se imprime la codificacion
    impirmir_dic_codificacion(dic)
    # Se imprime el arbol
    print(arbol.imprimir_estructura())
    # Se imprime el numero resultado de codificar
    entero_cod = obtener_bytes_codificacion(terminos,dic)
    print("Numero resultado codificacion: "+ str(entero_cod))
    # Se almacena la codificacion
    almacenar_compresion(entero_cod,NOMBRE_ARCHIVO.replace(".txt",""),dic)
        
correr()
