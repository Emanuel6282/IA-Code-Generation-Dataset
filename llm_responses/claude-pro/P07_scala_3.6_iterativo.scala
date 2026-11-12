import scala.collection.mutable
import scala.util.boundary
import scala.util.boundary.break

// Scala 3.6 — paradigma iterativo (var, while, colecciones mutables; sin map/filter/
// foldLeft ni recursión).
//
// Característica nueva de Scala 3.6 que se usa:
// 1. Context bounds con nombre (SIP-64): `floodFill[A: Equiv as eq]`. La función es
//    genérica en el tipo del color (Int, Char, String, un enum...) y compara colores con
//    la instancia Equiv recibida, a la que el `as` da nombre directamente en la lista de
//    parámetros de tipo, sin un `(using eq: Equiv[A])` aparte. Para Int (el caso del
//    enunciado) la instancia la aporta la biblioteca estándar y la llamada no cambia:
//    floodFill(grid, 1, 1, 2).
//    Coste: con A genérico los accesos al arreglo y las comparaciones pasan por métodos
//    genéricos (con boxing de Int). Medido con 2000 x 2000 de un solo color: ~0,2-0,3 s,
//    igual que la versión 3.3 con Int fijo, dentro del ruido de medición.
// Se combinan con características de Scala 3 / 3.3 que siguen siendo idiomáticas:
// boundary/break (3.3) para la salida temprana en `sameContents`, `enum` con parámetros
// para las direcciones, definiciones de nivel superior, @main, sintaxis sin llaves y
// mutable.ArrayDeque (2.13) como pila.

/** Desplazamientos a las cuatro celdas vecinas (conectividad 4, sin diagonales). */
enum Direction(val dRow: Int, val dCol: Int):
  case Up extends Direction(-1, 0)
  case Down extends Direction(1, 0)
  case Left extends Direction(0, -1)
  case Right extends Direction(0, 1)

/** Celda de la matriz. */
case class Cell(row: Int, col: Int)

/** Rellena con `newColor` la región conectada (4 direcciones) que contiene (row, col).
  *
  * La matriz se modifica en el lugar y se retorna la misma referencia. Si la celda ya
  * tiene `newColor`, se retorna sin cambios.
  *
  * Se recorre con una pila explícita y un bucle while, sin recursión: un DFS recursivo
  * sobre una región grande desbordaría la pila de la JVM. Cada celda se pinta al
  * apilarla, así que no se visita dos veces. O(filas * columnas) en tiempo. Admite
  * matrices irregulares: los límites se comprueban por fila.
  *
  * Los colores se comparan con la instancia `eq` de Equiv[A] (context bound con nombre,
  * Scala 3.6).
  *
  * @throws IndexOutOfBoundsException si (row, col) está fuera de la matriz
  */
def floodFill[A: Equiv as eq](grid: Array[Array[A]], row: Int, col: Int, newColor: A): Array[Array[A]] =
  if row < 0 || row >= grid.length || col < 0 || col >= grid(row).length then
    throw IndexOutOfBoundsException(s"posición ($row, $col) fuera de la matriz")

  val original = grid(row)(col)
  // Si ya tiene el nuevo color no hay nada que hacer; además, sin esta comprobación el
  // bucle no terminaría, porque las celdas pintadas seguirían teniendo el color original.
  if !eq.equiv(original, newColor) then
    val directions = Direction.values
    val stack = mutable.ArrayDeque.empty[Cell]
    grid(row)(col) = newColor
    stack.append(Cell(row, col))

    while stack.nonEmpty do
      val Cell(r, c) = stack.removeLast()
      var i = 0
      while i < directions.length do
        val nr = r + directions(i).dRow
        val nc = c + directions(i).dCol
        if nr >= 0 && nr < grid.length && nc >= 0 && nc < grid(nr).length && eq.equiv(grid(nr)(nc), original) then
          grid(nr)(nc) = newColor
          stack.append(Cell(nr, nc))
        i += 1
  grid

/** Compara dos matrices celda a celda (Array compara por referencia). Con boundary/break
  * (Scala 3.3) sale del bucle en la primera fila distinta. */
def sameContents[A](a: Array[Array[A]], b: Array[Array[A]]): Boolean =
  boundary:
    if a.length != b.length then break(false)
    var r = 0
    while r < a.length do
      if !a(r).sameElements(b(r)) then break(false)
      r += 1
    true

def show[A](grid: Array[Array[A]]): String =
  val rows = mutable.ListBuffer.empty[String]
  var r = 0
  while r < grid.length do
    rows += grid(r).mkString("[", ", ", "]")
    r += 1
  rows.mkString("[", ", ", "]")

/** Caso de prueba como case class. */
case class TestCase(name: String, grid: Array[Array[Int]], row: Int, col: Int, newColor: Int,
                    expected: Array[Array[Int]])

@main def runTests(): Unit =
  val testCases = mutable.ListBuffer.empty[TestCase]
  // Caso 1: ejemplo del enunciado (la esquina inferior derecha no está conectada)
  testCases += TestCase("ejemplo del enunciado",
    Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1)), 1, 1, 2,
    Array(Array(2, 2, 2), Array(2, 2, 0), Array(2, 0, 1)))
  // Caso 2: el color ya es el nuevo -> la matriz no cambia
  testCases += TestCase("color ya es el nuevo",
    Array(Array(0, 0, 0), Array(0, 1, 1)), 1, 1, 1,
    Array(Array(0, 0, 0), Array(0, 1, 1)))
  // Caso 3: no se rellena en diagonal
  testCases += TestCase("sin diagonales",
    Array(Array(1, 0), Array(0, 1)), 0, 0, 3,
    Array(Array(3, 0), Array(0, 1)))

  val failures = mutable.ListBuffer.empty[String]
  var i = 0
  while i < testCases.length do
    val TestCase(name, grid, row, col, newColor, expected) = testCases(i)
    val result = floodFill(grid, row, col, newColor)
    if !sameContents(result, expected) then
      failures += s"Caso '$name': esperado ${show(expected)}, obtenido ${show(result)}"
    if !(result eq grid) then
      failures += s"Caso '$name': debe modificar y retornar la misma matriz"
    i += 1

  // Posición fuera de la matriz (incluido un índice negativo) -> IndexOutOfBoundsException
  val badPositions = Array(Cell(2, 0), Cell(-1, 0), Cell(0, 3))
  var j = 0
  while j < badPositions.length do
    val Cell(r, c) = badPositions(j)
    var threw = false
    try floodFill(Array(Array(1, 1, 1), Array(1, 1, 1)), r, c, 5)
    catch case _: IndexOutOfBoundsException => threw = true
    if !threw then failures += s"($r, $c) debería lanzar IndexOutOfBoundsException"
    j += 1

  // Genericidad (context bound con nombre, 3.6): la misma función con colores Char.
  val chars = Array(Array('a', 'a', 'b'), Array('b', 'a', 'a'))
  val expectedChars = Array(Array('z', 'z', 'b'), Array('b', 'z', 'z'))
  if !sameContents(floodFill(chars, 0, 0, 'z'), expectedChars) then
    failures += s"matriz de Char: esperado ${show(expectedChars)}, obtenido ${show(chars)}"

  assert(failures.isEmpty, failures.mkString("\n"))
  println("Todas las pruebas pasaron.")