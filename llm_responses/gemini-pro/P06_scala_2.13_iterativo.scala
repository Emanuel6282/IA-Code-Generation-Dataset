// CARACTERISTICAS DE SCALA 2.13 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Paradigma Imperativo/Iterativo Estricto: Se utiliza un bucle `while` en lugar de un bucle `for`. En Scala, los bucles `for` son en realidad un "azúcar sintáctico" (syntactic sugar) que se traduce a llamadas funcionales como `foreach`, `map` o `flatMap`. Usar `while` garantiza un control de flujo iterativo puro a nivel de bytecode, idéntico al de C o Java.
// 2. Mutabilidad Explícita y Gestión de Estado: Uso extensivo de la palabra clave `var` para los acumuladores y el índice del bucle. Se evita por completo cualquier método de colecciones de orden superior.
// 3. Estructura Estándar de Entrada (OOP): Al estar en Scala 2.13, carecemos de la anotación `@main` introducida en Scala 3. Por lo tanto, se empaqueta la ejecución dentro de un `object` con el método clásico `def main(args: Array[String]): Unit`.

object MaxSubarray_Scala213_Iterative {

  def maxSubarraySum(arr: Array[Int]): Int = {
    require(arr != null && arr.nonEmpty, "El arreglo debe tener al menos 1 elemento.")

    var currentSum = arr(0)
    var maxSum = arr(0)
    var i = 1

    // Bucle imperativo explícito, evitando for-comprehensions funcionales
    while (i < arr.length) {
      
      // Bifurcación explícita de Kadane
      if (currentSum + arr(i) > arr(i)) {
        currentSum = currentSum + arr(i)
      } else {
        currentSum = arr(i)
      }

      // Actualización del máximo global
      if (currentSum > maxSum) {
        maxSum = currentSum
      }

      // Incremento manual del iterador
      i += 1
    }

    maxSum
  }

  def main(args: Array[String]): Unit = {
    // ==========================================
    // CASOS DE PRUEBA
    // ==========================================

    // 1. Caso base del ejemplo: mezcla de positivos y negativos
    assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6, "Error: Caso base esperado 6")

    // 2. Caso con todos los elementos negativos (retorna el menos negativo)
    assert(maxSubarraySum(Array(-5, -2, -9, -1, -4)) == -1, "Error: Arreglo de negativos esperado -1")

    // 3. Caso con todos los elementos positivos (retorna la suma total)
    assert(maxSubarraySum(Array(1, 2, 3, 4, 5)) == 15, "Error: Arreglo de positivos esperado 15")

    println("Todos los tests iterativos en Scala 2.13 pasaron correctamente.")
  }
}