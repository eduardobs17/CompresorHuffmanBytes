import pickle, json

# Definicion de constantes
LLAVE_FIN = "FIN"


# metodo que permite obtener el diccionario almacenado en la tabla de codificacion
def obtener_dic_de_tabla(nombre_archivo):
    temp_dic = json.loads(open(nombre_archivo, "r").read())
    dic = {}
    for llave, valor in temp_dic.items():
        dic[valor] = llave
    return dic

# metodo que permite obtener los bytes codificados del archivo
def obtener_bytes_codificacion(nombre_archivo):
    archivo = open(nombre_archivo,"rb")
    return pickle.load(archivo)

# metodo que permite obtener la decodificacion del archivo como lista de termino
def decodificar(arreglo_bytes,dic):
    res = []
    tamanno = arreglo_bytes.bit_length() - 1
    # El primer caracter debe ser 1, o hay un error
    if arreglo_bytes >> tamanno != 1:
        raise Error("Codificacion incorrecta")
    es_fin = False
    # Iteramos hasta llegar al final
    while tamanno > 0 and not es_fin:
        shift = tamanno - 1
        # Incrementamos de un bit en un bit
        while True:            
            num = arreglo_bytes >> shift
            # Quitamos el 1 inicial y el 0b de formato
            bitnum = bin(num)[3:]
            if bitnum not in dic:
                shift -= 1
                continue
            termino = dic[bitnum]
            if termino == LLAVE_FIN:
                es_fin = True
                break
            res.append(termino)
            arreglo_bytes = arreglo_bytes - ((num - 1) << shift)
            tamanno = shift
    return res

# Metodo que comienza la decodificacion
def correr():
    ARCHIVO_CODIFICADO = "animals"
    ARCHIVO_TABLA = "animals.dic"
    # primero se obtiene el diccionario de codificacion del json
    dic = obtener_dic_de_tabla(ARCHIVO_TABLA)
    # luego se obtienen los bytes de la codificacion resultado del algoridmo
    bytes = obtener_bytes_codificacion(ARCHIVO_CODIFICADO)
    lista_terminos = decodificar(bytes,dic)
    for termino in lista_terminos:
        print(termino)

correr()






