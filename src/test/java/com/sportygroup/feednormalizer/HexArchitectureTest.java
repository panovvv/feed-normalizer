package com.sportygroup.feednormalizer;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "com.sportygroup.feednormalizer")
public class HexArchitectureTest {

  private static final String DOMAIN = "com.sportygroup.feednormalizer.domain..";
  private static final String APPLICATION = "com.sportygroup.feednormalizer.application..";
  private static final String INBOUND = "com.sportygroup.feednormalizer.adapters.inbound..";
  private static final String OUTBOUND = "com.sportygroup.feednormalizer.adapters.outbound..";

  private static final String SPRING = "org.springframework..";
  private static final String JACKSON = "com.fasterxml.jackson..";

  // -------------------------
  // Domain layer rules
  // -------------------------
  @ArchTest
  static final ArchRule domain_must_not_depend_on_frameworks =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage(DOMAIN)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(SPRING, JACKSON)
          .because("Domain must be framework-agnostic and must not depend on Spring or Jackson");

  @ArchTest
  static final ArchRule domain_must_not_depend_on_application =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage(DOMAIN)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(APPLICATION)
          .because("Domain must not depend on application layer");

  @ArchTest
  static final ArchRule domain_must_not_depend_on_inbound_adapters =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage(DOMAIN)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(INBOUND)
          .because("Domain must not depend on inbound adapters");

  @ArchTest
  static final ArchRule domain_must_not_depend_on_outbound_adapters =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage(DOMAIN)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(OUTBOUND)
          .because("Domain must not depend on outbound adapters");

  // -------------------------
  // Application layer rules
  // -------------------------
  @ArchTest
  static final ArchRule application_must_not_depend_on_inbound_adapters =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage(APPLICATION)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(INBOUND)
          .because("Application layer must not depend on inbound adapters");

  @ArchTest
  static final ArchRule application_must_not_depend_on_outbound_adapters =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage(APPLICATION)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(OUTBOUND)
          .because("Application layer must not depend on outbound adapters");

  // -------------------------
  // Inbound adapters rules
  // -------------------------
  @ArchTest
  static final ArchRule inbound_adapters_must_not_depend_on_outbound_adapters =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage(INBOUND)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(OUTBOUND)
          .because("Inbound adapters must not call outbound adapters directly");

  // -------------------------
  // Outbound adapters rules
  // -------------------------
  @ArchTest
  static final ArchRule outbound_adapters_must_not_depend_on_inbound_adapters =
      ArchRuleDefinition.noClasses()
          .that()
          .resideInAPackage(OUTBOUND)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(INBOUND)
          .because("Outbound adapters must not depend on inbound adapters");
}
