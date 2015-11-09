package jacs.config;

import jmcc.ArduinoUNO;
import jmcc.MicroMaestro;
import jmcc.Microcontroller;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * This negotiates the connection between the specific microcontroller connection and the serial port. The port name is
 * chosen based on a combination of the operating system and card. The particular microcontroller card determines the
 * protocol that is used for serial communications. Right now, only Mini-SSC is supported. This uses {@link jmcc} for
 * supporting device-independent microcontroller methods and {@link jssc} for cross-platform serial communications
 * 
 * 
 * @author galford
 *
 */
public class MicrocontrollerConnection {

	private Microcontroller microcontroller = null;
	private SerialPort port = null;

	// Settings

	/**
	 * Uses the name of the microcontroller to establish a connection through the operating system. The microcontroller
	 * should be plugged in when this is called
	 * 
	 * @param microcontrollerName
	 *            Name of a supported microcontroller card from {@link jmcc.Microcontroller}
	 */
	public MicrocontrollerConnection(String microcontrollerName) {

		String chosenPort = SystemPortsList.findPortForController(microcontrollerName);

		if (!chosenPort.equals("")) {

			port = new SerialPort(chosenPort);

			if (microcontrollerName.equals(Microcontroller.POLOLU_MAESTRO)
					|| microcontrollerName.equals(Microcontroller.POLOLU_MICRO_MAESTRO))
				microcontroller = new MicroMaestro();

			else if (microcontrollerName.equals(Microcontroller.ARDUINO)
					|| microcontrollerName.equals(Microcontroller.ARDUINO_UNO)
					|| microcontrollerName.equals(Microcontroller.GENERIC_MINI_SSC_DEVICE))
				microcontroller = new ArduinoUNO();
		}

	}

	/**
	 * Create SerialPort connection based on user selection of ports form Operating System
	 * 
	 * @param portName
	 *            This is a valid serial port selected from a list of ports returned from opera
	 *            {@link getServoControllerPorts}
	 */
	public void chooseAndConnectToPort(String portName) {
		this.port = new SerialPort(portName);
	}

	/**
	 * Create microcontroller based on card name. This establishes the communications protocol.
	 * 
	 * @param microcontrollerName
	 *            This is a constant from
	 */
	public void setControllerConnectionFromCardName(String microcontrollerName) {
		if (microcontrollerName.equals(Microcontroller.POLOLU_MAESTRO)
				|| microcontrollerName.equals(Microcontroller.POLOLU_MICRO_MAESTRO))
			microcontroller = new MicroMaestro();

		else if (microcontrollerName.equals(Microcontroller.ARDUINO)
				|| microcontrollerName.equals(Microcontroller.ARDUINO_UNO)
				|| microcontrollerName.equals(Microcontroller.GENERIC_MINI_SSC_DEVICE))
			microcontroller = new ArduinoUNO();
	}

	/**
	 * Opens serial port using defaults of 9600 /8-N-1
	 */
	public void openPort() throws SerialPortException {
		try {
			port.openPort();
			port.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		} catch (SerialPortException e) {
			throw new SerialPortException(null, null, null);
		}
	}

	/**
	 * Closes serial port.
	 */
	public void closePort() throws SerialPortException {
		try {
			port.closePort();
		} catch (SerialPortException e) {
			throw new SerialPortException(null, null, null);
		}
	}

	/**
	 * This provides a way to explicitly choose the serial port to use for sending commands. This is necessary if the
	 * automatically selected port is incorrect, which may occur on some platforms or if multiple devices are detected.
	 * 
	 * @param portName
	 *            Name of a port from the operating system. It should be selected from {@link getServoControllerPorts}
	 */

	public void resetPort(String portName) {
		this.port = new SerialPort(portName);
	}

	public SerialPort getPort() {
		return port;
	}

	public void setPort(SerialPort port) {
		this.port = port;
	}

	/**
	 * This is a device-independent method to sends a single servo positioning command. This will format the command
	 * correctly for the underlying controller card and chosen command format.
	 * 
	 * @param pin
	 *            Pin to which servo is attached.
	 * @param position
	 *            Target position of servo in range [0, 254]
	 * @throws SerialPortException
	 */
	public void setTarget(byte pin, byte position) throws SerialPortException {
		byte[] command = microcontroller.buildSetTargetCommand((byte) pin, (byte) position);
		sendSingleCommand(command);

	}

	/**
	 * This sends a single command to serial port. This is to be used after the command has been formatted for the
	 * currently connected microcontroller using {@link nuildSetTargetCommand}
	 * 
	 * @param command
	 *            Properly formatted command to set one servo to one position using correct communication protocol
	 * 
	 * @throws SerialPortException
	 */
	private void sendSingleCommand(byte[] command) throws SerialPortException {
		if (port.isOpened())
			port.writeBytes(command);
		else {
			this.openPort();
			sendSingleCommand(command);
		}

	}

}
