# NaverMapProject

## 1. 프로젝트 시작 & Map 초기 설정(9/16 ~ 9/22)
- Naver Map API (Mobile Dynamic App)을 활용하여 Naver Map 연동 완료

- 사용자의 현재 위치를 실시간으로 추적하고, 사용자의 위치에 따라 카메라 이동

- 사용자가 팔달관 주차장 반경 200m내에 접근 시, Marker가 생기고 info 창에 현재 주차 가능한 자리/전체 주차장 자리 정보가 뜸

- Map의 초기설정으로는 zoom level, layergroup, maptype, 야간 모드, 실시간 위치 추적 이벤트 등 navigation에 필요한 초기 설정들을 구현하였음.



## 2. Navigation Service(9/24 ~ 9/27)
- 사용자의 Map Click Event 발생 시 Marker & InfoWindow 객체가 생성되어 해당 Info를 선택하면, 해당 Marker의 LatLng 객체를 Param으로 
navigation service가 시작됨

- AsyncTask abstract class를 가지고 비동기식으로 동작하도록 구현한다. 
 
- Background에서 Naver Direction API를 제공하는 URL에 접근하여 Retrofit을 통해 HTTP 

### 9월 25일 기준 문제점...
- Navigation.java의 클래스가 부정확하다 -> 더 연구해서 수정해야 될듯
- Naver Cloud Platform의 Direction API는 호출이 되지만, AsyncTask의 doInBackground 내부에 Retrofit 객체를 만들고 호출이 발생할 때 onFailure 메소드로 접근하여 
  
      Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 2 path $ 
  
  라는 error가 발생함.
  
- 이러한 문제점을 해결 못할 시 Retrofit을 사용하지 않고, clone 해온 Open Source를 참고하여 구현한다.

    https://github.com/jeondoohyun/NaverMap_Directions
    
- 지도교수님 Comment(9/27) 
    1. 위의 toy navigation app은 부가적인 기능이고, 본 프로젝트의 실질적인 main 서비스가 아니기 때문에 Deep Learning model을 통한 output에 대한 test case를 만들고 main service를 먼저 구현하도록 한다.
    2. 위의 toy navigation app에서 발생한 문제는 안드로이드 sdk내에 있는 JSON 라이브러리를 이용해서 데이터를 받아오고 파싱한다.
    3. 다음 회의까지 main service에 대한 prototype(1. 목적지 주차장에 진입했을 때 오버레이된 주차장 구조도가 zoomin된다 -> 2. 빈 자리에 대한 UI가 나타나도록 한다) 구현    
    
    
    
## 3. Main Service 구현(9/28 ~
- 주차장 내에 들어오는 것을 판단하면(how? 팔달관 주차장 가운데 기준 반경 50m 내) MainServiceActivity로 intent가 넘어감.
- 기존의 Foreground에 있는 Toy Navigation App은 life cycle의 onPause() 상태에 있고, MainService를 담당하는 Activity가 Foreground로 오면서 parking_lot_activity.xml으로 전환.
- ImageView 위에 빈자리 UI를 띄우기 위해 SurfaceView를 올려놓았고, SurfaceView의 Canvas를 통해 빈 자리에 대한 UI를 그린다.

### 9월 30일 기준 문제점...     => 해결(10/7)
빈 자리 UI를 하나 띄우는 것은 가능하지만, Thread가 여러번 돌고 draw()를 여러번 호출한다고 했을 때 기존의 그렸던 UI는 지워지고, 마지막에 그려진 빈 자리 UI만 남음
-> Canvas를 그대로 유지하면서 여러번 그리고 지우는 Action이 가능하게 구현 해야 한다(Canvas 객체를 생성했을 때 한번만 그리면 그 위에 중복으로 작업할 수 없음)

### 위의 문제점 해결 방안 
우선 9월 30일에 발생한 문제점을 해결하기 위해선 크게 두 가지 방법이 있다.

#### 1. layout에 ImageView를 넣지 않고 별도의 View를 상속한 Java 클래스를 생성한 뒤, Bitmap과 getResources()를 통해 png파일을 불러와 Image를 생성한다. 그 이후 onDraw()와 invalidate()함수를 사용하여 화면을 갱신하는 방법

#### 2. ImageView와 동일한 SurficeView를 최상단에 올려놓고 그 위에서 작업을 하는 방법 => 선택

해결방안 2번을 선택한 이유는 다음과 같다.
- UI update는 Cloud에서 REST API를 통해 지속적으로 통신을 하며 Data를 get해야 하고, UI update를 위해 별도의 Thread에서 지속적으로 반복 작업을 해야 update를 할 수 있다.
- invalidate() 메소드는 Thread 내부에서 작동할 수 없다.

View 클래스에 정의된 invalidate() 메소드를 호출하면, 해당 View 화면이 무효(invalid)가 되고, 안드로이드 logic은 현재의 View 상태를 반영하여 새로 갱신해준다. 
그러나 본 프로젝트의 Evaluation을 정량화하여 평가할 때 Service time(UI update time)을 측정하기 때문에 Update Service Time을 줄이기 위해 UI Thread 내에서 통신을 하는 것이 아닌 별도의 Thread에서 통신을 함으로써 UI Thread에 Overloading을 하지 않고 ANR이 발생하지 않도록 하므로 Surfice View를 활용하였다. 



## 4. Main Service Todolist(10/7~10/19)
#### 1. 촬영 범위 밖의 사각지대 영역을 제외한 모든 자리에 대해 Section을 나누고, Section 별 x, y 좌표에 대한 data structure 구성 => 해결

#### 2. 각 Section 별 제 자리에 UI가 뜨도록 구성 => 해결

#### 3. Navigation UI 생성(주차장 여석 정보) => 해결

#### 4. 사용자의 현위치를 받아와서 위도 경도를 ImageView의 x,y 좌표로 변환한 뒤(Linear Transform) ImageView 위에 Rendering하기 => 해결했으나 문제점 발생
- 사용자의 위치에 따라 ImageView에서 UI(초록색 원)로 표시를 하였으나 가끔 위도와 경도가 이상치를 나올 경우를 제외하고, UI가 사라지는 경우가 발생하였다. 이에 따라 Debug를 통해 오류를 분석하여 코드를 수정할 계획 

#### 5. 서버 구축이 완료된 이후(Rest API를 활용해 주차장 자리 여부 Json 데이터를 받아올 수 있을 때) Retrofit을 통해 update_testCase() Refactoring 하기


### 10/28 문제점...
- Main Service Todolist 의 4번 Rendering에서 문제점을 파악하기 위해 디버그 모드로 탐색해본 결과, MainService 스레드를 하나만 생성하였고 UI(Main) 스레드까지 총 두 개의 스레드로 이루어져야 하는데 디버그 모드에서는 총 3개의 스레드(Main, MainService Thread, 원인 모를 Thread)로 구성되어 있는 것을 확인하였다. Location에 대한 UI를 띄우는 thread가 Running 할 때, 원인 모를 Thread는 wait하고 있고, UI가 사라지는 시점에서는 wait하고 있던 Thread가 Running, Location Thread가 wait 하게 되는 문제점을 포착하였다. 
- 이를 해결하기 위해 생각해낸 방법으로는 SingleThreadPool 혹은 Handler 부분 수정 총 두가지이고 최악의 경우 Thread가 아닌 AsyncTask 로 작업을 하는 구조로 Refactoring 할 계획이다.

