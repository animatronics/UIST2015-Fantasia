# UIST2015-Fantasia
##introduction
We have developed the system which controls the dancing puppet according to the motion of the "magic baton". In the "magic baton", both a USB camera and an acceleration sensor is embedded. By wielding the baton, the user can work the puppet.

##About programs that are run to realize this system
To realize this system, we use Arduino, Processing, Visualstudio and Eclipse. Arduino and Processing use to move five servo motors by the value of an acceleration sensor. Visualstudio uses to judge whether the baton forces toward alpaca puppet by image processing. Eclipse uses to control directly five servo motors according to the value of an acceleration sensor and the result of image processing. Visualstudio is written by C++. The program which need to use in the file "uist2015-release-master" of eclipse is "Fantasia.java" in "uist2015-release-master\src\jacs\demos".
