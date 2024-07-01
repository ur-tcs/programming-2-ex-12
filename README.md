# Programming 2 - Exercise 12: Huffman Codes and Structuring Software Projects

## "We'll get back to that later" is now

In exercise 4, students were instructed to start a project from scratch. This will be the foundation for the first section. We'll start another project, but this time take the time to explain everything in detail.

### Starting a simple project from scratch

(this part is copied straight from exercise 4)

VSCode and Metals will actually do most of the work for you. A quick glance at https://code.visualstudio.com/api/ux-guidelines/overview can be helpful for navigating the user interface as you follow the instructions below.

* Create a new folder for your project. If you manage your exercises as recommended in exercise 0, this means creating a folder named `programming-2-ex-12` in the parent folder `ProgrammingII`.
* Start VSCode and click on the Metals icon in the Activity Bar.
* Among the build commands that are now displayed in the Side Bar, select "New Scala Project".
* You will now be offered a bunch of different templates. Choose `scala/scala3.g8`, since `scala/hello-world.g8` will not be in Scala 3! 
* Next, select the folder you created in step 1.
* Now, you're asked to choose a name for your project. If you don't feel like making decisions today, name it "huffman".
* Done! VSCode will ask whether you want to open your project in a new window and whether you want to import the build. Then, you only need to wait a little while your new project is set up for you. 

These instructions contain a URL to a webpage explaining the VSCode UI. Maybe we should expand on this and reference more resources that explain how VSCode works, since it was a suggestion in the evaluation. 

Now, we should explain **all of the files and the folder structure** of the new project. New files are added after importing the build and compiling, those should be explained as well. Unimportant stuff that is not the focus of the lecture only needs to be explained very briefly, enough that the students understand why they don't need to worry about it (best case: Explain in which semester/module it will be explained). Explanations should be how-do-I-apply-this-to-projects-of-my-own oriented. So, for example, only one sentence on the .bsp folder, while explaining in detail why the main folder exists and what kind of files to put in there.

In my opinion, there is no harm in adding the unaltered `scala/scala3.g8` project files as scaffold code, in case some students struggle to get the project running.

This exercise has to main subjects, Huffman codes and general software engineering practice. We can either keep these topics strictly separated or jump back and forth. I'd suggest the second, since then we can use the Huffman code part as an example for the software engineering principles. In that case, this section would be only "What kind of files exists", and stuff like "What do I put into which file" should be explained later.

## Huffman codes

I asked around whether students were already familiar with Huffman codes, and they were not. This section should explain the theory in detail, roughly covering "Theory of Huffman Coding" from EPFL. I'd (Franziska) like to do that part if that's ok.

## Implementation

Julie found the source files including solutions outside of gitlab here: https://github.com/userdarius/software-construction-epfl/tree/main/labs/huffman-coding. 

This section should roughly follow the "Implementation Guide" section from EPFL, although our approach needs to be different since the students won't be working with a skeleton. We have to add explanations for the following:

* What functions do I need and why? (can maybe be moved to the Huffman Codes section)
* What do I put into which file?
* Maybe try to divide into sections that each can stand on its own, so students don't have to finish everything to see their code in action?

## Finalizing the project 

More stuff: 

* The EPFL scaffold code contains a lot of supplementary functions that tie everything together and are not explained in the exercise. Having the students write everything themselves would be too much work for one week. We could either supply those functions for them to copy and paste, or try to rewrite the exercise such that we don't need them. Best might be a combination of both: Supply the functions that allow students to process larger files, but omit the fancy user interface.
* EPFL uses a custom .huf format for encoded files. Maybe scrap and replace this (I tried to open a .huf file in notepad++ and it did NOT look pretty)
* building on exercise 11: Testing, pre- and postconditions.






