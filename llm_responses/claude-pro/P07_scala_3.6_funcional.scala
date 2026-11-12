import scala.annotation.tailrec

// Scala 3.6 — paradigma funcional (val, colecciones inmutables, map/filter/foldLeft/
// flatMap, @tailrec; sin var ni mutable.*).
//
// Característica nueva de Scala 3.6 que se usa:
// 1. Context bounds con nombre (SIP-64): `floodFill[A: Equiv as eq]` y
//    `neighbors[A: Equiv as eq]`. La función es genérica en el tipo del color (Int, Char,
//    String, un enum...) y compara colores con la instancia Equiv recibida, a la que el
//    `as` da nombre directamente en la lista de parámetros de tipo, sin un
//    `(using eq: Equiv[A])` aparte. Para Int (el caso del enunciado) la instancia la aporta
//    la biblioteca estándar y la llamada no cambia: floodFill(grid, 1, 1, 2).
// Se combinan con características de Scala 3 que siguen siendo idiomáticas: métodos de
// extensión (grid.colorAt, grid.paint, cell.move), `enum` con parámetros para las
// direcciones, alias de tipo genérico de nivel superior (Grid[A]), @main, lambdas con
// tuplas desempaquetadas y sintaxis sin llaves. boundary/break (3.3) no se usa: es
// control de flujo imperativo y el DFS con @tailrec termina de forma natural.
//
// Inmutabilidad: la matriz es un Vector[Vector[A]]; floodFill no la modifica, sino que
// retorna una nueva con `updated` (que comparte la estructura que no cambia).

type Grid[A] = Vector[Vector[A]]

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

extension [A](grid: Grid[A])
  /** Color de la celda, o None si no existe. `lift` no lanza excepciones con índices
    * negativos o fuera de rango y admite filas de distinto largo. */
  def colorAt(cell: Cell): Option[A] = grid.lift(cell.row).flatMap(_.lift(cell.col))

  /** Matriz nueva con `cell` pintada de `color` (Vector.updated comparte el resto). */
  def paint(cell: Cell, color: A): Grid[A] = grid.updated(cell.row, grid(cell.row).updated(cell.col, color))

private val Directions: List[Direction] = Direction.values.toList

/** Vecinos de `cell` que existen y todavía tienen el color `original` (según `eq`). */
def neighbors[A: Equiv as eq](grid: Grid[A], cell: Cell, original: A): List[Cell] =
  Directions.map(cell.move).filter(next => grid.colorAt(next).exists(eq.equiv(_, original)))

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
  * Los colores se comparan con la instancia `eq` de Equiv[A] (context bound con nombre,
  * Scala 3.6).
  *
  * @throws IndexOutOfBoundsException si (row, col) está fuera de la matriz
  */
def floodFill[A: Equiv as eq](grid: Grid[A], row: Int, col: Int, newColor: A): Grid[A] =
  val start = Cell(row, col)
  val original = grid.colorAt(start).getOrElse(
    throw IndexOutOfBoundsException(s"posición ($row, $col) fuera de la matriz"))

  @tailrec
  def loop(pending: List[Cell], current: Grid[A]): Grid[A] = pending match
    case Nil => current
    case cell :: rest =>
      val next = neighbors(current, cell, original)
      loop(next ::: rest, next.foldLeft(current)(_.paint(_, newColor)))

  if eq.equiv(original, newColor) then grid
  else loop(List(start), grid.paint(start, newColor))

/** Caso de prueba: case class inmutable. */
final case class TestCase(name: String, grid: Grid[Int], row: Int, col: Int, newColor: Int, expected: Grid[Int])

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

  // Genericidad (context bound con nombre, 3.6): la misma función con colores Char.
  val charsExpected = Vector(Vector('z', 'z', 'b'), Vector('b', 'z', 'z'))
  val charsResult = floodFill(Vector(Vector('a', 'a', 'b'), Vector('b', 'a', 'a')), 0, 0, 'z')
  val charFailure = Option.when(charsResult != charsExpected)(
    s"matriz de Char: esperado $charsExpected, obtenido $charsResult")

  val failures = wrongResults ++ boundsFailures ++ identityFailure ++ charFailure
  assert(failures.isEmpty, failures.mkString("\n"))
  println("Todas las pruebas pasaron.")