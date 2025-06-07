# MySQL의 격리수준

격리수준은 크게 4가지로 나뉜다.

- READ UNCOMMITTED
- READ COMMITTED
- REPEATABLE READ
- SERIALIZABLE

> READ UNCOMMITTED 는 DIRTY READ 라고도 하며 일반적인 데이터 베이스에서 사용하지 않는다. <br />
> SERIALIZABLE 또한 동시성이 중요한 데이터베이스에서는 거의 사용되지 않는다.

뒤로 갈수록 각 트랜잭션간 데이터 격리 정도가 높아지고 동시 처리 성능도 떨어지는 것이 일반적이다.

- SERIALIZABLE 격리 수준이 아니라면 크게 성능의 개선이나 저하는 발생하지 않는다.

|                  | DIRTY READ | NON-REPEATABLE READ | PHANTOM READ         |
| ---------------- | ---------- | ------------------- | -------------------- |
| READ UNCOMMITTED | 발생       | 발생                | 발생                 |
| READ COMMITTED   | 없음       | 발생                | 발생                 |
| REPEATABLE READ  | 없음       | 없음                | 발생 (InnoDB는 없음) |
| SERIALIZABLE     | 없음       | 없음                | 없음                 |

> SQL-92, SQL-99 표준에는 REPEATABLE READ 격리 수준에서는 PANTHOM READ 가 발생할 수 있지만, <br />
> InnoDB 의 독특한 특성으로 REPEATABLE READ 격리 수준에서도 PANTHOM READ 가 발생하지 않는다.

일반적인 온라인 서비스 용도의 데이터 베이스에서는 READ COMMITTED 와 REPEATABLE READ 중 하나를 사용한다.

## READ UNCOMMITTED

각 트랜잭션에서의 변경 내용이 COMMIT 이나 ROLLBACK 여부에 상관없이 다른 트랜잭션처럼 보인다.

- 어떤 트랜잭션이 처리한 작업이 완료되지 않았는데도 보이는 Dirty read 현상이 나타난다.
- 데이터가 나타났다가 사라졌다 하는 현상을 초래하여 사용자를 혼란스럽게 만들 수 있다.
- 더티 리드를 유발하는 READ UNCOMMITTED 는 RDBMS 표준에서는 트랜잭션의 격리 수준으로 인정하지 않을 정도로 정합성에 문제가 많은 격리 수준이다.

## READ COMMITTED

오라클 RDBMS 에서 기본적으로 사용되는 격리 수준으로 온라인 서비스에서 가장 많이 선택되는 격리 수준이다. <br />
Dirty read 가 발생하지 않으며 어떤 트랜잭션에서 데이터를 변경했더라도 COMMIT 이 완료된 데이터만 다른 트랜잭션에서 조회할 수 있다.

- 언두 영역에서 백업된 레코드를 가져와 변경 내용이 커밋되기 전까지 다른 트랜잭션에서 그러한 변경 내역을 조회할 수 없다.
- REPEATABLE READ 가 불가능하다.
  - 현재 트랜잭션에서 SELECT 를 하여 데이터를 얻었지만 다른 트랜잭션에서 현재 트랜잭션이 끝나기 전에 변경을 하고 커밋을 했을 떄 현재 트랜잭션에서 다시 SELECT 를 하여 데이터를 얻게 되면 이전과는 다른 변경된 데이터를 얻게될 수 있다.
  - REPEATLABLE READ 정합성에 어긋나며 하나의 트랜잭션에서 동일 데이터를 여러번 읽고 변경하는 작업이 금전적인 중요한 처리와 연결되면 문제가 될 수 있다.
- READ COMMITTED 격리 수준에서는 트랜잭션 내에서 실행되는 SELECT 문장과 트랜잭션 외부에서 실행되는 SELECT 문장의 차이는 별로 없다.

## REPEATABLE READ

MySQL의 InnoDB 스토리지 엔진에서 기본으로 사용되는 격리 수준 <br />
NON-REPEATABLE READ 부정합이 발생하지 않는다.

**MVCC (Multi Version Concurrency Control)**

InnoDB 스토리지 엔진은 트랜잭션이 ROLLBACK 될 가능성에 대비해 변경되기 전에 언두(Undo) 공간에 백업해 두고 실제 레코드 값을 변경한다.

- REPEATABLE READ 는 MVCC 를 위해 언두 영역에 백업된 이전 데이터를 이용해 동일 트랜잭션 내에서는 동일한 결과를 보여줄 수 있게 보장한다.
- READ COMMITTED 도 MVCC 를 이용해 COMMIT 되기 이전의 내용을 보여주지만 둘의 차이는 언두 영역에 백업된 레코드의 여러 버전 가운데 몇 번쨰 버전까지 찾아 들어가야 하느냐에 있다.

모든 InnoDB 의 트랜잭션은 고유한 번호를 가지며 언두 영역에 백업된 모든 레코드에는 변경을 발생시킨 트랜잭션 번호가 있다.
REPEATABLE READ 격리 수준에서는 MVCC를 보장하기 위해 실행 중인 트랜잭션 가운데 가장 오래된 트랜잭션 번호보다 트랜잭션 본호가 앞선 언두 영역의 데이터는 삭제할 수가 없다.

- 특정 트랜잭션 번호의 구간 내에서 백업된 언두 데이터가 보존되어야 한다.
- 하나의 레코드에 대해 백업이 하나 이상 얼마든지 존재할 수 있어 <br />
  한 사용자가 트랜재겻능ㄹ 시작하고 종료하지 않으면 언두 영역이 백업된 데이터로 무한정 커질 수도 있다.

PANTHOM READ 로 인한 부정합이 발생할 수 있다.

**PANTHOM READ**
다른 트랜잭션에서 수행한 변경 작업에 의해 레코드가 보였다 안보였다 하는현상

SELECT ... FOR UPDATE 의 쿼리에 대해서는 결과가 다를 수 있다.

- 쓰기 잠금을 걸어야 하는데 언두 레코드에는 잠금을 걸 수 없다.
- SELECT ... FOR UPDATE 나 SELECT ... LOCK IN SHARE MODE 로 조회되는 레코드는 언두 영역의 변경 전 데이터를 가져오지 않고 현재 레코드의 값을 가져오게 된다.

> InnoDB 스토리지 엔진에서는 갭 락과 넥스트 키 락 덕분에 일반적인 DBMS 에서 일어나는 PHANTOM READ 라는 문제가 발생하지 않는다.

## SERIALIZABLE

가장 단순한 격리 수준이고 가장 엄격한 격리 수준이며 동시 처리 성능도 다른 트랜잭션 격리 수준보다 떨어진다.

- 읽기 작업도 공유 잠금을 획득해야만 하고 동시에 다른 트랜잭션은 레코드를 변경하지 못하게 된다.
