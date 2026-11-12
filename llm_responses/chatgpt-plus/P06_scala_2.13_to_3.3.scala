//> using scala 3.3.6

// P06: Max Subarray Sum (Kadane) — Scala 3.3
//
// CARACTERÍSTICAS DE SCALA 3 UTILIZADAS:
//
// 1. Sintaxis de indentación significativa:
//    Permite eliminar llaves y mejorar la legibilidad.
//
// 2. Sintaxis object Nombre:
//    Define el objeto mediante indentación.
//
// 3. Sintaxis def funcion =
//    Simplifica la declaración de funciones.
//
// 4. Tuplas con desestructuración:
//    Permiten representar currentMax y globalMax
//    sin utilizar variables mutables.
//
// 5. Lambdas con patrones de tupla:
//    Permiten expresar claramente la transición
//    de estados dentro de foldLeft.
//
// NOTA:
// La indentación significativa es propia de Scala 3,
// pero foldLeft, las tuplas y su desestructuración
// ya existían en Scala 2.13.
//
// Esta migración prioriza legibilidad e inmutabilidad.
// No se atribuyen mejoras de rendimiento automáticas
// a la nueva sintaxis.

object P06MaxSubarray_v33:

  def maxSubarraySum(arr: Array[Int]): Int =

    // Mismo comportamiento del código original:
    // el primer elemento inicializa ambos máximos.
    val initialState = (arr(0), arr(0))

    // foldLeft reemplaza el bucle while.
    val (_, globalMax) =
      arr.iterator.drop(1).foldLeft(initialState) {
        case ((currentMax, globalMax), num) =>

          // Misma decisión que el if original.
          val newCurrentMax =
            if currentMax + num > num then
              currentMax + num
            else
              num

          // Mismo cálculo del máximo global.
          val newGlobalMax =
            if newCurrentMax > globalMax then
              newCurrentMax
            else
              globalMax

          // Nuevo estado inmutable.
          (newCurrentMax, newGlobalMax)
      }

    globalMax

  def main(args: Array[String]): Unit =

    // TEST 1: Ejemplo original.
    assert(
      maxSubarraySum(
        Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)
      ) == 6
    )

    // TEST 2: Un solo elemento.
    assert(
      maxSubarraySum(Array(1)) == 1
    )

    // TEST 3: Todos los elementos negativos.
    assert(
      maxSubarraySum(Array(-2, -1)) == -1
    )

    // TEST 4: Otro caso del código original.
    assert(
      maxSubarraySum(Array(5, 4, -1, 7, 8)) == 23
    )

    println("All tests passed")

// CAMBIOS REALIZADOS EN LA MIGRACIÓN:
//
// 1. Se actualizó el código a Scala 3.3.
//
// 2. Se reemplazaron las llaves por indentación
//    significativa, idiomática en Scala 3.
//
// 3. Se sustituyó el bucle while por foldLeft,
//    haciendo explícita la acumulación de estados.
//
// 4. Se eliminaron las variables mutables var.
//    Ahora los acumuladores se representan mediante
//    una tupla inmutable (currentMax, globalMax).
//
// 5. Se utilizó desestructuración de tuplas para
//    mejorar la claridad de las transiciones.
//
// 6. Se incorporó la sintaxis if ... then ... else
//    de Scala 3.
//
// 7. Se conservaron exactamente los cuatro tests
//    originales y sus resultados esperados.
//
// 8. Se mantuvo el nombre maxSubarraySum.
//
// 9. Se preservó la complejidad temporal O(n).
//    El iterador evita crear una copia del arreglo.
//
// 10. No se agregó validación para arreglos vacíos
//     o null, conservando el comportamiento original.