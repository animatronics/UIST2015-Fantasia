package jacs.config;

/**
 * This provides data on servo operating characteristics.
 * <p>
 * Correctly calibrated animatronics show movements, once a show has been properly computed and synchronized in software
 * or hardware, depend on a combination of individual servo characteristics and servo controller card configuration.
 * <p>
 * The Pololu card provided to UIST contestants has been preconfigured so that each of the 6 channels have been set to
 * the widest minimum and maximum microsecond range for the HS-485HB, which is [608, 2400].
 * <p>
 * The code base uses an 8-bit range [0,254] to generate commands that span the [608,2400] to ideally control servo
 * angular position of [0,180].
 * <p>
 * Ideal, however, may damage your servo by pushing it beyond it's actualy operating range, particularly the HS-82MG
 * microservo. Contestants should adjust the actual min and max of the 8-bit range based on their own calibration
 * results, which are easily determined using provided tool {@link jacs.demos.PololuSweepTest} to experiment with
 * correct ranges. You can limit the range applied to a servo by adjusting the min and max positions within the [0,254]
 * range used by the AnimatronicsShowPlayer.
 * 
 * 
 * 
 * @author galford
 *
 */
public class ServoSpecs {

	// Supported Servo Models
	public static final int DEFAULT_SERVO = -1;
	public static final int HS_22MG = 0;
	public static final int HS_485HB = 1;
	public static final int HS_422 = 2;

	public static int servo = -1;
	public static String servoModel = "";

	// Behaviours
	public static final int STANDARD_SERVO = 100;
	public static final int CONTINUOUS_ROTATION_SERVO = 101;

	// Default operating characteristics for servos with 180 degree range
	// These are based on values from the HiTec model servos listed above.
	// These values may need to be adjusted for specific servos

	public static final int DEFAULT_MIN_RANGE = 608; // in microseconds, manufacturer specs

	public static final int DEFAULT_MAX_RANGE = 2400; // in microseconds, manufacturer specs

	// public static final int DEFAULT_8BIT_RANGE = 900;

	/**
	 * Returns the minimum operating range in microseconds for a servo model based on published device characteristics.
	 * For future development to support finer calibration of shows.
	 * 
	 * @param servoModel
	 *            This should be one of the supported Models or Default. Currently returning same value for any input.
	 * @return
	 */
	public static int getMinServoRange(int servoModel) {
		switch (servoModel) {
		case HS_22MG:
		case HS_485HB:
		case HS_422:
			return DEFAULT_MIN_RANGE;

		default:
			return DEFAULT_MIN_RANGE;
		}
	}

	/**
	 * Returns the maximum operating range in microseconds for a servo model based on published device characteristics.
	 * For future development to support finer calibration of shows.
	 * 
	 * @param servoModel
	 *            This should be one of the supported Models or Default. Currently returning same value for any input.
	 * @return
	 */
	public static int getMaxServoRange(int servoModel) {
		switch (servoModel) {
		case HS_22MG:
		case HS_485HB:
		case HS_422:
			return DEFAULT_MAX_RANGE;

		default:
			return DEFAULT_MAX_RANGE;
		}
	}

}
