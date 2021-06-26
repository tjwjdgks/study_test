package me.seo.demo.cases;

import me.seo.demo.domain.Study;
import me.seo.demo.domain.StudyStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class StudyTest {
    @Test
    @DisplayName("스터디 만들기") // test이름 바꾸기
    void create(){
        Study study = new Study(10);

        assertAll(
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
                ()->assertNotNull(study),
                ()->assertEquals(StudyStatus.DRAFT,study.getStatus(),()->"test입니다"),
                () ->assertTrue(study.getLimitCount()>0,()->"스터디 인원 10")
        );

    }
    @Test
    @Disabled // test 실행하지 않음
    void create1(){
        // 예외 확인하기
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Study(-10));
        String message = exception.getMessage();
        assertEquals("limit은 0보다 큼",message);
    }
    @Test
    @DisplayName("시간 test")
    void create2(){
        // 시간으로 테스트
        //assertTimeout(Duration.ofSeconds(10),()-> new Study(10));
        /*
        assertTimeout(Duration.ofMillis(100),()->{
            new Study(10);
            Thread.sleep(300);
        });
         */
        // TODO ThreadLocal 사용시 문제 발생할 수 있음
        // 스레드 로컬에서 문제 발생할 수도 있음 test 바로 종료 하는 코드
        // db tranaction 반영 안될 수 도 있음
        assertTimeoutPreemptively(Duration.ofMillis(100),()->{
            new Study(10);
           // Thread.sleep(300);
        });
    }
    @Test
    @DisplayName("다른 라이브러리 방법")
    void create3(){
        Study actual = new Study(10);
        assertThat(actual.getLimitCount()).isGreaterThan(0);
    }
    @BeforeAll
    static void beforeAll(){
        System.out.println("before all");
    }
    @AfterAll
    static void afterAll(){
        System.out.println("after all");
    }
    // 테스트마다 이전 호출
    // test 끝나고 한번 호출
    // test 적용시 합번 호출
    // private 안됨 ,default 가능, return type 없어야됨
    // TODO 조건에 따라 테스트 실행하기
    /*
    자바 버전, 환경 변수에 따라 실행하는지 실행하지 말아야 하는지
    @Test
    @DisplayName("test환경")
    void create4(){
        String s = System.getenv("TEST_ENV");
        assertNotNull(s,()->"null 입니다");
        assumeTrue("LOCAL".equalsIgnoreCase(System.getenv("TEST_ENV")));
    }
     */
    // OS 에 따라
    @Test
    @DisplayName("test환경")
    @EnabledOnOs(OS.WINDOWS)
    void create5(){
        System.out.println("윈도우");
    }
    // OS 에 따라
    @Test
    @DisplayName("test환경")
    @EnabledOnOs({OS.LINUX,OS.MAC})
    void create6(){
        System.out.println("리눅스 기반");
    }

    // JRE 버전에 따라
    @Test
    @DisplayName("JRE 테스트")
    @DisabledOnJre(JRE.JAVA_8)
    void create7(){
        System.out.println("JRE 8 아님");
    }

    @Test
    @DisplayName("JRE 테스트")
    @EnabledOnJre({JRE.JAVA_9,JRE.JAVA_10,JRE.JAVA_11})
    void create8(){
        System.out.println("JRE 8 보다 이상");
    }

    @Test
    @DisplayName("스터디 만들기 fast")
    @Tag("fast")
    void create9(){
        System.out.println("fast");
    }
    // 인텔리 제이 tag로 실행할 수 있음
    @Test
    @Tag("slow")
    @DisplayName("스터디 만들기 slow")
    void create10() {
        System.out.println("slow");
    }
    /*
    junit5에서 제공하는 애노테이션 meta 에노테이션 사용가능
    커스텀 애노테이션 위에 이러한 에노테이션을 사용하면 동일한 동작한다

     */
    //커스텀 애노테이션
    @FastTest
    void create11(){
        System.out.println("fasttest");
    }
    @BeforeEach
    void beforeEach(){
        System.out.println("after each");
    }
    // 테스트 여러번 반복하기 (단순 반복)
    @DisplayName("스터디 반복")
    @RepeatedTest(value= 10 ,name = "{displayName}, {currentRepetition}/{totalRepetitions}")
    void createRepeat(RepetitionInfo repetitionInfo){
        System.out.println("repeat" + repetitionInfo.getCurrentRepetition()
                +"/" + repetitionInfo.getTotalRepetitions());
    }
    // 랜덤 반복
    @ParameterizedTest
    @ValueSource(strings ={"날씨가", "많이","추워요"})
    void parameterTest(String s){
        System.out.println(s);
    }
    // 랜덤 & 이름 정의하기
    @DisplayName("랜덤 & 이름")
    // 첫번째 파라미터
    @ParameterizedTest(name="{index} {displayName} message={0}")
    @ValueSource(strings ={"날씨가", "많이","추워요"})
    @NullAndEmptySource
    void parameterTest2(String s){
        System.out.println(s);
    }
    // converter 사용
    @DisplayName("랜덤 & 이름 2")
    @ParameterizedTest(name = "{displayName} : {index} - s={0}")
    @ValueSource(ints = {10,20,30})
    void parameterTest3(@ConvertWith(StudyConverter.class) Study s){
        System.out.println(s.getLimitCount());
    }
    static class StudyConverter extends SimpleArgumentConverter{

        @Override
        protected Object convert(Object o, Class<?> aClass) throws ArgumentConversionException {
            assertEquals(Study.class,aClass, ()->"NOT MATCH");
            return new Study(Integer.parseInt(o.toString()));
        }
    }

    @DisplayName("랜덤 & 이름 2")
    @ParameterizedTest(name = "{displayName} : {index} - s={0} {1}")
    @CsvSource({"10, '자바 공백일때 작은 따옴표 '","20, 스프링"})
    void parameterTest4(Integer limit, String name){
        System.out.println(new Study(limit,name));
    }
    @DisplayName("랜덤 & 이름 3")
    @ParameterizedTest(name = "{displayName} : {index} - s={0} {1}")
    @CsvSource({"10, '자바 공백일때 작은 따옴표 '","20, 스프링"})
    void parameterTest5(ArgumentsAccessor argumentsAccessor){
        System.out.println(new Study(argumentsAccessor.getInteger(0),argumentsAccessor.getString(1)));
    }
    @DisplayName("랜덤 & 이름 4 커스텀")
    @ParameterizedTest(name = "{displayName} : {index} - s={0} {1}")
    @CsvSource({"10, '자바 공백일때 작은 따옴표 '","20, 스프링"})
    void parameterTest6(@AggregateWith(StudyAggregateor.class)Study study){
        System.out.println(study);
    }
    //static inner class 거나 public class 여야한다
    static class StudyAggregateor implements ArgumentsAggregator{


        @Override
        public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
            return new Study(argumentsAccessor.getInteger(0),argumentsAccessor.getString(1));
        }
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