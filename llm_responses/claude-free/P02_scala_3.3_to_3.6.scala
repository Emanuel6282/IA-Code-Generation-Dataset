// P02: Binary Search — Scala 3.6 (migrado desde Scala 2.13)
//
// CARACTERISTICAS NUEVAS/IDIOMATICAS USADAS EN ESTA MIGRACION:
// 1. Sintaxis por indentacion significativa ("quiet syntax", sin llaves {}):
//    def ... =, while ... do, if ... then. Consolidada desde Scala 3.0 y
//    vigente como estilo idiomatico en toda la serie 3.x, incluida 3.6.
// 2. Definiciones de nivel superior (top-level definitions): binarySearch
//    ya no necesita estar envuelta en un "object" explicito.
// 3. "@main": reemplaza "object { def main(args: Array[String]): Unit =
//    ... }" por una anotacion sobre una funcion de nivel superior, que
//    genera el punto de entrada del programa de forma mas concisa.
// 4. Nota de honestidad sobre novedades especificas de 3.6: revise el
//    changelog de esta version (rama posterior a la LTS 3.3): sus cambios
//    se concentran en refinamientos del *safe initialization checker*,
//    ajustes de inferencia de tipos y mejoras internas de rendimiento del
//    compilador, sin nueva sintaxis de superficie que aplique a un
//    algoritmo de busqueda binaria sobre un arreglo primitivo. Por ello,
//    las caracteristicas "nuevas" aprovechadas aqui son las de Scala 3 en
//    general (indentacion significativa, top-level defs, @main), que
//    siguen siendo el estilo idiomatico vigente en 3.6.
// 5. Nota de honestidad sobre el paradigma: el codigo base es
//    deliberadamente imperativo (var left/right + while + "return mid")
//    "para forzar el desafio recursivo en el refactor", pero este ejercicio
//    pide una migracion de version con comportamiento identico, no un
//    cambio de paradigma. Por eso se conserva intencionalmente el estilo
//    imperativo con var/while, incluyendo el "return mid" tal cual (aunque
//    "return" dentro de una funcion es un antipatron poco idiomatico en
//    Scala por su semantica de non-local return), ya que alterarlo seria un
//    cambio de logica/estilo que excede el alcance de una simple migracion
//    de version con "comportamiento identico".

def binarySearch(arr: Array[Int], target: Int): Int =
  var left = 0
  var right = arr.length - 1
  while left <= right do
    val mid = left + (right - left) / 2
    if arr(mid) == target then return mid
    else if arr(mid) < target then left = mid + 1
    else right = mid - 1
  -1

@main def runTests(): Unit =
  assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)
  assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)
  assert(binarySearch(Array.empty[Int], 1) == -1)
  println("All tests passed")

// CAMBIOS REALIZADOS Y JUSTIFICACION:
// 1. Se elimino el "object P02BinarySearch_v213 { ... }" que envolvia todo
//    el codigo, aprovechando las definiciones de nivel superior de Scala 3.
//    Reduce anidamiento e indentacion innecesaria sin alterar el
//    comportamiento.
// 2. Se convirtio la sintaxis con llaves a sintaxis por indentacion
//    significativa: "def binarySearch(...) = { ... }" -> sin llaves,
//    "while (left <= right) { ... }" -> "while left <= right do ...",
//    "if (arr(mid) == target) return mid else if (...) ... else ..." ->
//    "if arr(mid) == target then return mid else if ... then ... else
//    ...". Estilo idiomatico vigente en Scala 3.6.
// 3. Se reemplazo "def main(args: Array[String]): Unit = { ... }" dentro
//    del object por "@main def runTests(): Unit = ..." a nivel superior.
// 4. La logica interna se mantuvo exactamente igual: mismas variables
//    mutables left/right, mismo bucle while, mismo calculo de mid con
//    proteccion contra overflow, y el mismo "return mid" como mecanismo de
//    salida anticipada — cumpliendo el requisito de comportamiento
//    identico y respetando la intencion original del codigo base
//    (deliberadamente imperativo).
// 5. El nombre de la funcion "binarySearch" y los tres casos de prueba con
//    "assert" se conservaron sin cambios logicos, ya que ya eran validos y
//    compatibles en Scala 3.6 tal como estaban escritos en 2.13.