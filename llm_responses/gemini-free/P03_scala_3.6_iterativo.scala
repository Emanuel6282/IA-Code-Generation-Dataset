import scala.collection.mutable

// Scala 3.6 - Paradigma Iterativo Estricto
// Utiliza la sintaxis basada en sangría, wildcard types con `?` e iteración mediante `while` 
// sobre estructuras mutables (`ListBuffer` y `ArrayDeque`) evitando recursión y combinadores funcionales.

def flatten(nestedList: List[Any]): List[Int] =
  val result = mutable.ListBuffer[Int]()
  val stack = mutable.ArrayDeque[Any]()

  stack.append(nestedList)

  while stack.nonEmpty do
    val current = stack.removeLast()

    current match
      case list: List[?] =>
        // Iteración mediante while inverso para mantener el orden de evaluación
        var i = list.length - 1
        while i >= 0 do
          stack.append(list(i))
          i -= 1
      case elem: Int =>
        result.append(elem)
      case _ => ()

  result.toList

@main def run(): Unit =
  // --- Casos de prueba usando assert (Scala 3.6 - Iterativo) ---

  // Caso 1: Ejemplo del problema con múltiples niveles de anidamiento
  val test1: List[Any] = List(1, List(2, 3), List(4, List(5, 6)))
  assert(flatten(test1) == List(1, 2, 3, 4, 5, 6), "Falló el Caso 1")

  // Caso 2: Lista vacía y estructuras con sublistas vacías intercaladas
  val test2a: List[Any] = Nil
  val test2b: List[Any] = List(List(), List(List()), List(1, List()), List(List(2)), List())
  assert(flatten(test2a) == Nil, "Falló el Caso 2a (Lista vacía)")
  assert(flatten(test2b) == List(1, 2), "Falló el Caso 2b (Sublistas vacías)")

  // Caso 3: Lista totalmente plana y lista profundamente anidada
  val test3a: List[Any] = List(10, 20, 30)
  val test3b: List[Any] = List(List(List(List(List(99)))))
  assert(flatten(test3a) == List(10, 20, 30), "Falló el Caso 3a (Lista ya plana)")
  assert(flatten(test3b) == List(99), "Falló el Caso 3b (Profundamente anidada)")

  println("¡Todos los casos de prueba pasaron correctamente!")