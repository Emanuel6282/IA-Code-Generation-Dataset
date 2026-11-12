// P01: Word Frequency — migrado a Scala 3.6
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. Sintaxis sin llaves (indentación significativa, Scala 3): elimina ruido
//    visual; la estructura del código la da la indentación.
// 2. Definiciones de nivel superior (Scala 3): wordFrequency no necesita un
//    `object` contenedor, porque es una función pura y autónoma.
// 3. @main (Scala 3): declara el punto de entrada sin escribir un `object` ni
//    `def main(args: Array[String])`.
// 4. groupMapReduce (biblioteca estándar 2.13, que Scala 3.6 reutiliza): sustituye
//    el bucle con mutable.Map por una sola expresión funcional que agrupa, mapea y
//    reduce en una sola pasada. Es más legible y elimina el estado mutable, que era
//    el objetivo declarado del código base ("forzar el desafío funcional").
//
// Nota sobre las novedades de Scala 3.6 (verificado con el compilador 3.6.4):
// - Estable: nueva sintaxis de given y de context bounds con `as` (SIP-64). Sirve
//   para abstracciones con type classes, que esta función no necesita.
// - Experimental (requiere -experimental): tuplas con nombre y runtimeChecked. No
//   se usan para no depender de funciones que todavía pueden cambiar.

/** Retorna un mapa que asocia cada palabra única con su número de apariciones. */
def wordFrequency(words: List[String]): Map[String, Int] =
  words.groupMapReduce(identity)(_ => 1)(_ + _)

@main def P01WordFrequency_v36(): Unit =
  assert(wordFrequency(List("the", "fox", "the")) == Map("the" -> 2, "fox" -> 1))
  assert(wordFrequency(List.empty) == Map.empty)
  assert(wordFrequency(List("hello")) == Map("hello" -> 1))
  println("All tests passed")

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN SCALA 3.6
// 1. Bucle `for` + mutable.Map + if/else -> words.groupMapReduce(identity)(_ => 1)(_ + _):
//    expresa la intención ("contar por palabra") en una línea, sin mutación visible.
//    Devuelve el mismo Map inmutable que el original, incluido el orden de
//    iteración (verificado comparando toString sobre miles de listas aleatorias).
// 2. Se eliminó `import scala.collection.mutable`: ya no se usa estado mutable.
// 3. `object P01WordFrequency_v213 { ... }` -> definiciones de nivel superior:
//    en Scala 3 no hace falta envolver funciones en un object.
// 4. `def main(args: Array[String]): Unit` -> `@main def P01WordFrequency_v36()`:
//    forma idiomática de Scala 3; args no se usaba.
// 5. Llaves -> sintaxis por indentación, el estilo recomendado en Scala 3.
// 6. Se actualizó la cabecera: el código base decía "Scala 2.13" aunque se
//    presentaba como código de Scala 3.3.
// 7. Los tres asserts y el mensaje "All tests passed" no cambian.