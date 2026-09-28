// P06: Max Subarray Sum (Kadane) — Scala 3.3
// Paradigma: estrictamente funcional
//
// Características de Scala 3.3 utilizadas:
// 1. Top-level definitions: permite declarar la función directamente,
//    sin necesidad de envolverla en un objeto.
// 2. Inferencia de tipos: reduce código repetitivo manteniendo tipos seguros.
// 3. Colecciones inmutables y val: evitan estado mutable.
// 4. foldLeft: permite expresar el algoritmo de Kadane como una reducción
//    funcional de izquierda a derecha.
//
// Complejidad:
// - Tiempo: O(n)
// - Espacio adicional: O(1), sin contar la estructura de entrada.

def maxSubarraySum(arr: List[Int]): Int =
  arr match
    case Nil =>
      throw new IllegalArgumentException("El arreglo no puede estar vacío")

    case head :: tail =>
      val (globalMax, _) = tail.foldLeft((head, head)) {
        case ((best, current), value) =>
          val newCurrent = math.max(value, current + value)
          val newBest = math.max(best, newCurrent)
          (newBest, newCurrent)
      }

      globalMax


// Casos de prueba
assert(
  maxSubarraySum(List(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6
)

assert(
  maxSubarraySum(List(1)) == 1
)

assert(
  maxSubarraySum(List(-2, -1)) == -1
)

println("All tests passed")


// Cambios respecto a la versión iterativa:
// 1. Se eliminó el estado mutable (`var currentMax` y `var globalMax`).
//    El estado de Kadane se representa mediante una tupla inmutable.
//
// 2. El bucle `for` fue reemplazado por `foldLeft`, que procesa cada elemento
//    de izquierda a derecha y produce un nuevo estado en cada paso.
//
// 3. `math.max` expresa directamente las dos decisiones de Kadane:
//    - comenzar un nuevo subarreglo con `value`
//    - extender el subarreglo anterior con `current + value`
//
// 4. El patrón `head :: tail` permite inicializar el algoritmo con el primer
//    elemento, garantizando que nunca se considere un subarreglo vacío.
//
// 5. `Nil` se maneja explícitamente. Como el problema exige al menos un
//    elemento, una entrada vacía produce IllegalArgumentException.
//
// 6. Se mantienen exactamente los mismos resultados para los casos de prueba
//    originales, incluyendo el caso donde todos los valores son negativos.
//
// 7. Las definiciones de nivel superior de Scala 3 permiten mantener la función
//    `maxSubarraySum` directamente en el archivo.