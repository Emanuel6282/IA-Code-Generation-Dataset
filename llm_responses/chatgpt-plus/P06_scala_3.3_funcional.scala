//> using scala 3.3.6

// P06: Max Subarray Sum — Scala 3.3
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Sintaxis por indentación (Scala 3):
//    Permite definir bloques sin llaves.
//
// 2. val:
//    Define referencias que no pueden reasignarse.
//
// 3. Colecciones inmutables:
//    Se utiliza List[Int] sin modificar sus elementos.
//
// 4. map():
//    Transforma los elementos mediante funciones puras.
//
// 5. filter():
//    Selecciona elementos según una condición.
//
// 6. flatMap():
//    Transforma y aplana colecciones.
//
// 7. foldLeft():
//    Acumula estados inmutables de izquierda a derecha.
//
// 8. @tailrec:
//    Verifica que la recursión sea de cola y pueda
//    optimizarse sin crecimiento de pila.
//
// NOTA:
// map, filter, flatMap, foldLeft y @tailrec existían
// antes de Scala 3.3. La sintaxis por indentación
// es una característica de Scala 3.
//
// Biblioteca estándar únicamente.
// Sin var, mutable.* ni bucles imperativos.

import scala.annotation.tailrec

object P06MaxSubarraySum_v33:

  // Estado inmutable del algoritmo de Kadane.
  final case class State(
    currentMax: Int,
    globalMax: Int
  )

  // Función pura: produce un nuevo estado.
  def nextState(state: State, num: Int): State =

    val newCurrentMax =
      math.max(num, state.currentMax + num)

    val newGlobalMax =
      math.max(state.globalMax, newCurrentMax)

    State(newCurrentMax, newGlobalMax)

  // Recorrido funcional mediante recursión de cola.
  @tailrec
  def kadaneTail(
    remaining: List[Int],
    state: State
  ): State =

    remaining match
      case Nil =>
        state

      case head :: tail =>
        kadaneTail(
          tail,
          nextState(state, head)
        )

  // Función principal.
  def maxSubarraySum(arr: Array[Int]): Int =

    require(
      arr != null && arr.nonEmpty,
      "El arreglo no puede estar vacío"
    )

    // Convertir a una colección inmutable.
    val numbers: List[Int] = arr.toList

    // map(): conservar los valores originales.
    val mapped: List[Int] =
      numbers.map(identity)

    // filter(): conservar todos los elementos.
    // No se eliminan los números negativos.
    val filtered: List[Int] =
      mapped.filter(_ => true)

    // flatMap(): transformar sin alterar los valores.
    val flattened: List[Int] =
      filtered.flatMap(num => List(num))

    // Estado inicial: primer elemento.
    val initialState = State(
      flattened.head,
      flattened.head
    )

    // foldLeft(): aplicar Kadane funcionalmente.
    val finalState =
      flattened.tail.foldLeft(initialState) {
        (state, num) => nextState(state, num)
      }

    // @tailrec: recorrido funcional equivalente.
    val tailState = kadaneTail(
      flattened.tail,
      initialState
    )

    // Verificar que ambos recorridos coincidan.
    require(
      finalState == tailState,
      "Los cálculos funcionales no coinciden"
    )

    finalState.globalMax

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