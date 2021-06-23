package me.seo.demo;

// 5-> 4 마이그레이션
import org.junit.Before;
import org.junit.Test;
public class StudyJunit4Test {

    @Before
    public void before(){
        System.out.println("before");
    }
    @Test
    public void CreateTest(){
        System.out.println("test");
    }
}
