package me.seo.demo;

import org.junit.jupiter.api.*;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class StudyTest {
    @Test
    @DisplayName("스터디 만들기") // test이름 바꾸기
    void create(){
        Study study = new Study();
        assertNotNull(study);
        // 기대 나온 값 순서로
        //assertEquals(StudyStatus.DRAFT, study.getStatus(),"스터니 처음 만들시 DRAFT");
        /*
        assertEquals(StudyStatus.DRAFT, study.getStatus(), new Supplier<String>() {
            @Override
            public String get() {
                return "test 입니다";
            }
        });
         */
        // 람다로 작성시 test 실패에만 한다 문자열 연산이 줄어든다
        assertEquals(StudyStatus.DRAFT,study.getStatus(),()->"test입니다");
        System.out.println("create");

    }
    @Test
    @Disabled // test 실행하지 않음
    void create1(){
        System.out.println("create");
    }
    // test 적용시 합번 호출
    // private 안됨 ,default 가능, return type 없어야됨
    @BeforeAll
    static void beforeAll(){
        System.out.println("before all");
    }
    // test 끝나고 한번 호출
    @AfterAll
    static void afterAll(){
        System.out.println("after all");
    }
    // 테스트마다 이전 호출
    @BeforeEach
    void beforeEach(){
        System.out.println("after each");
    }
    // 테스트마다 이후 호출
    @AfterEach
    void afterEach(){
        System.out.println("after each");
    }


    /*
    junit4 vs junit5
    test -> test
    beforeclass -> beforeall
    afterclass -> afterall
    before -> beforeeach
    after -> aftereach
    ignore -> disabled
     */
}