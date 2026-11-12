// CARACTERISTICAS DE SCALA 3.6 UTILIZADAS Y POR QUE:
// 1. Funciones de Nivel Superior (Top-level): Permite definir la función y el programa directamente en el archivo sin necesidad de envolverlos en un `object`, reduciendo el nivel de anidamiento.
// 2. Sintaxis de Indentación Significativa (Braceless): Al igual que en la versión 3.3, Scala 3.6 consolida el uso de la indentación en lugar de llaves `{}` para definir bloques funcionales, logrando un código más limpio y legible.
// 3. Anotación @main: Simplifica drásticamente el punto de entrada del programa al reemplazar la antigua y verbosa firma `def main(args: Array[String]): Unit`.
// 4. Transformación Funcional Declarativa (`groupMapReduce`): Respondiendo al comentario original del código base de "forzar el desafío funcional", reemplazamos la pesada iteración mutable por esta potente abstracción nativa de la biblioteca estándar, maximizando rendimiento y seguridad.

def wordFrequency(words: List[String]): Map[String, Int] =
  // identity: usa la palabra misma como clave de agrupación
  // _ => 1: a cada palabra encontrada le asigna un valor base de 1
  // _ + _: reduce/suma los valores cuando la clave (palabra) se repite
  words.groupMapReduce(identity)(_ => 1)(_ + _)

@main def runWordFrequencyTests(): Unit =
  // Adaptación de los tests usando la sintaxis limpia de Scala 3
  assert(wordFrequency(List("the", "fox", "the")) == Map("the" -> 2, "fox" -> 1))
  assert(wordFrequency(List.empty) == Map.empty)
  assert(wordFrequency(List("hello")) == Map("hello" -> 1))
  
  println("All tests passed")


// EXPLICACION DE CAMBIOS E IDIOMATISMOS EN SCALA 3.6:
//
// 1. Eliminación del Estado Mutable y Bucles: El estilo imperativo (con `mutable.Map` y variables reasignables) se considera un anti-patrón en el desarrollo moderno y concurrente en Scala. En Scala 3.6, el enfoque idiomático por defecto es funcional puro. La función `groupMapReduce` fusiona un `groupBy` y un `map` en una sola pasada altamente optimizada.
//
// 2. Desaparición del `object P01WordFrequency_v213`: Ya no necesitamos envolver nuestras funciones estáticas dentro de un objeto singleton como dictaba Scala 2. Las funciones libres (top-level) son ahora la norma para utilidades de este tipo.
//
// 3. Punto de entrada `@main`: En lugar del bloque `def main` con el array de argumentos explícito, la anotación `@main` sobre una función genera en tiempo de compilación todo el código de envoltura necesario para que la aplicación sea ejecutable, eliminando ruido visual (boilerplate).
//
// 4. Expresiones sin llaves (Quiet Syntax): Todo el código fluye naturalmente gracias a la sintaxis sin llaves. Esto reduce el tamaño del archivo y acerca el lenguaje al ideal visual de lenguajes de scripting, manteniendo su fuerte tipado y compilación.