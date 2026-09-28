"""
Unit Tests — P06: Maximum Subarray Sum (Kadane's Algorithm)
============================================================
"""
import unittest, importlib.util, os, sys

def load_solution():
    path = os.environ.get("SOLUTION_PATH", os.path.join(os.path.dirname(__file__), "solution.py"))
    if not os.path.exists(path): raise FileNotFoundError(f"No se encontro solution.py en {path}")
    spec = importlib.util.spec_from_file_location("solution", path)
    m = importlib.util.module_from_spec(spec); spec.loader.exec_module(m); return m

class TestMaxSubarraySum(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.sol = load_solution()
        if not hasattr(cls.sol, "max_subarray_sum"): raise AttributeError("Falta 'max_subarray_sum(arr)'")
        cls.fn = staticmethod(cls.sol.max_subarray_sum)

    def test_tc01_classic_kadane(self):
        self.assertEqual(self.fn([-2,1,-3,4,-1,2,1,-5,4]), 6)

    def test_tc02_single_positive(self):
        self.assertEqual(self.fn([1]), 1)

    def test_tc03_single_negative(self):
        self.assertEqual(self.fn([-1]), -1)

    def test_tc04_mostly_positive(self):
        self.assertEqual(self.fn([5,4,-1,7,8]), 23)

    def test_tc05_all_negative(self):
        self.assertEqual(self.fn([-2,-1]), -1)

    def test_tc06_all_positive(self):
        self.assertEqual(self.fn([1,2,3,4,5]), 15)

    def test_tc07_middle_subarray(self):
        self.assertEqual(self.fn([-3,2,-1,4,-2]), 5)

if __name__ == "__main__":
    loader = unittest.TestLoader()
    suite = loader.loadTestsFromTestCase(TestMaxSubarraySum)
    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(suite)
    total = result.testsRun; failed = len(result.failures) + len(result.errors); passed = total - failed
    print(f"\n--- RESULTADO FINAL ---\ntests_passed_ratio: {round(passed/total,2) if total else 0.0} ({passed}/{total})")
    sys.exit(0 if result.wasSuccessful() else 1)
