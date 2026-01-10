package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.decode.AutoDecode;

@Autonomous()
public class BLUE_Auto_Position2  extends AutoDecode{
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
    public void init_loop(){
        flyWheelController.setVelocity(1350);
    }
    
    @Override
    public void loop() {
        //double left=1.0;
        //double right=0.87;
        //setCalibrationFactors(left,right);
        
        if(!taskOneDone) {
            // Go forward and turn to the goal.
            robotController.goForward(movePower,50,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            robotController.turnLeft(turnPower,160);
            wait(200);
            
            // Open the exit door. Keep it open for long enough to throw all the balls.
            flyWheelController.openDoor();
            wait(6000);
            flyWheelController.closeDoor();
            
            // Task1 done             
            taskOneDone = true;
        }else if(!taskTwoDone){
            // Go back and turn to the 2nd set of balls.
            robotController.goBackward(movePower,8,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            robotController.turnLeft(turnPower,35);
            wait(200);
            
            // Go and collect the balls. Go slowly to grab the balls.
            robotController.goForward(movePower*0.7,31,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            
            // Go back and turn to the goal.
            robotController.goBackward(movePower,27,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            robotController.turnRight(turnPower,35);
            wait(200);
            
            // Open the exit door. Keep it open for long enough to throw all the balls.
            flyWheelController.openDoor();
            wait(10000);
            flyWheelController.closeDoor();
            
            // Task2 done.
            taskTwoDone = true;
        }else{
            stop();
        }
    }
}
