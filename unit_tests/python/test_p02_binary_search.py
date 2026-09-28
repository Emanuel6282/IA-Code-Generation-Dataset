"""
Unit Tests — P02: Binary Search
=================================
"""
import unittest, importlib.util, os, sys

def load_solution():
    path = os.environ.get("SOLUTION_PATH", os.path.join(os.path.dirname(__file__), "solution.py"))
    if not os.path.exists(path): raise FileNotFoundError(f"No se encontro solution.py en {path}")
    spec = importlib.util.spec_from_file_location("solution", path)
    m = importlib.util.module_from_spec(spec); spec.loader.exec_module(m); return m

class TestBinarySearch(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.sol = load_solution()
        if not hasattr(cls.sol, "binary_search"): raise AttributeError("Falta funcion 'binary_search(arr, target)'")
        cls.fn = staticmethod(cls.sol.binary_search)

    def test_tc01_target_in_middle(self):
        self.assertEqual(self.fn([1,3,5,7,9], 5), 2)

    def test_tc02_target_at_start(self):
        self.assertEqual(self.fn([1,3,5,7,9], 1), 0)

    def test_tc03_target_at_end(self):
        self.assertEqual(self.fn([1,3,5,7,9], 9), 4)

    def test_tc04_target_not_found(self):
        self.assertEqual(self.fn([1,3,5,7,9], 4), -1)

    def test_tc05_empty_array(self):
        self.assertEqual(self.fn([], 1), -1)

    def test_tc06_single_element_found(self):
        self.assertEqual(self.fn([1], 1), 0)

    def test_tc07_even_length_array(self):
        self.assertEqual(self.fn([2,4,6,8,10,12], 10), 4)

if __name__ == "__main__":
    loader = unittest.TestLoader()
    suite = loader.loadTestsFromTestCase(TestBinarySearch)
    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(suite)
    total = result.testsRun; failed = len(result.failures) + len(result.errors); passed = total - failed
    print(f"\n--- RESULTADO FINAL ---\ntests_passed_ratio: {round(passed/total,2) if total else 0.0} ({passed}/{total})")
    sys.exit(0 if result.wasSuccessful() else 1)
