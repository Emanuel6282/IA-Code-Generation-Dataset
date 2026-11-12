// CARACTERISTICAS DE SCALA 3.6 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Sintaxis Braceless Madura: La versión 3.6 consolida la escritura basada en indentación significativa. Todo el algoritmo prescinde de llaves, logrando una densidad visual mínima que compite directamente con Python, pero respaldada por un sistema de tipos estático y robusto.
// 2. Desestructuración Directa en Trailing Lambdas: Aprovechamos al máximo el tipado de Scala 3 pasando un bloque de función con `:` a `foldLeft`. Las tuplas `(currMax, globMax)` se empaquetan y desempaquetan sobre la marcha, sin sobrecarga de sintaxis.
// 3. Inmutabilidad Absoluta: Cero variables `var`. Todo el estado de Kadane fluye de forma pura a través de las evaluaciones del acumulador, siendo ideal para el análisis de transparencia referencial.

def maxSubarraySum(arr: Array[Int]): Int =
  require(arr != null && arr.nonEmpty, "El arreglo debe contener al menos un elemento.")

  // El estado inicial es una tupla puramente inmutable: (currentMax, globalMax)
  val initialState = (arr.head, arr.head)

  // Iteración funcional usando foldLeft sobre el resto de la colección.
  // La sintaxis de trailing lambda ':' mantiene el bloque limpio y declarativo.
  val (_, finalMax) = arr.tail.foldLeft(initialState): (acc, x) =>
    val (currMax, globMax) = acc
    val newCurr = math.max(x, currMax + x)
    val newGlob = math.max(globMax, newCurr)
    (newCurr, newGlob)

  finalMax


@main def runFunctionalTests36(): Unit =
  // ==========================================
  // CASOS DE PRUEBA
  // ==========================================

  // 1. Caso base del ejemplo: mezcla de positivos y negativos
  assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6, "Error: Caso base esperado 6")

  // 2. Caso con todos los elementos negativos (retorna el menos negativo)
  assert(maxSubarraySum(Array(-5, -2, -9, -1, -4)) == -1, "Error: Arreglo de negativos esperado -1")

  // 3. Caso con todos los elementos positivos (retorna la suma total)
  assert(maxSubarraySum(Array(1, 2, 3, 4, 5)) == 15, "Error: Arreglo de positivos esperado 15")

  println("Todos los tests funcionales en Scala 3.6 pasaron correctamente.")