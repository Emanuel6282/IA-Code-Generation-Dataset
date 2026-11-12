// CARACTERISTICAS DE SCALA 3.3 UTILIZADAS Y POR QUE:
// 1. Sintaxis de Sangría Significativa (Braceless Syntax): Scala 3 permite omitir las llaves `{}` para los bloques de código, confiando en la indentación. Esto reduce enormemente el "ruido visual" y mejora la legibilidad.
// 2. Anotación @main: Sustituye la antigua necesidad de crear un `object` con un método `def main(args: Array[String])`. Es la forma moderna, directa y "boilerplate-free" de definir un punto de entrada.
// 3. Refactorización Funcional Pura (groupMapReduce): Aceptando el "desafío funcional" del código base, eliminamos completamente la colección mutable y las estructuras de control imperativas. Esto mejora el rendimiento general (sin reasignaciones de estado) y hace que el código sea inherentemente seguro y declarativo.

def wordFrequency(words: List[String]): Map[String, Int] =
  // identity: agrupa utilizando la propia palabra como clave
  // _ => 1: a cada ocurrencia le asigna el valor 1
  // _ + _: reduce (suma) las ocurrencias acumuladas para una misma clave
  words.groupMapReduce(identity)(_ => 1)(_ + _)

@main def runTests(): Unit =
  assert(wordFrequency(List("the", "fox", "the")) == Map("the" -> 2, "fox" -> 1))
  assert(wordFrequency(List.empty) == Map.empty)
  assert(wordFrequency(List("hello")) == Map("hello" -> 1))
  println("All tests passed")


// EXPLICACION DE CAMBIOS E IDIOMATISMOS EN SCALA 3.3:
//
// 1. Eliminación de estado mutable (var / mutable.Map): El código original en Scala 2 usaba un estilo imperativo clásico. En el ecosistema moderno de Scala, las operaciones deben ser inmutables por defecto. `groupMapReduce` es la abstracción estándar definitiva (optimizada internamente) para "agrupar y transformar", reemplazando más de 10 líneas imperativas por una sola línea declarativa.
//
// 2. Desaparición del `object P01WordFrequency_v213`: En Scala 3, gracias a las definiciones de "top-level" (nivel superior), ya no es obligatorio envolver funciones sueltas o métodos `main` dentro de objetos singleton. Las funciones pueden existir libres en el archivo.
//
// 3. Sintaxis Limpia (Quiet Syntax): En lugar de `def wordFrequency(...) { ... }`, se utiliza `def wordFrequency(...) =`. Combinado con la eliminación de llaves, el código se lee de forma mucho más fluida, similar a lenguajes como Python o Haskell, representando el estándar idiomático absoluto para Scala 3.3+.