def maxSubarraySum(arr: Array[Int]): Int =
  require(arr != null && arr.nonEmpty, "El arreglo no puede ser null ni vacio")

  var currentMax = arr(0)
  var globalMax = arr(0)

  var i = 1
  while i < arr.length do
    val x = arr(i)
    currentMax = if currentMax + x > x then currentMax + x else x
    if currentMax > globalMax then
      globalMax = currentMax
    i += 1

  globalMax

@main def runTests(): Unit =
  assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6, "Fallo caso 1")
  assert(maxSubarraySum(Array(-5, -2, -8, -1, -9)) == -1, "Fallo caso 2")
  assert(maxSubarraySum(Array(5)) == 5, "Fallo caso 3")

  println("Todos los tests pasaron correctamente.")