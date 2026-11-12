// CARACTERISTICAS DE SCALA 3.6 UTILIZADAS Y POR QUE:
// 1. Sintaxis sin llaves (Significant Indentation): Estandarizada y madura en Scala 3.6, elimina el ruido visual de las llaves `{}` usando la indentación estricta para definir bloques de código.
// 2. Estructuras de control silenciosas (Quiet Syntax): Se utiliza `while ... do` sin paréntesis alrededor de la condición, logrando un flujo imperativo más natural y declarativo.
// 3. Tipos existenciales con '?': El wildcard para tipos desconocidos es ahora `?` (ej. `List[?]`), reemplazando al antiguo `_` de Scala 2 que resultaba ambiguo en ciertos contextos.
// 4. Punto de entrada @main y Top-level definitions: Scala 3.6 permite definir funciones directamente en la raíz del archivo sin encapsularlas en un `object`, y el macro `@main` autogenera el código necesario para la ejecución.

import scala.collection.mutable.{ArrayDeque, ListBuffer}

// Definición de nivel superior (Top-level), sin necesidad de envolver en un `object`
def flatten(nested: Any): List[Int] =
  val result = ListBuffer.empty[Int]
  
  // mutable.Stack está obsoleto. ArrayDeque es la estructura moderna recomendada
  // en Scala para operaciones de pila (LIFO) de alto rendimiento.
  val stack = ArrayDeque[Any](nested)

  // Bucle con quiet syntax y braceless
  while stack.nonEmpty do
    stack.removeHead() match
      case list: List[?] =>
        // ArrayDeque permite inyectar colecciones enteras al frente de forma eficiente
        // manteniendo el orden, eliminando el ineficiente `list.reverse.foreach`
        stack.prependAll(list)
      case n: Int =>
        result += n
      case _ => 
        // Ignorar otros tipos (Unit implícito)
        ()
        
  result.toList


// Entrypoint generado automáticamente por el compilador
@main def runFlattenTests36(): Unit =
  assert(flatten(List(1, List(2, 3), List(4, List(5, 6)))) == List(1, 2, 3, 4, 5, 6))
  assert(flatten(List.empty) == List.empty)
  assert(flatten(List(1, 2, 3)) == List(1, 2, 3))
  println("All tests passed in Scala 3.6")

/*
EXPLICACION DE CAMBIOS E IDIOMATISMOS EN SCALA 3.6:

1. Eliminación del Boilerplate (Top-level y @main):
   En Scala 2.13, todo debía ir dentro de un `object` y se requería una firma verbosa `def main(args: Array[String]): Unit`. En Scala 3.6, el estándar idiomático para scripts y puntos de entrada simples es usar definiciones a nivel de archivo (Top-level) combinadas con la anotación `@main`, lo que delega la generación de la clase principal al compilador.

2. Sintaxis Braceless y 'do':
   Todo el código fue migrado a la sintaxis de indentación estricta introducida en Scala 3. El clásico `while (stack.nonEmpty) { ... }` es ahora `while stack.nonEmpty do`. Esto no solo ahorra caracteres, sino que alinea a Scala con el diseño visual de lenguajes como Python, haciéndolo altamente idiomático en la versión 3.6.

3. Actualización de Pattern Matching (List[?]):
   La coincidencia de patrones se actualizó de `case list: List[_]` a `case list: List[?]`. El uso de `?` como wildcard de tipos existenciales es obligatorio/idiomático en Scala 3 para evitar colisiones con el uso de `_` en lambdas y pattern matching de valores.

4. Sustitución de Stack por ArrayDeque:
   El uso de `mutable.Stack` es un antipatrón en Scala moderno (está marcado como obsoleto/deprecated). Al migrar a `ArrayDeque`, no solo nos adherimos a las mejores prácticas de las colecciones de Scala 3.6, sino que ganamos el método `prependAll()`. Esto nos permite deshacernos del bucle interno imperativo `list.reverse.foreach(stack.push)`, delegando la inserción múltiple a la capa optimizada de la colección subyacente.
*/