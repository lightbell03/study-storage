# Item 2

# 생성자에 매개변수가 많다면 빌더를 고려하라

앞서 [item 1](https://www.notion.so/Item-1-2efba22aa1304330be4ad6fee2ba4323) 에서 사용한 정적 팩토리 메서드와 생성자에는 똑같은 제약이 하나 있다. 선택적 매개변수가 많을 때 적절히 대응하기 어렵다는 점이다.

예를 들어 영양 정보를 가지는 클래스가 있다고 하자. 이 클래스는 1회 내용량, 총 n회 제공량 같은 필수 항목 몇개와 칼로리, 지방, 나트륨, 탄수화물 등 많은 수의 선택항목으로 이루어진다. 이때 선택항목의 경우 대다수 값이 0인데 이때 선택 매개변수를 전부다 받는 생성자까지 늘려가는 방식을 적용하면 아래와 같은 코드로 나올 것이다.

### 점층적 생성자 패턴(telescoping constructor pattern)

```java
public class NutritionFacts {
		//필수
    private final int servingSize;
    private final int servings;
		//선택
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public NutritionFacts(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories) {
        this(servingSize, servings, calories, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat) {
        this(servingSize, servings, calories, fat, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium){
        this(servingSize, servings, calories, fat, sodium, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }
}
```

이제 이 클래스의 인스턴스를 만들려면 원하는 매개변수를 모두 포함한 생성자 중 필요한 생성자를 선택하면 된다. 하지만 이런 생성자를 사용하게 되면 사용자가 설정하기 원하지 않는 매개변수까지 포함하기 때문에 그런 매개변수에도 값을 지정해 줘야 한다. 지금은 사용자가 선택할 변수가 4개 밖에 없지만 선택할 변수가 더 늘어나게 되면 그만큼 점층적 생성자가 늘어나야 할 것이다.

즉, **점층적 생성자 패턴도 쓸 수는 있지만, 매개변수 개수가 많아지면 클라이언트 코드를 작성하거나 읽기 어려워진다.**

각 값의 의미가 무엇인지 헷갈릴 것이고, 매개변수가 몇 개인지도 주의해서 세어 보아야 한다. 또한 타입이 같은 매개변수가 연달아 있으면 찾기 어려운 버그로 이어질 수 있다. 순서를 바꾸어 전달하면 컴파일러는 알아채지 못하고, 결국 런타임에 의도치 않은 동작을 하게 될것이다.

---

### 자바빈즈 패턴(JavaBeans pattern)

```java
public class NutritionFacts {
    private int servingSize = -1;
    private int servings = -1;
    private int calories;     
		private int fat;
    private int sodium;
    private int carbohydrate;

    public NutritionFacts() {}

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }
}
```

점층적 생성자 패턴보다 인스턴스를 만들기 쉽고, 더 읽기 쉬운 코드가 되었다. 하지만 자바빈즈 패턴은 심각한 단점을 가지고 있다.

- 객체 하나를 만들려면 메서드를 여러 개 호출해야 한다.
- 객체가 완전히 생성되기 전까지는 일관성이 무너진 상태이다.
- 일관성이 무너져 클래스를 불변으로 만들 수 없다.
- 불변(immutable, immutability)
    
    어떠한 변경도 허용하지 않는다는 뜻, 주로 변경을 허용하는 가변(mutable)객체와 구분하는 용도로 쓰인다.
    
    대표적으로 String 객체는 한번 만들어지면 절대 값을 바꿀 수 없는 불변 객체이다.
    

---

### 빌더 패턴(Builder pattern)

점층적 생성자 패턴의 안전성과 자바빈즈 패턴의 가독성을 모두 가지고 있다.

1. 클라이언트는 필요한 객체를 직접 만드는 대신, 필수 매개변수만으로 생성자(혹은 정적 팩토리)를 호출해 빌더 객체를 얻는다.
2. 빌더 객체가 제공하는 일종의 세터(setter) 메서드들로 원하는 선택 매개변수들을 설정한다.
3. 매개변수가 없는 build 메서드를 호출해 우리에게 필요한(보통은 불변인) 객체를 얻는다.

빌더는 생성할 클래스 안에 정적 멤버 클래스로 만들어두는 게 보통이다.

```java
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    private NutritionFacts(Builder builder) {
        this.servingSize = builder.servingSize;
        this.servings = builder.servings;
        this.calories = builder.calories;
        this.fat = builder.fat;
        this.sodium = builder.sodium;
        this.carbohydrate = builder.carbohydrate;
    }

    public static class Builder {
        private final int servingSize;
        private final int servings;

        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        public Builder calories(int val) {
            this.calories = val;
            return this;
        }
        public Builder fat(int val) {
            this.fat = val;
            return this;
        }

        public Builder sodium(int val) {
            this.sodium = val;
            return this;
        }

        public Builder carbohydrate(int val) {
            this.carbohydrate = val;
            return this;
        }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }
}
```

빌더의 세터들, 즉 위의 빌더 클래스 안에서 build 를 제외한 메서드들은 빌더 자신을 반환하기 때문에 연쇄적으로 호출 할 수 있다. 메서드 호출이 흐르듯 연결된다는 뜻으로 플루언트 API(fluent API) 혹은 메서드 연쇄(method chaining)라 한다.

### 빌더 패턴을 이용한 클라이언트 코드

```java
NutritionFacts cola = new Builder(240, 8)
        .calories(100).sodium(35).carbohydrate(27).build();
```

코드에서 보이는 것과 같이 쓰기 쉽고, 읽기 쉽다.

빌더 패턴은 **명명된 선택적 매개변수(named optional parameters)를 흉내 낸 것**이다.

- 유효성 검사
    
    잘못된 매개변수를 빠르게 발견하려면 빌더의 생성자와 메서드에 입력 매개변수를 검사하고, build 메서드가 호출하는 생성자에 여려 매개변수에 걸친 불변식을 검사하면 된다.
    
    이런 불변식을 보장하려면 빌더로부터 매개변수를 복사한 후 해당 객체 필드들도 검사해야 한다.
    
    - 불변식(invariant)
        
        프로그램이 실행되는 동안, 혹은 정해진 기간 동안 반드시 만족해야 하는 조건을 말한다.
        
        변경을 허용할 수는 있으나 주어진 조건 내에서만 허용한다는 뜻
        

## 계층적으로 설계된 빌터 패턴

### 부모 추상 클래스

```java
public abstract class Pizza {
    public enum Topping {HAM,MUSHROOM,ONION,PEPPER,SAUSAGE}
    final Set<Topping> toppings;

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);
        public T addTopping(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }
        abstract Pizza build();
				//simulated self-type
        protected abstract T self();
    }

    Pizza(Builder<?> builder) {
        toppings = builder.toppings.clone();
    }
}
```

Pizza.Builder 는 재귀적 타입 한정을 이용하는 제네릭 타입이다. 여기에 추상 메서드인 self 를 더해 하위 클래스에서는 형변환 하지 않고 메서드 연쇄를 지원할 수 있다. 이 우회 방법을 시뮬레이트한 셀프 타입(simulated self-type) 관용구라 한다.

하위 클래스에서 self 를 오버라이딩하여 Pizza.Builder를 상속받은 하위클래스 Builder를 반환하게 만들어 메서드 연쇄를 가능하게 할 수 있다.

### 구체 클래스

```java
public class NyPizza extends Pizza{
    public enum Size {SMALL,MEDIUM,LARGE};
    private final Size size;

    public static class Builder extends Pizza.Builder<Builder> {
        private final Size size;

        public Builder(Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override
        public NyPizza build() {
            return new NyPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    NyPizza(Builder builder) {
        super(builder);
        size = builder.size;
    }
}
```

```java
public class Calzone extends Pizza{
    private final boolean sauceInside;

    public static class Builder extends Pizza.Builder<Builder> {
        private boolean sauceInside = false;

        public Builder sauceInside() {
            sauceInside = true;
            return this;
        }

        @Override
        public Calzone build() {
            return new Calzone(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    Calzone(Builder builder) {
        super(builder);
        this.sauceInside = builder.sauceInside;
    }
}
```

NyPizza.Builder 는 NyPizza 클래스를 반환, Calzone.Builder 는 Calzone 을 반환한다. **하위 클래스의 메서드가 상위 클래스의 메서드가 정의한 반환 타입이 아닌, 그 하위 타입을 반환하는 기능을 공변 반환 타이핑**이라 한다(convariant return typing). 

- 상위 추상 클래스에서 Pizza 를 반환하게 되었는데 NyPizza 와 Calzone 은 Pizza 를 상속받아 하위 타입으로 반환을 해주고 있다.

### 계층적 빌더 패턴을 이용한 클라이언트 코드

```java
NyPizza pizza = new NyPizza.Builder(SMALL)
                .addTopping(SAUSAGE).addTopping(ONION).build();
Calzone calzone = new Calzone.Builder()
                .addTopping(HAM).sauceInside().build();
```

---

### 장점

- 빌더 패턴을 이용해 가변인수 매개변수를 여러개 사용할 수 있다. (각각을 적절한 메서드로 나워 선언)
- 상당히 유연함
- 빌더 하나로 여러 객체를 순회하면서 만들 수 있다.
- 빌더에 넘기는 매개변수에 따라 다른 객체를 만들 수 있다.
- 객체마다 부여되는 일련번호와 같은 특정 필드는 빌더가 알아서 채우도록 할 수 있다.

### 단점

- 객체를 만들려면 빌더부터 만들어야 한다.
- 빌더는 생성 비용이 크지 않지만 성능에 민감한 상황에서는 문제가 될 수 있다.
- 점층적 생성자 패턴보다는 코드가 장황해서 매개변수가 4개 이상은 되어야 값어치가 있다. (API 는 시간이 지날수록 매개변수가 많아지는 경향이 있다. → 애초에 빌더 패턴으로 시작이 나을 수 있다.)

---

### 정리

> **생성자나 정적 팩토리가 처리해야 할 매개변수가 많다면 빌더 패턴을 선택하는 게 더 낫다.**
빌더는 점층적 생성자보다 클라이언트 코드를 읽고 쓰기가 훨씬 간겨하고, 자바빈즈보다 훨씬 안전하다.
>