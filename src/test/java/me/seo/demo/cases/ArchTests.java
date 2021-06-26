package me.seo.demo.cases;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import me.seo.demo.App;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

//archtest junit5 사용
@AnalyzeClasses(packagesOf = App.class)
public class ArchTests {

    @ArchTest
    ArchRule domainPackageRuleJ = classes().that().resideInAnyPackage("..domain..")
            .should().onlyBeAccessed().byClassesThat()
            .resideInAnyPackage("..study..","..memeber..","..domain..","..cases..");

    @Test
    void packageDependencyTest(){
        JavaClasses classes = new ClassFileImporter().importPackages("me.seo.demo");
        /*
           TODO ..domain.. 패키지에 있는 클래스는 ..study.., ..member.., ..domain에서 참조 가능. test case는 별도
           TODO..member.. 패키지에 있는 클래스는 ..study..와 ..member..에서만 참조 가능. test case 별도 (1)
                 (반대로) ..domain.. 패키지는 ..member.. 패키지를 참조하지 못한다. test case 별도 (2)
                 (1)과 (2) 같은 것
           TODO ..study.. 패키지에 있는 클래스는 ..study.. 에서만 참조 가능.
           TODO 순환 참조 없어야 한다.
         */
        ArchRule domainPackageRule = classes().that().resideInAnyPackage("..domain..")
                .should().onlyBeAccessed().byClassesThat()
                .resideInAnyPackage("..study..","..memeber..","..domain..","..cases..");
        domainPackageRule.check(classes);

        ArchRule memberPackageRule = noClasses().that().resideInAnyPackage("..domain..")
                .should().accessClassesThat().resideInAnyPackage("..member..");
        memberPackageRule.check(classes);
        /*
        ArchRule studyPackageRule = classes().that().resideInAnyPackage("..study..")
                .should().onlyBeAccessed().byClassesThat()
                .resideInAnyPackage("..study..","..cases..");
        studyPackageRule.check(classes);

         */
        //study 예시
        ArchRule studyPackageRule = noClasses().that().resideOutsideOfPackages("..study..","..cases..")
                .should().accessClassesThat().resideInAPackage("..study..");
        studyPackageRule.check(classes);

        ArchRule freeOfCycles = slices().matching("..me.seo.demo.(*)..")
                .should().beFreeOfCycles();
        freeOfCycles.check(classes);

    }
}
