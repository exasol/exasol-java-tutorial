<img style="float: right; width: 33%;" src="https://www.esa.int/var/esa/storage/images/esa_multimedia/images/1998/01/ariane_501_explosion/9088578-6-eng-GB/Ariane_501_explosion_pillars.jpg"/><p style="text-align: right">Image: Ariane 501 explosion 4 June 1996,<br/>courtesy of European Space Agency (ESA)</p>

# Why Testing Matters

There are a number of reasons why tests are necessary in professional software development:

1. Developers make mistakes, tests help uncovering them
2. Tests help avoiding regressions
3. Tests are an executable form of specification &mdash; if you can't formulate a test, your specification has holes

## The Numeric Conversion Error That Cost ESA a Rocket

On 4 June 1996 the first Ariane 5 launcher veered off course and self-destructed due to a software bug [[LIONS1996]](#lions1996).

It was a on overflow in a conversion from a 64-bit floating point number to a 16-bit integer that caused both Inertial Reference Systems (SRI) to enter and report a failure state. The root cause was a combination of reusing software from a previous launch vehicle on the newer Ariane 5 model and a downward conversion that did not fail safely in case of overflow. The overflow itself was due considerably higher horizontal velocity of Ariane 5 compared to its predecessor. Since the On-Board Computer (OBC) misinterpreted the error bit pattern as an actual measurement, engaged in drastic course correction causing the solid state thrusters to break loose due to mechanical stress. This in turn triggered self-destruction of the launcher.

### The Missing Test, &hellip;

> There is no evidence that any trajectory data were used to analyse the behaviour of the unprotected variables, and it is even more important to note that it was jointly agreed not to include the Ariane 5 trajectory data in the SRI requirements and specification.

&mdash; [[LIONS1996, 2.2, §4](#lions1996)]

Ultimately what kept the bug hidden until the actual first launch was the fact that the SRI software was never tested against projected accelerometric signals for Ariane 5.  

### &hellip; the Code That Should not Have run,  &hellip;

The irony was that the fault happened in a part of the software that was responsible for _pre-launch_ alignment, which would be active to around 40 seconds after launch. The reason this code was still active, was that this allowed _previous_ versions of Ariane to recover faster from a halted countdown. That use case did not exist anymore for Ariane 5 though due to changed launch preparation sequence.

So what was tried-and-true code for Ariane 4 had now become a liability for Ariane 5.

### &hellip; and the "Failure as Designed"

The failure report states that the supplier implemented the fault handling _according to specification_ and that specification aimed to handle random hardware failure. The fact that both SRIs ran the same software and that the software had a systematic flaw caused both of them to shut down, leaving the OCB without proper alignment data. What's worse, the failure code looked like flight data to the OCB.

Consequently, the report argues that hard failure of a mission-critical component was the wrong design decision. The launcher might have been saved had the software been written to fail more gracefully, i.e. provided best-effort alignment data after detecting the overflow.

### Lessons Learned the Hard Way

1. Make assumptions explicit and verify them.
2. Skipping tests because the code worked before in a similar scenario is dangerous.
3. Complex software is inherently prone to subtle errors.
4. When a requirement is no longer valid, the code for it must be removed.
5. Spell out the limitations and constraints of your software.
6. When on a mission-critical path, fail safely.
7. Last, but not least: conduct reviews of implementation, test _and specification_.


## References

###### LIONS1996
["ARIANE 5, Flight 501 Failure, Report by the Inquiry Board"](https://esamultimedia.esa.int/docs/esa-x-1819eng.pdf), Prof. J. L. LIONS, Paris, 19 July 1996

&rarr; [Tests are an Executable Specification](test_are_an_executable_specification.md)