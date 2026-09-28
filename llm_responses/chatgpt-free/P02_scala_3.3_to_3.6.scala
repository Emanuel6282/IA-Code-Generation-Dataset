// P02: Binary Search — Scala 3.6
//
// ============================================================
// CARACTERÍSTICAS UTILIZADAS
// ============================================================
//
// 1. Sintaxis de indentación de Scala 3:
//    Se utilizan `:` y `then` en lugar de llaves `{}`.
//    Esto reduce el código visualmente y mejora la legibilidad.
//
// 2. Sintaxis de control de Scala 3:
//    `while ... do` e `if ... then` hacen explícita la estructura
//    del algoritmo.
//
// Nota:
// No se introduce una característica exclusiva de Scala 3.6,
// porque ninguna de las nuevas características de esta versión
// mejora directamente el algoritmo Binary Search.
// Se conserva la implementación iterativa para mantener el mismo
// comportamiento y complejidad.
//
// Complejidad:
// Tiempo:  O(log n)
// Espacio: O(1)

object P02BinarySearch_v36:

  def binarySearch(arr: Array[Int], target: Int): Int =
    var left = 0
    var right = arr.length - 1

    while left <= right do
      val mid = left + (right - left) / 2

      if arr(mid) == target then
        return mid
      else if arr(mid) < target then
        left = mid + 1
      else
        right = mid - 1

    -1

  def main(args: Array[String]): Unit =
    assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)
    assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)
    assert(binarySearch(Array.empty[Int], 1) == -1)

    println("All tests passed")


// ============================================================
// EXPLICACIÓN DE LOS CAMBIOS
// ============================================================
//
// 1. Se actualizó el nombre del objeto:
//
//       P02BinarySearch_v213
//
//    a:
//
//       P02BinarySearch_v36
//
//    para identificar que la implementación corresponde a
//    Scala 3.6.
//
// 2. La función mantiene exactamente el nombre:
//
//       binarySearch
//
// 3. Se mantiene el algoritmo iterativo original utilizando:
//
//       var
//       while
//
//    No se introduce recursión ni un paradigma funcional.
//
// 4. Se utiliza la sintaxis de indentación de Scala 3:
//
//       object P02BinarySearch_v36:
//
//       def binarySearch(...) =
//
//       while left <= right do
//
//       if ... then
//
//    Esta sintaxis elimina llaves innecesarias y es idiomática
//    en Scala 3.6.
//
// 5. Se conserva exactamente el cálculo del punto medio:
//
//       left + (right - left) / 2
//
//    en lugar de:
//
//       (left + right) / 2
//
//    para evitar un posible overflow al sumar los límites.
//
// 6. Los tres tests originales se mantienen:
//
//       [1, 3, 5, 7, 9], 5 -> 2
//       [1, 3, 5, 7, 9], 4 -> -1
//       []                , 1 -> -1
//
// 7. No se utiliza ninguna función de búsqueda binaria de la
//    biblioteca estándar.
//
// 8. No se utilizan `map`, `filter`, `foldLeft`, recursión ni
//    colecciones mutables, ya que el código base corresponde al
//    paradigma iterativo.
//
// 9. No se introducen `enum`, `given/using`, extension methods,
//    macros u otras características que no aporten una ventaja
//    real a este algoritmo.
//
// 10. El comportamiento permanece idéntico:
//
//       - Si encuentra el objetivo, devuelve su índice.
//       - Si no lo encuentra, devuelve -1.
//       - Para un arreglo vacío devuelve -1.
//
// 11. Complejidad:
//
//       Tiempo:  O(log n)
//       Espacio: O(1)