import scala.collection.mutable

/**
 * Dada una lista de palabras, retorna un mapa con la frecuencia 
 * de cada palabra utilizando un enfoque estrictamente iterativo en Scala 3.6.
 */
def wordFrequency(words: List[String]): Map[String, Int] =
  // 1. Instanciamos un mapa mutable explícitamente para mantener el estado
  val freqMap = mutable.Map.empty[String, Int]
  
  // 2. Convertimos la lista inmutable de entrada a un ListBuffer mutable (como solicitado)
  val buffer = mutable.ListBuffer.from(words)
  
  // 3. Declaramos una variable 'var' para el control del índice del bucle
  var i = 0
  
  // 4. Bucle while clásico con la sintaxis limpia de Scala 3 (while ... do)
  while i < buffer.length do
    val word = buffer(i)
    
    // Condicional con la sintaxis de Scala 3 (if ... then ... else)
    if freqMap.contains(word) then
      freqMap(word) += 1
    else
      freqMap(word) = 1
      
    // Mutación explícita del estado del contador
    i += 1 

  // Convertimos el resultado a la interfaz inmutable Map exigida por la firma
  freqMap.toMap


// ==========================================
// CASOS DE PRUEBA
// ==========================================
// En Scala 3 usamos @main para declarar el punto de entrada sin necesidad de un 'object' wrapper
@main def testWordFrequencyIterative(): Unit =
  // 1. Caso base del ejemplo proporcionado
  val result1 = wordFrequency(List("the", "fox", "the"))
  assert(result1 == Map("the" -> 2, "fox" -> 1), "Error en el caso base")

  // 2. Caso con una lista vacía (debe retornar un mapa vacío)
  val result2 = wordFrequency(List.empty[String])
  assert(result2 == Map.empty[String, Int], "Error en el caso de lista vacía")

  // 3. Caso con repeticiones múltiples
  val result3 = wordFrequency(List("scala", "iterativo", "scala", "var", "scala", "var"))
  assert(
    result3 == Map("scala" -> 3, "iterativo" -> 1, "var" -> 2), 
    "Error en repeticiones múltiples iterativas"
  )

  println("Todos los tests iterativos en Scala 3.6 pasaron correctamente.")