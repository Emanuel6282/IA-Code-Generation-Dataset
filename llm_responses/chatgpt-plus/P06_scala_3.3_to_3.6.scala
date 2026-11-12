//> using scala 3.6.4

// P06: Max Subarray Sum (Kadane) — Scala 3.6
//
// CARACTERÍSTICAS UTILIZADAS Y JUSTIFICACIÓN:
//
// 1. Indentación significativa de Scala 3:
//    Elimina llaves innecesarias y mejora la legibilidad.
//
// 2. Sintaxis if ... then ... else:
//    Expresa las condiciones de manera más clara.
//
// 3. Definición de objetos mediante object Nombre:
//    Aprovecha la sintaxis moderna de Scala 3.
//
// 4. foldLeft con tupla acumuladora:
//    Sustituye el estado mutable por valores inmutables.
//    Es una refactorización funcional, no una novedad
//    exclusiva de Scala 3.6.
//
// 5. Desestructuración de tuplas:
//    Facilita la lectura de los dos acumuladores.
//
// 6. Iterator:
//    Permite recorrer el arreglo sin copiar sus elementos.
//
// NOTA SOBRE LA VERSIÓN:
// Las características sintácticas utilizadas se
// introdujeron en Scala 3 y siguen disponibles en 3.6.
// No se fuerza ninguna característica exclusiva de 3.6
// porque no aporta una mejora necesaria a Kadane.
//
// Se preservan la lógica, los resultados y los tests.
// Biblioteca estándar únicamente.

object P06MaxSubarray_v36:

  def maxSubarraySum(arr: Array[Int]): Int =

    // Igual que en el código original:
    // el primer elemento inicializa ambos acumuladores.
    val initialState = (arr(0), arr(0))

    // Recorrer desde el segundo elemento.
    // Cada iteración produce una nueva tupla inmutable.
    val finalState =
      arr.iterator.drop(1).foldLeft(initialState) {
        case ((currentMax, globalMax), num) =>

          // Mantener exactamente la decisión original.
          val newCurrentMax =
            if currentMax + num > num then
              currentMax + num
            else
              num

          // Actualizar el máximo global.
          val newGlobalMax =
            if newCurrentMax > globalMax then
              newCurrentMax
            else
              globalMax

          // Devolver el nuevo estado.
          (newCurrentMax, newGlobalMax)
      }

    // Extraer la máxima suma de la tupla final.
    finalState._2

  def main(args: Array[String]): Unit =

    // TEST 1: Ejemplo original.
    assert(
      maxSubarraySum(
        Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)
      ) == 6
    )

    // TEST 2: Arreglo con un único elemento.
    assert(
      maxSubarraySum(Array(1)) == 1
    )

    // TEST 3: Todos los elementos negativos.
    assert(
      maxSubarraySum(Array(-2, -1)) == -1
    )

    // TEST 4: Caso adicional original.
    assert(
      maxSubarraySum(Array(5, 4, -1, 7, 8)) == 23
    )

    println("All tests passed")

// RESUMEN DE CAMBIOS:
//
// 1. Se configuró Scala 3.6.4 mediante la directiva
//    de versión de Scala CLI.
//
// 2. Se cambió el nombre del objeto de
//    P06MaxSubarray_v213 a P06MaxSubarray_v36.
//
// 3. Se adoptó la indentación significativa de Scala 3
//    para simplificar la estructura del código.
//
// 4. Se sustituyó while por foldLeft, expresando
//    el recorrido como una acumulación funcional.
//
// 5. Se reemplazaron currentMax y globalMax mutables
//    por una tupla inmutable (Int, Int).
//
// 6. Se utilizaron val y desestructuración de tuplas,
//    prácticas idiomáticas de programación funcional.
//
// 7. Se utilizó if ... then ... else, sintaxis de Scala 3.
//
// 8. Se conservó la lógica exacta de comparación:
//    currentMax + num > num.
//
// 9. Se mantuvo el nombre maxSubarraySum.
//
// 10. Se conservaron los cuatro casos de prueba,
//     sin modificar sus resultados esperados.
//
// 11. No se agregó validación de entradas vacías o null,
//     para preservar el comportamiento original.
//
// 12. La complejidad temporal sigue siendo O(n)
//     y el espacio auxiliar permanece en O(1).