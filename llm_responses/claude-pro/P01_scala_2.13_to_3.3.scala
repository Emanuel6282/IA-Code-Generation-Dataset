// P01: Word Frequency — migrado de Scala 2.13 a Scala 3.3
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. Sintaxis sin llaves (indentación significativa, Scala 3): elimina ruido
//    visual; la estructura del código la da la indentación.
// 2. Definiciones de nivel superior (Scala 3): wordFrequency no necesita un
//    `object` contenedor, porque es una función pura y autónoma.
// 3. @main (Scala 3): declara el punto de entrada sin escribir un `object` ni
//    `def main(args: Array[String])`.
// 4. groupMapReduce (biblioteca estándar 2.13, que Scala 3.3 reutiliza): sustituye
//    el bucle con mutable.Map por una sola expresión funcional que agrupa, mapea y
//    reduce en una sola pasada. Es más legible y elimina el estado mutable, que era
//    el objetivo declarado del código base ("forzar el desafío funcional").
//
// Nota: la novedad propia de 3.3, scala.util.boundary/break, sirve para salir
// antes de un cálculo; aquí hay que recorrer la lista entera, así que no aplica.

/** Retorna un mapa que asocia cada palabra única con su número de apariciones. */
def wordFrequency(words: List[String]): Map[String, Int] =
  words.groupMapReduce(identity)(_ => 1)(_ + _)

@main def P01WordFrequency_v33(): Unit =
  assert(wordFrequency(List("the", "fox", "the")) == Map("the" -> 2, "fox" -> 1))
  assert(wordFrequency(List.empty) == Map.empty)
  assert(wordFrequency(List("hello")) == Map("hello" -> 1))
  println("All tests passed")

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN SCALA 3.3
// 1. Bucle `for` + mutable.Map + if/else -> words.groupMapReduce(identity)(_ => 1)(_ + _):
//    expresa la intención ("contar por palabra") en una línea, sin mutación visible.
//    Devuelve el mismo Map inmutable que el original, incluido el orden de
//    iteración (verificado comparando toString sobre miles de listas aleatorias).
// 2. Se eliminó `import scala.collection.mutable`: ya no se usa estado mutable.
// 3. `object P01WordFrequency_v213 { ... }` -> definiciones de nivel superior:
//    en Scala 3 no hace falta envolver funciones en un object.
// 4. `def main(args: Array[String]): Unit` -> `@main def P01WordFrequency_v33()`:
//    forma idiomática de Scala 3; args no se usaba.
// 5. Llaves -> sintaxis por indentación, el estilo recomendado en Scala 3.
// 6. Los tres asserts y el mensaje "All tests passed" no cambian.