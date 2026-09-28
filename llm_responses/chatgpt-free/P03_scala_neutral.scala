def flatten(nested: List[Any]): List[Int] = {
  nested.flatMap {
    case value: Int   => List(value)
    case list: List[?] => flatten(list)
  }
}

// Casos de prueba
assert(flatten(List(1, List(2, 3), List(4, List(5, 6)))) == List(1, 2, 3, 4, 5, 6))
assert(flatten(List.empty) == List.empty)
assert(flatten(List(1, 2, 3)) == List(1, 2, 3))