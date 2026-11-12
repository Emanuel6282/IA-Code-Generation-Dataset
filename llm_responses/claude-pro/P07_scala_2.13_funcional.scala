import scala.annotation.tailrec

// Scala 2.13 — paradigma funcional (val, colecciones inmutables, map/filter/foldLeft/
// flatMap, @tailrec; sin var ni mutable.*).
//
// Características nuevas de Scala 2.13 que se usan:
// 1. Option.when (2.13): `Option.when(condición)(valor)` produce Some(valor) o None. En
//    `neighbors` convierte cada vecino en Some solo si tiene el color original, y
//    flatMap descarta los None; en los tests produce el mensaje de fallo solo si falla.
// 2. Colecciones inmutables rediseñadas en 2.13: Vector.updated y `lift` (acceso seguro
//    que devuelve Option en vez de lanzar) sobre la jerarquía nueva de Seq inmutables.
//
// Inmutabilidad: la matriz es un Vector[Vector[Int]]; floodFill no la modifica, sino que
// retorna una nueva con `updated` (que comparte la estructura que no cambia). La
// entrada, al ser inmutable, no puede verse alterada.
object FloodFill {

  type Grid = Vector[Vector[Int]]
  type Cell = (Int, Int)

  /** Desplazamientos arriba, abajo, izquierda, derecha (conectividad 4, sin diagonales). */
  private val Directions: List[Cell] = List((-1, 0), (1, 0), (0, -1), (0, 1))

  /** Color de la celda, o None si no existe. `lift` no lanza excepciones con índices
    * negativos o fuera de rango y admite filas de distinto largo. */
  private def colorAt(grid: Grid, cell: Cell): Option[Int] =
    grid.lift(cell._1).flatMap(_.lift(cell._2))

  private def paint(grid: Grid, cell: Cell, color: Int): Grid =
    grid.updated(cell._1, grid(cell._1).updated(cell._2, color))

  /** Vecinos de `cell` que existen y todavía tienen el color `original`. */
  private def neighbors(grid: Grid, cell: Cell, original: Int): List[Cell] =
    Directions.flatMap { case (dr, dc) =>
      val next = (cell._1 + dr, cell._2 + dc)
      Option.when(colorAt(grid, next).contains(original))(next)
    }

  /** Retorna una matriz nueva en la que la región conectada (4 direcciones) que contiene
    * (row, col) pasa a `newColor`. Si (row, col) ya tiene `newColor`, retorna la misma
    * matriz, sin cambios.
    *
    * DFS con @tailrec: la pila de celdas pendientes es una List inmutable que se pasa como
    * parámetro, y la matriz parcialmente pintada es el otro parámetro. Cada celda se pinta
    * al apilarla, así que deja de tener el color original y no se visita dos veces. Al
    * ser recursión de cola, el compilador la convierte en un bucle: no consume pila de
    * la JVM aunque la región sea enorme. O(filas * columnas) actualizaciones de Vector.
    *
    * @throws IndexOutOfBoundsException si (row, col) está fuera de la matriz
    */
  def floodFill(grid: Grid, row: Int, col: Int, newColor: Int): Grid = {
    val start = (row, col)
    val original = colorAt(grid, start).getOrElse(
      throw new IndexOutOfBoundsException(s"posición ($row, $col) fuera de la matriz"))

    @tailrec
    def loop(pending: List[Cell], current: Grid): Grid = pending match {
      case Nil => current
      case cell :: rest =>
        val next = neighbors(current, cell, original)
        loop(next ::: rest, next.foldLeft(current)(paint(_, _, newColor)))
    }

    if (original == newColor) grid
    else loop(List(start), paint(grid, start, newColor))
  }

  def main(args: Array[String]): Unit = {
    val testCases: List[(String, Grid, Int, Int, Int, Grid)] = List(
      // Caso 1: ejemplo del enunciado (la esquina inferior derecha no está conectada)
      ("ejemplo del enunciado",
        Vector(Vector(1, 1, 1), Vector(1, 1, 0), Vector(1, 0, 1)), 1, 1, 2,
        Vector(Vector(2, 2, 2), Vector(2, 2, 0), Vector(2, 0, 1))),
      // Caso 2: el color ya es el nuevo -> la matriz no cambia (y es la misma)
      ("color ya es el nuevo",
        Vector(Vector(0, 0, 0), Vector(0, 1, 1)), 1, 1, 1,
        Vector(Vector(0, 0, 0), Vector(0, 1, 1))),
      // Caso 3: no se rellena en diagonal
      ("sin diagonales",
        Vector(Vector(1, 0), Vector(0, 1)), 0, 0, 3,
        Vector(Vector(3, 0), Vector(0, 1)))
    )

    // map + filter + map: calcula cada resultado, conserva los incorrectos y los
    // convierte en mensajes.
    val wrongResults: List[String] = testCases
      .map { case (name, grid, row, col, newColor, expected) =>
        (name, expected, floodFill(grid, row, col, newColor)) }
      .filter { case (_, expected, result) => result != expected }
      .map { case (name, expected, result) => s"Caso '$name': esperado $expected, obtenido $result" }

    // Posición fuera de la matriz (incluido un índice negativo) -> IndexOutOfBoundsException.
    // Option.when produce el mensaje solo si no se lanzó la excepción esperada.
    val boundsFailures: List[String] = List((2, 0), (-1, 0), (0, 3)).flatMap { case (r, c) =>
      Option.when(
        !scala.util.Try(floodFill(Vector(Vector(1, 1, 1), Vector(1, 1, 1)), r, c, 5))
          .failed.toOption.exists(_.isInstanceOf[IndexOutOfBoundsException])
      )(s"($r, $c) debería lanzar IndexOutOfBoundsException")
    }

    // Si el color ya es el nuevo, se retorna la misma instancia (sin copiar).
    val sameColorGrid = Vector(Vector(5))
    val identityFailure = Option.when(!(floodFill(sameColorGrid, 0, 0, 5) eq sameColorGrid))(
      "con el mismo color debe retornarse la misma matriz")

    val failures = wrongResults ++ boundsFailures ++ identityFailure
    assert(failures.isEmpty, failures.mkString("\n"))
    println("Todas las pruebas pasaron.")
  }
}