// CARACTERISTICAS DE SCALA 3.3 Y PARADIGMA ITERATIVO UTILIZADAS:
// 1. Sintaxis sin llaves (Braceless Syntax - Opcional): Scala 3 introduce la indentación significativa. Se eliminan las llaves `{}` tradicionales, reemplazándolas por un estilo visualmente más limpio similar a Python, ideal para el seguimiento iterativo de variables.
// 2. Control de flujo moderno (`while ... do` e `if ... then`): Las estructuras imperativas clásicas han sido rediseñadas sintácticamente para prescindir de los paréntesis alrededor de las condiciones, mejorando la legibilidad del código procedimental.
// 3. Definiciones de Nivel Superior (Top-level definitions) y `@main`: Ya no es necesario empaquetar el código dentro de un objeto `Singleton` con un método `main`. Scala 3.3 permite declarar funciones globales directamente y designar el punto de entrada a la JVM mediante la anotación `@main`.
// 4. Paradigma iterativo estricto: Uso explícito de `var` y el bucle de bajo nivel `while ... do` para gestionar la mutación local, evitando cualquier abstracción funcional.

def maxSubarraySum(arr: Array[Int]): Int =
  require(arr != null && arr.nonEmpty, "El arreglo debe tener al menos 1 elemento.")

  var currentSum = arr(0)
  var maxSum = arr(0)
  var i = 1

  // Bucle iterativo de bajo nivel con la nueva sintaxis de Scala 3.3
  while i < arr.length do
    
    // Bifurcación imperativa explícita (Kadane)
    if currentSum + arr(i) > arr(i) then
      currentSum = currentSum + arr(i)
    else
      currentSum = arr(i)

    // Actualización del máximo global
    if currentSum > maxSum then
      maxSum = currentSum

    // Incremento manual del índice
    i += 1

  maxSum


@main def runIterativeTests(): Unit =
  // ==========================================
  // CASOS DE PRUEBA
  // ==========================================

  // 1. Caso base del ejemplo: mezcla de positivos y negativos
  assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6, "Error: Caso base esperado 6")

  // 2. Caso con todos los elementos negativos (retorna el menos negativo)
  assert(maxSubarraySum(Array(-5, -2, -9, -1, -4)) == -1, "Error: Arreglo de negativos esperado -1")

  // 3. Caso con todos los elementos positivos (retorna la suma total)
  assert(maxSubarraySum(Array(1, 2, 3, 4, 5)) == 15, "Error: Arreglo de positivos esperado 15")

  println("Todos los tests iterativos en Scala 3.3 pasaron correctamente.")