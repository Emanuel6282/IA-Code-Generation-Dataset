// CARACTERISTICAS DE SCALA 3.6 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Estabilidad Sintáctica (Braceless): Scala 3.6 mantiene y refina la sintaxis sin llaves basada en indentación. El uso de `while ... do` e `if ... then` se consolida como la forma definitiva de escribir código procedimental moderno en la JVM, emulando la limpieza visual de Python pero con tipado estático fuerte.
// 2. Compilador y Estrictez (Linting integrado): Las versiones recientes de la rama 3.x (incluyendo 3.6) mejoran los diagnósticos del compilador respecto a la mutabilidad. Al utilizar `var` deliberadamente en un entorno que favorece la inmutabilidad, el compilador aísla perfectamente este bloque de efectos locales, optimizándolo para iteraciones ultra-rápidas sobre arreglos primitivos.
// 3. Paradigma Imperativo Puro: Gestión manual del estado a través de mutación de variables y saltos condicionales explícitos, garantizando un rendimiento $O(n)$ sin el overhead de instanciar iteradores o tuplas.

def maxSubarraySum(arr: Array[Int]): Int =
  require(arr != null && arr.nonEmpty, "El arreglo debe tener al menos 1 elemento.")

  var currentSum = arr(0)
  var maxSum = arr(0)
  var i = 1

  // Bucle de bajo nivel utilizando indentación significativa pura
  while i < arr.length do
    
    // Decisión algorítmica de Kadane sin azucar sintáctico funcional
    if currentSum + arr(i) > arr(i) then
      currentSum = currentSum + arr(i)
    else
      currentSum = arr(i)

    // Actualización imperativa del acumulador global
    if currentSum > maxSum then
      maxSum = currentSum

    // Gestión manual del índice
    i += 1

  maxSum


@main def runIterativeTests36(): Unit =
  // ==========================================
  // CASOS DE PRUEBA
  // ==========================================

  // 1. Caso base del ejemplo: mezcla de positivos y negativos
  assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6, "Error: Caso base esperado 6")

  // 2. Caso con todos los elementos negativos (retorna el menos negativo)
  assert(maxSubarraySum(Array(-5, -2, -9, -1, -4)) == -1, "Error: Arreglo de negativos esperado -1")

  // 3. Caso con todos los elementos positivos (retorna la suma total)
  assert(maxSubarraySum(Array(1, 2, 3, 4, 5)) == 15, "Error: Arreglo de positivos esperado 15")

  println("Todos los tests iterativos en Scala 3.6 pasaron correctamente.")