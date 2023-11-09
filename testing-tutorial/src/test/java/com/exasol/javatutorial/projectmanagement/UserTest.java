package com.exasol.javatutorial.projectmanagement;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class UserTest {
    @ParameterizedTest
    @ValueSource(strings = {"Alice", "Bob"})
    void shouldTellItsName(final String expectedName)
    {
        final User user = new User(expectedName);
        assertThat(user.getName(), equalTo(expectedName));
    }
}
