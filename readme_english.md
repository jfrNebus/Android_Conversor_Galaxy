# Introduction :wave:

A few years ago I had to work with the alarm systems _Galaxy Dimension_ from _HoneyWell_. It's been quite long time since I worked with them and I might have forgotten some of their specs, but here are the reassons for me to make this app.

This app is meant to improve the work efficiency. Usually, this type of system is connected to a private security company that monitors the signals sent by the system. The process of checking the system can be confusing when you are trying to determine if the sensors linked to the system are sending their signals to the security company. This is due to the way the system assigns an ID to each sensor. The system assigns a number, while the operator can assign a name, to each sensor, and the software used to see live signals provided by the security company assigns a different ID to the same device. This leads to situations where it is hard to track which ID belongs to each device.

The security company provides a conversion table to track each sensor. This way, the operator can get the ID set by the company by knowing the ID set by the system for the device being tested, and vice versa. The main problem is that this process significantly slows down the work efficiency since the operator has to scroll up and down the table to find each device. This process takes a lot of time when the whole system is being tested.

Usually, each system is tested by two operators. Once the whole testing process is over, one of the operators checks the log of signals while the other one reviews the list of sensors in the company documentation, marking as "Tested" those sensors which were tested with positive result.

* The sensor with the ID 125 is in the log.
* The first operator looks in the conversion table for the zone 125. It belongs to the device numbered by the system with the ID 1135.
* The second operator operator checks in the documentation provided by the company, whether the sensor with the ID 1135 has already been tested or not, and updates its state in the documentation.
* The process starts again with a new signal in the log. 

This whole way of working leads to time losses. What happens if both operators are not alone in the building? There will be devices triggered more than once sice other people will trigger the sensors in the most populated areas. This will fill the log with multiple iterations of the same signals. When the log is being checked, the operator reading the log will usually ask the operator checking the documentation whether a specific ID was already checked or not. The operator won't be able to remember which ones have already been reported to the second operator if the log is too long and the system has many devices. The second operator will have to check if different devices were already checked or not.

Another problem is the way that the operator browses each zone in the conversion table. The system assigns the ID using numbers in the ranges of 1001 to 1158, 2001 to 2158, 3001 to 3158 and 4001 to 4158. The number used by the system depends on the way the whole system was wired, which depends on the security needs of the building. Because of this, the log can provide either organized information or just information where a device in the range of 1001 to 1158 reports a signal followed by a device in the range of 3001 to 3158, followed by an array of devices in different ranges. When the operator is reading the log to the second operator, the one checking the conversion table will have to scroll up and down constantly through the pages.

Both situations made a no sense to me. Because of this I thought that it could be easy for me to make an app to get the conversion of a device ID for a specific model of the system. Additionally, I could add a mode to the app to report if the ID being entered was already used. This way, the operator has quick access the conversion of an ID and can seeif it was already checked or not. This way, no time is wasted while scrolling constantly throught the conversion table and the documentation.

<br>
<br>

## **Working principle 🧰**

<br>

Let's start by understanding how the system numbers the sensors. The system has screw connections for 16 sensors. For more than 16 sensors it is necesssary to add expansion boards to the communications bus. A communications bus is the connection between the system and devices, sending or receiving information. An expansion board is a device that allows to add a certain number of sensors to the system, and it is connected to it through a communication bus. The bus used by this type of system works under the RS-485 protocol. If the area to be protected requires 26 sensors when using this system, it will be necessary to add 2 expansion boards to the system.

Each expansion board allows to adding up to 8 sensors to the system. Therefore, in order to have 26 sensors added to the system 2 expansion boards are needed. The main system hosts 16 sensors, the first expansion board hosts the next 8 devices, and the second expansion board hosts the last 2 sensors. Each bus can host a maximum of 16 groups of 8 sensors, totaling 128 sensors. Finally, a system can have up to 4 buses depending to the model of the system. A system with 4 buses can host a total of 512 sensors.

It is really important to know how the architecture of the system works in order to locate breakdowns, test the system etc. If the system reports a fault in the expansion board 4 on the first bus, and you know that the first bus is located throughout the west side of the protected area, it is easier to identify and fix the problem, especially if it is your first time in the area.

The system numbers the sensors in a way that it is really helpful for the operator to know all the information related to a sensor. Below are some examples:


Sensor 2036:
* Bus: 2
* Expansion board: 03 -> The count starts at 0. The first expansion board is the number 00, the second expansion board is the number 01, the third one is the number 02, the forth one is the number 03.
* Number of sensor: 6 -> The sixth sensor of the expansion board.

Summary: Sixth sensor in the fourth expansor board, in the second bus.

Sensor 1014:
* Bus: 1
* Expansion board: 01 -> As has been explained, the system board has screw connectors for the first 16 sensors hosted by bus number 1. Therefore the first 8 sensors belong to the first built-in "expansion board" in the system, the expansion board number 00. The next 8 sensors, sensors 9 to 16, belong to the second built-in "expansion board" in the system, the number 01.
* Number of sensor: 4 -> The fourth sensor of the expansion board.

Summary: Fourth sensor in the second expansion board, in the first bus.

Sensor 3138:
* Bus: 3
* Expansion board: 13 -> Physical expansion board number 14, logical expansion number 13.
* Number of sensor 8: -> The eighth sensor of the expansion board.

Summary: Eighth sensor in the expansion board number 14 (physical count), in the third bus.


View:

<p align="center">
 <img src="Docs/pics/asci_architecture.png"/> 
</p>

The conversion table shows the conversion the ID conversions for three different systems. Right now I don't know if there are more models for this system, since it's been a long time since I worked with them. Each model numbers the sensors in different ways.

* CLASSIC: Up to 512 sensors, numbered as explained before.
* G2: Up to 44 sensors. The first 4 sensors are numbered as explained before, from 1001 to 1004. The fifth sensor is numbered as 1011. This way, the sixth sensor gets the ID 1012, sensor number 7 gets the ID 1013, and so on until sensor number 12 which gets the ID 1018. From this point and following the same logic used by the system to number the sensors, device number 13 will be numbered as 1021, device 14 gets the ID 1022, untill the last sensor, sensor number 44, which will be 1058
* G3, also known as Dimension: Up to 512 sensors. The first 16 zones are numbered as explained before. The first device gets the ID 1001, and sensor number 16 gets the ID 1018. The ID 1021, which in CLASSIC would belong to sensor number 17, in this system belongs to sensor number 33, and the count continues from 33. The ID 1022 now belongs to sensor 34 instead of sensor 18 as it would in CLASSIC. This way, in this system there are no sensors numbered from 17 to 32 by any software.

Let's remember that IDs set by the alarm system are in the ranges 1001 to 1158, 2001 to 2158, 3001 to 3158 and 4001 to 4158. IDs set by the software used by the operator, provided by the security company, are in the range 1-512. Therefore, there are no sensors identified by the security company with the numbers 17 to 32 for a Dimension system.

All this can be understood better by reading the comments in the code.
 
<br>
<br>

## **App :iphone:**

<br>

<p align="center">
 <img src="Docs/pics/playstore1.jpg" width="307" height="640"/> 
 <img src="Docs/pics/playstore2.jpg" width="307" height="640"/> 
 <img src="Docs/pics/playstore3.jpg" width="307" height="640"/> 
</p>

<br>

The user interface is quite simple, but I still have to work on it. My main target was to set a comfortable color palette. I didn't want a high contrast, black text over a white background, which can be harmful for users with vision disorders. I am sure that color-blind people will still struggle with the colors chosen. I guess that I will work on it.

It is really easy to use the app. The user enters the sensor number in any format, and the app reports the ID conversion for that sensor according to the system type selected through the checkboxes in the top area. If the operator enters a sensor ID formatted by the alarm system, the app understands that the operator is looking for the ordinary name set by the software provided by the security company, and the other way around.

The check boxes are meant to select which kind of conversion must be applied over the ID entered by the user, since each system has its own way to calculate the conversion.

The option _Lista_ sorts the log of IDs entered in descending  order. This is really useful if the operator needs to report the log, since it can be copied. Additionally, the log won't accept duplicated entries, it will report an error message. This way, the situation described above about checking the table and scrolling up and down constantly is avoided.

<br>

## **Code explanation :bulb:**

The code is explained as comments, in the files inside the "commented" folder. I programmed and commented everything a few years ago. I didn't check it again and I know that now I could fix and clean most of the code, I could delete several useless files, as well as create new documentation to explain the whole code. Right now I have no time since I am quite busy, so for now, bear with me; I was less experienced back then.

<br>

<p align="center">
    <a href="commented/app/src/main/java/com/gps/conversorgalaxy/MainActivity.java"><img src="https://img.shields.io/badge/Commented%20code-8f529e?style=plastic"/></a>
</p>


