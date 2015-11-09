/**
 * 
 */
package jacs.player;

/**
 * Formatted Show Data for representing animatronic show designed to be used with an {@linkplain AnimatronicsShowPlayer}
 * <p>
 * An animatronic show is comprised of a single audio track represented as a WAV file, accompanied by sets of servo
 * motor positions designed to accompany and be synchronized to the WAV file playback. Additionally, the servo motor
 * positions are designated for individual control of servos attached to specific pins of a servo controller card
 * configured to recognize serial port commands using Mini-SSC protocol. So, any show that is represented in the
 * following way can be played back repeatedly using this player.
 * 
 * <p>
 * <b> Detailed Notes:</b> The following are some detailed notes on the data. Data captured or produced in any way that
 * complies with the format and limitations can be used to playback a show.
 * 
 * <p>
 * <b>Audio: </b>A WAV format file.
 * 
 * <p>
 * <b>Servo Motions:</b> The motion sequence for a single servo is stored as one row of a 2D array of byte values that
 * represent the position sequence for the servo during playback. There is an row of bytes for each servo that is
 * physically connected to the servo controller, up to the maximum supported by the card. Each array row must contain
 * the same number of elements. (i.e. only rectangular arrays are supported at this time. Pad each row with a fixed
 * position if you want the servo to remain still.)
 * <p>
 * Each array element contains a value between [0, 254], which corresponds to the ideal range of angular servo positions
 * from 0 to 180 degrees. ( Value 127 is the neutral position value of 90 degrees.)
 * 
 * <p>
 * The length of the array determines the time duration of the motion playback sequence. We assume 30 positions(frames)
 * per second. So, for example, a file with 1275 lines will take 1275/30 = 42.5 second to playback. Typically, this
 * matches the playback time of the audio file.
 * 
 * <p>
 * <b>Pin Numbers: </b> The physical pins to which the servos are attached on the servo controller card. The number of
 * pins must match the number of rows in the 2D array. The i th pin number will be controlled by the i th row in the
 * array of servo motions.
 * 
 * Included in this code base are utilities to generate motion patterns and store them in a CSV text file.
 * <p>
 * Additional utilities read CSV files into a 2D array of bytes for use with the player, where each column corresponds
 * to postions for a single servo. The data in the CSV file can be produced in any way - (e.g. procedurally generated,
 * synchronize to the audio waveform, captured from a physical device such as a joystick) and may contain any number of
 * columns. However, a data value MUST be provided for each column for every row and each line must terminate with a
 * comma. See the accompanying data files and utility programs for examples.
 * <p>
 * 
 * 
 * 
 * @author Jared Cline
 *
 * 
 *
 */
public class FormattedShowData {

	private String audioFile;
	private byte[][] servoMotions;
	private byte[] pinNumbers;

	private byte[] recordedPinNumbers;

	/**
	 * Creates a package of show data to be used with an {@linkplain AnimatronicsShowPlayer}
	 * <p>
	 * 
	 * @param audioFile
	 *            the path to the .WAV format audio file. "" denotes no audio file
	 *            <p>
	 * @param servoMotions
	 *            2D rectangular array of bytes representing servo motions. Each row is a sequence of motions for one
	 *            servo. Each array element contains a value between [0, 254], which corresponds to the ideal range of
	 *            angular servo positions from 0 to 180 degrees. ( Value 127 is the neutral position value of 90
	 *            degrees.)
	 * 
	 *            The length of the array determines the time duration of the motion playback sequence. We assume 30
	 *            positions(frames) per second. So, for example, a file with 1275 lines will take 1275/30 = 42.5 second
	 *            to playback. Typically, this matches the playback time of the audio file.
	 *            <p>
	 * @param pinNumbers
	 *            This reflects the current physical connections of the servos to the pins on the servo controller card.
	 *            <p>
	 *            The order of the pins values corresponds to the order of the rows of the array in the servoMotions
	 *            parameter.
	 *            <p>
	 *            For example,
	 *            <p>
	 *            if pinNumbers = { 2, 5, 1} then
	 *            <p>
	 *            positions in row servoMotions[0] will be sent to pin 2 and
	 *            <p>
	 *            positions in row servoMotions[1] will be sent to pin 5 and
	 *            <p>
	 *            positions in row servoMotions[2] will be sent to pin 1
	 *            <p>
	 * @param recordedPinNumbers
	 *            the pin numbers for the servos whose motions are coming in from a real-time recording device. If no
	 *            recorded servos are present, use an array of length 0. Feature currently disabled. Input ignored.
	 *            <p>
	 * @throws Exception
	 *             if motions aren't found for each pre-loaded motor, servo lag is negative, if the motion array is not
	 *             rectangular, or if any fields are blank.
	 */
	public FormattedShowData(String audioFile, byte[] pinNumbers, byte[][] servoMotions, byte[] recordedPinNumbers)
			throws Exception {
		this.audioFile = audioFile;
		this.pinNumbers = pinNumbers;
		this.servoMotions = servoMotions;
		// this.recordedPinNumbers = recordedPinNumbers;
		this.recordedPinNumbers = new byte[] {}; // empty array

		// TODO Compute frames per second based on length of servo motions and time length of audio file
		// TODO Pad frames to offset start times
		// TODO Design scheme for delayed audio start from separate file.

		checkData();
	}

	private void checkData() throws Exception {
		if (pinNumbers.length != servoMotions.length)
			throw new Exception("Motions not found for every servo");

		for (int i = 0; i < servoMotions.length; i++) {
			if (servoMotions[i].length != servoMotions[0].length)
				throw new Exception("Motion array not rectangular - pad motion tracks to be same length and reformat");
		}

		if (areBlank())
			throw new Exception("One or more data fields is blank");

	}

	public boolean areBlank() {
		if (audioFile.equals("") && (pinNumbers.length == 0 || servoMotions.length == 0))
			return true;
		else
			return false;
	}

	/**
	 * @return the audioFile
	 */
	public String getAudioFile() {
		return audioFile;
	}

	/**
	 * @return the pinNumbers
	 */
	public byte[] getPinNumbers() {
		return pinNumbers;
	}

	/**
	 * @return the servoMotions
	 */
	public byte[][] getServoMotions() {
		return servoMotions;
	}

	/**
	 * @return the recordedPinNumbers
	 */
	public byte[] getRecordedPinNumbers() {
		return recordedPinNumbers;
	}
}
