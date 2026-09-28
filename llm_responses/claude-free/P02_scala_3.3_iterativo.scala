// P02: Binary Search — Scala 3.3 (paradigma iterativo)
// Solo biblioteca estandar de Scala. Sintaxis "significant indentation" de Scala 3.

def binarySearch(arr: Array[Int], target: Int): Int =
  var left: Int = 0
  var right: Int = arr.length - 1
  var resultado: Int = -1
  var encontrado: Boolean = false

  while left <= right && !encontrado do
    val mid = left + (right - left) / 2
    if arr(mid) == target then
      resultado = mid
      encontrado = true
    else if arr(mid) < target then
      left = mid + 1
    else
      right = mid - 1

  resultado

@main def runTests(): Unit =
  // Caso 1: ejemplo del enunciado, objetivo presente en el medio
  assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)

  // Caso 2: ejemplo del enunciado, objetivo ausente
  assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)

  // Caso 3: arreglo vacio -> siempre -1
  assert(binarySearch(Array.empty[Int], 1) == -1)

  println("Todos los casos de prueba pasaron correctamente.")