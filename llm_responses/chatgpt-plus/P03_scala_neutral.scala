import scala.annotation.tailrec

object FlattenNestedList {

  def flatten(nested: List[Any]): List[Int] = {

    @tailrec
    def process(
        pending: List[Any],
        result: List[Int]
    ): List[Int] = {

      pending match {
        case Nil =>
          result.reverse

        case (sublist: List[_]) :: rest =>
          process(
            sublist.asInstanceOf[List[Any]] ::: rest,
            result
          )

        case (number: Int) :: rest =>
          process(rest, number :: result)

        case _ :: rest =>
          process(rest, result)
      }
    }

    process(nested, Nil)
  }

  def main(args: Array[String]): Unit = {

    // Test 1: Lista con varios niveles de anidamiento
    assert(
      flatten(List(1, List(2, 3), List(4, List(5, 6)))) ==
        List(1, 2, 3, 4, 5, 6)
    )

    // Test 2: Lista vacía
    assert(
      flatten(List.empty[Any]) == List.empty[Int]
    )

    // Test 3: Lista con anidamiento profundo
    assert(
      flatten(List(1, List(List(2)), List.empty[Int],
        List(3, List(4, List(5))))) ==
        List(1, 2, 3, 4, 5)
    )

    println("All tests passed")
  }
}