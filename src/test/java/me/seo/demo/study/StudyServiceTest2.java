package me.seo.demo.study;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
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
//logger 애노테이션
@Slf4j
@ContextConfiguration(initializers = StudyServiceTest2.ContainerPropertyInitializer.class)
public class StudyServiceTest2 {
    @Mock
    MemberService memberService;
    @Autowired
    StudyRepository studyRepository;
    @Autowired
    Environment environment;
    @Value("${container.port}") int port;
    /*
    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1").withDatabaseName("studytest");
    */
    // Container 기본 만들기

    @Container
    static GenericContainer postgreSQLContainer = new GenericContainer("postgres")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_DB","studytest")
            .withEnv("POSTGRES_PASSWORD","password");

    // docker compose 파일
     // user가 life cycle 관리

    /*
    @BeforeAll
    static void beforeAll(){
        postgreSQLContainer.start();
    }
    @AfterAll
    static void afterAll(){
        postgreSQLContainer.stop();
    }

     */


    @BeforeAll
    static void beforeAll(){
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);
        postgreSQLContainer.followOutput(logConsumer);
    }
    // 만약 각각의 container를 띄워야 한다 즉 초기화를 하려면 container 인스턴스로 만드는 것이아니라 static으로 하고 정보를 지운다

    // example
    @BeforeEach
    void beforeEach(){
        System.out.println(postgreSQLContainer.getMappedPort(5432));
        //로그 출력
        //System.out.println(postgreSQLContainer.getLogs());
        System.out.println(environment.getProperty("container.port"));
        System.out.println(port);
        studyRepository.deleteAll();

    }


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
    // spring boot test에 적용하기
    static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("container.port="+postgreSQLContainer.getMappedPort(5432))
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
