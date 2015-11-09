package jacs.config;

import java.util.regex.Pattern;

import jmcc.Microcontroller;
import jssc.SerialNativeInterface;
import jssc.SerialPortList;

/**
 * This class supports detection and selection of correct serial port has been assigned by the operating system to a
 * microcontroller card plugged into a USB port.
 * <p>
 * Under some circumstances, it is easy to guess which is the correct port by using a combination of string filters and
 * position in the returned array. But this is not always possible. Under some platform configurations, such as when
 * multiple USB ports are in use, it may be necessary for a user to select the correct port from this list in order for
 * the {@link Animatronics Show Player} or other functionality demonstrated in test programs in {@link jacs.demos} to
 * work.
 * 
 * @author galford
 *
 */
public class SystemPortsList {
	/**
	 * This uses a string filter to return a list of serial ports names assigned by the underlying operating system
	 * based on typical port assignment used when attaching a a Pololu or Arduino card to a USB under Windows 7 or Mac
	 * OS X.
	 * 
	 * @return Array of (abridged) port names assigned by the operating system.
	 *         <p>
	 *         In Windows, these are simply "COM ##", in Mac OS X, these begin with "/dev/cu"
	 *         <p>
	 *         An empty array if no ports can be found.
	 */
	public static String[] getServoControllerPorts() {

		Pattern searchPattern;
		String[] ports = {};

		switch (SerialNativeInterface.getOsType()) {

		case SerialNativeInterface.OS_MAC_OS_X: {
			searchPattern = Pattern.compile("cu.(serial|usbserial|usbmodem).*");
			ports = SerialPortList.getPortNames(searchPattern);
			break;
		}

		case SerialNativeInterface.OS_WINDOWS:
		default: {
			searchPattern = Pattern.compile("");
			ports = SerialPortList.getPortNames(searchPattern);
			break;
		}

		}
		return ports;

	}

	/**
	 * This uses positional information to return the correct serial command port to which a Pololu or Arduino has been
	 * attached via USB from a list which has been returned from {@link getServoControllerPorts}
	 * 
	 * 
	 * @param microcontrollerPorts
	 *            List of serial ports filtered to find USB connected servo controllers and returned from
	 *            {@link getServoControllerPorts} Preconditions: microcontrollerPorts is not empty
	 * @param microcontrollerName
	 *            The name of supported controllers, found in {@link jmcc.Microcontroller }
	 * @return * A single port name if a reasonable guess exists, otherwise an empty string.
	 */
	private static String bestGuessPortFromList(String[] microcontrollerPorts, String microcontrollerName) {

		String bestGuessPortName = "";

		switch (SerialNativeInterface.getOsType()) {

		case SerialNativeInterface.OS_MAC_OS_X: {
			bestGuessPortName = microcontrollerPorts[0];
		}
			break;

		case SerialNativeInterface.OS_WINDOWS: {
			if (microcontrollerPorts.length >= 2
					&& (microcontrollerName.equals(Microcontroller.POLOLU_MAESTRO) || microcontrollerName
							.equals(Microcontroller.POLOLU_MICRO_MAESTRO)))

				return microcontrollerPorts[microcontrollerPorts.length - 2];

			// Last one in list
			else if (microcontrollerPorts.length >= 1
					&& (microcontrollerName.equals(Microcontroller.ARDUINO) || microcontrollerName
							.equals(Microcontroller.ARDUINO_UNO)))
				bestGuessPortName = microcontrollerPorts[0];

			break;

		}
		default:
			break;

		}
		return bestGuessPortName;

	}

	/**
	 * This attempts to automatically return the correct serial command port to use for communicating with a Pololu or
	 * Arduino has been attached via USB on either a Windows 7 or a Mac OS X system.
	 * <p>
	 * It is an automated best guess. It is provided for convenience for a common case; however operator selection of
	 * the correct port may be necessary under some configurations. In that case, method {@link getServoControllerPorts}
	 * can be used to present a selection list to the operator.
	 * 
	 * @param microcontrollerName
	 *            The name of supported controllers, found in {@link jmcc.Microcontroller }
	 * @return A single port name if a reasonable guess exists, otherwise an empty string.
	 */
	public static String findPortForController(String microcontrollerName) {
		String[] portList = getServoControllerPorts();
		if (portList.length > 0)
			return bestGuessPortFromList(portList, microcontrollerName);
		else
			return "";

	}

}
