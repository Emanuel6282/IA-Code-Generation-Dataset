// Caracteristicas nuevas de Scala 3.6 utilizadas y por que:
// 1. Sintaxis significativa por indentacion (optional braces), el estilo por
//    defecto y recomendado en Scala 3.6 para reducir ruido sintactico frente
//    a las llaves tradicionales.
// 2. `then` en el if, requerido por la sintaxis sin llaves para separar
//    claramente la condicion del cuerpo.
// 3. `@main` en lugar de un metodo `main(args: Array[String])` dentro de un
//    `object`, forma idiomatica y estable en la linea 3.x (incluida 3.6) para
//    declarar un punto de entrada ejecutable de forma concisa.
// 4. Definiciones de nivel superior (top-level definitions), disponibles desde
//    Scala 3 y vigentes en 3.6, que evitan el boilerplate de envolver la
//    funcion en un `object` cuando no se necesita un namespace.
// 5. Se mantiene el paradigma iterativo original (var, while), ya que esta es
//    una migracion de version, no un cambio de paradigma.

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
// 1. Se agrego el comentario inicial documentando las caracteristicas de Scala 3.6
//    consideradas y su aporte a la legibilidad del codigo.
// 2. Se elimino el `object P06MaxSubarray_v213` envolvente y se paso a
//    definiciones de nivel superior, evitando el boilerplate de un objeto
//    contenedor innecesario.
// 3. Se removieron las llaves `{}` en favor de la sintaxis basada en
//    indentacion, el estilo idiomatico por defecto en la linea 3.x.
// 4. Se reemplazo `def main(args: Array[String]): Unit = { ... }` por
//    `@main def runTests(): Unit = ...`, la forma idiomatica en Scala 3 de
//    declarar un punto de entrada, sin necesidad de gestionar `args`.
// 5. La logica del algoritmo de Kadane se mantuvo exactamente igual (mismas
//    comparaciones, mismo flujo con `var` y `while`), preservando el mismo
//    comportamiento y resultados que la version 2.13.
// 6. Se conservaron los 4 casos de prueba originales con `assert`, sin alterar
//    los valores esperados.
// 7. No se introdujeron colecciones inmutables, `foldLeft`, `@tailrec` ni otras
//    caracteristicas funcionales, ya que el codigo base es explicitamente
//    iterativo y la migracion debe preservar ese paradigma, no cambiarlo.