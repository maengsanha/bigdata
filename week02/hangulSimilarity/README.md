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

## 실행 결과

![result]()

<br><br>

## Installation

```shell
go get github.com/joshua-dev/bigdata/week02/hangulSimilarity/src/hangulsimilarity
```
<br>

## Run

```shell
make
```

<br>

## Test

```shell
make test
```

<br><br>

* 제출하는 모든 실습 및 과제의 소스 코드는 아래 GitHub 경로에 있습니다.
  ```https://github.com/joshua-dev/bigdata```

<br><br>

## 구현 방법

1. 음절 ngram 방식
   

2. WPM model
