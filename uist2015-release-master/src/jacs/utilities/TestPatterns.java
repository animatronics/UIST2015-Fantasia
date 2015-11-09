package jacs.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Utility programs to generate .CSV files with procedurally generated servo motion tracks.
 * 
 * @author galford
 *
 */
public class TestPatterns {

	public static ArrayList<Short> generatePatternSingleServo() {

		// byte[][] pattern;

		ArrayList<Short> motionTrack = new ArrayList<Short>();

		AnimatronicsUtilities.appendFullSweepData(motionTrack);
		AnimatronicsUtilities.appendFullSweepDataDoubleTime(motionTrack);
		AnimatronicsUtilities.padMotionTrack(motionTrack, 60, (short) 0);
		AnimatronicsUtilities.appendFullSweepDataHalfTime(motionTrack);
		AnimatronicsUtilities.fillRandomValues(motionTrack, 30);
		AnimatronicsUtilities.appendFullSweepData(motionTrack);
		motionTrack.add(new Short((byte) 127));

		return motionTrack;

	}

	public static ArrayList<ArrayList<Short>> generatePatternThreeServo() {

		// byte[][] pattern;

		ArrayList<Short> motionTrack0 = new ArrayList<Short>();
		ArrayList<Short> motionTrack1 = new ArrayList<Short>();
		ArrayList<Short> motionTrack2 = new ArrayList<Short>();

		ArrayList<ArrayList<Short>> allTracks = new ArrayList<ArrayList<Short>>();

		AnimatronicsUtilities.appendFullSweepData(motionTrack0);
		AnimatronicsUtilities.padMotionTrack(motionTrack1, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.padMotionTrack(motionTrack2, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);

		AnimatronicsUtilities.padMotionTrack(motionTrack0, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.appendFullSweepData(motionTrack1);
		AnimatronicsUtilities.padMotionTrack(motionTrack2, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);

		AnimatronicsUtilities.padMotionTrack(motionTrack0, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.padMotionTrack(motionTrack1, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.appendFullSweepData(motionTrack2);

		AnimatronicsUtilities.appendFullSweepDataHalfTime(motionTrack0);

		AnimatronicsUtilities.appendFullSweepDataDoubleTime(motionTrack1);
		AnimatronicsUtilities.padMotionTrack(motionTrack1, AnimatronicsUtilities.FULL_SWEEP_DOUBLE_TIME_LENGTH,
				(short) 127);
		AnimatronicsUtilities.appendFullSweepDataDoubleTime(motionTrack1);
		AnimatronicsUtilities.padMotionTrack(motionTrack1, AnimatronicsUtilities.FULL_SWEEP_DOUBLE_TIME_LENGTH,
				(short) 127);

		AnimatronicsUtilities.padMotionTrack(motionTrack2, AnimatronicsUtilities.FULL_SWEEP_DOUBLE_TIME_LENGTH,
				(short) 127);
		AnimatronicsUtilities.appendFullSweepDataDoubleTime(motionTrack2);
		AnimatronicsUtilities.padMotionTrack(motionTrack2, AnimatronicsUtilities.FULL_SWEEP_DOUBLE_TIME_LENGTH,
				(short) 127);
		AnimatronicsUtilities.appendFullSweepDataDoubleTime(motionTrack2);

		// TODO - Pad all to equal lengths
		motionTrack1.add((short) 127);
		motionTrack1.add((short) 127);
		motionTrack2.add((short) 127);
		motionTrack2.add((short) 127);

		System.out.println("Sizes: " + motionTrack0.size() + " " + motionTrack1.size() + " " + motionTrack2.size());

		allTracks.add(motionTrack0);
		allTracks.add(motionTrack1);
		allTracks.add(motionTrack2);

		return allTracks;

	}

	public static ArrayList<ArrayList<Short>> generateCharacterPanAndTiltSweep() {

		// byte[][] pattern;

		ArrayList<Short> motionTrack0 = new ArrayList<Short>();
		ArrayList<Short> motionTrack1 = new ArrayList<Short>();
		ArrayList<Short> motionTrack2 = new ArrayList<Short>();

		ArrayList<ArrayList<Short>> allTracks = new ArrayList<ArrayList<Short>>();

		// Go to neutral position
		motionTrack0.add((short) 127);
		motionTrack1.add((short) 127);
		motionTrack2.add((short) 127);

		// Move first servo full sweep and return
		AnimatronicsUtilities.appendFullSweepData(motionTrack0);
		AnimatronicsUtilities.padMotionTrack(motionTrack1, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.padMotionTrack(motionTrack2, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.appendFullReverseSweepData(motionTrack0);
		AnimatronicsUtilities.padMotionTrack(motionTrack1, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.padMotionTrack(motionTrack2, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);

		// Pause all servos at neutral
		AnimatronicsUtilities.padMotionTrack(motionTrack0, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.padMotionTrack(motionTrack1, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.padMotionTrack(motionTrack2, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);

		// Move second servo full sweep and return
		AnimatronicsUtilities.padMotionTrack(motionTrack0, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.appendFullSweepData(motionTrack1);
		AnimatronicsUtilities.padMotionTrack(motionTrack2, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.padMotionTrack(motionTrack0, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.appendFullReverseSweepData(motionTrack1);
		AnimatronicsUtilities.padMotionTrack(motionTrack2, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);

		// Pause all servos at neutral
		AnimatronicsUtilities.padMotionTrack(motionTrack0, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.padMotionTrack(motionTrack1, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.padMotionTrack(motionTrack2, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);

		// Move third servo full sweep and return
		AnimatronicsUtilities.padMotionTrack(motionTrack0, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.padMotionTrack(motionTrack1, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.appendFullSweepData(motionTrack2);
		AnimatronicsUtilities.padMotionTrack(motionTrack0, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.padMotionTrack(motionTrack1, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.appendFullReverseSweepData(motionTrack2);

		// Go to neutral position
		motionTrack0.add((short) 127);
		motionTrack1.add((short) 127);
		motionTrack2.add((short) 127);

		// TODO - Pad all to equal lengths

		motionTrack2.add((short) 127);

		System.out.println("Sizes (character): " + motionTrack0.size() + " " + motionTrack1.size() + " "
				+ motionTrack2.size());

		allTracks.add(motionTrack0);
		allTracks.add(motionTrack1);
		allTracks.add(motionTrack2);

		return allTracks;

	}

	public static ArrayList<ArrayList<Short>> generateReversePatternThreeServo() {

		// byte[][] pattern;

		ArrayList<Short> motionTrack0 = new ArrayList<Short>();
		ArrayList<Short> motionTrack1 = new ArrayList<Short>();
		ArrayList<Short> motionTrack2 = new ArrayList<Short>();

		ArrayList<ArrayList<Short>> allTracks = new ArrayList<ArrayList<Short>>();

		// Go to neutral position
		motionTrack0.add((short) 127);
		motionTrack1.add((short) 127);
		motionTrack2.add((short) 127);

		AnimatronicsUtilities.appendFullReverseSweepData(motionTrack0);
		AnimatronicsUtilities.padMotionTrack(motionTrack1, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.padMotionTrack(motionTrack2, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);

		AnimatronicsUtilities.padMotionTrack(motionTrack0, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.appendFullReverseSweepData(motionTrack1);
		AnimatronicsUtilities.padMotionTrack(motionTrack2, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);

		AnimatronicsUtilities.padMotionTrack(motionTrack0, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.padMotionTrack(motionTrack1, AnimatronicsUtilities.FULL_SWEEP_LENGTH, (short) 127);
		AnimatronicsUtilities.appendFullSweepData(motionTrack2);

		AnimatronicsUtilities.appendFullSweepDataHalfTime(motionTrack0);

		AnimatronicsUtilities.appendFullReverseSweepDataDoubleTime(motionTrack1);
		AnimatronicsUtilities.padMotionTrack(motionTrack1, AnimatronicsUtilities.FULL_SWEEP_DOUBLE_TIME_LENGTH,
				(short) 127);
		AnimatronicsUtilities.appendFullReverseSweepDataDoubleTime(motionTrack1);
		AnimatronicsUtilities.padMotionTrack(motionTrack1, AnimatronicsUtilities.FULL_SWEEP_DOUBLE_TIME_LENGTH,
				(short) 127);

		AnimatronicsUtilities.padMotionTrack(motionTrack2, AnimatronicsUtilities.FULL_SWEEP_DOUBLE_TIME_LENGTH,
				(short) 127);
		AnimatronicsUtilities.appendFullReverseSweepDataDoubleTime(motionTrack2);
		AnimatronicsUtilities.padMotionTrack(motionTrack2, AnimatronicsUtilities.FULL_SWEEP_DOUBLE_TIME_LENGTH,
				(short) 127);
		AnimatronicsUtilities.appendFullReverseSweepDataDoubleTime(motionTrack2);

		// TODO - Pad all to equal lengths
		motionTrack1.add((short) 127);
		motionTrack1.add((short) 127);
		// motionTrack2.add((short) 127);
		// motionTrack2.add((short) 127);

		System.out.println("Sizes: " + motionTrack0.size() + " " + motionTrack1.size() + " " + motionTrack2.size());

		allTracks.add(motionTrack0);
		allTracks.add(motionTrack1);
		// allTracks.add(motionTrack2);

		return allTracks;

	}

	// return pattern;

	public static byte[][] generateFormattedData() {

		byte[][] pattern = new byte[1][];

		return pattern;

	}

	/*****
	 * public static void writeFile(String filename, ArrayList<Short> a) { File f = new File(filename); PrintStream s;
	 * try { s = new PrintStream(f); for (int i = 0; i < a.size(); i++) s.println(a.get(i)); s.close();
	 * 
	 * } catch (FileNotFoundException e) { // TODO Auto-generated catch block e.printStackTrace(); } }
	 ****/

	public static void writeFile(String filename, ArrayList<ArrayList<Short>> a) {
		File f = new File(filename);
		PrintStream s;
		try {
			s = new PrintStream(f);

			for (int i = 0; i < a.get(0).size(); i++)
				s.println(a.get(0).get(i) + "," + a.get(1).get(i) + "," + a.get(2).get(i) + ",");

			s.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		// ArrayList<Short> servo1 = generatePatternSingleServo();
		// writeFile("data/OneServo.csv", servo1);

		ArrayList<ArrayList<Short>> allTracks = generatePatternThreeServo();
		writeFile("data/ThreeServoMotion.csv", allTracks);

		ArrayList<ArrayList<Short>> allTracksReverse = generateReversePatternThreeServo();
		writeFile("data/ThreeServoReverseMotion.csv", allTracksReverse);

		ArrayList<ArrayList<Short>> allPanAndTilt = generateCharacterPanAndTiltSweep();
		writeFile("data/CharacterPanAndTiltSweep.csv", allPanAndTilt);

		//
		// System.out.println("Number of Motions " + allTracks.get(0).size() + " Time Duration: "
		// + allTracks.get(0).size() / 30.0 + " seconds");

	}
}
