import scala.annotation.tailrec

object BinarySearchScala213Functional {

  def binarySearch(arr: Array[Int], target: Int): Int = {
    // Paradigma Funcional Estricto en Scala 2.13:
    // Uso de recursión de cola (@tailrec), inmutabilidad total (val),
    // ausencia de 'var' o colecciones mutables y pattern matching.
    @tailrec
    def searchRange(left: Int, right: Int): Int = {
      if (left > right) -1
      else {
        val mid = left + (right - left) / 2
        arr(mid) match {
          case `target`          => mid
          case x if x < target   => searchRange(mid + 1, right)
          case _                 => searchRange(left, mid - 1)
        }
      }
    }

    // Evaluación funcional inmutable utilizando la colección estándar de Scala
    Option(arr)
      .filter(_.nonEmpty)
      .map(a => searchRange(0, a.length - 1))
      .getOrElse(-1)
  }

  def main(args: Array[String]): Unit = {
    // --- Casos de prueba (Scala 2.13) ---

    // Caso 1: Elemento presente en el arreglo (ejemplo provisto)
    assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2, "Falló el Caso 1")

    // Caso 2: Elemento no presente en el arreglo (ejemplo provisto)
    assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1, "Falló el Caso 2")

    // Caso 3: Arreglo vacío y comprobación en extremos
    assert(binarySearch(Array(), 10) == -1, "Falló el Caso 3a (Arreglo vacío)")
    assert(binarySearch(Array(10, 20, 30, 40), 10) == 0, "Falló el Caso 3b (Primer elemento)")
    assert(binarySearch(Array(10, 20, 30, 40), 40) == 3, "Falló el Caso 3c (Último elemento)")

    println("¡Todos los casos de prueba pasaron correctamente!")
  }
}