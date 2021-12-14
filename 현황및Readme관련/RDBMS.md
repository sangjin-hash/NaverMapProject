# RDBMS

### 1. 사전작업
- Naver Cloud 생성
- MySQL DB 생성
- 포트포워딩
- References
  
  Naver Cloud youtube : https://youtu.be/3sybjS7Z3S0
  
  Naver Cloud Guide : https://guide.ncloud-docs.com/docs/database-database-5-6










### 2. Putty를 이용해 원격 접속


##### (1) 포트 포워딩 정보에 서버 접속용 공인 IP와 외부 포트를 기입한다.
![111](https://user-images.githubusercontent.com/77181865/141427175-d870780b-a1cb-402b-b665-5ef660eb114a.png)





##### (2) 아이디 : root와 해당 비밀번호를 입력
![222](https://user-images.githubusercontent.com/77181865/141427371-f0920ac9-f21f-41ce-81a5-c22a900cb702.png)


- 비밀번호 변경 명령어
    
      passwd root
    
    


### 3. MySQL Client & MySQL Community Server 설치
- MySQL Client 설치


      yum -y install http://dev.mysql.com/get/mysql-community-release-el7-5.noarch.rpm
    
      
- Community Server 설치


      yum -y install mysql-community-server 
    
    
    
    
    
### 4. All Product -> Cloud DB for Mysql -> Private 도메인 / DB 접속 포트 확인
- DB 관리 -> User 관리 -> mysql_dba(관리자용 DB 계정) 비밀번호 바꿀 수 있음



### 5. MySQL 접속
    mysql -h "Private 도메인명" -u "user_id" -p --port "DB 접속포트"
    
    ex) mysql -h db-8dl88.cdb.ntruss.com -u mysql_dba -p --port 3306
    
    
![333](https://user-images.githubusercontent.com/77181865/141428850-818ff354-7782-4fa1-970a-c81bad721581.png)





-------------------------------------------------------------------------------------------------------------------------------





# PHP 


### 1. Apache, PHP, PHP - Mysql 설치
- Apache 설치

      yum -y install httpd
    
    
- PHP 설치

      yum -y install php
      
      
- PHP - Mysql 설치

      yum -y install 
      
      
    
### 2. 자동 시작 설정
- Apache 자동 시작

      chkconfig httpd on
      
      
- Mysql 자동 시작 

      chkconfig mysqld on
      
      
### 3. Apache 설정 파일 추가

      vi /etc/httpd/conf/httpd.conf
      
      LoadModule php5_module modules/libphp5.so
      AddType application/x-httpd-php .php .php3 .php4 .php5 .html .htm .inc
      
      
### 4. Start
- Apache Start


      service httpd start
      
      
- Mysql Start


      service mysqld start 
      
      
      
      
      
      
# PHP 구동 확인
- var/www/html/ 에 php 파일을 생성하여 확인하기

![444](https://user-images.githubusercontent.com/77181865/141436848-1ef36e52-9028-4599-8218-8ccf1fb7f706.png)



- test.php가 잘 생성되었는지 FTP를 통해 확인하기(Host : sftp://[서버 접속 공인IP], User : root, Password : [root 
![666](https://user-images.githubusercontent.com/77181865/141437394-35b604bf-380b-4abe-914f-8aa95e9d8703.png)






------------------------------------------------------------------------------------------------------------------------------------------





# 네이버 클라우드 웹서버 열기

### 1. 방화벽 확인

    firewall-cmd --state
    
    
### 2. 위의 1번에서 Not Running 결과가 나올 때


#### (1) yum update 하기


    yum update
    
    
#### (2) 방화벽 확인하기


    firewall-cmd
    
    
#### (3) 작동 여부 확인하기


    1. systemctl status firewalld
    
    2. firewall-cmd --state
    
    
#### (4) 위 3번에서 Not Running이 뜰 경우(방화벽 설치 여부 확인) => 방화벽 버전 확인


    yum list installed firewalld
    
    
#### (5) 방화벽 설치하기


    yum install firewalld
    
    
#### (6) 방화벽 실행하기


    systemctl start firewalld
    
    
#### (7) 방화벽 설치 여부 확인    => active가 뜨면 성공


    systemctl status firewalld
    
    
![555](https://user-images.githubusercontent.com/77181865/141451095-8d0c27c4-ca76-46a1-a6ae-6e1c120d527d.png)




#### 3. 방화벽 자동으로 실행되도록 등록하기


    systemctl enable firewalld
    
    
    
#### 4. 방화벽에 http와 https를 설정하기


    firewall-cmd --permanent --add-service=http
    firewall-cmd --permanent --add-service=https


#### 5. 방화벽 재시작하기


    firewall-cmd --reload
    
    
#### 6. 웹서버 동작 확인하기
##### 정상적으로 웹서버가 동작된 모습이다.
![777](https://user-images.githubusercontent.com/77181865/141474626-bc75612e-7547-4798-b614-e3604e13e63f.png)




##### 웹서버가 동작하지 않을 때 IP주소 0.0.0.0/0 으로 설정하고 Port번호를 80으로 ACG 설정을 추가해줘야 한다.
![888](https://user-images.githubusercontent.com/77181865/141474685-ef7c1a85-b45a-4fb1-b101-69945df1d97f.png)

