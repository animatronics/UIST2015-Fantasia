/**
 * A suite of software provided for the UIST 2015 Student Contest. 
 * <p> 
 * <b>Basic Functionality: </b> A complete show player is provided {@link jacs.player} that will play back any prerecorded show stored as a
 * {@link FormattedShowData}.  Tools for converting .WAV audio file and accompanying .CSV file with 
 * servo motions into the required format are provided in {@link jacs.utilities} and demonstrations of
 * how to use this with the API are provided in {@link jacs.demos}.
 * <p> 
 * <b>Extended Functionality: </b> Hooks are provided in the code to
 * enable simultaneous real time capture of motion data synchronized to playback of a recorded show. 
 * Hooks are also provided to enable immediate simultaneous playback of captured microphone data 
 * during a recorded show playback.  
 * <p> 
 * <b>Contest Goals: </b> Contestants are encouraged to provide these functionality in their entries.  A goal of the contest is to provide an integrated toolset for children to create animatronics
 * shows comprised of these stages:  (1) write dialogue for a character, (2) physically build 
 * a character (3) record the voice for a character (audio) and (2) perform (servo motion) 
 * character movement and expression.
 * <p>
 * <b>Sample Programs: </b> Demo programs and sample data files are provided in {@link jacs.demos} as tools to verify basic device communications, 
 * test the range of servo motion and study samples of prerecorded shows.
 * <p>
 * <b>Utilities: </b>Utilities are provided in {@link jacs.utilities} for convenient data conversions, 
 * data and file formatting and procedurally generating motion tracks patterns for servo.
 * Contestants are encouraged to extend these utilities.
 * <p>
 * <b>Platform: </b> The show player works across operating systems and relies on the system audio and
 * system serial ports to mediate audio playback and Mini-SSC commands over a USB connection to a 
 * microcontroller card for controlling servo motion.  The {@link jacs.config} contains
 * code to manage the port connection.  This package requires the {@link jmcc} package and 
 * the {@link jssc} package.
 * <p>
 * 
 * @author Ginger Alford
 *
 */

package jacs;