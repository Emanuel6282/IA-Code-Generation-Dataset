import scala.annotation.tailrec

object BinarySearchExample {

  /**
   * Busca un valor objetivo en un arreglo de enteros ordenado ascendentemente.
   * Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
   */
  def binarySearch(arr: Array[Int], target: Int): Int = {
    
    // Función auxiliar recursiva de cola
    @tailrec
    def search(left: Int, right: Int): Int = {
      if (left > right) {
        -1 // Condición base: no se encontró el elemento
      } else {
        // Calculamos el punto medio de forma segura contra desbordamientos (overflow)
        val mid = left + (right - left) / 2
        
        if (arr(mid) == target) {
          mid // Elemento encontrado
        } else if (arr(mid) < target) {
          search(mid + 1, right) // Descartar mitad izquierda
        } else {
          search(left, mid - 1) // Descartar mitad derecha
        }
      }
    }

    // Invocación inicial
    search(0, arr.length - 1)
  }

  def main(args: Array[String]): Unit = {
    // ==========================================
    // CASOS DE PRUEBA
    // ==========================================
    
    // 1. Caso del ejemplo: el elemento existe en el arreglo
    assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2, "Fallo: el objetivo 5 debería estar en el índice 2")
    
    // 2. Caso del ejemplo: el elemento no existe en el arreglo
    assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1, "Fallo: el objetivo 4 no está, debería retornar -1")
    
    // 3. Caso límite: el arreglo está vacío
    assert(binarySearch(Array.empty[Int], 5) == -1, "Fallo: en un arreglo vacío debería retornar -1")
    
    println("Todos los casos de prueba pasaron correctamente.")
  }
}