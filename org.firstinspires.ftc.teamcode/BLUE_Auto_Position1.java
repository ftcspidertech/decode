package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import java.util.concurrent.TimeUnit;
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
        intakeController.stopIfStalled();
        if(!taskOneDone) {
            // Go forward and turn towards the goal.
            flyWheelController.setVelocity(1650);
            robotController.goForward(movePower,5,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            robotController.turnLeft(turnPower,20,TurnMethod.IMU);// turn to shoot balls
            wait(1500);
            openDoor();
            wait(3000);
            flyWheelController.closeDoor();
            wait(100);
            robotController.goForward(movePower,18,DistanceUnit.INCH,useSpeedCorrection);//move forward to aqquire second row of balls
            wait(200);
            robotController.turnLeft(turnPower,64,TurnMethod.IMU); //turn to get balls
            wait(200);
            robotController.goForward(movePower,41,DistanceUnit.INCH,useSpeedCorrection);//move forward
            wait(400);
            robotController.goBackward(movePower,41,DistanceUnit.INCH,useSpeedCorrection);// move back with balls
            wait(200);
            robotController.turnRight(turnPower,61,TurnMethod.IMU);
            wait(200);
            robotController.goBackward(movePower,20,DistanceUnit.INCH,useSpeedCorrection);// move back to home to shoot
            wait(100);
            openDoor();//shoot second line of balls
            wait(3000);
            flyWheelController.closeDoor();
            wait(200);
            robotController.slideRight(movePower);
            wait(400);
            robotController.goForward(movePower,39,DistanceUnit.INCH,useSpeedCorrection);//move forward to get third line of balls
            wait(200);
            robotController.turnLeft(turnPower,64,TurnMethod.IMU); //turn to get 3RD row of balls
            wait(200);
            robotController.goForward(movePower,40,DistanceUnit.INCH,useSpeedCorrection);//move forward to get 3RD line of balls
            wait(200);
            robotController.goBackward(movePower,40,DistanceUnit.INCH,useSpeedCorrection);//move back with 3RD line of balls
            
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
