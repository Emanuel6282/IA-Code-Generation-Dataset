import scala.annotation.tailrec

object MaxSubarraySum {

private case class State(
currentSum: Int,
maxSum: Int
)

@tailrec
private def toList(
numbers: Array[Int],
index: Int,
result: List[Int]
): List[Int] = {
if (index >= numbers.length) {
result.reverse
} else {
toList(
numbers,
index + 1,
numbers(index) :: result
)
}
}

def maxSubarraySum(numbers: Array[Int]): Int = {
val values = toList(numbers, 0, Nil)
.filter(_ => true)
.map(identity)
.flatMap(number => List(number))

values.headOption
  .map { first =>
    values.tail.foldLeft(State(first, first)) {
      (state, number) =>
        val newCurrentSum =
          math.max(number, state.currentSum + number)

        val newMaxSum =
          math.max(state.maxSum, newCurrentSum)

        State(newCurrentSum, newMaxSum)
    }.maxSum
  }
  .getOrElse(
    throw new IllegalArgumentException(
      "El arreglo no puede estar vacío"
    )
  )

}

def main(args: Array[String]): Unit = {
assert(
maxSubarraySum(
Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)
) == 6
)

assert(
  maxSubarraySum(
    Array(1)
  ) == 1
)

assert(
  maxSubarraySum(
    Array(-2, -1)
  ) == -1
)

println("All tests passed")

}
}
