//> using scala 2.13.16

// P06: Max Subarray Sum — Scala 2.13
// Paradigma: ESTRICTAMENTE ITERATIVO
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. var:
//    Permite actualizar variables durante el recorrido.
//
// 2. mutable.ListBuffer:
//    Colección mutable para almacenar los elementos.
//
// 3. mutable.Map:
//    Almacena los acumuladores del algoritmo.
//
// 4. while:
//    Realiza el procesamiento iterativo.
//
// 5. math.max():
//    Selecciona la mayor suma en cada iteración.
//
// 6. Scala 2.13:
//    Se utiliza sintaxis y biblioteca estándar
//    compatibles con esta versión.
//
// NOTA:
// ListBuffer, mutable.Map y while no son novedades
// de Scala 2.13. Se utilizan porque son adecuados
// para el paradigma iterativo solicitado.
//
// Biblioteca estándar únicamente.
// Sin map(), filter(), foldLeft() ni recursión.

import scala.collection.mutable

object P06MaxSubarraySum_v213 {

  def maxSubarraySum(arr: Array[Int]): Int = {

    // No se permite un subarreglo vacío.
    require(arr != null && arr.nonEmpty,
      "El arreglo no puede estar vacío")

    // Colección mutable para almacenar los números.
    val numbers = mutable.ListBuffer[Int]()

    // Copiar elementos con un bucle while.
    var i = 0

    while (i < arr.length) {
      numbers += arr(i)
      i += 1
    }

    // Estado mutable del algoritmo de Kadane.
    val state = mutable.Map[String, Int]()

    state("currentMax") = numbers.head
    state("globalMax") = numbers.head

    // Recorrido iterativo de los elementos restantes.
    // Iterator evita búsquedas por índice en ListBuffer.
    val iterator = numbers.iterator

    // Saltar el primer elemento, usado para inicializar.
    iterator.next()

    while (iterator.hasNext) {

      val num = iterator.next()

      // Recuperar los acumuladores actuales.
      var currentMax = state("currentMax")
      var globalMax = state("globalMax")

      // Iniciar un nuevo subarreglo o continuar el anterior.
      currentMax = math.max(num, currentMax + num)

      // Actualizar el máximo global.
      globalMax = math.max(globalMax, currentMax)

      // Guardar el estado actualizado.
      state("currentMax") = currentMax
      state("globalMax") = globalMax
    }

    state("globalMax")
  }

  def main(args: Array[String]): Unit = {

    // TEST 1: Ejemplo del enunciado.
    assert(
      maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6
    )

    // TEST 2: Todos los elementos negativos.
    assert(
      maxSubarraySum(Array(-8, -3, -6, -2, -5, -4)) == -2
    )

    // TEST 3: Todos los elementos positivos.
    assert(
      maxSubarraySum(Array(1, 2, 3, 4, 5)) == 15
    )

    println("All tests passed")
  }
}