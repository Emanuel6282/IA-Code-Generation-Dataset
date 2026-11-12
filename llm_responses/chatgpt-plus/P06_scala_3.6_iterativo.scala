//> using scala 3.6.4

// P06: Max Subarray Sum (Kadane) — Scala 3.6
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Sintaxis por indentación de Scala 3:
//    Permite escribir bloques sin llaves.
//
// 2. Sintaxis while ... do:
//    Facilita la lectura de los bucles iterativos.
//
// 3. Sintaxis object Nombre:
//    Permite definir objetos con indentación significativa.
//
// 4. var:
//    Permite actualizar índices y acumuladores.
//
// 5. mutable.ListBuffer:
//    Almacena los elementos en una colección mutable.
//
// 6. mutable.Map:
//    Almacena el estado mutable del algoritmo.
//
// 7. math.max():
//    Calcula los máximos de Kadane.
//
// NOTA SOBRE SCALA 3.6:
//
// Las características anteriores son compatibles
// con Scala 3.6, pero no son exclusivas de esta versión.
//
// Scala 3.6 incorpora otras novedades del lenguaje,
// como mejoras en el manejo de importaciones y
// advertencias de migración. No son necesarias para
// mejorar este algoritmo.
//
// Se prioriza una implementación clara y eficiente.
//
// Sin map(), filter(), foldLeft() ni recursión.
// Biblioteca estándar únicamente.

import scala.collection.mutable

object P06MaxSubarraySum_v36:

  def maxSubarraySum(arr: Array[Int]): Int =

    // El subarreglo debe contener al menos un elemento.
    require(
      arr != null && arr.nonEmpty,
      "El arreglo no puede estar vacío"
    )

    // Colección mutable para almacenar los números.
    val numbers = mutable.ListBuffer.empty[Int]

    // Copiar los elementos utilizando while.
    var i = 0

    while i < arr.length do
      numbers += arr(i)
      i += 1

    // Estado mutable del algoritmo de Kadane.
    val state = mutable.Map.empty[String, Int]

    // Inicializar con el primer elemento.
    state("currentMax") = numbers.head
    state("globalMax") = numbers.head

    // Utilizar un iterador para recorrer ListBuffer.
    val iterator = numbers.iterator

    // Saltar el primer elemento, ya utilizado.
    iterator.next()

    // Procesar los elementos restantes.
    while iterator.hasNext do

      val num = iterator.next()

      // Variables mutables para el estado actual.
      var currentMax = state("currentMax")
      var globalMax = state("globalMax")

      // Iniciar un nuevo subarreglo o continuar el anterior.
      currentMax = math.max(
        num,
        currentMax + num
      )

      // Actualizar el máximo global.
      globalMax = math.max(globalMax, currentMax)

      // Guardar el estado actualizado.
      state("currentMax") = currentMax
      state("globalMax") = globalMax

    // Retornar la máxima suma encontrada.
    state("globalMax")

  def main(args: Array[String]): Unit =

    // TEST 1: Ejemplo original.
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