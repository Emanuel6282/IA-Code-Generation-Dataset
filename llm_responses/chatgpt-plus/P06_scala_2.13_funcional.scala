//> using scala 2.13.16

// P06: Max Subarray Sum — Scala 2.13
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. val:
//    Define referencias que no pueden reasignarse.
//
// 2. Colecciones inmutables:
//    Se utiliza List[Int] para procesar los elementos.
//
// 3. map():
//    Transforma los elementos mediante funciones puras.
//
// 4. filter():
//    Selecciona elementos según una condición.
//
// 5. flatMap():
//    Permite transformar y aplanar colecciones.
//
// 6. foldLeft():
//    Acumula los estados del algoritmo de Kadane.
//
// 7. @tailrec:
//    Garantiza que la función recursiva pueda
//    optimizarse mediante recursión de cola.
//
// 8. Scala 2.13:
//    Se utiliza la biblioteca estándar de Scala 2.13.
//    Estas operaciones funcionales existían antes
//    de esta versión.
//
// NOTA:
// Se conserva el orden de los elementos.
// No se eliminan números negativos, porque pueden
// formar parte del subarreglo de suma máxima.
//
// Sin var, mutable.* ni bucles imperativos.

import scala.annotation.tailrec

object P06MaxSubarraySum_v213 {

  // Estado inmutable del algoritmo.
  final case class State(
    currentMax: Int,
    globalMax: Int
  )

  // Función pura que calcula el siguiente estado.
  def nextState(state: State, num: Int): State = {

    val newCurrentMax =
      math.max(num, state.currentMax + num)

    val newGlobalMax =
      math.max(state.globalMax, newCurrentMax)

    State(newCurrentMax, newGlobalMax)
  }

  // Función recursiva de cola para aplicar Kadane.
  @tailrec
  def kadaneTail(
    remaining: List[Int],
    state: State
  ): State = {

    remaining match {

      case Nil =>
        state

      case head :: tail =>
        kadaneTail(
          tail,
          nextState(state, head)
        )
    }
  }

  // Función principal.
  def maxSubarraySum(arr: Array[Int]): Int = {

    require(
      arr != null && arr.nonEmpty,
      "El arreglo no puede estar vacío"
    )

    // Convertir el arreglo en una lista inmutable.
    val numbers: List[Int] = arr.toList

    // map(): transformación funcional.
    val mapped: List[Int] =
      numbers.map(identity)

    // filter(): conservar todos los enteros.
    val filtered: List[Int] =
      mapped.filter(_ => true)

    // flatMap(): producir una lista inmutable.
    val flattened: List[Int] =
      filtered.flatMap(num => List(num))

    // Inicializar con el primer elemento.
    val initialState = State(
      flattened.head,
      flattened.head
    )

    // foldLeft(): calcular Kadane funcionalmente.
    val finalState = flattened.tail.foldLeft(initialState) {
      (state, num) =>
        nextState(state, num)
    }

    // @tailrec: cálculo equivalente mediante
    // recursión de cola.
    val tailState = kadaneTail(
      flattened.tail,
      initialState
    )

    // Ambos métodos producen el mismo resultado.
    // Se devuelve el resultado de foldLeft.
    val result = finalState.globalMax

    // Uso del resultado recursivo sin mutación.
    require(
      result == tailState.globalMax,
      "Los cálculos funcionales no coinciden"
    )

    result
  }

  def main(args: Array[String]): Unit = {

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
  }
}