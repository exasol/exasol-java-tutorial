## Red, Green, Clean, Fast

> There is no doubt that the grail of efficiency leads to abuse. Programmers waste enormous amounts of time thinking about, or worrying about, the speed of noncritical parts of their programs, and these attempts at efficiency actually have a  strong negative impact when debugging and maintenance are considered. We should forget about small efficiencies, say about 97% of the time: pre-mature optimization is the root of all evil.
> 
> Yet we should not pass up our opportunities in that critical 3%. A good programmer will not be lulled into complacency by such reasoning, he will be wise to look carefully at the critical code; but only after that code has been identifie

&mdash; Donald E. Knuth [[KNUTH1974]](#knuth1974)

I added the full quote here since all aspects that Donald Knuth brought up here in his 1974 paper are still as relevant today as they were back then.

### Learn to Distinguish Between Code That Requires Optimization and Code That Doesn't

Testing code is not production code. While [we want tests to be fast](../automatic_testing.md#speed-matters), we should focus on testing the correct thing. Performance optimization tends to make test more complex, which in turn makes them harder to read and in the end harder to determine if they are correct.

There performance optimization must be **the very last step** in your test writing process.

Remember the old programmer joke?

> First rule of optimization: don't do it.
> 
> Second rule of optimzation (for experts only): don't do it _yet_.

The `yet` is a not so subtle hint that you should first measure and then decide, where to optimize and how.

#### Define When a Test Run is Too Expensive

You need hard criteria to make the decision when to spend the effort to improve test performance. Here are some suggestions for factors to consider:

* How long do you allow a complete test suite to run?
* How long should each test category (unit tests, integration tests, end-to-end tests) take at max?
* What monetary costs am I willing to accept for a full test on a cloud CI?
* What is the quota on our shared test infrastructure that I have to fit my nightly tests into?

#### Look at the Execution Times

Luckily with almost all test execution environments, you get your first statistics for free.

<!-- TODO: add screenshot from execution of long-running tests -->

If your overall test execution time is unacceptable, first go through the list of tests and identify the main offenders. It is unlikely that all your test are equally slow, so focus your efforts at the worst in descending order.



## References

###### KNUTH1974

["Structured Programming with `go to` Statements"](https://dl.acm.org/doi/10.1145/356635.356640), Donald E. Knuth, Stanford University, December 1974
