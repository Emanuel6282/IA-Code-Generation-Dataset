object MaxSubarray {

  /** Retorna la suma máxima de un subarreglo contiguo no vacío de `nums`.
    *
    * Algoritmo de Kadane en una pasada, O(n) en tiempo y O(1) en memoria:
    *  - scanLeft genera, para cada posición, la mejor suma de un subarreglo que termina
    *    ahí: o extiende el anterior (`current + x`) o empieza de nuevo en `x`. Parte del
    *    primer elemento (no de 0), así que nunca considera el subarreglo vacío y, si
    *    todos son negativos, el resultado es el menos negativo.
    *  - max se queda con la mayor de esas sumas.
    * Se recorre con un iterador, así que no se crean colecciones intermedias.
    *
    * Las sumas se acumulan en Long: la suma de varios Int puede superar el rango de Int
    * (por ejemplo, Int.MaxValue + Int.MaxValue) y con Int se desbordaría en silencio.
    *
    * @throws IllegalArgumentException si `nums` está vacío
    */
  def maxSubarraySum(nums: Seq[Int]): Long = {
    require(nums.nonEmpty, "el arreglo debe tener al menos un elemento")
    nums.iterator
      .drop(1)
      .scanLeft(nums.head.toLong)((current, x) => math.max(x.toLong, current + x))
      .max
  }

  def main(args: Array[String]): Unit = {
    // Caso 1: ejemplo del enunciado ([4, -1, 2, 1])
    assert(maxSubarraySum(Seq(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6)

    // Caso 2: todos negativos -> el menos negativo
    assert(maxSubarraySum(Seq(-3, -1, -2)) == -1)
    assert(maxSubarraySum(Seq(-5)) == -5)

    // Caso 3: suma que supera el rango de Int (con Int se desbordaría a -2)
    assert(maxSubarraySum(Seq(Int.MaxValue, Int.MaxValue)) == 2L * Int.MaxValue)

    // Arreglo vacío -> IllegalArgumentException
    assert(scala.util.Try(maxSubarraySum(Seq.empty)).failed.toOption.exists(_.isInstanceOf[IllegalArgumentException]),
      "un arreglo vacío debería lanzar IllegalArgumentException")

    println("Todas las pruebas pasaron.")
  }
}