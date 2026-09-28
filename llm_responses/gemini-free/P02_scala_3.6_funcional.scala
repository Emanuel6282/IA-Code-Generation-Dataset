import scala.annotation.tailrec

// Scala 3.6 - Paradigma Funcional Estricto
// Implementación pura e inmutable con optimización de recursión de cola (@tailrec),
// sintaxis basada en sangría (quiet syntax) y transformaciones funcionales con Option.

def binarySearch(arr: Array[Int], target: Int): Int =
  // Función auxiliar puramente recursiva optimizada para Tail-Call Elimination
  @tailrec
  def searchRange(left: Int, right: Int): Int =
    if left > right then -1
    else
      val mid = left + (right - left) / 2
      arr(mid) match
        case `target`        => mid
        case x if x < target => searchRange(mid + 1, right)
        case _               => searchRange(left, mid - 1)

  // Enfoque declarativo: transformación inmutable usando la monada Option
  Option(arr)
    .filter(_.nonEmpty)
    .map(a => searchRange(0, a.length - 1))
    .getOrElse(-1)

@main def run(): Unit =
  // --- Casos de prueba (Scala 3.6) ---

  // Caso 1: Elemento presente en el arreglo (ejemplo provisto)
  assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2, "Falló el Caso 1")

  // Caso 2: Elemento no presente en el arreglo (ejemplo provisto)
  assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1, "Falló el Caso 2")

  // Caso 3: Arreglo vacío y comprobación en extremos
  assert(binarySearch(Array(), 10) == -1, "Falló el Caso 3a (Arreglo vacío)")
  assert(binarySearch(Array(10, 20, 30, 40), 10) == 0, "Falló el Caso 3b (Primer elemento)")
  assert(binarySearch(Array(10, 20, 30, 40), 40) == 3, "Falló el Caso 3c (Último elemento)")

  println("¡Todos los casos de prueba pasaron correctamente!")