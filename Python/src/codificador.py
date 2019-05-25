from collections import Counter
import heapq
import pickle, json


# Definicion de constantes
CANTIDAD_HIJOS = 256
LLAVE_FIN = "FIN"
CODIGO_BINARIO = {
        0: "00000000",
        1: "00000001",
        2: "00000010",
        3: "00000011",
        4: "00000100",
        5: "00000101",
        6: "00000110",
        7: "00000111",
        8: "00001000",
        9: "00001001",
        10: "00001010",
        11: "00001011",
        12: "00001100",
        13: "00001101",
        14: "00001110",
        15: "00001111",
        16: "00010000",
        17: "00010001",
        18: "00010010",
        19: "00010011",
        20: "00010100",
        21: "00010101",
        22: "00010110",
        23: "00010111",
        24: "00011000",
        25: "00011001",
        26: "00011010",
        27: "00011011",
        28: "00011100",
        29: "00011101",
        30: "00011110",
        31: "00011111",
        32: "00100000",
        33: "00100001",
        34: "00100010",
        35: "00100011",
        36: "00100100",
        37: "00100101",
        38: "00100110",
        39: "00100111",
        40: "00101000",
        41: "00101001",
        42: "00101010",
        43: "00101011",
        44: "00101100",
        45: "00101101",
        46: "00101110",
        47: "00101111",
        48: "00110000",
        49: "00110001",
        50: "00110010",
        51: "00110011",
        52: "00110100",
        53: "00110101",
        54: "00110110",
        55: "00110111",
        56: "00111000",
        57: "00111001",
        58: "00111010",
        59: "00111011",
        60: "00111100",
        61: "00111101",
        62: "00111110",
        63: "00111111",
        64: "01000000",
        65: "01000001",
        66: "01000010",
        67: "01000011",
        68: "01000100",
        69: "01000101",
        70: "01000110",
        71: "01000111",
        72: "01001000",
        73: "01001001",
        74: "01001010",
        75: "01001011",
        76: "01001100",
        77: "01001101",
        78: "01001110",
        79: "01001111",
        80: "01010000",
        81: "01010001",
        82: "01010010",
        83: "01010011",
        84: "01010100",
        85: "01010101",
        86: "01010110",
        87: "01010111",
        88: "01011000",
        89: "01011001",
        90: "01011010",
        91: "01011011",
        92: "01011100",
        93: "01011101",
        94: "01011110",
        95: "01011111",
        96: "01100000",
        97: "01100001",
        98: "01100010",
        99: "01100011",
        100: "01100100",
        101: "01100101",
        102: "01100110",
        103: "01100111",
        104: "01101000",
        105: "01101001",
        106: "01101010",
        107: "01101011",
        108: "01101100",
        109: "01101101",
        110: "01101110",
        111: "01101111",
        112: "01110000",
        113: "01110001",
        114: "01110010",
        115: "01110011",
        116: "01110100",
        117: "01110101",
        118: "01110110",
        119: "01110111",
        120: "01111000",
        121: "01111001",
        122: "01111010",
        123: "01111011",
        124: "01111100",
        125: "01111101",
        126: "01111110",
        127: "01111111",
        128: "10000000",
        129: "10000001",
        130: "10000010",
        131: "10000011",
        132: "10000100",
        133: "10000101",
        134: "10000110",
        135: "10000111",
        136: "10001000",
        137: "10001001",
        138: "10001010",
        139: "10001011",
        140: "10001100",
        141: "10001101",
        142: "10001110",
        143: "10001111",
        144: "10010000",
        145: "10010001",
        146: "10010010",
        147: "10010011",
        148: "10010100",
        149: "10010101",
        150: "10010110",
        151: "10010111",
        152: "10011000",
        153: "10011001",
        154: "10011010",
        155: "10011011",
        156: "10011100",
        157: "10011101",
        158: "10011110",
        159: "10011111",
        160: "10100000",
        161: "10100001",
        162: "10100010",
        163: "10100011",
        164: "10100100",
        165: "10100101",
        166: "10100110",
        167: "10100111",
        168: "10101000",
        169: "10101001",
        170: "10101010",
        171: "10101011",
        172: "10101100",
        173: "10101101",
        174: "10101110",
        175: "10101111",
        176: "10110000",
        177: "10110001",
        178: "10110010",
        179: "10110011",
        180: "10110100",
        181: "10110101",
        182: "10110110",
        183: "10110111",
        184: "10111000",
        185: "10111001",
        186: "10111010",
        187: "10111011",
        188: "10111100",
        189: "10111101",
        190: "10111110",
        191: "10111111",
        192: "11000000",
        193: "11000001",
        194: "11000010",
        195: "11000011",
        196: "11000100",
        197: "11000101",
        198: "11000110",
        199: "11000111",
        200: "11001000",
        201: "11001001",
        202: "11001010",
        203: "11001011",
        204: "11001100",
        205: "11001101",
        206: "11001110",
        207: "11001111",
        208: "11010000",
        209: "11010001",
        210: "11010010",
        211: "11010011",
        212: "11010100",
        213: "11010101",
        214: "11010110",
        215: "11010111",
        216: "11011000",
        217: "11011001",
        218: "11011010",
        219: "11011011",
        220: "11011100",
        221: "11011101",
        222: "11011110",
        223: "11011111",
        224: "11100000",
        225: "11100001",
        226: "11100010",
        227: "11100011",
        228: "11100100",
        229: "11100101",
        230: "11100110",
        231: "11100111",
        232: "11101000",
        233: "11101001",
        234: "11101010",
        235: "11101011",
        236: "11101100",
        237: "11101101",
        238: "11101110",
        239: "11101111",
        240: "11110000",
        241: "11110001",
        242: "11110010",
        243: "11110011",
        244: "11110100",
        245: "11110101",
        246: "11110110",
        247: "11110111",
        248: "11111000",
        249: "11111001",
        250: "11111010",
        251: "11111011",
        252: "11111100",
        253: "11111101",
        254: "11111110",
        255: "11111111"
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
        cadena += margen*"\t" +"(" + str(round(self.proba,5)) + ", " + str(self.termino) + ")\n"
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
