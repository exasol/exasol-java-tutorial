package com.exasol.javatutorial.projectmanagement;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ProjectTest {
    @ParameterizedTest
    @ValueSource(strings = {"ACME", "BigSuccess"})
    void shouldTellItsName (final String expectedName) {
        final Project project = new Project(expectedName);
        assertThat(project.getName(), equalTo(expectedName));
    }
}