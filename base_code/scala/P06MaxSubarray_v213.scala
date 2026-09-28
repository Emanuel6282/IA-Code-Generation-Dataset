// P06: Max Subarray Sum (Kadane) — Scala 2.13 (codigo base para refactoring)
// Estilo: actualizacion in-place de variables mutables (currentMax, globalMax)
// El desafio funcional en el refactor es usar foldLeft con tupla acumuladora

object P06MaxSubarray_v213 {

  def maxSubarraySum(arr: Array[Int]): Int = {
    var currentMax = arr(0)
    var globalMax = arr(0)
    var i = 1
    while (i < arr.length) {
      if (currentMax + arr(i) > arr(i)) {
        currentMax = currentMax + arr(i)
      } else {
        currentMax = arr(i)
      }
      if (currentMax > globalMax) {
        globalMax = currentMax
      }
      i += 1
    }
    globalMax
  }

  def main(args: Array[String]): Unit = {
    assert(maxSubarraySum(Array(-2,1,-3,4,-1,2,1,-5,4)) == 6)
    assert(maxSubarraySum(Array(1)) == 1)
    assert(maxSubarraySum(Array(-2,-1)) == -1)
    assert(maxSubarraySum(Array(5,4,-1,7,8)) == 23)
    println("All tests passed")
  }
}
