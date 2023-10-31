# item 9 - try-finally 보다는 try-with-resources를 사용하라

## try-finally

InputStream, OutputStream, java.sql.Connection 등을 사용하여 자원을 사용하면 close 메소드를 사용하여 자원을 일일이 다 닫아주어야 한다.

이런 자원을 사용하는 객체 중 상당수가 이전 item 8 에서 보았던 finalizer를 활용해 자원을 닫아주고 있지만 finalizer는 실행됨을 보장하지 않아 믿을만 하지 않는다.

자원이 제대로 닫힘을 보장하기 위해 try-finally 가 자주 쓰였다. 예외가 발생하는 경우 catch를 추가해 예외가 발생하더라도 자원이 닫힘을 보장해준다.

```java
static String firstLineOfFile(String path) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(path));
    try {
        return br.readLine();
    } finally {
        br.close();
    }
}
```

위의 코드에는 치명적인 단점이 존재한다.

1. 먼저 가독성이 좋지않다.
2. 스택 추적이 어려워진다.

기기에 물리적인 문제가 발생하게 되면 메소드 안의 readLine에서 예외가 던져지고 같은이유로 close 메소드에서도 예외가 발생하게 된다. 예외가 2개가 발생하게 되고 이때 close 에서 발생한 예외가 readLine에서 발생한 예외를 덮어버려 스택을 추적 내역에서 첫번째에 발생한 예외가 기록되지 않게 된다.

일반적으로 문제를 해결할 때 처음 발생한 예외를 먼저 해결하는 것이 순서인데 실제 시스템에서 위의 상황이 발생하면 디버깅을 몹시 어렵게 만든다.

자원이 하나 더 늘어나게 되면 코드가 지저분해지고 보기 힘들어진다.

```java
static void copy(String src, String dst) throws IOException {
    InputStream in = new FileInputStream(src);
    try {
        OutputStream out = new FileOutputStream(dst);
        try {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while((n = in.read(buf)) >= 0)
                out.write(buf, 0, n);
        } finally {
            out.close();
        }
    } finally {
        in.close();
    }
}
```

## try-with-resources

위의 try-finally 에서 나타난 문제들이 try-with-resource를 통해 모두 해결되었다. 이 구조를 사용하려면 해당 자원이 AutoCloseable 인터페이스를 구현해야 한다(item 8 에 간단한 구현이 있다.). 만약 닫아야 하는 자원을 뜻하는 클래스를 작성한다면 AutoCloseable을 반드시 구현하자.

```java
static String firstLineOfFile(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        return br.readLine();
    }
}
```

try-with-resource 를 적용해 수정한 코드이다.

코드가 매우 짧아졌고 읽기 편해졌다. 또한 문제를 진단하기도 훨씬 좋다. 위의 try-fianlly 에서의 상황 예에서 처럼 readLine과 코드에는 나타나지 않은 close 호출 양쪽에서 예외가 발생하면, close 에서 발생한 예외는 숨겨지고 readLine 에서 발생한 예외가 기록된다. 이때 숨겨진 예외들은 Suppressed(‘숨겨졌다’)는 꼬리표를 달고 출력된다.

Java 7 에서 Throwable 에 추가된 getSuppressed 메소드를 이용하면 프로그램 코드에서 가져올 수도 있다.

보통의 try-finally에서 catch 를 사용할 수 있던 것처럼 try-with-resources에서도 catch 절을 사용할 수 있다.

> 꼭 회수해야 하는 자원을 다룰 때는 try-finally 말고, try-with-resource를 사용하자. 예외는 없다. 코드는 더 짧고 분명해지고, 만들어지는 예외 정보도 훨씬 유용하다. try-finally로 작성하면 실용적이지 못할 만큼 코드가 지저분해지는 경우라도, try-with-resource로는 정확하고 쉽게 자원을 회수할 수 있다.
>