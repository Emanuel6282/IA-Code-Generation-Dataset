import scala.collection.mutable
import scala.util.boundary
import scala.util.boundary.break

/** Busca target en arr (ordenado ascendentemente) y retorna su índice, o -1 si no está.
  *
  * Context bound con nombre `A: Ordering as ord` (SIP-64, estable desde Scala 3.6):
  * la función sirve para cualquier tipo ordenable (Int, Long, String...) y da nombre
  * a la instancia de Ordering directamente en la firma, sin un `using` aparte. Con
  * un Array[Int], A se infiere como Int y se usa el Ordering[Int] estándar.
  *
  * scala.util.boundary / break (Scala 3.3+): sale del bucle en cuanto se encuentra
  * el objetivo, sin `return` ni variables bandera.
  */
def binarySearch[A: Ordering as ord](arr: Array[A], target: A): Int =
  boundary:
    var low = 0
    var high = arr.length - 1
    while low <= high do
      val mid = low + (high - low) / 2 // evita el desbordamiento de Int de (low + high) / 2
      val cmp = ord.compare(arr(mid), target)
      if cmp == 0 then break(mid)      // encontrado: sale del boundary con el índice
      else if cmp < 0 then low = mid + 1  // está en la mitad derecha
      else high = mid - 1                 // está en la mitad izquierda
    -1

/** Caso de prueba como case class. */
case class TestCase(name: String, arr: Array[Int], target: Int, expected: Int)

/** Punto de entrada con @main: sin object ni `def main(args: Array[String])`. */
@main def runTests(): Unit =
  val testCases = mutable.ListBuffer.empty[TestCase]
  // Caso 1: el objetivo está en el arreglo (ejemplo del enunciado)
  testCases += TestCase("objetivo presente", Array(1, 3, 5, 7, 9), 5, 2)
  // Caso 2: el objetivo no está en el arreglo (ejemplo del enunciado)
  testCases += TestCase("objetivo ausente", Array(1, 3, 5, 7, 9), 4, -1)
  // Caso 3: arreglo vacío
  testCases += TestCase("arreglo vacío", Array.empty[Int], 1, -1)

  var i = 0
  while i < testCases.length do
    val TestCase(name, arr, target, expected) = testCases(i)
    val result = binarySearch(arr, target)
    assert(
      result == expected,
      s"Caso '$name': arr=${arr.mkString("[", ", ", "]")}, target=$target, esperado $expected, obtenido $result"
    )
    i += 1

  println("Todas las pruebas pasaron.")