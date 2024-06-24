# Programming 2 - Exercise 12: Centralized Webapps

In this lab, you will build centralized webapps using many of the principles of software construction you have seen in the course so far. That term will be explained in more detail very soon, but you probably already use many such centralized webapps in your everyday life: ticket-reservation platforms, multiplayer online card games or board games, e-banking, Q&A forums, chat, etc. In particular, using a simple state machine model, you will implement the logic of two games—Tic-Tac-Toe and Memory Matching—and then a third app of your choice.

This lab aims to exercise the following concepts and techniques:

* Creating a complex project from scratch
* Working with code written by others
  * As if it was written by a colleague, depending on a previously agreed-upon interface
  * Or in the form of a library with well-defined APIs
* Modeling and implementing systems using state machines
  * Including how to translate specifications of interactive processes into state machines
* Safe, functional uses of mutable state (the power of just one var)
* Designing webapps with a simple model-view design pattern

The most important parts of the exercise are marked with ⭐️. Exercises that are particularly challenging are marked with 🔥.

**You are allowed to copy/clone/fork this repository, but not to share solutions of the exercise in any public repository or web page.**

## High-level overview

A webapp is a piece of software that runs over the web. (Our running example for this section will be a classroom booking platform.) The *clients* of a webapp are the programs that expose its core functionality to the outside world. (For instance, a campus administrator’s portal where they can update the availability of a room is a client, as is the smartphone app where you can search for rooms on a given date.) Often, this system has some *state* (e.g. which dates a particular room is free/booked), and some information needs to be communicated between clients (if the campus admin removes a room for maintenance, it shouldn’t show up in your search results).

One way to structure the webapp is to have a *server* that manages *all* the state and performs actions only in response to client *events*. On receiving an event (e.g. book a room for Nov 22), the server can optionally modify the state, and optionally send a response to that client, or other messages to other clients too. Then clients can initiate further events. This is a *centralized webapp* because the state is centralized in the server.

>[!NOTE]
>Another way to structure the system would be to have no complex logic on the server, and to have clients communicate directly with one another, where each client would hold some part of the state. In that model, servers may be used to facilitate communication and queue messages, but don’t perform complex processing. (For example, when you send an SMS message to your friend, you do connect to a ‘server’ (the cell tower), but it only facilitates communication between your client (phone) and theirs. The server has no state and you and your friend maintain local copies of your message history.) This would be a *decentralized webapp*. What are the advantages and disadvantages of this design compared to a centralized one? (For example, who is affected if you lose your phone? Is it possible to fully delete a message?)

### State machines

Suppose we have a system with some state that changes in response to events. We can construct a graph where every vertex corresponds to a possible state and every edge, or *state transition*, corresponds to an event. Then, the evolution of the system over time as it responds to events can be represented as a path in this graph, following the corresponding transitions from state to state. (The graph itself does not change at all.)

This representation of a stateful system as a graph that transitions (responds to events) from one vertex (state) to the next is called a *state machine*. State machines are one of the most useful concepts in computer science, because they let us reason about many systems that look different on the surface with the same mental tools.

As a first example, let’s try to model the software that decides when to open and shut the doors on the M2 metro line. Each bubble is a state and each arrow is an event.

PUT FIGURE HERE

Hmm, but that’s not quite right. The doors don’t transition directly from open to shut: they try to shut, but if someone is standing in the way, they go back to being open, and try again. The metro does not start until they are actually shut. Also, they shouldn’t open whenever the metro stops: they should only open when the metro stops and is aligned with the external doors on the platform. We can try to represent these additional details in a different state machine:

PUT FIGURE HERE

We have incorporated more information into our state, and introduced more events, to better represent the real system. Having this sort of model lets us reason about the system in many different ways.

* Is there a sequence of events that could lead to the metro moving with the doors still open?
* We have only one arrow leaving the “doors open” state, i.e. only one allowed event. What should happen if any other events are triggered? Ignore them silently, sound an alarm, alert a human operator, something else?
* Let’s compare the state machine to the actual implementation code. Is there any state in the implementation, i.e. any mutable variables, that is not reflected in the state machine? (There shouldn’t be, if we modeled the system correctly.)
* There are even tools that let us semi-automatically convert state machines into code and verify that the translation was done correctly! But in this lab, you’ll be doing that by hand.

```
Try to come up with as detailed of a state machine for the M2 doors as possible. Think about safety of passengers, emergency protocols, malfunctioning equipment, the fact that there are multiple independent doors, etc. How many states do you need? How would you structure the actual implementation? How would you prove that it is correct with respect to the state machine?
```

For our second example, let’s try to model the classroom booking platform from before with a state machine. The state maintains, for each campus, for each of its rooms, for each date, whether or not the room is free on that date. “For each date” means that the state is infinitely large! Of course, the actual implementation does not have infinite memory, so we may choose to have a different representation of the state—for example, storing only the dates on which a room is booked, and assuming the rest are free—or to make a simplifying assumption—for example, no reservations can be made more than six months in advance. But an infinite theoretical state lets us model the system elegantly and reason about whether those implementation choices are valid.

An example state:

PUT FIGURE HERE

We can’t draw the infinitely many states, but we describe them and reason about them, because we know how the possible events change the state.

* *Event*: Try to make a reservation for a particular room and date, but it is already booked.

   *State transition*: To the same state.
* *Event*: Try to make a reservation for a particular room and date, and it is free.

    *State transition*: To the state that is identical to the current one, except that that room is marked as booked.
* *Event*: Any search query.

    *State transition*: To the same state.

Again, we can ask whether our state and transitions capture everything we want about the system. How can a user cancel a reservation (ideally, only their own)? Does ‘unavailable due to maintenance’ need to be a separate status from ‘booked’, i.e. are there events that apply to one but not the other? How do we represent the server “holding” a reservation for 15 minutes to give the user time to fill out their personal information and information about the event, before finalizing the reservation? And so on.

```
Try to come up with a state machine for a Tic-Tac-Toe game with two clients (players). The state space is not infinite, but still very large, so you won’t be able to write/draw it out in full, but try to describe it as accurately as possible. Later, compare your state machine against the one suggested by the scaffold code.

Bonus question: exactly how large is the state space? It’s not 3^9 (9 squares, with 3 options for each square, blank, X, and O), because, for instance, all Xs is not a valid Tic-Tac-Toe state!
```

### Architecting webapps with state machines

Each webapp you will design and implement in this exercise is a state machine. The state machine will be initialized with a fixed list of clients. Clients will initiate events and send them to the server. The server will receive these events and perform the corresponding state transitions, i.e. update the state. Only the server maintains any state.

Beyond that, a key aspect of your webapps will be that each user has access to some part of the state. (E.g., the campus admin has access to the booking status of all the rooms on that campus through their own reservation portal.) The clients have no state nor memory, so all that they know about the world at a given point in time is exactly the content of the last message they received from the server. We call this a *view* or *projection* of the state. Views are sent by the server to each client, and the clients render them with some UI (user interface). Finally, the client sets up the UI to translate the user’s actions (e.g. clicking the search button) into events that are sent to the server.

```
Many card games and board games can be modeled in this pattern. Can you think of some games where the view/projection is the same for all clients (players)? And others where it is different for each client? Once you have thought of a few examples, the   [Wikipedia article about perfect information](https://en.wikipedia.org/wiki/Perfect_information) is a good starting point to learn more about how this topic is studied in game theory.
```

#### Actions: render, pause, alert

We are going to extend our notion of a state machine to make it more convenient to model the sorts of apps we want to build. Instead of each event triggering a state transition, it will trigger a sequence of *actions* that are sent to all the clients. In technical terms, this is a *labeled* transition system.

There are three types of actions: *renders*, *pauses*, and *alerts*.

* Renders update the server’s internal state and instruct clients to redraw their UI accordingly.
* Pauses instruct clients to stop processing actions for a given duration.
* Alerts instruct clients to display a message.

Pauses and alerts are dispatched as-is to all clients. Renders must be projected into views before being sent to clients.

These actions are performed by clients in order.

<details>
<summary> Why do we need these extensions? </summary>

For example, consider the game Uno. When a player plays the penultimate card in their hand, they must say “Uno!”. So the other players (clients) not only see the card that was just played (the view), but also hear “Uno!” (a side effect). In a webapp version of Uno, the server has to tell the clients to perform that side effect (in our model we might use an alert or a separate view).

This means that each event can trigger multiple actions, even multiple renders which correspond to state transitions. Why? Consider a countdown timer as an example. The first event from the client sets the countdown to, say, 3 seconds. The second event from the client starts the countdown. But then, the state machine transitions between four states—“3 seconds left”, “2”, “1”, “0”—without receiving any events in between. For each state, the client receives a side effect of waiting for 1 second, along with a view of the time left.

</details><br/>


To summarize:

1. A client sends the server an event.
2. The server produces a sequence of actions:
   1. For each render action the server updates its state and sends each client a view of that new state.
   2. Other actions are sent as-is.
3. Each client:
   1. Performs the actions in order. Side effects are executed and views are rendered with some UI.
   2. Sets up the UI to fire the next event based on the user’s actions.

Below is an illustration of client-server interactions for a simple classroom reservation platform.

PUT FIGURE HERE

PUT FIGURE HERE

One last bit of terminology: the representation of the state (which exists only on the server side) and the way the server responds to events is collectively called the *model*. Hence, this structure for webapps is called the *model-view* design pattern.

>[!NOTE]
>Optional reading: for a different explanation of a similar design pattern, you can read about the [Elm architecture](https://guide.elm-lang.org/architecture/). It involves the Elm programming language but the ideas are broadly applicable.

## Implementation overview

The previous section was purely about the design of centralized webapps. In this section, we’ll look a bit more concretely at how they are implemented.

### Serialization and deserialization

So far, we haven’t looked too closely at how a client and server can actually communicate with each other. Values in a program—say, instances of a class in Scala—cannot directly be transmitted over the internet, because the format of internet communications is different from the internal format of values in a program’s memory. Thus, the server and its clients need to agree on how to translate between the two, called *serialization* (from program to internet) and *deserialization* (the reverse).

Your webapps will have to define their own serializers and deserializers.

### Extensibility

In this exercise, every webapp defines:

* Three types: `Event`, `State`, `View`
* A server-side state machine (three functions: init, transition, projection)
* A client-side UI (one function from View to HTML document)
* Support code to serialize and deserialize views and events

The event and view types, as well as the serialization and deserialization functions, will be shared between the client code and the server code. The state and transition function will only live with the server. The view rendering will only live with the client.

Everything else is just the plumbing between these parts, and thus we have provided it to you as a library. You don’t have to understand how the library code works but you do have to understand its API. We have done our best to make the library code readable, though!

But then, creating another app is just a matter of implementing the three representation types and functions! This is how you will extend your program with a Memory Matching app and some other app of your choice after implementing Tic-Tac-Toe.

These apps are extensible in another way, too, because the same app can have different clients as long as they communicate with the server with the same events and views in the same serialized format. For example, Tic-Tac-Toe comes with a browser-based client where the user can click to move, but you could just as well have a REPL client where the user types in their moves, or a physical UI with buttons and lights. Similarly, the webapp doesn’t have to run over the web. As long as the server and its clients can communicate using the format they have agreed upon, they could be on a LAN, or even on the same machine, or there could even be just one client. Again, this is possible because they have a well-defined communication interface that doesn’t depend on the specifics of the plumbing that connects them.

### Code distribution

In the top-level directory for this exercise that you cloned:

* `driver/` contains the initialization code for the server (in `driver/jvm/`) and the default browser-based client (in `driver/js/`). (Yes, “JS” stands for JavaScript, and yes, we are still writing Scala! This magic is made possible by Scala.js which is an alternative compiler for Scala that produces JavaScript instead of JVM bytecode.) You do not have to understand this code at all.
* `lib/` contains the webapp library that you will be using. There is not very much code in here, but it provides all the flue you need to turn a state machine into an app. The first part of this lab, implementing Tic-Tac-Toe, will teach you how to use the APIs of this library. If you want to know more about a particular part, you can read its documentation.
* `apps/ticTacToe/` contains the scaffold code for the Tic-Tac-Toe app. This is a cross-platform project, which means that different parts of the project are compiled for JS and for the JVM, but can have shared dependencies. Hence, there are three subdirectories:
  * `js/`, in our case for the UI;
  * `jvm/`, in our case for the server, which implements the state transitions and projections; and
  * `shared/`, in our case for the type definitions for state, events, and views, as well as their serializers and deserializers.
* `apps/memory/` is organized the same as above.

The following tree summarizes the layout of the code:

```
.
├── apps
│   ├── memory
│   │   ├── js/src/main/scala/memory/MemoryUI.scala
│   │   ├── jvm/src/main/scala/memory/MemoryLogic.scala
│   │   └── shared/src/main/scala/memory
│   │       ├── MemoryLogic.scala
│   │       └── MemoryWire.scala
│   └── ticTacToe
│       ├── js/src/main/scala/tictactoe/TicTacToeUI.scala
│       ├── jvm/src/main/scala/tictactoe/TicTacToeLogic.scala
│       └── shared/src/main/scala/tictactoe
│           ├── TicTacToeLogic.scala
│           └── TicTacToeWire.scala
├── driver
│   ├── js/src/main/scala/driver/MainJS.scala
│   └── jvm/src
│           ├── main/scala/driver
│           │   ├── howManyHoursISpentOnThisLab.scala
│           │   └── MainJVM.scala
│           └── test/scala/driver
│               ├── MemoryTest.scala
│               ├── TestHttpClient.scala
│               ├── TestWebsocketClientFactory.scala
│               ├── TicTacToeTest.scala
│               └── WebappTest.scala
└── lib
    ├── client/src/main/scala/cs214/webapp/client
    │   ├── Pages.scala
    │   ├── StateMachineClientApp.scala
    │   ├── Traits.scala
    │   └── WebClient.scala
    ├── server/src/main
    │   ├── resources/www/static
    │   │   ├── main.css
    │   │   └── webapp.html
    │   └── scala/cs214/webapp/server
    │       ├── StateMachineServerApp.scala
    │       ├── WebServer.scala
    │       └── Websocket.scala
    └── shared/shared/src/main/scala/cs214/webapp
        ├── Apps.scala
        ├── Common.scala
        ├── Exceptions.scala
        ├── Messages.scala
        └── Wires.scala
```

## Implementation guide for Tic-Tac-Toe

### State, event, and view types

Let’s start with the state, event, and view types. These will be in the file `TicTacToeTypes.scala` under `shared/`.

Your friend has been working on the UI already, and the UI needs to know about the event and view types, so they went ahead and implemented those:

```Scala
enum TicTacToeEvent:
  /** User clicked cell (x, y) */
  case Move(x: Int, y: Int)

enum TicTacToeView:
  /** Game in progress. */
  case Playing(board: Board, yourTurn: Boolean)

  /** Game over. [[winner]] is [[None]] if the game ended in a tie. */
  case Finished(winner: Option[UserId])
```
<div style="text-align: right; color:grey"> 

[apps/ticTacToe/shared/src/main/scala/tictactoe/TicTacToeTypes.scala](./apps/ticTacToe/shared/src/main/scala/tictactoe/TicTacToeTypes.scala)
</div>

(What is `UserID`? Your first interaction with the webapp library! Since user IDs are something that many apps will have to deal with, the library provides an implementation. It is imported at the top of the file.)

On the other hand, the UI does not care about the state, so the `TicTacToeState` type is not implemented. Implement that type.

Remember that you already came up with a state machine for Tic-Tac-Toe in a previous section. Are those events and views the same as in `TicTacToeTypes.scala`? Can you use the same state type?

### Serialization and deserialization

For this section, you will work in `TicTacToeWire.scala` in the same directory as `TicTacToeTypes.scala`.

Events and views need to be serialized and deserialized for communication between the server and the clients. In addition, the automated tests don’t know the details of your representations, but need to check that the game is implemented correctly anyway. This is done via probes: methods that check specific internal properties of views and events without relying on the internal representations. For instance, the `getCellOwner` probe returns the `UserID` who has marked a given cell if there is one, else `None`.

Fill in the missing pieces marked with `???` in `TicTacToeWire.scala`.

### State transitions and projections

You’ve defined the state, event, and view types, and the serialization and deserialization functions. Your friend wrote the rendering function that displays a view and sets up events to be fired on user actions. What’s left is the server-side code: the transition function that responds to events, and the projection function that determines what view each client receives.

This code is in `TicTacToeLogic.scala` under the `jvm/` subdirectory.

1. The server receives an event from a client as an instance of `TicTacToeEvent`.
2. The `transition` function takes as input that `TicTacToeEvent`, as well as the `UserID` that originated it and the current `TicTacToeState`. It produces as output a `Try[Seq[Action[TicTacToeState]]]`.
   1. The `Try[...]` allows for the possibility of returning a `Failure(...)` containing an exception: one of the exceptions in `Exceptions.scala` under `lib/shared/`. Else, the function returns a `Success(...)`.
   2. The `Seq[...]` means that a sequence of `Action`s can be returned.
   3. An `Action[T]` is either a `Render` of `T`, a `Pause`, or an `Alert`.
   4. In our case, `T` is `TicTacToeState`. A `Render` of a `TicTacToeState` results in the corresponding state transition on the server and the corresponding `TicTacToeView` being sent to the client.
3. The `project` function takes as input a `TicTacToeState` and a `UserID` and produces as output the appropriate `TicTacToeView`. This is used by the webapp library when it handles a `Render` action as described above.

Further, when the server first starts, the `init` function produces the initial `TicTacToeState`.

Implement the missing parts (`???`) in `TicTacToeLogic.scala`.

### Try it!

Check your implementation by trying to play the game with the browser client.

1. In the top-level directory (not the *subproject directories*!), run `sbt`.
2. At the SBT prompt, use `run` to start the server on port 8080.
3. In your browser, go to localhost:8080. This will start a client in that browser tab.
4. You will be asked to choose an app; pick `tictactoe` and enter two user IDs (e.g. `x ; o`) in the `User IDs` field. Click “Start!”, then select one of these IDs for yourself on the next screen.
5. To play with a friend, send them the URL that is displayed on the final screen. You can also open this URL in a separate browser or tab.

You can also run the automated tests with `test` at the SBT prompt.

Every time you make any changes, to restart the webapp:

1. Close all browser tabs where you have started the client.
2. Stop the server by pressing `Ctrl+C` twice.
3. Do the steps above to restart the server and client.

## Implementation un-guide for Memory Matching

Do the same as above for the Memory Matching app! If you haven’t played the game before, [try it out](https://www.memozor.com/memory-games/for-kids/animals-on-colorful-background) for a few rounds. In our version, we want:

* 1 or more players who take turns trying to match tiles.
* After selecting two tiles, they are both shown for a few seconds, before being hidden again.

Hence, the rules are as follows:

* The board starts with two copies of each card in `CARDS`, shuffled.
* Players take turns according to the order of `userIds` given to `init`.
* On their turn, a player must first select two cards, then flip them to check whether they got the right pair. (this is unlike some version of memory where players start by flipping one card and then flip a second one after looking at the first one — in our version, they select one, then a second one without looking at the first, and finally they flip both at once.)
* If a player finds a match (the same card twice), they immediately take an additional turn (another guess). They continue taking turns like that until they make a mistake (flip a mismatched pair).

And some tips:

* Cards are represented are strings (emoji); a vector of cards is available in the CARDS variable:
   ```Scala
   // Use any strings you want here — the tests don't check for these specific emoji
    val CARDS: Vector[String] = DECKS("Birds")
    ```
    [apps/memory/jvm/src/main/scala/memory/MemoryLogic.scala](./apps/memory/jvm/src/main/scala/memory/MemoryLogic.scala)
    
    You can chose to use any of the existing decks, or use your own.
* Use `Random.shuffle` to shuffle a vector. Don’t forget that you need each card twice!
* There are useful building blocks for encoders and decoders in `lib/shared/shared/src/main/scala/cs214/webapp/Wires.scala`.


## Ungraded mini-project: make your own app!

After Tic-Tac-Toe and Memory Matching, your task in this last section of the exercise is to add a third app of your choice. There is no scaffolding, no instructions, and no tests. You are strongly recommended to attempt this part and build something fully functional on your own, even if it is very simple.

Some important notes:

* You can copy `tic-tac-toe` or `memory` as a starting point. You will need to update the `build.sbt` files: use `grep` or other search tools to find mentions of `ticTacToe` or `memory`, and use them as inspiration (best to do this in a clean clone, to avoid searching through the many files that SBT generates as part of the build process).
* You are encouraged to collaborate. You can work in teams, copy code from classmates, host your work on `github`, etc. You are strongly encouraged to use branches, perform code reviews of each other’s code, etc. But remember, don’t post solutions to the previous parts! (It’s best to start from a fresh clone to avoid posting solutions by accident.)
* We are available to help with this part as well.
* We’re excited to see what you come up with! If you think you have a cool app, please let everyone know, and add a link to your repo (there will be a dedicated thread, and there will definitely be bragging rights, but sorry, no bonus points).

Here are some ideas for apps you can build with this framework. They are not meant to be fully fleshed-out, but rather just suggestions for further creativity, in rough order of complexity:

* **Q&A queue** for online exercise sessions: students connect and get placed in a queue to get their questions answered. Projections are used to make sure that students see only the queue length, whereas assistants see names as well.
* **TODO/Notes app** for group project: everyone can add and delete entries to a shared list. The list persists if you close your browser tab and open it again, because all the state is on the server.
* **Raise-hands** feedback system for courses: students can use emoji to indicate their understanding, the prof sees the breakdown.
* **Chat room system**, with possible extensions to private chats, threads, linked replies.
* **Lobby** meta-application: takes an existing state machine and wraps it to have a UI to add clients dynamically until there are enough users/players to start.
* **Classroom booking system/Ticket-reservation system** for on-campus events.
* **Chat room**
* **Vocabulary practice app**, with flashcards and spaced repetition.
* **Authentication meta-app**: takes an existing state machine and wraps it to force users to provide a security token before connecting.
* **Bill-splitting app** (simple): someone inputs the prices of dishes, then everyone taps what they ate. Extension: someone uploads a picture of the bill, then the server does OCR to identify items and prices, and everyone taps what they ate.
* **Online voting system** for non-critical elections.
* **Photo gallery** with real-time updating comments.
* **Collaborative painting app** (think multi-user paint, or r/place)
* **Board games**! Checkers, Chess, Connect 4, Monopoly, Battleship, Reversi, …
* **Card games**! SET, Belote, Jass, Tarot, Président, …
* etc, etc, etc… surprise us!

As an alternative, if you’re more interested in devops, reliability, and security than frontend and backend dev, you could design a mini-project to harden and industrialize the lab’s scaffold code (in `lib/`):

* Add error handling to avoid server crashes
* Add automatic reconnection when the connection drops
* Implement long polling as a fallback mechanism when websockets are blocked or malfunction
* Serialize the state to disk to support shutdown and restart without losing app state
* Eliminate unnecessary libraries: port from htt4s to cask, merge the websocket server into the main HTTP server
* Set up a repo with CI
* Add integration tests, either with a simulated HTTP client or with Selenium
* Deploy to a VM and host on a publicly-accessible server

