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
    private double movePower = 0.9;
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
            flyWheelController.setVelocity(1650);
            robotController.goForward(movePower,5,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            robotController.turnRight(turnPower,25,TurnMethod.IMU);// turn to shoot balls
            wait(1500);
            flyWheelController.openDoor();
            wait(4000);
            flyWheelController.closeDoor();
            wait(100);
            robotController.goForward(movePower,22,DistanceUnit.INCH,useSpeedCorrection);//move forward to aqquire third row of balls
            wait(200);
            robotController.turnRight(turnPower,60,TurnMethod.IMU); //turn to get balls
            wait(200);
            robotController.goForward(movePower,35,DistanceUnit.INCH,useSpeedCorrection);//move forward
            wait(200);
            robotController.goBackward(movePower,35,DistanceUnit.INCH,useSpeedCorrection);// move back with balls
            wait(200);
            robotController.turnLeft(turnPower,63,TurnMethod.IMU);
            wait(200);
            robotController.goBackward(movePower,23,DistanceUnit.INCH,useSpeedCorrection);// move back to home to shoot
            wait(100);
            flyWheelController.openDoor();//shoot second line of balls
            wait(4000);
            flyWheelController.closeDoor();
            wait(200);
            robotController.slideLeft(movePower);
            wait(400);
            robotController.goForward(movePower,43,DistanceUnit.INCH,useSpeedCorrection);//move forward to get second line of balls
            wait(200);
            robotController.turnRight(turnPower,62,TurnMethod.IMU);//turn to second row of balls
            wait(200);
            robotController.goForward(movePower,40,DistanceUnit.INCH,useSpeedCorrection);//move forward to get second line of balls
            wait(200);
            robotController.goBackward(movePower,40,DistanceUnit.INCH,useSpeedCorrection);//move forward to get second line of balls
            
            // Task1 is done.
            taskOneDone = true;
        }else if(!taskTwoDone) {
            // Turn to get second set of balls.
           
           

            // Task2 is done.
            taskTwoDone = true;
        }else{
            stop();
        }
    }
    
}
