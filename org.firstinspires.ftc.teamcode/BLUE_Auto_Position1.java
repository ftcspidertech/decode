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
    private int doorOpenTime = 800;
    private int doorCloseTime = 700;
    private int camWaitTime = 200;

    private double kGoalLeft;
    private double kGoalRight;
    private double kBallLeft;
    private double kBallRight;
    private double kDistance;
    
    // Task variables
    // Task 1: Throw pre-loaded balls. 
    private boolean taskOneDone = false;
    // Task 2: Collect and throw another set of balls.
    private boolean taskTwoDone = false;
    // Task 3: If possible, collect balls from the loading zone.
    private boolean taskThreeDone = true;

    
    @Override
    public void loop() {
        intakeController.releaseBallIfStalled();
        //shootTime=2000;
        if(!taskOneDone) {
            // Go forward and turn towards the goal.
            goForward(movePower,0.5,TimeUnit.SECONDS);
            wait(200);
            robotController.turnLeft(turnPower,0.55,TimeUnit.SECONDS);// turn to shoot balls
            wait(150);
            kGoalLeft = 0.95;
            kGoalRight = 0.35;
            utilities.turnToGoalAndShootOneByOne(goalID,camWaitTime,doorOpenTime,doorCloseTime,kGoalLeft,kGoalRight);
            // utilities.turnToGoalAndShoot(goalID,camWaitTime,shootTime,kGoalLeft,kGoalRight);
            if(utilities.robotPose==null){
                shootWithoutCamera(shootTime);
                //telemetry.addLine("Task1-No-Shoot");
            }
            setBallPipelineID(100);
            
            // Task1 is done.
            taskOneDone = true;
        }else if(!taskTwoDone) {
            // Turn to get second set of balls.
            goForward(movePower,0.85,TimeUnit.SECONDS);
            wait(150);
            robotController.turnLeft(turnPower,0.9,TimeUnit.SECONDS);
            wait(150);
            kBallLeft = 0.7;
            kBallRight = 0.7;
            kDistance=1.25;
            //utilities.turnToBall(camWaitTime,kBallLeft,kBallRight);
            //wait(150);
            //goForward(movePower*.5,2.3,TimeUnit.SECONDS);
            utilities.turnToBallAndCollect(camWaitTime,kBallLeft,kBallRight,0.5*movePower,kDistance,2400);
            if (utilities.robotPose==null){
                goForward(movePower*.5,2.3,TimeUnit.SECONDS);
                //telemetry.addLine("Didn't see 1st set of balls");
            }
            setGoalPipelineID(100);
            wait(150);
            robotController.goBackward(movePower*.5,2.1,TimeUnit.SECONDS);
            wait(150);
            robotController.turnRight(turnPower,0.85,TimeUnit.SECONDS);
            wait(150);
            robotController.goBackward(movePower,1.05,TimeUnit.SECONDS);
            wait(150);
            kGoalRight=0.7;
            kGoalLeft=0.6;
            utilities.turnToGoalAndShootOneByOne(goalID,camWaitTime,doorOpenTime,doorCloseTime,kGoalLeft,kGoalRight);
            if(utilities.robotPose==null){
                shootWithoutCamera(shootTime);
                //telemetry.addLine("Task2-No-Shoot");
            }
            
            if(taskThreeDone){ // If the robot does not aim for the third task.
                goForward(movePower,1.0,TimeUnit.SECONDS);
            }
            
            setBallPipelineID(100);
            
            // Task2 is done.
            taskTwoDone = true;
        }else if(!taskThreeDone) {
            robotController.turnLeft(turnPower,0.86,TimeUnit.SECONDS);
            wait(150);
            robotController.slideRight(movePower,700,TimeUnit.MILLISECONDS);
            wait(150);
            kBallLeft=0.25; // Don't turn too much. It may bump into the wall.
            kBallRight=0.25;
            kDistance = 1.25;
            //utilities.turnToBall(camWaitTime,kBallLeft,kBallRight);
            //goForward(movePower*.5,3,TimeUnit.SECONDS);
            utilities.turnToBallAndCollect(camWaitTime,kBallLeft,kBallRight,0.5*movePower,kDistance,3000);
            if (utilities.robotPose==null){
                goForward(movePower*.5,3,TimeUnit.SECONDS);
                //telemetry.addLine("Didn't see 2nd set of balls");
            }
            wait(150);
            robotController.goBackward(movePower*.5,2.3,TimeUnit.SECONDS);

            // Task3 is done.
            taskThreeDone = true;
        }else{
            stop();
        }
    }
    
}
