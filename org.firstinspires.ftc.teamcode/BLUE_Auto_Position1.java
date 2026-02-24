package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.decode.BLUE_AutoDecode;
import org.firstinspires.ftc.teamcode.navigation.TurnMethod;

@Autonomous()
public class BLUE_Auto_Position1 extends BLUE_AutoDecode{
    /*
    *  Controller variables from the superclass:
    *      - robotController
    *      - flyWheelController
    *      - IntakeController
    */

    
     private double movePower = .25;
    private double turnPower = 0.2;
    private boolean useSpeedCorrection = true;
    private double kGoalLeft = 0.95;
    private double kGoalRight = 0.35;
    private double kBallLeft = 0.25;
    private double kBallRight = 0.5;
    private int shootTime = 2000; // milliseconds
    private double kDistance;
    private double doorOpenTime = 800;
    private double doorCloseTime = 200;
    
    // Task variables
    // Task 1: Throw pre-loaded balls. 
    private boolean taskOneDone = false;
    // Task 2: Collect and throw another set of balls.
    private boolean taskTwoDone = false;
    private boolean taskThreeDone = true;
    

    
    @Override
    public void loop() {
        intakeController.stopIfStalled();
        if(!taskOneDone) {
            // Go forward and turn towards the goal.
            robotController.goForward(movePower,0.5,TimeUnit.SECONDS);
            wait(200);
            robotController.turnLeft(turnPower,0.55,TimeUnit.SECONDS);// turn to shoot balls
            wait(150);
            utilities.turnToGoalAndShootOneByOne(goalID,200,800,700,kGoalLeft,kGoalRight);
             //utilities.turnToGoalAndShoot(goalID,200,shootTime,kGoalLeft,kGoalRight);
              if(utilities.robotPose==null){
                telemetry.addLine("Task1-No-Shoot");
                telemetry.update();
                shootWithoutCamera(shootTime);
            }
            setBallPipelineID(100);
            
            // Task1 is done.
            taskOneDone = true;
        }else if(!taskTwoDone) {
            // Turn to get second set of balls.
            robotController.goForward(movePower,0.7,TimeUnit.SECONDS);
            wait(150);
            robotController.turnLeft(turnPower,0.9,TimeUnit.SECONDS);
            wait(150);
            utilities.turnToBall(200,kBallLeft,kBallRight);
            if (utilities.robotPose==null){
                telemetry.addLine("Didn't see 1st set of balls");
            }
            setGoalPipelineID(100);
            wait(150);
            robotController.goForward(movePower*.5,2.3,TimeUnit.SECONDS);
            wait(150);
            robotController.goBackward(movePower*.5,2.2,TimeUnit.SECONDS);
            wait(150);
            robotController.turnRight(turnPower,0.8,TimeUnit.SECONDS);
            wait(150);
            robotController.goBackward(movePower,0.75,TimeUnit.SECONDS);
            wait(150);
            kGoalRight=0.7;
            utilities.turnToGoalAndShootOneByOne(goalID,300,800,700,kGoalLeft,kGoalRight);
            //utilities.turnToGoalAndShoot(goalID,200,shootTime,kGoalLeft,kGoalRight);
              if(utilities.robotPose==null){
                telemetry.addLine("Task2-No-Shoot");
                telemetry.update();
                shootWithoutCamera(shootTime);
            }
            setBallPipelineID(100);
            // Task2 is done.
            taskTwoDone = true;
        }else if(!taskThreeDone) {
            robotController.turnLeft(turnPower,0.9,TimeUnit.SECONDS);
            utilities.turnToBall(200,kBallLeft,kBallRight);
            if (utilities.robotPose==null){
                telemetry.addLine("Didn't see 2nd set of balls");
            }
            wait(150);
            robotController.slideLeft(movePower,800,TimeUnit.MILLISECONDS);
            wait(150);
            robotController.goForward(movePower*.5,3,TimeUnit.SECONDS);
            wait(150);
            robotController.goBackward(movePower*.5,2.5,TimeUnit.SECONDS);
            taskThreeDone = true;
        }else{
            stop();
        }
    }
    
}
