//> using scala 3.6.4

// P06: Max Subarray Sum — Scala 3.6
// Paradigma: ESTRICTAMENTE FUNCIONAL
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. val:
//    Referencias inmutables.
//
// 2. List[Int]:
//    Colección inmutable para procesar los elementos.
//
// 3. map(), filter(), flatMap():
//    Transformaciones funcionales sin efectos secundarios.
//
// 4. foldLeft():
//    Implementa Kadane acumulando estados inmutables.
//
// 5. @tailrec:
//    Garantiza que la función recursiva sea de cola.
//
// 6. case class:
//    Representa el estado inmutable del algoritmo.
//
// 7. Sintaxis Scala 3:
//    Utiliza indentación significativa, match y
//    definiciones sin llaves.
//
// NOTA SOBRE SCALA 3.6:
// Estas construcciones son compatibles con Scala 3.6,
// aunque no son novedades exclusivas de esa versión.
// No se necesitan características específicas de 3.6
// para resolver este problema de manera funcional.
//
// Biblioteca estándar únicamente.
// Sin var, mutable.* ni bucles imperativos.

import scala.annotation.tailrec

object P06MaxSubarraySum_v36:

  // Estado inmutable de Kadane.
  private final case class State(
    currentMax: Int,
    globalMax: Int
  )

  // Función pura: calcula un nuevo estado.
  private def nextState(state: State, num: Int): State =

    val current =
      math.max(num, state.currentMax + num)

    val global =
      math.max(state.globalMax, current)

    State(current, global)

  // Recorrido alternativo mediante recursión de cola.
  @tailrec
  private def kadaneTail(
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

  // Función requerida.
  def maxSubarraySum(arr: Array[Int]): Int =

    require(
      arr != null && arr.nonEmpty,
      "El arreglo no puede estar vacío"
    )

    // Crear una colección inmutable.
    val numbers: List[Int] = arr.toList

    // map(): conservar los valores originales.
    val mapped =
      numbers.map(identity)

    // filter(): conservar todos los enteros.
    // No se deben eliminar los negativos.
    val filtered =
      mapped.filter(_ => true)

    // flatMap(): mantener los elementos y su orden.
    val flattened =
      filtered.flatMap(num => List(num))

    // Inicializar con el primer elemento.
    val initialState = State(
      flattened.head,
      flattened.head
    )

    // Kadane funcional mediante foldLeft().
    val finalState =
      flattened.tail.foldLeft(initialState) {
        (state, num) => nextState(state, num)
      }

    // Cálculo equivalente mediante @tailrec.
    val recursiveState =
      kadaneTail(flattened.tail, initialState)

    // Comprobar que ambos enfoques coinciden.
    require(
      finalState == recursiveState,
      "Los resultados funcionales no coinciden"
    )

    finalState.globalMax

  def main(args: Array[String]): Unit =

    // TEST 1: Ejemplo del enunciado.
    assert(
      maxSubarraySum(
        Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)
      ) == 6
    )

    // TEST 2: Todos los números negativos.
    assert(
      maxSubarraySum(
        Array(-8, -3, -6, -2, -5, -4)
      ) == -2
    )

    // TEST 3: Todos los números positivos.
    assert(
      maxSubarraySum(
        Array(1, 2, 3, 4, 5)
      ) == 15
    )

    println("All tests passed")

// RESUMEN FINAL:
//
// - Scala 3.6.
// - Paradigma estrictamente funcional.
// - Estados y colecciones inmutables.
// - Uso de map(), filter(), flatMap() y foldLeft().
// - Uso de @tailrec para recursión de cola.
// - Sin var ni mutable.*.
// - Exactamente 3 pruebas con assert.
//
// Las transformaciones map(identity), filter(_ => true)
// y flatMap(num => List(num)) son intencionalmente
// neutras para cumplir los requisitos solicitados.
// En una implementación de producción pueden omitirse.