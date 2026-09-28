// P06: Max Subarray Sum (Kadane) — Scala 3.6
//
// Características utilizadas:
// 1. Sintaxis `while ... do`: reduce ruido sintáctico respecto a Scala 2.13
//    y mantiene claramente el enfoque iterativo.
// 2. Sintaxis `if ... then ... else`: permite expresar las decisiones
//    condicionales de forma más legible.
// 3. Inferencia de tipos: evita anotaciones innecesarias en las variables
//    locales sin perder seguridad estática.
//
// Nota: Scala 3.6 no introduce una característica que mejore
// materialmente el algoritmo de Kadane. Por ello no se fuerzan
// características nuevas que no aporten una ventaja real.
//
// Algoritmo: Kadane
// Complejidad: O(n) tiempo y O(1) espacio adicional.

object P06Maxsubarray_v36:

  def maxSubarraySum(arr: Array[Int]): Int =
    var currentMax = arr(0)
    var globalMax = arr(0)
    var i = 1

    while i < arr.length do
      currentMax =
        if currentMax + arr(i) > arr(i) then
          currentMax + arr(i)
        else
          arr(i)

      if currentMax > globalMax then
        globalMax = currentMax

      i += 1

    globalMax

  def main(args: Array[String]): Unit =
    assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6)
    assert(maxSubarraySum(Array(1)) == 1)
    assert(maxSubarraySum(Array(-2, -1)) == -1)
    assert(maxSubarraySum(Array(5, 4, -1, 7, 8)) == 23)

    println("All tests passed")


// Cambios realizados:
//
// 1. Versión de la clase:
//    P06MaxSubarray_v213 -> P06Maxsubarray_v36 para identificar
//    explícitamente la versión Scala 3.6.
//
// 2. Sintaxis de Scala 3:
//    Se reemplazó:
//
//        while (condicion) {
//            ...
//        }
//
//    por:
//
//        while condicion do
//            ...
//
//    Esta sintaxis es más concisa e idiomática en Scala 3.
//
// 3. Condicionales:
//    Se utiliza `if ... then ... else`, sintaxis moderna de Scala 3,
//    especialmente clara al asignar el nuevo valor de currentMax.
//
// 4. Algoritmo:
//    Se conserva exactamente Kadane. currentMax representa la mejor suma
//    de un subarreglo que termina en la posición actual y globalMax la mejor
//    suma encontrada hasta ese momento.
//
// 5. Comportamiento:
//    Se mantienen los mismos resultados, incluyendo el caso en que todos
//    los elementos sean negativos.
//
// 6. Tests:
//    Se conservan exactamente los cuatro casos de prueba originales.
//
// 7. Rendimiento:
//    Se mantiene O(n) en tiempo y O(1) en espacio adicional.
//
// 8. Paradigma:
//    Se conserva el enfoque iterativo original con var y while. No se
//    introducen Streams, map, filter, foldLeft ni recursión porque eso
//    modificaría el paradigma del código base.