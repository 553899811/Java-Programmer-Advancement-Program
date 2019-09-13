# Java内存模型及happens-before原则
## JMM数据原子操作
 - read(读取): 从主内存读取数据
 - load(载入): 将主内存读取到的数据写入工作内存
 - use(使用): 从工作内存读取数据用于计算
 - assign(赋值): 将计算好的值重新赋值到工作内存中
 - store(存储): 将工作内存数据写入主内存
 - write(写入): 将store过去的变量值赋值给主内存中的变量
 - lock(锁定): 将主内存变量加锁，标识为线程独占状态
 - unlock(解锁): 将主内存变量解锁,解锁后其他线程可以锁定该变量
 ## CPU缓存
 ### 缓存一致性-总线加锁
 ![](/about/media/pic/总线加锁.png)
``` 
  线程2 从主存中read值之后 开始加锁，直到store 线程执行完之后 unlock，其他线程才可以执行。
```
#### 缺点:
```
存在严重的性能问题
``` 
### 缓存一致性-MESI 缓存一致性协议
 ![](/about/media/pic/MESI缓存一致性协议.png)
 - 对应代码:
 ```
   public class VolatileVisiblityTest {

    private static volatile boolean initFlag = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            System.out.println("waiting data...");
            while (!initFlag) {

            }
            System.out.println("---------------success");
        }).start();
        Thread.sleep(2000);
        new Thread(() -> prepareData()).start();
    }

    public static void prepareData() {
        System.out.println("准备数据中....");
        initFlag = true;
        System.out.println("数据准备完毕");
    }

}

 ```
 ```
    MESI是保持一致性的协议。它的方法是在CPU缓存中保存一个标记位，这个标记位有四种状态:
 ```  
   - MESI
     -  M: Modify，修改缓存，当前CPU的缓存已经被修改了，即与内存中数据已经不一致了；
     -  E: Exclusive，独占缓存，当前CPU的缓存和内存中数据保持一致，而且其他处理器并没有可使用的缓存数据；
     -  S: Share，共享缓存，和内存保持一致的一份拷贝，多组缓存可以同时拥有针对同一内存地址的共享缓存段；
     -  I: Invalid，失效缓存，这个说明CPU中的缓存已经不能使用 
    - CPU的读取遵循下面几点：
      - 如果缓存状态是I，那么就从内存中读取，否则就从缓存中直接读取。
      - 如果缓存处于M或E的CPU读取到其他CPU有读操作，就把自己的缓存写入到内存中，并将自己的状态设置为S。
      - 只有缓存状态是M或E的时候，CPU才可以修改缓存中的数据，修改后，缓存状态变为M。 
 
- **汇编#Lock前缀指令**
 ![](/about/media/pic/汇编Lock前缀指令.png)
```
   volatile关键字修饰的变量 在线程对其赋值操作的时候，会在其汇编语言中加上#Lock前缀，如上图。有2个功能：
   [1] 将当前处理器缓存行中的数据立刻刷新到主内存中
   [2] 这个写回内存的操作会触发 CPU总线嗅探机制 ，会引起在其他CPU核里缓存了该内存地址的数据无效(MESI缓存一致性协议)。从而  从表面上实现了内存可见性。
```
 - VM参数配置
 ```
  -server -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly,*VolatileVisiblityTest.prepareData
 ```
## volatile无法保证线程操作的原子性
![](/about/media/pic/volatile无法保证线程操作的原子性.png)
 - 代码:
 ```
 public class VolatileAtomicTest {
    static int num = 0;
    static void increase() {
        num++;
    }
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000; i++) {
                        increase();
                    }
                }
            });
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println(num);
    }
}
 ```
```
  结果：num<=10000
```
 - 分析:
 ```
    线程1计算完之后 通过主线向主存更新num值，此时MESI缓存一致性及CPU嗅探，使得线程2已经计算完的num丢失。原本两个线程计算完之后应该是2，此时只能是1；
 ```

