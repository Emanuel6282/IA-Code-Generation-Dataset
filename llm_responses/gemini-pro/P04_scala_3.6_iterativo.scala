// CARACTERISTICAS DE SCALA 3.6 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. scala.util.boundary (Directiva de Control de Flujo): Consolidado en las versiones recientes de Scala 3 (introducido experimentalmente en 3.3, ahora es el estándar idiomático en 3.6+). Permite realizar salidas tempranas ("early returns" o "breaks") de bucles imperativos de manera segura a nivel de tipos, sin la penalización de rendimiento de lanzar excepciones en tiempo de ejecución. Esto elimina la necesidad de usar variables bandera auxiliares (como `var isValid = true`).
// 2. Sintaxis sin llaves (Significant Indentation): Se omite el uso de llaves, utilizando `while ... do` y `if ... then ... else` para un código iterativo mucho más limpio y legible.
// 3. Paradigma Iterativo Explícito: Gestión manual estricta del estado con `var i = 0`, bucles `while`, mutación de `ListBuffer` para simular operaciones LIFO y mapeo con `mutable.Map`.
// 4. Punto de entrada moderno (@main): Ejecución directa eliminando el boilerplate de la orientación a objetos clásica.

import scala.collection.mutable
import scala.util.boundary
import scala.util.boundary.break

object BalancedParentheses:

  def isBalanced(s: String): Boolean =
    // Inicialización explícita de colecciones mutables
    val stack = mutable.ListBuffer.empty[Char]
    val pairs = mutable.Map.empty[Char, Char]
    
    // Poblado manual del mapa
    pairs.addOne(')' -> '(')
    pairs.addOne(']' -> '[')
    pairs.addOne('}' -> '{')

    var i = 0
    
    // Definimos el bloque 'boundary' que retorna un Boolean.
    // Llamar a 'break(valor)' aborta la ejecución del bloque y retorna ese valor directamente.
    boundary:
      while i < s.length do
        val c = s.charAt(i)
        
        if c == '(' || c == '[' || c == '{' then
          stack.addOne(c)
        else if pairs.contains(c) then
          // Si hay cierre pero la pila está vacía, abortamos el bucle iterativo retornado false
          if stack.isEmpty then 
            break(false)
          
          // Extraemos el último elemento insertado y mutamos la colección (O(1))
          val top = stack.last
          stack.remove(stack.length - 1)
          
          // Si el par no coincide, abortamos el bucle retornando false
          if top != pairs(c) then 
            break(false)
        
        i += 1
        
      // Si iteramos toda la cadena sin activar ningún 'break', el balance depende 
      // de que no queden caracteres de apertura pendientes en la pila.
      stack.isEmpty

// Punto de entrada nativo
@main def runIterativeTests36(): Unit =
  // ==========================================
  // CASOS DE PRUEBA
  // ==========================================
  
  // 1. Caso base del ejemplo: un solo par balanceado
  assert(BalancedParentheses.isBalanced("()") == true, "Error: '()' deberia retornar true")

  // 2. Caso del ejemplo: anidamiento incorrecto o cruzado
  assert(BalancedParentheses.isBalanced("([)]") == false, "Error: '([)]' deberia retornar false")

  // 3. Caso del ejemplo: anidamiento profundo balanceado
  assert(BalancedParentheses.isBalanced("{[]}") == true, "Error: '{[]}' deberia retornar true")

  // 4. Caso explícito de los requisitos: cadena vacía
  assert(BalancedParentheses.isBalanced("") == true, "Error: Una cadena vacia deberia retornar true")

  println("Todos los tests iterativos en Scala 3.6 pasaron correctamente.")