// P03: Flatten Nested List — Scala 2.13 (paradigma funcional)
// Solo biblioteca estandar de Scala.

import scala.annotation.tailrec

object Flatten {

  /**
   * Dada una lista arbitrariamente anidada de enteros, retorna una lista
   * plana con todos los enteros en el mismo orden de aparicion.
   *
   * Implementacion estrictamente funcional: usa recursion de cola
   * anotada con @tailrec y colecciones inmutables unicamente. Se modela
   * el "descenso" por los niveles de anidamiento con una pila inmutable
   * (una List[Any] que actua como stack, reconstruida en cada paso con
   * "::" en O(1) sin mutacion) en lugar de mutable.Stack, y el resultado
   * se acumula tambien de forma inmutable, con appended (":+") solo al
   * final mediante reverse para mantener O(n) total.
   *
   * Nota sobre @tailrec y estructuras arbitrariamente anidadas: el
   * aplanado de una lista anidada es, por naturaleza, un recorrido de
   * arbol (cada sublista es una "rama"), no una recursion lineal simple;
   * una funcion que llama recursivamente en dos "direcciones" (un
   * elemento hoja y continuar con el resto) no es tail-recursive de forma
   * directa. Para satisfacer genuinamente el requisito de @tailrec, se
   * usa el patron de aplanar con una pila explicita *inmutable*: en lugar
   * de que la funcion se llame a si misma para "bajar" a una sublista y
   * luego "volver" a procesar el resto (lo cual no es cola-recursivo), se
   * mantiene toda la estructura pendiente de procesar en una unica pila
   * (List[Any]), de modo que cada llamada recursiva es la ultima
   * operacion de la funcion — cumpliendo la definicion estricta de
   * recursion de cola que el compilador de Scala puede verificar y
   * optimizar a un bucle.
   *
   * @param nested Lista que puede contener Int y/o otras List[Any]
   *               anidadas a cualquier profundidad.
   * @return Una List[Int] plana con todos los enteros en el mismo orden de
   *         aparicion. Si la lista de entrada esta vacia, retorna una
   *         lista vacia.
   */
  def flatten(nested: List[Any]): List[Int] = {

    @tailrec
    def loop(pendientes: List[Any], acumulado: List[Int]): List[Int] =
      pendientes match {
        case Nil =>
          acumulado.reverse
        case (sublista: List[_]) :: resto =>
          // Se antepone (prepend) el contenido de la sublista al resto de
          // pendientes, preservando el orden: los elementos de la
          // sublista deben procesarse antes que los que ya estaban en
          // "resto". "prependedAll" (PEP-equivalente en Scala 2.13 para
          // colecciones) inserta en O(k) sin mutacion.
          loop(sublista.asInstanceOf[List[Any]] ++ resto, acumulado)
        case (entero: Int) :: resto =>
          loop(resto, entero :: acumulado)
        case _ :: resto =>
          loop(resto, acumulado)
      }

    loop(nested, List.empty[Int])
  }

  def main(args: Array[String]): Unit = {
    // Caso 1: ejemplo del enunciado, anidamiento de varios niveles
    val caso1: List[Any] = List(1, List(2, 3), List(4, List(5, 6)))
    assert(flatten(caso1) == List(1, 2, 3, 4, 5, 6))

    // Caso 2: lista vacia -> lista vacia
    assert(flatten(List.empty[Any]) == List.empty[Int])

    // Caso 3: lista plana sin anidamiento
    val caso3: List[Any] = List(1, 2, 3)
    assert(flatten(caso3) == List(1, 2, 3))

    println("Todos los casos de prueba pasaron correctamente.")
  }
}