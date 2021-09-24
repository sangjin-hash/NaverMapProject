# NaverMapProject

## 1. 프로젝트 시작 & Map 초기 설정(9/16 ~ 9/22)
- Naver Map API (Mobile Dynamic App)을 활용하여 Naver Map 연동 완료

- 사용자의 현재 위치를 실시간으로 추적하고, 사용자의 위치에 따라 카메라 이동

- 사용자가 팔달관 주차장 반경 200m내에 접근 시, Marker가 생기고 info 창에 현재 주차 가능한 자리/전체 주차장 자리 정보가 뜸

- Map의 초기설정으로는 zoom level, layergroup, maptype, 야간 모드, 실시간 위치 추적 이벤트 등 navigation에 필요한 초기 설정들을 구현하였음.


## 2. Navigation Service(9/24~)
- 사용자의 Map Click Event 발생 시 Marker & InfoWindow 객체가 생성되어 해당 Info를 선택하면, 해당 Marker의 LatLng 객체를 Param으로 
navigation service가 시작됨

- AsyncTask abstract class를 가지고 비동기식으로 동작하도록 구현한다. 
 
- Background에서 Naver Direction API를 제공하는 URL에 접근하여 Retrofit을 통해 HTTP 
