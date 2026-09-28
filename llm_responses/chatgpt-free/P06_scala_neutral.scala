object MaxSubarraySum:

def maxSubarraySum(numbers: Array[Int]): Int =
var currentSum = numbers(0)
var maxSum = numbers(0)

for i <- 1 until numbers.length do
  currentSum = math.max(
    numbers(i),
    currentSum + numbers(i)
  )

  maxSum = math.max(
    maxSum,
    currentSum
  )

maxSum

def main(args: Array[String]): Unit =
assert(
maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6
)

assert(
  maxSubarraySum(Array(-5, -2, -8, -1)) == -1
)

assert(
  maxSubarraySum(Array(1, 2, 3, 4)) == 10
)

println("All tests passed")
