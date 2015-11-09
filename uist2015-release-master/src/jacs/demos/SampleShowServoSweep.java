package jacs.demos;

import jacs.config.MicrocontrollerConnection;
import jacs.player.AnimatronicsShowPlayer;
import jacs.player.FormattedShowData;
import jacs.utilities.AnimatronicsUtilities;
import jmcc.Microcontroller;

/**
 * This simple program provides an example of playback with no audio.
 * <p>
 * 
 * SERVO_SWEEP - is a range of motion demonstration for servo motors on 3 pins. There is no audio file. An empty string
 * indicates. Servo motions were generated programmatically and saved to a CSV file. This code expects the servo pins to
 * be on 2,3,4
 * <p>
 * The .CSV files was generated using tools in {@link jacs.utilities}
 **/

public class SampleShowServoSweep {

	public static void main(String[] args) throws Exception {

		// Initialized prerecorded show data
		String audioFile = "";
		byte[] pins = { (byte) 2, (byte) 3, (byte) 4 }; // List of exact pins to which servos are attached on the card.
		byte[][] servoMotions; // Each row of the array is a series of servo postions to be sent to a single pin.
		servoMotions = AnimatronicsUtilities.getBytes("data/ThreeServoMotion.csv", 3); // Read motion tracks from CSV
																						// file
		byte[] dummy = {}; // For recording motion tracks. Unused here.

		// Format data for use in the player using provided code
		FormattedShowData showData = new FormattedShowData(audioFile, pins, servoMotions, dummy);

		// Create a connection
		MicrocontrollerConnection mc = new MicrocontrollerConnection(Microcontroller.POLOLU_MICRO_MAESTRO);

		// Create a player using the connection
		AnimatronicsShowPlayer player = new AnimatronicsShowPlayer(mc);

		// initiate playback
		player.playShow(showData);

	}

}
