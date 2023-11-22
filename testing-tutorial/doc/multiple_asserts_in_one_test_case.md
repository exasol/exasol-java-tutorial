## Dealing With Multiple Assertion

Most of the time test cases are perfect with a single assertion at the end. That being said, there are more than enough situations where you either need to validate a state change or multiple aspects of the unit under test to be sure that you are covering everything.

Let's take a simple example from the project management exercise:

```java
class RoleTest {
    @Test
    void shouldHaveNoPermissionsWhenCreatedWithoutAny() {
        final Role role = new Role();
        assertFalse(role.hasPermission(READ));
        assertFalse(role.hasPermission(WRITE));
    }
    
    // ...
}
```

What happens if the first assertion fails? The test case terminates and the second assertion never happens.

Do we want that?

Unfortunately the answer is: it depends.

### Independent Assertions

In the example above the two assertions are independent, meaning that if the first fails, the result of the second one is still relevant to see if you have one or two errors. In those cases we want to make sure both assertions are executed. Otherwise you would lose valuable debugging information.

```java
class RoleTest {
    @Test
    void shouldHaveNoPermissionsWhenCreatedWithoutAny() {
        final Role role = new Role();
        assertAll(
                () -> assertFalse(role.hasPermission(READ)),
                () -> assertFalse(role.hasPermission(WRITE))
        );
    }
    
    // ...
}
```

Wrapping the assertions in a list of lambdas inside `assertAll()` makes sure the test case runs all of them regardless of whether they succeed.

### Assertions With Dependencies

Some assertions validate aspects that depend on each other. In those cases you might as well stop early, since later errors are the necessary consequence of earlier ones.

A classical example of this situation is when you have to validate exceptions.

```java
class PluginFactoryTest {
    @Test
    void shouldThrowExceptionWhenCreatedWithEmptyName() {
        final PluginFactory factory = new PluginFactory();
        final Throwable exception = assertThrows(IllegalArgumentException.class, () -> factory.create(""));
        assertThat(exception.getMessage(), "Cannot create plugin when no name is provided.");
    } 
}
```

It is pretty obvious that checking the exception message requires that an exception is caught first. There is no point here in using `assertAll()`.

### Expensive Assertions

Another situation in which forcing all assertions to be executed is not a good idea is if they are very expensive. This situation usually only happens in integration tests, end-to-end-tests or performance test. Sure if the assertions are independent, you will lose debugging information. But if that can save you valuable CI server minutes it might be worth cutting your losses on failing tests.

The good news is that this is usually a rare case.

### Expensive Setup

The closer to the user a test is the more expensive it gets in terms of runtime. It is not uncommon for say an end-to-end test to be so expensive that you will want to avoid repeating the test setup. This is one of the _rare exceptions_ where instead of always starting with a clean slate it can be overall beneficial to have a shared setup for your tests and squeeze as many tests cases (read "assertions") out of the now ready-to-test system as possible.

Remember though that this approach can quickly backfire if any of your test cases changes the state of the system under test. In that case you pay the price of the optimization with tests that depend on each other and therefore are fragile.

As always, _measure first_ before you decide to sacrifice reliability for speed!

[TDD and BDD](tdd/tdd_and_bdd.md) &larr; | &uarr; [Introduction](introduction.md) |  &rarr; [Dealing with Randomness](dealing_with_randomness.md)