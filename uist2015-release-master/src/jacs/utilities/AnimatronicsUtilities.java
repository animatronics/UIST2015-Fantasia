package jacs.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A variety of utilities for converting data types, reading CSV files, procedurally generating motion tracks.
 * 
 * @author galford
 *
 */
public class AnimatronicsUtilities {

	public static final int FULL_SWEEP_LENGTH = 255;
	public static final int FULL_SWEEP_DOUBLE_TIME_LENGTH = FULL_SWEEP_LENGTH / 2;
	public static final int FULL_SWEEP_HALF_TIME_LENGTH = FULL_SWEEP_LENGTH * 2;

	public static final int FULL_REVERSE_SWEEP_LENGTH = 255;
	public static final int FULL_REVERSE_SWEEP_DOUBLE_TIME_LENGTH = FULL_REVERSE_SWEEP_LENGTH / 2;
	public static final int FULL_REVERSE_SWEEP_HALF_TIME_LENGTH = FULL_REVERSE_SWEEP_LENGTH * 2;

	/**
	 * This method will take a csv file of servo motions, listed in columns, and put it into a 2D array of bytes
	 * 
	 * @param fileName
	 *            the name of the input file
	 * @param numServos
	 *            the number of columns (motors) in the file
	 * @return a 2D array of motions, one array per motor
	 * @throws FileNotFoundException
	 */
	public static byte[][] getBytes(String fileName, int numServos) throws FileNotFoundException {
		Scanner scanner;
		File inputFile;
		inputFile = new File(fileName);
		scanner = new Scanner(inputFile);
		scanner.useDelimiter(",");

		// Build 2D ArrayList of Bytes
		ArrayList<ArrayList<Byte>> outerList = new ArrayList<ArrayList<Byte>>();
		for (int i = 0; i < numServos; i++) {
			outerList.add(new ArrayList<Byte>());
		}

		// Populate ArrayList
		int counter = 0;
		while (scanner.hasNext()) {
			String s = scanner.next();
			s = s.replace(',', ' ').trim();
			if (!s.isEmpty()) {
				outerList.get(counter).add((byte) Integer.parseInt(s));
				counter++;

				if (counter == numServos) {
					counter = 0;
				}
			}
		}

		// cast to primitive
		byte[][] outputArray = new byte[numServos][outerList.get(0).size()];
		for (int i = 0; i < numServos; i++) {
			for (int j = 0; j < outerList.get(0).size(); j++) {
				outputArray[i][j] = (byte) outerList.get(i).get(j);
			}
		}

		return outputArray;
	}

	public static byte[] readBytesSingleServo(String fileName) throws FileNotFoundException {

		File inputFile = new File(fileName);
		Scanner scanner = new Scanner(inputFile);

		ArrayList<Byte> data = new ArrayList<Byte>();
		String s;

		// Populate ArrayList
		while (scanner.hasNext()) {
			s = scanner.next();
			s = s.trim();
			if (!s.isEmpty()) {
				data.add((byte) Integer.parseInt(s));
			}
		}
		scanner.close();
		return byteArrayListToByteArray(data);
	}

	/**
	 * This method will take a csv file of servo motions, listed in columns, and put it into a 2D array of bytes
	 * 
	 * @param fileName
	 *            the name of the input file
	 * @param numServos
	 *            the number of columns (motors) in the file
	 * @return a 2D array of motions, one array per motor
	 * @throws FileNotFoundException
	 */
	public static byte[][] readBytesMultipleServo(String fileName, int numServos) throws FileNotFoundException {

		File inputFile = new File(fileName);
		Scanner scanner = new Scanner(inputFile);
		scanner.useDelimiter(",");

		// Build 2D ArrayList of Bytes
		ArrayList<ArrayList<Byte>> outerList = new ArrayList<ArrayList<Byte>>();
		for (int i = 0; i < numServos; i++) {
			outerList.add(new ArrayList<Byte>());
		}

		// Populate ArrayList
		int counter = 0;
		while (scanner.hasNext()) {
			String s = scanner.next();
			s = s.replace(',', ' ').trim();
			if (!s.isEmpty()) {
				outerList.get(counter).add((byte) Integer.parseInt(s));
				counter++;

				if (counter == numServos) {
					counter = 0;
				}
			}
		}

		scanner.close();

		// cast to primitive
		byte[][] outputArray = new byte[numServos][outerList.get(0).size()];
		for (int i = 0; i < numServos; i++) {
			for (int j = 0; j < outerList.get(0).size(); j++) {
				outputArray[i][j] = outerList.get(i).get(j);
			}
		}

		return outputArray;
	}

	// ==============================================================
	// CONVERT basic patterns as ARRAYLISTS and ARRAYS for various data types
	// Intended to construct test sequences for motion tracks
	// ==============================================================

	// Generate byte arrays with specific length motion commands
	public byte[] generateFullSweepData() {
		byte[] sweep = new byte[255];
		for (int i = 0; i < sweep.length; i++)
			sweep[i] = (byte) i;
		return sweep;
	}

	public byte[] generateFullSweepData_DoubleTime() {
		byte[] sweep = new byte[255 / 2];
		for (int i = 0; i < sweep.length; i++)
			sweep[i] = (byte) (i * 2);
		return sweep;
	}

	public byte[] generateFullSweepData_HalfTime() {
		byte[] sweep = new byte[255 * 2];
		for (int i = 0; i < sweep.length; i++)
			sweep[i] = (byte) (i / 2);
		return sweep;
	}

	public byte[] generateFullReverseSweepData() {
		byte[] sweep = new byte[255];
		for (int i = sweep.length; i > 0; i--)
			sweep[i] = (byte) i;
		return sweep;
	}

	public byte[] generateFullReverseSweepDataDoubleTime() {
		byte[] sweep = new byte[255 / 2];
		for (int i = sweep.length; i > 0; i--)
			sweep[i] = (byte) (i * 2);
		return sweep;
	}

	public byte[] generateFullReverseSweepDataHalfTime() {
		byte[] sweep = new byte[255 * 2];
		for (int i = sweep.length; i > 0; i--)
			sweep[i] = (byte) (i / 2);
		return sweep;
	}

	public byte[] generateFullSweepProportion(int rate) {
		byte[] sweep = new byte[255 * rate];
		for (int i = 0; i < sweep.length; i++)
			sweep[i] = (byte) (i / rate);
		return sweep;
	}

	// Add bytes to existing ArrayList of bytes
	public static void appendFullSweepData(ArrayList<Short> a) {
		for (int i = 0; i < 255; i++)
			a.add(new Short((short) i));
	}

	public static void appendFullSweepDataDoubleTime(ArrayList<Short> a) {
		for (int i = 0; i < 255 / 2; i++)
			a.add(new Short((short) (i * 2)));
	}

	public static void appendFullSweepDataHalfTime(ArrayList<Short> a) {
		for (int i = 0; i < 255 * 2; i++)
			a.add(new Short((short) (i / 2)));
	}

	public static void appendFullReverseSweepData(ArrayList<Short> a) {
		for (int i = 255; i > 0; i--)
			a.add(new Short((short) i));
	}

	public static void appendFullReverseSweepDataDoubleTime(ArrayList<Short> a) {
		for (int i = 255 / 2; i > 0; i--)
			a.add(new Short((short) (i * 2)));
	}

	public static void appendFullReverseSweepDataHalfTime(ArrayList<Short> a) {
		for (int i = 255 * 2; i > 0; i--)
			a.add(new Short((short) (i / 2)));
	}

	// Add arbitrary number of zeroes
	public static void padMotionTrack(ArrayList<Short> a, int padSize, short padValue) {
		for (int i = 0; i < padSize; i++)
			a.add(new Short((short) padValue));
	}

	public static void fillRandomValues(ArrayList<Short> a, int fillSize) {
		for (int i = 0; i < fillSize; i++)
			a.add(new Short((short) (Math.random() * 255)));
	}

	public static void apendShorts(ArrayList<Short> a, byte[] b) {
		if (a != null)
			for (int i = 0; i < b.length; i++)
				a.add(new Short(b[i]));
	}

	// ==============================================================
	// CONVERT between ARRAYLISTS and ARRAYS for various data types
	//
	// ==============================================================

	public static byte[] shortArrayListToByteArray(ArrayList<Short> shortArrayList) {
		byte[] temp = new byte[shortArrayList.size()];
		for (int i = 0; i < temp.length; i++)
			temp[i] = shortArrayList.get(i).byteValue();
		return temp;
	}

	public static byte[] byteArrayListToByteArray(ArrayList<Byte> indata) {
		byte[] temp = new byte[indata.size()];
		for (int i = 0; i < temp.length; i++)
			temp[i] = indata.get(i).byteValue();
		return temp;
	}

	public static ArrayList<Short> byteArrayToShortArrayList(byte[] byteArray) {
		ArrayList<Short> temp = new ArrayList<Short>();
		for (int i = 0; i < byteArray.length; i = i + 1000) {
			temp.add((short) byteArray[i]);
		}

		return temp;
	}

	public static ArrayList<Byte> byteArrayToByteArrayList(byte[] byteArray) {
		ArrayList<Byte> temp = new ArrayList<Byte>();
		for (int i = 0; i < byteArray.length; i = i + 1000) {
			temp.add(byteArray[i]);
		}

		return temp;
	}

	public static byte[] resample(byte[] original, int factor) {
		byte[] temp = new byte[original.length / factor + 1];

		for (int i = 0; i < temp.length; i = i + factor)
			temp[i] = original[i * factor];

		return temp;

	}

	public static void invertByteArray(byte[] array) {

		for (int i = 0; i < array.length; i++) {
			short temp = array[i];
			// array[i] = (byte) ((short)Byte.MAX_VALUE - temp); // Compute as
			// Short since Java uses signed bytes
		}

	}

	private static int averageArray(float[] data, int mid, int radius) {
		double avg = data[mid];
		int lower = 0;
		int upper = 0;
		int count = 1;
		for (int i = 1; i <= radius; i++) {
			lower = Math.max(0, mid - i);
			upper = Math.min(data.length, mid + i);
			if (lower != 0) {
				avg += Math.abs(data[lower]);
				count++;
			}
			if (upper != data.length) {
				count++;
				avg += Math.abs(data[upper]);
			}
		}

		avg /= count;

		return (int) avg;
	}

}
