package me.seo.demo;

// junit 확장 방법 단순해졌다
// Extension 확장 모델 하나이다


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

// Extendion 선언적 방법
// 쉽지만 커스터 마이징을 할 수가 없기 때문에 인스턴스로 생성하는 방법
//@ExtendWith(FindSlowTestExtension.class) // 선언방법
public class StudyTest3 {


    // 프로그래밍으로 인스턴스 생성 방법
    @RegisterExtension
    static FindSlowTestExtension findSlowTestExtension = new FindSlowTestExtension();

    @Test
    @DisplayName("테스트 입니다")
    void fun1(){
        System.out.println("1초안");
    }
    @Test
    @DisplayName("테스트 입니다2")
    void fun2() throws InterruptedException {
        Thread.sleep(1005L);
        System.out.println("1초밖");
    }
    @Test
    @SlowTest
    @DisplayName("테스트 입니다3")
    void fun3() throws InterruptedException {
        Thread.sleep(1005L);
        System.out.println("1초밖");
    }
}

