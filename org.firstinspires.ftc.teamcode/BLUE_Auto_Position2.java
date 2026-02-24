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
    private double kGoalLeft = 1.1;
    private double kGoalRight = 0.70;
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
     
    
    @Override
    public void loop() {
        intakeController.stopIfStalled();
        if(!taskOneDone) {
            // Go forward and turn to the goal.
            //robotController.goBackward(movePower*0.8,1.75,TimeUnit.SECONDS);
            robotController.goBackward(movePower*0.8,45,useSpeedCorrection);
            wait(75);
            // Open the exit door. Keep it open for long enough to throw all the balls.
            utilities.turnToGoalAndShoot(goalID,200,shootTime,kGoalLeft,kGoalRight);
            if(utilities.robotPose==null){
                telemetry.addLine("Task1-No-Shoot");
                shootWithoutCamera(shootTime);
            }
            setBallPipelineID(100);
            
            // Task1 done             
            taskOneDone = true;
        }else if(!taskTwoDone){
            // Go back and turn to the 2nd set of balls.
            robotController.turnLeft(turnPower,33);
            wait(75);
            robotController.slideLeft(movePower*.5,1250,TimeUnit.MILLISECONDS);
            //wait(100);

            kDistance = 0.8;
            utilities.turnToBallAndCollect(200,kBallLeft,kBallRight,movePower,kDistance);            
            if(utilities.robotPose==null){
                robotController.goForward(movePower*.5,2.3,TimeUnit.SECONDS);
            }
            setGoalPipelineID(100);
            wait(100);
            robotController.goBackward(movePower*.5,2.2,TimeUnit.SECONDS);
            
            //Go back and turn to the goal.
            robotController.turnRight(turnPower,45);
            wait(75);
            utilities.turnToGoalAndShoot(goalID,200,shootTime,kGoalLeft,kGoalRight);
            if(utilities.robotPose==null){
                //telemetry.addLine("Task2-No-Shoot");
                //telemetry.update();
                shootWithoutCamera(shootTime);
            }
            setBallPipelineID(100);

            // Task2 done.
            taskTwoDone = true;
       }else if(!taskThreeDone){
            // Go and collect the 3rd set of balls.
            robotController.turnLeft(turnPower,35);
            wait(75);
            robotController.slideLeft(movePower*.5,2150,TimeUnit.MILLISECONDS);
            //wait(100);
            kBallLeft = 0.25;//0.15
            kBallRight = 0.5;
            //utilities.turnToBall(200,,kBallLeft,kBallRight);
            kDistance = 1.0;
            utilities.turnToBallAndCollect(200,kBallLeft,kBallRight,0.9*movePower,kDistance);
            if(utilities.robotPose==null){
                robotController.goForward(movePower*.5,2.7,TimeUnit.SECONDS);
            }
            setGoalPipelineID(100);
            
            wait(75);
            robotController.goBackward(movePower,1.0,TimeUnit.SECONDS);
            wait(75);
            robotController.turnRight(turnPower,35);
            wait(75);
            robotController.slideRight(movePower,1600,TimeUnit.MILLISECONDS);
            wait(75);
            utilities.turnToGoalAndShoot(goalID,200,shootTime,kGoalLeft,kGoalRight);
            if(utilities.robotPose==null){
                //telemetry.addLine("Task3-No-Shoot");
                //telemetry.update();
                shootWithoutCamera(shootTime);
            }
            
            robotController.slideLeft(movePower,1300,TimeUnit.MILLISECONDS);

            // Task3 done.
            taskThreeDone = true;
       }else{
            stop();
        }
    }
}
