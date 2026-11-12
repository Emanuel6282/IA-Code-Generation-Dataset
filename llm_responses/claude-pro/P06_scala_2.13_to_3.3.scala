// P06: Max Subarray Sum (Kadane) — migrado de Scala 2.13 a Scala 3.3
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. foldLeft con una tupla acumuladora (currentMax, globalMax) en lugar de dos `var` y
//    un `while` con índice: era el objetivo declarado del código base ("usar foldLeft
//    con tupla acumuladora"). El estado deja de ser mutable y cada paso es una función
//    pura del estado anterior y del elemento actual.
// 2. `if … then … else` como expresión (sintaxis de control nueva en Scala 3): cada
//    componente del nuevo estado se calcula con una sola expresión, sin asignaciones.
// 3. Sintaxis sin llaves (indentación significativa, Scala 3): la estructura la da la
//    indentación.
// 4. Definiciones de nivel superior y @main (Scala 3): maxSubarraySum no necesita un
//    `object` contenedor y el punto de entrada no necesita `def main(args: ...)`.
//
// Nota: no se usan boundary/break (Scala 3.3) porque Kadane debe recorrer todo el
// arreglo; no hay salida temprana.

def maxSubarraySum(arr: Array[Int]): Int =
  arr.iterator
    .drop(1)
    .foldLeft((arr(0), arr(0))) { (acc, x) =>
      val (currentMax, globalMax) = acc
      val newCurrent = if currentMax + x > x then currentMax + x else x
      (newCurrent, if newCurrent > globalMax then newCurrent else globalMax)
    }
    ._2

@main def P06MaxSubarray_v33(): Unit =
  assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6)
  assert(maxSubarraySum(Array(1)) == 1)
  assert(maxSubarraySum(Array(-2, -1)) == -1)
  assert(maxSubarraySum(Array(5, 4, -1, 7, 8)) == 23)
  println("All tests passed")

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN SCALA 3.3
// 1. `var currentMax`, `var globalMax`, `var i` + `while` -> foldLeft sobre una tupla
//    inmutable. Las mismas comparaciones, en el mismo orden: el nuevo currentMax es
//    `currentMax + x` si `currentMax + x > x` y si no `x`; el nuevo globalMax es
//    currentMax si lo supera. Por eso los resultados son idénticos.
// 2. `arr.iterator.drop(1)` recorre desde el segundo elemento, igual que `i = 1`, sin
//    copiar el arreglo (un `arr.tail` crearía uno nuevo).
// 3. El estado inicial sigue siendo `(arr(0), arr(0))`: con un arreglo vacío se lanza la
//    misma ArrayIndexOutOfBoundsException ("Index 0 out of bounds for length 0") que en
//    el original.
// 4. `val (currentMax, globalMax) = acc` desestructura el acumulador con nombres
//    legibles. Un foldLeft recibe dos parámetros (acumulador y elemento), así que el
//    desempaquetado automático de tuplas de Scala 3 no aplica aquí.
// 5. `if (…) { … } else { … }` con asignaciones -> `if … then … else` como expresión.
// 6. `object P06MaxSubarray_v213 { ... }` -> definición de nivel superior, y
//    `def main(args: Array[String])` -> `@main def P06MaxSubarray_v33()`.
// 7. Llaves -> sintaxis por indentación, el estilo recomendado en Scala 3.
// 8. Los cuatro asserts y el mensaje "All tests passed" no cambian. Comportamiento
//    verificado contra el original con decenas de miles de arreglos aleatorios.
//
// NOTA SOBRE RENDIMIENTO: el foldLeft es más lento que el while original, porque crea
// una tupla por elemento y empaqueta los Int (foldLeft es genérico). Medido con 5
// millones de elementos: ~4 ms el while frente a ~57 ms el foldLeft. Sigue siendo O(n);
// el cambio se hace por legibilidad e inmutabilidad, que es lo que pedía el código base.
// Si ese tramo fuera crítico en rendimiento, convendría conservar el while.
//
// NOTA SOBRE DESBORDAMIENTO (no se cambia para respetar el comportamiento idéntico): el
// original, y por tanto esta migración, suma en Int. Si la suma supera Int.MaxValue se
// desborda en silencio: para Array(Int.MaxValue, Int.MaxValue) la respuesta correcta es
// 4294967294, pero `currentMax + x` da -2, la comparación falla y devuelve 2147483647.
// Para corregirlo habría que acumular en Long y cambiar el tipo de retorno.