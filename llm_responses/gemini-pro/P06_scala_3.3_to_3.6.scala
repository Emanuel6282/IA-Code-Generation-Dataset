// CARACTERISTICAS DE SCALA 3.6 UTILIZADAS Y POR QUE:
// 1. Definiciones de Nivel Superior (Top-level) y @main: Scala 3.6 consolida la eliminación del "boilerplate" orientado a objetos. Las funciones y el punto de entrada a la JVM se declaran libremente sin necesidad de encapsularlos en un `object` Singleton.
// 2. Sintaxis Basada en Indentación (Braceless Syntax): Se eliminan las llaves en favor de la indentación significativa. Esto reduce la carga visual drásticamente, acercando la ergonomía de lectura a lenguajes como Python.
// 3. Trailing Lambdas y Desestructuración Nativa: Para resolver el desafío funcional del código base, se migra la mutación de estado a un plegado inmutable (`foldLeft`). Se usa la sintaxis `:` para abrir el bloque funcional de forma limpia, desempaquetando la tupla de estado `(currentMax, globalMax)` in-place.

def maxSubarraySum(arr: Array[Int]): Int =
  require(arr != null && arr.nonEmpty, "El arreglo debe contener al menos un elemento")

  // Estado inicial inmutable empaquetado en una tupla nativa
  val initialState = (arr.head, arr.head)

  // Propagación pura del estado usando foldLeft y un trailing lambda block
  val (_, finalGlobalMax) = arr.tail.foldLeft(initialState): (acc, curr) =>
    val (currentMax, globalMax) = acc
    val newCurrentMax = math.max(curr, currentMax + curr)
    val newGlobalMax = math.max(globalMax, newCurrentMax)
    (newCurrentMax, newGlobalMax)

  finalGlobalMax


@main def runRefactoredTests36(): Unit =
  assert(maxSubarraySum(Array(-2, 1, -3, 4, -1, 2, 1, -5, 4)) == 6)
  assert(maxSubarraySum(Array(1)) == 1)
  assert(maxSubarraySum(Array(-2, -1)) == -1)
  assert(maxSubarraySum(Array(5, 4, -1, 7, 8)) == 23)
  println("All tests passed")


/*
EXPLICACION DE CAMBIOS E IDIOMATISMOS EN SCALA 3.6:

1. **Resolución del Desafío Funcional y Tuplas Nativas**:
   El código base en Scala 2.13 operaba puramente bajo un paradigma imperativo con mutación de variables locales (`var`) e iteradores (`while`). Al refactorizar el código, el estado se eleva a una tupla `(Int, Int)` gestionada por `foldLeft`. A diferencia de los modelos analizados en Java donde requerías estructuras auxiliares explícitas para transportar múltiples valores, Scala 3.6 desestructura y propaga estas tuplas nativas de forma matemática, garantizando inmutabilidad y un altísimo rendimiento gracias a las optimizaciones del compilador.

2. **Estabilidad Sintáctica en la Rama 3.x**:
   Esta migración representa una pieza clave para completar los datasets comparativos en tu página web de recolección de prompts. Al alinear este resultado de Scala 3.6 con tus pruebas anteriores en Scala 3.3, observarás que la sintaxis funcional y estructural se mantiene intacta. Para el libro de tu trabajo final de grado en la FP-UNA, esto te permite concluir que las características disruptivas introducidas al inicio de Scala 3 (como las "trailing lambdas" y la sintaxis "braceless") han madurado y se consideran hoy el estándar idiomático definitivo.

3. **Independencia del Paradigma Orientado a Objetos**:
   Se eliminó por completo el `object P06MaxSubarray_v213`. En el desarrollo de software moderno con Scala 3.6, forzar un contenedor estático para la lógica funcional pura se considera un antipatrón heredado. La función `maxSubarraySum` existe ahora de forma autónoma, reforzando la filosofía de tratar las transformaciones de datos como componentes independientes y componibles.
*/