// P03: Flatten Nested List — Scala 3.6 (paradigma funcional)
// Solo biblioteca estandar de Scala. Sintaxis "significant indentation" de Scala 3.

import scala.annotation.tailrec

// CARACTERISTICA DE SCALA 3 USADA: Union types recursivos (introducidos en
// Scala 3.0, vigentes y estables en 3.6). En lugar de modelar la entrada
// como "List[Any]" (perdiendo toda informacion de tipo), se define un
// alias de tipo recursivo "Nested = Int | List[Nested]", que expresa con
// precision en el sistema de tipos la naturaleza real del dato: "un
// entero, o una lista de (enteros o listas de enteros, recursivamente)".
// El compilador puede ahora rechazar en tiempo de compilacion, por
// ejemplo, "List(1, "hola")", algo imposible de detectar con List[Any].
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