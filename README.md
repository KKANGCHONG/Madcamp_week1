# madcamp_week1
몰입캠프 1주차 과제입니다.

# 타임노트 4.5
![아이콘](https://github.com/KKANGCHONG/madcamp_week1/blob/main/images/KakaoTalk_Photo_2025-01-01-17-10-54.png)
## 사진과 텍스트, 위치 업로드 기능을 제공하며 일정 시간 뒤에 이를 볼 수 있는 타임 캡슐 어플

---

## 소개
타임노트 4.5는 몰입캠프 4.5주 동안의 추억을 기록하고, 캠프 종료 후 다시 떠올릴 수 있는 타임캡슐 어플입니다. 특별한 날의 사진, 텍스트, 위치를 저장하고 시간이 지난 후 열어보며 소중한 순간들을 되돌아볼 수 있습니다.

### 주요 기능
- 이미지, 텍스트, 위치 정보 업로드 및 저장
- 저장된 캡슐을 일정 시간 뒤에 열어 확인 가능
- 실제 타임캡슐처럼 열기 전까지 내용 확인 불가

---

## 팀원 소개

### 이종복
- [GitHub 프로필](https://github.com/ljbassa)
- 카이스트 산업및시스템공학과 19학번
- 두 번째 탭 (캡슐 생성, DB 연결), 각 탭 연결 구현

### 이현정
- [GitHub 프로필](https://github.com/KKANGCHONG)
- 이화여대 컴퓨터공학과 22학번
- 첫 번째 탭 (캡슐 오픈, 정보 확인) & 세 번째 탭 (지도 위치 확인) & DB 구현

---

## 개발 스택 소개
- **개발 언어**: Kotlin
- **IDE**: Android Studio
- **버전 및 이슈 관리**: GitHub
- **협업 툴**: GitHub

---

## 구현 기술 소개

### Tab 1
- **캡슐 리스트**: 리스트 아이템을 터치하여 캡슐 오픈 가능
- **캡슐 오픈**: 이미지를 터치하여 캡슐 오픈, 3초 후 캡슐 정보 확인 가능
  - 제목, 날짜, 텍스트, 이미지, 위치 정보 포함

### Tab 2
- **이미지 업로드**:
  - 갤러리에서 이미지 선택 및 DB 저장
  - 캡슐에 저장된 이미지는 화면에서 비가시화 처리
- **캡슐 저장**:
  - DB에 저장된 이미지 중 선택하여 캡슐 저장
  - 제목, 날짜, 텍스트 입력 및 저장 가능
- **위치 정보 저장**:
  - 위치 정보도 캡슐에 저장 가능

### Tab 3
- **위치 확인**:
  - Naver API를 이용하여 현재 위치를 지도에 표시

---

## 보완할 점 & 발전 가능성

1. **이미지 저장**: 안드로이드 버전에 따라 이미지 경로를 가져올 수 없는 경우 사진을 그대로 저장했으며, 이로 인해 큰 용량의 사진 저장이 불가. 큰 용량의 사진도 저장할 수 있도록 보완 필요.
2. **캡슐 잠금 기능**: 일정 시간 이후에 열 수 있도록 하는 잠금 장치를 구현하지 못함. 이를 개선할 가능성 있음.

---

## 실행 화면 및 시연 영상

### 화면
1. 탭 1: 생성한 캡슐 리스트 표시
2. 탭 2: 이미지 리스트 확인
3. 탭 3: 지도 API를 이용한 현재 위치 확인

### 영상
1. 탭 1: 캡슐 열기 및 정보 확인 영상
2. 탭 2: 이미지 선택 후 캡슐 업로드 영상
3. 탭 3: 지도 API 확인 영상

---
