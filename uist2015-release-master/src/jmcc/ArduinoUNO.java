package jmcc;

public class ArduinoUNO extends Arduino {
	public ArduinoUNO() {
		super(ARDUINO_UNO);
	}

	@Override
	public byte[] buildCommandSetTargetNative(byte pin, short target) {
		return buildCommandSetTargetMiniSSC(pin, (byte) target); // only Mini-SSC supported on Arduino
	}
}