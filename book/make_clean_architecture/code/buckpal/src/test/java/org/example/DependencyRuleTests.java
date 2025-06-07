package org.example;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import org.junit.jupiter.api.Test;

public class DependencyRuleTests {

	@Test
	void domainLayerDoesNotDependOnApplicationLayer() {
		noClasses()
			.that()
			.resideInAnyPackage("buckpal.domain..")
	}
}
