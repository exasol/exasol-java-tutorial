# Software Testing

> The exmaple throughout the document
> 
> * Right / role management
> * Persist in a database (to force integration testing)

## Automatic vs. Manual

### Speed is Quality

```plantuml
title: In an ideal world
test -> implement
implement -> test
```

```plantuml
title: In reality ideal world
test -> get angry : wait
get-angry -> implement : try to remember\nwhat you wanted to do
implement -> test
```

Tests need to be fast. If they are not, people don't execute them. If they are not executed, the software accumulates regressions.

Tests used in TDD and BDD need to be so fast that you can afford to run them after each small modification you made. That can be a couple of times per minute.

As a rule of thumb individual unit tests should be tiny fractions of a second, tests for a single unit not longer than a second, a unit test suite should ideally run in under a minute.

To judge how long integration tests should run, ask youself: how long am I willing to wait for feedback of a continuos integration build (CI) after I pushed a commit? An hour? Probably not. Half an hour? Not really. 15 minutes? Maybe, but that's the limit.

## Testing Pyramid

Software tests can be done on different levels. In this chapter we are going to discuss why each type has its merits and short-comings and why the question "which kind is most important" will always depend on the _target audience_ of a test.

```
^ Relevance for end user  /\                        |
| Complexity             /  \                       |
| Effort                /Perf\                      |
| Costs                /------\                     |
|                     / System \                    |
|                    /----------\                   | 
|                   /Integration \  Maintainability |
|                  /--------------\    Localization |
|                 /     Unit       \      Stability |
|                /__________________\         Speed v
```

You need a solid fundament of unit tests because unit tests:

1. &hellip; very fast
2. &hellip; pinpoint error locations
3. &hellip; are robust to local changes (best case a single change makes one test red)
4. &hellip; have low maintenance effort

So your goal should be to test everything that you can with unit tests.

Unfortunately, unit tests are not enough. At some point you need to validate the interaction of your units with other units. And at some point you also want to make sure that everything works end-to-end.

The higher up you climb on the test pyramid, the more expensive, slow and the more fragile your tests get. "Fragile" means that a small change in the production code can flip a lot of integration tests and even more end-to-end tests. Expensive and fragile is also a really unpleasant combination. All of this goes to say that you should intentionally limit all tests to an as low level as is sufficient to prove correct operation. 

## Handling Dependencies

With integration comes dependency handling. Dependencies are modules your modules need to function and can range from libraries for data serialization to a web service.

There are two approaches when it comes to dependency handling: floating dependencies and strict version pinning. While there is a best practice in computer science, that you should be strict in what you deliver and flexible what you accept, this rule applies to your interfaces, not to your tests.

Letting the versions of dependencies free-float in builds or tests is begging for trouble. First of all you will lose reproducibility when it comes to your build products and test results. We are going to discuss that later (see section "reproducibility") in detail. Suffice to say for now that you can forget about regression testing, if your builds and tests are not reproducible.

Also, avoid cyclic dependencies. Cycles in your dependency chain lead to tight coupling, meaning that you can't change one thing without changing the other. 

Here is an actual example from one of our modules

```plantuml
exasol-testcontainers --> bucketfs-java : <<depend>>
bucketfs-java --> exasol-testcontainers : <<depend>>
```

Why did it happen?

`exasol-testcontainers` contains an abstraction that makes installation of drivers in BucketFS more convenient and also simplifies access to the default bucket of a running test container.

`bucketfs-java` on the other hand uses the testcontainers for integration testing.

If you look closely, this is a testing cycle, not a cycle in production code. Nevertheless, it creates coupling.

Options to resolve this:

1. Join the projects. Downside: `bucketfs-java` can and should be usable without the testcontainers
2. Run the BucketFS test without testcontainers. Downside: creates lots of code duplication and makes the integration tests very hard.
3. Move the integration tests to `exasol-testcontainers` Downside: you lose immediate feedback on the `bucketfs-java` project and introduce the danger of regressions.
4. Remove the integration of `bucketfs-java` from `exasol-testcontainers`. Downside: while you made the maintainers` lives easier, you made it harder for users. 


### Testing in Isolation

#### Decoupling

- Refactor for better decoupling
- Dependency injection for better testability

#### Stubbing

#### Mocking

##### Mock Only What you own

Exception: to trigger exception handling

## Reproducibility

## Why Reproducible Builds Matter

## Reproducible Tests

- Clean slate before test
- Avoid tests that build on each other
- Eliminate randomness
    - Pseudo random with given seed
- Isolation (s.a.) can remove randomness that you can't control
- Replace `sleep` by event-driven mechanisms
  - Register listeners where available
  - Use polling of system state where not (faster and more reliable than fixed sleep time)
  - Use timeouts in tests when polling
- Inject date and time where possible (otherwise: see fuzzy matching)
- Parallelism and timing

### Speed vs. Reproducibility

- Die early, die loud
- When preconditions fail, fail all dependent tests
- Assumptions: a better way to check preconditions than dependent tests
- Integration tests with shared preparation: separate tests that modify the setup from those that don't

### Fuzzy Testing

- Sometimes you cannot get rid of some randomness
- Fuzzy test can help alleviate the pain points
- Example: fuzzy date / time matching

## Test First

### TDD and BDD

#### Red - Green - Clean

#### Red - Green - Clean - Fast

#### When "Fast" and "Clean" are in Conflict

## Dry vs. Damp

- Avoid duplication where you can
- Parameterized tests
- When deduplication makes test hard to read, prioritize clearness
- When test setup is too far detached from the test case

## Making Your Live Easier with Dedicated Matchers

### Hamcrest Matchers

--> Find something similar for C++ / Python

### Writing Your own Matcher

## How and What Much Should I Test?

- Where's your specification?
- Can I reduce complexity to reduce the number of tests?
- Can I reduce coupling to reduce the number of tests?
- Risk management and bad weather tests

### Setting Test Goals

### Test Coverage

- tests that don't add coverage
- coverage quirks (enum test coverage Java 11 vs. 17 construct)
- raising coverage for boilerplate code (equalverifier)
- It is okay to test exception paths with mocks

## Static Code Analysis

- in IDE
- in automation

<!-- Not for Core -->

## Dynamic Analysis With Instrumented Code

--> Thomas

### Matrix Tests

- Version Matrices
- Variant Matrices

<!-- Java only -->

### Performance Tests

- Adding performance expectations: the cheapest way to detect performance regressions
