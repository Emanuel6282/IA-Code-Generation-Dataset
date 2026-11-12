import scala.annotation.tailrec

// CARACTERISTICAS DE SCALA 2.13 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Recursión de Cola (`@tailrec`): En lugar de bucles iterativos (`while`), se emplea recursividad pura. La anotación `@tailrec` obliga al compilador de Scala 2.13 a optimizar la llamada recursiva, transformándola a nivel de bytecode en un bucle iterativo sin consumir memoria en la pila de llamadas (previniendo StackOverflow).
// 2. Pattern Matching Estructural: Se aprovecha el poder del pattern matching sobre colecciones inmutables (`head :: tail`) para desestructurar la lista recursivamente de forma declarativa, eliminando el acceso por índices inseguros (`arr(i)`).
// 3. Inmutabilidad Absoluta (`val` y `List`): Transformamos el `Array` mutable primitivo de la JVM en una `List` puramente inmutable nativa de Scala. Toda la gestión del estado se realiza a través del paso de parámetros en funciones, eliminando por completo las variables mutables (`var`).

object MaxSubarray_Scala213_Functional {

  def maxSubarraySum(arr: Array[Int]): Int = {
    require(arr != null && arr.nonEmpty, "El arreglo debe tener al menos 1 elemento.")

    // Función pura interna para propagar el estado inmutable
    @tailrec
    def loop(remaining: List[Int], currentMax: Int, globalMax: Int): Int = {
      remaining match {
        // Caso base: la lista está vacía, retornamos el máximo global acumulado
        case Nil => globalMax
        
        // Caso recursivo: separamos la cabeza (elemento actual) de la cola (resto)
        case head :: tail =>
          // Transición de estado sin mutación
          val newCurrent = math.max(head, currentMax + head)
          val newGlobal = math.max(globalMax, newCurrent)
          
          // Llamada recursiva de cola propagando los nuevos estados
          loop(tail, newCurrent, newGlobal)
      }
    }

    // Conversión a colección estrictamente inmutable e inicio de la recursión
    val immutableList = arr.toList
    loop(immutableList.tail, immutableList.head, immutableList.head)
  }

  def main(args: Array[String]): Unit = {
    // ==========================================
    // CASOS DE PRUEBA
    // ==========================================

    // 1. Caso base del ejemplo: mezcla de positivos y negativos
    assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6, "Error: Caso base esperado 6")

    // 2. Caso con todos los elementos negativos (retorna el menos negativo)
    assert(maxSubarraySum(Array(-5, -2, -9, -1, -4)) == -1, "Error: Arreglo de negativos esperado -1")

    // 3. Caso con todos los elementos positivos (retorna la suma total)
    assert(maxSubarraySum(Array(1, 2, 3, 4, 5)) == 15, "Error: Arreglo de positivos esperado 15")

    println("Todos los tests funcionales en Scala 2.13 pasaron correctamente.")
  }
}