### AWS Cloud9 환경에서 Docker를 이용한 Amazon DocumentDB 활용



1. Amazon DocumentDB를 이용한 MongoDB 환경 구축

   

   1.1.1. DocumentDB 클러스터 생성

   

   ​		[안내 가이드](https://docs.aws.amazon.com/documentdb/latest/developerguide/getting-started.html)에 따라 DocumentDB 클러스터를 생성합니다.

   ​		인스턴스 클래스는 가장 저렴한 db.r4.large로, 인스턴스 갯수는 1개로 설정하여 복제본을 두지 않도록 설정했습니다.

   ​		네트워크 설정, 로깅 등의 기타 설정은 default 설정을 따랐습니다.

   ​		이 때, 인스턴스 갯수를 3개로 하면 2개의 복제본을 만들게 됩니다.

   ​		하둡 파일 시스템에서 파일이 생성되면 1~2개의 복제본을 만들어 저장하는 것과 같이 데이터가 소실될 확률을 크게 낮출 수 있다는 장점이 있으나,  		더 많은 storage를 쓰게 되므로 비용이 좀 더 든다는 단점이 있습니다.

   ​		인스턴스 갯수가 1개일 경우엔, 반대로 비용은 더 저렴하지만 데이터가 소실될 경우 복구할 방법이 없다는 단점이 있습니다.

   <img src="https://user-images.githubusercontent.com/29545214/85934442-4ae60480-b91d-11ea-9eb3-8ba9961f8398.png">

   

   1.1.2. DocumentDB 클러스터가 생성된 모습

   <img src="https://user-images.githubusercontent.com/29545214/85934454-87196500-b91d-11ea-9d0c-b55ec3193bd8.png">

   

   1.2.1. EC2 인스턴스 생성 - AMI

   ​		Ubuntu 18.04 LTS로 선택합니다.

   <img src="https://user-images.githubusercontent.com/29545214/85934459-9b5d6200-b91d-11ea-9f48-f4a4035c0bfb.png">

   1.2.2. EC2 인스턴스 생성 - 인스턴스 유형

   ​		프리티어에서 사용 가능한 t2.micro로 선택합니다.<img src="https://user-images.githubusercontent.com/29545214/85934527-74536000-b91e-11ea-80d6-c3b73d4ffced.png">

   

   1.2.3. EC2 인스턴스 생성 - 스토리지 추가

   ​		기본으로 8GB가 제공되지만 프리티어는 30GB까지 사용 가능하므로 30GB로 늘립니다.<img src="https://user-images.githubusercontent.com/29545214/85934540-a795ef00-b91e-11ea-8ba0-b5219ee017ee.png">

   

   1.2.4. EC2 인스턴스 생성 - 보안 그룹 설정

   ​		SSH 접속을 위해 22번 포트를 인바운드 규칙에 추가합니다. (22번 포트를 나중에 추가해서, 추가한 후의 모습)

   <img src="https://user-images.githubusercontent.com/29545214/85953252-f9d02200-b9a9-11ea-99ef-aea29c3d2bf1.png">

   

   1.2.5. EC2 인스턴스 생성 - 검토

   ​		인스턴스 생성 전 설정을 확인합니다.<img src="https://user-images.githubusercontent.com/29545214/85934562-0f4c3a00-b91f-11ea-8860-bbc4251ea289.png">

   1.2.6. EC2 인스턴스 생성 - 키 페어 생성

   ​		원격 접속을 위한 키 페어를 생성하고 다운로드합니다.

   <img src="https://user-images.githubusercontent.com/29545214/85934594-bb8e2080-b91f-11ea-86dd-4ccd338d7323.png">

   1.2.7. EC2 인스턴스가 성공적으로 생성된 모습<img src="https://user-images.githubusercontent.com/29545214/85934601-f42dfa00-b91f-11ea-81d3-954488854522.png">

   1.3.1. 생성된 EC2 인스턴스에서 mongo Shell을 이용하여 DocumentDB 클러스터에 접속

   ​		`1.2.6` 에서 생성한 키 페어를 이용해 EC2 인스턴스에 접속합니다.

   ​		접속 후 mongo Shell를 설치하고 DocumentDB 클러스터에 접속합니다.

   ```bash
   $ sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2930ADAE8CAF5059EE73BB4B58712A2291FA4AD5 
   $ echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.6 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.6.list
   $ sudo apt-get update
   $ sudo apt-get install -y mongodb-org-shell
   $ wget https://s3.amazonaws.com/rds-downloads/rds-combined-ca-bundle.pem
   $ mongo --ssl --host endpoint --sslCAFile rds-combined-ca-bundle.pem --username yourMasterUsername
   ```

   <img src="https://user-images.githubusercontent.com/29545214/85934629-8afab680-b920-11ea-934c-3ccf5e90ca14.png">

   

   1.3.2. mongo Shell을 이용하여 DocumentDB에 접속한 모습

   <img width="1680" alt="step3-2-completed" src="https://user-images.githubusercontent.com/29545214/85934639-ac5ba280-b920-11ea-9fa9-238496770b0e.png">

   <hr>

   
2. AWS Cloud9 환경에서 Docker를 활용하여 DocumentDB에 접속

   

   DocumentDB 클러스터는 기본적으로 27017번 포트를 사용하기 때문에, Cloud9의 EC2 인스턴스에서 생성된 컨테이너에서 이 클러스터에 접근하려면 27017번 포트를 열어줘야 합니다.

   27017번 포트를 인바운드 규칙에 추가합니다.

   <img src="https://user-images.githubusercontent.com/29545214/85953255-08b6d480-b9aa-11ea-823d-e134aa85dcf1.png">

   

   포트가 열렸으니, Cloud9에서 Docker 컨테이너를 실행하고 `1.3`에서와 같이 mongo shell을 이용해 DocumentDB에 접속합니다.

   ```bash
   $ docker exec -it mongodb bash
   $ wget https://s3.amazonaws.com/rds-downloads/rds-combined-ca-bundle.pem
   $ mongo --ssl --host endpoint --sslCAFile rds-combined-ca-bundle.pem --username yourMasterUsername
   ```

   

   Cloud9에서 Docker를 이용해 DocumentDB에 접속한 모습

   <img src="https://user-images.githubusercontent.com/29545214/85953293-55021480-b9aa-11ea-8081-b2ade5cc3c04.png">

   <hr>

   

3. MongoDB 실습 시나리오

   

   3.1. 시나리오와 데이터 셋 다운로드 및 DocumentDB로 전송

   ​	3.1.1. 시나리오

   ​	COVID19가 잠잠해질 기미를 보이지 않고 있습니다.

   ​	마스크 착용은 매너가 아닌 의무가 되었고, 마스크 소비량이 늘어남에 따라 마스크 판매처를 알아야할 필요성도 증가했습니다.

   ​	이 프로젝트에서는 공공 데이터 활용 지원센터에서 제공하는 [주소 기준 공적 마스크 판매정보 데이터](https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/storesByAddr/json)를 Amazon DocumentDB 환경에서 MongoDB 쿼리를  이용하여 내 주변 마스크를 파는 곳은 어디인지, 마스크 재고량은 얼마나 있는지 알아보겠습니다.

   

   ​	3.1.2. 데이터 셋 다운로드

   ```bash
   $ wget https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/storesByAddr/json
   ```

   <img src="https://user-images.githubusercontent.com/29545214/86060132-1732e800-ba9f-11ea-90e9-8c130d42566b.png">

   

   ​	3.1.3. DocumentDB로 전송 (mongoimport)

   ```bash
   $ mongoimport --collection masksales --file masks.json --jsonArray --ssl --host endpoint --sslCAFile rds-combined-ca-bundle.pem --username yourMasterUsername
   ```

   <img src="https://user-images.githubusercontent.com/29545214/86060153-23b74080-ba9f-11ea-95d1-ac432cefcaa2.png">

   ​	

   3.2. 데이터 셋을 이용한 쿼리

   1. 우선 데이터를 일부 살펴보겠습니다.

      ```javascript
      db.masksales.find().limit(5).pretty()
      ```

      <img src="https://user-images.githubusercontent.com/29545214/86060314-6e38bd00-ba9f-11ea-8baf-5071f73865e8.png">

   2. 내 주변 마스크 판매처를 찾아보겠습니다. addr 필드가 주소를 나타내므로 서울특별시 강남구 삼성로에 있는 마스크 판매처를 검색하겠습니다.

      ```javascript
      db.masksales.find({addr: {$regex: "^서울특별시 강남구 삼성로"}})
      ```

      <img src="https://user-images.githubusercontent.com/29545214/86068896-66831380-bab3-11ea-8564-ba3d3dca7612.png">

   3. aggregation과 match를 이용해도 같은 결과를 낼 수 있습니다.

      ```javascript
      db.masksales.aggregate([
        {$match: {addr: {$regex: "^서울특별시 강남구 삼성로"} } }
      ])
      ```

      <img src="https://user-images.githubusercontent.com/29545214/86068924-769af300-bab3-11ea-8e5d-04fb510e74e4.png">

   4. 마스크 재고량도 중요하니, 마스크 재고량도 검색하겠습니다. 우선 마스크 재고 상태를 나타내는 remain_stat의 값에는 어떤 종류가 있는지 알아보겠습니다.

      ```javascript
      db.masksales.find({},{remain_stat:1})
      ```

      <img src="https://user-images.githubusercontent.com/29545214/86068946-84507880-bab3-11ea-8156-af91bde23e9e.png">

   5. remain_stat 필드의 값은 empty, few, some, plenty 중 하나로 정해져있는 것 같습니다.

      마스크를 사러 갔는데 재고가 없으면 곤란하니, remain_stat이 some이나 plenty인 마스크 판매처들의 주소와 재고 상태만 검색하겠습니다.

      ```javascript
      db.masksales.find({$or: [{remain_stat: "some"}, {remain_stat: "plenty"}]}, {addr: 1, remain_stat:1})
      ```

      <img src="https://user-images.githubusercontent.com/29545214/86068976-97fbdf00-bab3-11ea-9814-84ece5b95844.png">

   6. in을 사용하면 같은 결과를 좀 더 짧은 쿼리문으로 낼 수 있습니다.

      ```javascript
      db.masksales.find({remain_stat: {$in: ["some", "plenty"]}}, {addr: 1, remain_stat:1})
      ```

      <img src="https://user-images.githubusercontent.com/29545214/86068994-a2b67400-bab3-11ea-906b-979954333d6b.png">

   7. 이제 강남구 삼성로에 마스크 재고량이 넉넉한 판매처를 검색해보겠습니다.

      마스크 재고 상태가 some이나 plenty이고, 삼성로에 있는 판매처의 주소와 재고 상태만 검색하겠습니다.

      ```javascript
      db.masksales.aggregate([
        {$match: {addr: {$regex: "^서울특별시 강남구 삼성로"}}},
        {$match: {$or: [{remain_stat: "some"}, {remain_stat: "plenty"}]}},
        {$project: {_id: 0, addr: 1, remain_stat:1}}
      ])
      ```

      <img src="https://user-images.githubusercontent.com/29545214/86069029-bb268e80-bab3-11ea-92ff-05931d503ec8.png">

   8. out을 이용하여 위 결과를 near_masksales로 저장하겠습니다.

      ```javascript
      db.masksales.aggregate([
        {$match: {addr: {$regex: "^서울특별시 강남구 삼성로"}}},
        {$match: {$or: [{remain_stat: "some"}, {remain_stat: "plenty"}]}},
        {$project: {_id: 0, addr: 1, remain_stat:1}},
        {$out: 'near_masksales'}
      ])
      ```

      <img src="https://user-images.githubusercontent.com/29545214/86069055-c974aa80-bab3-11ea-8edb-d68588229673.png">

   9. 자바스크립트에서처럼 변수에 저장할 수도 있습니다.

      ```javascript
      near_masksales = db.masksales.aggregate([
        {$match: {addr: {$regex: "^서울특별시 강남구 삼성로"}}},
        {$match: {$or: [{remain_stat: "some"}, {remain_stat: "plenty"}]}},
        {$project: {_id: 0, addr: 1, remain_stat:1}}])
      ```

      <img src="https://user-images.githubusercontent.com/29545214/86069075-d5f90300-bab3-11ea-8384-6a29ff8e8c73.png">

   10. 마지막으로 컬렉션을 삭제하고 종료합니다.

       ```javascript
       db.masksales.drop()
       db.near_masksales.drop()
       exit
       ```

       <img src="https://user-images.githubusercontent.com/29545214/86069242-499b1000-bab4-11ea-9249-6ed6b0ae55f0.png">

   <hr>

   

4. Docker를 활용한 MongoDB 운용과 Amazon DocumentDB를 활용한 운용 각각의 장/단점

   

   4.1. Docker를 활용한 MongoDB 운용

    - 장점

      플랫폼(AWS, Google Cloud Platform, Microsoft Azure 등)에 구애받지 않고 호스팅이 가능합니다.

      버전 선택이 자유롭습니다.

    - 단점

      모니터링 등의 추가적인 기능을 사용하려면 별도의 설정이 필요합니다.

      

   4.2. Amazon DocumentDB를 활용한 운용

   - 장점

     클러스터 생성이 매우 간단합니다.

     완전 관리형이기 때문에 DB 유지/보수 관리면에서 유리합니다.

     기존 AWS 제품들과 호환이 잘 됩니다.

   - 단점

     AWS라는 하나의 플랫폼에 국한되게 되는데 멀티 클라우드 접근 방식을 사용하는 어플리케이션이 많은 요즘, vendor 의존성이 높은 것은 단점이 될 수 있습니다.

     또한 DocumentDB는 MongoDB와의 호환성을 강조하지만, 사실은 Aurora PostgreSQL 엔진을 사용하면서 일부 MongoDB API를 모방하는 형태를 취하여 특히 MongoDB 3.6 버전의 API를 사용하기 때문에 MongoDB 4.0에서 릴리즈된 여러 기능들(oplog 등)을 사용할 수 없게 됩니다. BSON 표준의 일부만 지원하는 점 또한 단점이라 할 수 있습니다.
     
     
     
     분명 DocumentDB는 편리하고 강력하고, 매력적인 제품입니다.
     
     하지만 제품 선택 시 DocumentDB에서는 지원하지 않는 MongoDB의 기능을 사용해야할 경우가 있는지, vendor 의존성이 과하지는 않은지 신중하게 판단해야할 것 같습니다.

   <hr>

   

5. DocumentDB 클러스터 삭제

   5.1. 삭제 보호 수정

   ​	클러스터를 삭제하기 위해 우선 인스턴스 삭제 보호를 비활성화합니다.

   ​	<img src="https://user-images.githubusercontent.com/29545214/86069656-8fa4a380-bab5-11ea-8ec8-4226e0a08b49.png">

   5.2. 클러스터를 삭제합니다.

   ​	<img src="https://user-images.githubusercontent.com/29545214/86069687-a0edb000-bab5-11ea-968b-179936f897f7.png">

   <hr>

   

6. 크레딧 차감량과 대략적인 계산

   <img src="https://user-images.githubusercontent.com/29545214/86070105-c4fdc100-bab6-11ea-9d5f-58c0250da1ba.png">

   수업 시간에 약 8달러를 사용하여 42달러 정도에서 과제를 수행하기 시작했습니다.
   
   약 14달러를 사용한 셈인데, 과금된 금액을 계산해보면 다음과 같습니다.
   
   Cloud9의 요금은 m5.xlarge를 기준으로 EC2 인스턴스가 0.0116달러, 스토리지가 월당 1.0달러 정도이고 3일 정도 사용하였지만 창을 닫은 후 1시간 뒤에 세션이 종료되도록 설정했기 때문에 실제 사용량은 그만큼 많지 않아 약 20시간 정도로 계산할 수 있을 것 같습니다. 따라서 0.3달러가 조금 넘는 정도로 추정됩니다.
   
   DocumentDB의 요금은 db.r4.large를 기준으로 시간당 0.277달러입니다. 이는 2일 간 종료되지 않고 계속 켜져있었으므로 13달러가 조금 넘게 과금된 것으로 추정됩니다.
   
   따라서 대부분의 과금이 DocumentDB에서 발생했다고 예상할 수 있습니다.

