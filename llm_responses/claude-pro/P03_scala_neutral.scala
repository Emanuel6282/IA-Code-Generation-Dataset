import scala.annotation.tailrec

object Flatten {

  /** Retorna una lista plana con todos los enteros de `nested`, en el mismo orden.
    * Cada elemento debe ser un Int o, a su vez, una List anidada.
    *
    * La recursión es de cola (@tailrec): el compilador la convierte en un bucle, así
    * que funciona con cualquier nivel de anidamiento sin desbordar la pila.
    *
    * @throws IllegalArgumentException si algún elemento no es Int ni List
    */
  def flatten(nested: List[Any]): List[Int] = {
    @tailrec
    def loop(pending: List[Any], acc: List[Int]): List[Int] =
      pending match {
        case Nil                       => acc.reverse                   // se acumuló al revés
        case (sub: List[_]) :: rest    => loop(sub ::: rest, acc)       // sublista -> su contenido
        case (n: Int) :: rest          => loop(rest, n :: acc)          // entero -> a la salida
        case other :: _                => throw new IllegalArgumentException(s"Elemento no válido: $other")
      }

    loop(nested, Nil)
  }

  def main(args: Array[String]): Unit = {
    // Caso 1: ejemplo del enunciado
    assert(flatten(List(1, List(2, 3), List(4, List(5, 6)))) == List(1, 2, 3, 4, 5, 6))

    // Caso 2: lista vacía y sublistas vacías anidadas
    assert(flatten(List.empty) == List.empty)
    assert(flatten(List(Nil, List(Nil), List(Nil, List(Nil)))) == List.empty)

    // Caso 3: anidamiento muy profundo (una recursión que no sea de cola desbordaría la pila)
    val deep: Any = Iterator.iterate[Any](List(7))(x => List(x)).drop(100000).next()
    assert(flatten(List(0, deep, 8)) == List(0, 7, 8))

    println("Todas las pruebas pasaron.")
  }
}