## [과제 3] 대규모 말뭉치(KCC 원시말뭉치)에서 가장 유사한 문장 상위 n개 추출

<과제 목적> 대규모 말뭉치 (빅 텍스트 데이터) 분석의 문제점, 해결방안 실습

- 입력: 한글 문장 1개

- 출력: 입력문장과 가장 유사한 n개의 문장 추출 및 유사도, 소요시간 출력

- 방법: 위 2번의 형태소분석 (또는 WPM) 방식의 유사도 계산 모듈을 대규모 말뭉치에 적용, 말뭉치의 각 문장들과 순서대로 비교하여 가장 유사도가 높은 상위 n개 문장 및 유사도, 소요시간을 출력
  <br>
  n값은 실행할 때 command line 인자 (또는 사용자 입력) 으로 받음

  <br>

  <참고1>
  <br>
  말뭉치의 각 문장에서 토큰 추출 방식은 입력문장에서 토큰 추출과 동일한 방식 (형태소 분석기 또는 WPM) 을 사용해야 함.

  <참고2>
  <br>
  말뭉치의 각 문장에 대한 토큰 추출은 유사도 비교 전에 한꺼번에 배치처리 방식으로 처리하여 저장하는 방법, 또는 유사도 계산 직전에 토큰을 추출해도 됨.
  <br>
  이 때 문장에서 토큰 추출하는 시간을 반드시 `소요시간 (실행시간)` 에 포함시켜야 함

  <참고3>
  <br>
  말뭉치의 각 문장/라인을 미리 소팅한 후에 사용해도 되며, 소팅 시간은 `소요시간 (실행시간)` 에서 제외함.

  <참고4>
  <br>
  각 문장의 어절들을 문장 내에서 소팅하여 저장한 후에 사용해도 되며, 이 처리시간은 `소요시간 (실행시간)` 에서 제외함.

<br>

* 제출하는 모든 실습 및 과제의 소스 코드는 아래 GitHub 경로에 있습니다.

  `https://github.com/joshua-dev/bigdata`

<br><br>

## 실행 결과

- ### tokenizer (SPM model) 생성
  
  <img width="732" alt="make_start" src="https://user-images.githubusercontent.com/29545214/78385065-3e038e80-7616-11ea-9b37-ec26aa0121a6.png">

  <img width="732" alt="train" src="https://user-images.githubusercontent.com/29545214/78385109-4bb91400-7616-11ea-9c20-e99c8b683914.png">

- ### 상위 n개 유사 문장 추출 및 소요 시간 출력

  - 10000개의 단어로 학습했을 때

  <img width="732" alt="result_10000" src="https://user-images.githubusercontent.com/29545214/78385128-570c3f80-7616-11ea-8e17-5f95868c5568.png">

  - 20000개의 단어로 학습했을 때

  <img width="732" alt="result_20000" src="https://user-images.githubusercontent.com/29545214/78385154-64c1c500-7616-11ea-9a92-a8030ff3d450.png">

  - 50000개의 단어로 학습했을 때

  <img width="732" alt="result_50000" src="https://user-images.githubusercontent.com/29545214/78385200-74410e00-7616-11ea-9d0f-eec1e816ccef.png">

<br>

## Installation
```shell
pip install -r requirements.txt
```

## Run

```shell
// n means the number of high similarity sentences to be printed.
// ex) make argv=10
make argv=n
```

<br>

## 구현 방법 및 결과 분석

Google에서 제공하는 SPM model의 tokenizer API (sentencepiece) 를 사용했다.

주어진 문장과 가장 유사한 상위 n개의 문장을 추출하는 알고리즘은 다음과 같이 구현했다.

<br>

1. KCC 원시 말뭉치를 이용하여 spm model (bpe model) 을 생성한다.
2. processor를 생성하고 **1** 을 통해 얻은 model을 로딩하여 tokenizing 준비를 마친다.
3. 문장이 입력되면 processor를 통해 tokenizing을 수행한다.
4. KCC 원시 말뭉치를 읽어들여 문장 단위로 자르고 문장 부호를 제거하여 processor에게 전달한다.
5. processor는 **4** 로부터 전달받은 각 문장들을 tokenizing한다.
6. **3** 과 **5** 로부터 얻은 입력 문장 토큰과 KKC 원시 말뭉치의 각 문장들에 대한 토큰을 비교하여 유사도를 계산하고 연관 배열에 저장한다.
7. **6** 에서 얻은 연관 배열을 유사도 순으로 내림차순 정렬하고 유사도가 가장 높은 상위 n개 문장과 수행 시간을 출력한다.

<br>

이 때 model 생성 (학습) 및 토큰 추출 시간은 중간에 I/O가 발생하기 때문에 각각 측정하여 마지막에 더한 값을 최종 소요 시간으로 사용했으며 sorting하는 데에 걸린 시간은 포함시키지 않았다.

또한 model을 생성할 때 몇개의 문장으로 학습시킬지 입력하는데, 이를 vocabulary size라 한다.

이번 과제에선 vacabulary size를 10000개, 20000개, 50000개로 늘려가면서 유사한 문장 추출의 변화를 관찰했다.

그 결과 같은 말뭉치로 학습한 모델에 같은 문장을 입력해도 vocabulary size에 따라 tokenizing 방법이 달라지기 때문에 유사한 문장들의 유사도 순위가 바뀔 수 있고
<br>
순위가 같더라도 유사도가 다를 수 있는 것을 확인할 수 있다.

<br>

- **1** 을 구현한 함수 train

  ```python
  def train(vocab_size: int):

    '''
    train creates spm model using a given text data.

    @param vocab_size the number of sentences the model will use to train
    
    '''

    spm.SentencePieceTrainer.Train(f'--input=./src/spm/KCC940_Korean_sentences_UTF8.txt --model_prefix=BPE --vocab_size={vocab_size}')
  ```
  
  <br>

- **2 ~ 6** 을 구현한 함수 get

  ```python
  def get(inputSentence: str) -> dict:

    '''
    get returns similarites of sentences in KCC sentences to a given Hangul sentence.

    @param inputSentence a Hangul sentence
    @return a map containing similarities between sentence of KCC sentences and input sentence

    '''
  ```

  이 때, **6** 에서 입력한 문장과 KCC 원시 말뭉치의 문장 간 유사도를 계산하기 위해 compare 함수를 구현했다.

    ```python
    def compare(tokens_first: list, tokens_second: list) -> float:

      '''
      compare compares two token lists using BPE model.

      @param tokens_first a Hangul sentence tokens.
      @param tokens_second a Hangul sentence tokens.
      @return similarity between two sentence tokens using BPE model.
      
      '''
    ```

  <br>

- **7** 을 구현한 함수 main

  ```python
  def main(argv):

    '''
    main runs training model, sorting and printing results and timing.

    @param argv command-line parameter
    
    '''
  ```
