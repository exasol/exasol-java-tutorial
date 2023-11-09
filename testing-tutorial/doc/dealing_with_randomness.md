### Dealing With Randomness

Please take a look at the code below. Can you spot where you might have to be smart in your test? 

```java
class Role {
    private final Set<Permission> permissions;

    // ...
    
    public String toString() {
        if(permissions.isEmpty()) {
            return "Role: no permissions";
        }
        else {
            return "Role: "
                    + permissions.stream().map(p -> p.toString().toLowerCase()).collect(Collectors.joining(", "));
        }
    }
}
```

