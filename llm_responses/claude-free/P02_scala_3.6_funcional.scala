// P02: Binary Search — Scala 3.6 (paradigma funcional)
// Solo biblioteca estandar de Scala. Sintaxis "significant indentation" de Scala 3.

import scala.annotation.tailrec

def binarySearch(arr: Array[Int], target: Int): Int =

  @tailrec
  def loop(left: Int, right: Int): Int =
    if left > right then
      -1
    else
      val mid = left + (right - left) / 2
      if arr(mid) == target then mid
      else if arr(mid) < target then loop(mid + 1, right)
      else loop(left, mid - 1)

  loop(0, arr.length - 1)

@main def runTests(): Unit =
  // Caso 1: ejemplo del enunciado, objetivo presente en el medio
  assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)

  // Caso 2: ejemplo del enunciado, objetivo ausente
  assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)

  // Caso 3: arreglo vacio -> siempre -1
  assert(binarySearch(Array.empty[Int], 1) == -1)

  println("Todos los casos de prueba pasaron correctamente.")