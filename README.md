# varsql
varsql websqltool sqltool 

# site
<https://varsql.com/>

<p>
<img src="https://user-images.githubusercontent.com/46696460/210193661-3fee9240-a83e-4c70-81ba-5acbeabf6a9c.gif"/>
</p>


# 개발환경
- java : 17 이상
- gradle : 7.3.3
- spring boot : 2.6.6
- lombok : 1.18.30


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
http://localhost:12312/varsql
```

## Star History

<a href="https://star-history.com/#varsqlinfo/varsql&Date">
  <picture>
    <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=varsqlinfo/varsql&type=Date" />
    <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=varsqlinfo/varsql&type=Date" />
  </picture>
</a>