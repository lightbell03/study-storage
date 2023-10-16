# item 5 - 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

# 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

많은 클래스들은 하나 이상의 자원을 사용한다. 하지만 사용하는 자원들에 따라 동작이 달라지는 클래스에는 **정적 유틸리티 클래스**나 **싱글턴 방식이 적합하지 않다**.

### 정적 유틸리티를 잘못 사용한 예 - 유연하지 않고 테스트하기 어렵다.

```java
public class SpellChecker {
    private static final Lexicon dictionary = ...;

    private SpellChecker() {}

    public static boolean isValid(String word) { ... }
    public List<String> suggestions(String typo) { ... }
}
```

### 싱글턴을 잘못 사용한 예 - 유연하지 않고 테스트하기 어렵다.

```java
public class SpellChecker {
    private static final Lexicon dictionary = ...;

    private SpellChecker(...) {}
    public static SpellChecker INSTANCE = new SpellChecker(...);

    public static boolean isValid(String word) { ... }
    public List<String> suggestions(String typo) { ... }
}
```

두 방식 모두 사전을 단 하나만 사용한다고 가정한다는 점에서 그리 훌륭하지 않다.
사전이 언어별로 따로 있고 테스트용 사전도 필요할 수도 있다. 사전 하나로 이 모든 쓰임에 대응하기에는 너무 어려운 방법이다.

필드에서 final 한정자를 제거하고 다른 사전으로 교체하는 메서드를 추가할 수 있지만 이 방식은 어색하고 오류를 내기 쉬우며 멀티 쓰레드 환경에서는 쓸 수 없다.

---

## 의존 객체 주입

위의 문제점을 간단하게 해결하며 클래스가 여러 자원을 지원하며, 클라이언트가 원하는 자원을 사용하게 해주는 패턴이 있다.

### 인스턴스를 생성할 때 생성자에 필요한 자원을 넘겨주는 방식

```java
public class SpellChecker {
    private static final Lexicon dictionary;

    private SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }

    public static boolean isValid(String word) { ... }
    public List<String> suggestions(String typo) { ... }
}
```

의존 객체 주입의 한 형태로, 클래스를 생성할 때 의존 객체를 주입해주면 된다.

예에서는 dictionary 하나의 자원만 사용하지만, 자원이 몇 개든 의존 관계가 어떻든 상관없이 잘 동작한다.
불변을 보장하며 여러 클라이언트가 의존 객체들을 안심하고 공유할 수도 있다.

### 팩터리 메서드 패턴 - 변형

의존 관계 주입 패턴의 변형으로 생성자에 자원 팩토리를 넘겨주는 방식
팩토리: 호출할 때마다 특정 타입의 인스턴스를 반복해서 만들어주는 객체

**ex) Supplier<T> 인터페이스가 팩토리를 표현한 완벽한 예**

```java
@FunctionalInterface
public interface Supplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();
}
```

Supplier<T>를 입력으로 받는 메서드는 일반적으로 한정적 와일드카드 타입을 사용해 팩토리의 타입 매개변수를 제한해야 한다. 이 방식을 사용해 클라이언트는 자신이 명시한 타입의 하위 타입이라면 무엇이든 생성할 수 있는 팩토리를 넘길 수 있다.

```java
public class SpellChecker {
    private final Lexicon dictionary;

    private SpellChecker(Supplier<? extends Lexicon> dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary).get();
    }

    public static boolean isValid(String word) { ... }
    public List<String> suggestions(String typo) { ... }
}

interface Lexicon {}

class KoreanDictionary implements Lexicon {

}
class EnglishDictionary implements Lexicon {
    
}

class TestDictionary implements Lexicon {
    
}
```

의존 객체 주입이 유연성과 테스트 용이성을 개선해주지만 의존성 매우 많은 큰 프로젝트에서는 코드가 어지럽게 될 수 있다. 이런 어지러움은 대거(Dagger), 주스(Guice), 스프링(Spring) 같이 의존 객체를 직접 주입하도록 설계된 API를 알맞게 응용해 사용하는 프레임워크를 이용해 해결할 수 있다.

---

### 정리

> 클래스가 내부적으로 하나 이상의 자원에 의존하고, 그 자원이 클래스 동작에 영향을 준다면 싱글턴과 정적 유틸리티 클래스는 사용하지 않는 것이 좋다. 또한 이 자원들을 클래스가 직접 만들게 해서도 안 된다.
필요한 자원을(혹은 그 자원을 만들어주는 팩터리를) 생성자(혹은 정적 팩터리나 빌더에)에 넘겨주자. 의존객체 주입이라 하는 이 기법은 클래스의 유연성, 재사용성, 테스트 용이성을 기막히게 개선한다.
>
