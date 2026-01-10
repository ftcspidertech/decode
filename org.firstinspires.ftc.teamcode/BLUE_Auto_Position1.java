package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.decode.AutoDecode;
import org.firstinspires.ftc.teamcode.navigation.TurnMethod;

@Autonomous()
public class BLUE_Auto_Position1 extends AutoDecode{
    /*
    *  Controller variables from the superclass:
    *      - robotController
    *      - flyWheelController
    *      - IntakeController
    */
    
    // Navigation variables   
    private double movePower = 0.65;
    private double turnPower = 0.5;
    private boolean useSpeedCorrection = false;
    
    // Task variables
    // Task 1: Throw pre-loaded balls. 
    private boolean taskOneDone = false;
    // Task 2: Collect and throw another set of balls.
    private boolean taskTwoDone = false;
        
    @Override
    public void loop() {
        //double left=1.0;
        //double right=0.87;
        //setCalibrationFactors(left,right);
        if(!taskOneDone) {
            // Go forward and turn towards the goal.
            robotController.goForward(movePower,48,DistanceUnit.INCH,useSpeedCorrection);
            wait(300);
            robotController.turnLeft(turnPower,28,TurnMethod.IMU);
            wait(200);

            // Open the exit door. Keep it open for long enough to throw all the balls.
            flyWheelController.openDoor();
            wait(6000);
            flyWheelController.closeDoor();
            
            // Task1 is done.
            taskOneDone = true;
        }else if(!taskTwoDone) {
            // Turn to get second set of balls.
            robotController.turnLeft(turnPower,44,TurnMethod.IMU);
            
            // Move forward to aqquire balls.
            robotController.goForward(movePower*0.8,35,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            
            // Go back and turn to the scoring location.
            robotController.goBackward(movePower*0.8,35,DistanceUnit.INCH,useSpeedCorrection);
            wait(300);
            robotController.turnRight(turnPower,44,TurnMethod.IMU);
            
            // Open the exit door. Keep it open for long enough to throw all the balls.
            flyWheelController.openDoor();
            wait(6000);
            flyWheelController.closeDoor();
            
            // Task2 is done.
            taskTwoDone = true;
        }else{
            stop();
        }
    }
    
}
