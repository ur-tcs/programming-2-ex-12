# Programming 2 - Exercise 12: Huffman Codes and Structuring Software Projects

This week's exercise will revisit many concepts that were introduced earlier in the course. The main goal of the project is to implement **Huffman codes.**

The most important parts of the exercise are marked with ⭐️. Exercises that are particularly challenging are marked with 🔥. Make sure to reach the last task, as it is marked with both. ⭐️🔥

**You are allowed to copy/clone/fork this repository, but not to share solutions of the exercise in any public repository or web page.**

## Starting a simple project from scratch

The following section explains every step needed for setting up a basic project. You can either use the Metals extention or the terminal. If you are using different IDEs, the second way will propably also work with the integrated terminals in these.

1. Metals extention
  * Create a new folder for your project and give it a fitting name.
  * Start VSCode and click on the Metals icon in the Activity Bar.
  * Among the build commands that are now displayed in the Side Bar, select "New Scala Project".
  * You will now be offered a bunch of different templates. Choose `scala/scala3.g8`, since `scala/hello-world.g8` will not be in Scala 3! 
  * Next, select the folder you created in step 1.
  * Now, you're asked to choose a name for your project. If you don't feel like making decisions today, name it "huffman".
  * Done! VSCode will ask whether you want to open your project in a new window and whether you want to import the build. Then, you only need to wait a little while your new project is set up for you.
  * Note: If importing the build using the button in the popup or in the Metals menu takes forever, try importing the build by starting sbt in the terminal.   Using the ```cd <name of project folder>``` command, you can navigate to the right folder and via starting sbt using the ```sbt``` command, the build is automatically imported.

2. Terminal
  * Create a new folder for your project and give it a fitting name.
  * Start VSCode, open the new folder and show the terminal using the "Toggle-Panel" button on the top right or by pressing ```Ctrl+J```
  * Run the command ```sbt new scala/scala3.g8```
  * Now, you're asked to choose a name for your project. If you don't feel like making decisions today, name it "huffman".
  * Done! You can now see the newly created project folder in the file explorer.
  * Using the ```cd <name of project folder>``` command, you can now navigate to the right folder and via starting sbt using the ```sbt``` command, the build is automatically imported.

### What did we just create and what purpose does it serve?

After following the steps above, your explorer should display the project folder. The folder should contain the following files: (In case you failed to set up the project, we added the basic project in this GitHub repository)

```.
├── project
│   ├── target
│   │   └── ...
│   └── build.properties
├── src
│   ├── main
│   │   └── main.scala
│   └── test
│       └── MySuite.scala
├── target
│   ├── global-logging
│   │   └── ...
│   └── task-temp-directory
│       └── ...
├── build.sbt
└── README.md
``` 

#### src

In this folder, all code you are writing is stored. It contains the main folder and the test folder.

  1. /main
    
  This folder contains all files with the actual code of your project. At the start, there is only the ```Main.scala``` file. The file named "Main" is usually the starting point when you run your code. As you might have noticed from earlier exercises, the amount of ```.scala``` files in this folder can increase quite rapidly, so if needed sort your files in fittingly named folders for better oversight. 

  2. /test
    
  This folder contains all files with automated tests for your project. At the start, it only contains a short test that always succeeds. You have propably seen and used such tests in earlier exercises. Later in this exercise you will learn to write these tests yourself (if you haven't written some yourself already). You do not need to write all tests into one file, you can spread them over multiple separate files.

#### target

Generated files (compiled classes, packaged jars, managed files, caches, and documentation) will be written to the target directory by default. You can take a look at all included files, but you will propably not need to change any of these files right now.

#### build.sbt

```scala
val scala3Version = "3.4.2"
```

As part of your build definition, you specify the Scala version to be used in your build. sbt will take care of downloading and installing this version of the compiler. This allows people with different system configurations to build the same projects with consistent results.

```scala
lazy val root = project
  .in(file("."))
  .settings(
    name := "huffman",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test
  )
```

In the second part, there are various other settings for your project, including the name of the project folder, the build version or any library dependencies. For a detailed explaination, read the [sbt documentation](https://www.scala-sbt.org/1.x/docs/Basic-Def.html)

#### project

build.sbt conceals how sbt really works. sbt builds are defined with Scala code. That code, itself, has to be built. What better way than with sbt?

The project directory is another build inside your build, which knows how to build your build. Your build definition is an sbt project.

This topic is rather complex but you can read about what it is and how to use it in the [sbt documentation](https://www.scala-sbt.org/1.x/docs/Organizing-Build.html)

#### README.md

A README is a Markdown file that usually encapsulates important information about the provided code. README files typically include information on:

- What the project does
- Why the project is useful
- How users can get started with the project
- Where users can get help with your project
- Who maintains and contributes to the project

When taking a look at the README file you generated in the beginning, you can read about all these topics.

If you have any important information about your code which should not be part of the code itself as a comment, do not hesitate to write everything into the README file. Using the Markdown format, you have various very easy options to modify, structure or format your text. A quick oversight over all functions of Markdown offers the [cheatsheet](https://www.markdownguide.org/cheat-sheet/). If you need a previw of how your README will look, klick the "Open Preview to the Side" button (book with magnifying glasses) on the top right.

### Running your project

You read about what these files are, now let's quickly rehearse all important steps for running your program.

#### Starting sbt

Before being able to run your code, you must start the sbt in the right folder as mentioned before.

Using the ```cd <path to project folder>``` command, navigate to the right folder. Then you start sbt using the ```sbt``` command. If you encounter a warning message telling you about missing directories, check if you started sbt from the right folder.
If you want to stop sbt because you want to restart or for other reasons, simply run ```exit```.

To compile your code, simply type ```compile``` and for running your code type ```run```.
To run all available tests, run ```test```. 

Be aware that running ```run``` or ```test``` will automatically compile the code and you do not need to take this step manually.

In case you want to test only a specific function of your code, simply type ```testOnly -- "*<name of function>*"```

For further run commands check out the [sbt documentation](https://www.scala-sbt.org/1.x/docs/Running.html) and for further test commands also check out the [sbt documentation](https://www.scala-sbt.org/1.x/docs/Testing.html)

## Theory of Huffman codes

Tody's exercise is about Huffman coding. Huffman coding is a lossless (i.e., keeping all information) compression algorithm that can be used to compress lists of symbols. It’s widely used in data compression tasks such as file archiving. For example, huffman coding is used in [Gzip](https://en.wikipedia.org/wiki/Gzip).

### Idea

Imagine you want represent the example string `aaaaabaacdac` in binary, using the symbols `0` and `1`. One of the most common ways to do this is [extended ASCII coding](https://www.ascii-code.com/), which may be the most-used 8 character encoding in the world. In extended ASCII, each symbol is represented by a sequence of 8 bits, meaning that 2^8 = 256 distinct characters and symbols can be represented. 

>[!NOTE]
> We will talk of a "code" when referring to the sequence of bits that represents a concrete symbol or string of symbols. The entire set of encoding rules will be called a "coding". In other words, we apply the coding to a string to obtain a code.

The ASCII code for the example string `aaaaabaacdac` looks like this:

```
01100001 01100001 01100001 01100001 
01100001 01100010 01100001 01100001 
01100011 01100100 01100001 01100011 
``` 

In ASCII, every symbol is encoded using 8 bits, so the encoded text has 8 * 12 = 96 bits. That's a lot!

There are several ways to obtain a shorter compression algorithm. Huffman coding utilizes the following principles: 

* Huffman codes are tailored to the text they encode, while ASCII coding is fixed: The ASCII code of `a` will always be `01100001`, while the Huffman code of `a` will be determined based on the text we wish to encode. Technically, Huffman coding is a method of obtaining a coding. 
* This allows us to use shorter codes: The string we wish to encode only contains 4 distinct symbols. We do not need 8 bits to represent each symbol. In the ASCII example, the bit representations of the letters only differ in the last three digits, so the first 5 bits could be omitted entirely and the encoding would still work.
* The bit representations of different letters do not need to all be the same length. The letter `a` occurrs a lot more frequently than `b`, `c` or `d`. The compressed text will be shorter if the symbols that often appear in a text are represented by a shorter bit sequence than those being used more rarely. 
* Since symbols can be encoded using different numbers of bits, we need a way to avoid ambiguity. For example, if the letter `a` were encoded `0` and the letter `b` were encoded `00`, then the code `00` could represent both `aa` and `b`. In Huffman coding, this is ensured by the *prefix property*: No Huffman code of a symbol can be the prefix of the Huffman code of another symbol. This also allows us to decode a string from left-to-right without backtracking.

A Huffman encoding of `aaaaabaacdac` could look like this: 

| Symbol | Frequency | Huffman Code |
| -------|-----------|--------------|
| a | 8 | 0|
| b | 1 | 110 |
| c | 2 | 10 |
| d | 1 | 111 |

For `aaaaabaacdac`, we obtain the code ` 0 0 0 0 0 110 0 0 10 111 0 10 `. Instead of 96 bits, we only need 18!

We will now see how to obtain a Huffman encoding for a given text.

### Constructing Huffman Code Trees

Huffman codings are represented by a code tree, which is a binary tree with leaves representing the symbols. The tree is constructed using the following steps: 

1. Count the number of appearances of each symbol in the text.
2. Create a forest of unconnected leaf nodes. Each leaf represents a symbol and is associated with a weight representing the frequency of appearances of that symbol. In the `aaaaabaacdac` example, this forest looks like this: 
```
a(8)   b(1)   c(2)   d(1) 
```

3. While there’s more than one code tree, merge the two trees with the lowest root weight into a new tree by creating a new branching node. A branching node can be thought of as a set containing the symbols present in the leaves below it, with its weight being the total weight of those leaves. 

```
               {b,d}(2)
                /    \
a(8)   c(2)   b(1)   d(1)
```

```
    {b,c,d}(4)
     /      \
   c(2)   {b,d}(2)
           /     \
a(8)     b(1)   d(1) 
```

```
 {a,b,c,d}(12)
   /       \
a(8)   {b,c,d}(4)
        /      \
      c(2)   {b,d}(2)
              /     \
            b(1)   d(1) 
```

### Encoding 

For a given Huffman tree, one can obtain the code of a symbol by traversing from the root of the tree to the leaf containing the symbol. Along the way, when a left branch is chosen, a `0` is added to the code, and when a right branch is chosen, `1` is added to the code.

⭐️ Check yourself: What is the Huffman code for the symbol `d` in the below tree? 

```
{a,b,c,d,e,f,g,h}(17)
  /             \
a(8)        {b,c,d,e,f,g,h}(9)
           /                 \
      {bcd}(5)              {efgh}(4)
         /    \              /    \
      b(3)   {c,d}(2)   {e,f}(2)   {g,h}(2) 
            /   \        /   \       /   \   
          c(1) d(1)    e(1) f(1)   g(1) h(1)
```

<details>
<summary> Solution </summary> 

`1011`

</details><br/>

Practice Huffman coding together with a friend: Have each of you write down a string of letters. Then, take your friend's string and construct a corresponding Huffman tree! More distinct letters mean more work, so try to aim for 6 to 10 distinct letters.

### Decoding 

Decoding also starts at the root of the tree. Given a sequence of bits to decode, we successively read the bits, and for each 0, we choose the left branch, and for each 1 we choose the right branch.

When we reach a leaf, we decode the corresponding symbol and then start again at the root of the tree.

⭐️ Check yourself: Given the Huffman code tree of the last check, what does the sequence of bits `10001010` correpond to?

<details>
<summary> Solution </summary> 

`bac`

</details><br/>

If you practiced Huffman coding with a friend or a group of friends, each of you should have constructed a Huffman tree. Now, take the tree your friend made and encode a message!

### Suitable Types of Symbols

Huffman coding can handle different types of symbols. The previous examples show that a symbol can be a letter. Here are a few more examples in which Huffman coding can be applied. Don't worry, this week's exercise will only deal with "normal" characters.

#### Image Compression

We can compress an image where each symbol is a color represented as a RGB tuple (Red, Green, Blue). Each symbol can be something like (255, 0, 0) for pure red, (0, 255, 0) for pure green, etc.

#### DNA Sequences

DNA sequences are composed of four primary nucleotides: Adenine (A), Cytosine (C), Guanine (G), and Thymine (T). Besides A, C, G, T, common codons like ATG or TAA can also be treated as individual symbols to make the compression more efficient.

#### Binary Data

For files that aren’t text-based, such as executables, Huffman coding can operate on bytes (sequences of 8 bits) or even larger chunks of bits.

In general, any data that can be broken down into discrete, countable units can be compressed using Huffman coding. This includes things mentioned in above examples or sensor readings, log entries, and more.

## Implementation

This week, you will implement your project mostly from scratch. In the first section of this exercise, you have created a new project that already came with some basic infrastructure, such as a build.sbt file and a reasonable folder structure. We will now build on this. This section heavily revisits the contents of the second lecture. If you get stuck, we recommend having a look at the lecture slides!

At first, we will only focus on the theory of Huffman coding that you have just been introduced to: 

* Implementation of Huffman trees
* Write a function that constructs a Huffman tree based on an input text
* Decoding
* Encoding

You can always add more later. 

### Implementation of Huffman trees ⭐️

Come up with an `abstract class` `CodeTree[T]` that represents Huffman trees in Scala. Remember:

* Huffman trees are binary, meaning each node has at most two children.
* Each node, both leaf nodes and inner nodes, has a weight.
* Each leaf is associated with a symbol, while branching nodes are associated with sets or lists of symbols.
* We use a type parameter `[T]` so our definition is not limited to one type.

<details>
<summary> Solution </summary> 

```Scala
abstract class CodeTree[T]
case class Leaf[T](symbol: T, weight: Int) extends CodeTree[T]
case class Fork[T](left: CodeTree[T], right: CodeTree[T], symbols: List[T], weight: Int) extends CodeTree[T]
```

</details><br/>

You also need to decide *where* to put the definition. Of course you could put everything into the `main.scala` file, but that is messy and poor programming practice. Therefore, we recommend you create one or more separate folders to structure your work. We leave this to you. 

<details>
<summary> Hint </summary> 

One of the easiest ways to manage multiple folders, and a method that has been employed in most exercises throughout this semester, is to put the contents of every folder into the same `package`. Your folder structure could look like this:

```.
├── src
│   ├── main
│   │   └── main.scala
│   │   └── HuffImpl.scala
│   └── test
│       └── MySuite.scala
```

Put the command `package huffman` at the top of both `main.scala` and `HuffImpl.scala`, where `huffman` is the name you can give your package.

</details><br/>

Time to define some useful functions! Before you can start implementing, you should think about class hierarchy. We want the following functions to work on every data type, as indicated by `[T]`. Therefore, all following functions should be members of a trait `HuffmanImpl[T]`.

First, you are going to define the functions `weight`, which returns the weight of a Huffman tree's root node, and `symbols`, which returns a list of the symbols defined in a given Huffman tree.

```Scala
trait HuffmanImpl[T]:

  def weight(tree: CodeTree[T]): Int =
    ???

  def symbols(tree: CodeTree[T]): List[T] =
    ???
```

Using `weight` and `symbols`, you can define the function `makeCodeTree` which takes the left and right subtrees, then builds a new tree by adding a parent node on top of these two trees:

```Scala
def makeCodeTree(left: CodeTree[T], right: CodeTree[T]): CodeTree[T] =
  ???
```

Remember that `makeCodeTree` should be a member of `HuffmanImpl[T]`, so we recommend putting the code right after the definition of `symbols` using the same indentation.

<details>
<summary> Solution </summary> 

```Scala
def makeCodeTree(left: CodeTree[T], right: CodeTree[T]): CodeTree[T] =
  Fork(left, right, symbols(left) ++ symbols(right), weight(left) + weight(right))
```

</details><br/>

Now, how do you test your work? At this point, the data type `T` is not specified. If you want to make a tree where the symbols correspond to `Char`s, you need to define a new trait `HuffmanChar` that inherits from `Huffman[Char]`. Then, define `HuffmanChar`'s companion object. 

```Scala
trait HuffmanChar extends HuffmanImpl[Char]

object HuffmanChar extends HuffmanChar
```

Now, you can apply your defined functions! 

`makeCodeTree` automatically calculates the list of symbols and the weight when creating a node. Therefore, code trees can be constructed manually in the following way:

```Scala
import HuffmanChar.*

val sampleTree = makeCodeTree(
  makeCodeTree(Leaf('x', 1), Leaf('e', 1)),
  Leaf('t', 2)
)
```

Which brings us to...

### Testing ⭐️

If you created your project following our instructions, a folder and file `MySuite.scala` have been automatically generated. The contents of `MySuite.scala` should look like this:

```Scala
// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class MySuite extends munit.FunSuite {
  test("example test that succeeds") {
    val obtained = 42
    val expected = 42
    assertEquals(obtained, expected)
  }
}
```

MUnit is a Scala testing library that is already included in your `build.sbt` file.

You will now add tests on your own! We will start with `weight`. You can write the tests into the existing file `MySuite.scala`, or create a new file in the same folder. Have a look at the following code:

```Scala
package huffman

class HuffmanCharTest extends munit.FunSuite:

  import HuffmanChar.*

  test("weight: weight of a simple leaf"):
    assertEquals(weight(Leaf('c', 29)), 29)
```
<div style="text-align: right; color:grey"> 

[huffman/src/test/scala/simpleTest.scala](./huffman/src/test/scala/simpleTest.scala)
</div>

You can copy this code fragment or download the respective file from this repository. Then, you can run the test by simply typing `test` in sbt or, if you only want to test the `weight` function and not the old `MySuite` test, use the command `testOnly -- "*weight*"`.

The test itself is fairly simple: In `test()`, you write the description of the test. The `testOnly` command will look for keywords within this string, not within the "body" of the test. So make sure to include a meaningful description!

The following line assures that the weight of `Leaf('c', 29)`, a Huffman tree that only consists of one leaf node with weight 29, is correctly calculated.

Task: Construct a larger Huffman tree to use in the following tests. Then, use that tree to write more elaborate tests for `weight` and `symbols`.

<details>
<summary> Hint </summary> 

Create an object `TestTrees` that contains the tree, and to which you can add more useful test subjects (for example, texts to decode) later. Your file could start like this:

```Scala
package huffman

class HuffmanCharTest extends munit.FunSuite:

  import HuffmanChar.*
  object TestTrees:
    val t2 = Fork(Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5), Leaf('d',4), List('a','b','d'), 9)
    // put more members here when needed later


  import TestTrees.*

  test("weight: weight of a simple leaf"):
    assertEquals(weight(Leaf('c', 29)), 29)

  test("weight: weight of a larger tree"):
    ???
```

</details><br/>

In the following tasks, we will not dictate which tests you have to implement. Instead, you'll have to come up with meaningful tests on your own. Two approaches that are usually a good idea:

* Test whether the function handles "minimal" input correctly, such as empty lists.
* Test if the function works correctly on an example that you calculated manually.

### Constructing Huffman code trees ⭐️

Hey, didn't we just define a function that constructs trees? Why the caption?

Because `makeCodeTree` still requires you to build the tree manually. We want to create a function `createCodeTree` that takes an input text (in the form of a list) and automatically identifies the symbols, calculates the weights, creates a forest of leaf nodes and then iteratively builds a connected tree.

That is a lot of steps, so you'll need a lot of supporting functions. Take a moment about which parts of the process should be isolated and turned into separate functions. What do the functions take as input and what should the output look like? Based on our existing class hierarchy, where do you put the functions?

When you are finished thinking about this, you can continue. We will walk through one possible solution step by step, but of course you are free to come up an approach of your own.

1. Define a function `symbolFreqs` which calculates the frequency of each symbol in the text.

```Scala
def symbolFreqs(symbols: List[T]): List[(T, Int)] =
  ???
```

<details>
<summary> Hint </summary> 

Look into the `groupBy` method for lists.

</details><br/>

2. Write a function `makeOrderedLeafList` which generates a list containing all the leaves of the Huffman code tree to be constructed (the case `Leaf[T]` of the algebraic datatype `CodeTree[T]`). The list should be ordered by ascending weights where the weight of a leaf is the number of times (i.e., the frequency) it appears in the given text.

```Scala
def makeOrderedLeafList(freqs: List[(T, Int)]): List[Leaf[T]] =
  ???
```

3. Write a simple function `isSingleton` which checks whether a list of code trees contains only one single tree.

```Scala
def isSingleton(trees: List[CodeTree[T]]): Boolean =
  ???
```

4. Write a function `combine` which
   1. removes the two trees with the lowest weight from the list constructed in the previous step, and
   2. merges them by creating a new node of type `Fork[T]`. Add this new tree to the list - which is now one element shorter - while preserving the order (by weight).

```Scala
def combine(trees: List[CodeTree[T]]): List[CodeTree[T]] =
  ???
```

5. Write a function `until` which calls the `isSingleton` and `combine` until this list contains only a single tree. This tree is the final coding tree. The function `until` can be used in the following way:

```Scala
until(isSingleton, combine)(trees)
```

where `isSingleton` and `combine` refer to the two functions defined above.

```Scala
def until(
    isDone: List[CodeTree[T]] => Boolean,
    merge: List[CodeTree[T]] => List[CodeTree[T]]
)(trees: List[CodeTree[T]]): List[CodeTree[T]] =
  ???
```

6. Finally, use the functions defined above to implement the function `createCodeTree`.

```Scala
def createCodeTree(symbols: List[T]): CodeTree[T] =
  ???
```

7. Write tests for all of your functions!

### Decoding ⭐️

Now, it is time to write functions for decoding. We will again split up the process. The encoded messages will be represented by lists of integers. For example, the sequence `10001010` will be represented by `list(1,0,0,0,1,0,1,0)`.

First, define a function `decodeOne` which decodes one symbol from a bit sequence using the given code tree. The function returns an option. If succeeding, it returns `Some(sym, remainingBits)`, where `sym` is the decoded symbol and `remainingBits` is the rest of the bit sequence that remains unused. Otherwise, return `None` if it fails to decode.

```Scala
def decodeOne(tree: CodeTree[T], bits: List[Int]): Option[(T, List[Int])] =
  ???
```

Using `decodeOne`, now we can define the function `decode` which decodes a list of bits (which were already encoded using a Huffman code tree), given the corresponding code tree.

```Scala
def decode(tree: CodeTree[T], bits: List[Int]): List[T] =
  ???
``` 

### Encoding

This section deals with the Huffman encoding of a sequence of symbols into a sequence of bits.

#### …Using a Huffman Tree ⭐️

Define the function `encode` which encodes a list of characters using Huffman coding, given a code tree.

```Scala
def encode(tree: CodeTree[T])(text: List[T]): List[Int] =
  ???
```

Your implementation must traverse the coding tree for each symbol, which is a task that should be done using a helper function.

<details>
<summary> Hint </summary> 

`flatMap` may be useful here! 

</details><br/>

#### …Using a Coding Table 🔥

The previous function is simple, but very inefficient. The goal is now to define `quickEncode` which encodes an equivalent representation, but more efficiently.

```Scala
def quickEncode(tree: CodeTree[T])(text: List[T]): List[Int] =
  ???
```

Your implementation will build a coding table once which, for each possible symbol, gives the list of bits of its code. The simplest way - but not the most efficient - is to encode the table of symbols as a list of pairs.

```Scala
type CodeTable = List[(T, List[Int])]
```

The encoding must then be done by accessing the table, via a function `codeBits` which returns the bit sequence representing the given symbol in the code table:

```Scala
def codeBits(table: CodeTable)(symbol: T): List[Int] =
  ???
```

The creation of the table is defined by `convert` which traverses the coding tree and constructs the character table, using the `mergeCodeTables` defined below:

```Scala
def convert(tree: CodeTree[T]): CodeTable =
  ???
```

<details>
<summary> Hint </summary> 

Think of a recursive solution: Every subtree of the code tree tree is itself a valid code tree that can be represented as a code table. Using the code tables of the subtrees, think of how to build the code table for the entire tree (that’s why we need `mergeCodeTables`).

</details><br/>

```Scala
def mergeCodeTables(a: CodeTable, b: CodeTable): CodeTable =
  ???
```

Now we can implement `quickEncode` using `convert`.

### Bonus: Decoding & Encoding into a file

In prior tasks you already established many functions, but now it is time for you to try it yourself. This task is still a bit above your knowledge level, so you do not need to understand all provided code, this task is merely for you to explore further possibilities if you want to.

The following code offers the functionality to Encode and Decode txt files.

```scala
package huffman

import huffman.SerializationUtils.{serialize, deserialize, intToByteArray}
import java.nio.file.{Files, Path, Paths}
import java.nio.file.StandardOpenOption.APPEND
import java.nio.ByteBuffer

def bitsToBytes(bits: List[Int]): List[Byte] =
    bits.grouped(8).toList.map { group =>
      val byte = group.foldLeft(0) { (acc, bit) => (acc << 1) | bit }.toByte
      if group.size < 8 then
        (byte << (8 - group.size)).toByte
      else byte
    }

def encodeFile(filePath: String): Unit =
  val path = Paths.get(filePath)
  val filenameNoExt = path.getFileName.toString.split("\\.").head
  val hufPath = Paths.get(path.getParent.toString, s"$filenameNoExt.huf")
  val textBytes = Files.readAllBytes(path)
  val text = new String(textBytes).toList
  val codeTree = HuffmanChar.createCodeTree(text)
  val n = serialize(codeTree, hufPath)
  val encodedBits = HuffmanChar.quickEncode(codeTree)(text)
  val encodedBytes = bitsToBytes(encodedBits)
  Files.write(hufPath, intToByteArray(encodedBits.size), APPEND)
  Files.write(hufPath, intToByteArray(encodedBytes.size), APPEND)
  Files.write(hufPath, encodedBytes.toArray, APPEND)
  val compressRatio = textBytes.size.toDouble / (12 + n + encodedBytes.size)
  println(s"Compression Finished.")
  println(s"Compression Ratio (Original Size / Compressed Size): ${(compressRatio * 100).round / 100.toDouble}")

def decodeFile(filePath: String): Unit =
  def getCodeTree(buffer: ByteBuffer): CodeTree[Char] =
    val n = buffer.getInt()
    val bytes = new Array[Byte](n)
    buffer.get(bytes)
    deserialize(bytes)
  val path = Paths.get(filePath)
  val filenameNoExt = path.getFileName.toString.split("\\.").head
  val buffer = ByteBuffer.wrap(Files.readAllBytes(path))
  val codeTree = getCodeTree(buffer)
  val nBit = buffer.getInt()
  val nByte = buffer.getInt()
  val encodedBytes = new Array[Byte](nByte)
  buffer.get(encodedBytes)
  val encodedBits: List[Int] =
    encodedBytes.flatMap { byte => (7 to 0 by -1).map { i => (byte >> i) & 1 } }.toList.take(nBit)
  val decodedBytes = HuffmanChar.decode(codeTree, encodedBits).mkString.getBytes
  Files.write(Paths.get(path.getParent.toString, s"$filenameNoExt-decoded.txt"), decodedBytes.toArray)
```
Put above code into your `Main.scala`.

And put the code below into a new file `SerializationUtils.scala` in your `main` folder

```scala
package huffman

import huffman.{CodeTree, Leaf, Fork}
import upickle.default.*
import java.nio.file.{Files, Path, Paths}
import java.nio.file.StandardOpenOption.APPEND

object SerializationUtils:

  sealed trait SerialCodeTree derives ReadWriter
  case class SerialLeaf(char: Char, weight: Int) extends SerialCodeTree
  case class SerialFork(left: SerialCodeTree, right: SerialCodeTree, chars: List[Char], weight: Int)
      extends SerialCodeTree

  def intToByteArray(v: Int): Array[Byte] = Array(
    (v >>> 24).toByte,
    (v >>> 16).toByte,
    (v >>> 8).toByte,
    v.toByte
  )

  def convertToSerial(tree: CodeTree[Char]): SerialCodeTree = tree match
    case Leaf(c, w)               => SerialLeaf(c, w)
    case Fork(left, right, cs, w) => SerialFork(convertToSerial(left), convertToSerial(right), cs, w)

  def convertFromSerial(tree: SerialCodeTree): CodeTree[Char] = tree match
    case SerialLeaf(c, w)               => Leaf(c, w)
    case SerialFork(left, right, cs, w) => Fork(convertFromSerial(left), convertFromSerial(right), cs, w)

  def serialize(tree: CodeTree[Char], path: Path): Int =
    val json = write(convertToSerial(tree)).getBytes
    Files.write(path, intToByteArray(json.size))
    Files.write(path, json, APPEND)
    json.size

  def deserialize(bytes: Array[Byte]): CodeTree[Char] =
    val json = new String(bytes)
    convertFromSerial(read[SerialCodeTree](json))

```

You also have to edit the `build.sbt` file to include a specific library dependency. therefore just add `libraryDependencies += "com.lihaoyi" %% "upickle" % "3.1.2"` into the file, so it looks like this:

```scala
val scala3Version = "3.4.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "huffman",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "com.lihaoyi" %% "upickle" % "3.1.2",
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test
  )
```

After changing the `build.sbt` file, in case you had `sbt` still running from previous tasks, make sure that you restart `sbt` to apply the changes.

For this to be more orderly, create a new folder `assets`, as a place to store all files needed for and created from Encoding and Decoding. For example, place a new file called `test.txt` there, and write in some text you want to test, for example "Hello, functional programming is fun!" and compress it using `encodeFile()`. The generated code tree and encoded bit sequences are stored in a `.huf` file, in our example `test.huf`.

You’ll find the outputed “Compression Ratio” (`original text size` divided by `compressed size`) is very small. It’s because `test.txt` is very small, and the code tree and encoded text is relatively large.

Use `decodeFile()` to again decode the compressed `test.huf` files. A file `test-decoded.txt` will be generated, storing the decoded text. Compare `test.txt` and `test-decoded.txt` to see if they’re identical.

### Bonus: Overflow error

You might have stack overflow error when trying to pass very big files. 

If you’ve encountered this issue, do you know why the `decode` function leads to stack overflow?

<details>
<summary> Hint </summary> 

The reason is likely that your `decode` function is not tail recursive.

In a standard recursive function, each recursive call adds a new layer to the call stack. If the recursion is deep, this can quickly consume all the available stack space, leading to a stack overflow.

Tail recursion, on the other hand, is a form of recursion where the recursive call is the last operation in the function, allowing the Scala compiler to optimize it and use a constant amount of stack space.
</details><br/>

## Burrows-Wheeler transform ⭐️🔥

Actual compression tools are not based on Huffman code alone, as this would not result in very good compression rates. The popular [bzip2](https://en.wikipedia.org/wiki/Bzip2) tool, for instance, applies several stages of compression, one of which are Huffman codes. Another stage used in bzip2 is the [Burrows-Wheeler transform (BWT)](https://en.wikipedia.org/wiki/Burrows%E2%80%93Wheeler_transform), which is a remarkably elegant (and invertible) way of permuting the characters in a string. Using BWT in the right way, we can make the Huffman encoding much more effective.

Your task is simple: Implement the BWT, either by adding it to your Huffman project or by creating a new project the way you were shown. There is plenty of documentation online (researching things on your own is an important skill for every programmer, so we intentionally don't give you any more guidance than this). You are on your own now, have fun!
