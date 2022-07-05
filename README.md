# Project.final

## 코드 컨벤션
### DTO
#### DTO 형식
- Http 응답 메시지 작성 시 Body에 들어갈 Dto는 한개만 사용(ResponseDto\<T>)
- ResponseDto의 제네릭스안에 들어갈 여러 형태들을 클래스로 정의(README에서는 Body형식이라고 표기)
- Dto 패키지 안에는 ResponseDto\<T>와 여러 패키지로 구성
#### Dto패키지 안의 여러 패키지의 구성
    여러 패키지의 명명규칙은 "기능명/기능과 관련된 단어"
- RequestDto의 명명규칙은 "기능명/기능과 관련된 단어+RequestDto"
- Body형식 클래스들의 명명규칙은 "보여지는 페이지+기능명/기능과 관련된 단어+Dto"
- Dto패키지 하위 패키지명은 Dto들의 공통된 내용을 이름으로 생성
#### DTO패키지 구성 예시
- ex) 게시글 작성의 RequestDto > PostRequestDto
- ex) 메인페이지에서 보여지는 게시글 리스트 > MainPostDto
- ex) 카페 페이지에서 보여지는 게시글 리스트 > CafePostDto

- Dto ------ResponseDto\<T>.java  
  &nbsp;&nbsp;&nbsp;|--------Post-----PostReqeustDto.java  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|---------MainPostDto.java  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|---------CafePostDto.java
---
## Git 전략
### 우리 팀은 Git전략으로 GitHub Flow를 사용합니다.(CI/CD에 적합)
#### Branch 구성
- main : 릴리즈에 있어서 절대적인 역할(항상 최신버전 유지)
- hotfix : main branch에서 오류발생 시 오류 가져와서 수정 후 main으로 merge
- feature/ : main에서 뻗어나오는 branch 기능별로 branch생성  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;기능 구현 후main으로 merge  


    feature/ branch는 팀원에게 얘기하고 생성할 것!!!


      