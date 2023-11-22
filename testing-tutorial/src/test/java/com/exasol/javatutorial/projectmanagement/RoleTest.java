package com.exasol.javatutorial.projectmanagement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.exasol.javatutorial.projectmanagement.Permission.READ;
import static com.exasol.javatutorial.projectmanagement.Permission.WRITE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {
    @Test
    void shouldHaveNoPermissionsWhenCreatedWithoutAny() {
        final Role role = new Role();
        assertAll(
                () -> assertFalse(role.hasPermission(READ)),
                () -> assertFalse(role.hasPermission(WRITE))
        );
    }

    @Test
    void shouldHaveReadPermissionWhenCreatedWithRead() {
        final Role role = new Role(READ);
        assertTrue(role.hasPermission(READ));
        assertFalse(role.hasPermission(WRITE));
    }

    // For comparison here is an approach that iterates over all permutations of permissions in a role.
    // Would be more compact if there were 10 permissions. For only two it is overkill.
    @Test
    void shouldReportPermissionsTheRoleWasGiven() {
        final int numberOfPermissions = Permission.values().length;
        final int numberOfPermutations = 1 << numberOfPermissions;
        for(int mutation = 0; mutation < numberOfPermutations; ++mutation)
        {
            final Set<Permission> permissionSet = generatePermutation(mutation, numberOfPermissions);
            final Role role = new Role(permissionSet.toArray(new Permission[]{}));
            for(int permission = 0; permission < numberOfPermissions; ++permission)
            {
                assertThat(role.toString(), role.hasPermission(Permission.values()[permission]),
                        equalTo((mutation & (1 << permission)) != 0));
            }
        }
    }

    private Set<Permission> generatePermutation(int mutation, int numberOfPermissions) {
        final Set<Permission> permissions = new HashSet<>();
        for(int permission = 0; permission < numberOfPermissions; ++permission)
        {
            if((mutation & (1  << permission)) != 0) {
                permissions.add(Permission.values()[permission]);
            }
        }
        return permissions;
    }

    // And another variant using a test matrix. While test matrices are in general a good idea, in this particular case
    // the readability suffers compared to simple one-liner test methods.
    @CsvSource({
            "'', false, false",
            "READ, true, false",
            "WRITE, false, true",
            "READ:WRITE, true, true"
    })
    @ParameterizedTest
    void shouldReportPermissionsTheRoleWasGiven(final String grantedPermissionString, final boolean mayRead,
                                                final boolean mayWrite) {
        final Permission[] grantedPermissions = Arrays.stream(grantedPermissionString
                        .split(":"))
                .filter(str -> !str.isBlank())
                .map(Permission::valueOf)
                .toArray(Permission[]::new);
        final Role role = new Role(grantedPermissions);
        assertThat(role.hasPermission(READ), equalTo(mayRead));
        assertThat(role.hasPermission(WRITE), equalTo(mayWrite));
    }

    @Test
    void shouldProduceHumanReadableStringWhenRoleHasNoPermissions() {
        assertRoleStringForPermissions("Role: no permissions");
    }

    @Test
    void shouldProduceHumanReadableStringWithOnePermission() {
        assertRoleStringForPermissions("Role: read", READ, WRITE);
    }

    @Test
    void shouldProduceHumanReadableStringWithAllPermissions() {
        assertRoleStringForPermissions("Role: read, write", READ, WRITE);
    }

    private static void assertRoleStringForPermissions(final String operand, final Permission... permissions) {
        assertThat(new Role(permissions).toString(), equalTo(operand));
    }
}
