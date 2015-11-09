package jacs.player;

/**
 * This stores data related to timing and synchronization of audio and servo. Currently the player uses fixed values
 * that assume WAV format audio and 30 frames per second for servo playback.
 * 
 * @author Jared Cline
 *
 */
class TimingSettings {

	private int servoFramesPerSecond;
	/**
	 * Must be <= servoFramesPerSecond and <= audioBytesPerSecond
	 */
	private int cyclesPerSecond;
	private int audioBytesPerSecond = 176400; // 44100 samples/second * 16 bits
												// per sample / 8 bits per byte *
												// 2
	// channels

	private int servoFramesPerCycle; // Only the value 30 FPS supported now
	private int audioBytesPerCycle;
	private int servoLag = 5; // This is a delay used to throttel servo commands

	public TimingSettings(int servoFramesPerSecond, int cyclesPerSecond) throws Exception {
		this.servoFramesPerSecond = servoFramesPerSecond;
		this.cyclesPerSecond = cyclesPerSecond;

		calculatePerCycleInfo(servoFramesPerSecond, cyclesPerSecond);

		if (servoFramesPerSecond == 0)
			throw new Exception("servoFramesPerSecond too low");

		if (cyclesPerSecond > servoFramesPerSecond || cyclesPerSecond > 176400 || cyclesPerSecond == 0)
			throw new Exception("Cycles Per Second is too high");

		if (areBlank()) {
			throw new Exception("One or more inputs is blank.");
		}
	}

	private boolean areBlank() {
		if (servoFramesPerSecond == 0 || cyclesPerSecond == 0)
			return true;
		else
			return false;
	}

	/**
	 * @param servoFramesPerSecond
	 * @param cyclesPerSecond
	 */
	private void calculatePerCycleInfo(int servoFramesPerSecond, int cyclesPerSecond) {
		servoFramesPerCycle = servoFramesPerSecond / cyclesPerSecond; // TODO Rename to indicate bytes versus frames
		audioBytesPerCycle = audioBytesPerSecond / cyclesPerSecond;
	}

	/**
	 * @return the servoFramesPerSecond
	 */
	int getServoFramesPerSecond() {
		return servoFramesPerSecond;
	}

	/**
	 * @param servoFramesPerSecond
	 *            the servoFramesPerSecond
	 */
	void setServoFramesPerSecond(int servoFramesPerSecond) {
		this.servoFramesPerSecond = servoFramesPerSecond;
		calculatePerCycleInfo(servoFramesPerSecond, cyclesPerSecond);
	}

	/**
	 * @return the cyclesPerSecond
	 */
	int getCyclesPerSecond() {
		return cyclesPerSecond;
	}

	/**
	 * @param cyclesPerSecond
	 *            the cyclesPerSecond to set
	 */
	void setCyclesPerSecond(int cyclesPerSecond) {
		this.cyclesPerSecond = cyclesPerSecond;
		calculatePerCycleInfo(servoFramesPerSecond, cyclesPerSecond);
	}

	/**
	 * @return the audioBytesPerSecond
	 */
	int getAudioBytesPerSecond() {
		return audioBytesPerSecond;
	}

	/**
	 * @param audioBytesPerSecond
	 *            the audioBytesPerSecond to set
	 */
	void setAudioBytesPerSecond(int audioBytesPerSecond) {
		this.audioBytesPerSecond = audioBytesPerSecond;
		calculatePerCycleInfo(servoFramesPerSecond, cyclesPerSecond);
	}

	/**
	 * @return the servoFramesPerCycle
	 */
	int getServoFramesPerCycle() {
		return servoFramesPerCycle;
	}

	/**
	 * @return the audioBytesPerCycle
	 */
	int getAudioBytesPerCycle() {
		return audioBytesPerCycle;
	}

	int getServoLag() {
		return servoLag;
	}

	/**
	 * @param servoLag
	 *            the servoLag to set
	 */
	void setServoLag(int servoLag) {
		this.servoLag = servoLag;
	}

}
