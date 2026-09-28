object MaxSubarraySum {

  def maxSubarraySum(arr: Array[Int]): Int = {
    require(arr != null && arr.nonEmpty, "El arreglo no puede ser null ni vacio")

    arr.tail.foldLeft((arr.head, arr.head)) { case ((currentMax, globalMax), x) =>
      val newCurrentMax = math.max(x, currentMax + x)
      val newGlobalMax = math.max(globalMax, newCurrentMax)
      (newCurrentMax, newGlobalMax)
    }._2
  }

  def main(args: Array[String]): Unit = {
    assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6, "Fallo caso 1")
    assert(maxSubarraySum(Array(-5, -2, -8, -1, -9)) == -1, "Fallo caso 2")
    assert(maxSubarraySum(Array(5)) == 5, "Fallo caso 3")

    println("Todos los tests pasaron correctamente.")
  }
}