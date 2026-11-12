import scala.collection.mutable

/**
 * Dada una lista de palabras, retorna un mapa con la frecuencia 
 * de cada palabra utilizando un enfoque estrictamente iterativo en Scala 3.
 */
def wordFrequency(words: List[String]): Map[String, Int] =
  // 1. Uso de mapa mutable para mantener el estado
  val freqMap = mutable.Map.empty[String, Int]
  
  // 2. Uso de ListBuffer como se solicitó
  val buffer = mutable.ListBuffer.from(words)
  
  // 3. Variable mutable para el control del bucle
  var i = 0
  
  // 4. Bucle while clásico con la nueva sintaxis de Scala 3 (while ... do)
  while i < buffer.length do
    val word = buffer(i)
    
    // Condicional con la nueva sintaxis de Scala 3 (if ... then)
    if freqMap.contains(word) then
      freqMap(word) += 1
    else
      freqMap(word) = 1
      
    i += 1 // Mutación explícita del contador

  // Retornamos el mapa convertido a su versión inmutable
  freqMap.toMap


// ==========================================
// CASOS DE PRUEBA
// ==========================================
// En Scala 3, usamos @main en lugar de definir un 'object' con 'def main(...)'
@main def testWordFrequency(): Unit =
  // 1. Caso base del ejemplo proporcionado
  val result1 = wordFrequency(List("the", "fox", "the"))
  assert(result1 == Map("the" -> 2, "fox" -> 1), "Error en el caso base")

  // 2. Caso con una lista vacía (debe retornar un mapa vacío)
  val result2 = wordFrequency(List.empty[String])
  assert(result2 == Map.empty[String, Int], "Error en el caso de lista vacía")

  // 3. Caso con repeticiones múltiples
  val result3 = wordFrequency(List("iterativo", "scala3", "iterativo", "while", "scala3", "iterativo"))
  assert(
    result3 == Map("iterativo" -> 3, "scala3" -> 2, "while" -> 1), 
    "Error en repeticiones múltiples iterativas"
  )

  println("Todos los tests iterativos en Scala 3.3 pasaron correctamente.")