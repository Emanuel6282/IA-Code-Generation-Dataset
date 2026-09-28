// P06: Max Subarray Sum (Kadane) — Scala 3.3
//
// Características de Scala 3.3 utilizadas:
// 1. Sintaxis de Scala 3 para declaraciones y bloques más limpios.
// 2. Inferencia de tipos en variables locales para reducir código
//    innecesario manteniendo la legibilidad.
// 3. La implementación conserva while y variables mutables porque
//    el requisito exige estrictamente el paradigma iterativo.
//
// Biblioteca estándar únicamente.
// Sin map(), filter(), foldLeft(), recursion ni colecciones mutables
// innecesarias.

// Algoritmo de Kadane:
// Mantiene la mejor suma que termina en la posición actual
// y la mejor suma encontrada globalmente.
def maxSubarraySum(arr: Array[Int]): Int =
  var currentMax = arr(0)
  var globalMax = arr(0)
  var i = 1

  while i < arr.length do
    currentMax = math.max(arr(i), currentMax + arr(i))
    globalMax = math.max(globalMax, currentMax)
    i += 1

  globalMax


@main def runTests(): Unit =
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


// Cambios respecto a Scala 2.13:
//
// 1. Se utiliza la sintaxis opcional de Scala 3:
//      def maxSubarraySum(...) =
//      while condicion do
//    Esto elimina llaves y hace el código más conciso.
//
// 2. Se utiliza @main, característica idiomática de Scala 3,
//    para definir directamente el punto de entrada del programa,
//    reemplazando el main(args: Array[String]) tradicional.
//
// 3. Se utiliza math.max mediante la API estándar para conservar
//    exactamente la lógica del algoritmo de Kadane.
//
// 4. Se mantienen var y while porque el requisito exige
//    estrictamente el paradigma iterativo.
//
// 5. No se utilizan map(), filter(), foldLeft(), recursion,
//    Streams ni colecciones mutables.
//
// 6. El comportamiento es idéntico al código original:
//    - el subarreglo debe contener al menos un elemento;
//    - funciona cuando todos los valores son negativos;
//    - [-2,1,-3,4,-1,2,1,-5,4] produce 6.