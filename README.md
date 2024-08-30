# EPS
In this project - Environmental Procedural Simulator, I implemented a simulator designed to simulate a world, the world has land, trees, wind, sky, sun and characters. The world is dynamic and the characters have the ability to move within it as much as they want. This project focuses on implementing a variety of advanced object-oriented programming techniques.





UML Graph:


![uml_after](https://github.com/user-attachments/assets/4e70b1e4-d92e-49a1-87ff-208c85b028e3)



UML Explanation:

There is one large package that contains 
the game manager and within it a package that defines the world Iv'e build and sub-packages that make up the
world by parts - day and night lighting in the world, trees in the world and other objects living in the world
(avatar, sky and earth).
Our final graph has been slightly redesigned. Iv'e have added a utils package in which there are several classes
that help us create the rednomes of objects in the game as well as arrange the ranges of the earth to the size
of the blocks in the game. In addition, Iv'e added a class in the Tree package that contains a simple enum that
indicates the condition of a leaf at any given moment (depending on the tree, in the air or on the ground), 
this class helps us to model the code better and decide on the condition of each tree at each game.
In addition, Iv'e have added a TreeGenerator class that produces the parts of the tree smoothly and allows the
Tree class to produce trees in a cleaner and more modular way.
Adding these classes has significantly improved the modeling of the code, it has allowed us to paste the code
more conveniently and add beautiful functionality in an easy and clear way.


Explanation of the implementation of infinite world functionality:
In order to use the "infinite world" functionality, I "updated" the world each time when the
'PepseGameManager' class was updated (using its overridden 'update' method). Each time (when necessary) I
updated the minimal and maximal x coordinates that 'exist' in the world of the game and removed objects out of 
these bounds (the minimal and maximal x coordinates of the world).


Explanation of the implementation of Tree class:
Iv'e have created several different departments, each responsible for something different.
First, Iv'e created a LeafPositionStatus class that contains an enum that contains all possible single-leaf
situations in the game (depending on the tree, in the air, or on the ground).
Second, Iv'e created a Leaf class that represents a single leaf in a game. In this class Iv'e realized all the
functionality required of it to produce for all the leaves in the game (continuous brightness, falling to the
ground, etc.).
Third, Iv'e created a TreeGenerator department that is responsible for the production of the trunk and top of a
single tree.
Fourth, Iv'e have created a Tree class that is responsible for randomly creating trees within a certain range
(our screen).


Design dilemmas:
Iv'e debated whether to leave all of the functionality associated with trees inside the 'Tree' class
mentioned in the API, but eventually decided to split it to some other classes ('TreeGenerator', 'Leaf', and
even the 'LeafPositionStatus' enum), and that made code more modular, readable, and easier to debug.
In addition, Iv'e debated whether to export the objects in the 'world' package (specifically those which
doesn't belong to an inner package inside 'world') to a 'gameobjects' package, but eventually decided not to
do so because it's not allowed by the official API of this exercise.

And here some a little spoiler to heaven :) 

![EPSWorldImg](https://user-images.githubusercontent.com/64755588/152696184-11fedb8b-8eeb-4432-9ba5-457b975c5563.png)

Enjoy!
