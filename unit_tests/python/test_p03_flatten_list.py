"""
Unit Tests — P03: Flatten Nested List
=======================================
"""
import unittest, importlib.util, os, sys

def load_solution():
    path = os.environ.get("SOLUTION_PATH", os.path.join(os.path.dirname(__file__), "solution.py"))
    if not os.path.exists(path): raise FileNotFoundError(f"No se encontro solution.py en {path}")
    spec = importlib.util.spec_from_file_location("solution", path)
    m = importlib.util.module_from_spec(spec); spec.loader.exec_module(m); return m

class TestFlattenList(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.sol = load_solution()
        if not hasattr(cls.sol, "flatten"): raise AttributeError("Falta funcion 'flatten(nested)'")
        cls.fn = staticmethod(cls.sol.flatten

    def test_tc01_basic_nesting(self):
        self.assertEqual(self.fn([1,[2,3],[4,[5,6]]]), [1,2,3,4,5,6])

    def test_tc02_empty_list(self):
        self.assertEqual(self.fn([]), [])

    def test_tc03_already_flat(self):
        self.assertEqual(self.fn([1,2,3]), [1,2,3])

    def test_tc04_deeply_nested(self):
        self.assertEqual(self.fn([[1,[2,[3,[4]]]]]), [1,2,3,4])

    def test_tc05_nested_empty(self):
        self.assertEqual(self.fn([[]]), [])

    def test_tc06_mixed_empty_sublists(self):
        self.assertEqual(self.fn([1,[],2,[3,[]]]), [1,2,3])

if __name__ == "__main__":
    loader = unittest.TestLoader()
    suite = loader.loadTestsFromTestCase(TestFlattenList)
    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(suite)
    total = result.testsRun; failed = len(result.failures) + len(result.errors); passed = total - failed
    print(f"\n--- RESULTADO FINAL ---\ntests_passed_ratio: {round(passed/total,2) if total else 0.0} ({passed}/{total})")
    sys.exit(0 if result.wasSuccessful() else 1)
