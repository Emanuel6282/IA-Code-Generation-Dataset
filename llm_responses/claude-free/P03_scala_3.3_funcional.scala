// P03: Flatten Nested List — Scala 3.3 (paradigma funcional)
// Solo biblioteca estandar de Scala. Sintaxis "significant indentation" de Scala 3.

import scala.annotation.tailrec

// CARACTERISTICA NUEVA DE SCALA 3.3 USADA: Union types recursivos (una de
// las caracteristicas insignia de Scala 3, no disponible en Scala 2.13).
// En lugar de modelar la entrada como "List[Any]" (perdiendo toda
// informacion de tipo, como se hizo en las versiones anteriores de Java y
// Scala 2.13 de este mismo problema), se define un alias de tipo
// recursivo "Nested = Int | List[Nested]", que expresa con precision en el
// sistema de tipos la naturaleza real del dato: "un entero, o una lista de
// (enteros o listas de enteros, recursivamente)". Esto mejora
// genuinamente la legibilidad y la seguridad de tipos: el compilador
// ahora puede advertir si se pasa, por ejemplo, un String suelto, algo que
// era imposible de detectar con "List[Any]".
type Nested = Int | List[Nested]

/**
 * Dada una lista arbitrariamente anidada de enteros, retorna una lista
 * plana con todos los enteros en el mismo orden de aparicion.
 *
 * Implementacion estrictamente funcional: recursion de cola con @tailrec
 * y colecciones inmutables unicamente. El "descenso" por los niveles de
 * anidamiento se modela con una pila inmutable de pendientes (List[Nested]
 * reconstruida en cada paso con "::"/"++", sin mutacion), y el resultado
 * se acumula tambien de forma inmutable (en orden inverso con "::" por
 * eficiencia O(1), revertido una sola vez al final).
 *
 * Nota sobre @tailrec: el aplanado de una lista anidada es un recorrido
 * de arbol, no una recursion lineal simple (una funcion que hiciera
 * "flatten(cabeza) ++ flatten(resto)" no seria tail-recursive, pues queda
 * trabajo pendiente tras la llamada recursiva sobre "cabeza"). Para
 * satisfacer @tailrec de forma genuina, se usa una pila explicita
 * *inmutable* de trabajo pendiente, de modo que cada llamada recursiva es
 * la ultima operacion de la funcion.
 *
 * @param nested Lista de tipo List[Nested]: puede contener Int y/o otras
 *               List[Nested] anidadas a cualquier profundidad.
 * @return Una List[Int] plana con todos los enteros en el mismo orden de
 *         aparicion. Si la lista de entrada esta vacia, retorna una
 *         lista vacia.
 */
def flatten(nested: List[Nested]): List[Int] =

  @tailrec
  def loop(pendientes: List[Nested], acumulado: List[Int]): List[Int] =
    pendientes match
      case Nil =>
        acumulado.reverse
      case (sublista: List[Nested]) :: resto =>
        loop(sublista ++ resto, acumulado)
      case (entero: Int) :: resto =>
        loop(resto, entero :: acumulado)

  loop(nested, List.empty[Int])

@main def runTests(): Unit =
  // Caso 1: ejemplo del enunciado, anidamiento de varios niveles
  val caso1: List[Nested] = List(1, List(2, 3), List(4, List(5, 6)))
  assert(flatten(caso1) == List(1, 2, 3, 4, 5, 6))

  // Caso 2: lista vacia -> lista vacia
  assert(flatten(List.empty[Nested]) == List.empty[Int])

  // Caso 3: lista plana sin anidamiento
  val caso3: List[Nested] = List(1, 2, 3)
  assert(flatten(caso3) == List(1, 2, 3))

  println("Todos los casos de prueba pasaron correctamente.")