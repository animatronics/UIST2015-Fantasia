package jacs.demos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import jacs.player.AnimatronicsShowPlayer;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 *
 * Program to run through the full range of motion [0,254] on each pin.
 * それぞれのピンで０から２５４の全ての範囲を動かすためのプログラム
 *
 * <b>Usage: </b>
 *
 * This tool can be used to（）
 * <p>
 * <b>Verify</b> the correct port was used to connect. See {@link jacs.config.SystemPortsList} for details and
 * {@link jacs.demos.ViewSerialPortsList} tools.（正しいポートは接続のために使われた。）
 * <p>
 * <b>Calibrate</b> the min and max values for each servo. If a servo "grinds" towards the ends of the range rather than
 * continuing to move, the values of [0,254] should be clamped to a narrow range to avoid burning out the motor.（それぞれのサーボの最大値・最小値を測定する。[0,254]の値はモーターの故障を防ぐために狭い範囲でなければいけない。)
 * ）
 * <p>
 * <i>Details: </i>
 * <p>
 * Correctly calibrated animatronics show movements, once a show has been properly computed and synchronized in software
 * or hardware, depend on a combination of individual servo characteristics and servo controller card configuration.（正しく測定されたアニマトロニクスは動く。一方でショーは個々のサーボの特徴とサーボ制御器カードの構成の組み合わせによって適切に計算され、ソフトウェアとハードウェアを同時に起こす。）
 * <p>
 * The Pololu card provided to UIST contestants has been preconfigured so that each of the 6 channels have been set to
 * the widest minimum and maximum microsecond range for the HS-485HB, which is [608, 2400].（UIST参加者に与えたPololuカードはそれぞれの６チャネルが「HS-485HB」のための最も広い最小値と最大値のマイクロセカンドの範囲を取れるようにするために予め設定されていた。）
 * <p>
 * The code base uses an 8-bit range [0,254] to generate commands that span the [608,2400] to ideally control servo
 * angular position of [0,180].（コードのベースは理想的に０から１８０度のサーボの角度位置を制御するためにコマンドを起こすために0から254の８ビットの範囲を使う。）
 * <p>
 * Ideal, however, may damage your servo by pushing it beyond it's actualy operating range, particularly the HS-82MG
 * microservo. Contestants should adjust the actual min and max of the 8-bit range based on their own calibration
 * results, which are easily determined using this tool to experiment with correct ranges.（しかしながら理想は実際に操作している範囲を超えてサーボを押すことによってあなたのサーボを傷つけるかもしれない（特に）「HS-82MG」マイクロサーボは）。
 * 参加者は彼ら自身の測定結果を下に８ビット範囲の実際の最小値と最大値を調節すべきだ。（それは正しい範囲で実験するためにこのツールを使って容易に決められる。）
 *
 * <p>
 * By modifying the values in these parallel arrays, contestants can easily enable and disable the sweep test per pin
 * and adjust the min and max values per pin, based on individual servos attached to each. In this example, the min and
 * max range for servo on pin 2 is limited to [30, 220] and commands have been disabled for pins 0, 1 and 5.
 * （これらのその値を平行配列に修正することによって、参加者は容易にピンごとのスイープテストを可能・不可能にでき、ピンごとの最小値・最大値を調節できる（それぞれに付けられた個々のサーボをベースとして）。
 * この例では、ピン２のサーボの最小値・最大値は[30,220]に制限されていて、コマンドはピン0,1,5を動かなくしている。）
 *
 * <p>
 * {@code public static int[] pinsToTest = 0, 1, 2, 3, 4, 5 }
 * <p>
 * {@code static int[] minServoPosition = 0, 0, 30, 0, 0, 0 }
 * <p>
 * {@code public static int[] maxServoPosition = 254, 254, 220, 254, 254,254 }
 * <p>
 * {@code boolean[] testEnabled = false, false, true, true, true, false }
 *
 * <p>
 *
 * <p>
 * <b>Experiment</b> with timing values that on platform and data.
 * <p>
 * SERIAL_COMMAND_PAUSE - is used to delay between sending serial commands. Changes to this will affect the speed and
 * smoothness of the servo sweep. In a show mode, when commands to different servos are interleaved per frame, number
 * may need to be adjusted. The equivalent number for show playback is in {@link jacs.player.Timer_Settings}.
 * （シリアルコマンド停止ーシリアルコマンドを送る間、遅らせるために使われる。この変化はサーボスイープの速度と平滑度に影響するだろう。
 * ショーモードでは、異なるサーボのコマンドがフレームごとに閉じられたとき、番号を調節する必要があるかもしれない。）
 * <p>
 * OBSERVER_PAUSE - slows down the time between each sweep for observation.（観測のためのそれぞれのスイープの間、時間をゆっくりにする。）
 *
 *
 * @author Ginger Alford
 *
 *
 */


public class Fantasia {

	private static SerialPort port = null; // Port name depends on combination of servo controller and operating system

	// Defaults
	public static int MIN_SERVO_POSITION = 0;
	public static int MAX_SERVO_POSITION = 255;
	public static int NEUTRAL_SERVO_POSTION = 127;

	public static int MAX_NUMBER_SERVOS = 6;

	// Setting for min and max values per pin to calibrate to each servo individually
	//個々にそれぞれのサーボを測定するためにピンごとの最大値・最小値をセット
	public static int[] pinsToTest = { 0, 1, 2, 3, 4, 5 }; // pins
	public static int[] minServoPosition = { 0, 0, 30, 0, 0, 0 }; // tune range per pin（ピンごとの調律範囲）
	public static int[] maxServoPosition = { 254, 254, 200, 200, 200, 254 }; // min and max values
	public static boolean[] testEnabled = { false, false, true, true, true, false }; // enable and disable commands per
																						// servo（サーボごとの可能・不可能なコマンド）
	public static int SERIAL_COMMAND_PAUSE = 14; // microseconds to pause between sending serial commands to controller
												//コントローラーにシリアルコマンドを送るために待つ時間（マイクロ秒）
	public static int OBSERVER_PAUSE = 1500; // 1.5 seconds

	public static void main(String[] args) throws InterruptedException {

		// TODO - use system port utility to make best guesss

		String[] portNames = SerialPortList.getPortNames();
		System.out.println(Arrays.toString(portNames));

		if (portNames.length < 1) {
			System.out.println("Empty Port List.");
		} else // create port
		{
			// TODO - handle Windows .v. Mac
			System.out.println("Attempting to open to: " + portNames[0]);	//○ポートを開こうとしています
			port = new SerialPort(portNames[0]);

			if (port == null)
				System.out.println("Null Port.");

			else // openPort Connection
			{
				//例外処理
				// OPEN CONNECTION
				try {
					port.openPort();
					System.out.println("Opened Port Successfully");
				} catch (SerialPortException e) {
					System.out.println("Error Opening Port: " + e);
				}

				try {
					System.out.println("Beginning series of calibration tests...");	//測定テスト開始

					positionTest(127); // neutral position for all（全ての中間位置） - end with this to relieve stress on servos（サーボのストレス軽減）
					System.out.println("All servos in 127 position (neutral) to avoid burnout");	//焼損を避けるための全ての位置
					Thread.sleep(OBSERVER_PAUSE);

					System.out.println("Sweep full range for each servo that is enabled.");	//可能なそれぞれの全ての範囲を掃除
					sweepTest();
					Thread.sleep(OBSERVER_PAUSE);
					System.out.println("Sweeps complete");	//掃除完了

					System.out.println("Calibration tests complete...");	//測定テスト完了

				} catch (Exception e) {
					System.out.println(" Error with Sweep Test " + e);	//スイープテストでエラー
				}

				// CLOSE CONNECTION
				try {
					port.closePort();
					System.out.println("Closed Port Successfully.  You may disconnect the microcontroller.");	//マイクロコントローラーを抜いてもいい

				} catch (SerialPortException e) {
					System.out.println("Error Closing Port: " + e);
				}
			} // else openPort connection(それ以外、オープンポートの接続)
		} // else create port（それ以外、ポート作成）

	} // main

	/**
	 * This performs a sweep test on all pins that are enabled in the static data arrays {@link testEnabled} using
	 * pin-specific min and max values as specified in the {@link minServoPosition} and {@link maxServoPosition}.
	 *（これは特定のピンの最小値と最大値を使って、静的なデータ配列を格納可能なすべてのピンでスイープテストを実行している。）
	 * @throws Exception
	 *             An exception is thrown if there is an error opening or writing to the serial port
	 */
	public static void sweepTest() throws Exception {
		try {

			for (int pin = 0; pin < pinsToTest.length; pin++) {
				if (testEnabled[pin])
					sweepTest(pin, minServoPosition[pin], maxServoPosition[pin], SERIAL_COMMAND_PAUSE);
			}

		} catch (Exception e) {
			System.out.println("Error Opening Port: " + e);
		}

	}

	public static void positionTest(int position) throws Exception {
		try {
			for (int pin = 0; pin < pinsToTest.length; pin++) {
				port.writeByte((byte) 255);
				port.writeByte((byte) pin);
				port.writeByte((byte) position);
				Thread.sleep(SERIAL_COMMAND_PAUSE); // Give servo time to catch up
			}

		} catch (Exception e) {
			System.out.println("Error Opening Port: " + e);
		}

	}

	// TODO - remove this method
	// public static void sweepTest(int pin) throws Exception {
	// sweepTest(pin, MIN_SERVO_POSITION, MAX_SERVO_POSITION, SERIAL_COMMAND_PAUSE);
	// }

	/**
	 * This sends a series of uniformly timed and spaced commands to sweep the servo through a full range of positions
	 * represented in the range [0,254]
	 *
	 * @param pin
	 *            pin on controller to which servo is attached
	 * @param start
	 *            beginning position of sweep expressed in the range [ 0,254]
	 * @param stop
	 *            ending position of sweep expressed in the range [ 0,254]
	 * @param pause
	 *            delay in microseconds between issues of commands to serial port
	 * @throws Exception
	 */
	AnimatronicsShowPlayer show;

	public static void sweepTest(int pin, int start, int stop, int pause) throws Exception {

		int ch = 10;
		int light = 10;
		String y_value = null;
		String x_value = null;
		String y_value1 = null;
		String x_value1 = null;
		String ac_value = null;
		String ac_y_value = null;
		int a=0;
		double first_pos = 0.0;
		long start_t = 0;
		long end_t = 0;
		int count1 = 0;
		int count2 = 1;

		int i = 0;
		int i1 = 0;
		int i2 = 0;
		int i3 = 0;
		int ac = 0;
		int ac_y = 0;
		int befo_i = 0;
		int befo_i1 = 0;
		int ac_mouth = 0;
		int ac_agree = 0;
		int ac_shake = 0;
		int befo_ac_mouth = 127;
		int befo_ac_agree = 127;
		int befo_ac_shake = 127;
		int b = 0;
		int h = 0;
		int h_first = 0;

		while(true)
		{

			try{
			      File file = new File("C:\\Users\\Vostro3460\\Documents\\Visual Studio 2013\\Projects\\file_io_color\\file_io_color\\test.txt");
			      File file1 = new File("C:\\Users\\Vostro3460\\Documents\\Visual Studio 2013\\Projects\\file_io_color\\file_io_color\\test1.txt");	//左上y座標
			      File file2 = new File("C:\\Users\\Vostro3460\\Documents\\Visual Studio 2013\\Projects\\file_io_color\\file_io_color\\test2.txt");	//左上x座標
			      File file3 = new File("C:\\Users\\Vostro3460\\Documents\\Visual Studio 2013\\Projects\\file_io_color\\file_io_color\\test3.txt");	//右上y座標
			      File file4 = new File("C:\\Users\\Vostro3460\\Documents\\Visual Studio 2013\\Projects\\file_io_color\\file_io_color\\test4.txt");	//右上x座標
			      File file5 = new File("C:\\Users\\Vostro3460\\Documents\\Visual Studio 2013\\Projects\\tes3_kai2\\x.txt");	//加速度センサの値(x)
			      File file6 = new File("C:\\Users\\Vostro3460\\Documents\\Visual Studio 2013\\Projects\\tes3_kai2\\light.txt");	//光ったかどうか
			      File file7 = new File("C:\\Users\\Vostro3460\\Documents\\Visual Studio 2013\\Projects\\tes3_kai2\\y.txt");	//加速度センサの値(y)




			      FileReader filereader = new FileReader(file);
			      FileReader filereader1 = new FileReader(file1);
			      FileReader filereader2 = new FileReader(file2);
			      FileReader filereader3 = new FileReader(file3);
			      FileReader filereader4 = new FileReader(file4);
			      FileReader filereader5 = new FileReader(file5);
			      FileReader filereader6 = new FileReader(file6);
			      FileReader filereader7 = new FileReader(file7);

			      BufferedReader br = new BufferedReader(filereader1);
			      BufferedReader br1 = new BufferedReader(filereader2);
			      BufferedReader br2 = new BufferedReader(filereader3);
			      BufferedReader br3 = new BufferedReader(filereader4);
			      BufferedReader br4 = new BufferedReader(filereader5);
			      BufferedReader br5 = new BufferedReader(filereader7);



			      if(count1==0)
			      {
			    	  start_t = System.currentTimeMillis();
			    	  count1++;
			      }

			      end_t = System.currentTimeMillis();
			      System.out.println("end_t-start_t="+(end_t-start_t));
			      if(end_t-start_t>2000)
			      {
			    	  ch = filereader.read();			//赤色の領域があるかどうか（０か１が入る）
			    	  System.out.println(ch);
			    	  start_t = System.currentTimeMillis();
			    	  count2++;
			      }
			      else
			      {
			    	  if(ch=='0')
			    	  {
			    		  ch = filereader.read();			//赤色の領域があるかどうか（０か１が入る）
			    	  }
			      }
			      light = filereader6.read();			//LEDが光ったかどうか




                  /*
			      //左上のyの値
			      while((y_value = br.readLine()) != null)
			      {
			    	  i=Integer.parseInt(y_value);
			    	  i = i * 200 / 480;
			      }



			      //左上のxの値
			      while((x_value = br1.readLine()) != null)
			      {
			    	  i1 = Integer.parseInt(x_value);
			    	  i1 = i1 * 200 / 640;
			      }



			      //右下のyの値
			      while((y_value1 = br2.readLine()) != null)
			      {
			    	  i2=Integer.parseInt(y_value1);
			    	  i2 = i2 * 200 / 480;
			      }

			      //右下のxの値
			      while((x_value1 = br3.readLine()) != null)
			      {
			    	  i3=Integer.parseInt(x_value1);
			    	  i3 = i3 * 200 / 640;
			      }
                  */


			      //加速度センサの値(x)
			      while((ac_value = br4.readLine()) != null)
			      {
			    	  ac=Integer.parseInt(ac_value);
			      }



			    //加速度センサの値(y)
			      while((ac_y_value = br5.readLine()) != null)
			      {
			    	  ac_y=Integer.parseInt(ac_y_value);
			      }



			      filereader.close();
			      filereader6.close();
			      br.close();
			      br1.close();
			      br2.close();
			      br3.close();
			      br4.close();
			      br5.close();



			    }catch(FileNotFoundException e){
			      System.out.println(e);
			    }catch(IOException e){
			      System.out.println(e);
			    }

				if(a==0)
				{
					port.writeByte((byte) 255);
					port.writeByte((byte) 1);
					port.writeByte((byte) 127);

					port.writeByte((byte) 255);
					port.writeByte((byte) 2);
					port.writeByte((byte) 127);

					port.writeByte((byte) 255);
					port.writeByte((byte) 3);
					port.writeByte((byte) 127);

					port.writeByte((byte) 255);
					port.writeByte((byte) 4);
					port.writeByte((byte) 127);

					port.writeByte((byte) 255);
					port.writeByte((byte) 5);
					port.writeByte((byte) 127);

					a++;
				}

				if(a==1)
				{

					if(ch=='1')
					{
				    	    	ac_mouth = (ac-250) * 150 / (650-250);
				    	    	befo_ac_mouth = ac_mouth;

				    	    	port.writeByte((byte) 255);
								port.writeByte((byte) 2);
								port.writeByte((byte) ac_mouth);

								port.writeByte((byte) 255);
								port.writeByte((byte) 1);
								port.writeByte((byte) ac_mouth);

								port.writeByte((byte) 255);
								port.writeByte((byte) 5);
								port.writeByte((byte) ac_mouth);

				    	    	if(ac<450){
				    	    		ac_agree = 110;
					    	    	befo_ac_agree = ac_agree;
				    	    	}
				    	    	else if(ac>=450){
				    	    		ac_agree = 145;
					    	    	befo_ac_agree = ac_agree;
				    	    	}

				    	    	port.writeByte((byte) 255);
								port.writeByte((byte) 3);
								port.writeByte((byte) ac_agree);

				    	    System.out.println("count2="+count2);
				    	    System.out.println("ac_y="+ac_y);

				    	    if(ac_y < 400)
				    	    {
				    	    	port.writeByte((byte) 255);
								port.writeByte((byte) 4);
								port.writeByte((byte) 164);
								b = 0;

				    	    }
				    	    else if(ac_y > 550)
				    	    {
				    	    	port.writeByte((byte) 255);
								port.writeByte((byte) 4);
								port.writeByte((byte) 90);
								b = 1;
				    	    }
					}
					else
					{
						port.writeByte((byte) 255);
						port.writeByte((byte) 2);
						port.writeByte((byte) befo_ac_mouth);

						port.writeByte((byte) 255);
						port.writeByte((byte) 3);
						port.writeByte((byte) befo_ac_agree);

						if(b == 0)
						{
							port.writeByte((byte) 255);
							port.writeByte((byte) 4);
							port.writeByte((byte) 164);
						}
						else
						{
							port.writeByte((byte) 255);
							port.writeByte((byte) 4);
							port.writeByte((byte) 90);
						}

					}
				}
		}

	}

}