// CARACTERISTICAS DE SCALA 3.3 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Sintaxis sin llaves (Braceless Syntax) y nuevas palabras clave: Uso extensivo de indentación significativa, `if ... then ... else` y omisión de llaves en objetos y métodos, lo que reduce el ruido visual y es el estándar idiomático en Scala 3.
// 2. Punto de entrada @main: Reemplaza la verbosidad de definir un objeto con un método `main(args: Array[String])`.
// 3. Inmutabilidad Absoluta: Eliminación total de 'var' y estructuras mutables. El estado (la pila) se gestiona creando nuevas listas inmutables (`char :: stack` o `stack.tail`).
// 4. Mónada Option con flatMap y Option.when: Representa el estado válido como `Some(List)` y el desbalance como `None`. `flatMap` propaga el error (short-circuit semántico) sin usar bloques imperativos.
// 5. Recursión de Cola (@tailrec): Se provee una función recursiva pura de alto rendimiento que evalúa los tokens y aborta anticipadamente (short-circuit físico) si el estado se vuelve `None`.

import scala.annotation.tailrec

object BalancedParentheses:

  /**
   * Verifica si una cadena de paréntesis, corchetes y llaves 
   * está correctamente balanceada y cerrada en orden.
   */
  def isBalanced(s: String): Boolean =
    val pairs = Map(')' -> '(', ']' -> '[', '}' -> '{')

    // 1. filter() y map(): Pipeline funcional perezoso para limpiar la entrada.
    val tokens = s.toList
      .filter(c => "()[]{}".contains(c))
      .map(identity)

    // 2. Demostración funcional usando foldLeft() y flatMap().
    // foldLeft es el combinador funcional por excelencia para reducción de estado.
    // Aunque procesa toda la lista, flatMap arrastra eficientemente el estado inválido (None).
    lazy val foldLeftResult = tokens.foldLeft(Option(List.empty[Char])): (state, char) =>
      state.flatMap: stack =>
        if "([{".contains(char) then
          Some(char :: stack)
        else
          Option.when(stack.nonEmpty && stack.head == pairs(char))(stack.tail)

    // 3. Implementación productiva usando @tailrec (actúa como un fold corto-circuitado)
    @tailrec
    def evaluate(remaining: List[Char], state: Option[List[Char]]): Option[List[Char]] =
      if state.isEmpty then None // Short-circuit: abortamos la recursión si ya falló
      else remaining match
        case Nil => state
        case char :: tail =>
          // Calculamos el siguiente estado inmutable usando flatMap
          val nextState = state.flatMap: stack =>
            if "([{".contains(char) then 
              Some(char :: stack) // Push inmutable
            else 
              // Pop inmutable si coincide el par, de lo contrario devuelve None
              Option.when(stack.nonEmpty && stack.head == pairs(char))(stack.tail)
          
          evaluate(tail, nextState)

    // Iniciamos la evaluación pasando los tokens limpios y el estado inicial (Some vacío)
    val finalState = evaluate(tokens, Some(Nil))
    
    // Es válido únicamente si el Option existe y la pila interna no tiene elementos pendientes
    finalState.contains(Nil)


// Punto de entrada moderno y limpio de Scala 3
@main def runFunctionalTests(): Unit =
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

  println("Todos los tests funcionales en Scala 3.3 pasaron correctamente.")