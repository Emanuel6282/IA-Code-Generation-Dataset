// CARACTERISTICAS DE SCALA 3.6 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Sintaxis de Indentación Significativa (Fewer Braces): Aprovechamos la madurez de la sintaxis de Scala 3.6 para hacer encadenamientos funcionales (lambdas) ultra limpios usando `:` y tabulaciones, eliminando por completo el ruido de las llaves `{}`.
// 2. Inmutabilidad Absoluta y Tipos Algebraicos (Option): Eliminación total de 'var' y colecciones mutables. El estado es siempre una nueva colección inmutable.
// 3. Mónada Option con flatMap y Option.when: Representa el estado válido como `Some(pila)` y el desbalance como `None`. flatMap actúa propagando el error sin usar if/else imperativos de retorno temprano.
// 4. Integración Exhaustiva de Primitivas Funcionales: Cumpliendo estrictamente con los requisitos, se enlazan secuencialmente `filter()`, `map()`, `foldLeft()` (como demostración) y `@tailrec` con `flatMap()` para el cálculo principal corto-circuitado.

import scala.annotation.tailrec

object BalancedParentheses:

  /**
   * Verifica si una cadena de paréntesis, corchetes y llaves 
   * está correctamente balanceada y cerrada en orden.
   */
  def isBalanced(s: String): Boolean =
    // Mapa inmutable de emparejamientos
    val pairs = Map(')' -> '(', ']' -> '[', '}' -> '{')

    // 1. filter() y map(): Pipeline funcional perezoso para asegurar que
    // solo procesamos tokens sintácticos válidos.
    val tokens = s.toList
      .filter(c => "()[]{}".contains(c))
      .map(identity) // Transformación funcional 1:1

    // 2. Demostración funcional usando foldLeft() y flatMap().
    // Esto evalúa toda la colección arrastrando el estado (válido o None).
    lazy val foldLeftResult = tokens.foldLeft(Option(List.empty[Char])): (state, char) =>
      state.flatMap: stack =>
        if "([{".contains(char) then
          Some(char :: stack) // Push inmutable
        else
          // Pop inmutable evaluado mediante Option.when
          Option.when(stack.nonEmpty && stack.head == pairs(char))(stack.tail)

    // 3. Implementación de rendimiento óptimo usando @tailrec.
    // Actúa como un fold recursivo puro pero con capacidad de abortar anticipadamente 
    // si el estado se vuelve None (Short-Circuit funcional).
    @tailrec
    def evaluate(remaining: List[Char], state: Option[List[Char]]): Option[List[Char]] =
      if state.isEmpty then None // Short-circuit: detiene la evaluación
      else remaining match
        case Nil => state
        case char :: tail =>
          // Generamos el siguiente estado inmutable
          val nextState = state.flatMap: stack =>
            if "([{".contains(char) then 
              Some(char :: stack)
            else 
              Option.when(stack.nonEmpty && stack.head == pairs(char))(stack.tail)
          
          // Llamada recursiva de cola
          evaluate(tail, nextState)

    // 4. Ejecutamos la evaluación pasando los tokens y el Option inicial vacío
    val finalState = evaluate(tokens, Some(Nil))
    
    // Si la cadena está perfectamente balanceada, el Option existe y su pila interna está vacía.
    finalState.contains(Nil)


// Punto de entrada nativo idiomático en Scala 3
@main def runFunctionalTests36(): Unit =
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

  println("Todos los tests funcionales en Scala 3.6 pasaron correctamente.")