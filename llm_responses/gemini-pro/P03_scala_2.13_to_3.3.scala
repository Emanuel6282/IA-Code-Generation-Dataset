// CARACTERISTICAS DE SCALA 3.3 UTILIZADAS Y POR QUE:
// 1. Sintaxis sin llaves (Significant Indentation): Elimina el uso de `{}` en favor de la indentación estricta, lo que reduce el "ruido visual" y alinea Scala con lenguajes como Python.
// 2. Estructuras de control silenciosas (Quiet Syntax): Uso de `while condición do` en lugar de `while (condición) {...}`. Hace que el código imperativo sea más fluido y declarativo.
// 3. Tipos Existenciales Modernos (?): En Scala 3, el wildcard `_` para tipos fue reemplazado por `?` (`List[?]`). Esto evita confusiones con otros usos de `_` en el lenguaje y mejora la semántica.
// 4. Punto de entrada @main: Evita el código repetitivo de crear un `object` con un método `def main(args: Array[String]): Unit`. El compilador genera la clase envolvente de forma transparente.

import scala.collection.mutable.{ArrayDeque, ListBuffer}

def flatten(nested: Any): List[Int] =
  val result = ListBuffer.empty[Int]
  
  // mutable.Stack está obsoleto en las colecciones modernas de Scala. 
  // ArrayDeque es el estándar actual y altamente optimizado para usar como Pila (LIFO).
  val stack = ArrayDeque[Any](nested)

  while stack.nonEmpty do
    stack.removeHead() match
      case list: List[?] =>
        // Al usar ArrayDeque, podemos inyectar la lista entera al frente
        // manteniendo su orden sin necesidad de hacer '.reverse.foreach'
        stack.prependAll(list)
      case n: Int =>
        result += n
      case _ => 
        // ignorar otros tipos
        ()
        
  result.toList


@main def runFlattenTests(): Unit =
  assert(flatten(List(1, List(2, 3), List(4, List(5, 6)))) == List(1, 2, 3, 4, 5, 6))
  assert(flatten(List.empty) == List.empty)
  assert(flatten(List(1, 2, 3)) == List(1, 2, 3))
  println("All tests passed in Scala 3.3")

/*
EXPLICACION DE CAMBIOS E IDIOMATISMOS EN SCALA 3.3:

1. Desaparición del boilerplate (object / def main): 
   En Scala 2.13, las funciones de nivel superior no existían; todo debía estar encapsulado en un `object`. En Scala 3.3, `flatten` se declara directamente en el nivel superior (top-level definition) y `@main` autogenera el punto de entrada, lo cual es el estándar moderno para scripts y aplicaciones simples.

2. Sintaxis Braceless y 'do':
   El código se migró completamente al estilo basado en indentación (Optional Braces). El patrón `while (stack.nonEmpty) { ... }` se transformó en el idiomático `while stack.nonEmpty do`, ahorrando caracteres y mejorando la verticalidad visual del bloque lógico.

3. Pattern Matching de Tipos (List[?]):
   La coincidencia de patrones se actualizó de `case list: List[_]` a `case list: List[?]`. El wildcard `?` fue introducido en Scala 3 para denotar un tipo desconocido de manera inequívoca.

4. ArrayDeque en lugar de Stack:
   Aunque `mutable.Stack` seguía existiendo por retrocompatibilidad, su uso se desaconseja fuertemente en favor de `mutable.ArrayDeque`. Al hacer este cambio idiomático, el código de Scala 3.3 no solo es más seguro a nivel de compilador, sino que reemplazamos el ineficiente y verboso `list.reverse.foreach(stack.push)` por un simple `stack.prependAll(list)`, lo que realiza la mutación estructural en bloque delegando la eficiencia directamente a la colección de la biblioteca estándar.
*/