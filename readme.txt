COMP 2601 W2017
Course Project
ShiftSwapper
April 11, 2017

Carolyn Fenwick - 100956658
Pierre Seguin 	- 100859121

===---===---===---===--- ShiftSwapper ---===---===---===---===

Adding the project
==================
- Open Android Studio
- Go to File > New > Import Project
	- navigate to and select COMP2601-ShiftSwapper folder
	- click OK


Adding the server
=================
- Go to edit configurations (Drop down beside run button)
- Press the + button at the top right
	- Select application
	- In configuration tab
		- Name: Server
		- Main class: Server
		- Use classpath of module: server
- Press ok


To run JUnit tests:
==================
- navigate to ..\COMP2601A2\app\src\test\java\edu\carleton\COMP2601
- right click on ShiftSwapRepositoryTest.java and select 'Run ShiftSwapRepositoryTest'

To run the server:
=================

- Select Server from the drop down list
- Press the run button
- Find out the ip address of your device

To run the app:
==============

Select app from the drop down list
Press the run button and launch on 2 different devices




Logging in:

Administrator (manager)
=======================

-login with employee id 1
- update the ip address and port of the server if needed
- Press login button



Employee
========
- login with an employee between 2 and 6
- update the ip address and port of the server if needed
- Press login button



Features

Administrator
=============
When logged in as an administrator, you are presented with a list of all shifts. This includes assigned
shifts as well as shifts that are available to be assigned.

The shift item in the list displays the date/time of the shift and how many people are currently
assigned to that shift.

Clicking on a shift brings you to a detailed view of which employees (if any) are currently assigned
to the shift. Updating the fields will either create a new shift assignment or change or delete an
existing one.

Employee
========
When logged in as an employee, you are presented with a list of your shifts, as well as any pending
shift change requests that you have been sent.

- Clicking on a shift will present a list of shifts that are available to be swapped, should you
so choose. (not yet implemented)
- Clicking on a shift change request will present a dialog to either accept or reject the
request. (not yet implemented)



















