// P06: Max Subarray Sum (Kadane) — Scala 3.6
// Características de Scala 3.6 utilizadas:
// 1. Sintaxis simplificada de Scala 3: permite un código más limpio y legible.
// 2. Inferencia de tipos mejorada: reduce anotaciones innecesarias sin perder seguridad de tipos.
// 3. Sintaxis de colecciones compatible con Scala 3: se utilizan Array[Int] y operaciones
//    imperativas estándar, manteniendo el algoritmo de Kadane eficiente.
//
// Paradigma: estrictamente iterativo.
// No se utilizan map(), filter(), foldLeft(), flatMap() ni recursión.

object P06MaxSubarray_v36 {

  def maxSubarraySum(arr: Array[Int]): Int = {
    require(arr.nonEmpty, "El arreglo no puede estar vacío")

    var currentMax = arr(0)
    var globalMax = arr(0)
    var i = 1

    while (i < arr.length) {
      currentMax = Math.max(arr(i), currentMax + arr(i))
      globalMax = Math.max(globalMax, currentMax)
      i += 1
    }

    globalMax
  }

  def main(args: Array[String]): Unit = {
    // TC01: caso general
    assert(
      maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6
    )

    // TC02: un solo elemento
    assert(
      maxSubarraySum(Array(1)) == 1
    )

    // TC03: todos los elementos negativos
    assert(
      maxSubarraySum(Array(-2, -1)) == -1
    )

    println("All tests passed")
  }
}

// Cambios realizados:
// 1. Se actualizó la declaración del objeto a Scala 3.6.
// 2. Se mantiene el algoritmo de Kadane con complejidad O(n) en tiempo y O(1) en memoria.
// 3. Se utiliza while para cumplir estrictamente el paradigma iterativo solicitado.
// 4. Se utilizan var para los acumuladores currentMax, globalMax e índice i.
// 5. Se utiliza Math.max para expresar directamente la elección entre extender
//    el subarreglo actual o comenzar uno nuevo.
// 6. Se añadió require(arr.nonEmpty) para evitar acceder a arr(0) cuando el arreglo
//    está vacío. Esto no modifica los resultados para las entradas válidas de los tests.
// 7. Los tres casos de prueba originales se conservan y se ejecutan mediante assert.
// 8. No se utilizan librerías de terceros ni operaciones funcionales como map/filter/foldLeft.