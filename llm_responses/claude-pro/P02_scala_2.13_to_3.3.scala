// P02: Binary Search — migrado de Scala 2.13 a Scala 3.3
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. Recursión de cola con @tailrec en lugar de while + var + return: era el
//    objetivo declarado del código base ("forzar el desafío recursivo"). El
//    compilador verifica que la recursión es de cola y la convierte en un salto,
//    así que el rendimiento y el uso de pila son los mismos que con el bucle, pero
//    sin variables mutables ni `return`.
// 2. Sintaxis sin llaves e `if ... then ... else` (Scala 3): la estructura la da la
//    indentación y cada condición se lee como una expresión.
// 3. Definiciones de nivel superior (Scala 3): binarySearch no necesita un
//    `object` contenedor.
// 4. @main (Scala 3): declara el punto de entrada sin `object` ni
//    `def main(args: Array[String])`.
//
// Nota: la novedad propia de Scala 3.3, scala.util.boundary/break, es la forma
// idiomática de salir antes de un bucle `while`. Aquí no hace falta, porque la
// versión recursiva simplemente devuelve el índice cuando lo encuentra; sería la
// opción adecuada si se quisiera conservar el estilo iterativo.

import scala.annotation.tailrec

def binarySearch(arr: Array[Int], target: Int): Int =
  @tailrec
  def search(left: Int, right: Int): Int =
    if left > right then -1
    else
      val mid = left + (right - left) / 2
      if arr(mid) == target then mid
      else if arr(mid) < target then search(mid + 1, right)
      else search(left, mid - 1)

  search(0, arr.length - 1)

@main def P02BinarySearch_v33(): Unit =
  assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)
  assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)
  assert(binarySearch(Array.empty[Int], 1) == -1)
  println("All tests passed")

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN SCALA 3.3
// 1. while + `var left/right` + `return mid` -> función interna `search(left, right)`
//    con @tailrec: el estado que antes se mutaba pasa a ser parámetros de cada
//    llamada. Se evita `return`, que en Scala es poco idiomático porque rompe el
//    modelo de "todo es una expresión".
// 2. Se conservan la fórmula del punto medio (sin desbordamiento de Int), las
//    comparaciones y el orden de las ramas: el índice devuelto es idéntico al del
//    original, incluso con valores repetidos (verificado con miles de casos
//    aleatorios).
// 3. `object P02BinarySearch_v213 { ... }` -> definiciones de nivel superior.
// 4. `def main(args: Array[String]): Unit` -> `@main def P02BinarySearch_v33()`:
//    forma idiomática de Scala 3; args no se usaba.
// 5. Llaves e `if (cond)` -> sintaxis por indentación e `if cond then`, el estilo
//    recomendado en Scala 3.
// 6. Los tres asserts y el mensaje "All tests passed" no cambian (solo se añadieron
//    espacios en los arreglos, según las convenciones de estilo).