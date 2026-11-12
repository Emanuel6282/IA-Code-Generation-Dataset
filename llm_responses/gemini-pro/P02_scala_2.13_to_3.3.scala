// CARACTERISTICAS DE SCALA 3.3 UTILIZADAS Y POR QUE:
// 1. Sintaxis sin llaves (Significant Indentation / Braceless Syntax): Elimina el ruido visual de las llaves {} y los paréntesis en las estructuras de control, basando el alcance (scope) en la indentación, lo que hace el código mucho más limpio.
// 2. Definiciones de Nivel Superior (Top-Level Definitions): En Scala 3 ya no es necesario envolver funciones independientes dentro de un `object` (Singleton).
// 3. Punto de entrada @main: Reemplaza la verbosa estructura `def main(args: Array[String]): Unit` dentro de un objeto, generando automáticamente el código de inicialización.
// 4. Recursividad de Cola (@tailrec): Aceptando el "desafío recursivo" mencionado en el comentario original, se eliminan el bucle `while`, las variables `var` y el `return` explícito (el cual es un antipatrón fuertemente desaconsejado en Scala 3) a favor de un enfoque funcional matemáticamente puro.

import scala.annotation.tailrec

// La función se define libremente en la raíz del archivo (Top-Level)
def binarySearch(arr: Array[Int], target: Int): Int =
  
  // Función auxiliar anidada recursiva
  @tailrec
  def search(left: Int, right: Int): Int =
    // Sintaxis moderna: if ... then (sin paréntesis obligatorios)
    if left > right then
      -1
    else
      val mid = left + (right - left) / 2
      
      // Retornos declarativos sin la palabra clave 'return'
      if arr(mid) == target then 
        mid
      else if arr(mid) < target then 
        search(mid + 1, right)
      else 
        search(left, mid - 1)

  // Invocación inicial
  search(0, arr.length - 1)


// El punto de entrada se anota simplemente con @main
@main def runTests(): Unit =
  assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)
  assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)
  assert(binarySearch(Array.empty[Int], 1) == -1)
  
  println("All tests passed")


/*
EXPLICACION DE CAMBIOS E IDIOMATISMOS EN SCALA 3.3:

1. Eliminación del 'return' explícito y adopción de recursión pura:
   En el código de Scala 2.13, salir de un bucle `while` usando `return` forzaba internamente al compilador a lanzar y capturar una excepción de control (NonLocalReturnControl), lo cual es ineficiente y no idiomático. En Scala 3, el uso de `return` está muy desalentado (y pronto será obsoleto sin utilidades especiales como `boundary`). La forma idiomática y performante de iterar es mediante `@tailrec`, donde el compilador traduce la recursión en un salto de bucle ultra-eficiente a nivel de bytecode de la JVM.

2. Sintaxis Braceless (if/then):
   Scala 3 adopta un estilo muy similar a Python y Haskell. Declaraciones como `if (arr(mid) == target) return mid` pasaron a escribirse como `if arr(mid) == target then mid`. Esto aligera la carga cognitiva al leer el algoritmo.

3. Desaparición del `object P02BinarySearch_v213`:
   Las versiones anteriores requerían que todo el código viviera dentro de una clase, rasgo o un objeto estático debido a restricciones estructurales heredadas de Java. Scala 3 remueve esta limitación; las funciones `binarySearch` y `@main` viven y se compilan directamente en el nivel superior, eliminando la jerarquía innecesaria y enfocándose puramente en la lógica.
*/