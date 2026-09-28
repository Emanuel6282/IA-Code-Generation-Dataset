"""
Unit Tests — P05: Transaction Pipeline
========================================
Especificacion de la funcion:
  process_transactions(transactions: list[dict]) -> float
  - transactions: lista de dicts con claves: id (int), amount (float), status (str), category (str)
  - Filtrar: status == "approved" AND amount > 0
  - Aplicar: fee del 10% (multiplicar por 0.9)
  - Retornar: suma total redondeada a 2 decimales
"""
import unittest, importlib.util, os, sys

def load_solution():
    path = os.environ.get("SOLUTION_PATH", os.path.join(os.path.dirname(__file__), "solution.py"))
    if not os.path.exists(path): raise FileNotFoundError(f"No se encontro solution.py en {path}")
    spec = importlib.util.spec_from_file_location("solution", path)
    m = importlib.util.module_from_spec(spec); spec.loader.exec_module(m); return m

class TestTransactionPipeline(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.sol = load_solution()
        if not hasattr(cls.sol, "process_transactions"): raise AttributeError("Falta 'process_transactions(transactions)'")
        cls.fn = staticmethod(cls.sol.process_transactions)

    def test_tc01_filter_and_fee(self):
        txs = [
            {"id": 1, "amount": 100.0, "status": "approved", "category": "income"},
            {"id": 2, "amount": -50.0, "status": "approved", "category": "income"},
            {"id": 3, "amount": 200.0, "status": "rejected", "category": "income"},
            {"id": 4, "amount": 150.0, "status": "approved", "category": "expense"},
        ]
        self.assertAlmostEqual(self.fn(txs), 225.0, places=2)

    def test_tc02_empty_list(self):
        self.assertAlmostEqual(self.fn([]), 0.0, places=2)

    def test_tc03_all_rejected(self):
        txs = [{"id": 1, "amount": 200.0, "status": "rejected", "category": "income"}]
        self.assertAlmostEqual(self.fn(txs), 0.0, places=2)

    def test_tc04_single_approved(self):
        txs = [{"id": 1, "amount": 1000.0, "status": "approved", "category": "income"}]
        self.assertAlmostEqual(self.fn(txs), 900.0, places=2)

    def test_tc05_two_approved(self):
        txs = [
            {"id": 1, "amount": 500.0, "status": "approved", "category": "income"},
            {"id": 2, "amount": 300.0, "status": "approved", "category": "expense"},
        ]
        self.assertAlmostEqual(self.fn(txs), 720.0, places=2)

if __name__ == "__main__":
    loader = unittest.TestLoader()
    suite = loader.loadTestsFromTestCase(TestTransactionPipeline)
    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(suite)
    total = result.testsRun; failed = len(result.failures) + len(result.errors); passed = total - failed
    print(f"\n--- RESULTADO FINAL ---\ntests_passed_ratio: {round(passed/total,2) if total else 0.0} ({passed}/{total})")
    sys.exit(0 if result.wasSuccessful() else 1)
