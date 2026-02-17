package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.decode.BLUE_AutoDecode;
import org.firstinspires.ftc.teamcode.navigation.TurnMethod;

@Autonomous()
public class BLUE_Auto_Position2  extends BLUE_AutoDecode{
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
    private double kGoalLeft = 1.0;
    private double kGoalRight = 0.80;
    private double kBallLeft = 0.25;
    private double kBallRight = 0.5;
    private int shootTime = 2000; // milliseconds
    private double kDistance;
    
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
            utilities.turnToGoalAndShoot(goalID,200,shootTime,kGoalLeft,kGoalRight);
            if(utilities.robotPose==null){
                //telemetry.addLine("Task1-No-Shoot");
                //telemetry.update();
                //telemetry.addLine("No");
                utilities.openDoor();
                wait(shootTime);
                flyWheelController.closeDoor();
            }
            setBallPipelineID(100);
            
            // Task1 done             
            taskOneDone = true;
        }else if(!taskTwoDone){
            // Go back and turn to the 2nd set of balls.
            robotController.turnLeft(turnPower,33);
            wait(100);
            robotController.slideLeft(movePower*.5,1250,TimeUnit.MILLISECONDS);
            //wait(100);

            kDistance = 0.85;
            utilities.turnToBallAndCollect(200,kBallLeft,kBallRight,movePower,kDistance);            
            if(utilities.robotPose==null){
                robotController.goForward(movePower*.5,2.3,TimeUnit.SECONDS);
            }
            setGoalPipelineID(100);
            wait(100);
            robotController.goBackward(movePower*.5,2.2,TimeUnit.SECONDS);
            
            //Go back and turn to the goal.
            robotController.turnRight(turnPower,45);
            wait(100);
            utilities.turnToGoalAndShoot(goalID,200,shootTime,kGoalLeft,kGoalRight);
            if(utilities.robotPose==null){
                //telemetry.addLine("Task2-No-Shoot");
                //telemetry.update();
                utilities.openDoor();
                wait(2000);
                flyWheelController.closeDoor();
            }
            setBallPipelineID(100);

            // Task2 done.
            taskTwoDone = true;
       }else if(!taskThreeDone){
            // Go and collect the 3rd set of balls.
            robotController.turnLeft(turnPower,45);
            wait(100);
            robotController.slideLeft(movePower*.5,2000,TimeUnit.MILLISECONDS);
            //wait(100);
            kBallLeft = 0.15;
            kBallRight = 0.5;
            //utilities.turnToBall(200,,kBallLeft,kBallRight);
            kDistance = 1.25;
            utilities.turnToBallAndCollect(200,kBallLeft,kBallRight,movePower,kDistance);
            if(utilities.robotPose==null){
                robotController.goForward(movePower*.5,2.5,TimeUnit.SECONDS);
            }
            setGoalPipelineID(100);
            
            wait(100);
            robotController.goBackward(movePower,1.25,TimeUnit.SECONDS);
            wait(100);
            robotController.turnRight(turnPower,35);
            wait(100);
            robotController.slideRight(movePower,1400,TimeUnit.MILLISECONDS);
            wait(100);
            utilities.turnToGoalAndShoot(goalID,200,shootTime,kGoalLeft,kGoalRight);
            if(utilities.robotPose==null){
                //telemetry.addLine("Task3-No-Shoot");
                //telemetry.update();
                utilities.openDoor();
                wait(2000);
                flyWheelController.closeDoor();
            }
            
            robotController.slideLeft(movePower,1000,TimeUnit.MILLISECONDS);

            // Task3 done.
            taskThreeDone = true;
       }else{
            stop();
        }
    }
}
