## [과제 2] 한글 문장의 유사도 계산 (2)

  <과제 목적> ngram 모델 이해, 형태소 분석 및 토크나이징 이해

  - 입력: 한글 문장 2개 (매우 유사하거나 약간 유사한 문장)

  - 출력: 유사도 (%)

  - 방법: 1) 음절 ngram 방식, 2) 형태소 분석기(또는 WPM model)

      두 가지 방식으로 토큰 추출 후에 유사도 계산

      <참고1> 유사도 계산식은 위 1번 또는 다른 유사도 계산식을 사용해도 됨.

      <참고2> 음절 ngram 모델은 bigram, trigram, bi+tri 3가지로 각각 구현

      입력문장에서 ngram 토큰으로 분할(또는 추출)하는 함수의 인자값이

      2는 bigram, 3은 trigram, 5는 bi tri 둘다 추출하는 방식으로 구현!

      <참고3> 형태소 분석기는 KoNLPy 등에 공개된 것 중에서 사용, 또는

      형태소 분석기 대신에 WPM(또는 SPM) tokenizer를 사용해도 됨.

<br><br>

* 제출하는 모든 실습 및 과제의 소스 코드는 아래 GitHub 경로에 있습니다.
  <br>

  ```https://github.com/joshua-dev/bigdata```

<br>

# 1. 음절 ngram 방식

## 실행 결과

<img src="https://user-images.githubusercontent.com/29545214/77829994-d0181c80-7168-11ea-90e6-081f6996f062.png" width="857" height="866">

<br><br>

## Installation

```shell
go get -v github.com/joshua-dev/bigdata/week02/ngram/src/ngram
```
<br>

## Run

- Bigram
  ```shell
  make
  ./ngram 2
  ```
  <br>
- Trigram
  ```shell
  make
  ./ngram 3
  ```
  <br>
- Bigram + Trigram
  ```shell
  make
  ./ngram 5
  ```

<br>

## Test

```shell
make test
```

<br><br>

## 구현 방법

- 아래와 같이 ngram 모델을 이용한 한글 유사도 계산 알고리즘을 구현했다.
    <br><br>
    1. ngram이라는 type을 정의한다.
    
    2. 문장 1개와 n이 주어지면 해당 문장을 ngram으로 변환한다. 이 때, 해당 문장의 공백과 문장 부호를 모두 제거한다.
    
    3. **2** 에서 얻은 ngram의 각 요소들의 출현 횟수를 계산하고 map 구조에 저장한다.
    
    4. 두 문장을 받으면 각 문장을 **1 ~ 3** 의 과정을 거쳐 ngram 요소들의 출현 횟수를 구하고, 출현 횟수 정보가 담긴 두 map을 비교하여 공통된 요소의 총 갯수를 구한다.
    
    5. 공통된 요소를 길이가 짧은 문장의 ngram 갯수로 나누고 100을 곱하여 유사도를 구한다. (% 단위이므로)

    <br><br>

    - **1** 을 수행하는 코드
    <br>

      ```go
      type ngram []string
      ```

    <br>
    
    - **2** 를 구현하는 함수 New
    <br>

      ```go
      // New returns a new n-gram with the given n and string.
      func New(n int, s string) ngram
      ```
      
      <br>
      이때, 문장 부호와 공백을 제거하는 함수 cleanse를 호출한다.
      <br>
      
        ```go
        // cleanse returns a string with punctuation removed.
        func cleanse(s string) string
        ```
    
    <br>

    - **3** 을 구현하는 함수 count
    <br>

      ```go
      // count counts the frequency of n-grams.
      func count(ngrams ngram) (map[string]int, int)
      ```
    
    <br>
    
    - **4** 를 구현하는 함수 Compare
    <br>

      ```go
      // Compare compares two hangul string with ngram.
      func Compare(first, second string, n int) float64
      ```
    
    <br>
    
    - **5** 를 계산하는 코드 (Compare 함수에 포함)
    <br>

      ```go
      var common float64

      for gram, cnt1 := range firstCount {
        if cnt2, exists := secondCount[gram]; exists {
          common += math.Min(float64(cnt1), float64(cnt2))
        }
      }

      return 100 * common / float64(firstLen)
      ```
      
<br><br>

# 2. WPM model
