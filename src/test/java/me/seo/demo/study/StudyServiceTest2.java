package me.seo.demo.study;

import me.seo.demo.domain.Member;
import me.seo.demo.domain.Study;
import me.seo.demo.member.MemberService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers
public class StudyServiceTest2 {
    @Mock
    MemberService memberService;
    @Autowired
    StudyRepository studyRepository;
    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1").withDatabaseName("studytest");
    /*

     // user가 life cycle 관리
    @BeforeAll
    static void beforeAll(){
        postgreSQLContainer.start();
    }
    @AfterAll
    static void afterAll(){
        postgreSQLContainer.stop();
    }

     */
    // 만약 각각의 container를 띄워야 한다 즉 초기화를 하려면 container 인스턴스로 만드는 것이아니라 static으로 하고 정보를 지운다
    /*
    // example
    @BeforeEach
    void beforeEach(){
        studyRepository.deleteAll();
    }

     */
    @Test
    void createNewStudy(){
        System.out.println(postgreSQLContainer.getDockerImageName());
        System.out.println("========");

        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("keesun@email.com");

        Study study = new Study(10, "테스트");

        given(memberService.findById(1L)).willReturn(Optional.of(member));

        // When
        studyService.createNewStudy(1L, study);

        // Then
        assertEquals(1L, study.getOwnerId());
        then(memberService).should(times(1)).notify(study);

    }
}
