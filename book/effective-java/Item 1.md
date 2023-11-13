# Item 1

# 생성자 대신 정적 팩토리 메서드를 고려하라

클래스의 인스턴스를 얻는 방법에는 public 생성자를 이용하는 방법 외에 정적 팩토리 메서드(static factory method) 를 이요해 얻을 수 있다. 

### 생성자를 이용해 인스턴스 생성

```java
public class Item {
    public Item() {}
}
```

### 정적 팩토리 메서드를 이용해 인스턴스 생성

```java
public class Item {
    public Item() {}

    public static Item getInstance() {
        return new Item();
    }
}
```

정적 팩토리 메서드란 static을 사용하여 메서드를 만들고, new 와 같이 생성자를 이용한 객체생성을 하지 않고 사용할 수 있는 메서드이다.

---

### 정적 팩토리 메서드가 생성자보다 좋은 점

1. **이름을 가질 수 있다.**

생성자에 넘기는 매개변수와 생성자 자체만으로는 반환될 객체의 특성을 제대로 설명하기 힘들다. 하지만 정적 팩토리 는 이름만 잘 지으면 반환될 객체의 특성을 쉽게 묘사할 수 있다.

예를 들어 생성자인 `BigInteger(int, int, Random)`과 정적 팩토리 메서드인 `BigInteger.probablePrime` 을 비교 해보면 후자가 소수인 BigInteger 를 반환한다는 의미를 더 잘 설명해 준다. 

```java
public class Item {
    private String name;
    private String code;
    public Item(String name) {
        this.name = name;
    }
		//에러 발생
    public Item(String code) {
        this.code = code;
    }
}
```

또한 하나의 시그니처로는 생성자를 하나만 만들 수 있다. 위의 코드처럼 생성자의 매개변수 의미는 서로 다르겠지만 매개변수의 수와 타입이 같아 중복되는 생성자로 인식되어 에러가 발생하게 된다. 입력 매개변수들의 순서를 다르게 한 생성자를 새로 추가하는 식으로 제한을 피할 수 있지만 좋지않은 생각이다.

```java
public class Item {
    private String name;
    private String code;
    public Item() {}

    public static Item ItemWithName(String name) {
        Item item = new Item();
        item.name = name;
        return item;
    }

    public static Item ItemWithCode(String code) {
        Item item = new Item();
        item.code = code;
        return item;
    }
}
```

위의 코드 처럼 정적 팩토리 매서드를 사용하게 되면 메서드의 이름으로 유추를 할 수 있으며 하나의 시그니처로 여러개의 인스턴스를 생성할 수 있는 방법을 제공할 수 있다.

1. **호출될 때마다 인스턴스를 새로 생성하지는 않아도 된다.**

불변 클래스는 인스턴스를 미리 만들어 놓거나 새로 생성한 인스턴스를 캐싱하여 재활용하는 식으로 불필요한 객체 생성을 피할 수 있다. 위에서 Boolean.valueOf(boolean) 메서드는 객체를 아예 생성하지 않아 같은 객체가 자주 요청되는 상황이라면 성능을 상당히 끌어올려 줄 수 있다.

```java
Boolean boolean1 = Boolean.valueOf(true);
Boolean boolean2 = Boolean.valueOf("true");
Boolean boolean3 = new Boolean(true);
```

위의 3 Boolean 래퍼 클래스에서 boolean1 과 boolean2 는 같은 인스턴스이지만 boolean3 의 경우는 boolean1, boolean2 와는 다른 인스턴스가 된다.

```java
public class Item {
    private static final Itemitem= new Item();
    private Item() {}
    public static Item getInstance() {
        returnitem;
    }

    public static void main(String[] args) {
        Item item = Item.getInstance();
    }
}
```

반복되는 요청에 같은 객체를 반환하는 식으로 정적 팩토리 방식의 클래스는 언제 어느 인스턴스를 살아 있게 할지 철저히 통제할 수 있다. → 인스턴스 통제***(instance-controlled)*** 클래스

싱글턴(singleton), 인스턴스화 불가(noninstantable), 동치인 인스턴스가 단 하나뿐임을 보장(a == b 일 때만 a.equals(b) 가 성립), 플라이웨이트 패턴의 근간, 열거 타입에서는 인스턴스가 하나만 만들어짐을 보장.

1. **반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.**

반환할 객체의 클래스를 자유롭게 선택할 수 있게 하는 **유연성**을 제공. 이를 응용해 구현 클래스를 공개하지 않고도 그 객체를 반환할 수 있다.

리턴타입의을 인터페이스로 지정하고 구현 클래스를 노출시키지 않고 그 객체를 반환할 수 있다. 이는 API 를 작게 유지할 수 있게하며 API를 사용하기 위해 익혀야 하는 개념의 수와 난이도도 낮춰준다. 명시한 인터페이스대로 동작하는 객체를 얻을 것임을 알기에 굳이 문서를 찾아가며 실제 구현 클래스가 무엇인지 몰라도 된다. 이는 인터페이스 기반 프레임워크를 만드는 핵심 기술이기도 하다.

대표적인  예로 java.util.Collections 가 있다.

1. **입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.**

반환 타입의 하위 타입이기만 하면 어떤 클래스의 객체를 반환하든 상관없다 → 사용자는 반환하는 인스턴스가 어떤 클래스인지 알 필요가 없어지며 인터페이스 기반의 구현이 이루어진다.

`EnumSet` 클래스는 public 생성자 없이 오직 정적 팩토리만 제공하는데 원소의 수에 따라 두 가지 하위 클래스 중 하나의 인스턴스르 반환한다. 원소가 64개 이하면 `RegularEnumSet`, 65 개 이상이면 `JumboEnumSet`의 인스턴스를 반환한다.

```java
public class Item{
    enum Number {
ONE,TWO,THREE
}

    enum LargeNumber {
e1,e2,e3,e4,e5,e6,e7,e8,e9,e10,
e11,e12,e13,e14,e15,e16,e17,e18,e19,e20,
e21,e22,e23,e24,e25,e26,e27,e28,e29,e30,
e31,e32,e33,e34,e35,e36,e37,e38,e39,e40,
e41,e42,e43,e44,e45,e46,e47,e48,e49,e50,
e51,e52,e53,e54,e55,e56,e57,e58,e59,e60,
e61,e62,e63,e64,e65,e66

}

    public static void main(String[] args) {
        EnumSet<Number> numbers = EnumSet.allOf(Number.class);
        EnumSet<LargeNumber> largeNumbers = EnumSet.allOf(LargeNumber.class);

        System.out.println("EnumSet<Number> = " + numbers.getClass());
        System.out.println("EnumSEt<LargeNumber> = " + largeNumbers.getClass());
    }
}
```

![Untitled](https://user-images.githubusercontent.com/70000247/218750383-4e7d2b18-0345-4f8f-9f34-de9b209b07ed.png)


```java
public interface NumberInterface {
    static NumberInterface of(int number) {
        NumberInterface instance;

        if(number > 50) {
            return new OverFiftyNumber(number);
        }
        else {
            return new UnderFiftyNumber(number);
        }
    }

    String getValue();

    class UnderFiftyNumber implements NumberInterface{
        private int value;

        private UnderFiftyNumber(int value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return this.getClass().toString() + " value = " + value;
        }
    }

    class OverFiftyNumber implements NumberInterface {
        private int value;

        private OverFiftyNumber(int value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return this.getClass().toString() + " value = " +value;
        }
    }
}

```

```java
public class Item{

    public static void main(String[] args) {
        NumberInterface underFifty = NumberInterface.of(39);
        NumberInterface overFifty = NumberInterface.of(100);

        System.out.println("underFifty = " + underFifty.getValue());
        System.out.println("overFifty = " + overFifty.getValue());
    }
}
```

![Untitled 1](https://user-images.githubusercontent.com/70000247/218750319-b73ac261-9b4c-4e4c-8a91-d6e96e2c7d67.png)



`NumberInterface` 인터페이스에 of 스태틱 팩토리 메서드를 이용해 51 이상이면 `OverFiftyNumber` 클래스를 50 이하이면 `UnderFiftyNumber` 클래스를 반환해 주고 있다. `main` 함수에서 사용자는 구체 클래스를 알지 못하여도 인터페이스가 알아서 클래스를 결정해 반환해줄 수 있게 된다.

1. **정적 팩토리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.**

서스 제공자 프레임워크(service provider framework)를 만드는 근간이 된다.

대표적으로 JDBC(Java Database Connectivity) 가 있다. 서비스 제공자 프레임워크에서의 제공자(provider) 는 서비스의 구현체이다. 이 구현체들을 클라이언트에 제공하는 역할을 프레임워크가 통제하여, 클라이언트를 구현체로부터 분리해준다.

서비스 제공자 프레임워크는 3개의 핵심 컴포넌트로 이뤄진다.

- 구현체의 동작을 정의하는 서비스 인터페이스(service interface)
- 제공자가 구현체를 등록할 때 사용하는 제공자 등록 API(provider registration API)
- 클라이언트가 서비스의 인스턴스를 얻을 때 사용하는 서비스 접근 API(service access API) → 원하는 구현체의 조건을 명시할 수 있으며 조건을 명시하지 않으면 기본 구현체를 반환하거나 지원하는 구현체들을 하나씩 돌아가며 반환한다.

3개의 핵심 컴포넌트와 더불어 종종 네번째 컴포넌트가 쓰이기도 한다.

- 서비스 제공자 인터페이스(service provider interface) → 섯비스 인터페이스의 인스턴스를 생성하는 팩토리 객체를 설명

JDBC 는 MySQL, OracleDB, PostgreSQL, MariaDB 등 다양한 Database 를 프레임워크로 관리할 수 있다. Connection 이 서비스 인터페이스 역할, DriverManager.registerDriver() 가 제공자 등록 API 역할, DriverManager.getConnection() 이 서비스 접근 API 역할, Driver 가 서비스 제공자 인터페이스 역할을 수행한다. getConnection() 을 쓸때 실제 return 되어 나오는 객체는 DB Driver 마다 다르다.

---

### 정적 팩토리의 단점

1. **상속을 하려면 public 이나 protected 생성자가 필요하니 정적 팩토리 메서드만 제공하면 하위 클래스를 만들 수 없다.**

즉 컬렉션 프레임워크의 유틸리티 구현 클래스들은 상속할 수 없다는 것이다. 정적 팩토리 메서드만 제공하는 경우 생성자를 private 으로 접근제한을 두어 생성자를 통한 인스턴스 생성을 막는 경우가 많아 super() 를 부를 수 없어 상속을 할 수 없다.

그러나 이 제약은 상속보다 컴포지션을 사용하도록 유도하고 불변타입으로 만들려면 이 제약을 지켜야 한다는 점에서 오히려 장점이 될 수 있다.

- 컴포지션: 기존 클래스를 확장하는 대신에 새로운 클래스를 만들고 private 필드로 기존 클래스의 인스턴스를 참조하는 방식
[https://iyoungman.github.io/designpattern/Inheritance-and-Composition/](https://iyoungman.github.io/designpattern/Inheritance-and-Composition/)
1. **정적 팩토리 메서드는 프로그래머가 찾기 어렵다.**

생성자처럼 API 설명에 명확히 드러나지 않아 사용자는 정적 팩터리 메서드 방식 클래스를 인스턴스화할 방법을 알아내야 한다.

---

### 정적 팩토리 메서드 명명 방식들

- from: 매개변수를 하나 받아서 해당타입의 인스턴스를 반환하는 형변환 메서드
ex) Date d = Date.from(instance);
- of: 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드
ex) Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING);
- valueOf: form 과 of 의 더 자세한 버전
ex) BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);
- intance or getInstance: (매개변수를 받는다면) 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 보장하지 않는다.
ex) StackWalker luke = StackWalker.getInstance(options);
- create or newInstance: instance 혹은 getInstance 와 같지만, 매번 새로운 인스턴스를 생성해 반환함을 보장한다.
ex) Object newArray = Array.newInstance(classObject, arrayLen);
- getType: getInstance 와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩토리 메서드를 정의할 때 쓴다. “Type” 은 팩토리 매서드가 반환할 객체의 타입이다.
ex) FileStorefs = Files.getFileStore(path)
- newType: newInstance 와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩토리 메서드를 정의할 때 쓴다. “Type” 은 팩토리 메서드가 반환할 객체의 타입이다.
ex) BufferedReader br = Files.newBufferedReader(path);
- type: getType 과 newType의 간결한 버전
ex) List<Complaint> litany = Collections.list(legacyLitany);

---

## 정리

> **정적 팩토리 메서드와 public 생성자는 각자의 쓰임새가 있으니 상대적인 장단점을 이해하고 사용하자.
정적 팩토리를 사용하는 것이 유리한 경우가 더 많으므로 무작정 public 생성자를 제공하던 습관이 있다면 고치자!!**
> 

(2023.02.09)
