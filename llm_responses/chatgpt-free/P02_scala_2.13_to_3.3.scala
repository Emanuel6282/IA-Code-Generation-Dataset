// P02: Binary Search — Scala 3.3
//
// ============================================================
// CARACTERÍSTICAS DE SCALA 3.3 UTILIZADAS
// ============================================================
//
// 1. Sintaxis de indentación de Scala 3:
//    Se eliminan las llaves `{}` innecesarias y se utiliza la
//    indentación para delimitar bloques. Esto mejora la legibilidad
//    sin modificar el comportamiento.
//
// 2. Sintaxis moderna de Scala 3:
//    El `object`, los métodos y los bloques condicionales utilizan
//    la sintaxis concisa de Scala 3.
//
// No se utilizan `enum`, `given/using`, extension methods ni
// pattern matching porque no proporcionan una mejora real para
// esta implementación concreta de Binary Search.
//
// El algoritmo continúa siendo iterativo y mantiene:
// Tiempo:  O(log n)
// Espacio: O(1)

object P02BinarySearch_v33:

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
// 1. Se actualizó el nombre de la clase:
//
//       P02BinarySearch_v213
//
//    a:
//
//       P02BinarySearch_v33
//
//    para identificar la versión de Scala utilizada.
//
// 2. La función mantiene exactamente el nombre:
//
//       binarySearch
//
// 3. Se conserva el algoritmo de búsqueda binaria iterativo:
//
//       var + while
//
//    Por lo tanto, la lógica y el comportamiento son idénticos.
//
// 4. Se utiliza la sintaxis de indentación de Scala 3:
//
//       object P02BinarySearch_v33:
//
//       def binarySearch(...) =
//
//       while left <= right do
//
//       if ... then
//
//    Esta sintaxis es más concisa y es idiomática en Scala 3.
//
// 5. Se mantienen exactamente los tres casos de prueba:
//
//       [1, 3, 5, 7, 9], 5 -> 2
//       [1, 3, 5, 7, 9], 4 -> -1
//       []                , 1 -> -1
//
// 6. Se mantiene el cálculo del punto medio:
//
//       left + (right - left) / 2
//
//    en lugar de:
//
//       (left + right) / 2
//
//    para evitar un posible overflow al sumar los límites.
//
// 7. No se utiliza ninguna función de búsqueda binaria de la
//    biblioteca estándar.
//
// 8. No se introducen características de Scala 3 que no aporten
//    una ventaja real al problema. Esto evita hacer una migración
//    artificial únicamente para utilizar nuevas características.
//
// 9. El comportamiento permanece idéntico al código de Scala 2.13:
//
//       - Si encuentra el objetivo, devuelve su índice.
//       - Si no lo encuentra, devuelve -1.
//       - Si el arreglo está vacío, devuelve -1.
//
// 10. Complejidad:
//
//       Tiempo:  O(log n)
//       Espacio: O(1)