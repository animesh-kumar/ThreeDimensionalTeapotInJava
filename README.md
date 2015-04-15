# ThreeDimensionalTeapotInJava
A Three- Dimensional Teapot Viewer made using Java 3D APIs.

## Setting Up the Project
1. Make sure you have Java installed in your



## Interpreting the Data Files
Each of the 8 data (.dat) files represent a portion of the complete teapot, which are as follows:-
* tpot1.dat - Body parhttps://github.com/animesh-kumar/ThreeDimensionalTeapotInJava/new/master?readme=1#t 1
* tpot2.dat - Body part 2
* tpot3.dat - Body part 3
* tpot4.dat - Handle 
* tpot5.dat - Base 
* tpot6.dat - Spout
* tpot7.dat - Top of lid 
* tpot8.dat - Bottom of lid

##### Explanation for numbers inside each data file
The files give the coordinates of the points for a portion of the teapot.<br/>
Each files starts with the number 64 stating that there will be 64 points to be followed.<br/>
Each of the next 64 lines will have 4 space separated numbers. <br/>
Eg. 5     1.38426   0.00000   2.48750<br/>
This indicates 5th point with the x coordinate 1.38426, y coordinate 0.00000 and z coordinate 2.48750.<br/>
These 64 lines will be followed by 1 & then next line will have number 36 representing that next 36 lines will give the points for 36 quadrilaterals forming the portion.<br>
The last 36 lines can be interpreted as follows:-<br/>
Example, 3 1 4 3 4 8 7<br/>
taken from line 3rd line from the 36 quadrilateral of teapot2.dat<br/>
It says that the line 3 gives you a figure, with 4 points. The points are the 3rd, 4th, 8th and 7th of the previous 64 in the same file.<br/>
