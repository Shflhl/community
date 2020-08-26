[TOC]

# 1 Java 基础

 1. 类和方法命名：都是字母开头，后面字母、数字、下划线的组合。类一般大写开头，方法小写开头。
 2. 三种注释方法：// 单行注释          /* */多行注释          /** */特殊多行注释（javadoc）
 3. 基本数据类型
	整数类型：byte，short，int，long
	浮点数类型：float，double
	字符类型：char
	布尔类型：boolean

 4. var 关键字：StringBuilder sb = new StringBuilder();var sb = new StringBuilder();
 5. 位移：<< 地位补0         >> 符号位不变，高位补0      >>>符号位跟着动    ||   byte，short先变int
 6. 比较两个浮点数一般是验证他们之差是否小于一个很小的数字
 7. 整数运算在除数为0时会报错，而浮点数运算在除数为0时，不会报错，但会返回几个特殊值：
	NaN表示Not a Number
	Infinity表示无穷大
	-Infinity表示负无穷大
  
 8. 从Java12 开始switch 新语法

```java
switch (fruit) {
        case "apple" -> System.out.println("Selected apple");
        case "pear" -> System.out.println("Selected pear");
        case "mango" -> {
            System.out.println("Selected mango");
            System.out.println("Good choice!");
        }
        default -> System.out.println("No fruit selected");
        }
// switch 直接赋值
int opt = switch (fruit) {
            case "apple" -> 1;
            case "pear", "mango" -> 2;
            default -> 0;
        }; // 注意赋值语句要以;结束
// 需要多条语句 yield
 int opt = switch (fruit) {
     case "apple" -> 1;
     case "pear", "mango" -> 2;
     default -> {
         int code = fruit.hashCode();
         yield code; // switch语句返回值
     }
 };
```

 9. for each

```java
 int[] ns = { 1, 4, 9, 16, 25 };
 for (int n : ns) {
     System.out.println(n);
 }
```
10. classpath 和 jar
classpath是JVM用到的一个环境变量，它用来指示JVM如何搜索class，它是一组目录集合
假设classpath是.;C:\work\project1\bin;C:\shared，当JVM在加载abc.xyz.Hello这个类时，会依次查找：
	<当前目录>\abc\xyz\Hello.class
	C:\work\project1\bin\abc\xyz\Hello.class
	C:\shared\abc\xyz\Hello.class
设置classpath的两种方法
（1）系统环境变量
（2）JVM启动时设置环境变量
<font color="blue" size=5> 在命令行设置classpath运行Java程序</font>

```bash
java -cp .;C:\work\project1\bin;C:\shared abc.xyz.Hello
```
<font color="red" size=5> 
	（1）在IDE中运行Java程序，IDE自动传入的-cp参数是当前工程的bin目录和引入的jar包。
	（2）不要把任何Java核心库添加到classpath中！JVM根本不依赖classpath加载核心库！
</font>

11. jar
jar包还可以包含其它jar包，这个时候，就需要在MANIFEST.MF文件里配置classpath了
这里的第一个jar包按我自己的理解就是自己的工程
# 2 面向对象

 1. Java只能继承一个类。
 2. 子类无法访问父类的private字段或者private方法，此时可用到protected修饰符
 3. super 一般用于子类调用父类的构造方法
 4. 向上转型（upcasting）   把子类对象赋值给父类引用变量
     向下转型（downcasting）  instanceof成功，才能转型
 5. 相对父方法进行覆写时，加上@Override，可以让编译器检查是否覆写成功
 6. **多态**是指，针对某个类型的方法调用，其真正执行的方法取决于运行时期实际类型的方法

```java
// 此时体现出多态性，run（）是Person还是Student是根据具体传入的p是什么类型
public void runTwice(Person p) {
    p.run();
    p.run();
}
```

 7. 如果一个父类不允许子类对它的某个方法进行覆写，可以把该方法标记为final
```java
class Person {
    protected String name;
    public final String hello() {
        return "Hello, " + name;
    }
}
```
 8. 抽象类的定义方法：下面的抽象方法在子类中必须重写
```java
abstract class Person {
    public abstract void run();
}
```

 9. 如果一个抽象类没有字段，所有方法全部都是抽象方法，那么就可以用接口代替。
 10. 一个interface可以继承自另一个interface。interface继承自interface使用extends
 11. interface中还有个default方法，被定义为default的方法在子类中不必重写。 
	目的：当我们需要给接口新增一个方法时，会涉及到修改全部子类。如果新增的是default方法，那么子类就不必全部修改，只需要在需要覆写的地方去覆写新增方法。
12. default方法和抽象类的普通方法是有所不同的。因为interface没有字段，default方法无法访问字段，而抽象类的普通方法可以访问实例字段。
13. 静态方法内部，无法访问this变量，也无法访问实例字段，它只能访问静态字段.
14. interface是可以有静态字段的，并且静态字段必须为final类型：

```java
public interface Person {
    public static final int MALE = 1;
    public static final int FEMALE = 2;
}
```
15. final 修饰类可以防止类被继承

```java
public final class Hello {
    private int n = 0;
    protected void hi(int t) {
        long i = t;
    }
}
```
16. Commons Logging和Log4j它们一个负责充当日志API，一个负责实现日志底层。
默认情况下，Commons Loggin自动搜索并使用Log4j（Log4j是另一个流行的日志系统），如果没有找到Log4j，再使用JDK Logging。（SLF4J和Logback    与    Commons Logging和Log4j）
# 3 反射 
1. **反射是指程序在运行期可以拿到一个对象的所有信息**
2. JVM持有的每个Class实例都指向一个数据类型（class或interface）；例子：下面三个Class实例都是Class类的对象。我们平常创建的类也是对象。
┌───────────────────────────┐
│      Class Instance       │──────> String
├───────────────────────────┤
│name = "java.lang.String"  │
└───────────────────────────┘
┌───────────────────────────┐
│      Class Instance       │──────> Random
├───────────────────────────┤
│name = "java.util.Random"  │
└───────────────────────────┘
┌───────────────────────────┐
│      Class Instance       │──────> Runnable
├───────────────────────────┤
│name = "java.lang.Runnable"│
└───────────────────────────┘
3. 以String类为例，当JVM加载String类时，它首先读取String.class文件到内存，然后，为String类创建一个Class实例并关联起来

```java
// 不过Class类的构造方法是private，所以不能在我们自己的程序里面创建类实例。
Class cls = new Class(String);
```
4. JVM为每个加载的class创建了对应的Class实例，并在实例中保存了该class的所有信息，**这种通过Class实例获取class信息的方法称为反射（Reflection）。**
5. 获取一个class的Class实例有三种方法

```java
//方法一：class的静态变量
Class cls = String.class;
//方法二:实例对象的getclass()
String s = "Hello World!";
Class c = s.getClass();
//方法三：知道完整类路径名
Class cls = Class.forName("java.lang.String");
```

 6. 用instanceof不但匹配指定类型，还匹配指定类型的子类。而用==判断class实例可以精确地判断数据类型，但不能作子类型比较。
 7. 在获取到Class实例后就可以创建对应类型的实例
```java
// 获取String的Class实例:
Class cls = String.class;
// 创建一个String实例:  newInstance()方法不调用的是public、无参构造方法
String s = (String) cls.newInstance();
```
8. 动态加载特性：当使用到某个类时再去动态的加载（场景：Commons Logging 优先使用Log4j）

```java
// Commons Logging优先使用Log4j: 这就是为什么把Log4j 的jar包放入classpath中就能使用的原因
// 比如我是框架的编写者，因为我不知道用户的项目中是否回到如Log4j的jar。所以在我的代码中不能去判断
// 有没有Log4j然后new出对象来。因为要new的话就必须import xxx.yyy.zzz.class
LogFactory factory = null;
if (isClassPresent("org.apache.logging.log4j.Logger")) {
    factory = createLog4j();
} else {
    factory = createJdkLog();
}

boolean isClassPresent(String name) {
    try {
        Class.forName(name);
        return true;
    } catch (Exception e) {
        return false;
    }
}
```
<font size=5>9.使用Class实例访问信息</font>

1. 访问字段
**Field getField(name)：根据字段名获取某个public的field（包括父类）
Field getDeclaredField(name)：根据字段名获取当前类的某个field（不包括父类）
Field[] getFields()：获取所有public的field（包括父类）
Field[] getDeclaredFields()：获取当前类的所有field（不包括父类）**

调用上面这些方法后可以得到一个Field对象，包括修饰符，字段名，字段名称
<font size=5 color=red>进而可以获取该Class实例的某个具体对象的字段值</font>
f.get(o);f.set(o,v)
如果字段是private修饰则不可以访问，要想访问可以改成public、f.setAccessable(true)
SecurityManager可能不允许对java和javax开头的package的类调用setAccessible(true)

2. 访问方法（中的Class... 为要获取的方法的参数列表，当有方法重载时做出区别）
**Method getMethod(name, Class...)：获取某个public的Method（包括父类）
Method getDeclaredMethod(name, Class...)：获取当前类的某个Method（不包括父类）
Method[] getMethods()：获取所有public的Method（包括父类）
Method[] getDeclaredMethods()：获取当前类的所有Method（不包括父类）**

获取方法后调用方法

```java
	  // String对象:
      String s = "Hello world";
      // 获取String substring(int)方法，参数为int:
      Method m = String.class.getMethod("substring", int.class);
      // 在s对象上调用该方法并获取结果:    调用静态方法时第一个参数为null
      String r = (String) m.invoke(s, 6);
      // 打印调用结果:
      System.out.println(r);
      //Method.setAccessible(true) 可以调用非public方法
```
使用反射调用方法时，仍然遵循多态原则：即总是调用实际类型的覆写方法（如果存在）。

3. 构造方法
**getConstructor(Class...)：获取某个public的Constructor；
getDeclaredConstructor(Class...)：获取某个Constructor；
getConstructors()：获取所有public的Constructor；
getDeclaredConstructors()：获取所有Constructor**

```java
//此方法只能调用无参公共构造函数，如需要其它的构造函数，则需要Constructor类来构造，使用方法如Method
String.class.newInstance()
```

4. 获取继承关系
**Class getSuperclass()：获取父类类型；
Class[] getInterfaces()：获取当前类实现的所有接口。**

10. 动态代理
这种没有实现类但是在运行期动态创建了一个接口对象的方式，我们称为动态代码。JDK提供的动态创建接口对象的方式，就叫动态代理；
动态代理实际上是JDK在运行期动态创建class字节码并加载的过程；
其实就是JDK帮我们自动编写了一个类（不需要源码，可以直接生成字节码）；


# 4 注解
1. 放在类、方法、字段、参数前的特殊注释
2. 注解的定义

```java
public @interface Report {
    int type() default 0;
    String level() default "info";
    String value() default "";
}
```
3. 元注解：可以修饰其它注解的注解
4. 注解读取

```java
Class cls = Person.class;
if (cls.isAnnotationPresent(Report.class)) {
    Report report = cls.getAnnotation(Report.class);
    ...
}
```
# 5 泛型
泛型是一种“代码模板”，可以用一套代码套用各种类型。

1. ArrayList<Integer>向上转型为List<Integer>（T不能变！），但不能把ArrayList<Integer>向上转型为ArrayList<Number>（T不能变成父类）。
2. 使用ArrayList时，如果不定义泛型类型时，泛型类型实际上就是Object；

```java
List list = new ArrayList();
list.add("Hello");
list.add("World");
String first = (String) list.get(0);
String second = (String) list.get(1);
```
3. 接口实现范型（Comparable<T>）

```java
class Person implements Comparable<Person> {
    String name;
    int score;
    Person(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public int compareTo(Person other) {
    	// 在这个方法中必须给出对比的同类型其它对象的比较方法，这几使用name能比较，是因为name事String。
        return this.name.compareTo(other.name);
    }
    public String toString() {
        return this.name + "," + this.score;
    }
}
// 这个是Comparable<T>，要想对象能比较，必须实现这个接口。
// 现在下面这个东西相当于代码模板，上面实现这个模板并且设置为Comparable<Person>这个是特定接口
public interface Comparable<T> {
    /**
     * 返回-1: 当前实例比参数o小
     * 返回0: 当前实例与参数o相等
     * 返回1: 当前实例比参数o大
     */
    int compareTo(T o);
}
```

4. 类中的静态方法不能使用类名后面的范型

```java
// 对静态方法使用<T>:   此处报错
    public static Pair<T> create(T first, T last) {
        return new Pair<T>(first, last);
    }
//
// 可以编译通过:     不报错：此时的<T>与类中的<T>没有任何关系
    public static <T> Pair<T> create(T first, T last) {
        return new Pair<T>(first, last);
    }
```

5. 多个范型 public class Pair<T, K> ：Map<K,V>

6. Java泛型的实现方式——擦拭法
	Java的泛型是由编译器在编译时实行的，**编译器内部永远把所有类型T视为Object处理**，但是，**在需要转型的时候，编译器会根据T的类型自动为我们实行安全地强制转型。**
```java
// 这是个范型使用的代码
public class Pair<T> {
    private T first;
    private T last;
    public Pair(T first, T last) {
        this.first = first;
        this.last = last;
    }
    public T getFirst() {
        return first;
    }
    public T getLast() {
        return last;
    }
}
Pair<String> p = new Pair<>("Hello", "world");
String first = p.getFirst();
String last = p.getLast();
// 虚拟机对泛型其实一无所知，所有的工作都是编译器做的。
// 经过编译器的擦拭，代码变成这样
public class Pair {
    private Object first;
    private Object last;
    public Pair(Object first, Object last) {
        this.first = first;
        this.last = last;
    }
    public Object getFirst() {
        return first;
    }
    public Object getLast() {
        return last;
    }
}
Pair p = new Pair("Hello", "world");
String first = (String) p.getFirst();
String last = (String) p.getLast();
// 所以因为这种机制，范型存在许多局限
// 局限一：范型不能是基本类型，因为int不是object的持有类型
// 局限二:T是Object，我们对Pair<String>和Pair<Integer>类型获取Class时，获取到的是同一个Class，
// 		也就Pair类
// 局限三：无法实例化T
public class Pair<T> {
    private T first;
    private T last;
    public Pair() {
        // Compile error:
        first = new T();
        last = new T();
    }
}
// 局限三解决办法： 传入(Class<T> clazz)
public class Pair<T> {
    private T first;
    private T last;
    public Pair(Class<T> clazz) {
        first = clazz.newInstance();
        last = clazz.newInstance();
    }
}
```
7. 泛型继承:在继承了泛型类型的情况下，子类可以获取父类的泛型类型。

8. extends通配符

```java
public class PairHelper {
    static int add(Pair<Number> p) {
        Number first = p.getFirst();
        Number last = p.getLast();
        return first.intValue() + last.intValue();
    }
}// 这个代码编译没问题，但是使用的时候有些问题
//这条语句，虽然传入的是Number，但是实际上初始化是Integer，这样是错误为
// Pair<Number> 不是Pair<Integer>父类
int sum = PairHelper.add(new Pair<Number>(1, 2));

static int add(Pair<? extends Number> p) {
    Number first = p.getFirst();
    Number last = p.getLast();
    return first.intValue() + last.intValue();
}
// 参数换成这个就可以传Pair<Integer>
// 方法参数签名setFirst(? extends Number)无法传递任何Number类型给setFirst(? extends Number)
// 假设方法中传的时Pair<Double>,那么获得的p的set方法为setFrist(? extends Number)
// 因为是Pair<Double> 所以不能传Integer、Number....
```
9. extends的作用：限定

```java
// 这是一个对参数List<? extends Integer>进行只读的方法
// 限定类
public class Pair<T extends Number> { ... }
```
extends总结（2020/05/13 2st）
1. Pair<? extends Number> p  这个可以放在方法参数列表中，调用方法时可以传入的参数：Pair<Number>、Pair<Integer>其中范型
	一定得是Number得子类。这个时候p只能读取数据，并且只能用Number接收读取的对象。p无法赋值，就算是传入Pair<Integer>
	也无法set Integer的值。
2. class  类名<T extends Number>{} 这样修饰类时，使得这个泛型只能传入Number或其子类。

10. super通配符

```java
 public static void main(String[] args) {
     Pair<Number> p1 = new Pair<>(12.3, 4.56);
     Pair<Integer> p2 = new Pair<>(123, 456);
     setSame(p1, 100);
     setSame(p2, 200);
     System.out.println(p1.getFirst() + ", " + p1.getLast());
     System.out.println(p2.getFirst() + ", " + p2.getLast());
 }
 // 这里使用super 可以传入Integer或Integer父类的Pair类型
 static void setSame(Pair<? super Integer> p, Integer n) {
     p.setFirst(n);
     p.setLast(n);
 }
// 使用？ super Integer的意义
//    允许调用set(? super Integer)方法传入Integer的引用；（因为传入的都是Integer的父类）
//    不允许调用get()方法获得Integer的引用。（获取最小是Integer）
```
super总结（2020/05/13 2st）
1. super只能set，并且传入的值 必须是最底下的。不能get，除非是用Object接收。

11. extends和super的对比

```java
//<? extends T>允许调用读方法T get()获取T的引用，但不允许调用写方法set(T)传入T的引用（传入null除外）；
//<? super T>允许调用写方法set(T)传入T的引用，但不允许调用读方法T get()获取T的引用（获取Object除外）。
```
12. 无限定通配符<?>
不允许调用set(T)方法并传入引用（null除外）；
不允许调用T get()方法并获取T引用（只能获取Object引用）。
既不能读，也不能写，那只能做一些null判断

13. Pair<?>是所有Pair<T>的超类

14. 泛型和反射

```java
// compile warning:   是因为把Class<T> 当成 Class<Object>    Class<Object> 不是 Class<String>的父类
Class clazz = String.class;
String str = (String) clazz.newInstance();

// no warning:
Class<String> clazz = String.class;
String str = clazz.newInstance();
```
15. 范型数组：

```java
// 我们可以声明带泛型的数组，但不能用new操作符创建带泛型的数组：
Pair<String>[] ps = null; // ok
Pair<String>[] ps = new Pair<String>[2]; // compile error!
// 必须通过强制转型实现带泛型的数组：
@SuppressWarnings("unchecked")
Pair<String>[] ps = (Pair<String>[]) new Pair[2];
```

16. 可变参数；；；；方法里面创建范型数组的方法

```java
// 所以我们不能直接创建泛型数组T[]，因为擦拭后代码变为Object[]：
// compile error:
public class Abc<T> {
    T[] createArray() {
        return new T[5];
    }
}
// 必须借助Class<T>来创建泛型数组：
T[] createArray(Class<T> cls) {
    return (T[]) Array.newInstance(cls, 5);
}
// 我们还可以利用可变参数创建泛型数组T[]：
public class ArrayHelper {
    @SafeVarargs
    static <T> T[] asArray(T... objs) {
        return objs;
    }
}
String[] ss = ArrayHelper.asArray("a", "b", "c");
Integer[] ns = ArrayHelper.asArray(1, 2, 3);
```

# 6 集合
1. 集合定义:Java对象可以在内部持有若干其他Java对象，并对外提供访问接口，我们把这种Java对象称为集合。
2. java.util包提供的集合类：Collection 是除Map外其它集合的根接口
三种类型的集合:List(有序)、Set（不可重复）、Map(Key-Value)
3. 集合特点：（1）接口和实现类相分离（2）支持泛型（3）访问都是迭代器（Iterator）
4. 历史遗留类，不建议使用
HashTable ：Map的实现类
Vector：线程安全的List实现
Stack：基于Vector实现的栈

5. List的使用

```java
// 两种实现，ArrayList和LinkedList
// ArrayList不会让里面数组占满，当添加后占满就会自己扩容

// 1.List的遍历
// （1）索引遍历，LinkedList当索引很大时效率会变低
// （2）迭代器遍历：Iterator，不同的List类型，返回的Iterator对象实现也是不同的，总是具有最高的访问效
//     率。
//  tips: for each()可以直接遍历List，原理也是Iterator，只要是实现了Iterator接口的都可以for each
// 2.List 转 Array
//	(1) list.toArray() 返回 object[]
//	(2) list.toArray(new T[]) 可以返回具体类型
// 3.Array 转 List
// 	(1) List.of(array) 返回的既不是ArrayList也不是LinkedList，是个只读List
// 4.equal()
public static void main(String[] args) {
    List<String> list = new ArrayList<>();
    list.add("A");
    list.add("B");
    list.add("C");
    System.out.println(list.contains(new String("C"))); // true
    System.out.println(list.indexOf(new String("C"))); // 2
}
// 上面代码中出现正确结果，按道理new String("C")和放入的“C”是两个不同的实例。
// 之所以会出现正确结果，是应为用equals对比元素是否相等，==比较是否是同一个对象
public int indexOf(Object o) { //这个是List内部查找元素的方法源代码
    if (o == null) {
        for (int i = 0; i < size; i++)
            if (elementData[i]==null)
                return i;
    } else {
        for (int i = 0; i < size; i++)
            if (o.equals(elementData[i]))// 对象o为范型对应的对象，比如String、Integer....
                return i;
    }
    return -1;
}
// tips:如果不调用List的contains()、indexOf()这些方法，那么放入的元素就不需要实现equals()方法。
//		实现equals时，要考虑传入的是null的话就不能调用equals()方法，此时要用==
```

6. Map的使用

```java
// 产生的需求：通过一个键去查对应的值，比如根据学生的name查对应的score
// 速度快的原因：HashMap;通过空间换时间，利用一个大容量的数组，计算key的hash值得到value的索引
// Map<K, V>是一种键-值映射表；常用实现是HashMap                 与List对应：同样是一个接口
// 1.Map遍历（两种方法）
// 	(1) for each(map.keySet())
// 	(2)	for each(map.entitySet())
// 2.编写equals()和hashCode()
//  (1)在下面这段代码中，key1和key2内容相同但不是同一个对象，通过他们作为key取出的value相同
//     这就说明在Map里面key的比较是equals(),所以要想Map正确使用，key对应的对象必须是重写equals()
//  (2)key计算索引的原理，通过key对象调用hashCode()得到一个int值
//  总结：根据（1）（2）得出，要想正确使用Map，必须重写key对象的equals（）和hashCode（）方法
//    对应两个实例a和b：
//       如果a和b相等，那么a.equals(b)一定为true，则a.hashCode()必须等于b.hashCode()；
//       如果a和b不相等，那么a.equals(b)一定为false，则a.hashCode()和b.hashCode()尽量不要相等。
//    对于String s1 = "a" 和 String s2 = new String("a") s1.hashCode()==s2.hashCode()
//  (3)hashCode()编写方法
//     借助Object.hash(k1,k2,..)得到值，但k1，k2...必须在equals中
public class Main {
    public static void main(String[] args) {
        String key1 = "a";
        Map<String, Integer> map = new HashMap<>();
        map.put(key1, 123);

        String key2 = new String("a");
        map.get(key2); // 123

        System.out.println(key1 == key2); // false
        System.out.println(key1.equals(key2)); // true
    }
}
// 3.拓展知识
int index = key.hashCode() & 0xf; // 0xf = 15
//  这条代码能保证映射的索引在0到15之间，当满了的时候会自动扩容一倍。
int index = key.hashCode() & 0x1f; // 0x1f = 31
//  扩容后之前的值要重新计算一遍hash值，重新分布。这样很影响效率，所以如果可以提前知道map的容量可以提前
//  设置，HashMap的容量总是2的n次方。
//  HashMap解决hash冲突的方法，每个位置上存的都是list。
// 4.其它Map
//	（1）EnumMap：当key是枚举类型的时候使用这个效率高
//	（2）TreeMap：key是排好序的，不过key对象必须实现Comparable对象,如果没有，初始化时必须传一个
//               comparator的实现。
```

7. 使用配置文件
- 创建Properties实例；
- 调用load()读取文件；
- 调用getProperty()获取配置。

8. Set的使用：Set相当于只储存key的Map，常用Set去除重复元素。
	Set和HashMap一样都需要正确覆写equals()和hashCode()方法。
	Set与Map相对应，常用实现是HashSet，而对于需要排序的则是SortedSet接口的实现类，TreeSet

9. Queue的使用
LinkedList即实现了List接口，又实现了Queue接口
```java
// 这是一个List:
List<String> list = new LinkedList<>();
// 这是一个Queue:
Queue<String> queue = new LinkedList<>();
```
通过add()/offer()方法将元素添加到队尾；
通过remove()/poll()从队首获取元素并删除；
通过element()/peek()从队首获取元素但不删除。

10. PriorityQueue
放入对象必须实现comparable接口或者 初始化时传入Comparator的实现，每次取出都是优先级最高的元素。

11. Deque 双端队列
12. Stack


# 7 多线程
1. 当Java程序启动的时候，实际上是启动了一个JVM进程，然后，JVM启动主线程来执行main()方法。在main()方法中，我们又可以启动其他线程。此外，JVM还有负责垃圾回收的其他工作线程等。

2.创建新线程（两种方法）
（1）方法一：继承Thead类，并且重写run()，在run()里面的代码就是新线程将要执行的代码。
						new 出这个类，然后调用它的star()方法。
```java
public class Main {
    public static void main(String[] args) {
        Thread t = new MyThread();
        t.start(); // 启动新线程
    }
}
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("start new thread!");
    }
}
```
（2）方法二：创建Thread实例时，传入一个Runnable实例

```java
public class Main {
    public static void main(String[] args) {
        Thread t = new Thread(new MyRunnable());
        t.start(); // 启动新线程
    }
}
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("start new thread!");
    }
}
```

3. native修饰符表示这个方法是由JVM虚拟机内部的C代码实现的，不是由Java代码实现的。


4. 线程的状态
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200413171311169.PNG?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA1MDQ0MzU=,size_16,color_FFFFFF,t_70)

5. 线程终止的原因
（1）run（）方法运行完了
（2）run（）方法未捕获异常
（3）线程实例调用stop()

6. join()
	比如在main里面有线程实例t，t.join() 会使得main等待t执行完后再执行

7. 线程中断（2种方法）
（1）在其他线程中对目标线程调用interrupt()方法，目标线程需要反复检测自身状态是否是interrupted状态，如果是，就立刻结束运行。

```java
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new MyThread();
        t.start();
        Thread.sleep(1); // 暂停1毫秒
        t.interrupt(); // 中断t线程
        t.join(); // 等待t线程结束
        System.out.println("end");
    }
}
// 这个代码就可以实现中断线程，通过while不断检测，但是这种方法有个弊端，就是如果我们想要中断的线程
// 在等待其他的线程，比如调用了join();此时while不会运行，这种方法就是失效了。
class MyThread extends Thread {
    public void run() {
        int n = 0;
        while (! isInterrupted()) {
            n ++;
            System.out.println(n + " hello!");
        }
    }
}
//////////////////////////////////////////////////////////////////////////
// 下面代码时解决当要中断的线程在等待其他线程。如果我们要中断的线程中有join(),它在等待的时候被另一个
// 线程interrupt，则它的join会抛出异常。
// 2st 2020/05/19 就是如果要被中断的线程正在等待其它线程。因为此时是在join（）处等待的，所以此时会在join（）出抛出异常。
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new MyThread();
        t.start();
        Thread.sleep(1000);
        t.interrupt(); // 中断t线程
        t.join(); // 等待t线程结束
        System.out.println("end");
    }
}
class MyThread extends Thread {
    public void run() {
        Thread hello = new HelloThread();
        hello.start(); // 启动hello线程
        try {
            hello.join(); // 等待hello线程结束
        } catch (InterruptedException e) {
            System.out.println("interrupted!");
        }
        hello.interrupt();
    }
}
class HelloThread extends Thread {
    public void run() {
        int n = 0;
        while (!isInterrupted()) {
            n++;
            System.out.println(n + " hello!");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
```
（2）在目标线程种设置标志位；
线程间共享变量需要使用volatile关键字标记，<font color=red>确保每个线程都能读取到更新后的变量值。
</font>public volatile boolean running = true;
关键词volatile:涉及到Java内存模型
volatile关键字解决的是可见性问题：当一个线程修改了某个共享变量的值，其他线程能够立刻看到修改后的值。
如果没有volatile关键词修饰，就保证不了读取的是最新值。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200413195427684.PNG?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA1MDQ0MzU=,size_16,color_FFFFFF,t_70)

8. 守护线程：
前提：当所有线程都运行结束时，JVM退出，进程结束。如果有一个线程没有退出，JVM进程就不会退出。但是有种线程目的就是无线循环。
定义：守护线程是指为其他线程服务的线程。在JVM中，所有非守护线程都执行完毕后，无论有没有守护线程，虚拟机都会自动退出。因此，JVM退出时，不必关心守护线程是否已结束。

```java
Thread t = new MyThread();
t.setDaemon(true);
t.start();
```
tips：守护线程不能持有任何需要关闭的资源，例如打开文件等，因为虚拟机退出时，守护线程没有任何机会来关闭文件，这会导致数据丢失。

9. 线程同步

```java
public class Main {
    public static void main(String[] args) throws Exception {
        var add = new AddThread();
        var dec = new DecThread();
        add.start();
        dec.start();
        add.join();
        dec.join();
        System.out.println(Counter.count);// 输出结果每次都不同
    }
}
class Counter {
    public static int count = 0;
}
class AddThread extends Thread {
    public void run() {
        for (int i=0; i<10000; i++) { Counter.count += 1; }
    }
}
class DecThread extends Thread {
    public void run() {
        for (int i=0; i<10000; i++) { Counter.count -= 1; }
    }
}
```
上面代码结果每次输出都不同，原因是对于一次自加操作对应三条指令。不是原子操作。

通过加锁和解锁的操作，就能保证3条指令总是在一个线程执行期间，不会有其他线程会进入此指令区间。
<font color=red>即使在执行期线程被操作系统中断执行，其他线程也会因为无法获得锁导致无法进入此指令区间。只有执行线程将锁释放后，其他线程才有机会获得锁并执行。这种加锁和解锁之间的代码块我们称之为临界区（Critical Section），任何时候临界区最多只有一个线程能执行。</font>
 可见，保证一段代码的原子性就是通过加锁和解锁实现的。Java程序使用synchronized关键字对一个对象进行加锁：
```java
synchronized(lock) {
    n = n + 1;
}
```

10. synchronized关键字的使用
- 找出修改共享变量的线程代码块；
- 选择一个共享实例作为锁；
- 使用synchronized(lockObject) { ... }。

11. Java原子操作：此处代码不需要用synchroized
（1）基本类型赋值（double，long除外）
（2）引用类型赋值

12. 同步的方法
比如上面同步的方法，在每个线程里面用synchronized关键字；这个时候就需要每个线程考虑到要锁住那个对象。这样往往会造成混乱，所以下面时正确使用synchronized关键字的方法。

```java
public class Counter {
    private int count = 0;
    public void add(int n) {
        synchronized(this) {
            count += n;
        }
    }
    public void dec(int n) {
        synchronized(this) {
            count -= n;
        }
    }
    public int get() {
        return count;
    }
}
```
在这个代码中，synchronized关键放到计数器里面。这个既可以线程同步又不会混乱。

13. 线程安全和线程不安全；如果一个类被设计成可以被多个线程安全访问那么就说这个类是线程安全的
线程安全类举例：
（1）有些类被设计成线程安全的；比如StringBuffer
（2）有些类内部字段是final，本来就是只能读不能写，所以也是线程安全的
（3）有些类只提供静态方法，此类是线程安全的

14. synchronized修饰方法

```java
// 下面两个方法是同样的效果
public void add(int n) {
    synchronized(this) { // 锁住this
        count += n;
    } // 解锁
}
public synchronized void add(int n) { // 锁住this
    count += n;
} // 解锁
```
第二个方法用synchronized修饰方法，相当于锁住this对象。
对于静态方法用synchronized修饰时，相当于锁住Class的实例。

15. 可重入锁：就是一个线程可以重复获取一个对象的锁
JVM允许同一个线程重复获取同一个锁，这种能被同一个线程反复获取的锁，就叫做可重入锁。
由于Java的线程锁是可重入锁，所以，获取锁的时候，不但要判断是否是第一次获取，还要记录这是第几次获取。每获取一次锁，记录+1，每退出synchronized块，记录-1，减到0的时候，才会真正释放锁。



16. 死锁：死锁后没有办法，只能强制结束进程。所以要避免死锁。
```java
public void add(int m) {
    synchronized(lockA) { // 获得lockA的锁
        this.value += m;
        synchronized(lockB) { // 获得lockB的锁
            this.another += m;
        } // 释放lockB的锁
    } // 释放lockA的锁
}

public void dec(int m) {
    synchronized(lockB) { // 获得lockB的锁
        this.another -= m;
        synchronized(lockA) { // 获得lockA的锁
            this.value -= m;
        } // 释放lockA的锁
    } // 释放lockB的锁
}
```
避免死锁有几种方法：Java按同样的顺序获取锁。

16. wait() 方法
- wait()方法必须在当前获取的锁对象上调用，这里获取的是this锁，因此调用this.wait()
- 调用wait()方法后，线程进入等待状态，wait()方法不会返回，直到将来某个时刻，线程从等待状态被其他线程唤醒后，wait()方法才会返回，然后，继续执行下一条语句。
- 必须在synchronized块中才能调用wait()方法，因为wait()方法调用时，会释放线程获得的锁，wait()方法返回后，线程又会重新试图获得锁。

17. notify() 方法 ：唤醒在this锁等待的其他线程
已唤醒的线程还需要重新获得锁后才能继续执行。

```java
// 这个代码中if是错的
// 因为可以有多个线程运行到wait(),这是有可能的，假设第一个线程调用getTask（）时，队列还是为空，则此时
// 第一个线程进入等待状态，并且释放锁，不光如此，还会等待其他线程调用this.notify（）才会醒来。醒来后会
// 去获取锁。
// 第一个线程进入等待状态后，释放锁。其它的线程就会有个获得锁并且运行到wait()处。正式因为如此，setTask
// 使得队列不空且唤醒了其它所有等待的线程的时候，只有一个线程能获得锁，其它的还要因为没有获得锁而等待，
// 当着这个获得锁的线程删除元素使得队列为空时，刚才的其它线程会被唤醒并且会竞争锁，如果此时没有while再
// 判断一下，此时获得锁的线程会从一个空队列中删除元素。所以这里必须时while。
public synchronized String getTask() throws InterruptedException {
    if (queue.isEmpty()) {
        this.wait();
    }
    return queue.remove();
}
```

18. ReentrantLock：与synchronized对应，可以实现同步、也可wait、notify
```java
public class Counter {
	// 使用ReetranLock比syncchronized安全一点
    private final Lock lock = new ReentrantLock();
    private int count;

    public void add(int n) {
        lock.lock();
        try {
            count += n;
        } finally {
            lock.unlock();
        }
    }
}
// 1秒没有获取锁就返回false
if (lock.tryLock(1, TimeUnit.SECONDS)) {
    try {
        ...
    } finally {
        lock.unlock();
    }
}
///////////////////////////////////////////////////////////////////////////
// 有几个关键点
// 使用Condition类来实现wait和notify
// condition实例必须通过ReentrantLock实例.newCondition()获取，以此绑定。
// condition.await() == this.wait()
// condition.signal() == this.notify()
// condition.signalAll() == this.notifyAll()
class TaskQueue {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private Queue<String> queue = new LinkedList<>();

    public void addTask(String s) {
        lock.lock();
        try {
            queue.add(s);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public String getTask() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                condition.await();
            }
            return queue.remove();
        } finally {
            lock.unlock();
        }
    }
}
```

19. ReadWriteLock
前面的synchronized、this.wait()、this.notify()
		   ReentrantLock lock、condition.await()、condition.signal()
上面这些实现的不光是同样一时刻只能一个线程写、并且也只能一个线程读。
现在想要实现这样一个功能：只能一个在写，在写的时候不能读。没有写的时候可以多个一起读。

```java
public class Counter {
    private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private final Lock rlock = rwlock.readLock();
    private final Lock wlock = rwlock.writeLock();
    private int[] counts = new int[10];

    public void inc(int index) {
        wlock.lock(); // 加写锁
        try {
            counts[index] += 1;
        } finally {
            wlock.unlock(); // 释放写锁
        }
    }
    public int[] get() {
        rlock.lock(); // 加读锁
        try {
            return Arrays.copyOf(counts, counts.length);
        } finally {
            rlock.unlock(); // 释放读锁
        }
    }
}
```

20. StampedLock 与ReadWriteLock对应是个读写锁。后一个不允许在读的时候获取写锁。
乐观锁的意思就是乐观地估计读的过程中大概率不会有写入，因此被称为乐观锁。
悲观锁则是读的过程中拒绝有写入，也就是写入必须等待。

乐观锁的并发效率更高，但一旦有小概率的写入导致读取的数据不一致，需要能检测出来，再读一遍就行。

```java
public class Point {
    private final StampedLock stampedLock = new StampedLock();

    private double x;
    private double y;

    public void move(double deltaX, double deltaY) {
        long stamp = stampedLock.writeLock(); // 获取写锁
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            stampedLock.unlockWrite(stamp); // 释放写锁
        }
    }
    public double distanceFromOrigin() {
        long stamp = stampedLock.tryOptimisticRead(); // 获得一个乐观读锁 1.获取版本号
        // 注意下面两行代码不是原子操作
        // 假设x,y = (100,200)
        double currentX = x;
        // 此处已读取到x=100，但x,y可能被写线程修改为(300,400)
        double currentY = y;
        // 此处已读取到y，如果没有写入，读取是正确的(100,200)
        // 如果有写入，读取是错误的(100,400)
        if (!stampedLock.validate(stamp)) { // 检查乐观读锁后是否有其他写锁发生 2. 验证版本号
            stamp = stampedLock.readLock(); // 获取一个悲观读锁 2.1. 版本号变换用悲观锁读
            try {
                currentX = x;
                currentY = y;
            } finally {
                stampedLock.unlockRead(stamp); // 释放悲观读锁
            }
        }// 2.2. 版本号不边 （这是大概率事件，也因此提高并发率）
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
}
```
StampedLock把读锁细分为乐观读和悲观读，能进一步提升并发效率。但这也是有代价的：一是代码更加复杂，二是StampedLock是不可重入锁，不能在一个线程中反复获取同一个锁。

21. Java标准库的java.util.concurrent包；里面包括线程安全的集合
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200414194214649.PNG?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTA1MDQ0MzU=,size_16,color_FFFFFF,t_70)
22. 使用java.util.concurrent.atomic提供的原子操作可以简化多线程编程：
 - 原子操作实现了无锁的线程安全；
- 适用于计数器，累加器等。

23. 线程池：能接收大量小任务并进行分发处理的就是线程池。
线程池内部维护了若干个线程，没有任务的时候，这些线程都处于等待状态。如果有新任务，就分配一个空闲线程执行。如果所有线程都处于忙碌状态，新任务要么放入队列等待，要么增加一个新线程进行处理。

```java
// 1. 线程池的使用
// 使用ExecutorService接口来接受线程池
//       FixedThreadPool：线程数固定的线程池；
//       CachedThreadPool：线程数根据任务动态调整的线程池；
//       SingleThreadExecutor：仅单线程执行的线程池。
// 创建这些线程池的方法集成到Executors类中
// 2. 线程池关闭的时间
//	（1）程序运行完毕
//	（2）线程池调用shutdown()，此时会等待任务运行完成再关闭
//	（3）线程池调用shutdownNow()，会立刻停止执行
//	（4）线程池调用awaitTermination(()，会等待指定时间后关闭
// 3. 其它线程池
//	（1）ScheduledThreadPool
```

24. 线程池对应任务Task返回值和future使用

```java
// 这个是线程池的Task实现方式。只需要实现Runnable接口就可以。但是这种方法有
// 缺点，就是没有返回值。
class Task implements Runnable {
    public String result;

    public void run() {
        this.result = longTimeCalculation(); 
    }
}
// 可以实现Callable<String>接口，它是个范型。那么这里就存在一个问题。
// 比如main线程启动一个线程做一件事，但刚才那个线程完成
// 工作并且返回的时候要告诉main。所以在这场景中就存在一个问题，main并不能
// 预知工作线程要运多久。所以说需要future。
class Task implements Callable<String> {
    public String call() throws Exception {
        return longTimeCalculation(); 
    }
}
// future的使用
// 一个Future类型的实例代表一个未来能获取结果的对象
ExecutorService executor = Executors.newFixedThreadPool(4); 
// 定义任务:
Callable<String> task = new Task();
// 提交任务并获得Future:
Future<String> future = executor.submit(task);
// 从Future获取异步执行返回的结果:
String result = future.get(); // 可能阻塞
```

25. Fork/Join线程池
它可以执行一种特殊的任务：把一个大任务拆成多个小任务并行执行。

```java
// 核心代码SumTask继承自RecursiveTask，在compute()方法中，关键是如何“分
// 裂”出子任务并且提交子任务
class SumTask extends RecursiveTask<Long> {
    protected Long compute() {
        // “分裂”子任务:
        SumTask subtask1 = new SumTask(...);
        SumTask subtask2 = new SumTask(...);
        // invokeAll会并行运行两个子任务:
        invokeAll(subtask1, subtask2);
        // 获得子任务的结果:
        Long result1 = fork1.join();
        Long result2 = fork2.join();
        // 汇总结果:
        return result1 + result2;
    }
}
```

Fork/Join线程池在Java标准库中就有应用。Java标准库提供的java.util.Arrays.parallelSort(array)可以进行并行排序，它的原理就是内部通过Fork/Join对大数组分拆进行并行排序，在多核CPU上就可以大大提高排序的速度。

26. ThreadLocal
需求：比如web请求

```java
// 这是个web请求，为每个用户创建一个user，如果后续的工作都需要用到user，这个
// 就被称为上下文，下面是一种实现上下文的方法，每个方法里面都传入user，但这样
// 很容易造成混乱。所以这个时候就需要ThreadLocal。
public void process(User user) {
    checkPermission(user);
    doWork(user);
    saveStatus(user);
    sendResponse(user);
}
// ThreadLLoacl使用
// 定义成static 并且与User关联起来
static ThreadLocal<User> threadLocalUser = new ThreadLocal<>();
void processUser(user) {
    try {
        threadLocalUser.set(user);
        step1();
        step2();
    } finally {
    	// 最后得在这里清除
        threadLocalUser.remove();
    }
}
// 使用的时候直接用静态字段get()
void step1() {
    User u = threadLocalUser.get();
    log();
    printUser();
}
void log() {
    User u = threadLocalUser.get();
    println(u.name);
}
void step2() {
    User u = threadLocalUser.get();
    checkUser(u.id);
}
```
实际上，可以把ThreadLocal看成一个全局Map<Thread, Object>：每个线程获取ThreadLocal变量时，总是使用Thread自身作为key：

```java
Object threadLocalValue = threadLocalMap.get(Thread.currentThread());
```

# 8 IO
1. IO概念

从Java代码来看，输入实际上就是从外部，例如，硬盘上的某个文件，把内容读到内存，并且以Java提供的某种数据类型表示，例如，byte[]，String，这样，后续代码才能处理这些数据。

因为内存有“易失性”的特点，所以必须把处理后的数据以某种方式输出，例如，写入到文件。Output实际上就是把Java表示的数据格式，例如，byte[]，String等输出到某个地方。

2. InputStream / OutputStream

IO流是一种顺序读写数据的模式，它的特点是单向流动。
IO流是以字节byte为最小单位，也称字节流。例如从一个文件包含6个字节
则读入内存的方式是按6个字节一个一个字节的读。
InputStream / OutputStream代表输入输出流，是最基本的两种流。

3. Reader / Writer

Java在<font size=5 color=red>内存</font>中总是使用Unicode表示字符，所以，一个英文字符和一个中文字符都用一个char类型表示，它们都占用两个字节。
ASCII编码是单字节编码，只有英文字符，不能编码汉字。
GBK编码1个英文字符是1个字节，一个汉字是是2个字节。
UTF-8编码1个英文字符是1个字节，一个汉字是3个字节。
Unicode编码1个英文字符是2个字节，一个汉字是2个字节。
Java提供了Reader和Writer表示字符流，字符流传输的最小数据单位是char。

例子：我们把char[]数组Hi你好这4个字符用Writer字符流写入文件，并且使用**UTF-8编码**，得到的最终文件内容是8个字节，英文字符H和i各占一个字节，中文字符你好各占3个字节。

**Reader和Writer本质上是一个能自动编解码的InputStream和OutputStream使用Reader，数据源虽然是字节，但我们读入的数据都是char类型的字符，原因是Reader内部把读入的byte做了解码，转换成了char。使用InputStream，我们读入的数据和原始二进制数据一模一样，是byte[]数组，但是我们可以自己把二进制byte[]数组按照某种编码转换为字符串。究竟使用Reader还是InputStream，要取决于具体的使用场景。如果数据源不是文本，就只能使用InputStream，如果数据源是文本，使用Reader更方便一些。Writer和OutputStream是类似的。**

```java
// 上面的理解，InputStream / OutputStream知识把文件按字节原样输入输出内存
// 而Reader和Writer不一样就在于除了读入写出的字节并且还带有意义。
// Reader和Writer数据源都是字节流，但带了上自动编码解码。比如一个文本文件
// 以utf-8保存”Hi段思鸿“这里两个字母占2两个字节，三个中文占9个字节，直接用
// InputStream读进内存是有11个字节，但是在内存中Java字符都是用Unicode编
// 码，这里对应不上。我的理解是Reader和Writer是在这里做了转化。同理写出也类
// 似。
```

4. File对象：Java中操作文件或者目录

```java
File f = new File("C:\\Windows\\notepad.exe");
File f = new File("/usr/bin/javac");
// 1. 文件路径：在windows中'\'表示分隔，Linux中则是'/'
//            但是在Java中'\'需要转义
// 2. 获取文件路径
//	（1）getpath() 返回构造时传入的路径
//	（2）getAbsolutePath() 返回绝对路径
//	（3）getCanonicalPath() 返回规范路径
// 3. 创建file对象时，即使传入的文件或者目录不存在也不会出错。因为创建的时候
//	  不会进行IO操作。
// 4. 文件的创建和删除
//	（1）普通创建和删除：构造file对象，file调用createNewFile（）delete()
//	（2）临时文件：File.createTempFile（）file.deleteOnExit()
// 5. 文件遍历：file.list()或者file.listFiles()
// 6. 目录创建
//		boolean mkdir()：创建当前File对象表示的目录；
//		boolean mkdirs()：创建当前File对象表示的目录，并在必要时将不存在
//		的父目录也创建出来；
//		boolean delete()：删除当前File对象表示的目录，当前目录必须为才
//		能删除成功
```

5. InputStream：抽象类、所有输入类的超类
下面这个方法最重要，每次读取下一个字节，并返回字节表示（0到255）。
直到文件尾返回-1
```java
public abstract int read() throws IOException;

public void readFile() throws IOException {
    try (InputStream input = new FileInputStream("src/readme.txt")) {
        int n;
        while ((n = input.read()) != -1) {
            System.out.println(n);
        }
    } // 编译器在此自动为我们写入finally并调用close()
}
// 因为InputStream和OutputStream中都实现了AutoCloseable接口。
// 1. 缓存；必须自己定义byte数组
int read(byte[] b)
// 读取若干字节并填充到byte[]数组，返回读取的字节数
int read(byte[] b, int off, int len)
// 指定byte[]数组的偏移量和最大填充数
// 2. 其它实现类
// 	ByteArrayInputStream 可以在内存中模拟InputStream
//  测试的时候就不需要真实的文件。
public class Main {
    public static void main(String[] args) throws IOException {
    	// 这是定义在内存里面
        byte[] data = { 72, 101, 108, 108, 111, 33 };
        // 模拟InputStream
        try (InputStream input = new ByteArrayInputStream(data)) {
            int n;
            while ((n = input.read()) != -1) {
                System.out.println((char)n);
            }
        }
    }
}
// 3. 缓冲区
// 	操作系统不会一个一个字节读，而是一次读若干字节到缓冲区。并且维护一个未读
//  缓冲区指针。
```

6. OutputStream：抽象类、所有输出类的超类

```java
// 与InputStream类似
// 不过它有个不同：flush()把缓冲区里面的内容真正写入到目标。
// 原因：因为处于效率考虑，操作系统并不是一个字节一个字节写入。所以先写到
// 缓冲区，实际上是byte[]。一般来说不用自己写flush()，缓冲区满了会自动刷新
// close()时也会刷新。
// 自己调用flush()的时候。聊天系统。

// 接受一个int型的参数，不过只取低8位作为一个字节写入。
public abstract void write(int b) throws IOException;
// 还可以一次写一个byte[]
// 1. 其它实现类 ByteArrayOutputStream在内存中模拟输出流
//   与输入类似，都时将一个byte[]当成目标
public class Main {
    public static void main(String[] args) throws IOException {
        byte[] data;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            output.write("Hello ".getBytes("UTF-8"));
            output.write("world!".getBytes("UTF-8"));
            data = output.toByteArray();
        }
        System.out.println(new String(data, "UTF-8"));
    }
}

```
7. Filter模式

Java的IO标准库使用Filter模式为InputStream和OutputStream增加功能：
可以把一个InputStream和任意个FilterInputStream组合；
可以把一个OutputStream和任意个FilterOutputStream组合。

8. classpath

把资源存储在classpath中可以避免文件路径依赖；
Class对象的getResourceAsStream()可以从classpath中读取指定资源；
根据classpath读取资源时，需要检查返回的InputStream是否为null。

9. 序列化

序列化是指把一个Java对象变成二进制内容，本质上就是一个byte[]数组。这样就可以写进文件了。
要序列化必须实现Serializable 接口
使用ObjectOutputStream负责把Java对象写进字节流。可以写基本类型。
使用ObjectInputStream负责从字节流读取Java对象。

serialVersionUID这个值是为了控制Java序列化版本的。 

10. Reader：所有字符流的超类

InputStream	                                  Reader
字节流，以byte为单位	                 字符流，以char为单位
读取字节（-1，0~255）：int read()	     读取字符（-1，0~65535）：int read()
读到字节数组：int read(byte[] b)	     读到字符数组：int read(char[] c)

```java
// 1. 常用实现
// （1）FileReader 打开文件读入字符
//     下面这个代码中未指明编码，如果文件里面只是ASCLL字符没问题，含有中文
//	   会出现乱码。如果不指定编码则与系统编码有关。
public void readFile() throws IOException {
    // 创建一个FileReader对象:
    Reader reader = new FileReader("src/readme.txt");//字符编码？？
    for (;;) {
        int n = reader.read(); // 反复调用read()方法，直到返回-1
        if (n == -1) {
            break;
        }
        System.out.println((char)n); // 打印char
    }
    reader.close(); // 关闭流
}
// （2）CharArrayReader 在内存中模拟一个Reader，实际上把char[]当成目标
try (Reader reader = new CharArrayReader("Hello".toCharArray())) {}
// （3）StringReader：与CharArrayReader类似，它是把String当成目标
```

11. Reader和InputStream的关系

普通的Reader实际上是基于InputStream构造的，因为Reader需要从InputStream中读入字节流（byte），然后，根据编码设置，再转换为char就可以实现字符流。

```java
// 从inputStream转换到reader，并且指定编码
// 持有InputStream:
InputStream input = new FileInputStream("src/readme.txt");
// 变换为Reader:
Reader reader = new InputStreamReader(input, "UTF-8");
```

12. Writer

Writer定义了所有字符输出流的超类：
FileWriter实现了文件字符流输出；

```java
try (Writer writer = new FileWriter("readme.txt", StandardCharsets.UTF_8)) {
    writer.write('H'); // 写入单个字符
    writer.write("Hello".toCharArray()); // 写入char[]
    writer.write("Hello"); // 写入String
}
```

CharArrayWriter和StringWriter在内存中模拟一个字符流输出。

```java
try (CharArrayWriter writer = new CharArrayWriter()) {
    writer.write(65);
    writer.write(66);
    writer.write(67);
    char[] data = writer.toCharArray(); // { 'A', 'B', 'C' }
}
```

Writer是基于OutputStream构造的，可以通过OutputStreamWriter将OutputStream转换为Writer，转换时需要指定编码。

# 9 网络编程

1. 计算机网络常识

（1）如果两台计算机在同一个网络里面，则他们可以直接通信。因为他们的IP地址的网络号是相同的。一个IP地址的网络号是用IP地址与子网掩码运算后得到的。
（2）网关：在同一个网络中的两台计算机可以直接通信，但是不在同一个网络中的计算机可以用路由器连接这两个网络。这个路由器就是网关，网关的作用就是负责把一个网络的数据包发送给另外一个网络。
所以说网卡的三个关键配置：IP地址、子网掩码、网关地址
（3）网络模型：OIS 由 ISO制定的标准网络模型
网络模型只是一个定义，简化网络各层操作，便于提供标准接口便与实现和维护。
（4）常见协议：
IP协议：一种分组交换传输协议；
IP协议只负责发送数据包，不保证数据和正确性。
TCP协议：一种面向连接，可靠传输的协议；
TCP之所以能保证数据可靠传输，是通过接受确认、超时重传机制。
UDP协议：一种无连接，不可靠传输的协议。

2. TCP编程

（1）Socket：一个应用程序通过一个Socket来建立一个远程连接，而Socket内部通过TCP/IP协议把数据传输到网络。
**Socket、TCP和部分IP的功能都是由操作系统提供的，不同的编程语言只是提供了对操作系统调用的简单的封装。**

使用Socket进行网络编程时，本质上就是两个进程之间的网络通信。其中一个进程必须充当服务器端，它会主动监听某个指定的端口，另一个进程必须充当客户端，它必须主动连接服务器的IP地址和指定端口，如果连接成功，服务器端和客户端就成功地建立了一个TCP连接，双方后续就可以随时发送和接收数据。

```java
// 1. 服务端：Java中ServerSocket来实现对指定IP和指定端口的监听
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(6666); // 监听指定端口
        System.out.println("server is running...");
        for (;;) {
        	// ss.accept() 会在这里等待连接
            Socket sock = ss.accept();
            System.out.println("connected from " + sock.getRemoteSocketAddress());
            Thread t = new Handler(sock);
            t.start();
        }
    }
}

class Handler extends Thread {
    Socket sock;

    public Handler(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        try (InputStream input = this.sock.getInputStream()) {
            try (OutputStream output = this.sock.getOutputStream()) {
                handle(input, output);
            }
        } catch (Exception e) {
            try {
                this.sock.close();
            } catch (IOException ioe) {
            }
            System.out.println("client disconnected.");
        }
    }

    private void handle(InputStream input, OutputStream output) throws IOException {
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        writer.write("hello\n");
        writer.flush();
        for (;;) {
            String s = reader.readLine();
            if (s.equals("bye")) {
                writer.write("bye\n");
                writer.flush();
                break;
            }
            writer.write("ok: " + s + "\n");
            writer.flush();
        }
    }
}
// 2. 客户端
public class Client {
    public static void main(String[] args) throws IOException {
    	// 连接指定服务器和端口
        Socket sock = new Socket("localhost", 6666); 
        try (InputStream input = sock.getInputStream()) {
            try (OutputStream output = sock.getOutputStream()) {
                handle(input, output);
            }
        }
        sock.close();
        System.out.println("disconnected.");
    }

    private static void handle(InputStream input, OutputStream output) throws IOException {
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in);
        System.out.println("[server] " + reader.readLine());
        for (;;) {
            System.out.print(">>> "); // 打印提示
            String s = scanner.nextLine(); // 读取一行输入
            writer.write(s);
            writer.newLine();
            writer.flush();
            String resp = reader.readLine();
            System.out.println("<<< " + resp);
            if (resp.equals("bye")) {
                break;
            }
        }
    }
}
```

3. UDP编程：端口号范围和TCP的一样，但是不冲突

```java
// 1. 服务端 DatagramSocket
DatagramSocket ds = new DatagramSocket(6666); // 监听指定端口
for (;;) { // 无限循环
    // 数据缓冲区:
    byte[] buffer = new byte[1024];
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    ds.receive(packet); // 收取一个UDP数据包
    // 收取到的数据存储在buffer中，由packet.getOffset(), packet.getLength()指定起始位置和长度
    // 将其按UTF-8编码转换为String:
    String s = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
    // 发送数据:
    byte[] data = "ACK".getBytes(StandardCharsets.UTF_8);
    packet.setData(data);
    ds.send(packet);
}
```

# 10 Java 常用功能

## 10.1 Java 日期时间

### 10.1.1 基础时间

- 