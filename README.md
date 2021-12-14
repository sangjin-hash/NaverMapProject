# NaverMapProject
## Index
  - [Overview](#Overview)
  - [Period](#Period)
  - [System](#System)
  - [Roles](#Roles)
  - [View](#View)
  - [Challenge](#Challenge)
  - [Demo](#Demo)
  - [Future_plan](#Future_plan)
  
## Overview
**국내 운전자 10명중 6명이 주차문제로 스트레스를 겪는다고 한다.** 주차 문제 때문에 차를 끌고 나갈지, 말지를 고민하는 경우가 많다는 응답이 81.5%로 조사되었다.
따라서 본 프로젝트를 통해 사용자가 겪는 불편함을 최소화하기 위해 사용자가 가고자 하는 **해당 목적지 주변의 주차장에 대한 정보를 UI를 통해 제공하고, 
주차장에 들어왔을 때 주차가 가능한 자리를 제시하고자 한다.** 주차장 내 자리 수를 판단하는 것은 초음파 센서, 영상촬영카메라가 있는데 이는 각 자리마다 설치해야 하므로 금전적인 비용이
매우 크기 때문에 기존의 방법이 아닌, **오로지 주차장 내 cctv에 접근**하여 딥러닝 기술인 classification을 활용하여 빈 자리를 판단해 사용자의 device로 UI를 통해 정보를 제공하고자 한다.

## Period
#### 21.9.16 ~ 21.12.14 (시험기간 제외 약 8주정도 개발 기간을 가졌다)

## System
#### 전반적인 System의 flow는 다음과 같다.


<img width="80%" src="https://user-images.githubusercontent.com/77181865/145988808-c836e365-8d31-41fb-97aa-090ead362f82.png"/>



## Roles
팀원은 총 2명으로 딥러닝을 맡은 팀원 1명과 진행하였다. **필자는 RaspberryPi, RDBMS 구축 및 LAMP(Linux-Apache-Mysql-PHP), 안드로이드를 맡았다.**


## View

**Develop Environment**
  - Language : Java
  - Device : Google Pixel 5
  - Minimum SDK Version : 16
  - Target SDK Version : 30
  
#### View는 다음과 같다.
  ##### 1. NaverMap View 
  
  - 사용자 주변에 주차장이 있고, 주차장 기준 반경 200m내로 사용자가 들어오게 되면 Marker가 활성화되어 남은자리/전체자리 가 나오게 된다.
  또한 사용자의 위치를 실시간으로 추적하여 주차장 내로 들어오게 되면 두번째 화면으로 넘어가게 된다.
    
  ##### 2. MainService View
  
  - **(Main Service) -> 주차장 내 빈자리를 빨간색 Box를 활용하여 빈 자리임을 나타내준다.** 이는 실시간으로 서버와 통신을 통해 
 딥러닝 모델에서 수정해준 값을 계속 읽어와 UI를 Update한다.
  - 부가서비스 -> 주차장 내 빈자리를 순회하여 좌우 차량이 비어 있는 자리는 파란색 박스로 나타내 주었고, 
 가까운 좌석을 선택할 시 건물 입구로부터 가장 가까운 빈 자리를 파란색 path UI를 통해 길안내를 제시한다.
  

<img width="80%" src="https://user-images.githubusercontent.com/77181865/145992457-a8ce4f69-31ad-4e15-8ecf-5b5c0fe0a2a8.png"/>


  ##### 3. Tracking View
  
  - GPS의 위도, 경도를 ImageView의 x,y 좌표로 변환하여 사용자의 위치를 실시간으로 표시하였다.
  


## Challenge
  #### 1. (Longitude, Latitude) -> ImageView (X,Y)
  
  - 위도와 좌표는 GPS Provider와 Network를 이용하여 받아올 수 있었으나, GPS 상 50m 오차가 발생할 가능성이 있고 위도와 경도를 그대로 ImageView 위로 표현할 수 없기 때문에
  Linear Transform을 통해 변환하여 사용자를 Tracking 하는 UI를 초록색 동그라미 UI로 표현하였다. 
  
  
  #### 2. GPS는 50m 오차가 간혹 발생할 수 있다. 
  
  - 이러한 문제로 인해 ImageView에 UI를 Rendering하는 과정에서 지속적으로 흔들리고 간혹 주차장 외부로 UI가 출력이 되기도 했다. 그래서 위도, 경도를 tuning하는 작업이 필요했고
  별도의 모듈을 추가하여 Test 기반 개발(Test-Driven-Driven)을 해서 임의의 Mock GPS Location 좌표를 생성(Trackingtest Module/src/main/assets/mock_gps_data.csv)하여 
  404개 좌표 중 48개(50m 이상 오차가 발생할 경우), 10개(주차장 밖으로 위도, 경도가 잡힌 경우) 모두 검출하였다. 
  
  #### 3. CCTV 역할을 하는 Raspberry Pi에 카메라 모듈을 추가하여 사진을 지속적으로 촬영한 뒤 TCP/IP Socket 통신으로 딥러닝 모델 내로 전송하였다.
  
  

## Demo
[![Video](https://youtu.be/-6rvRmzgiqY/0.jpg)](https://youtu.be/-6rvRmzgiqY)

## Future_plan
안드로이드를 본격적으로 공부한지 얼마 되지 않아서 책과 강의 위주로 공부하던 찰나, 교내에서 운이 좋게도 '다학제캡스톤디자인'이라는 일선과목으로 개설이 되어서 참여하였다.
안드로이드에 대해 전반적인 지식은 조금밖에 없었던지라 '맨땅에 헤딩이라도 하자' 싶은 마음으로 수강신청을 하였고, 서버에 '서'자도 몰랐는데 이 과목을 통해 서버도 직접 배포해보고
HTTP REST API인 Retrofit2으로 Json 형식의 데이터를 웹서버와 주고 받으며 Raspberry PI까지 사용해볼 수 있었다. 준비하면서 뼈저리게 느꼈던 것은 아직 한참 많이 부족하다는 것을 알았고
기술적인 문제에 직면했을 때 해결하면 그 뒤로 새로운 문제가 발생해서 '개발은 기술적인 문제의 연속'임을 깨달았다. 본 프로젝트에서 Frontend인 안드로이드에서 직접적으로 보여주는 
UI/UX 퍼포먼스가 적어서 아쉬웠으나 이러한 값진 경험을 토대로 더욱 더 성장할 수 있는 개발자가 되고자 한다. 따라서 향후 계획으로는 유지보수 및 Clean Code에 초점을 맞춰 준비해보고자 한다.

  1. Clean Code, Design Pattern(MVVM, MVP, Singleton, ...)에 대하여 공부를 하고 적용해보기
  2. 본 프로젝트에서 Multi Thread로 구현한 것이 많았는데, 비동기 프로그래밍(RxJava, Coroutine, ...)등 여러가지 기술들을 공부하고 활용해보기
  3. Espresso, JUnit4와 같은 framework를 이용하여 자동화 UI Test Code 작성하기
  
### 끝으로, 위의 세 가지 향후 계획을 토대로 준비해서 더욱 더 성장할 수 있는 모습을 보여주겠다.
  
  
