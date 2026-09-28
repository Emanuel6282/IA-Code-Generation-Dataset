// CARACTERÍSTICAS NUEVAS DE SCALA 3.3 UTILIZADAS:
// 1. Sintaxis basada en indentación significativa (Significant Indentation): Elimina la necesidad de llaves `{}` redundantes en objetos, métodos y estructuras de control, mejorando drásticamente la limpieza visual del código.
// 2. Palabras clave de control de flujo modernas (`then`, `do`): Se emplean en estructuras `if-then-else` y bucles `while-do` para proveer una lectura más natural y fluida.
// 3. Optimizaciones de compilación en Scala 3: Ofrece un rendimiento optimizado al interactuar con arreglos nativos y estructuras imperativas planas de la JVM.

object P06MaxSubarray_v33:

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

// EXPLICACIÓN DE CAMBIOS Y POR QUÉ SON IDIOMÁTICOS EN SCALA 3.3:
//
// 1. Supresión de llaves por indentación:
//    Scala 3 estandariza el diseño basado en bloques por espacio en blanco (al estilo Python), reduciendo significativamente
//    el "ruido de llaves" heredado de Java y permitiendo un código más elegante y limpio.
//
// 2. Estructuras de control sintácticamente ligeras (`if ... then` y `while ... do`):
//    El uso de las palabras clave `then` y `do` en lugar de paréntesis y llaves previene ambigüedades visuales y hace que los bloques
//    imperativos sean más idiomáticos dentro del ecosistema moderno de Scala 3.
//
// 3. Preservación del enfoque imperativo de alto rendimiento:
//    Se mantuvo el núcleo del algoritmo de Kadane basado en variables mutables `var` y acceso directo por índice al arreglo para conservar
//    la máxima eficiencia en tiempo de ejecución de la JVM sin penalizaciones de asignación de memoria.