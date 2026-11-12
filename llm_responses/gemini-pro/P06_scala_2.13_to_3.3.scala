// CARACTERISTICAS DE SCALA 3.3 UTILIZADAS Y POR QUE:
// 1. Definiciones de Nivel Superior (Top-level definitions) y @main: Se elimina el objeto contenedor (`object P06MaxSubarray_v213`) y el engorroso método `main(args: Array[String])`. En Scala 3, las funciones pueden existir libremente en el archivo y el punto de entrada se marca simplemente con `@main`, reduciendo el "boilerplate" al mínimo.
// 2. Sintaxis sin llaves (Braceless Syntax): Se adopta la indentación significativa. Al eliminar las llaves `{}` en favor de la indentación estructurada, el código adquiere una limpieza visual comparable a la de Python, lo cual mejora enormemente la legibilidad.
// 3. Trailing Lambdas para Plegado Funcional: Atendiendo al desafío propuesto en los comentarios del código base, se migra el estado mutable a un `foldLeft` funcional. Se utiliza la nueva sintaxis de Scala 3 con `:` para pasar el bloque de la función anónima de forma limpia, integrándolo como si fuera una estructura de control nativa.

def maxSubarraySum(arr: Array[Int]): Int =
  require(arr != null && arr.nonEmpty, "El arreglo debe contener al menos un elemento")

  // Se inicializa la tupla acumuladora con el primer elemento: (currentMax, globalMax)
  val initialState = (arr(0), arr(0))

  // Iteración funcional sobre el resto del arreglo utilizando foldLeft y trailing lambda
  val (_, finalGlobalMax) = arr.tail.foldLeft(initialState): (acc, curr) =>
    val (currentMax, globalMax) = acc
    val newCurrentMax = math.max(curr, currentMax + curr)
    val newGlobalMax = math.max(globalMax, newCurrentMax)
    (newCurrentMax, newGlobalMax)

  finalGlobalMax


@main def runRefactoredTests(): Unit =
  assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6)
  assert(maxSubarraySum(Array(1)) == 1)
  assert(maxSubarraySum(Array(-2, -1)) == -1)
  assert(maxSubarraySum(Array(5, 4, -1, 7, 8)) == 23)
  println("All tests passed")


/*
EXPLICACION DE CAMBIOS E IDIOMATISMOS EN SCALA 3.3:

1. **Resolución del Desafío Funcional (De Imperativo a Declarativo)**:
   El código original de Scala 2.13 dependía de variables mutables (`var`) y un bucle `while`, un enfoque puramente imperativo. Al refactorizar hacia Scala 3.3, adoptamos el paradigma funcional empaquetando el estado en una tupla inmutable `(Int, Int)`. El método `foldLeft` se encarga de propagar este estado matemáticamente sin efectos secundarios. Para tu TFG en la FP-UNA, este contraste es una demostración perfecta de cómo un mismo algoritmo transita entre paradigmas.

2. **Ergonomía de las "Trailing Lambdas"**:
   En versiones anteriores de Scala, el `foldLeft` funcional a menudo resultaba visualmente pesado por el anidamiento de paréntesis y llaves: `foldLeft(initialState) { (acc, curr) => ... }`. Scala 3.3 introduce los dos puntos `:` para abrir un bloque indentado. Esto hace que las operaciones de orden superior se lean con la misma naturalidad que un bloque `for` o `while` nativo del lenguaje.

3. **Eliminación del Ruido Estático (Orientado a Objetos)**:
   Al ingresar esta migración en tu aplicación web de rastreo de prompts, documentarás la fuerte simplificación estructural de Scala 3. Al no requerir un `object` Singleton para envolver la lógica y los tests, el lenguaje abandona la rigidez estricta de Java, permitiendo que la arquitectura funcional respire y se enfoque exclusivamente en la transformación de los datos.
*/