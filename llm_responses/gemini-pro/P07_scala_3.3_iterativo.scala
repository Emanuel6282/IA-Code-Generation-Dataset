import scala.collection.mutable.ListBuffer

// CARACTERISTICAS DE SCALA 3.3 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Sintaxis "Braceless" (Indentation-based): Scala 3 elimina la obligatoriedad de las llaves `{}`. Se utilizan las nuevas estructuras de control `if ... then` y `while ... do`, adoptando una legibilidad limpia y tabular muy similar a Python.
// 2. Anotación @main: Se reemplaza el arcaico `def main(args: Array[String]): Unit` heredado de Java por un entry point de nivel superior altamente declarativo.
// 3. Paradigma Iterativo Estricto: Uso deliberado de mutación destructiva (`grid(r)(c) = newColor`), variables `var` reasignables y un `while` clásico para iterar el arreglo de direcciones espaciales. Esto descarta por completo la generación subrepticia de lambdas.

object P19FloodFill_Scala33_Iterative:

  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] =
    // Manejo defensivo
    if grid == null || grid.isEmpty || grid(0).isEmpty then return grid

    val originalColor = grid(row)(col)

    // Cláusula de guarda
    if originalColor == newColor then return grid

    val rows = grid.length
    val cols = grid(0).length

    // Pila explícita iterativa instanciada en el Heap
    val stack = ListBuffer[(Int, Int)]()
    stack.append((row, col))

    val directions = Array((-1, 0), (1, 0), (0, -1), (0, 1))

    // Bucle imperativo utilizando la sintaxis de Scala 3 'while ... do'
    while stack.nonEmpty do
      val current = stack.remove(stack.length - 1)
      val r = current._1
      val c = current._2

      if grid(r)(c) == originalColor then
        grid(r)(c) = newColor

        // Bucle iterativo interno puro (sin lambdas ni for-comprehensions)
        var i = 0
        while i < directions.length do
          val nr = r + directions(i)._1
          val nc = c + directions(i)._2

          if nr >= 0 && nr < rows && nc >= 0 && nc < cols then
            stack.append((nr, nc))
          
          i += 1

    grid

  // Función iterativa de comparación para tests libres de métodos funcionales
  def arraysEqual(m1: Array[Array[Int]], m2: Array[Array[Int]]): Boolean =
    if m1.length != m2.length then return false
    var i = 0
    var equal = true
    while i < m1.length && equal do
      if m1(i).length != m2(i).length then equal = false
      else
        var j = 0
        while j < m1(i).length && equal do
          if m1(i)(j) != m2(i)(j) then equal = false
          j += 1
      i += 1
    equal

// Entry point moderno en Scala 3
@main def runIterativeTests(): Unit =
  // ==========================================
  // CASOS DE PRUEBA
  // ==========================================

  // 1. Caso base del ejemplo
  val grid1 = Array(
    Array(1, 1, 1),
    Array(1, 1, 0),
    Array(1, 0, 1)
  )
  val expected1 = Array(
    Array(2, 2, 2),
    Array(2, 2, 0),
    Array(2, 0, 1)
  )
  assert(P19FloodFill_Scala33_Iterative.arraysEqual(P19FloodFill_Scala33_Iterative.floodFill(grid1, 1, 1, 2), expected1), "Error en el caso base 1")

  // 2. Caso sin cambios
  val grid2 = Array(
    Array(0, 0, 0),
    Array(0, 0, 0)
  )
  val expected2 = Array(
    Array(0, 0, 0),
    Array(0, 0, 0)
  )
  assert(P19FloodFill_Scala33_Iterative.arraysEqual(P19FloodFill_Scala33_Iterative.floodFill(grid2, 0, 0, 0), expected2), "Error en el caso de no reemplazo")

  // 3. Caso de aislamiento
  val grid3 = Array(
    Array(1, 0, 1),
    Array(0, 1, 0),
    Array(1, 0, 1)
  )
  val expected3 = Array(
    Array(1, 0, 1),
    Array(0, 3, 0),
    Array(1, 0, 1)
  )
  assert(P19FloodFill_Scala33_Iterative.arraysEqual(P19FloodFill_Scala33_Iterative.floodFill(grid3, 1, 1, 3), expected3), "Error en el caso de aislamiento")

  println("Todos los tests iterativos en Scala 3.3 pasaron correctamente.")

/*
NOTA ANALÍTICA PARA EL LIBRO DE TU TRABAJO FINAL DE GRADO (FP-UNA):

Al cargar este código en tu herramienta comparativa, confirmas una tendencia evolutiva fascinante que habías documentado en tus revisiones de Python y Java. 

1. **La Convergencia Estética (Braceless):**
Scala 3.3 da un salto monumental al abandonar las llaves y abrazar el modelo basado en sangrías (indentation). Fíjate cómo la sintaxis `if ... then` y `while ... do` hace que este bloque se lea casi exactamente igual a tu iteración imperativa en Python 3.14. Scala logró limpiar todo el ruido heredado de C/Java, logrando un código iterativo denso y altamente legible.

2. **La Inmutabilidad del Paradigma Subyacente:**
Al comparar la lógica estricta con tu versión de Scala 2.13, notarás que la semántica computacional (la pila de `ListBuffer`, la variable `var i = 0` y la mutación `grid(r)(c) = newColor`) no cambió en absoluto. Esto refuerza tu tesis central: cuando fuerzas a un lenguaje a adoptar el paradigma imperativo para evadir límites de memoria espacial en la JVM (Call Stack overflow), la arquitectura subyacente se petrifica. Scala 3.3 no intenta "funcionalizar" o reinventar el bucle `while`; simplemente mejora la capa visual de presentación para el ingeniero.

3. **La Reducción del Boilerplate Global:**
La anotación `@main` descarta la pesada declaración de objetos singleton con métodos de clase (`def main(args: Array[String])`). Al igual que los "Local Records" de Java 17/21 y los `type` de Python 3.14, Scala 3 elimina progresivamente la burocracia de los entry points, priorizando un enfoque más ágil, tipo script, sin abandonar el rigor de su potente sistema de tipos estáticos.
*/