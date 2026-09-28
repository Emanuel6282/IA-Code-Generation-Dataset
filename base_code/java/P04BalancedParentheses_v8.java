// P04: Balanced Parentheses — Java 8 (codigo base para refactoring)
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

public class P04BalancedParentheses_v8 {

    public static boolean isBalanced(String s) {
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> pairs = new HashMap<>();
        pairs.put(')', '(');
        pairs.put(']', '[');
        pairs.put('}', '{');
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (pairs.containsKey(c)) {
                if (stack.isEmpty() || stack.peek() != pairs.get(c)) return false;
                stack.pop();
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        assert isBalanced("()") == true;
        assert isBalanced("([)]") == false;
        assert isBalanced("") == true;
        System.out.println("All tests passed");
    }
}
