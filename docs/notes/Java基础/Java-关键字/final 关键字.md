# final 关键字

在Java中，final关键字可以用来修饰类、方法和变量（包括成员变量和局部变量）
<a name="Dnert"></a>
# 1. final 修饰变量
 final**成员变量表示常量，只能被赋值一次，赋值后值不再改变。**

| 修饰基本数据类型 | 表示该基本数据类型的值一旦在初始化后便不能发生变化 |
| --- | --- |
| 修饰一个引用类型 | 在对其初始化之后便不能再让其指向其他对象了，但该引用所指向的对象的内容是可以发生变化的 |

**final修饰一个成员变量（属性），必须要显示初始化。****这里有两种初始化方式**，一种是在变量声明的时候初始化；第二种方法是在声明变量的时候不赋初值，但是要在这个变量所在的类的所有的构造函数中对这个变量赋初值。<br />
<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1614261702784-122c1f41-dfc5-4787-9b5f-e0e46c387165.png#align=left&display=inline&height=313&margin=%5Bobject%20Object%5D&name=image.png&originHeight=625&originWidth=745&size=39361&status=done&style=none&width=372.5)<br />当函数的参数类型声明为final时，说明该参数是只读型的。即你可以读取使用该参数，但是无法改变该参数的值。<br />![image.png](https://cdn.nlark.com/yuque/0/2021/png/177460/1614261752147-003a6fa8-8fa8-4a8b-b5cc-a9cc989d9c75.png#align=left&display=inline&height=253&margin=%5Bobject%20Object%5D&name=image.png&originHeight=505&originWidth=718&size=31473&status=done&style=none&width=359)
<a name="P8GX0"></a>
# 2.final 编译期常量
什么叫对类的依赖性？单从字面上理解就是需不需要类，其实也就是与类的创建有没有关系。<br />那么类的创建和常量有什么关系吗？或则更具体来说，编译期常量和运行时常量对类的创建有什么不同的影响？<br />
<br />要解答以上的疑问还得先来看类在什么情况下会创建：<br />JVM的虚拟机规范严格规定了有且只有5种情况必须立即对类进行“初始化”，其中第一条就是：<br />遇到new、getstatic或invokestatic这4条字节码指令时，如果类没有初始化，则需要先触发其初始化。生成这4条指令的最常见的Java代码场景是：<br />
<br />（1）使用new关键字实例化对象时<br />（2）读取或设置一个类的静态字段（被final修饰、已在编译期把结果放入常量池的静态字段除外）时<br />（3）调用一个类的静态方法时<br />

```cpp
/**
 * <p>Description: </p>
 *
 * @version 1.0.0
 * @date 2021/2/25 21:40
 */
public class Final {

    public static void main(String[] args) {
        System.out.println("num:" + Test.num);
        System.out.println("=== after get num ===");
        System.out.println("length" + Test.a);
    }
}

class Test {
    static {
        System.out.println("类初始化完成");
    }

    public static final int num = 111;
    public static final int a = "Hello Final".length();

}

```
```cpp
num:111
=== after get num ===
类初始化完成
length11
```
**编译期常量不依赖类，不会引起类的初始化；而运行时常量依赖类，会引起类的初始化。**
<a name="BK1L1"></a>
# 引用
[浅谈Java中的final关键字](https://www.cnblogs.com/xiaoxi/p/6392154.html)

[编译期常量与运行时常量](https://blog.csdn.net/qq_34802416/article/details/83548369)
