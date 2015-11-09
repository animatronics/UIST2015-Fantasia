package jacs.demos;

import jacs.config.MicrocontrollerConnection;
import jacs.player.AnimatronicsShowPlayer;

import java.util.regex.Pattern;

import jssc.SerialNativeInterface;
import jssc.SerialPortList;

/**
 * Simple cross-platform program to retrieve and view the list of serial ports from the operating system.
 * 
 * When a microcontroller is plugged into a USB port, the device will appear in the serial port list of the operating
 * system. The {@linkplain AnimatronicsShowPlayer} requires that the correct port name be used to establish a
 * {@linkplain MicrocontrollerConnection} with the Pololu or other servo controller.
 * <p>
 * This code highlights a modification to file {@linkplain SerialPortList.java} in the jssc package necessary to obtain
 * a list of USB-connected devices for the Pololu Micro Maestro or Arduino to allow this. It is noted here for those who
 * may have downloaded the jssc package directly from another source that does not have this modification.
 * <p>
 * For convenience, the jmcc package includes defaults that will choose the correct port for various systems if only one
 * device is connected. If that is not the case, operator selection will be required to specify the correct port name.
 * <p>
 * On Mac OS X, the Pololu port names begin with "/dev/cu.usbmodem*" and the correct port will be listed first.
 * <p>
 * On Windows 7, the Pololu port names begin with "COM" and the correct port will be listed next to last.
 * <p>
 * A Pololu Micro Maestro has two ports assigned. The first one listed is the one to which Mini-SSC commands should be
 * sent. An Arduino has only one port assigned.
 * <p>
 * 
 * @author galford
 *
 */
public class ViewSerialPortList {

	public static String[] getPorts() {

		Pattern searchPattern;
		String[] serialPorts = {};

		switch (SerialNativeInterface.getOsType()) {

		case SerialNativeInterface.OS_MAC_OS_X: {
			searchPattern = Pattern.compile("cu.(serial|usbserial|usbmodem).*"); // This expression finds Pololu
			serialPorts = SerialPortList.getPortNames(searchPattern); // The correct port for Pololu will be first
			break;
		}

		case SerialNativeInterface.OS_WINDOWS:
		default: {
			searchPattern = Pattern.compile(""); // This returns all COM ports
			serialPorts = SerialPortList.getPortNames(searchPattern); // The correct port for Pololu will be next last
			break;
		}

		}
		return serialPorts;

	}

	public static void main(String[] args) {

		String[] serialPorts = getPorts();
		for (int i = 0; i < serialPorts.length; i++)
			System.out.println("serialPort [" + i + "]  " + serialPorts[i]);

	}

}
