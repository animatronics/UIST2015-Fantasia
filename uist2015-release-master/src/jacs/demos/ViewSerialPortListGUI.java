package jacs.demos;

import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * GUI alternative for console output utility in ViewSerialPortList.java Please see detailed notes in
 * {@link ViewSerialPortList}
 * 
 * On Mac OS X, the Pololu port names begin with "/dev/cu.usbmodem*" and the correct port will be listed first.
 * 
 * On Windows 7, the Pololu port names begin with "COM" and the correct port will be listed next to last.
 * 
 * A Pololu Micro Maestro has two ports assigned. The first one listed is the one to which Mini-SSC commands should be
 * sent. An Arduino has only one port assigned.
 * 
 * 
 * @author galford
 *
 */
public class ViewSerialPortListGUI extends JFrame {

	JLabel portsListLabel;
	JComboBox<String> portsListComboBox; // Choice of shows
	JLabel UISTinfo;

	public ViewSerialPortListGUI() {

		this.setLayout(new FlowLayout());
		this.setVisible(true);
		this.setSize(600, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		portsListLabel = new JLabel("Serial Ports");
		portsListComboBox = new JComboBox<String>(ViewSerialPortList.getPorts());
		UISTinfo = new JLabel("TROUBLESHOOTING TIP:  Verify the player is sending commands to the correct port.");

		this.add(portsListLabel);
		this.add(portsListComboBox);
		this.add(UISTinfo);

		this.validate();

	}

	public static void main(String args[]) throws Exception {

		new ViewSerialPortListGUI();

	}

}
