# DBDKillerTimer
## Introduction
This program simply allows you to know timings so you can slob harder on killer.
Simply start the appropriate timer after the action has occured.

* Decisive-Strike Timer: How long until decisive strike runs out.
* Borrowed-Time Timer: How long until borrowed time runs out.
* Chase Timer: How long you have been chasing a survivor.
* On-Shoulder Timer: How long they have been on your shoulder.

## Binds

Binds can be changed in customization\\binds.txt.

* Bind 1: represents the Decisive-Strike Timer and Borrowed-Time Timer
* Bind 2: represents the Chase Timer
* Bind 3: represents the On-Shoulder Timer
* Bind 4: fully resets all timers.

if you click the bind again it will reset the current timer.

## Timers
Red Timer represents: 
* The survivor shouldn't be picked up
* The survivor shouldn't be hit
* The Chase has been too long, leave the survivor
* The survivor can wiggle off your shoulder
 
Green Timer represent: 
* The survivor can be hit and you avoid borrowed time.
* You can pick up and avoid decisive strike
* The survivor cannot wiggle off

White Timer represents neutral, you can keep chasing the survivor.

## Customization
You can change your resolution and iconSize in customization\\properties.txt however,
1920x1080 is recommended for optimal usage.

Resolution:
* Minimum: 800x600
* Maximum: 1920x1080

Icon Sizes:
* 128x128
* 96x96
* 64x64

## Issues
Program not launching:
* Make sure when editing binds and properties they're ammended in the same format.
* Make sure when editing binds and properties they're ammended with valid inputs.
* close "OpenJDK Platform Binary" in task manager, only one instance can be ran at the same time
