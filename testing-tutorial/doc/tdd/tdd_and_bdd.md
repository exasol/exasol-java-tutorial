## Test-driven Development and Behavior-driven Development

Test-driven Development (TDD) is a software development strategy that starts with formulating a test before writing the implementation.

Let's look at the benefits of starting from the tests:

1. Your software is guaranteed to be testable
2. The design will be cleaner because TDD fosters separating concerns better, in order to test units in isolation
3. The solution will be less complex, since you only implement what is needed to make the tests succeed
4. Less complex software is also more reliable and in most cases faster
5. Locating issues in your code goes quicker
6. You get high test coverage from the very first second

[Behavior-driven Development (BDD)](https://dannorth.net/introducing-bdd/) is a special form of TDD. It is more formalized to make sure that the way the tests are named and programmed matches the original user description of the expected behavior as closely as possible.

The formulation of a behavior follows a given-when-then template, where "given" describes the preconditions (or setup if you will), "when" is an event and "then" the thing that should happen under the given circumstances.

### From Text to Test

There are even test frameworks that aim at bridging the gap between textual behavior descriptions and the actual executable test. On a people-level that is the gap between requirement engineers and quality assurance. 

#### [Cucumber](https://cucumber.io/)

Cucumber is a test framework that accepts textual descriptions of user requirements in a very formalized way and uses converters to turn this into executable tests.

```
Scenario: User needs at least one role that allows viewing a project
   Given: A user needs read-permission to view a project
    When: the user has any role in that project with read-permission
    Then: the user can view the project's contents
```

#### [JBehave](https://jbehave.org/)

JBehave follows a similar approach as [Cucumber](#cucumber). Take formalized text (stories), implement converter rules. Run tests &mdash; or as the authors would call it, run the stories.

#### [FitNesse](http://www.fitnesse.org)

FitNesse takes an even more drastic approach by offering a wiki in which stakeholders describe the software.

#### Plain old Handwritten Tests

Of course, you don't need a framework to do either TDD or BDD. In both cases, handwritten tests work just fine. It is easy to see with the [cumcumber example](#cucumber) above that you could write a test that does the same thing and is readable for stakeholders.

```java
class ProjectTest {
    @Test
    void shouldBeViewableForUserWithAtLeastOneRoleHavingViewPermission() {
        // Given:
        final Project project = new Project("A");
        final User user = new User("alice");
        final Role roleReadWrite = new Role(Permission.READ, Permission.WRITE);
        final Role roleList = new Role(Permission.LIST);
        // When:
        project.grantUserRoles(user, roleReadWrite, roleList);
        // Then:
        assertTrue(project.isViewableForUser(user));
    }
}
```

If you go all in on the idea of showing test code to your stakeholders, I would advise to make it even more readable:

```java
import static com.example.projectmanagement.Permission.*;

class ProjectTest {
    
    // This is for the stakeholders:
    @Test
    void shouldBeViewableForUserWithAtLeastOneRoleHavingViewPermission() {
        // Given:
        final Project project = createProjectWithName("A");
        final User user = createUserWithName("alice");
        final Role roleReadWrite = createRoleWithPermissions(READ, WRITE);
        final Role roleList = createRoleWithPermission(LIST);
        // When:
        project.grantUserRoles(user, roleReadWrite, roleList);
        // Then:
        assertTrue(project.isViewableForUser(user));
    }
    
    // Stuff that only developers are interested in:
    private Project createProjectWithName(final String name) {
        // ...
    }
}
```

We will discuss further improvements in the later exercises. For now this example should be sufficient to explain how we can bridge the gap between business and development.

### Test First

No matter whether you choose plain TDD or prefer BDD, the test (or behavior definition) comes first.

Think of your product as a creature subjected to evolution. Each test or behavior definition adds a bit of evolutionary pressure that your product needs to adapt to in order to survive.

If you take this approach you will always get the simplest solution that satisfies the tests. And striving for simplicity is one of the key traits of a professional software developer.

Up &uarr; [Introduction to Java testing](../introduction.md) | Next &rarr; [First steps into TDD](first_steps_into_tdd.md).