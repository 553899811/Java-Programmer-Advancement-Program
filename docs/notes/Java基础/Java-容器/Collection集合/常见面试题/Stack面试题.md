```
  1.用两个栈来模拟一个队列(FIFO,先进先出原则),完成队列的push和pop操作;
  
import java.util.NoSuchElementException;
public class Solution<T> {
    Stack<T> t1 = new Stack<T>();
    Stack<T> t2 = new Stack<>();

    public void push(T data) {
        t1.push(data);
    }

    public T pop() {
        if (t1.isEmpty() && t2.isEmpty()) {
            throw new NoSuchElementException("队列中无元素");
        }
        if (t2.isEmpty()) {
            while (!t1.isEmpty()) {
                t2.push(t1.pop());
            }
        }
        return t2.pop();
    }
}
```
```
 2.设有一个栈，元素依次进栈的顺序是A,B,C,D,E。下列不可能的出栈顺序有?
 A:ABCDE
 B:BCDEA
 C:EABCD
 D:EDCBA
 
 解析: 正确理解题意是关键,在一边push的时候可以一边pop();
    A:ABCDE (可能的出栈顺序,push进去一个随即pop出来就是这个顺序)
    B:BCDEA (可能的出栈顺序,先push进去A之后,BCDE按照push进去随即pop出来,最后pop出A,就是这个顺序)
    C:EABCD (不可能的出栈顺序)
    D:EDCBA (将所有元素先全部push进去,然后一个一个pop出来)
```
```
 3.
```
