## Red, Green, Clean

In this part of the tutorial we will take a look at what we can do to make our tests and implementation cleaner when using TDD. Note that improving the test code comes after getting the test to do the right thing.

We implemented the zero-permissions requirement of the role entity in ["First Steps Into TDD"](first_steps_into_tdd.md), so we will pick it up from there.

### Don't Repeat Yourself (DRY)

In production code the "Don't Repeat Yourself" is an iron rule to avoid maintenance problems through implicitly coupled changes. Quick spoiler: test code is different and applying the rule blindly here can decrease code quality.

Let's first do the naive thing and add more test methods for different cases.

```java
class RoleTest {
    // ...    

    @Test
    void shouldHaveReadPermissionWhenCreatedWithRead() {
        final Role role = new Role(READ);
        assertTrue(role.hasPermission(READ));
        assertFalse(role.hasPermission(WRITE));
    }
}
```

By now, you know the drill:

1. Add the missing constructor (without implementation)
2. Run the test
3. Make sure it fails
4. Implement
5. Rerun the test
6. Make sure it succeeds

You will note that adding a single parameter to the constructor will break the previous test. A variable argument list will resolve the compiler error.

```java
public class Role {
    public Role(Permission... permissions) {
    }

    public boolean hasPermission(Permission permission) {
        return false;
    }
}
```

This is just enough to make the first test pass again and the second fail (which is what we want in step 3).

Please assign the constructor vararg parameter to an instance variable `permissions` of type `List<String> permissions`. And use a `contains` check in `hasPermission`.

```java
public class Role {
private final List<Permission> permissions;

    public Role(final Permission... permissions) {
        this.permissions = List.of(permissions);
    }

    public boolean hasPermission(final Permission permission) {
        return permissions.contains(permission);
    }
}
```

We could have gone through the trouble of having two different constructors, but here we reached the point where that would have just created bloat. The change was still tiny compared to the last successful test run, so it was safe enough.

Both tests are now green.

By this point you probably already think that adding new test methods for such small modifications gets old quite quickly. And you are right.

Remember `@ParameterizedTest`? Let's look at a solution that does all in one test and compare.

```java
class RoleTest {
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
                .toArray(size -> new Permission[size]);
        final Role role = new Role(grantedPermissions);
        assertThat(role.hasPermission(READ), equalTo(mayRead));
        assertThat(role.hasPermission(WRITE), equalTo(mayWrite));
    }
}
```

While this test has a nice test matrix on top and is fairly easy to extend, it is also convoluted and hard to read.

_Short_ test matrices are a super convenient and readable way to handle variant testing in a single test method.

### Auto-generated Permutations

The next option that comes to mind would be to iterate over all permutations of the permissions. So far we have 2^2 permutations: none, read only, write only, read / write. If we added more permissions, the number of tests would quickly rise. It is quite clear that with more than three manually copying the same test over and over hurts maintainability and even a test matrix would be too annoying to write.

Here is an example of automatic permutations for the role test. It works by looking at the role as a bit set and iterating over all bit combinations. As you can see the code is not quite obvious.

```java
class RoleTest {
    @Test
    void shouldReportPermissionsTheRoleWasGiven() {
        final int numberOfPermissions = Permission.values().length;
        final int numberOfPermutations = 1 << numberOfPermissions;
        for (int mutation = 0; mutation < numberOfPermutations; ++mutation) {
            final Set<Permission> permissionSet = generatePermutation(mutation, numberOfPermissions);
            final Role role = new Role(permissionSet.toArray(new Permission[permissionSet.size()]));
            for (int permission = 0; permission < numberOfPermissions; ++permission) {
                assertThat(role.toString(), role.hasPermission(Permission.values()[permission]),
                        equalTo((mutation & (1 << permission)) != 0));
            }
        }
    }

    private Set<Permission> generatePermutation(int mutation, int numberOfPermissions) {
        final Set<Permission> permissions = new HashSet<>();
        for (int permission = 0; permission < numberOfPermissions; ++permission) {
            if ((mutation & (1 << permission)) != 0) {
                permissions.add(Permission.values()[permission]);
            }
        }
        return permissions;
    }
}
```

As you saw you have two choices: either pick a couple of representative test case samples and consciously skip the rest or write an algorithm that automatically iterates over all permutations.

I advocate the first option. It is faster, produces more readable code, and you evade a typical trap.

Also, please keep in mind that not all permutations of a test case actually add to the test quality. If you test the same thing over and over again, you are wasting time and energy instead of making your code safer.

Check out [equivalence partitioning](https://www.baeldung.com/cs/software-testing-equivalence-partitioning) as a possible method of reducing the variants to be tested.

### The Danger of Testing Algorithms With Algorithms

One inherent danger of more involved test methods &mdash; especially those that generate variants &mdash; is that you might end up testing an algorithm with itself. This is a recipe for trouble, since it will more often then not make a test pass even if the implementation does the wrong thing.

So wherever possible, please use constant and explicit setup data and expectations.

## Descriptive and Meaningful Phrases (DAMP)

When developers realised that the DRY principle does not always lend itself best to unit tests, of course someone had to come up with a concept that had an opposing acronym. The DAMP principle focuses on readability over the last bit of eliminating duplication. The concepts are _not mutually exclusive_ though.

Let's go back to our role and permission example. With only permissions two you have four permutations in the role and the code is more expressive when spelled out. If you have ten different permissions, you have 1024 permutations, and it is pretty clear that spelling them all out is neither efficient nor useful.

On the other hand we saw that the programmatic approach of generating the permutations has its own problems rooted in complexity and potential waste.

In this particular case limiting yourself to a few short handwritten test cases is the best option.

To pick meaningful examples, let's use a boundary checking approach and make a list of what we want to test:

* no permissions
* one permission
* all permissions

If you apply this, you get a small set of tests for `toString`.

```java
class RoleTest {
    // ...    

    @Test
    void shouldProduceHumanReadableStringWhenRoleHasNoPermissions() {
        assertThat(new Role().toString(), equalTo("Role: no permissions"));
    }

    @Test
    void shouldProduceHumanReadableStringWithOnePermission() {
        assertThat(new Role(READ).toString(), equalTo("Role: read"));
    }

    @Test
    void shouldProduceHumanReadableStringWithAllPermissions() {
        assertThat(new Role(READ, WRITE).toString(), equalTo("Role: read, write"));
    }
}
```

You can see the DAMP principle applied here.

Let's check a couple of criteria:

| Criteria                            | Applies | Details                                                                                             |
|-------------------------------------|---------|-----------------------------------------------------------------------------------------------------|
| Did we avoid all duplication?       | ❌       | There is still a small amount of duplication. The pattern repeats.                                  |
| Do we avoid coupling between tests? | ✅       | Each tests a distinct aspect. Setup is separate. Changing one test does not affect the others.      |
| Are the tests descriptive?          | ✅       | They have proper names that will show up in the test log. The tests themselves are trivial to read. |
| Are they meaningful?                | ✅       | Thanks to limiting ourselves to boundary cases each of them adds value.                             |

ⓘ The test case `shouldProduceHumanReadableStringWithAllPermissions` needs extra consideration that is covered in section ["Dealing With Randomness"](../dealing_with_randomness.md).

So where do we stop with deduplication then? Let's look at the next iteration of the code:

```java
class RoleTest() {
    // ...
    
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
```

While we now rigorously eliminated all traces of code duplication, I would argue that we did so at the expense of readability. Sure, the code is more compact, but it does not convey the intent as well. Not to mention that the first test case is awkward due to the lack of a second parameter. You could add an empty array, but code scanners will likely mark that as a code smell.

In sum:

1. Avoid coupling
2. Express the test intent explicitly and clearly
3. Stop compacting the code 

For a more elaborate discussion on DRY and DAMP please check out [[1]](#1).

## References

<a id="1">[1]</a> ["DRY vs DAMP in Unit Tests"](https://enterprisecraftsmanship.com/posts/dry-damp-unit-tests/), Valdimir Khoirkov, Enterprise Craftsmanship, June 8 2020
