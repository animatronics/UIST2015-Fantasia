package jmcc;

import jacs.demos.Fantasia;
import jacs.player.AnimatronicsShowPlayer;

/**
 * This abstract class is the data type required by the {@link AnimatronicsShowPlayer} and lists specific supported
 * boards.
 * <p>
 * <b>Supported Hardware:<\b>Currently only the Pololu MicroMaestro and Arduino UNO running code responding to Mini-SSC
 * are supported.
 * <p>
 * <b>Usage:<\b> Provide the name of a supported board in the constructor.
 * <p>
 * {@code Microcontroller mc = new Microcontroller (Microcontroller.POLOLU_MICRO_MAESTRO); }
 * <p>
 * Only Mini-SSC commands are supported at this time. Native commands are for future implementation.
 * <p>
 * 
 * 
 * @author galford
 *
 */
public abstract class Microcontroller {

	// Support protocols for serial communications
	public static String MINI_SSC = "Mini-SSC";

	// Supported boards
	public static String GENERIC_MINI_SSC_DEVICE = "Generic MiniSSC Device";

	public static String POLOLU_MAESTRO = "Pololu Maestro"; // Generic pololu board
	public static String POLOLU_MICRO_MAESTRO = "Pololu Micro Maestro"; // Micro Maestro 6-channel servo controller

	// Note - Arduino must be running Servo Controller simulator code to receive Mini-SSC commands for pins 2..13
	public static String ARDUINO = "Arduino"; // Generic
	public static String ARDUINO_UNO = "Arduino Uno"; // Arduino UNO

	protected static final byte MINI_SSC_SET_TARGET_COMMAND = (byte) 0xFF; // Move to superclass Microcontroller

	private String microcontrollerName = ""; // e.g. "Pololu Micro Maestro" or "Arduino Uno"
	private String defaultProtocol = MINI_SSC;

	// Pololu pin information will vary with specific
	protected PinInfo[] pins;

	public Microcontroller(String controllerName) {
		microcontrollerName = controllerName;
		defaultProtocol = MINI_SSC;
	}

	public String getMicrocontrollerName() {
		return microcontrollerName;
	}

	public void setMicrocontrollerName(String microcontrollerName) {
		this.microcontrollerName = microcontrollerName;
	}

	public String getDefaultProtocol() {
		return defaultProtocol;
	}

	public void setDefaultProtocol(String defaultProtocol) {
		this.defaultProtocol = defaultProtocol;
	}

	/**
	 * This generates a byte sequence for writing to serial port to move a servo to a target position This will handle
	 * choosing protocol. Please see important notes below about servo calibration.
	 * 
	 * @param pin
	 *            pin on the controller to which the servo is physically attached
	 * @param position
	 *            a value in the range [0, 254] to indicate the desired rotational position of the servo from 0 to 180
	 *            degrees. A value of 127 will set the servo to a 90 degree position, the neutral position.
	 *            <p>
	 *            <b>USAGE<\b> that how the servo responds to each position command depends on how the controller card
	 *            is configured. Some individual servos can not be set to the extremes of the range [0,254] without
	 *            damage. Care should be taken to limit the position value based on servo-specific calibration data. Use
	 *            tool in {@link Fantasia} to observe the behavior of your servos in your build. Then adjust
	 *            your motion range within the limits of the servo.
	 * @return
	 */
	public byte[] buildSetTargetCommand(byte pin, byte position) {

		if (defaultProtocol.equals(MINI_SSC))
			return buildCommandSetTargetMiniSSC(pin, position);
		else
			// For native protocols
			return buildCommandSetTargetNative(pin, position);
	}

	/**
	 * @param pin
	 * @param target
	 * @return
	 */
	public byte[] buildCommandSetTargetMiniSSC(byte pin, byte target) {

		// Create byte array according to Pololu User's Guide to set target using MiniSSC protocol

		byte[] setTargetMiniSSCCmd = { MINI_SSC_SET_TARGET_COMMAND, (byte) pin, (byte) target };
		return setTargetMiniSSCCmd;
	}

	/**
	 * This builds native protocol command to set target servo Currently not implemented
	 * 
	 * @param pin
	 * @param target
	 * @return
	 */
	public abstract byte[] buildCommandSetTargetNative(byte pin, short target);

	// Write versions of the same command for different method signatures for convenience.
	// They should convert to proper data type and call the above routines as the base so if
	// there is a change in the command, only the base functin needs to change.
}
