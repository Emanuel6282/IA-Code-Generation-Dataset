// CARACTERISTICAS DE SCALA 3.6 UTILIZADAS Y POR QUE:
// 1. Sintaxis Braceless Completa (Significant Indentation): En Scala 3.6 la adopción de la sintaxis sin llaves {} y los bloques de control silenciosos (quiet syntax) como `while ... do` alcanzan su madurez. Permite un flujo iterativo imperativo tan limpio como Python.
// 2. Wildcards de Tipos Estrictos (`List[?]`): El uso de `?` para tipos existenciales (en lugar del antiguo `_` de Scala 2) es el estándar definitivo en Scala 3.6, haciendo que la comprobación de tipos por Pattern Matching sea semánticamente más precisa.
// 3. ArrayDeque de Alto Rendimiento: El ecosistema moderno de Scala consolida a `scala.collection.mutable.ArrayDeque` como la colección mutable estrella para operaciones O(1) en ambos extremos, siendo la mejor opción para simular nuestra pila iterativa.
// 4. Entrypoint Moderno (`@main`): Despliega el bloque de ejecución eliminando el boilerplate de los objetos contenedores heredado de Java.

import scala.collection.mutable.{ArrayDeque, ListBuffer}

/**
 * Toma una lista arbitrariamente anidada (representada como List[Any] en Scala)
 * y retorna una lista plana unidimensional de enteros.
 *
 * Implementación estrictamente iterativa en Scala 3.6: sin recursión, 
 * utilizando un bucle while y estructuras de datos mutables in-place.
 */
def flatten(nestedList: List[Any]): List[Int] =
  // ListBuffer es la colección mutable canónica para adjuntar elementos 
  // al final en O(1) y convertir a inmutable rápidamente al terminar
  val result = ListBuffer.empty[Int]
  
  // ArrayDeque opera como nuestra Pila (Stack) explícita
  val stack = ArrayDeque.from(nestedList)

  // Bucle imperativo puro con sintaxis limpia (sin paréntesis)
  while stack.nonEmpty do
    // Extraemos de la cima de la pila
    val item = stack.removeHead()
    
    // Pattern matching apalancando braceless syntax
    item match
      case number: Int =>
        // Mutación explícita del estado
        result += number
        
      case list: List[?] =>
        // Desanidamiento imperativo: inyectamos la sublista completa al frente.
        // En la siguiente iteración el bucle destripará estos nuevos elementos.
        stack.prependAll(list)
        
      case _ =>
        // Ignoramos tipos no compatibles o valores nulos de forma segura
        ()

  // Retorno convertido a inmutable O(1)
  result.toList


// Función autogenerada por el compilador como punto de entrada
@main def runFlattenIterativeTests(): Unit =
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

  // 3. Caso extremo: anidamiento profundo con listas vacías en el medio
  val nested3 = List(1, List(), List(List(List(3))))
  val expected3 = List(1, 3)
  assert(flatten(nested3) == expected3, "Error: Falló al procesar listas vacías internas")

  println("Todos los tests iterativos en Scala 3.6 pasaron correctamente.")