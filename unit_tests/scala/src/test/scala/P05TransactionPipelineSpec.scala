import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
 * Unit Tests - P05: Transaction Pipeline
 * La IA debe implementar:
 *   def processTransactions(transactions: List[Map[String, Any]]): Double
 *
 * Reglas:
 *   - Filtrar: status == "approved" AND amount > 0
 *   - Aplicar: fee 10% (amount * 0.9)
 *   - Retornar: suma total redondeada a 2 decimales
 */
class P05TransactionPipelineSpec extends AnyFunSuite with Matchers {

  def tx(id: Int, amount: Double, status: String, cat: String): Map[String,Any] =
    Map("id"->id, "amount"->amount, "status"->status, "category"->cat)

  test("TC01 - Filtrar negativo y rechazado, aplicar fee") {
    val txs = List(
      tx(1,100.0,"approved","income"),
      tx(2,-50.0,"approved","income"),
      tx(3,200.0,"rejected","income"),
      tx(4,150.0,"approved","expense")
    )
    Solution.processTransactions(txs) shouldBe (225.0 +- 0.01)
  }

  test("TC02 - Lista vacia") {
    Solution.processTransactions(List.empty) shouldBe (0.0 +- 0.01)
  }

  test("TC03 - Todos rechazados") {
    Solution.processTransactions(List(tx(1,200.0,"rejected","income"))) shouldBe (0.0 +- 0.01)
  }

  test("TC04 - Un approved: 1000*0.9=900") {
    Solution.processTransactions(List(tx(1,1000.0,"approved","income"))) shouldBe (900.0 +- 0.01)
  }

  test("TC05 - Dos approved: (500+300)*0.9=720") {
    val txs = List(tx(1,500.0,"approved","income"), tx(2,300.0,"approved","expense"))
    Solution.processTransactions(txs) shouldBe (720.0 +- 0.01)
  }
}
