## Executable Specification

Remember that we called tests an "executable specification" of your software? Because that is essentially what they are. Well-written tests define what your software is supposed to do (black box test), how fast it is supposed to do that (performance tests) and in some exceptional cases even how it is doing its job (white box tests).

Give tests proper names and make them readable and voilà, you have a nice specification of your software.

Obviously the level of the test defines what it specifies. Unit tests are not suitable to be end-user acceptance tests, so they also can't serve as specification for end-user requirements.

### Tests That Specify User Expectations

You can call this specific kind by many names, some of which also depend on the context. "Acceptance Tests" is an often used term, since it expresses the fact that in order for the users or generally stakeholders to accept the software product, these tests need to be successful.

> As far as the customer is concerned, the interface is the product.

*&mdash; Jeff Raskin, ["The Humane Interface", 2001](https://en.wikiquote.org/wiki/Jef_Raskin#The_Humane_Interface_(2001))*

As much as it hurts our feelings as software developers, Mr. Raskin is absolutely right on this topic. What users experience of your product is the outermost interface of your product, either a UI, CLI or API they interact with. If that does not behave like it should, the product is broken.

Where exactly the root cause sits is of little to no consequence for user acceptance. You can have the most elaborately built backend in the world and still be a commercial failure if the user interface sucks. Vice-versa, the prettiest user interface will not save your product, if it moves at a snails pace.

If you want your software to be a success, there is no way around knowing your users expectations and verifying by means of automated tests that they are met.

Unfortunately, acceptance tests sit at the very top of the [testing pyramid](../testing_pyramid.md), meaning they are both expensive and fragile.

### Tests That Specify Developer Expectations

Just because your end-users decide about the success of your product in the end does not mean you shouldn't have expectations about your product yourself. Quite the contrary.

The art of software design is to take your users' expectations and find the best balance between meeting them as closely as possible and engineering a system with just the necessary level of complexity.

How you break down your product is important to you and your fellow developers, but not to the end-users, as long as it does not conflict with their requirements. So once you left the level of the end-user interaction, the target audience for your tests shifts.

Lets for example say your product is implemented as a set of network services that communicate with each other via REST. The users of those services are other developers who want to connect to them and build something new on top of that. The REST interface is now your outside interface and the service consumers your new customers.

Go down another level, and you might provide a software library that encodes and decodes JSON. There is again a shift in target audience requiring you to formulate your tests in a way that they express the expectations of the users of your library's API.

As a rule of thumb, design each layer of your software as if you planned to sell it as a product. With this mindset the users of each layer's interface become your customers, and you give them the best experience on each level. 