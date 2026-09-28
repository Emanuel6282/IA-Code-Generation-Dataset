import scala.annotation.tailrec

// Scala 3.3 (LTS) - Paradigma Funcional Estricto
// Aprovecha la sintaxis basada en sangría (optional braces), tipos de unión (Match Types / Union Types)
// y recursión de cola (@tailrec) con estructuras inmutables.

def flatten(nestedList: List[Any]): List[Int] =
  @tailrec
  def process(acc: List[Int], remaining: List[Any]): List[Int] = remaining match
    case Nil => acc
    case (head: List[?]) :: tail => process(acc, head ::: tail)
    case (head: Int) :: tail     => process(acc :+ head, tail)
    case _ :: tail               => process(acc, tail)

  process(Nil, nestedList)

@main def run(): Unit =
  // --- Casos de prueba usando assert (Scala 3.3 - Funcional) ---

  // Caso 1: Ejemplo provisto con múltiples niveles de anidamiento
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