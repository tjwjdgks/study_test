package me.seo.demo.member;

import me.seo.demo.domain.Member;
import me.seo.demo.domain.Study;

import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(Long memberId);

    void validate(Long memberId);

    void notify(Study newstudy);

    void notify(Member member);
}