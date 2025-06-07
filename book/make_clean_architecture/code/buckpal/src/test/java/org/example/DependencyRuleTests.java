package org.example;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

public class DependencyRuleTests {

	@Test
	void domainLayerDoesNotDependOnApplicationLayer() {
		noClasses()
				.that()
				.resideInAnyPackage("org.example.domain")
				.should()
				.dependOnClassesThat()
				.resideInAnyPackage("org.example.application")
				.check(new ClassFileImporter()
						.importPackages("org.example"));
	}
}
