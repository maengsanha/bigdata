### AWS Cloud9 환경에서 Docker를 이용한 Amazon Document DB 활용

소프트웨어학부 맹산하



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
   $ mongo --ssl --host endpoint --sslCAFile rds-combined-ca-bundle.pem --username yourMasterUsername--password yourMasterPassword
   ```

   <img src="https://user-images.githubusercontent.com/29545214/85934629-8afab680-b920-11ea-934c-3ccf5e90ca14.png">

   

   1.3.2. mongo Shell을 이용하여 DocumentDB에 접속한 모습

   <img width="1680" alt="step3-2-completed" src="https://user-images.githubusercontent.com/29545214/85934639-ac5ba280-b920-11ea-9fa9-238496770b0e.png">

   <hr>

   

2. AWS Cloud9 환경에서 Docker를 활용하여 DocumentDB에 접속

   

   DocumentDB 클러스터는 기본적으로 27017번 포트를 사용하기 때문에, Cloud9의 EC2 인스턴스에서 생성된 컨테이너에서 이 클러스터에 접근하려면 27017번 포트를 열어줘야 합니다.

   27017번 포트를 인바운드 규칙에 추가합니다.

   <img src="https://user-images.githubusercontent.com/29545214/85953255-08b6d480-b9aa-11ea-823d-e134aa85dcf1.png">

   

   포트가 열렸으니, Cloud9에서 Docker 컨테이너를 띄우고 `1.3`에서와 같이 mongo shell을 이용해 DocumentDB에 접속합니다.

   ```bash
   $ docker exec -it mongodb bash
   $ wget https://s3.amazonaws.com/rds-downloads/rds-combined-ca-bundle.pem
   $ mongo --ssl --host DocumentDB_cluster_DNS:27017 --sslCAFile rds-combined-ca-bundle.pem --username user_name
   ```

   

   Cloud9에서 Docker를 활용해 DocumentDB에 접속한 모습

   <img src="https://user-images.githubusercontent.com/29545214/85953293-55021480-b9aa-11ea-8081-b2ade5cc3c04.png">

   

3. MongoDB 실습 시나리오

   1

   

4. MongoDB와 Amazon DocumentDB 각각의 장/단점

   1

   

5. DocumentDB 클러스터 삭제

   1

   

6. 크레딧 차감량과 대략적인 계산

   1

