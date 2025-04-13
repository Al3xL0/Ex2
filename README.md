﻿# Ex2

## Introduction

The following program is a solution to my second exercise in Intro to CS at Ariel University.
### Basic explanation of the program
The assignment we were given was to create a simple clone of Excel, a basic spreadsheet with cells.
Each cell can store one of the following things:
1. text
2. number
3. formula (start with **"="**)

### examples of valid data that you may assign into cells:

A0 : 0 \
A1 : =A0 + B0 , given cell A0 and B0 has numerical values or contain a valid formula \
A2: =A0 , if cell A0 contains any data. \
A3: hello
### examples of invalid data that you shouldn't assign to cells
A0:=A0  **formulas that contain self referencing cells will return an error** (like in the image below) \
A1:=text **you cant assign text or cells that hold non-mathematical data into a formula** 

## insturctions to operate the spreadsheet:
#### how to assign data into cell ? 
1. double-click on the target cell 
2. write the wanted data inside the input box
3. press ok to update or cancel to stop
#### working with files :
to save or load any file press on the file button on the left top-side \
if you want to load certain file you should make sure that the file is written in text format 
and in the following format : 

the first line is a text, the others built in the following way : x , y , data , comment (won't be written in the sheet)
## an image of the program at work
![img.png](img.png)


                
