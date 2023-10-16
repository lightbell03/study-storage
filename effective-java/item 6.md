# item 6 - 불필요한 객체 생성을 피하라

# 불필요한 객체 생성을 피하라

똑같은 기능의 객체를 매번 생성하기보다는 객체 하나를 재사용하는 편이 나을 때가 많다. 특히 불변 객체는 언제든지 재사용할 수 있다.

### String 인스턴스

```java
String s = new String("bikini");
```

위 문장은 실행될 때마다 String 인스턴스를 새로 만드는 완전히 쓸데없는 행위이다.
이 문장이 반복문이나 빈번이 호출되는 메서드 안에 있다면 쓸데없는 String 인스턴스가 수백만 개 만들어질 수 있다.

```java
String s = "bikini"
```

위 문장은 새로운 인스턴스를 매번 만드는 대신 하나의 String 인스턴스를 사용한다. Heap 영역 내 String Constant Pool에 저장되어 재사용
![Untitled](https://github.com/lightbell03/effective-java/assets/70000247/1c178d5b-349e-419f-932f-76ba75aa9396)

또한 같은 가상 머신 안에서 이와 똑같은 문자열 리터럴을 사용하는 모든 코드가 같은 객체를 재사용함이 보장된다.

### 정적 팩토리 매서드를 제공하는 불변 클래스

예를 들어서 new Boolean(String) 대신 Boolean.valueOf(String) 팩터리 메서드를 사용해 불필요한 객체 생성을 피할 수 있으며 더 좋다.

생성자는 호출할 때마다 새로운 객체를 만든다. 만약 생성비용이 아주 비싼 객체를 여러번 생성하는 코드를 호출하게 되면 성능이 떨어진다. 자신이 만든 객체가 비싼객체인지 명확히 알고 사용하면 좋겠지만 매번 명확히 알 수 없다.

하지만 팩토리 매서드는 전혀 불필요한 객체 생성을 하지 않는다.

예를 들어 매개변수로 주어진 문자열이 유효한 로마 숫자인지 확인하는 메서드가 존재한다고하자.

```java
public static boolean isRomanNumeral(String s) {
    return s.matches("^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
}
```

String.matches는 정규표현식으로 문자열 형태를 확인하는 가장 쉬운 방법이다. 하지만 성능이 중요한 상황에서 반복해 사용하기엔 적합하지 않다. 왜냐면 내부에서 만든 “…” 안의 Pattern 인스턴스는 한번 쓰고 버려져서 곧바로 가비지 컬렉션 대상이 되기때문이다.
![Untitled 1](https://github.com/lightbell03/effective-java/assets/70000247/180cf1e9-7e98-4ece-9cfb-45ccb918a33e)

![Untitled 2](https://github.com/lightbell03/effective-java/assets/70000247/58140f20-12ca-43ba-a431-7bbe70a43084)

![Untitled 3](https://github.com/lightbell03/effective-java/assets/70000247/26e66bf5-8638-41d1-908b-ccaddeb2e5a7)


거슬러 올라가 보면 new Pattern(regex, 0); 으로 객체를 생성하는 것을 확인할 수 있다.

> Pattern은 입력받은 정규표현식에 해당하는 유한 상태머신을 만들기 때문에 인스턴스 생성 비용이 높다.
> 

그래서 성능을 개선하기 위해 필요한 정규표현식을 표현하는 Pattern(불변) 인스턴스를 클래스 초기화과정에서 직접 생성해 캐싱해두고, 나중에 Pattern 정규표현식을 사용하는 메서드가 호출될 때 이 Pattern 인스턴스를 재사용한다.

```java
public class RomanNumerals {
    private static final Pattern ROMAN = Pattern.compile("^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
    
    static boolean isRomanNumeral(String s) {
        return ROMAN.matcher(s).matches();
    }
}
```

인스턴스를 재활용할 때와 하지않을때 시간 차이를 테스트 해보았다. 성능상 인스턴스를 재사용 했을때 훨씬 빠른 것을 볼 수 있다.

![Untitled 4](https://github.com/lightbell03/effective-java/assets/70000247/6e16272b-3779-4119-9b6b-25f9ab795603)

인스턴스를 재사용하는 방식을 사용하게 된다면 클래스가 초기화 된 후 이 인스턴스를 사용하는 메서드가 한번도 호출되지 않는다면 쓸데없이 객체가 생성된 것같다. 그래서 메서드가 처음 호출될 때 필드를 초기화 하는 지연 초기화로 불필요한 초기화를 없앨 수는 있다. 하지만 코드가 복잡해지고 성능은 크게 개선되지 않으므로 사용은 안해도 될 것 같다.

---

### 오토박싱

불필요한 객체를 만들어내는 또 다른 예이다.

우리는 프로그램을 개발할 때 기본타입(primary type)과 박싱된 기본타입(wrapper type)을 섞어 개발한다. 

- 기본타입: int, long, double, float …
- 박싱된 기본타입: Integer, Long, Double …

이때 오토박싱은 두 타입을 섞어 사용할 때 자동으로 상호 변환해주는 기술이다. **오토박식은 기본 타입과 그에 대응하는 박싱된 기본 타입의 구분을 흐겨주지만, 완전히 없애주는 것은 아니다.** 의미상으로는 비슷해 보이지만 성능에서는 큰 차이가 나타난다.

```java
private static long sum() {
    Long sum = 0L;
    for(long i = 0; i<=Integer.MAX_VALUE; i++) {
        sum += i;
    }

    return sum;
}
```

이 코드는 한글자 하나로 성능이 크게 갈려진다. 바로 L → l 이다. 반복문을 실행할 때 long 타입인 i 가 Long 타입인 sum 에 더해질 때마다 불필요한 Long 인스턴스가 약 $2^{31}$개 생성된다. 이렇게 되면 성능이 크게 저하가 된다.

따라서 **박싱된 기본 타입보다는 기본타입을 사용하고, 의도치 않은 오토박싱이 숨어들지 않도록 주의**하자.

> 프로그램의 명확성, 간결성, 기능을 위해서 객체를 추가로 생성하는 것은 일반적으로 좋은 예이다. 하지만 아주 무거운 객체가 아닌 경우는 단순히 객체 생성을 피하고자 자신만의 객체 풀을 만들지는 말자.
>
