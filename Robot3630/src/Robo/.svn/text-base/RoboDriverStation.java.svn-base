/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Robo;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO.EnhancedIOException;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author paul
 */
public class RoboDriverStation {
    final int port1 = 1;
    final int port2 = 2;

    public Joystick leftDriveStick;
    public Joystick rightDriveStick;
    public DriverStation ds; // driver station object for getting selections
    public DriverStationEnhancedIO eio;

    private final int buttonChannels[] = { 14, 16, 1, 15, 10, 13, 9};
    private final int ledChannels[] = {4, 8, 12, 5, 3, 7, 11};

    RoboDriverStation(){
        leftDriveStick = new Joystick(port1);
        rightDriveStick = new Joystick(port2);
        ds = DriverStation.getInstance();
        eio = ds.getEnhancedIO();
        try {
            // initialize DIO outputs for LED support
            for(int i = 0; i < 7; i++){
                eio.setDigitalConfig(ledChannels[i], DriverStationEnhancedIO.tDigitalConfig.kOutput);
                eio.setDigitalConfig(buttonChannels[i], DriverStationEnhancedIO.tDigitalConfig.kInputPullDown);
            }
        } catch (EnhancedIOException ex) {
            // ex.printStackTrace();
        }
        setLed(-1);
    }
    public int getPegButton(){
        try {
            short d = eio.getDigitals();
            for(int i = 0; i < 7; i++){
                if((d & (1 << (buttonChannels[i] - 1))) != 0) return i;
            }
        } catch (EnhancedIOException ex) {
            // ex.printStackTrace();
        }
        return -1;
    }
    public void setLed(int index){
        for(int i = 0; i < 7; i++){
            setLed(i, i == index);
        }
    }
    public void setLed(int index, boolean value){
        try {
            eio.setDigitalOutput(ledChannels[index], !value);
        } catch (EnhancedIOException ex) {
            // ex.printStackTrace();
        }
    }
}
