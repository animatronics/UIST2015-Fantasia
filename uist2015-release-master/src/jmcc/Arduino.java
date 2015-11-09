package jmcc;

public abstract class Arduino extends Microcontroller {

	// good place to constants
	public Arduino() {
		super(Microcontroller.ARDUINO); // Generic Arduino
	}

	public Arduino(String boardName) {
		super(boardName);
	}

}