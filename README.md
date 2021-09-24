# 밥줬어?

## 참여자
|K007|K026|K043|K056|
|---|---|---|---|
|김수명|박태훈|이승수|차지원|


## 한줄 소개
가족 구성원들 간 애완동물 밥 제공 여부와 시간을 공유해주는 서비스

## 기능

- 애완동물별 밥 준 기록 저장 + 조회 기능
- 밥 준 기록 바탕으로 순위 기능
- 가족 프로필 이미지로 계정 구분
<!-- - 밥 먹는 주기가 일정하지 않은 부분 고려 -->
<!-- - 밥 먹는 주기가 긴 부분 고려 -->
<!-- - 밥 떨어지는 시기 예측 (간단한 통계) -->

## 동작방식
1. 전화번호로 로그인 (한 가족이 한 계정으로)
2. 로그인 후 가족 구성원 등록
3. 애완동물 등록(n마리)
4. 밥주기 버튼을 누르면 밥 먹은 정보를 확인
5. 밥을 주면 준 사람과 시간을 기록
6. 프로필 선택 화면에서 가족 구성원 별 순위 확인 가능

<!-- 1. 밥 주기전에 가족 구성원들에게 푸시알림 전송 -->
<!-- 1. 가족 구성원 프로필 선택 후 애완동물 밥 주기 + 구성원에게 푸시알림 전송 -->


[프로토타입]

## 설치

### build.gradle(:app)

```gradle
android {
    viewBinding {
        enabled = true
    }
    ...
}
```
```gradle
plugins {
    id 'com.google.gms.google-services'
    id 'androidx.navigation.safeargs.kotlin'
    ...
}
```

```gradle
// navigation
def nav_version = "2.3.5"
implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

// glide
implementation 'com.github.bumptech.glide:glide:4.11.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'

// firebase
implementation platform('com.google.firebase:firebase-bom:28.4.1')
implementation 'com.google.firebase:firebase-auth-ktx'
implementation 'com.google.firebase:firebase-firestore-ktx'
implementation 'com.google.firebase:firebase-storage-ktx'
implementation 'com.google.android.gms:play-services-basement:17.6.0'

// chart animation
implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
```

### build.gradle(:module)

```gradle
allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```
```gradle
buildscript {
    ...
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
    
    dependencies {
        classpath "com.android.tools.build:gradle:7.0.2"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath 'com.google.gms:google-services:4.3.10'
        classpath "androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5"
    }
}
```

## 시연



![로그인시연]|![시연영상1] | ![시연영상2]
|--|--|--|

## 스크린샷
![init] |![login]|![profile]
|--|--|--|
![rank] |![main] |![add_animal]
![feed_pet]


<!-- 이미지 변수 -->

[프로토타입]: https://www.figma.com/proto/oD38cW4imoHvZeNJE9Bf6H/%EB%B0%A5%EC%A4%AC%EC%96%B4?node-id=0%3A1&scaling=min-zoom&starting-point-node-id=1%3A4

[로그인시연]: https://user-images.githubusercontent.com/55696672/134622937-bbc77246-bf78-4ce4-8ea7-e1175b9ea0bf.mp4
[시연영상1]: https://user-images.githubusercontent.com/55696672/134623083-4845ab79-56dd-4bd5-9aa3-01915348a37b.mp4
[시연영상2]: https://user-images.githubusercontent.com/55696672/134623145-0bde37f8-71c9-4dbb-a83e-21917e227f8a.mp4

[init]: https://user-images.githubusercontent.com/55696672/134577110-39fb66a7-5f56-4104-9b0e-bf1f5b6c9751.png
[login]: https://user-images.githubusercontent.com/55696672/134577102-3ca3d497-186b-4e5e-8b0e-8a6b78b5755f.png
[rank]: https://user-images.githubusercontent.com/55696672/134577114-45bfc009-a269-4af5-bfda-74459c9c9ac1.png
[profile]: https://user-images.githubusercontent.com/55696672/134577117-4c59b500-7556-4bf7-a805-1eddb1a37b19.png
[main]: https://user-images.githubusercontent.com/55696672/134578802-d21dbad4-8dfc-45f3-b8f7-401213cd1a26.png
[add_animal]: https://user-images.githubusercontent.com/55696672/134578819-0637f19d-72dc-48c1-95e3-2f134939ec80.png
[feed_pet]: https://user-images.githubusercontent.com/55696672/134619154-1f212018-c0c2-4c6c-85f0-ad21f73089d8.png
