// Caracteristicas nuevas de Scala 3.3 utilizadas y por que:
// 1. Sintaxis significativa por indentacion (optional braces) en lugar de llaves,
//    ya que es el estilo idiomatico recomendado en Scala 3 para mejorar la
//    legibilidad al eliminar ruido sintactico.
// 2. `then` en el if, requerido por la nueva sintaxis sin llaves para separar
//    la condicion del cuerpo de forma clara.
// 3. `@main` en lugar de extender una clase con `main(args: Array[String])`,
//    ya que es la forma idiomatica y mas concisa en Scala 3 de definir un
//    punto de entrada ejecutable.
// 4. Se mantiene el paradigma iterativo original (var, while) tal como en la
//    version 2.13, ya que la tarea es una migracion de version, no un cambio
//    de paradigma.

def maxSubarraySum(arr: Array[Int]): Int =
  var currentMax = arr(0)
  var globalMax = arr(0)
  var i = 1
  while i < arr.length do
    if currentMax + arr(i) > arr(i) then
      currentMax = currentMax + arr(i)
    else
      currentMax = arr(i)
    if currentMax > globalMax then
      globalMax = currentMax
    i += 1
  globalMax

@main def runTests(): Unit =
  assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6)
  assert(maxSubarraySum(Array(1)) == 1)
  assert(maxSubarraySum(Array(-2, -1)) == -1)
  assert(maxSubarraySum(Array(5, 4, -1, 7, 8)) == 23)
  println("All tests passed")

// Cambios realizados y justificacion:
// 1. Se agrego el comentario inicial documentando las caracteristicas de Scala 3.3
//    utilizadas, explicando su aporte a la legibilidad.
// 2. Se elimino el `object P06MaxSubarray_v213` envolvente y se paso a definiciones
//    de nivel superior (top-level definitions), una capacidad nueva de Scala 3 que
//    evita el boilerplate de un objeto contenedor cuando no es necesario.
// 3. Se removieron las llaves `{}` en favor de la sintaxis basada en indentacion,
//    el estilo idiomatico y recomendado por defecto en Scala 3.3.
// 4. Se reemplazo `def main(args: Array[String]): Unit = { ... }` por `@main def
//    runTests(): Unit = ...`, la forma idiomatica en Scala 3 de declarar un punto
//    de entrada, mas concisa y sin necesidad de gestionar `args` manualmente.
// 5. La logica del algoritmo de Kadane se mantuvo exactamente igual (mismas
//    comparaciones, mismo flujo con `var` y `while`), preservando comportamiento
//    y resultados identicos a la version 2.13.
// 6. Se conservaron los 4 casos de prueba originales con `assert`, sin alterar
//    los valores esperados.
// 7. No se introdujeron colecciones inmutables, `foldLeft`, `@tailrec` ni otras
//    caracteristicas funcionales, ya que el codigo base es explicitamente
//    iterativo y la migracion debe preservar ese paradigma, no cambiarlo.