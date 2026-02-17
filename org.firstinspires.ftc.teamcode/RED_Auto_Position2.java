package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.decode.RED_AutoDecode;
import org.firstinspires.ftc.teamcode.navigation.TurnMethod;

@Autonomous()
public class RED_Auto_Position2  extends RED_AutoDecode{
    /*
    *  Controller variables from the superclass:
    *      - robotController
    *      - flyWheelController
    *      - IntakeController
    */
    
    // Navigation variables   
    private double movePower = .25;
    private double turnPower = 0.2;
    private boolean useSpeedCorrection = true;
    private double kGoalLeft = 0.75;
    private double kGoalRight = 0.9;
    private double kBallLeft = 0.25;
    private double kBallRight = 0.5;
    private int shootTime = 2250; // milliseconds
    private double kDistance = 1.0;
    
    // Task variables
    // Task 1: Throw pre-loaded balls. 
    private boolean taskOneDone = false;
    // Task 2: Collect and throw another set of balls.
    private boolean taskTwoDone = false;
    private boolean taskThreeDone = false;
    private int regularFlywheelSpeed=1425;
    private int lowerFlywheelSpeed=1300;
    
     
    
    @Override
    public void loop() {
        intakeController.stopIfStalled();
        if(!taskOneDone) {
            // Go forward and turn to the goal.
            //flyWheelController.setVelocity(regularFlywheelSpeed);
            robotController.goBackward(movePower,1.4,TimeUnit.SECONDS);
            wait(100);
            // Open the exit door. Keep it open for long enough to throw all the balls.
            utilities.turnToGoalAndShoot(goalID,300,shootTime,kGoalLeft,kGoalRight);
            if(utilities.robotPose==null){
                utilities.openDoor();
                wait(2000);
                flyWheelController.closeDoor();
            }
            
            // Task1 done             
            taskOneDone = true;
        }else if(!taskTwoDone){
            // Go back and turn to the 2nd set of balls.
            robotController.turnRight(turnPower,33);
            wait(100);
            robotController.slideRight(movePower*.5,1300,TimeUnit.MILLISECONDS);
            //wait(100);

            kDistance = 0.95;
            utilities.turnToBallAndCollect(200,kBallLeft,kBallRight,movePower,kDistance);
            
            if(utilities.robotPose==null){
                robotController.goForward(movePower*.5,2.3,TimeUnit.SECONDS);
            }
            wait(100);
            robotController.goBackward(movePower*.5,2.1,TimeUnit.SECONDS);
            
            //Go back and turn to the goal.
            robotController.turnLeft(turnPower,31);
            wait(100);
            utilities.turnToGoalAndShoot(goalID,300,shootTime,kGoalLeft,kGoalRight);
            if(utilities.robotPose==null){
                utilities.openDoor();
                wait(2000);
                flyWheelController.closeDoor();
            }

            // Task2 done.
            taskTwoDone = true;
       }else if(!taskThreeDone){
            // Go and collect the 2nd set of balls.
            robotController.turnRight(turnPower,43);
            wait(100);
            robotController.slideRight(movePower*.5,2100,TimeUnit.MILLISECONDS);
            //wait(100);
            kBallLeft = 0.15;
            kBallRight = 0.5;
            //utilities.turnToBall(200,,kBallLeft,kBallRight);
            kDistance = 1.25;
            utilities.turnToBallAndCollect(200,kBallLeft,kBallRight,movePower,kDistance);
            if(utilities.robotPose==null){
                robotController.goForward(movePower*.5,2.5,TimeUnit.SECONDS);
            }
            wait(100);
            robotController.goBackward(movePower,1.25,TimeUnit.SECONDS);
            wait(100);
            robotController.turnLeft(turnPower,25);
            wait(100);
            robotController.slideLeft(movePower,1300,TimeUnit.MILLISECONDS);
            wait(200);
            utilities.turnToGoalAndShoot(goalID,200,shootTime,kGoalLeft,kGoalRight);
            if(utilities.robotPose==null){
                utilities.openDoor();
                wait(2000);
                flyWheelController.closeDoor();
            }

            robotController.goBackward(2*movePower,0.6,TimeUnit.SECONDS);

            // Task3 done.
            taskThreeDone = true;
       }else{
            stop();
        }
    }
}
