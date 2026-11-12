// CARACTERISTICAS DE SCALA 3.3 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Sintaxis sin llaves (Braceless Syntax): Se utiliza la nueva sintaxis de indentación estricta introducida en Scala 3, eliminando las llaves `{}` en objetos, métodos y estructuras de control.
// 2. Nuevas palabras clave de control de flujo: Uso de `while ... do` y `if ... then ... else`, que son el estándar idiomático en Scala 3 para bloques imperativos.
// 3. Punto de entrada @main: Reemplaza la antigua y verbosa firma `def main(args: Array[String]): Unit` u `object Main extends App` por una simple anotación `@main` sobre un método de nivel superior.
// 4. Paradigma Iterativo Explícito: Cumpliendo los requisitos, se usan `var`, `while`, `mutable.ListBuffer` y `mutable.Map` para manejar el estado y la memoria manualmente, sin recursión ni transformaciones funcionales.

import scala.collection.mutable

object BalancedParentheses:

  def isBalanced(s: String): Boolean =
    // Inicialización de colecciones mutables
    val stack = mutable.ListBuffer.empty[Char]
    val pairs = mutable.Map.empty[Char, Char]
    
    // Poblamos el mapa mutable
    pairs.addOne(')' -> '(')
    pairs.addOne(']' -> '[')
    pairs.addOne('}' -> '{')

    var i = 0
    var isValid = true

    // Scala 3: Bucle 'while' con sintaxis 'do'
    while i < s.length && isValid do
      val c = s.charAt(i)

      // Scala 3: Condicional 'if' con sintaxis 'then'
      if c == '(' || c == '[' || c == '{' then
        stack.addOne(c)
      else if pairs.contains(c) then
        if stack.isEmpty then
          isValid = false
        else
          // Extraemos el tope y mutamos la lista eliminándolo
          val top = stack.last
          stack.remove(stack.length - 1)
          
          if top != pairs(c) then
            isValid = false
      
      i += 1

    // Retorna verdadero solo si el ciclo terminó sin errores y la pila quedó vacía
    isValid && stack.isEmpty

// Scala 3: Nueva forma de definir el punto de entrada del programa
@main def runIterativeTests(): Unit =
  // ==========================================
  // CASOS DE PRUEBA
  // ==========================================
  
  // 1. Caso base del ejemplo: un solo par balanceado
  assert(BalancedParentheses.isBalanced("()") == true, "Error: '()' deberia retornar true")

  // 2. Caso del ejemplo: anidamiento incorrecto o cruzado
  assert(BalancedParentheses.isBalanced("([)]") == false, "Error: '([)]' deberia retornar false")

  // 3. Caso del ejemplo: anidamiento profundo balanceado
  assert(BalancedParentheses.isBalanced("{[]}") == true, "Error: '{[]}' deberia retornar true")

  // 4. Caso adicional explícito en los requisitos
  assert(BalancedParentheses.isBalanced("") == true, "Error: Una cadena vacia deberia retornar true")

  println("Todos los tests iterativos en Scala 3.3 pasaron correctamente.")