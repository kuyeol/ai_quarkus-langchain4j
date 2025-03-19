# 설계

1. 모델 호출 응답 구현 (웹소켓 엔드포인트)
   - 호출 서비스 개발 (역할,조건 등의 프롬프트 제약 정의)
     - 인터페이스 정의, 메서드 : 호출 반환 타입 ( 세션 + 메세지 인수 )
   - 대화 상태 기억(맥락 유지)
     - 앱스코프, ChatMemoryProvider 구현, 메서드 get 재정의 
   - 엔드포인트 구현
     - 서비스인터페이스, 상태빈 주입
     - @OnMessage void 메서드 (문자열, 세션) 응답 객체 try블록 세션 전달 

2. RAG 모델 확장
   - 문서 벡터화 수집 데이터베이스 사용
     - 문서 임배딩 클래스 생성
       - 레디스임베딩스토어, 랭체인패키지 임베딩모델 주입
     - void 메서드 (@Observes StartupEvent) 생성
       - 로컬 파일을 읽어 파싱
     - RAG 패턴 생성
       - 문서 검색 첨부 메서드 구현





# Docker Image



```yml

version: '3.8'
services:
  cache:
    image: redis:latest
    restart: always
    ports:
      - '6379:6379'
    command: redis-server --save 20 1 --loglevel warning --requirepass qwerty
    volumes: 
      - cache:/data
volumes:
  cache:
    driver: local

```

# Quarkus LangChain4j

[![Version](https://img.shields.io/maven-central/v/io.quarkiverse.langchain4j/quarkus-langchain4j-parent?logo=apache-maven&style=flat-square)](https://search.maven.org/artifact/io.quarkiverse.langchain4j/quarkus-langchain4j)

This repository contains Quarkus extensions that facilitate seamless integration between Quarkus and [LangChain4j](https://github.com/langchain4j/langchain4j), enabling easy incorporation of Large Language Models (LLMs) into your Quarkus applications.

## Features

Here is a non-exhaustive list of features that are currently supported:

- Declarative AI services
- Integration with diverse LLMs (OpenAI GPTs, Hugging Faces, Ollama...)
- Tool support
- Embedding support
- Document store integration (Redis, Chroma, Infinispan...)
- Native compilation support
- Integration with Quarkus observability stack (metrics, tracing...)
- Pluggable auth providers

## Documentation

Refer to the comprehensive [documentation](https://docs.quarkiverse.io/quarkus-langchain4j/dev/index.html) for detailed information and usage guidelines.

## Samples

Check out the [samples](https://github.com/quarkiverse/quarkus-langchain4j/tree/main/samples) and [integration tests](https://github.com/quarkiverse/quarkus-langchain4j/tree/main/integration-tests) to gain practical insights on how to use these extensions effectively.

## Getting Started

To incorporate Quarkus LangChain4j into your Quarkus project, add the following Maven dependency:

```xml
<dependency>
    <groupId>io.quarkiverse.langchain4j</groupId>
    <artifactId>quarkus-langchain4j-openai</artifactId>
    <version>0.26.0.CR2</version>
</dependency>
```

or, to use Ollama:

```xml
<dependency>
    <groupId>io.quarkiverse.langchain4j</groupId>
    <artifactId>quarkus-langchain4j-ollama</artifactId>
    <version>0.26.0.CR2</version>
</dependency>
```

## Contributing

Feel free to contribute to this project by submitting issues or pull requests.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.


