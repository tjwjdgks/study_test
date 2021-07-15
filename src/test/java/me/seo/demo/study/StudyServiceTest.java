package me.seo.demo.study;

import me.seo.demo.domain.Member;
import me.seo.demo.domain.Study;
import me.seo.demo.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

// mockito 애노테이션 사용
// 구현체가 없는 경우 test할때 mock을 주로 사용한다
@ExtendWith(MockitoExtension.class)
class StudyServiceTest {
    //에노테이션으로
    @Mock
    StudyRepository studyRepository;
    @Mock
    MemberService memberService;
    @Test
    void createsStudyService(){
        /*
        memberservice 와 studyrepository가 인터페이스이다. 구현체가 없음
        mock사용하기 좋은 예제
        없는 경우 mocking해야 한다
         */
        /*
        StudyRepository studyRepository = Mockito.mock(StudyRepository.class);
        MemberService memberService = Mockito.mock(MemberService.class);
        모든 객체 Null 값 return
        optional 경우 null;
        원시형일 경우 default값 return
         */
        /*
        모키토 가짜 변수 만들기
         */
        Member member = new Member();
        member.setId(1L);
        member.setEmail("seo");
        // when 조건이 반드기 만족해야  객체 만들어진다
        //when(memberService.findById(1L)).thenReturn(Optional.of(member));
        // 아무거나 호출해도 동일
        when(memberService.findById(any())).thenReturn(Optional.of(member));
        Optional<Member> id = memberService.findById(1L);
        assertEquals("seo",id.get().getEmail());
        StudyService studyService = new StudyService(memberService,studyRepository);
        assertNotNull(studyService);

    }
    //return 값 있을 때 예외
    @Test
    void testf2(){
        when(memberService.findById(1L)).thenThrow(new RuntimeException());
        Optional<Member> byId1 = memberService.findById(2L);
        Optional<Member> byId2  =memberService.findById(1L);
        assertNotNull(byId1);
        assertNotNull(byId2);
    }
    // return 값 없을 때 예외
    @Test
    void testf3(){
        doThrow(new IllegalArgumentException()).when(memberService).validate(1L);
        memberService.validate(1L);
    }
    // 메소드가 여러번 호출될때 순서에 따라
    @Test
    void testf4(){
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test");
        when(memberService.findById(any()))
                .thenReturn(Optional.of(member))
                .thenThrow(new RuntimeException())
                .thenReturn(Optional.empty());
        Optional<Member> byId = memberService.findById(1L);
        assertAll(
                ()->assertEquals("test",byId.get().getEmail()),
                ()->assertThrows(RuntimeException.class, ()->{
                    memberService.findById(2L); }),
                ()-> assertEquals(Optional.empty(),memberService.findById(3L))
        );
    }
    // mock 객체 확인
    @Test
    void testf5(){
        StudyService studyService = new StudyService(memberService,studyRepository);
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test");
        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        Study study = new Study(10,"test");
        when(studyRepository.save(study)).thenReturn(study);
        studyService.createNewStudy(1L,study);
        verify(memberService, times(1)).notify(study);
        // notify study 이후에 동작 x
        //verifyNoMoreInteractions(memberService);
        verify(memberService, times(1)).notify(member);
        verify(memberService,never()).validate(any());

        // 순서 test
        InOrder inOrder = inOrder(memberService);
        inOrder.verify(memberService).notify(study);

        inOrder.verify(memberService).notify(member);
    }
}