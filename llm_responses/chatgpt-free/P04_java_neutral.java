import java.util.ArrayDeque;
import java.util.Deque;

public class BalancedParentheses {

    public static boolean isBalanced(String s) {
        Deque<Character> stack = new ArrayDeque<>();

        for (char ch : s.toCharArray()) {
            if (ch == '(' || ch == '[' || ch == '{') {
                stack.push(ch);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }

                char opening = stack.pop();

                if ((ch == ')' && opening != '(')
                        || (ch == ']' && opening != '[')
                        || (ch == '}' && opening != '{')) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        assert isBalanced("()") == true;
        assert isBalanced("([)]") == false;
        assert isBalanced("{[]}") == true;

        System.out.println("All tests passed");
    }
}