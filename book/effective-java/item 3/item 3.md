# item 3

## private 생성자나 열거 타입으로 싱글턴임을 보증하라

## 싱글턴이란?

- 인스턴스를 오직 하나만 생성할 수 있는 클래스
- 함수와 같은 무상태(stateless) 객체나 설계상 유일해야 하는 시스템 컴포넌트 등…

### 장점

- 고정된 메모리 영역을 얻어 한번의 인스턴스 생성으로 인스턴스를 생성하기 때문에 메모리 낭비를 방지할 수 있다.
- 싱글턴으로 만들어진 클래스의 인스턴스는 전역이기 때문에 다른 클래스의 인스턴스들이 데이터를 공유하기 쉽다.
- 두 번째 이용시 부터는 객체 로딩 시간이 줄어 성능이 좋아진다.

### 단점

- 클래스를 싱글턴으로 만들면 이를 사용하는 클라이언트를 테스트하기가 어려워질 수 있다.
타입을 인터페이스로 정의한 후 그 인터페이스를 구현해 만든 싱글턴이 아니라면 싱글턴 인스턴스를 가짜(mock) 구현으로 대체할 수 없다.
- private 생성자를 가지고 있기 때문에 상속할 수 없다. → 객체지향의 장점인 상속과 다형성 사용이 불가능하다.
- 서버환경에서는 싱글턴이 하나만 만들어지는 것을 보장하지 못한다. → 서버가 여러개인 경우, 멀티쓰레드 환경
- 많은 데이터를 공유시킬 경우나 많은 일을 맡게되면 다른 클래스의 인스턴스들 간에 결합도가 높아져 “개방-폐쇄 원칙”을 위배한다.

---

## 싱글턴을 만드는 방법

보통 두 가지 방법으로 만든다. 두 방법 모두 생성자는 private으로 감춰두고, 인스턴스에 접근할 수단으로 public static 멤버를 하나 마련해둔다.

### 1. **pulbic static 멤버가 final 필드인 방식**

```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis() {}

    public void leaveTheBuilding(){}
}
```

private 생성자는 public static final 필드인 Elvis.INSTANCE 를 초기화할 때 딱 한 번만 호출된다. 다른 접근할 생성자나 메서드가 없으므로 인스턴스가 전체 시스템에서 단 하나뿐임이 보장된다.

```java
Elvis elvis1 = Elvis.INSTANCE;
Elvis elvis2 = Elvis.INSTANCE;

System.out.println(elvis1 == elvis2); //true
```

> 예외
권한이 있는 클라이언트가 리플렉션 API 인 AccessibleObject.setAccessible 을 사용해 private 생성자를 호출할 수 있다.
리플렉션 API: java.lang.refelct, 객체가 주어지면, 해당 클래스의 인스턴스를 생성하거나 메서드를 호출하거나, 필드에 접근할 수 있다.
> 

```java
Elvis elvis1 = Elvis.INSTANCE;
Constructor<Elvis> elvisConstructor = (Constructor<Elvis>) elvis1.getClass().getDeclaredConstructor();
//접근 허용
elvisConstructor.setAccessible(true);

Elvis elvis2 = elvisConstructor.newInstance();
System.out.println(elvis1 == elvis2); //false
```

생성자를 수정해 인스턴스가 생성되어 있는데 생성자를 이용해 객체가 생성될 때 예외를 날려주는 처리를 해주면 된다.

```java
private Elvis() {
    if(INSTANCE != null) {
       throw new RuntimeException("인스턴스가 생성되어 있습니다!!");
    }
}
```

**장점**

- 해당 클래스가 싱글턴임이 API에 명백히 드러난다.
- 간결하다.

### 2. **정적 팩토리 매서드를 public static 멤버로 제공**

```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis() {}
    public static Elvis getInstance() {
        return INSTANCE;
    }
    public void leaveTheBuilding(){}
}
```

Elvis.getInstance 는 항상 같은 객체의 참조를 반환하므로 제2의 Elvis 인스턴스는 결코 만들어지지 않는다.

(리플렉션을 통한 예외는 똑같이 적용된다.)

**장점**

- API 를 바꾸지 않고 싱글턴이 아니게 변경할 수 있다.
private static 으로 메모리에 하나만 생성하지 않고 그냥 private 으로만 선언해 API 를 호출할 때마다 새로운 인스턴스를 제공할 수 있다.
유일한 인스턴스를 반환하던 팩토리 메서드가 호출하는 스레드별로 다른 인스턴스를 넘겨주게 할 수 있다.
- 원한다면 정적 팩토리를 제네릭 싱글턴 팩토리로 만들 수 있다.
- 정적 팩토리의 메서드 참조를 공급자(supplier)로 사용할 수 있다.
    
    ```java
    Supplier<Elvis> supplier = Elvis::getInstance;
    Elvis elvis = supplier.get();
    ```
    

두 방식으로 만든 싱글턴 클래스를 직렬화한 후 역직렬화할 때 역직렬화할 때마다 새로운 인스턴스가 만들어진다.

싱글턴 클래스를 직렬화하려면 단순히 Serializable을 구현한다고 선언하는 것만으로는 부족하다. 따라서 모든 인스턴스 필드를 일시적(transient, 직렬화 제외)이라고 선언하고 readResolve 메서드를 제공해야핟나.

```java
public class Elvis implements Serializable {
    public static final transient Elvis INSTANCE = new Elvis();
    private Elvis() {}
    public static Elvis getInstance() {
        return INSTANCE;
    }

    public Elvis readResolve() {
        return INSTANCE;
    }
    public void leaveTheBuilding(){}
}
```

‘진짜’ Elvis 를 반환하고, 가짜 Elvis 는 가비지 컬렉터에 맡긴다.

> transient
• `transient`는 Serialize하는 과정에 제외하고 싶은 경우 선언하는 키워드입니다.
[https://nesoy.github.io/articles/2018-06/Java-transient](https://nesoy.github.io/articles/2018-06/Java-transient)
> 

### 3. 원소가 하나인 열거 타입을 선언하는 방법

```java
public enum Elvis {
    INSTANCE;
    
    public void leaveTheBuilding() {}
}
```

대부분의 상황에서는 **원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법**이다. 더 간결하고 복잡한 직렬화 상황이나 리플렉션 공격에서도 제2의 인스턴스가 생기는 일을 완벽히 막아준다.

(단, 만들려는 싱글턴이 Enum 외의 클래스를 상속해야 한다면 이 방법은 사용할 수 없다.)

---

### 참고

> Effective Java 3/E
[https://devmoony.tistory.com/43](https://devmoony.tistory.com/43)
[https://velog.io/@lychee/이펙티브-자바-아이템-3.-private-생성자나-열거-타입으로-싱글턴임을-보증하라](https://velog.io/@lychee/%EC%9D%B4%ED%8E%99%ED%8B%B0%EB%B8%8C-%EC%9E%90%EB%B0%94-%EC%95%84%EC%9D%B4%ED%85%9C-3.-private-%EC%83%9D%EC%84%B1%EC%9E%90%EB%82%98-%EC%97%B4%EA%B1%B0-%ED%83%80%EC%9E%85%EC%9C%BC%EB%A1%9C-%EC%8B%B1%EA%B8%80%ED%84%B4%EC%9E%84%EC%9D%84-%EB%B3%B4%EC%A6%9D%ED%95%98%EB%9D%BC)
>