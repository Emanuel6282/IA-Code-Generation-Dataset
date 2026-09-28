# P05: Transaction Pipeline — Python 3.9 (codigo base para refactoring)
from typing import List, Dict, Any

def process_transactions(transactions: List[Dict[str, Any]]) -> float:
    total = 0.0
    for t in transactions:
        if t["status"] == "approved" and t["amount"] > 0:
            total = total + t["amount"] * 0.9
    return round(total, 2)

assert process_transactions([]) == 0.0
assert process_transactions([{"id":1,"amount":1000.0,"status":"approved","category":"x"}]) == 900.0
assert process_transactions([{"id":1,"amount":100.0,"status":"rejected","category":"x"}]) == 0.0
