def maxSubarraySum(arr: Array[Int]): Int =
  require(arr.nonEmpty, "El arreglo debe tener al menos 1 elemento.")
  
  var currentMax = arr(0)
  var globalMax = arr(0)
  
  for i <- 1 until arr.length do
    // Decide si extender el subarreglo actual o comenzar uno nuevo
    currentMax = math.max(arr(i), currentMax + arr(i))
    // Actualiza el máximo global si el actual lo supera
    globalMax = math.max(globalMax, currentMax)
    
  globalMax


@main def runTests(): Unit =
  // ==========================================
  // CASOS DE PRUEBA
  // ==========================================

  // 1. Caso base del ejemplo: mezcla de positivos y negativos
  assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6, "Error en el caso base")

  // 2. Caso con todos los elementos negativos (retorna el menos negativo)
  assert(maxSubarraySum(Array(-5, -2, -9, -1, -4)) == -1, "Error en el caso de todos negativos")

  // 3. Caso con todos los elementos positivos (retorna la suma total)
  assert(maxSubarraySum(Array(1, 2, 3, 4, 5)) == 15, "Error en el caso de todos positivos")

  println("Todos los casos de prueba pasaron correctamente.")