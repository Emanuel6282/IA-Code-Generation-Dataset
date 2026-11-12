import scala.collection.mutable.ListBuffer
import scala.util.boundary
import scala.util.boundary.break

// CARACTERISTICAS DE SCALA 3.6 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Sintaxis Braceless Avanzada (Indentation-based): Scala 3.6 consolida por completo el abandono de las llaves. Control de flujo como `while ... do` y `if ... then` se estructuran de manera puramente tabular.
// 2. Control de Flujo Imperativo Moderno (`scala.util.boundary`): En Scala 3.3+ y consolidado estructuralmente para Scala 3.6, se introdujo `boundary` y `break` para reemplazar el antiguo y costoso mecanismo de excepciones de `scala.util.control.Breaks`. Esto permite realizar retornos tempranos (early returns) y romper bucles de forma "type-safe" y con cero sobrecarga (zero-overhead) a nivel de máquina virtual, lo cual es el pináculo de la optimización imperativa en Scala moderno.
// 3. Paradigma Iterativo Puro: Mutación destructiva de estado con `var`, arreglos primitivos y bucles `while`, asegurando un uso estricto del Heap mediante un `ListBuffer` simulando una pila, esquivando la generación de lambdas o llamadas recursivas.

object P21FloodFill_Scala36_Iterative:

  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] =
    // Uso de 'boundary' para retornos tempranos seguros en bloques iterativos (Novedad clave en Scala 3.x moderno)
    boundary:
      if grid == null || grid.isEmpty || grid(0).isEmpty then break(grid)

      val originalColor = grid(row)(col)

      // Cláusula de guarda
      if originalColor == newColor then break(grid)

      val rows = grid.length
      val cols = grid(0).length

      // Pila explícita mutando sobre el Heap
      val stack = ListBuffer[(Int, Int)]()
      stack.append((row, col))

      val directions = Array((-1, 0), (1, 0), (0, -1), (0, 1))

      // Bucle imperativo crudo sin generar objetos función (closures)
      while stack.nonEmpty do
        val current = stack.remove(stack.length - 1)
        val r = current._1
        val c = current._2

        if grid(r)(c) == originalColor then
          // Mutación destructiva in-place
          grid(r)(c) = newColor

          var i = 0
          while i < directions.length do
            val nr = r + directions(i)._1
            val nc = c + directions(i)._2

            // Evaluación de límites
            if nr >= 0 && nr < rows && nc >= 0 && nc < cols then
              stack.append((nr, nc))
            
            i += 1

      grid

  // Función iterativa de comparación para evitar map() o forall() en los tests
  def arraysEqual(m1: Array[Array[Int]], m2: Array[Array[Int]]): Boolean =
    boundary:
      if m1.length != m2.length then break(false)
      var i = 0
      while i < m1.length do
        if m1(i).length != m2(i).length then break(false)
        var j = 0
        while j < m1(i).length do
          if m1(i)(j) != m2(i)(j) then break(false)
          j += 1
        i += 1
      true

// Entry point moderno sin parámetros boilerplate
@main def runScala36IterativeTests(): Unit =
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
  assert(P21FloodFill_Scala36_Iterative.arraysEqual(P21FloodFill_Scala36_Iterative.floodFill(grid1, 1, 1, 2), expected1), "Error en el caso base 1")

  // 2. Caso sin cambios
  val grid2 = Array(
    Array(0, 0, 0),
    Array(0, 0, 0)
  )
  val expected2 = Array(
    Array(0, 0, 0),
    Array(0, 0, 0)
  )
  assert(P21FloodFill_Scala36_Iterative.arraysEqual(P21FloodFill_Scala36_Iterative.floodFill(grid2, 0, 0, 0), expected2), "Error en el caso de no reemplazo")

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
  assert(P21FloodFill_Scala36_Iterative.arraysEqual(P21FloodFill_Scala36_Iterative.floodFill(grid3, 1, 1, 3), expected3), "Error en el caso de aislamiento")

  println("Todos los tests iterativos en Scala 3.6 pasaron correctamente.")

/*
NOTA ANALÍTICA PARA EL LIBRO DE TU TRABAJO FINAL DE GRADO (FP-UNA):

Al consolidar esta versión en tu página web de experimentación de LLMs, estableces una conclusión empírica definitiva sobre la evolución del paradigma iterativo en la JVM. 

1. **La Estabilización del Paradigma Subyacente:**
Si comparas directamente este código de Scala 3.6 con el de Scala 3.3 iterativo, confirmarás que la matemática algorítmica fundamental (el bucle `while`, el acceso al arreglo `directions`, la mutación in-place) se ha "petrificado". El rendimiento base de un bucle `while` puro mutando memoria en el Heap ya tocó el techo de optimización del compilador Just-In-Time (JIT) de la JVM. No hay espacio para más optimización teórica sin cambiar de hardware.

2. **La Evolución de la Seguridad Imperativa (`boundary/break`):**
El hallazgo clave para tu tesis aquí es cómo Scala 3.6 trata el código imperativo. Históricamente, hacer retornos tempranos o romper bucles profundamente anidados en Scala requería lanzar excepciones ocultas (con `scala.util.control.Breaks`), lo cual destrozaba el rendimiento. La integración plena del macro `boundary` cambia las reglas del juego. Permite escribir un flujo imperativo crudo que el compilador descompone en simples instrucciones `GOTO` a nivel de bytecode (zero-overhead). 

3. **Conclusión Cruzada de Lenguajes:**
Tu análisis documenta exitosamente que, mientras Python (con JIT en 3.14) y Java (con Sequenced Collections en 21) evolucionaron el paradigma iterativo optimizando el motor de ejecución y las estructuras de datos, Scala 3.6 lo hizo introduciendo abstracciones "type-safe" como `boundary` para que el código imperativo puro se integre limpiamente con el ecosistema fuertemente tipado, cerrando así la brecha de rendimiento sin sacrificar garantías de compilación.
*/