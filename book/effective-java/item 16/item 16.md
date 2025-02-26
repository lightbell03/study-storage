# Item 16 - public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라

퇴보한 클래스

```java
class Point {
		public double x;
		public double y;
}
```

- 데이터 필드에 직접 접근할 수 있지만 캡슐화의 이점을 제공하지 못한다.
- API를 수정하지 않고는 내부 표현을 바꿀 수 없다.
- 불변식을 보장할 수 없다.
- 외부에서 필드에 접근할 때 부수 작업을 수행 할 수 없다.

필드들을 모두 private 으로 바꾸고 public 접근자(getter)를 추가한다.

```java
class Point {
		private double x;
		private double y;

		public Point(double x, double y) {
				this.x = x;
				this.y = y;
		}

		public double getX() { return x; }
		public double getY() { return y; }

		public void setX(double x) { this.x = x; }
		public void setY(double.y) { this.y = y; }
}
```

public 클래스라면 위의 방법이 맞다.

**패키지 바깥에서 접근할 수 있는 클래스라면 접근자를 제공**하여 클래스 내부 표현 방식을 언제든 바꿀 수 있는 유연성을 얻을 수 있다.

**package-private 혹은 private 중첩 클래스라면 데이터 필드를 노출한다 해도 문제가 없다.**

- 클래스 선언 면에서나 클라이언트 코드 면에서나 접근자 방식보다 훨씬 깔끔하다.

---

public 클래스의 필드가 불변이라면 직접 노출할 때의 단점이 조금은 줄어들지만 좋지 않다.

```java
public final class Time {
		private static final int HOURS_PER_DAY = 24;
		private static final int MINUTES_PER_HOUR = 60;

		public final int hour;
		public final int minute;

		public Time(int hour, int minute) {
		....
		}
}
```

- API를 변경하지 않고는 표현 방식을 바꿀 수 없다.
- 필드를 읽을 때 부수 작업을 수행할 수 없다.
- 불변식은 보장 할 수 있다.

---

> public 클래스는 절대 가변 필드를 직접 노출해서는 안 된다. 불변 필드라면 노출해도 덜 위험하지만 완전히 안심할 수는 없다. 하지만 package-private 클래스나 private 중첩 클래스에서는 종종(불변이든 가변이든) 필드를 노출하는 편이 나을 때도 있다.
