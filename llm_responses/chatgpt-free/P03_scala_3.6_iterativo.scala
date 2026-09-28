// P03: Flatten Nested List — Scala 3.6
//
// Características utilizadas:
// 1. Sintaxis de Scala 3:
//    Se utiliza la sintaxis moderna de Scala 3.6, incluyendo List[?]
//    para representar listas de tipo desconocido.
//
// 2. Pattern matching:
//    Permite distinguir claramente entre listas anidadas e enteros.
//
// 3. mutable.ListBuffer:
//    Se utiliza como resultado y como pila explícita para realizar
//    todo el procesamiento de forma iterativa.
//
// 4. mutable.Map:
//    Se utiliza para almacenar información mutable del procesamiento,
//    cumpliendo el requisito del paradigma iterativo.
//
// 5. while:
//    Controla explícitamente el recorrido de la estructura.
//
// 6. var:
//    Permite mantener el estado mutable necesario para el algoritmo.
//
// Nota:
// Scala 3.6 no introduce en este problema una característica específica
// que mejore significativamente el algoritmo iterativo. Por ello, no se
// fuerza el uso de características nuevas que no aporten valor.
//
// No se utilizan map(), filter(), foldLeft(), flatMap() ni recursión.

import scala.collection.mutable

def flatten(nested: List[Any]): List[Int] = {
  var result = mutable.ListBuffer[Int]()
  var stack = mutable.ListBuffer[Any]()
  var info = mutable.Map[String, Int]("processed" -> 0)

  // Colocar la lista inicial en la pila.
  stack += nested

  while (stack.nonEmpty) {
    val item = stack.remove(stack.length - 1)

    item match {
      case list: List[?] =>
        // Los elementos se agregan en orden inverso porque la pila
        // utiliza el principio LIFO.
        var i = list.length - 1

        while (i >= 0) {
          stack += list(i)
          i -= 1
        }

      case value: Int =>
        result += value
        info("processed") = info("processed") + 1

      case _ =>
        // No se procesa ningún otro tipo.
    }
  }

  result.toList
}


// ============================================================
// CASOS DE PRUEBA
// ============================================================

// TC01: anidamiento básico
assert(
  flatten(
    List(
      1,
      List(2, 3),
      List(4, List(5, 6))
    )
  ) == List(1, 2, 3, 4, 5, 6)
)

// TC02: lista vacía
assert(
  flatten(List.empty[Any]) == List.empty[Int]
)

// TC03: lista ya plana
assert(
  flatten(List(1, 2, 3)) == List(1, 2, 3)
)

println("All tests passed")

/*
 * CAMBIOS Y DECISIONES:
 *
 * 1. Se utiliza la sintaxis List[?].
 *
 *    En Scala 3, List[?] representa una lista cuyo tipo de elemento
 *    es desconocido. Es la sintaxis moderna de Scala 3 para este caso.
 *
 * 2. Se utiliza pattern matching.
 *
 *    Los elementos se clasifican mediante:
 *
 *        case list: List[?] =>
 *        case value: Int =>
 *
 *    Esto permite distinguir las listas anidadas de los enteros
 *    de forma clara.
 *
 * 3. Se utiliza mutable.ListBuffer.
 *
 *    result almacena los enteros encontrados y stack funciona como
 *    una pila explícita para procesar la estructura anidada.
 *
 * 4. Se utiliza mutable.Map.
 *
 *    info mantiene un contador de los enteros procesados.
 *    Se utiliza para cumplir explícitamente el requisito de utilizar
 *    mutable.Map dentro del paradigma iterativo.
 *
 * 5. Se utiliza var.
 *
 *    Las variables result, stack e info representan el estado mutable
 *    del algoritmo iterativo.
 *
 * 6. Se utiliza while.
 *
 *    El recorrido de la pila y el recorrido inverso de cada lista
 *    se realizan exclusivamente mediante while.
 *
 * 7. No se utiliza recursión.
 *
 *    La estructura anidada se procesa mediante una pila explícita,
 *    por lo que la profundidad de la estructura no depende de la
 *    pila de llamadas del programa.
 *
 * 8. No se utilizan operaciones funcionales.
 *
 *    Se excluyen explícitamente:
 *
 *        map()
 *        filter()
 *        foldLeft()
 *        flatMap()
 *
 *    Esto mantiene el código estrictamente dentro del paradigma
 *    iterativo solicitado.
 *
 * 9. Se conserva el orden original.
 *
 *    Los elementos de cada lista se introducen en la pila desde
 *    el último hasta el primero. Debido al comportamiento LIFO,
 *    posteriormente se procesan en el mismo orden en que aparecen
 *    originalmente.
 *
 * 10. Se mantiene exactamente el nombre flatten.
 *
 *    La firma es:
 *
 *        def flatten(nested: List[Any]): List[Int]
 *
 * 11. Biblioteca estándar únicamente.
 *
 *    Solo se utiliza scala.collection.mutable, que forma parte
 *    de la biblioteca estándar de Scala.
 *
 * 12. Scala 3.6.
 *
 *    Se mantiene la sintaxis moderna de Scala 3.6 sin introducir
 *    características nuevas artificiales que no aporten una mejora
 *    real al algoritmo.
 */