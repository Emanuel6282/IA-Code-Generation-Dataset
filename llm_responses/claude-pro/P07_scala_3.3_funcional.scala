import scala.annotation.tailrec

// Scala 3.3 — paradigma funcional (val, colecciones inmutables, map/filter/foldLeft/
// flatMap, @tailrec; sin var ni mutable.*).
//
// Características de Scala 3 que se usan:
// 1. Métodos de extensión (`extension (grid: Grid)`): colorAt y paint se leen como
//    operaciones de la matriz (`grid.colorAt(cell)`, `grid.paint(cell, color)`) sin
//    clases implícitas.
// 2. `enum` con parámetros: las cuatro direcciones son un tipo propio con nombre (Up,
//    Down, Left, Right); Direction.values.toList las recorre.
// 3. Alias de tipo de nivel superior, definiciones de nivel superior y @main: sin
//    `object` contenedor ni `def main(args: Array[String])`.
// 4. Lambdas que reciben tuplas desempaquetadas (`(name, expected, result) => …`, sin
//    `case`) y sintaxis sin llaves.
// boundary/break (nuevo en 3.3) no se usa: es control de flujo imperativo y el DFS con
// @tailrec ya termina de forma natural cuando no quedan celdas pendientes.
//
// Inmutabilidad: la matriz es un Vector[Vector[Int]]; floodFill no la modifica, sino que
// retorna una nueva con `updated` (que comparte la estructura que no cambia).

type Grid = Vector[Vector[Int]]

/** Celda de la matriz. */
final case class Cell(row: Int, col: Int)

/** Desplazamientos a las cuatro celdas vecinas (conectividad 4, sin diagonales). */
enum Direction(val dRow: Int, val dCol: Int):
  case Up extends Direction(-1, 0)
  case Down extends Direction(1, 0)
  case Left extends Direction(0, -1)
  case Right extends Direction(0, 1)

extension (cell: Cell)
  def move(direction: Direction): Cell = Cell(cell.row + direction.dRow, cell.col + direction.dCol)

extension (grid: Grid)
  /** Color de la celda, o None si no existe. `lift` no lanza excepciones con índices
    * negativos o fuera de rango y admite filas de distinto largo. */
  def colorAt(cell: Cell): Option[Int] = grid.lift(cell.row).flatMap(_.lift(cell.col))

  /** Matriz nueva con `cell` pintada de `color` (Vector.updated comparte el resto). */
  def paint(cell: Cell, color: Int): Grid = grid.updated(cell.row, grid(cell.row).updated(cell.col, color))

private val Directions: List[Direction] = Direction.values.toList

/** Vecinos de `cell` que existen y todavía tienen el color `original`. */
def neighbors(grid: Grid, cell: Cell, original: Int): List[Cell] =
  Directions.map(cell.move).filter(next => grid.colorAt(next).contains(original))

/** Retorna una matriz nueva en la que la región conectada (4 direcciones) que contiene
  * (row, col) pasa a `newColor`. Si (row, col) ya tiene `newColor`, retorna la misma
  * matriz, sin cambios.
  *
  * DFS con @tailrec: la pila de celdas pendientes es una List inmutable que se pasa como
  * parámetro, junto con la matriz parcialmente pintada. Cada celda se pinta al apilarla,
  * así que deja de tener el color original y no se visita dos veces. Al ser recursión de
  * cola, el compilador la convierte en un bucle: no consume pila de la JVM aunque la
  * región sea enorme. O(filas * columnas) actualizaciones de Vector.
  *
  * @throws IndexOutOfBoundsException si (row, col) está fuera de la matriz
  */
def floodFill(grid: Grid, row: Int, col: Int, newColor: Int): Grid =
  val start = Cell(row, col)
  val original = grid.colorAt(start).getOrElse(
    throw IndexOutOfBoundsException(s"posición ($row, $col) fuera de la matriz"))

  @tailrec
  def loop(pending: List[Cell], current: Grid): Grid = pending match
    case Nil => current
    case cell :: rest =>
      val next = neighbors(current, cell, original)
      loop(next ::: rest, next.foldLeft(current)(_.paint(_, newColor)))

  if original == newColor then grid
  else loop(List(start), grid.paint(start, newColor))

/** Caso de prueba: case class inmutable. */
final case class TestCase(name: String, grid: Grid, row: Int, col: Int, newColor: Int, expected: Grid)

@main def runTests(): Unit =
  val testCases = List(
    // Caso 1: ejemplo del enunciado (la esquina inferior derecha no está conectada)
    TestCase("ejemplo del enunciado",
      Vector(Vector(1, 1, 1), Vector(1, 1, 0), Vector(1, 0, 1)), 1, 1, 2,
      Vector(Vector(2, 2, 2), Vector(2, 2, 0), Vector(2, 0, 1))),
    // Caso 2: el color ya es el nuevo -> la matriz no cambia
    TestCase("color ya es el nuevo",
      Vector(Vector(0, 0, 0), Vector(0, 1, 1)), 1, 1, 1,
      Vector(Vector(0, 0, 0), Vector(0, 1, 1))),
    // Caso 3: no se rellena en diagonal
    TestCase("sin diagonales",
      Vector(Vector(1, 0), Vector(0, 1)), 0, 0, 3,
      Vector(Vector(3, 0), Vector(0, 1)))
  )

  // map + filter + map: calcula cada resultado, conserva los incorrectos y los convierte
  // en mensajes. Las lambdas reciben la tupla desempaquetada (Scala 3).
  val wrongResults = testCases
    .map(tc => (tc.name, tc.expected, floodFill(tc.grid, tc.row, tc.col, tc.newColor)))
    .filter((_, expected, result) => result != expected)
    .map((name, expected, result) => s"Caso '$name': esperado $expected, obtenido $result")

  // Posición fuera de la matriz (incluido un índice negativo) -> IndexOutOfBoundsException.
  // flatMap + Option.when: un mensaje solo si no se lanzó la excepción esperada.
  val boundsFailures = List(Cell(2, 0), Cell(-1, 0), Cell(0, 3)).flatMap(cell =>
    Option.when(
      !scala.util.Try(floodFill(Vector(Vector(1, 1, 1), Vector(1, 1, 1)), cell.row, cell.col, 5))
        .failed.toOption.exists(_.isInstanceOf[IndexOutOfBoundsException])
    )(s"$cell debería lanzar IndexOutOfBoundsException"))

  // Si el color ya es el nuevo, se retorna la misma instancia (sin copiar).
  val sameColorGrid = Vector(Vector(5))
  val identityFailure = Option.when(!(floodFill(sameColorGrid, 0, 0, 5) eq sameColorGrid))(
    "con el mismo color debe retornarse la misma matriz")

  val failures = wrongResults ++ boundsFailures ++ identityFailure
  assert(failures.isEmpty, failures.mkString("\n"))
  println("Todas las pruebas pasaron.")