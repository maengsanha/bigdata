## Final Project: Graph Mining on MapReduce and Spark

- 프로젝트 목표: 대용량 그래프의 군집계수 (Clustering Coefficient) 분석을 위한 효율적인 분산 알고리즘을 설계, 구현 및 실험한다.



**1. 각 단계 별 구현 방법**



**Task 1. 그래프의 중복 edge 및 self-loop 제거 (Hadoop MapReduce)**



MapReduce를 이용해서 중복된 데이터와 self-loop를 제거해야하기 때문에 map phase에서 self-loop를 제거하고 reduce phase에서 중복 데이터를 제거하도록 하였다. (shuffle phase에서 group by key를 해주기 때문)



```javascript
let map(key, value):
	// map removes self-loop and emits in order of value.
  if key != value:
    if key < value: emit(key, value)
    else: emit(value, key)

let reduce(key, values):
	// reduce removes duplicates.
  for each value in set(values):
  	emit(key, value)
```




<hr>

**Task 2. 각 node u의 degree d(u) 구하기 (Hadoop MapReduce)**



degree를 구하는 것은 word count와 똑같기 때문에 map phase에서는 (key, value)를 (key, 1), (value, 1)로 내보내고 reduce phase에서 이들을 합산하였다.

더하기 연산은 commutative하고 associative하기 때문에 combiner에 reducer를 넣어 데이터 발생량을 줄였다.



```javascript
let map(key, value):
	// map emits (u, 1), (v, 1) with given edge (u, v).
	emit(key, 1)
	emit(value, 1)

let reduce(key, values):
	// reduce emits the degree of node u.
	emit(key, sum(values))
```



<hr>

**Task 3. 각 node u마다 u를 포함하는 삼각형의 수 t(u) 구하기 (Spark)**



wedge와 edge를 join하면 삼각형을 찾을 수 있다는 것을 배워 알고있지만 좀 더 효율적인 알고리즘을 작성하기 위해 degree RDD를 만든 후, edge와 2번 join하여 d(u) < d(v) or d(u) == d(v) and u < v가 되도록 edge를 정렬했다.

정렬된 edge data를 self-join하고 filter와 map을 거쳐 정렬된 wedge RDD를 생성했다.

그 후 정렬된 wedge와 edge를 join하고 map, reduce하여 t(u)를 구했다.



```javascript
degrees = edges.flatMap{ (key, value) => [ (key, 1), (value, 1) ] }
							 .reduceByKey(_ + _)

sortedEdges = edges.join(degrees)
  								 .join(degrees)
  								 .map( ( u, v, d(u), d(v) ) =>
												if d(u) < d(v) or d(u) == d(v) and u < v: (u, v)
                        else: (v, u) )

sortedWedges = sortedEdges.join(sortedEdges)
  												.filter( ( u, d(u), v, d(v) ) => d(u) < d(v) or d(u) == d(v) and u < v )
													.map( ( ( u, d(u) ), ( ( v, d(v) ), ( w, d(w) ) ) )  => ( (u, v), w ) )

sortedWedges.join(sortedEdges)
  					.flatMap( ( (u, v), (w, $) ) => [(u, 1), (v, 1), (w, 1)] )
						.reduceByKey(_ + _)
						.emit()
```



<hr>

**Task 4. 각 node u마다 군집계수 cc(u) 구하기 (Spark)**



Task 3의 결과물에 Task 2의 결과물을 join하면 ( u , ( t(u), d(u) ) ) 꼴이 되는데, 이에 map으로 ( u, cc(u) )를 구한다.

이 때 degree = 0인 경우는 Task 1에서 제거되었으며 d(u) = 1인 경우엔 cc(u) = 0이 되도록 하였다.



```javascript
Task3Output.join(Task2Output)
           .map( (u, ( t(u), d(u) ) ) => if d(u) != 1: ( u, t(u) / d(u) ) else: 0 )
           .emit()
```



<hr>



**2. Google Cloud Platform에서의 실행 결과**



Task 1~4의 소스코드를 build한 후, LiveJournal dataset과 함께 GCP로 전송한다.

```bash
$ scp -p port_number user@external_ip task1.jar
$ scp -p port_number user@external_ip task2.jar
$ scp -p port_number user@external_ip task3.jar
$ scp -p port_number user@external_ip task4.jar
$ scp -p port_number user@external_ip soc-LiveJournal1.txt
```

- GCP로 전송 후 LiveJournal dataset을 하둡 파일 시스템에 저장한 모습

<img src="https://user-images.githubusercontent.com/29545214/85819072-63371180-b7ad-11ea-88c6-2ba6fc32cdb0.png">

<hr>

분산 환경에서 Task1을 수행한다. reduce task는 5개로 설정했고 input file path를 args[0]으로, output file path를 args[1]로 입력한다.

```bash
$ hadoop jar task1.jar bigdata.Task1 -Dmapreduce.job.reduces=5 soc-LiveJournal1.txt task1.out
```

<img src="https://user-images.githubusercontent.com/29545214/85823761-2244fa00-b7b9-11ea-939d-72b75f7a6dc7.png">

- Task 1이 성공적으로 수행된 모습

<img src="https://user-images.githubusercontent.com/29545214/85823983-abf4c780-b7b9-11ea-9b27-6609ec8b6777.png">

reduce task를 5개로 설정했기 때문에 output file도 5개 생성된다.

<img src="https://user-images.githubusercontent.com/29545214/85824476-db580400-b7ba-11ea-8dbd-bb44af322502.png">

5개로 나뉘어있는 결과를 하나의 파일로 병합한다. 결과 확인 후 다시 분산 파일 시스템에 저장한다.

```bash
$ hdfs dfs -getmerge task1.out/* task1_out
$ hdfs dfs -put task1_out
```

<img src="https://user-images.githubusercontent.com/29545214/85824649-43a6e580-b7bb-11ea-865b-644d5b4ebd44.png">

Task 1의 web interface history

<img src="https://user-images.githubusercontent.com/29545214/85824996-098a1380-b7bc-11ea-9108-1ee65cfb592f.png">

<hr>

Task 1의 결과를 이용하여 Task 2를 수행한다.

reduce task는 5개로 설정하고 args[0]에 병합한 Task 1의 결과를, args[1]에 output file path를 입력한다.

```bash
$ hadoop jar task2.jar bigdata.Task2 -Dmapreduce.job.reduces=5 task1_out task2.out
```

<img src="https://user-images.githubusercontent.com/29545214/85825279-c1b7bc00-b7bc-11ea-8bd2-fa85cd3164f2.png">

Task 2가 완료된 모습

<img src="https://user-images.githubusercontent.com/29545214/85825610-9f726e00-b7bd-11ea-8c06-9c2dfdc13c16.png">

Task 1과 마찬가지로 출력 파일이 5개로 나뉘기 때문에 병합한다.

결과 확인 후 분산 파일 시스템에 저장한다.

```bash
$ hdfs dfs -getmerge task2.out/* task2_out
$ hdfs dfs -put task2_out
```

<img src="https://user-images.githubusercontent.com/29545214/85825702-e06a8280-b7bd-11ea-9f48-037a0978f1cd.png">

Task 2의 web interface history

<img src="https://user-images.githubusercontent.com/29545214/85825770-0ee85d80-b7be-11ea-801f-69bb8cfcf4c7.png">

<hr>

Task 1의 결과를 이용하여 Task 3을 수행한다.

num-executors를 120으로 설정하고 args(0)에 Task 1의 결과를, args(1)에 output file path를 입력한다.

```bash
$ spark-submit --num-executors 12 --class bigdata.Task3 task3.jar task1_out task3.out
```

<img src="https://user-images.githubusercontent.com/29545214/85825832-32aba380-b7be-11ea-8915-dfa0480286c8.png">

num-executors를 120으로 설정했기 때문에 output file이 120개 생성된다.

결과를 확인한 후 병합하고 분산 파일 시스템에 저장한다.

```bash
$ hdfs dfs -cat task3.out/* | head
$ hdfs dfs -getmerge task3.out/* task3_out
$ hdfs dfs -put task3_out
```



output file이 num-executors만큼 생성된 모습

<img src="https://user-images.githubusercontent.com/29545214/85826002-a2ba2980-b7be-11ea-8f31-b347d6a5cdfe.png">

결과를 확인해보면 u, t(u)들이 정상적으로 저장돼있음을 알 수 있다.

<img src="https://user-images.githubusercontent.com/29545214/85826195-0fcdbf00-b7bf-11ea-8469-59328fb301fd.png">

병합 후 분산 파일 시스템에 저장

<img src="https://user-images.githubusercontent.com/29545214/85826219-1fe59e80-b7bf-11ea-9375-b2dd3bf04731.png">

Task 3의 web interface 중 DAG Visualization. Task 3은 약 25분 정도 소요되었다.

<img src="https://user-images.githubusercontent.com/29545214/85826431-8bc80700-b7bf-11ea-8b80-85ea0db15758.png">

<hr>

Task 2와 Task 3의 결과를 이용하여 Task 4를 수행한다.

args(0)에 Task 2의 결과를, args(1)에 Task 3의 결과를, args(2)에 output file path를 입력한다.

``` bash
$ spark-submit --class bigdata.Task4 task4.jar task2_out task3_out task4.out
```

<img src="https://user-images.githubusercontent.com/29545214/85826472-997d8c80-b7bf-11ea-9d19-1cdfafefdc91.png">

결과를 확인해보면 u, cc(u)가 정상적으로 저장돼있음을 확인할 수 있다.



```bash
$ hdfs dfs -cat task4.out/* | head
```

<img src="https://user-images.githubusercontent.com/29545214/85826936-21fc2d00-b7c0-11ea-8124-dc07fd7e4d30.png">

Task 4의 web interface 중 DAG Visualization. 한 눈에 보아도 Task 3보다 단순한 작업이었음을 알 수 있다. 소요 시간은 35초.

<img src="https://user-images.githubusercontent.com/29545214/85827094-815a3d00-b7c0-11ea-9fb6-dd86011d2ca6.png">

<hr>

- 소감

  분산 처리를 경험해볼 수 있는 좋은 기회였다.

  늘 병렬 컴퓨팅을 하다가 분산 컴퓨팅에서는 경쟁 상태가 없다는 말에 쉬울 줄 알았는데, 네트워크나 Disk I/O 같은 복병들이 숨어있어 굉장히 많은 생각을 하며 코드를 써내려갔다.

  좀 더 optimal한 코드를 쓰고싶었는데, 시험 기간에 쫓겨 부랴부랴 하다보니 간신히 구현만 해낸 것 같아 아쉽다.

