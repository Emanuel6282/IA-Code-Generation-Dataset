El siguiente codigo resuelve "Word Frequency" en Scala version 2.13:

```scala
﻿// P01: Word Frequency — Scala 2.13 (codigo base para refactoring)
// Estilo: imperativo con mutable.Map y bucle for, para forzar el desafio funcional en el refactor
import scala.collection.mutable

object P01WordFrequency_v213 {

  def wordFrequency(words: List[String]): Map[String, Int] = {
    val result = mutable.Map[String, Int]()
    for (word <- words) {
      if (result.contains(word)) {
        result(word) = result(word) + 1
      } else {
        result(word) = 1
      }
    }
    result.toMap
  }

  def main(args: Array[String]): Unit = {
    assert(wordFrequency(List("the", "fox", "the")) == Map("the" -> 2, "fox" -> 1))
    assert(wordFrequency(List.empty) == Map.empty)
    assert(wordFrequency(List("hello")) == Map("hello" -> 1))
    println("All tests passed")
  }
}
```

Migra este codigo a Scala version 3.3:

1. CARACTERISTICAS NUEVAS: Usa las de Scala 3.3 que mejoren legibilidad o rendimiento. Al inicio, en un comentario, lista cuales usaste y por que.

2. COMPORTAMIENTO IDENTICO: Misma logica, mismos resultados.

3. NOMBRE DE FUNCION: Debe permanecer exactamente "wordFrequency".

4. MISMOS TESTS: Conserva los casos de prueba, adaptados a Scala 3.3.

5. EXPLICA TUS CAMBIOS: Al final en un comentario, enumera los cambios y por que son idiomaticos en Scala 3.3.