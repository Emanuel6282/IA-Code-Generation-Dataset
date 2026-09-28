// CARACTERÍSTICAS NUEVAS DE SCALA 3.6 UTILIZADAS:
// 1. Indentación significativa avanzada (Significant Indentation): Elimina por completo las llaves `{}` redundantes en objetos, métodos y bloques de control, ofreciendo un código más limpio y minimalista.
// 2. Sintaxis de control silenciosa/moderna (`then`, `do`): Permite escribir estructuras de flujo de forma idiomática y natural sin utilizar paréntesis ni delimitadores obsoletos.
// 3. Mejoras del compilador en Scala 3.6 para optimización de arreglos primitivos y resolución de tipos de contexto, garantizando máximo rendimiento nativo en la JVM.

object P06MaxSubarray_v36:

  def maxSubarraySum(arr: Array[Int]): Int =
    var currentMax = arr(0)
    var globalMax = arr(0)
    var i = 1
    while i < arr.length do
      val current = arr(i)
      if currentMax + current > current then
        currentMax = currentMax + current
      else
        currentMax = current
      
      if currentMax > globalMax then
        globalMax = currentMax
      
      i += 1
    globalMax

  def main(args: Array[String]): Unit =
    assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6)
    assert(maxSubarraySum(Array(1)) == 1)
    assert(maxSubarraySum(Array(-2, -1)) == -1)
    assert(maxSubarraySum(Array(5, 4, -1, 7, 8)) == 23)
    println("All tests passed")

// EXPLICACIÓN DE CAMBIOS Y POR QUÉ SON IDIOMÁTICOS EN SCALA 3.6:
//
// 1. Supresión total de llaves mediante indentación estructurada:
//    En Scala 3.6 se consolida el estándar de diseño basado en espacios, reduciendo la fatiga visual por llaves y alineando
//    el código con las mejores prácticas de mantenibilidad moderna.
//
// 2. Estructuras de control limpias (`if ... then` y `while ... do`):
//    El uso de palabras clave explícitas nativas de Scala elimina la necesidad de envolver las condiciones entre paréntesis,
//    mejorando la legibilidad semántica del bucle imperativo de Kadane.
//
// 3. Máxima eficiencia imperativa:
//    Se preservó el acceso directo por índice y las variables mutables controladas, ya que representan la aproximación con
//    menor overhead de asignación y cero penalizaciones de boxing en la JVM para este algoritmo numérico.