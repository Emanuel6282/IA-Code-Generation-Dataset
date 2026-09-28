// Scala 3.6 - Paradigma Iterativo Estricto
// Aprovecha las características modernas de sintaxis limpia de Scala 3 (quiet syntax), 
// nombrado top-level y directiva @main.

def binarySearch(arr: Array[Int], target: Int): Int =
  var left: Int = 0
  var right: Int = arr.length - 1
  var result: Int = -1

  while left <= right && result == -1 do
    val mid: Int = left + (right - left) / 2

    if arr(mid) == target then
      result = mid
    else if arr(mid) < target then
      left = mid + 1
    else
      right = mid - 1

  result

@main def run(): Unit =
  // --- Casos de prueba (Scala 3.6) ---

  // Caso 1: Elemento presente en el arreglo (ejemplo provisto)
  assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2, "Falló el Caso 1")

  // Caso 2: Elemento no presente en el arreglo (ejemplo provisto)
  assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1, "Falló el Caso 2")

  // Caso 3: Arreglo vacío y comprobación en bordes/extremos
  assert(binarySearch(Array(), 10) == -1, "Falló el Caso 3a (Arreglo vacío)")
  assert(binarySearch(Array(10, 20, 30, 40), 10) == 0, "Falló el Caso 3b (Primer elemento)")
  assert(binarySearch(Array(10, 20, 30, 40), 40) == 3, "Falló el Caso 3c (Último elemento)")

  println("¡Todos los casos de prueba pasaron correctamente!")