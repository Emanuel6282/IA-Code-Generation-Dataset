// CARACTERISTICAS DE SCALA 3.3 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Sintaxis sin llaves y Funciones de Nivel Superior: El código prescinde de llaves `{}` usando indentación significativa. Además, la función y el `@main` existen libremente (top-level) sin necesidad de encapsularse en un `object`, reduciendo la carga visual.
// 2. Trailing Lambdas (Bloques de funciones): Scala 3 permite pasar funciones anónimas a métodos de orden superior usando `:` (como en `foldLeft(initialState):`), lo que simula una estructura de control nativa y limpia el código de paréntesis anidados.
// 3. Plegado Funcional (`foldLeft`) con Tuplas Nativas: A diferencia de Java, Scala posee soporte nativo para tuplas, permitiendo desempaquetar el estado `(currMax, globMax)` de forma directa e inmutable sin necesidad de declarar clases auxiliares o records.

def maxSubarraySum(arr: Array[Int]): Int =
  require(arr != null && arr.nonEmpty, "El arreglo debe contener al menos un elemento.")

  // El estado inicial inmutable es una tupla: (currentMax, globalMax)
  val initialState = (arr.head, arr.head)

  // Iteración pura sin mutación utilizando foldLeft sobre el resto del arreglo.
  // Se emplea la sintaxis de "Trailing Lambda" introducida en Scala 3.
  val (_, finalMax) = arr.tail.foldLeft(initialState): (acc, x) =>
    val (currMax, globMax) = acc
    val newCurr = math.max(x, currMax + x)
    val newGlob = math.max(globMax, newCurr)
    (newCurr, newGlob)

  finalMax


@main def runFunctionalTests(): Unit =
  // ==========================================
  // CASOS DE PRUEBA
  // ==========================================

  // 1. Caso base del ejemplo: mezcla de positivos y negativos
  assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6, "Error: Caso base esperado 6")

  // 2. Caso con todos los elementos negativos (retorna el menos negativo)
  assert(maxSubarraySum(Array(-5, -2, -9, -1, -4)) == -1, "Error: Arreglo de negativos esperado -1")

  // 3. Caso con todos los elementos positivos (retorna la suma total)
  assert(maxSubarraySum(Array(1, 2, 3, 4, 5)) == 15, "Error: Arreglo de positivos esperado 15")

  println("Todos los tests funcionales en Scala 3.3 pasaron correctamente.")