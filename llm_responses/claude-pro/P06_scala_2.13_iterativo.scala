import scala.collection.mutable

object MaxSubarray {

  /** Retorna la suma máxima de un subarreglo contiguo no vacío de `nums`.
    *
    * Usa el algoritmo de Kadane: O(n) en tiempo y O(1) en memoria. Las sumas se acumulan
    * en Long: la suma de varios Int puede superar el rango de Int (por ejemplo,
    * Int.MaxValue + Int.MaxValue) y con Int se desbordaría en silencio.
    *
    * @throws IllegalArgumentException si `nums` está vacío
    */
  def maxSubarraySum(nums: Array[Int]): Long = {
    require(nums.nonEmpty, "el arreglo debe tener al menos un elemento")

    // Se parte del primer elemento (no de 0) porque el subarreglo no puede ser vacío;
    // así, si todos son negativos, el resultado es el menos negativo.
    var best: Long = nums(0)
    var current: Long = nums(0)
    var i = 1
    while (i < nums.length) {
      val x: Long = nums(i)
      // La mejor suma que termina en x: o extiende el subarreglo anterior,
      // o empieza de nuevo en x si lo acumulado hasta ahora resta.
      if (current + x > x) current += x
      else current = x
      if (current > best) best = current
      i += 1
    }
    best
  }

  def main(args: Array[String]): Unit = {
    val testCases = mutable.ListBuffer.empty[(String, Array[Int], Long)]
    // Caso 1: ejemplo del enunciado ([4, -1, 2, 1])
    testCases += (("ejemplo del enunciado", Array(-2, 1, -3, 4, -1, 2, 1, -5, 4), 6L))
    // Caso 2: todos negativos -> el menos negativo
    testCases += (("todos negativos", Array(-3, -1, -2), -1L))
    testCases += (("un solo elemento negativo", Array(-5), -5L))
    // Caso 3: suma que supera el rango de Int (con Int se desbordaría a -2)
    testCases += (("suma mayor que Int", Array(Int.MaxValue, Int.MaxValue), 2L * Int.MaxValue))

    var i = 0
    while (i < testCases.length) {
      val (name, nums, expected) = testCases(i)
      val result = maxSubarraySum(nums)
      assert(result == expected,
        s"Caso '$name' ${nums.mkString("[", ", ", "]")}: esperado $expected, obtenido $result")
      i += 1
    }

    // Arreglo vacío -> IllegalArgumentException
    var threw = false
    try maxSubarraySum(Array.empty[Int])
    catch { case _: IllegalArgumentException => threw = true }
    assert(threw, "un arreglo vacío debería lanzar IllegalArgumentException")

    println("Todas las pruebas pasaron.")
  }
}