// Scala 3.6 — paradigma funcional (val, colecciones inmutables, foldLeft, map, filter,
// flatMap, @tailrec; sin var ni mutable.*).
//
// Características de Scala 3 que se usan: definiciones de nivel superior (sin `object`),
// @main, sintaxis sin llaves, case class con métodos y objeto compañero, y lambdas que
// reciben tuplas desempaquetadas (`(tc, result) => …`, sin `case`).
//
// Novedades propias de 3.6: la principal es SIP-64 (context bounds con nombre,
// `[A: Ordering as ord]`, y la nueva sintaxis de `given`). Aquí no aplica: la función
// trabaja con Int y acumula en Long, así que no hay ningún parámetro de tipo ni
// instancia implícita que nombrar. Las tuplas con nombre siguen siendo experimentales
// en 3.6 (requieren `import scala.language.experimental.namedTuples`), así que no se
// usan. Tampoco se usa boundary/break: Kadane debe recorrer todo el arreglo, no hay
// salida temprana. Antes que forzar una característica que no aporta nada, el código
// queda como Scala 3 idiomático, que compila igual en 3.6.

import scala.annotation.tailrec

/** Estado de Kadane: mejor suma que termina en la posición actual y mejor suma global. */
final case class State(current: Long, best: Long):
  /** Un paso de Kadane: devuelve un estado nuevo, sin modificar el actual. */
  def next(x: Int): State =
    val extended = math.max(x.toLong, current + x)   // extender o empezar de nuevo en x
    State(extended, math.max(best, extended))

object State:
  /** Estado inicial a partir del primer elemento (no de 0): sin subarreglo vacío. */
  def start(first: Int): State = State(first.toLong, first.toLong)

/** Retorna la suma máxima de un subarreglo contiguo no vacío de `nums`.
  *
  * Algoritmo de Kadane como un foldLeft sobre un estado inmutable. El estado inicial es
  * el primer elemento, así que nunca se considera el subarreglo vacío y, si todos son
  * negativos, el resultado es el menos negativo. O(n) en tiempo.
  *
  * Las sumas se acumulan en Long: la suma de varios Int puede superar el rango de Int
  * (por ejemplo, Int.MaxValue + Int.MaxValue) y con Int se desbordaría en silencio.
  *
  * @throws IllegalArgumentException si `nums` está vacío
  */
def maxSubarraySum(nums: Seq[Int]): Long = nums match
  case first +: rest => rest.foldLeft(State.start(first))(_.next(_)).best
  case _             => throw IllegalArgumentException("el arreglo debe tener al menos un elemento")

/** Referencia 1, con @tailrec: Kadane pasando el estado como parámetro de cada llamada. */
def byTailrec(nums: Seq[Int]): Long =
  @tailrec
  def loop(rest: List[Int], state: State): State = rest match
    case Nil       => state
    case x :: tail => loop(tail, state.next(x))
  loop(nums.toList.tail, State.start(nums.head)).best

/** Referencia 2, por fuerza bruta con flatMap + map: suma de todos los subarreglos. */
def byBruteForce(nums: Seq[Int]): Long =
  nums.indices.flatMap(i => (i + 1 to nums.length).map(j => nums.slice(i, j).map(_.toLong).sum)).max

/** Caso de prueba: case class inmutable. */
case class TestCase(name: String, nums: Vector[Int], expected: Long)

/** Punto de entrada con @main (Scala 3): sin object ni `def main(args: Array[String])`. */
@main def runTests(): Unit =
  val testCases = List(
    // Caso 1: ejemplo del enunciado ([4, -1, 2, 1])
    TestCase("ejemplo del enunciado", Vector(-2, 1, -3, 4, -1, 2, 1, -5, 4), 6L),
    // Caso 2: todos negativos -> el menos negativo
    TestCase("todos negativos", Vector(-3, -1, -2), -1L),
    TestCase("un solo elemento negativo", Vector(-5), -5L),
    // Caso 3: suma que supera el rango de Int (con Int se desbordaría a -2)
    TestCase("suma mayor que Int", Vector(Int.MaxValue, Int.MaxValue), 2L * Int.MaxValue)
  )

  // map + filter + map: calcula cada resultado, conserva los incorrectos (frente al
  // valor esperado o a cualquiera de las dos referencias) y los convierte en mensajes.
  // Las lambdas reciben la tupla desempaquetada (Scala 3).
  val wrongResults = testCases
    .map(tc => (tc, maxSubarraySum(tc.nums)))
    .filter((tc, result) => result != tc.expected || result != byTailrec(tc.nums) || result != byBruteForce(tc.nums))
    .map((tc, result) => s"Caso '${tc.name}' ${tc.nums}: esperado ${tc.expected}, obtenido $result")

  // Arreglo vacío -> IllegalArgumentException; Option.when produce el mensaje de fallo
  // solo si no se lanzó la excepción esperada.
  val emptyFailure = Option.when(
    !scala.util.Try(maxSubarraySum(Vector.empty)).failed.toOption.exists(_.isInstanceOf[IllegalArgumentException])
  )("un arreglo vacío debería lanzar IllegalArgumentException")

  val failures = wrongResults ++ emptyFailure
  assert(failures.isEmpty, failures.mkString("\n"))
  println("Todas las pruebas pasaron.")