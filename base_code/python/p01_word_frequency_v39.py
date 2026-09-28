# P01: Word Frequency — Python 3.9 (codigo base para refactoring)
# Estilo: imperativo clasico, compatible con Python 3.9
from typing import List, Dict

def word_frequency(words: List[str]) -> Dict[str, int]:
    result = {}
    for word in words:
        if word in result:
            result[word] = result[word] + 1
        else:
            result[word] = 1
    return result

# Casos de prueba
assert word_frequency(["the", "fox", "the"]) == {"the": 2, "fox": 1}
assert word_frequency([]) == {}
assert word_frequency(["hello"]) == {"hello": 1}
