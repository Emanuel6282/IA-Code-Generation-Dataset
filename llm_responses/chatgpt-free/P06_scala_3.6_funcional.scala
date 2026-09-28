// P06: Max Subarray Sum (Kadane) — Scala 3.6
//
// Características de Scala 3.6 utilizadas:
// 1. Sintaxis simplificada de Scala 3: permite código más conciso y legible.
// 2. Inferencia de tipos con val: reduce ruido sin perder seguridad estática.
// 3. Colecciones inmutables y operaciones funcionales: permiten expresar
//    la transformación de datos sin estado mutable.
// 4. @tailrec: garantiza que la recursión utilizada para recorrer los datos
//    sea recursión de cola y pueda optimizarse.
//
// Paradigma: estrictamente funcional.
// No se utilizan var, mutable.*, for, while ni estado mutable.

import scala.annotation.tailrec

object P06MaxSubarray_v36 {

  def maxSubarraySum(arr: List[Int]): Int = {
    require(arr.nonEmpty, "El arreglo no puede estar vacio")

    @tailrec
    def loop(
        remaining: List[Int],
        currentMax: Int,
        globalMax: Int
    ): Int =
      remaining match {
        case Nil =>
          globalMax

        case head :: tail =>
          val newCurrentMax = Math.max(head, currentMax + head)
          val newGlobalMax = Math.max(globalMax, newCurrentMax)

          loop(tail, newCurrentMax, newGlobalMax)
      }

    loop(arr.tail, arr.head, arr.head)
  }

  def main(args: Array[String]): Unit = {

    // TC01: caso general
    assert(
      maxSubarraySum(List(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6
    )

    // TC02: un solo elemento
    assert(
      maxSubarraySum(List(1)) == 1
    )

    // TC03: todos los elementos negativos
    assert(
      maxSubarraySum(List(-2, -1)) == -1
    )

    println("All tests passed")
  }
}

// Cambios respecto a Scala 2.13:
//
// 1. Se reemplazó el bucle for y las variables var por una función recursiva
//    de cola anotada con @tailrec. Esto mantiene el algoritmo iterativo
//    conceptualmente, pero expresado de forma funcional.
//
// 2. El estado mutable currentMax/globalMax se convirtió en parámetros
//    inmutables de la función loop.
//
// 3. Se utiliza List[Int] como colección inmutable y pattern matching
//    (head :: tail / Nil) para recorrerla.
//
// 4. Math.max mantiene exactamente la lógica del algoritmo de Kadane:
//
//       newCurrentMax = max(elemento, suma_actual + elemento)
//
//    garantizando además que un subarreglo nunca sea vacío.
//
// 5. Se mantiene el comportamiento para valores completamente negativos:
//    al comenzar con arr.head, el resultado será siempre al menos uno de
//    los elementos originales.
//
// 6. require(arr.nonEmpty) hace explícito que el problema requiere al menos
//    un elemento y evita acceder silenciosamente a una posición inexistente.
//
// 7. No se necesitan características externas ni librerías de terceros.
//    Todo utiliza únicamente la biblioteca estándar de Scala 3.6.