# Item 12 - toString을 항상 재정의하라

Object의 toString() 메서드는 재정의 하지 않으면 단순히 (클래스이름)@(16진수로 표현한 해시코드)를 반환한다.

```java
public class Student {
    private final String id;
    private final String name;
    private final int score;

    public Student(String id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }
}

----------------------------------------------------------------------------

public static void main(String[] args) {
    Student student = new Student("id", "bell", 100);

    System.out.println(student);
}
```

![Untitled](image/Untitled.png)

toString 의 일반 규약

- **‘간결하면서 사람이 읽기 쉬운 형태의 유익한 정보’**를 반환해야 한다.
- 모든 하위 클래스에서 이 메서드를 재정의하라

**toString을 잘 구현한 클래스는 사용하면 시스템은 디버깅하기 쉽다.**

toString 메서드는 객체를 println, printf, 문자열 연결 연산자(+), assert 구문에 넘길 때, 또는 디버거가 객체를 출력할 때 자동으로 불린다.

- 직접 호출하지 않아도 다른 어딘가에서 쓰일 수 있다.
- toString은 이 인스턴스를 포함하는 객체에서 유용하게 사용된다.
    - ex) map 객체를 출력할 때 유용
    
    ```java
    public static void main(String[] args) {
        Map<String, Student> studentMap = new HashMap<>();
        Student student = new Student("id", "bell", 100);
        studentMap.put("bell", student);
    
        System.out.println(studentMap);
    }
    ```
    
    - **toString을 사용하지 않은 경우**
    
    ![Untitled](image/Untitled%201.png)
    
    - **toString을 사용한 경우**
    
    ![Untitled](image/Untitled%202.png)
    

**toString은 그 객체가 가진 주요 정보 모두를 반환하는 게 좋다. (객체가 거대하거나 상태가 문자열로 표현하기 어려운 경우 요약정보를 담자)**

**toString 을 구현할 때 반환값의 포맷을 문서화할지 정해야 한다.**

- 전화번호나 행렬 같은 값 클래스라면 문서화하기를 권한다.
- 포맷을 명시하기로 한다면 명시한 포맷에 맞는 문자열을 제공해주면 좋다.
- 객체를 상호 전환할 수 정적 팩터리나 생성자를 함께 제공해 주면 좋다.

BigInteger의 toString

![Untitled](image/Untitled%203.png)

포맷을 한번 명시하면 평생 이 포맷에 얽메이는 단점이 있다. 다음 릴리스에서 포맷을 바꾼다면 포맷을 보고 사용하던 데이터들은 엉망이 될 수 있다.

**포맷을 명시하든 아니든 의도는 명확히 밝혀야 한다.**

- 포맷을 명시하려면 아주 정확하게 해야한다.

![Untitled](image/Untitled%204.png)

- 포맷을 명시하지 않기로 했다면 아래와 같이 작성할 수 있다.

![Untitled](image/Untitled%205.png)

**toString이 반환한 값에 포함된 정보를 얻어올 수 있는 API를 제공하자.**

정적 유틸리티 클래스는 toString을 제공할 필요가 없다.

하위 클래스들이 공유해야 할 문자열 표현이 있는 추상 클래스라면 toString을 재정의 해줘야 한다.

> 정리
모든 구체 클래스에서 Object의 toString을 재정의하자. 상위 클래스에서 이미 알맞게 재정의한 경우는 예외다. toString을 재정의한 클래스는 사용하기 좋고 그 클래스를 사용한 시스템을 디버깅하기 쉽게 해준다. toString은 해당 객체에 관한 명확하고 유용한 정보를 읽기 좋은 형태로 반환해야 한다.
>