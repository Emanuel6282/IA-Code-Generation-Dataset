import scala.annotation.tailrec

object MaxSubarray {

  /** Estado de Kadane: mejor suma que termina en la posición actual y mejor suma global. */
  private final case class State(current: Long, best: Long)

  /** Un paso de Kadane: recibe un estado y un elemento y devuelve un estado nuevo. */
  private def step(state: State, x: Int): State = {
    val current = math.max(x.toLong, state.current + x)   // extender o empezar de nuevo en x
    State(current, math.max(state.best, current))
  }

  /** Retorna la suma máxima de un subarreglo contiguo no vacío de `nums`.
    *
    * Algoritmo de Kadane como un foldLeft sobre un estado inmutable. El estado inicial
    * es el primer elemento (no 0), así que nunca se considera el subarreglo vacío y, si
    * todos son negativos, el resultado es el menos negativo. O(n) en tiempo.
    *
    * Las sumas se acumulan en Long: la suma de varios Int puede superar el rango de Int
    * (por ejemplo, Int.MaxValue + Int.MaxValue) y con Int se desbordaría en silencio.
    *
    * @throws IllegalArgumentException si `nums` está vacío
    */
  def maxSubarraySum(nums: Seq[Int]): Long = nums match {
    case first +: rest => rest.foldLeft(State(first.toLong, first.toLong))(step).best
    case _             => throw new IllegalArgumentException("el arreglo debe tener al menos un elemento")
  }

  def main(args: Array[String]): Unit = {
    val testCases: List[(String, Vector[Int], Long)] = List(
      // Caso 1: ejemplo del enunciado ([4, -1, 2, 1])
      ("ejemplo del enunciado", Vector(-2, 1, -3, 4, -1, 2, 1, -5, 4), 6L),
      // Caso 2: todos negativos -> el menos negativo
      ("todos negativos", Vector(-3, -1, -2), -1L),
      ("un solo elemento negativo", Vector(-5), -5L),
      // Caso 3: suma que supera el rango de Int (con Int se desbordaría a -2)
      ("suma mayor que Int", Vector(Int.MaxValue, Int.MaxValue), 2L * Int.MaxValue)
    )

    // Referencia 1, con @tailrec: Kadane recorriendo la lista y pasando el estado como
    // parámetro. El compilador garantiza que la recursión es de cola y no consume pila.
    def byTailrec(nums: Seq[Int]): Long = {
      @tailrec
      def loop(rest: List[Int], state: State): State = rest match {
        case Nil       => state
        case x :: tail => loop(tail, step(state, x))
      }
      loop(nums.toList.tail, State(nums.head.toLong, nums.head.toLong)).best
    }

    // Referencia 2, por fuerza bruta con flatMap + map: la suma de todos los
    // subarreglos no vacíos posibles, de la que se toma la máxima.
    def byBruteForce(nums: Seq[Int]): Long =
      nums.indices.flatMap(i => (i + 1 to nums.length).map(j => nums.slice(i, j).map(_.toLong).sum)).max

    // map + filter + map: calcula cada resultado, conserva los incorrectos (frente al
    // valor esperado o a cualquiera de las dos referencias) y los convierte en mensajes.
    val wrongResults: List[String] = testCases
      .map { case (name, nums, expected) => (name, nums, expected, maxSubarraySum(nums)) }
      .filter { case (_, nums, expected, result) =>
        result != expected || result != byTailrec(nums) || result != byBruteForce(nums)
      }
      .map { case (name, nums, expected, result) =>
        s"Caso '$name' $nums: esperado $expected, obtenido $result"
      }

    // Arreglo vacío -> IllegalArgumentException. Option.when (nuevo en Scala 2.13)
    // produce el mensaje de fallo solo si no se lanzó la excepción esperada.
    val emptyFailure: Option[String] = Option.when(
      !scala.util.Try(maxSubarraySum(Vector.empty)).failed.toOption.exists(_.isInstanceOf[IllegalArgumentException])
    )("un arreglo vacío debería lanzar IllegalArgumentException")

    val failures = wrongResults ++ emptyFailure
    assert(failures.isEmpty, failures.mkString("\n"))
    println("Todas las pruebas pasaron.")
  }
}