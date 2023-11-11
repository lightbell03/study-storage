# item 10 - equals는 일반 규약을 지켜 재정의하라

equals 메소드는 자바 클래스의 최상위 클래스인 Object 클래스에 정의되어 있는 메소드로 하위 클래스에서 오버라이딩으로 재정의하기 쉬워 보이지만 자칫 잘못사용하면 끔찍한 결과를 초래한다.

## equals 를 재정의 하지 않아야 하는 경우

잘못 사용하지 않기 위해 가장 쉬운 방법은 아예 재정의 하지 않는 방법이다. 다음과 같은 상황이면 **재정의 하지 않는 것이 최선이다.**

### 1. 각 인스턴스가 본질적으로 고유하다.

- 값을 표현하는게 아닌 동작하는 개체를 표현하는 클래스
- Integer, Long, String 같이 값을 표현하는 클래스가 아닌 Thread 처럼 동작하는 클래스

### 2. 인스턴스의 ‘논리적 동치성(logical equality)’을 검사할 일이 없다.

- 논리적 동치성을 검사하는 방법을 원하지 않거나 필요하지 않다고 판단이 되면 equals 를 재정의 하지 않고 Object 의 equals 만으로 해결할 수 있다.
- 논리적 동치성 ex)
    - java.util.regex.Pattern 은 equals 를 재정의해서 두 Pattern 의 인스턴스가 같은 정규표현식을 나타내는지를 논리적으로 검사

### 3. 상위 클래스에서 재정의한 equals가 하위 클래스에도 딱 들어맞는다.

- Collection Framework 의 클래스들은 대부분 Abstract… 을 상속 받아서 그대로 사용한다.
- Set 구현체는 AbstractSet이 구현한 equals를 상속받아 쓰고, List 구현체들은 AbstractList로부터, Map 구현체들은 AbstractMap 으로부터 상속받아 그대로 사용한다.

### 4. 클래스가 private이거나 package-private이고 equals 메서드를 호출할 일이 없다.

## equals를 재정의 하는 경우

객체 식별성(object identity, 두 객체가 물리적으로 같은가)이 아닌 논리적 동치성을 확인해야 되는데 상위 클래스의 equals 가 논리적 동치성을 비교하도록 재정의되지 않았을 때 재정의 해주어야 한다.

- 주로 값 클래스들이 여기에 해당한다. (Integer, String, …)
- Map의 키와 Set의 원소로 사용할 수 있게된다.
- 값이 같은 인스턴스가 둘 이상 만들어지지 않음을 보장하는 인스턴스 통제 클래스라면 equals를 재정의하지 않아도 된다. (Enum 도 여기에 해당)
논리적으로 같은 인스턴스가 2개 이상 만들어지지 않으니 논리적 동치성과 객체 식별성이 사실상 똑같은 의미가 된다.

## Equals 를 재정의 할 때 지켜야 하는 일반 규약

### 1. **반사성(reflexivity)**

**null이 아닌 모든 참조 값 x에 대해, x.equals(x)는 true 다.**

→ 객체는 자기 자신과 같아야 한다는 뜻

만약 이 조건을 어긴 클래스를 컬렉션에 넣은다음 contains 메서드를 호출하면 방금 넣은 인스턴스가 없다고 나올 것이다.

### 2. **대칭성(symmetry)**

**null이 아닌 모든 참조 값 x, y에 대해, x.equals(y)가 true이면 y.equals(x)도 true다.**

→ 두 객체는 서로에 대한 동치 여부에 똑같이 답해야 한다는 뜻

**대칭성 위배 예시**

```java
public class CaseInsensitiveString {
    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

    // 대칭성 위배
    @Override
    public boolean equals(Object o) {
        if(o instanceof CaseInsensitiveString)
            return s.equalsIgnoreCase(((CaseInsensitiveString) o).s);
        
        if(o instanceof String)     // 한 방향으로만 작동
            return s.equalsIgnoreCase((String) o);
        
        return false;
    }
}
```

이 클래스에 toString 메서드는 원본 문자열의 대소문자를 돌려주지만 equals에서는 대소문자를 무시한다.

```java
CaseInsensitiveString cis = new CaseInsensitiveString("Polish");
String s = "polish";

cis.equals(s); // true
s.equals(cis); // false
```

위의 코드에서 cis.equals(s)는 true 를 반환한다. CaseInsensitiveString의 equals 는 일반 String 을 알고 있지만 String 의 equals 는 CaseInsensitiveString의 존재를 모른다.

따라서 s.equals(cis.toString())은 false를 반환하여 대칭성을 위반한다.

```java
List<CaseInsensitiveString> list = new ArrayList<>();
list.add(cis);

list.contains(s); // false
```

list 컬렉션을 생성해 cis 객체를 넣어주고 list.contains(s); 를 호출했다.

결과는 false를 반환하는데 이는 구현하기 나름이며 OpenJDK 버전(현재 나의 버전은 11)이 바뀌거나 다른 JDK 에서는 true를 반환하거나 런타임 예외를 던질 수도 있다.

**equals 규약을 어기면 그 객체를 사용하는 다른 객체들이 어떻게 반응할지 알 수 없다.**

문제를 해결하려면 String과 연동을 없애야 한다.

```java
@Override
    public boolean equals(Object o) {
        return o instanceof CaseInsensitiveString &&
                ((CaseInsensitiveString) o).s.equalsIgnoreCase(s); 
}
```

### 3. **추이성(transitivity)**

**null이 아닌 모든 참조 값 x, y, z에 대해, x.equals(y)가 true이고 y.equals(z)도 true면 x.equals(z)도 true다.**

→ 첫 번째 객체와 두 번째 객체가 같고, 두 번째 객체와 세 번째 객체가 같다면, 첫 번째 객체와 세 번째 객체도 같아야 한다는 뜻이다.

코드로 확인해 보자

```java
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Point))
            return false;

        Point p = (Point) o;
        return p.x == x && p.y == y;
    }
}
```

```java
public class ColorPoint extends Point{
    private final Color color;
    
    public ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }
}
```

equals 메서드를 그대로 둔다면 Point의 구현이 상속되어 색상 정보는 무시한 채 비교를 수행한다. equals 규약은 어기지 않았지만 색상이라는 중요한 정보를 놓치게 되므로 쓰기 힘들다.

**대칭성 위배**

색상 정보를 지키기 위해 ColorPoint에서도 equals를 오버라이딩을 하자.

```java
@Override
public boolean equals(Object o) {
    if(!(o instanceof ColorPoint))
        return false;

    return super.equals(o) && ((ColorPoint) o).color == color;
}
```

위 equals 메서드는 Point 를 ColorPoint에 비교한 결과와 그 둘을 바꿔 비교한 결과가 다를 수 있다.

```java
Point point = new Point(1, 2);
ColorPoint colorPoint = new ColorPoint(1, 2, Color.RED);

point.equals(colorPoint);  // true
colorPoint.equals(point);  // false
```

point 에서 colorPoint 를 비교를 하게 되면 colorPoint는 point를 상속했으므로 Point 타입이 될 수 있고 좌표 값도 같으니 true를 반환한다.

하지만 colorPoint에서 point 를 비교하게 되면 point는 ColorPoint의 타입이 될 수도 없으며 point는 color 속성도 가지고 있지않으므로 false 를 반환하게 된다.

**추이성 위배**

그러면 ColorPoint.equals가 Point와 비교할 때는 색상을 무시하도록 하면 해결

```java
@Override
public boolean equals(Object o) {
    if(!(o instanceof Point))
        return false;

		// Point 인 경우, 색상을 무시하고 x, y만 비교
    if(!(o instanceof ColorPoint))
        return o.equals(this);
    
		// ColorPoint 인 경우, 색상까지 비교
    return super.equals(o) && ((ColorPoint) o).color == color;
}
```

위의 equals 코드는 대칭성은 지켜주지만 추이성은 깨고있다.

```java
ColorPoint p1 = new ColorPoint(1, 2, Color.RED);
Point p2 = new Point(1, 2);
ColorPoint p3 = new ColorPoint(1, 2, Color.BLUE);

System.out.println(p1.equals(p2));  // true
System.out.println(p2.equals(p3));  // true
System.out.println(p1.equals(p3));  // false
```

p1.equals(p2)와 p2.equals(p3)는 true 를 반환하지만 p1.equals(p3)는 false 가 반환된다. p1 과 p2 비교에서는 색상까지 고려했기 때문에 false 가 나온다.

객체 지향적 추상화의 이점을 포기하지 않으면 **구체 클래스를 확장해 새로운 값을 추가하면서 equals 규약을 만족시킬 방법은 존재하지 않는다.**

구체 클래스의 하위 클래스에서 값을 추가할방법은 없지만 우회 방법이 있다. “상속 대신 컴포지션을 사용하라”를 따르면 된다. Point를 상속하는 대신 Point를 ColorPoint의 private 필드로 두고, ColorPoint와 같은 위치의 일반 Point를 반환하는 뷰 메서드를 public으로 추가하는 식이다.

```java
public class ColorPointView {
    private final Point point;
    private final Color color;

    public ColorPointView(int x, int y, Color color) {
        this.point = new Point(x, y);
        this.color = Objects.requireNonNull(color);
    }

    // ColorPoint 의 point 뷰를 반환
    public Point asPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ColorPoint))
            return false;

        ColorPointView cp = (ColorPointView) o;
        return cp.point.equals(point) && cp.color.equals(color);
    }
}
```

### 4. **일관성(consistency)**

null이 아닌 모든 참조 값 x, y에 대해, x.equals(y)를 반복해서 호출하면 항상 true를 반환하거나 false를 반환한다.

→ 두 객체가 같다면(어느 하나 혹은 두 객체 모두가 수정되지 않는 한) 앞으로도 영원히 같아야 한다는 뜻이다.

가변 객체는 비교 시점에 따라 서로 다를 수도 혹은 같을 수도 있는 반면, 불변 객체는 한번 다르면 끝까지 달라야 한다.

> 클래스를 작성할 때는 불변 클래스로 만드는 게 나을지 심사숙고 하자.
> 

클래스가 불변이든 가변이든 **equals의 판단에 신뢰할 수 없는 자원이 끼어들게 해서는 안된다.** 이 제약을 어기면 일관성 조건을 만족시키기가 아주 어렵다.

equals는 항시 메모리에 존재하는 객체만을 사용한 결정적(deterministic) 계산만 수행해야 한다.

### 5. null-아님

null이 아닌 모든 참조 값 x에 대해, x.equals(null)은 false다.

→ 모든 객체가 null과 같지 않아야 한다는 뜻이다.

1. **== 연산자를 사용해 입력이 자기 자신의 참조인지 확인한다.**
자기 자신이면 true를 반환, 단순한 성능 최적화용이다.
2. **instanceof 연산자로 입력이 올바른 타입인지 확인한다.**
올바른 타입이 아니라면 false를 반환한다. 이 때의 올바른 타입은 equals 가 정의된 클래스인 것이 보통이지만, 가끔은 그 클래스가 구현한 특정 인터페이스가 될 수도 있다.
3. **입력을 올바른 타입으로 형변환한다.**
2번에서 instanceof 검사를 했기 때문에 이 단계는 100% 성공한다.
4. 입력 객체와 자기 자신의 대응되는 ‘핵심’ 필드들이 모두 일치하는지 하나씩 검사한다.
모든 필드가 일치하면 true를, 하나라도 다르면 false를 반환한다.
2단계에서 인터페이스를 사용했다면 입력의 필드 값을 가져올 때도 그 인터페이스의 메서드를 사용해야 한다.

> float, double을 제외한 기본 타입 필드는 == 연산자로 비교하고, 참조 타입 필드는 각각의 equals 메서드로, float, double 필드는 각각 정적 메서드인 Float.compare(float, float)과 Double.compare(double, double)로 비교한다. 이유는 float과 double 은 부동소수 값등을 다뤄야 하기 때문이다.
배열의 모든 원소가 핵심 필드라면 Arrays.equals 메서드들 중 하나를 사용하자.
> 

어떤 필드를 먼저 비교하느냐가 equals의 성능을 좌우하기도 한다.

- 다를 가능성이 더 크거나 비교하는 비용이 싼(혹은 둘 다 해당하는) 필드를 먼저 비교하자.
- 동기화용 락(lock) 필드 같이 객체의 논리적 상태와 관련 없는 필드는 비교하면 안 된다.
- 핵심 필드로부터 계산해낼 수 있는 파생 필드 역시 굳이 비교할 필요는 없지만, 파생 필드를 비교하는 쪽이 더 빠를 때도 있다. (캐시 필드가 있는 경우)

### 전형적인 equals 메서드 예

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

- equals 를 재정의 할 땐 hashCode도 반드시 재정의하자.
- 너무 복잡하게 해결하려 들지 말자.
필드들의 동치성만 검사해도 equals 규약을 어렵지 않게 지킬 수 있다.
- Object 외의 타입을 매개변수로 받는 equals 메서드는 선언하지 말자.

### 정리

> 꼭 필요한 경우가 아니라면 equals를 재정의 하지 말자. 많은 경우에 Object의 equals가 여러분이 원하는 비교를 정확히 수행해준다. 재정의해야 할 때는 그 클래스의 핵심 필드 모두를 빠짐없이, 다섯 가지 규약을 확실히 지켜가며 비교해야 한다.
>