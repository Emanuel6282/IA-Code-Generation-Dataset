# CARACTERISTICAS DE PYTHON 3.11 UTILIZADAS Y POR QUE:
# 1. Anotaciones de tipos genéricas estándar (PEP 585): Se reemplaza la importación de `List` desde el módulo `typing` por el tipo integrado genérico `list[int]`, haciendo que la firma del método sea nativa y más limpia.
# 2. Notas en Excepciones (`add_note()` - PEP 678): Se añadió manejo defensivo de listas vacías utilizando la característica de Python 3.11 que permite adjuntar notas informativas directamente a las excepciones, mejorando la observabilidad y el diagnóstico.
# 3. Optimización del Intérprete Especializado (PEP 659): Se beneficia de las mejoras masivas de rendimiento para bucles `for` y asignación de variables locales introducidas en la versión 3.11.

def max_subarray_sum(arr: list[int]) -> int:
    if not arr:
        err = ValueError("El arreglo no puede estar vacío.")
        err.add_note("Contexto de error: Se intentó ejecutar Kadane sobre una colección sin elementos.")
        raise err

    current_max = arr[0]
    global_max = arr[0]
    
    for i in range(1, len(arr)):
        if current_max + arr[i] > arr[i]:
            current_max = current_max + arr[i]
        else:
            current_max = arr[i]
            
        if current_max > global_max:
            global_max = current_max
            
    return global_max

# ==========================================
# CASOS DE PRUEBA
# ==========================================

assert max_subarray_sum([-2, 1, -3, 4, -1, 2, 1, -5, 4]) == 6
assert max_subarray_sum([1]) == 1
assert max_subarray_sum([-2, -1]) == -1

print("Todos los tests en Python 3.11 pasaron correctamente.")

# /*
# EXPLICACION DE CAMBIOS E IDIOMATISMOS EN PYTHON 3.11:
# 
# 1. **Modernización de Tipos Genéricos (PEP 585)**: 
#    En Python 3.9 el código base dependía de `from typing import List`. A partir de PEP 585, los tipos nativos como `list` y `dict` soportan genéricos directamente, eliminando importaciones innecesarias y estandarizando la firma como `list[int]`.
# 
# 2. **Depuración Avanzada con Notas en Excepciones (PEP 678)**: 
#    Se incorporó validación para entradas vacías utilizando `.add_note()`, una adición muy útil en Python 3.11 para enriquecer excepciones de manera programática sin interferir con el flujo del algoritmo.
# 
# 3. **Velocidad del Intérprete (PEP 659)**: 
#    Aunque la lógica algorítmica de Kadane se mantiene fiel al paradigma iterativo imperativo original, la ejecución de los bucles se beneficia directamente del *specializing adaptive interpreter* de Python 3.11, logrando una ejecución más rápida sobre secuencias de datos.
# */