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
    private double movePower = 0.25;
    private double turnPower = 0.2;
    private boolean useSpeedCorrection = true;
    private double kGoalLeft = 0.5; //0.7
    private double kGoalRight = 1.1;
    private double kBallLeft = 0.5;
    private double kBallRight = 0.5;
    private double kDistance;
    
    // Task variables
    // Task 1: Throw pre-loaded balls. 
    private boolean taskOneDone = false;
    // Task 2: Collect and throw another set of balls.
    private boolean taskTwoDone = false;
    private boolean taskThreeDone = false;
    
     
    
    @Override
    public void loop() { 
        intakeController.releaseBallIfStalled();
        shootTime = 2000;
        if(!taskOneDone) {
            // Go forward and turn to the goal.
            //robotController.goBackward(movePower*0.8,1.75,TimeUnit.SECONDS);
            //robotController.goBackward(movePower*0.9,45,useSpeedCorrection);
            goBackward(movePower,45,1500); //inputs: speed,distance,timeout
            wait(150);
            // Open the exit door. Keep it open for long enough to throw all the balls.
            //shootTime=2000;
            utilities.turnToGoalAndShoot(goalID,200,shootTime,kGoalLeft,kGoalRight);
            if(utilities.robotPose==null){
                shootWithoutCamera(shootTime);
                //telemetry.addLine("Task1-No-Shoot");
            }
            setBallPipelineID(100);
            wait(150);
            
            // Task1 done             
            taskOneDone = true;
        }else if(!taskTwoDone){
            // Go back and turn to the 2nd set of balls.
            robotController.turnRight(turnPower,28);
            wait(100);
            robotController.slideRight(movePower*.5,1200,TimeUnit.MILLISECONDS);
            wait(150);

            kDistance = 1.15;
            //utilities.turnToBallAndCollect(200,kBallLeft,kBallRight,0.6*movePower,kDistance,3000);            
            utilities.turnToBallAndCollect(200,kBallLeft,kBallRight,0.75*movePower,1.0,2030);            
            if(utilities.robotPose==null){
                goForward(movePower*.5,2.4,TimeUnit.SECONDS);
                //telemetry.addLine("Didn't see balls");
            }
            setGoalPipelineID(100);
            wait(100);
            robotController.goBackward(movePower*.5,2.3,TimeUnit.SECONDS);
            wait(100);
            //Go back and turn to the goal.
            robotController.turnLeft(turnPower,45);
            wait(100);
            kGoalRight=1.0;
            kGoalLeft=0;
            //shootTime=2000;
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
            robotController.turnRight(turnPower,42);
            wait(100);
            robotController.slideRight(movePower*.5,2350,TimeUnit.MILLISECONDS);
            wait(150);
            //utilities.turnToBall(200,,kBallLeft,kBallRight);
            kDistance = 1.2;
            utilities.turnToBallAndCollect(200,kBallLeft,kBallRight,0.75*movePower,kDistance,2060);
            if(utilities.robotPose==null){
                goForward(movePower*.5,3.0,TimeUnit.SECONDS);
            }
            setGoalPipelineID(100);
            
            wait(100);
            robotController.goBackward(movePower,1.0,TimeUnit.SECONDS);
            wait(300);
            robotController.turnLeft(turnPower,43);
            wait(100);
            robotController.slideLeft(movePower*1.2,1250,TimeUnit.MILLISECONDS);
            wait(150);
            kGoalLeft=0.3;
            kGoalRight=0.6;
            //shootTime=2000;
            utilities.turnToGoalAndShoot(goalID,200,shootTime,kGoalLeft,kGoalRight);
            if(utilities.robotPose==null){
                shootWithoutCamera(shootTime);
                //telemetry.addLine("Task3-No-Shoot");
            }
            wait(100);
            robotController.slideRight(2*movePower,1000,TimeUnit.MILLISECONDS);

            // Task3 done.
            taskThreeDone = true;
       }else{
            stop();
       }
    }
}
