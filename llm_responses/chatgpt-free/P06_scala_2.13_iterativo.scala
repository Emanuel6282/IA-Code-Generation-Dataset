// Max Subarray Sum — Scala 2.13
// Paradigma: estrictamente iterativo
// Algoritmo: Kadane
//
// Se utilizan var y while para mantener el estado mutable.
// No se utilizan map(), filter(), foldLeft() ni recursión.
//
// Complejidad: O(n) tiempo y O(1) espacio adicional.

object MaxSubarraySum {

def maxSubarraySum(numbers: Array[Int]): Int = {
var currentSum = numbers(0)
var maxSum = numbers(0)

var i = 1

while (i < numbers.length) {
  currentSum = math.max(
    numbers(i),
    currentSum + numbers(i)
  )

  maxSum = math.max(
    maxSum,
    currentSum
  )

  i += 1
}

maxSum

}

def main(args: Array[String]): Unit = {
assert(
maxSubarraySum(
Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)
) == 6
)

assert(
  maxSubarraySum(
    Array(-5, -2, -8, -1)
  ) == -1
)

assert(
  maxSubarraySum(
    Array(1, 2, 3, 4)
  ) == 10
)

println("All tests passed")

}
}
