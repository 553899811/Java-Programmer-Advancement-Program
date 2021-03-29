<a name="1GLfT"></a>
# 一、序列化算法的步骤
1. 将对象实例相关的类元数据输出。
1. 递归地输出类的超类描述 直到不再有超类。
1. 类元数据完了以后，开始从顶层的超类 开始输出对象实力的实际数据值。
1. 从上至下递归输出实例的数据.

二、JDK序列化为啥必须要实现_Serializable 接口_
<a name="efDkJ"></a>
# 三、序列化和单例模式
所谓单例：就是单例模式就是在整个全局中（无论是单线程还是多线程），该对象只存在一个实例，而且只应该存在一个实例，没有副本。<br />序列化对单例有破坏<br />1、通过对某个对象的序列化与反序列化得到的对象是一个新的对象，这就破坏了单例模式的单例性。<br />2、我们知道readObject()的时候，底层运用了反射的技术。<br />序列化会通过反射调用无参数的构造方法创建一个新的对象。<br />这破坏了对象的单例性。<br />
<br />举例说明：<br />实现序列化的单例
```cpp
public class Singleton implements Serializable {

    // volatile 禁止指令重排序
    private static volatile Singleton instance;

    /**
     * 私有化构造器,这样该类就不会被实例化;
     */
    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                    return instance;
                }
            }
        }
        return instance;
    }
    /**
	 * 提供readResolve()方法
	 * 当JVM反序列化恢复一个新对象时，系统会自动调用readResolve()方法返回指定好的对象
	 * 从而保证系统通过反序列化机制不会产生多的Java对象
	 * 
	 * @return 单例对象
	 * @throws ObjectStreamException 异常
	 */
	private Object readResolve() throws ObjectStreamException {
		return instance;
	}
}
```

<br />测试类：
```cpp
    @Test
    public void testSingeton() throws Exception {
        //获取instance 对象
        Singleton instance = Singleton.getInstance();
        // 获取文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\Test.txt");
        // 获取对象输出流
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        // 输出对象
        objectOutputStream.writeObject(instance);

        // 关闭资源
        objectOutputStream.close();
        fileOutputStream.close();

        // 获取对象输入流
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("d:\\Test.txt"));

        // 读取对象
        Object object = objectInputStream.readObject();

        // 判断两个对象是否相等，返回true/false
        System.out.println(instance == object);
    }
```
当前结果： false ；<br />原因：因为JVM在反序列化的时候会通过反射创建了一个新的对象;

如何避免 呢 ？<br />[https://blog.csdn.net/m0_48837505/article/details/110084930](https://blog.csdn.net/m0_48837505/article/details/110084930)

<a name="iEqPW"></a>
# 引用
[https://blog.csdn.net/weixin_39723544/article/details/80527550](https://blog.csdn.net/weixin_39723544/article/details/80527550)<br />[https://blog.csdn.net/fuhao_ma/article/details/102969349](https://blog.csdn.net/fuhao_ma/article/details/102969349)<br />[https://blog.csdn.net/weixin_39800144/article/details/103432511](https://blog.csdn.net/weixin_39800144/article/details/103432511)<br />

