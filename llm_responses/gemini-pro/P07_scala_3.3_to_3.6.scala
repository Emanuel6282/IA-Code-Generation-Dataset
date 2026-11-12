import scala.collection.mutable
import scala.util.boundary
import scala.util.boundary.break

// CARACTERISTICAS DE SCALA 3.6 UTILIZADAS Y POR QUE:
// 1. Control de Flujo Seguro (`boundary` y `break`): En Scala 3, el uso explícito de la palabra clave `return` se considera un antipatrón severo (y propenso a errores en cierres funcionales). Scala 3.6 consolida el uso de `scala.util.boundary` para retornos tempranos (early returns) "type-safe", logrando que la cláusula de guarda aborte la ejecución de manera limpia y sin sobrecarga de excepciones (zero-overhead).
// 2. Sintaxis Braceless (Indentation-based): Se eliminan las llaves `{}`. Las estructuras imperativas adoptan un diseño puramente tabular (`if ... then`, `while ... do`, `for ... do`), eliminando el ruido visual histórico de la JVM.
// 3. Entry point declarativo `@main`: Se elimina el objeto contenedor y el método estático heredado de Java `def main(args: Array[String]): Unit`, reemplazándolo por una simple función anotada de nivel superior.

object P07FloodFill_v36:

  /**
   * Aplica flood fill 4-direccional sobre grid a partir de (row, col).
   * Modifica y retorna el grid original de manera iterativa (BFS).
   */
  def floodFill(grid: Array[Array[Int]], row: Int, col: Int, newColor: Int): Array[Array[Int]] =
    // Bloque boundary para habilitar retornos tempranos seguros en Scala 3.6
    boundary:
      val originalColor = grid(row)(col)
      
      // Cláusula de guarda idiomática sin usar 'return'
      if originalColor == newColor then break(grid)

      val rows = grid.length
      val cols = grid(0).length
      
      // Instanciación limpia de la cola mutable
      val queue = mutable.Queue[(Int, Int)]()
      queue.enqueue((row, col))
      grid(row)(col) = newColor

      val directions = List((-1, 0), (1, 0), (0, -1), (0, 1))

      // Bucle iterativo BFS con sintaxis 'while ... do'
      while queue.nonEmpty do
        // Desestructuración directa de la tupla extraída
        val (r, c) = queue.dequeue()
        
        // Iteración sobre direcciones usando 'for ... do'
        for (dr, dc) <- directions do
          val nr = r + dr
          val nc = c + dc
          
          if nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid(nr)(nc) == originalColor then
            grid(nr)(nc) = newColor
            queue.enqueue((nr, nc))
            
      grid

  // Función de validación robusta para Scala 3 (reemplaza el obsoleto .deep)
  def arraysEqual(m1: Array[Array[Int]], m2: Array[Array[Int]]): Boolean =
    m1.map(_.toList).toList == m2.map(_.toList).toList


// Entry point moderno de Scala 3.6
@main def runFloodFillScala36Refactor(): Unit =
  import P07FloodFill_v36.*
  
  // TC01: fill clasico
  val r1 = floodFill(Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1)), 1, 1, 2)
  assert(arraysEqual(r1, Array(Array(2, 2, 2), Array(2, 2, 0), Array(2, 0, 1))), "TC01 fallo")

  // TC02: grilla completa
  val r2 = floodFill(Array(Array(0, 0, 0), Array(0, 0, 0)), 0, 0, 1)
  assert(arraysEqual(r2, Array(Array(1, 1, 1), Array(1, 1, 1))), "TC02 fallo")

  // TC03: mismo color, sin cambio
  val r3 = floodFill(Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1)), 1, 1, 1)
  assert(arraysEqual(r3, Array(Array(1, 1, 1), Array(1, 1, 0), Array(1, 0, 1))), "TC03 fallo")

  println("All tests passed (Scala 3.6 BFS Iterativo)")

/*
EXPLICACION DE CAMBIOS E IDIOMATISMOS PARA TU TESIS (FP-UNA):

Al ingresar este bloque a tu página web de recolección de prompts, cierras de manera concluyente el análisis del paradigma iterativo/imperativo en el ecosistema Scala.

1. **La Muerte de la Palabra Clave `return`:**
En el código de Scala 2.13 que utilizaste como base, el retorno temprano (`if (...) return grid`) era un vestigio de Java. En Scala, el `return` explícito altera el control de flujo de una manera que puede fallar silenciosamente si el código es refactorizado dentro de una lambda funcional. Scala 3.6 formaliza `scala.util.boundary` como la solución definitiva de rendimiento y seguridad para escapar de bloques imperativos. Esto te permite documentar cómo los lenguajes modernos están corrigiendo los errores de diseño fundacionales de la programación orientada a objetos clásica.

2. **Obsolescencia de `.deep` en Pruebas:**
En tus primeras versiones iterativas de Scala 2 utilizabas `r1.deep == ...deep`. Scala 3 rediseñó fuertemente su biblioteca de colecciones para mejorar la compatibilidad estricta de tipos. Depender de `.deep` para matrices multidimensionales mutables lanza advertencias de compilación en 3.6. Transformar estructuralmente el arreglo con `.map(_.toList).toList` refleja la postura idiomática actual: las conversiones de estado deben ser inmutables y explícitas cuando se verifican igualdades matemáticas.

3. **La Supervivencia del Motor BFS (Memoria en Heap):**
A pesar de la evolución visual hacia la sintaxis "braceless" (`while ... do`) que lo hace lucir casi idéntico a Python 3.14, la arquitectura física de este código no ha variado ni un milímetro. Seguimos utilizando `mutable.Queue` y mutando el arreglo `grid(nr)(nc) = newColor`. Esto valida empíricamente la hipótesis principal de tu Trabajo Final de Grado: cuando se trata de evadir los límites del Call Stack de la JVM en grafos bidimensionales, la gestión explícita de memoria en el Heap (mediante colecciones mutables) sigue siendo el estándar universal de rendimiento, invulnerable a las modas sintácticas de las nuevas versiones del lenguaje.
*/