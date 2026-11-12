import scala.annotation.tailrec

object BinarySearch {

  /** Busca target en arr (ordenado ascendentemente) y retorna su índice, o -1 si no está.
    *
    * Recibe un IndexedSeq inmutable (por ejemplo, Vector) en lugar de Array, que es
    * mutable: así la función solo trabaja con colecciones inmutables y conserva
    * acceso por índice en tiempo prácticamente constante.
    */
  def binarySearch(arr: IndexedSeq[Int], target: Int): Int = {
    // @tailrec: el compilador garantiza que la recursión es de cola y la convierte
    // en un salto, así que no consume pila.
    @tailrec
    def search(low: Int, high: Int): Option[Int] =
      if (low > high) None
      else {
        val mid = low + (high - low) / 2 // evita el desbordamiento de Int de (low + high) / 2
        if (arr(mid) == target) Some(mid)
        else if (arr(mid) < target) search(mid + 1, high) // está en la mitad derecha
        else search(low, mid - 1)                          // está en la mitad izquierda
      }

    search(0, arr.length - 1).getOrElse(-1)
  }

  def main(args: Array[String]): Unit = {
    val testCases: List[(String, Vector[Int], Int, Int)] = List(
      // Caso 1: el objetivo está en el arreglo (ejemplo del enunciado)
      ("objetivo presente", Vector(1, 3, 5, 7, 9), 5, 2),
      // Caso 2: el objetivo no está en el arreglo (ejemplo del enunciado)
      ("objetivo ausente", Vector(1, 3, 5, 7, 9), 4, -1),
      // Caso 3: arreglo vacío
      ("arreglo vacío", Vector.empty, 1, -1)
    )

    // Búsqueda lineal de referencia con foldLeft: se queda con el primer índice
    // cuyo valor coincide. Sirve para validar el resultado por otro camino.
    def linearSearch(arr: IndexedSeq[Int], target: Int): Int =
      arr.indices.foldLeft(-1)((found, i) => if (found == -1 && arr(i) == target) i else found)

    // map + filter + map: calcula cada resultado, conserva los incorrectos y los
    // convierte en mensajes.
    val wrongResults: List[String] = testCases
      .map { case (name, arr, target, expected) => (name, arr, target, expected, binarySearch(arr, target)) }
      .filter { case (_, arr, target, expected, result) => result != expected || result != linearSearch(arr, target) }
      .map { case (name, arr, target, expected, result) =>
        s"Caso '$name': arr=$arr, target=$target, esperado $expected, obtenido $result"
      }

    // flatMap + Option.when (nuevo en Scala 2.13): comprobación adicional de que
    // cada elemento de cada arreglo se encuentra en su propio índice.
    val missedElements: List[String] = testCases.flatMap { case (name, arr, _, _) =>
      arr.indices.flatMap { i =>
        Option.when(binarySearch(arr, arr(i)) != i)(s"Caso '$name': el elemento ${arr(i)} no se halló en el índice $i")
      }
    }

    val failures = wrongResults ++ missedElements
    assert(failures.isEmpty, failures.mkString("\n"))
    println("Todas las pruebas pasaron.")
  }
}