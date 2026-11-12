//> using scala 3.3.6

// P06: Max Subarray Sum — Scala 3.3
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Sintaxis por indentación (Scala 3):
//    Permite escribir bloques sin llaves.
//
// 2. Palabra clave do (Scala 3):
//    Mejora la legibilidad de los bucles while.
//
// 3. Palabra clave then (Scala 3):
//    Permite escribir condiciones sin paréntesis.
//
// 4. Sintaxis object Nombre: (Scala 3):
//    Define objetos utilizando indentación.
//
// 5. var:
//    Permite actualizar acumuladores.
//
// 6. mutable.ListBuffer:
//    Almacena los elementos en una colección mutable.
//
// 7. mutable.Map:
//    Almacena el estado del algoritmo.
//
// 8. while:
//    Permite recorrer los elementos iterativamente.
//
// NOTA:
// var, ListBuffer, Map y while existían antes
// de Scala 3.3. La sintaxis de indentación,
// do y then pertenece a Scala 3.
//
// Biblioteca estándar únicamente.
// Sin map(), filter(), foldLeft() ni recursión.

import scala.collection.mutable

object P06MaxSubarraySum_v33:

  def maxSubarraySum(arr: Array[Int]): Int =

    // Un subarreglo debe contener al menos un elemento.
    require(
      arr != null && arr.nonEmpty,
      "El arreglo no puede estar vacío"
    )

    // Crear una colección mutable.
    val numbers = mutable.ListBuffer.empty[Int]

    // Copiar los elementos mediante while.
    var i = 0

    while i < arr.length do
      numbers += arr(i)
      i += 1

    // Crear el estado mutable de Kadane.
    val state = mutable.Map.empty[String, Int]

    // Inicializar con el primer elemento.
    state("currentMax") = numbers.head
    state("globalMax") = numbers.head

    // Iterator permite recorrer ListBuffer
    // sin búsquedas repetidas por índice.
    val iterator = numbers.iterator

    // Saltar el primer elemento ya procesado.
    iterator.next()

    // Recorrer los elementos restantes.
    while iterator.hasNext do

      val num = iterator.next()

      // Recuperar los acumuladores.
      var currentMax = state("currentMax")
      var globalMax = state("globalMax")

      // Iniciar un subarreglo o extender el anterior.
      currentMax = math.max(
        num,
        currentMax + num
      )

      // Actualizar la máxima suma encontrada.
      globalMax = math.max(globalMax, currentMax)

      // Guardar el estado actualizado.
      state("currentMax") = currentMax
      state("globalMax") = globalMax

    state("globalMax")

  def main(args: Array[String]): Unit =

    // TEST 1: Ejemplo del enunciado.
    assert(
      maxSubarraySum(
        Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)
      ) == 6
    )

    // TEST 2: Todos los elementos negativos.
    assert(
      maxSubarraySum(
        Array(-8, -3, -6, -2, -5, -4)
      ) == -2
    )

    // TEST 3: Todos los elementos positivos.
    assert(
      maxSubarraySum(
        Array(1, 2, 3, 4, 5)
      ) == 15
    )

    println("All tests passed")