## Automatic Testing

Automatic testing means that your tests are code that runs without interaction. There are numerous advantages of automating your tests.

### Speed Matters

In an ideal world an implementation round-trip looks like this:

```
      ,-----------------.
      |                 v
.-----------.     .-----------.
| Implement |     |   Test    |
`-----------´     `-----------´
      ^                 |
      `-----------------´
```

The reality unfortunately often looks different.

```
                .-----------.           wait for
      ,-------->|   Test    |--------.  tests to finish
      |         `-----------´        v   
.-----------.                  .-----------.
| Implement |                  | Get angry |
`-----------´                  `-----------´
      ^   .----------------------.   |  
      |   | Try to remember what |   |  waste more
      `---| you wanted to do     |<--´  time
          `----------------------´
```

It doesn't help that with long-running tests you try to make good use of the waiting time by switching to a different task.
This only adds to the mental burden that eas away at your productivity. Task switches take time, and they cost our brain a lot of energy.

Also, let's be real: a modern computer can run thousands of unit tests in a second. In a second you don't even start to do the first manual test.

With end-to-end test it gets even worse. Even if a computer has to simulate all user interaction, a test including a UI is so fast that a spectator can't follow it, let alone be able to do it anywhere near as fast.

You need to design your tests for speed in order to get the best out of the automation.

### Speed is Coverage

The faster your tests are, the more you can afford to run. If your tests are slow, you start cutting corners with test execution. You put off running the expensive ones more and more until your feedback loop reaches a point where the feedback from your tests comes too late to be relevant.

This is especially damaging when it comes to regression testing. You want to run those regression tests as often as possible &mdash; best case after each small change &mdash; in order to make sure you did not break any existing functionality.

