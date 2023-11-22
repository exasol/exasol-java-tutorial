package com.exasol.javatutorial.projectmanagement;

import java.util.Set;
import java.util.stream.Collectors;

public class Role {
    private final Set<Permission> permissions;

    public Role(final Permission... permissions) {
        this.permissions = Set.of(permissions);
    }

    public boolean hasPermission(final Permission permission) {
        return permissions.contains(permission);
    }

    public String toString() {
        if(permissions.isEmpty()) {
            return "Role: no permissions";
        }
        else {
            return "Role: "
                    + permissions.stream()
                    .map(p -> p.toString().toLowerCase())
                    .sorted()
                    .collect(Collectors.joining(", "));
        }
    }
}
