# Programming 2 - Exercise 12: Huffman Codes and Structuring Software Projects

## "We'll get back to that later" is now

In exercise 4, you were instructed to start a project from scratch. this time we'll once again start another project, but now also take the time to explain everything in detail.

### Starting a simple project from scratch

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

As part of your build definition you specify the version of sbt that your build uses. This allows people with different versions of the sbt launcher to build the same projects with consistent results.

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

This topic is rather complex but you can read about what it is and how to use it in the [sbt doumentation](https://www.scala-sbt.org/1.x/docs/Organizing-Build.html)

#### README.md

A README is a Markdown file that usually encapsulates important information about the provided code. README files typically include information on:

- What the project does
- Why the project is useful
- How users can get started with the project
- Where users can get help with your project
- Who maintains and contributes to the project

When taking a look at the README file you generated in the beginning, you can read about all these topics.

If you have any important information about your code which should not be part of the code itself as a comment, do not hesitate to write everything into the README file. Using the Markdown format, you have various very easy options to modify, structure or format your text. A quick oversight over all functions of Markdown offers the [cheatsheet](https://www.markdownguide.org/cheat-sheet/). If you need a previw of how your README will look, klick the "Open Preview to the Side" button (book with magnifying glasses) on the top right.

^Explain all folders/files in detail, What is it for? What can you change?

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

* Huffman codes are hand-tailored to the text they encode, while ASCII coding is immutable: The ASCII code of `a` will always be `01100001`, while the Huffman code of `a` will be determined based on the text we wish to encode. Technically, Huffman coding is a method of obtaining a coding. 
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

Check yourself: What is the Huffman code for the symbol `d` in the below tree? 

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

Check yourself: Given the Huffman code tree of the last check, what does the sequence of bits `10001010` correpond to?

<details>
<summary> Solution </summary> 

`bac`

</details><br/>

If you practiced Huffman coding with a friend or a group of friends, each of you should have constructed a Huffman tree. Now, take the tree your friend made and encode a message!

### Suitable Types of Symbols

Huffman coding can handle different types of symbols. The previous examples shows that a symbol can be a letter.

#### Image Compression

We can compress an image where each symbol is a color represented as a RGB tuple (Red, Green, Blue). Each symbol can be something like (255, 0, 0) for pure red, (0, 255, 0) for pure green, etc.

#### DNA Sequences

DNA sequences are composed of four primary nucleotides: Adenine (A), Cytosine (C), Guanine (G), and Thymine (T). Besides A, C, G, T, common codons like ATG or TAA can also be treated as individual symbols to make the compression more efficient.

#### Binary Data

For files that aren’t text-based, such as executables, Huffman coding can operate on bytes (sequences of 8 bits) or even larger chunks of bits.

In general, any data that can be broken down into discrete, countable units can be compressed using Huffman coding. This includes things mentioned in above examples or sensor readings, log entries, and more.

## Implementation

Julie found the source files including solutions outside of gitlab here: https://github.com/userdarius/software-construction-epfl/tree/main/labs/huffman-coding. 

This section should roughly follow the "Implementation Guide" section from EPFL, although our approach needs to be different since the students won't be working with a skeleton. We have to add explanations for the following:

* What functions do I need and why? (can maybe be moved to the Huffman Codes section)
* What do I put into which file?
* Maybe try to divide into sections that each can stand on its own, so students don't have to finish everything to see their code in action?As already mentioned, you will implement this project mostly from scratch. Don't worry, we will help you!

Take a moment to think about all the features that could be implemented, and the supporting functions, objects and data structures you need.

Done? Here's what we'll do together this week. Of course, you can always add more!

* Implementation of Huffman trees
* Write a function that constructs a Huffman tree based on an input text
* Decoding
* Encoding

## Finalizing the project 

More stuff: 

* The EPFL scaffold code contains a lot of supplementary functions that tie everything together and are not explained in the exercise. Having the students write everything themselves would be too much work for one week. We could either supply those functions for them to copy and paste, or try to rewrite the exercise such that we don't need them. Best might be a combination of both: Supply the functions that allow students to process larger files, but omit the fancy user interface.
* EPFL uses a custom .huf format for encoded files. Maybe scrap and replace this (I tried to open a .huf file in notepad++ and it did NOT look pretty)
* building on exercise 11: Testing, pre- and postconditions.






