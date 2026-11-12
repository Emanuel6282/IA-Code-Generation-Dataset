// CARACTERISTICAS DE SCALA 3.3 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Sintaxis sin llaves (Braceless Syntax / Significant Indentation): Scala 3 permite omitir las llaves {} y utilizar la indentación estricta para definir el alcance de los bloques de código, métodos y pattern matching, resultando en un código más declarativo y limpio.
// 2. Wildcard de Tipos Moderno (?): En Scala 3, el comodín para tipos existenciales (o desconocidos) pasa de ser `_` a `?`. Por eso el pattern matching utiliza `case list: List[?]` en lugar del antiguo `case list: List[_]`.
// 3. Método @main: Elimina la verbosidad de declarar un `object` contenedor y un método `def main(args: Array[String])`. El compilador genera el entrypoint automáticamente.
// 4. @tailrec (Recursividad de Cola): Paradigma funcional puro sin variables mutables, asegurando que el compilador optimice la recursión en un bucle iterativo a nivel de bytecode para evitar un StackOverflow en anidamientos masivos.

import scala.annotation.tailrec

/**
 * Toma una lista arbitrariamente anidada (representada como List[Any] en Scala)
 * y retorna una lista plana unidimensional de enteros.
 *
 * Implementación estrictamente funcional en Scala 3.3:
 * Inmutable, basada en recursividad de cola (@tailrec) y pattern matching.
 */
def flatten(nestedList: List[Any]): List[Int] =
  // Función anidada recursiva de cola con sintaxis Scala 3.
  // 'todo' es nuestra lista inmutable de tareas pendientes.
  // 'acc' es el acumulador inmutable. Al inyectar elementos al frente (::)
  // logramos O(1) en rendimiento, por lo que revertimos el resultado al final.
  @tailrec
  def loop(todo: List[Any], acc: List[Int]): List[Int] = todo match
    // Caso base: se procesaron todos los elementos. Invertimos el acumulador.
    case Nil => 
      acc.reverse
      
    // Si la cabeza de la lista es un Int, lo agregamos al acumulador y avanzamos.
    case (n: Int) :: tail => 
      loop(tail, n :: acc)
      
    // Si la cabeza es una sublista, usamos el operador ::: para concatenarla
    // frente a las tareas pendientes y aplanarla progresivamente.
    // Usamos el wildcard '?' introducido en Scala 3.
    case (list: List[?]) :: tail => 
      loop(list ::: tail, acc)
      
    // Si encontramos un elemento nulo u otro tipo, lo ignoramos de forma segura.
    case _ :: tail => 
      loop(tail, acc)

  // Disparamos la recursión con la lista inicial y el acumulador vacío
  loop(nestedList, Nil)


// Punto de entrada generado automáticamente en Scala 3
@main def runFunctionalTests(): Unit =
  // ==========================================
  // CASOS DE PRUEBA
  // ==========================================

  // 1. Caso base del ejemplo: múltiples niveles de anidamiento
  val nested1 = List(1, List(2, 3), List(4, List(5, 6)))
  val expected1 = List(1, 2, 3, 4, 5, 6)
  assert(flatten(nested1) == expected1, "Error: Falló el caso base del ejemplo")

  // 2. Caso límite: lista completamente vacía
  val nested2 = List.empty[Any]
  val expected2 = List.empty[Int]
  assert(flatten(nested2) == expected2, "Error: Una lista vacía debería retornar una lista vacía")

  // 3. Caso extremo: anidamiento profundo asimétrico con listas vacías en el medio
  val nested3 = List(1, List(), List(List(List(3))))
  val expected3 = List(1, 3)
  assert(flatten(nested3) == expected3, "Error: Falló al procesar listas vacías internas")

  println("Todos los tests funcionales en Scala 3.3 pasaron correctamente.")