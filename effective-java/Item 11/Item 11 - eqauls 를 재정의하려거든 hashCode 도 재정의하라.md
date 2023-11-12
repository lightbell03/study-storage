# Item 11 - eqauls 를 재정의하려거든 hashCode 도 재정의하라

## hashCode

자바의 최상위 클래스인 Object 에서 hashCode() 가 정의되어 있으며 hashCode() 메서드는 객체의 hashCode를 리턴한다.

일반적으로 **각 객체의 주소값을 변환하여 생성한 객체의 고유한 정수값**이다.

### equals 를 재정의한 클래스 모두에서는 hashCode도 재정의해야 한다.

그렇지 않으면 hashCode 일반 규약을 어기게 되어 해당 클래스 인스턴스를 HashMap 이나 HashSet 같은 컬렉션 원소로 사용할 때 문제를 일으킨다.

**Object 명세 규약 일부**

- equals 비교에 사용되는 정보가 변경되지 않았다면, 애플리케이션이 실행되는 동안 그 객체의 hashCode 메서드는 몇 번을 호출해도 일관되게 항상 같은 값을 반환해야 한다.
(애플리케이션을 다시 실행한다면 값이 달라져도 상관없다.)
- equals(Object)가 두 객체를 같다고 판단했다면, 두 객체의 hashCode는 똑같은 값을 반환해야 한다.
- equals(Object)가 두 객체를 다르다고 판단했더라도, 두 객체의 hashCode가 서로 다른 값을 반환할 필요는 없다. 단, 다른 객체에 대해서는 다른 값을 반환해야 해시테이블의 성능이 좋아진다.

**hashCode 재정의를 잘못했을 때 큰 문제가 되는 것은 두번째이다. 논리적으로 같은 객체는 같은 해시코드를 반환해야 한다.**

equals로 두 객체가 논리적으로 같다고 할 수 있지만 Object의 기본 메서드인 hashCode 메서드는 이 둘이 서로 다르다고 판단해 서로 다른 값을 반환한다.

Item 10 에서 정의한 PhoneNumber 클래스

```java
public class PhoneNumber {
    private final short areaCode, prefix, lineNum;

    public PhoneNumber(int areaCode, int prefix, int lineNum) {
        this.areaCode = rangeCheck(areaCode, 999, "지역코드");
        this.prefix = rangeCheck(prefix, 999, "프리픽스");
        this.lineNum = rangeCheck(lineNum, 9999, "가입자 번호");
    }

    private static short rangeCheck(int val, int max, String arg) {
        if (val < 0 || val > max)
            throw new IllegalArgumentException(arg + ": " + val);
        return (short) val;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PhoneNumber))
            return false;

        PhoneNumber pn = (PhoneNumber) o;
        return pn.lineNum == lineNum && pn.prefix == prefix
                && pn.areaCode == areaCode;
    }
}
```

```java
Map<PhoneNumber, String> m = new HashMap<>();
m.put(new PhoneNumber(707, 867, 5409), "jenny");

String isJenny = m.get(new PhoneNumber(707, 867, 5309));
System.out.println(isJenny);
```

위의 코드에서 m.get(new PhoneNumber(707, 867, 5309)) 는 null 을 반환한다.

2개의 PhoneNumber 인스턴스가 사용

1. m.put(new PhoneNumber(707, 867, 6409), … )
2. m.get(new PhoneNumber(707, 857, 5309))

PhoneNumber 클래스는 hashCode 를 재정의하지 않았기 때문에 논리적 동치인 두 객체가 서로 다른 해시코드를 반환해 두 번째 규약을 지키지 못해 get 메서드는 엉뚱한 해시 버킷에 가서 객체를 찾은 것

두 객체가 서로 같은 버킷에 담겨 있어도 null 을 반환하는데 HashMap 은 해시코드가 다른 객체끼리는 동치성 비교를 시도조차 하지 않기 때문이다.

**최악의 hashCode 구현**

```java
@Override
public int hashCode() {
		return 42;
}
```

동치인 모든 객체에 똑같은 해시코드를 반환한다. 하지만 모든 객체에게 똑같은 값만 내어주어서 모든 객체가 해시테이블의 버킷 하나에 담겨 마치 연결 리스트처럼 동작한다.

시간 복잡도가 O(1) 인 해시테이블이 O(n)으로 느려져 객체가 많아지면 쓰기 힘들다.

### hashCode 작성 요령

1. int 변수 result 를 선언한 후 값 c로 초기화한다. 이때 c 는 해당 객체의 첫번째 핵심 필드를 단계 2.a 방식으로 계산한 해시코드다.
2. 해당 객체의 나머지 핵심 필드 f 각각에 대해 다음 작업을 수행한다.
    1. 해당 필드의 해시코드 c 를 계산한다.
        1. 기본 타입 필드라면, Type.hashCode(f)를 수행한다. 여기서 Type은 해당 기본 타입의 박싱 클래스다.
        2. 참조 타입 필드면서 이 클래스의 equals 메서드가 이 필드의 equals 를 재귀적으로 호출해 비교한다면, 이 필드의 hashCode를 재귀적으로 호출한다. 계산이 복잡해질 것 같으면 이 필드의 표준형(canonical representation)을 만들어 그 표준형의 hashCode를 호출한다. 필드의 값이 null 이면 0을 사용한다.(전통적으로 0을 사용)
        3. 필드가 배열이라면, 핵심 원소 각각을 별도 필드처럼 다룬다. 이상의 규칙을 재귀적으로 적용해 각 핵심 원소의 해시코드를 계산한 다음, 단계 2.b 방식으로 갱신한다. 배열에 핵심 원소가 하나도 없다면 단순히 상수(0)를 사용한다. 모든 원소가 핵심원소라면 Arrays.hashCode를 사용한다.
    2. 단계 2.a 에서 계산한 해시코드 c 로 result를 갱신한다.
3. result 를 반환한다.
- 파생 필드는 해시코드 계산에서 제외가능
- **equals 비교에 사용되지 않은 필드는 ‘반드시’ 제외해야 한다.** - 규약 2를 어길 위험이 있다.

PhoneNumber 클래스에 적용

```java
@Override
public int hashCode() {
		int result = Short.hashCode(areaCode);
		result = 31 * result + Short.hashCode(prefix);
		result = 31 * result + Short.hashCode(lineNum);
		return result;
}
```

핵심 필드 3개만을 사용해 간단한 계산만 수행한다.

- 해시 충돌이 더 적은 방법을 사용해야 한다면 구아바의 com.google.common.hash.Hashing 을 참고

**한 줄짜리 hashCode 메서드**

```java
@Override
public int hashCode() {
		return Objects.hash(linNum, prefix, areaCode);
}
```

Objects 클래스에서 임의의 개수만큼 객체를 받아 해시코드를 계산해 주는 정적 메서드인 hash를 제공해준다. 이 메서드는 성능에 민감하지 않은 상황에서만 사용하자.

**해시코드를 지연 초기화하는 hashCode 메서드 - 스레드 안정성까지 고려해야 한다.**

```java
private int hashCode;

@Override
public int hashCode() {
		int result = hashCode;
		if(result == 0) {
				result = Short.hashCode(areaCode);
				result = 31 * result + Short.hashCode(prefix);
				result = 31 * result + Short.hashCode(lineNum);
				hashCode = result;
		}
		return result;
}
```

해시 코드의 계산 비용이 커 매번 새로 계산하기 보다는 캐싱하는 방식

- 객체가 주로 해시의 키로 사용될 것 같다면 인스턴스가 만들어질 때 해시 코드를 계산해둬야 한다.
- 해시의 키로 사용되지 않는 경우 hashCode 가 처음 불릴 때 계산하는 지연 초기화 전략을 사용하자.
필드를 지연 초기화하려면 그 클래스를 스레드 세이프 하게 만들도록 신경써야 한다.

---

## 주의

**성능을 높인다고 해시코드를 계산할 때 핵심 필드를 생략해서는 안 된다.**

- 속도는 빨라지지만, 해시 품질이 나빠져 해시테이블의 성능을 심각하게 떨어뜨릴 수도 있다.

**hashCode가 반환하는 값의 생성 규칙을 API 사용자에게 자세히 공표하지 말자. 클라이언트가 이 값에 의지하지 않게 되고 추후에 계산 방식을 바꿀 수 있다.**

### 정리

> equals를 재정의할 때는 hashCode도 반드시 재정의해야 한다. 그렇지 않으면 프로그램이 제대로 동작하지 않을 것이다. 재정의한 hashCode는 Object의 API 문서에 기술된 일반 규약을 따라야 하며, 서로 다른 인스턴스라면 되도록 해시코드도 서로 다르게 구현해야 한다.
>