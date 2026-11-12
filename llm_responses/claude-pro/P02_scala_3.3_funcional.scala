import scala.annotation.tailrec

/** Busca target en arr (ordenado ascendentemente) y retorna su índice, o -1 si no está.
  *
  * Recibe un IndexedSeq inmutable (por ejemplo, Vector) en lugar de Array, que es
  * mutable: así la función solo trabaja con colecciones inmutables y conserva
  * acceso por índice en tiempo prácticamente constante.
  *
  * Definición de nivel superior (Scala 3): no necesita un `object` contenedor.
  */
def binarySearch(arr: IndexedSeq[Int], target: Int): Int =
  // @tailrec: el compilador garantiza que la recursión es de cola y la convierte
  // en un salto, así que no consume pila.
  @tailrec
  def search(low: Int, high: Int): Option[Int] =
    if low > high then None
    else
      val mid = low + (high - low) / 2 // evita el desbordamiento de Int de (low + high) / 2
      if arr(mid) == target then Some(mid)
      else if arr(mid) < target then search(mid + 1, high) // está en la mitad derecha
      else search(low, mid - 1)                             // está en la mitad izquierda

  search(0, arr.length - 1).getOrElse(-1)

/** Caso de prueba: case class inmutable. */
case class TestCase(name: String, arr: Vector[Int], target: Int, expected: Int)

/** Búsqueda lineal de referencia con foldLeft: primer índice cuyo valor coincide. */
def linearSearch(arr: IndexedSeq[Int], target: Int): Int =
  arr.indices.foldLeft(-1)((found, i) => if found == -1 && arr(i) == target then i else found)

/** Punto de entrada con @main (Scala 3): sin object ni `def main(args: Array[String])`. */
@main def runTests(): Unit =
  val testCases = List(
    // Caso 1: el objetivo está en el arreglo (ejemplo del enunciado)
    TestCase("objetivo presente", Vector(1, 3, 5, 7, 9), 5, 2),
    // Caso 2: el objetivo no está en el arreglo (ejemplo del enunciado)
    TestCase("objetivo ausente", Vector(1, 3, 5, 7, 9), 4, -1),
    // Caso 3: arreglo vacío
    TestCase("arreglo vacío", Vector.empty, 1, -1)
  )

  // map + filter + map: calcula cada resultado, conserva los incorrectos (frente al
  // valor esperado o a la búsqueda lineal de referencia) y los convierte en mensajes.
  val wrongResults = testCases
    .map(tc => (tc, binarySearch(tc.arr, tc.target)))
    .filter((tc, result) => result != tc.expected || result != linearSearch(tc.arr, tc.target))
    .map((tc, result) =>
      s"Caso '${tc.name}': arr=${tc.arr}, target=${tc.target}, esperado ${tc.expected}, obtenido $result"
    )

  // flatMap + Option.when: comprobación adicional de que cada elemento de cada
  // arreglo se encuentra en su propio índice.
  val missedElements = testCases.flatMap(tc =>
    tc.arr.indices.flatMap(i =>
      Option.when(binarySearch(tc.arr, tc.arr(i)) != i)(s"Caso '${tc.name}': ${tc.arr(i)} no se halló en el índice $i")
    )
  )

  val failures = wrongResults ++ missedElements
  assert(failures.isEmpty, failures.mkString("\n"))
  println("Todas las pruebas pasaron.")