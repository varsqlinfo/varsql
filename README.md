# varsql
varsql websqltool sqltool 

# site
<https://varsql.com/>

<p>
<img src="https://user-images.githubusercontent.com/46696460/210193661-3fee9240-a83e-4c70-81ba-5acbeabf6a9c.gif"/>
</p>


# 개발환경
- java : 1.8
- eclipse : Version: 2020-03 (4.15.0)
- gradle : 7.3.3
- spring boot : 2.6.6
- lombok : 1.18.10



# vscode 
```
vscode extensions 에서 아래 기능 추가

Spring Boot Tools
Spring Boot Dashboard
Spring Boot Extension Pack

varsql 폴더로 이동후 아래 커맨드 입력후 엔터  
gradle clean build 
```


# 도커 
```
- 도커 내려받기
docker pull varsql/app:latest

- 이미지 실행. 
docker run -d --privileged --name=varsql-app -p 12312:8080 varsql/app

- 웹 뷰
http://localhost:12312/vsql
id : varsqladmin
pw : varsqladmin
```

# Varsql 실행 및 문제 해결
- [wiki](https://github.com/varsqlinfo/varsql/wiki/)

- [설치방법](https://github.com/varsqlinfo/varsql/wiki/Varsql-%EC%8B%A4%ED%96%89)

- [기능](https://github.com/varsqlinfo/varsql/wiki/%EA%B8%B0%EB%8A%A5)

- [문제 해결 방법](https://github.com/varsqlinfo/varsql/wiki/%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0-%EB%B0%A9%EB%B2%95)


