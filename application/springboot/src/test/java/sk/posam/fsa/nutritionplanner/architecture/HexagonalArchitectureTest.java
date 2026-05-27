package sk.posam.fsa.nutritionplanner.architecture;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;
import java.util.stream.Collectors;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class HexagonalArchitectureTest {

    private static final JavaClasses CLASSES = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("sk.posam.fsa.nutritionplanner");

    @Test
    void domain_must_not_depend_on_frameworks_or_adapters() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework..",
                        "jakarta.persistence..",
                        "org.hibernate..",
                        "sk.posam.fsa.nutritionplanner.controller..",
                        "sk.posam.fsa.nutritionplanner.mapper..",
                        "sk.posam.fsa.nutritionplanner.security..",
                        "sk.posam.fsa.nutritionplanner.jpa..")
                .because("domain must remain technology-agnostic and must not depend on adapters or runtime layer");

        rule.check(CLASSES);
    }

    @Test
    void inbound_layer_must_not_depend_on_outbound_adapter_or_runtime() {
        ArchRule rule = noClasses()
                .that().resideInAnyPackage(
                        "sk.posam.fsa.nutritionplanner.controller..",
                        "sk.posam.fsa.nutritionplanner.mapper..",
                        "sk.posam.fsa.nutritionplanner.security..")
                .should().dependOnClassesThat()
                .resideInAPackage("sk.posam.fsa.nutritionplanner.jpa..")
                .because("REST input should delegate to domain, not reach into JPA adapter");

        rule.check(CLASSES);
    }

    @Test
    void outbound_layer_must_not_depend_on_inbound_layer() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("sk.posam.fsa.nutritionplanner.jpa..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "sk.posam.fsa.nutritionplanner.controller..",
                        "sk.posam.fsa.nutritionplanner.mapper..",
                        "sk.posam.fsa.nutritionplanner.security..")
                .because("JPA adapter must not depend on inbound layer");

        rule.check(CLASSES);
    }

    @Test
    void controllers_must_be_explicit_rest_entry_points() {
        ArchRule rule = classes()
                .that().resideInAPackage("sk.posam.fsa.nutritionplanner.controller..")
                .and().areTopLevelClasses()
                .should().beAnnotatedWith(RestController.class)
                .orShould().beAnnotatedWith(RestControllerAdvice.class)
                .because("controller package should contain only REST entry points and centralized error handling");

        rule.check(CLASSES);
    }

    @Test
    void domain_repository_ports_must_be_interfaces() {
        ArchRule rule = classes()
                .that().resideInAPackage("sk.posam.fsa.nutritionplanner.domain..")
                .and().haveSimpleNameEndingWith("Repository")
                .should().beInterfaces()
                .because("domain repositories are ports and must be interfaces");

        rule.check(CLASSES);
    }

    @Test
    void jpa_repository_adapters_must_be_annotated_with_repository_and_implement_domain_port() {
        ArchRule rule = classes()
                .that().resideInAPackage("sk.posam.fsa.nutritionplanner.jpa.adapter..")
                .and().haveSimpleNameEndingWith("RepositoryAdapter")
                .should().beAnnotatedWith(Repository.class)
                .andShould(implementDomainRepositoryPort())
                .because("JPA repository adapters must be Spring @Repository beans implementing a domain port");

        rule.check(CLASSES);
    }

    @Test
    void runtime_root_should_only_contain_application_and_configuration_classes() {
        ArchRule rule = classes()
                .that().resideInAPackage("sk.posam.fsa.nutritionplanner")
                .and().areTopLevelClasses()
                .should().beAnnotatedWith(Configuration.class)
                .orShould().beAnnotatedWith(SpringBootApplication.class)
                .because("runtime root package must contain only bootstrapping and bean configurations");

        rule.check(CLASSES);
    }

    private static ArchCondition<JavaClass> implementDomainRepositoryPort() {
        return new ArchCondition<>("implement a domain repository port") {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                Set<String> repositoryInterfaces = item.getAllRawInterfaces().stream()
                        .map(JavaClass::getFullName)
                        .filter(name -> name.startsWith("sk.posam.fsa.nutritionplanner.domain."))
                        .filter(name -> name.endsWith("Repository"))
                        .collect(Collectors.toSet());

                boolean satisfied = !repositoryInterfaces.isEmpty();
                String message = item.getName() + (satisfied
                        ? " implements domain port " + repositoryInterfaces
                        : " does not implement any domain repository port");
                events.add(new SimpleConditionEvent(item, satisfied, message));
            }
        };
    }
}
