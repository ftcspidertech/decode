package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.decode.AutoDecode;
import org.firstinspires.ftc.teamcode.navigation.TurnMethod;

@Autonomous()
public class RED_Auto_Position1 extends AutoDecode{
    /*
    *  Controller variables from the superclass:
    *      - robotController
    *      - flyWheelController
    *      - IntakeController
    */
    
    // Navigation variables   
    private double movePower = 0.65;
    private double turnPower = 0.5;
    private boolean useSpeedCorrection = true;
    
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
            robotController.goForward(movePower,72,DistanceUnit.INCH,useSpeedCorrection);
            wait(300);
            robotController.turnRight(turnPower,41,TurnMethod.IMU);
            wait(2000);

            // Open the exit door. Keep it open for long enough to throw all the balls.
            flyWheelController.openDoor();
            wait(7000);
            flyWheelController.closeDoor();
            
            // Task1 is done.
            taskOneDone = true;
        }else if(!taskTwoDone) {
            // Turn to get second set of balls.
           
           //wait(200)
            robotController.turnRight(turnPower,35,TurnMethod.IMU);
            
            // Move forward to aqquire balls.
            robotController.goForward(movePower*0.8,35,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            
            // Go back and turn to the scoring location.
            robotController.goBackward(movePower*0.8,31,DistanceUnit.INCH,useSpeedCorrection);
            wait(300);
            robotController.turnLeft(turnPower,33,TurnMethod.IMU);
            
            // Open the exit door. Keep it open for long enough to throw all the balls.
            flyWheelController.openDoor();
            wait(6500);
            flyWheelController.closeDoor();
            robotController.turnLeft(1,30,TurnMethod.IMU);
            robotController.goBackward(1.0,30,DistanceUnit.INCH,useSpeedCorrection);

            // Task2 is done.
            taskTwoDone = true;
        }else{
            stop();
        }
    }
    
}
