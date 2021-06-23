package me.seo.demo;


import org.junit.jupiter.api.*;

// test마다 인스턴스를 생성한다
// test마다 다른 인스턴스
// test간의 의존성을 없애기 위해
// 기본적으로는 선언한대로 test되지만 안될때도 있음
// test 인스턴스를 하나만 만드는 방법
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// 시나리오 테스트 등에 사용( 회원가입-> 로그인 -> 정보조회등의 플로우) 상태정보를 가지고 있을 때 많이 사용
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudyTest2 {
    int value = 0;

    @DisplayName("테스트")
    @Test
    // 낮은 값일 수록 높은 우선순위
    @Order(2)
    void test1(){
        System.out.println(value++);
    }
    @DisplayName("테스트2")
    @Test
    @Order(1)
    void test2(){
        System.out.println(value++);
    }
    @BeforeAll
    void before(){
        System.out.println("before");
    }
    @AfterAll
    void after(){
        System.out.println("after");
    }
}
