package me.seo.demo;

import me.seo.demo.domain.Member;
import me.seo.demo.domain.Study;
import me.seo.demo.member.MemberService;
import me.seo.demo.study.StudyRepository;
import me.seo.demo.study.StudyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

/*
Mockito Bdd 스타일 api
bdd : 애플리케이션이 어떻게 "행동" 해야하는 지에 대한 공통적 이해를 진행
행동에 스펙 핵심
    title
    narrative
        as a / i want / so that
    acceptance criteria

        given / when / then
 */
@DisplayName("ShouldStudy")
@ExtendWith(MockitoExtension.class)
public class BDDStudy {
    @Mock
    MemberService memberService;
    @Mock
    StudyRepository repository;
    @Test
    void createStudy(){
        // Given .. 주어졌을 때
        StudyService studyService = new StudyService(memberService,repository);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("test");
        Study study = new Study(10,"test1");
        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        when(repository.save(study)).thenReturn(study);
        // when --> given
        given(memberService.findById(1L)).willReturn(Optional.of(member));
        given(repository.save(study)).willReturn(study);
        //when .. 하면

        studyService.createNewStudy(1L,study);
        //then .. 될것이다

        verify(memberService,times(1)).notify(study);
        // verify --> then
        then(memberService).should(times(1)).notify(study);

    }
}
