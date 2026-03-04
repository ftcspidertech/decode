package org.firstinspires.ftc.teamcode.decode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import org.firstinspires.ftc.teamcode.navigation.MecanumRobotController;
import org.firstinspires.ftc.teamcode.decode.FlyWheelController;
import org.firstinspires.ftc.teamcode.decode.IntakeController;
import org.firstinspires.ftc.teamcode.vision.LimeLight3ACamera;
import org.firstinspires.ftc.teamcode.navigation.NavigationType;

public class Utilities {
    public double[] robotPose = null;
    public double kAngle = 0.9; //Original was .9
    public double kDistance = 0.85;
    public double correctionTurnSpeed = 0.1;
    public double collectionMoveSpeed = 0.125;
    public double firstShootK = 1.05;
    public double secondShootK = 0.9;
    public double thirdShootK = 0.6;

    private ElapsedTime waitTimer = new ElapsedTime();
    
    private MecanumRobotController robotController;
    private FlyWheelController flyWheelController;
    private IntakeController intakeController;
    private LimeLight3ACamera limelight;
    private Telemetry telemetry;
    
    private boolean loopExit = false;
    private double CameraOffset = 6;
    
    public Utilities(MecanumRobotController rbtController,
        FlyWheelController fwController,
        IntakeController inController,
        LimeLight3ACamera camera,
        Telemetry tmetry){
            
            robotController = rbtController;
            flyWheelController = fwController;
            intakeController = inController;
            limelight = camera;
            telemetry = tmetry;
    }
    
    public void turnToGoalAndShoot(int goalID, int camWaitTime){
        // Speed up well ahead so that no delay in shooting.
        intakeController.setShootPower();
        
        // Turn towards the goal.
        turnToGoal(goalID,camWaitTime);

        // Return if no target in vision.
        if(robotPose==null){
            //telemetry.speak("No");
            intakeController.resetPower();
            return;
        }        

        // Shoot
        //if (distance<96){
            shootOnce();
        //}else{
        //    shootOneByOne(800, 200); //800 ms: the gate will be open; 200ms: intake will be stopped in between two shots 
        //}        
    }

    public void turnToGoalAndShoot(int goalID, int camWaitTime, double kLeft, double kRight){
        // Speed up well ahead so that no delay in shooting.
        intakeController.setShootPower();
        
        // Turn towards the goal.
        turnToGoal(goalID,camWaitTime,kLeft,kRight);

        // Return if no target in vision.
        if(robotPose==null){
            //telemetry.speak("No");
            intakeController.resetPower();
            return;
        }        

        // Shoot
        //if (distance<96){
            shootOnce();
        //}else{
        //    shootOneByOne(800, 200); //800 ms: the gate will be open; 200ms: intake will be stopped in between two shots 
        //}        
    }

    public void turnToGoalAndShoot(int goalID, int camWaitTime, int doorOpenTime){
        // Speed up well ahead so that no delay in shooting.
        intakeController.setShootPower();

        // Turn towards the goal.
        turnToGoal(goalID,camWaitTime);

        // Return if no balls in vision.
        if(robotPose==null){
            //telemetry.speak("No");
            intakeController.resetPower();
            return;
        }        

        // Shoot
        //if (distance<96){
            shootOnce(doorOpenTime);
        //}else{
        //    shootOneByOne(800, 200);
        //}        
    }

    public void turnToGoalAndShoot(int goalID, int camWaitTime, int doorOpenTime, double kLeft, double kRight){
        // Speed up well ahead so that no delay in shooting.
        intakeController.setShootPower();

        // Turn towards the goal.
        turnToGoal(goalID,camWaitTime,kLeft,kRight);

        // Return if no balls in vision.
        if(robotPose==null){
            //telemetry.speak("No");
            intakeController.resetPower();
            return;
        }        

        // Shoot
        //if (distance<96){
            shootOnce(doorOpenTime);
        //}else{
        //    shootOneByOne(800, 200);
        //}        
    }

    public void turnToGoalAndShootFromInside(int goalID, int camWaitTime, int doorOpenTime, double kLeft, double kRight){
        // Speed up well ahead so that no delay in shooting.
        intakeController.setShootPower();

        // Turn towards the goal.
        turnToGoal(goalID,camWaitTime,kLeft,kRight);

        // Return if no balls in vision.
        if(robotPose==null){
            //telemetry.speak("No");
            intakeController.resetPower();
            return;
        }        

        // Shoot
        shootOnceFromInside(doorOpenTime);
    }
    
    public void turnToGoalAndShootOneByOne(int goalID, int camWaitTime,
        int intakeOnTime, int intakeOffTime){
        // Turn towards the goal.
        turnToGoal(goalID,camWaitTime);

        // Return if no balls in vision.
        if(robotPose==null){
            //telemetry.speak("No");
            return;
        }        

        // Shoot
        shootOneByOne(intakeOnTime, intakeOffTime);
    }

    public void turnToGoalAndShootOneByOne(int goalID, int camWaitTime,
        int intakeOnTime, int intakeOffTime,double kLeft,double kRight){
        // Turn towards the goal.
        turnToGoal(goalID,camWaitTime,kLeft,kRight);

        // Return if no balls in vision.
        if(robotPose==null){
            //telemetry.speak("No");
            return;
        }        

        // Shoot
        shootOneByOne(intakeOnTime, intakeOffTime);
    }

    public void shootOnce(){
        flyWheelController.convertToLineVelocity(firstShootK*robotPose[2]);
        flyWheelController.openDoor();
        wait(666);
        flyWheelController.convertToLineVelocity(secondShootK*robotPose[2]);
        wait(666);
        flyWheelController.convertToLineVelocity(thirdShootK*robotPose[2]);
        wait(666);
    }
    
    public void shootOnce(int doorOpenTime){
        flyWheelController.convertToLineVelocity(firstShootK*robotPose[2]);
        
        intakeController.setShootPower();
        flyWheelController.openDoor();
        
        wait(doorOpenTime/3);
        flyWheelController.convertToLineVelocity(secondShootK*robotPose[2]);
        wait(doorOpenTime/3);
        flyWheelController.convertToLineVelocity(thirdShootK*robotPose[2]);
        wait(doorOpenTime/3);
        
        flyWheelController.closeDoor();
        intakeController.resetPower();
    }

    public void shootOnceFromInside(int doorOpenTime){
        flyWheelController.convertToInsideVelocity(1.1*robotPose[2]);
        
        intakeController.setShootPower();
        flyWheelController.openDoor();
        
        wait(doorOpenTime/3);
        flyWheelController.convertToInsideVelocity(0.9*robotPose[2]);
        wait(doorOpenTime/3);
        flyWheelController.convertToInsideVelocity(0.6*robotPose[2]);
        wait(doorOpenTime/3);
        
        flyWheelController.closeDoor();
        intakeController.resetPower();
    }
    
    public void shootOneByOne(int intakeOnTime, int intakeOffTime){
        double[] k = {0.9,0.8,0.7};
        flyWheelController.convertToAwayVelocity(k[1]*robotPose[2]);
        intakeController.stop();
        flyWheelController.openDoor();
        for(int count=0;count<3;count++){
            wait(intakeOffTime);
            if(!loopExit){
                intakeController.setShootPower();
            }
            wait(intakeOnTime);
            if(!loopExit){
                intakeController.stop();
                if(count<2){
                    flyWheelController.convertToAwayVelocity(k[count+1]*robotPose[2]);
                }
            }
        }
        flyWheelController.closeDoor();
        intakeController.resetPower();
    }

    public void turnToBallAndCollect(int camWaitTime, double kLeft, double kRight, 
        double moveSpeed, double kDistance, int timeout){
        // Turn towards a ball.
        turnToBall(camWaitTime,kLeft,kRight);
        
        if(robotPose==null){
            return;
        }
        
        // Collect ball.
        int preTimeout = robotController.timeout;
        robotController.timeout = timeout;
        //robotController.goForward(moveSpeed, kDistance*robotPose[2],true);
        //robotController.goToPosition(moveSpeed, kDist*robotPose[2], DistanceUnit.INCH, NavigationType.GOFORWARD);
        robotController.goToPositionWithSpeedModulation(moveSpeed, kDistance*robotPose[2], 0.5, 3, DistanceUnit.INCH);
        robotController.timeout = preTimeout;
    }

    public void turnToBallAndCollect(int camWaitTime, double kLeft, double kRight, 
        double moveSpeed, double kDistance){
        // Turn towards a ball.
        turnToBall(camWaitTime,kLeft,kRight);
        
        if(robotPose==null){
            //telemetry.speak("No");
            return;
        }
        
        // Collect ball.
        //robotController.goForward(moveSpeed, kDistance*robotPose[2],true);
        robotController.goToPosition(moveSpeed, kDistance*robotPose[2], DistanceUnit.INCH, NavigationType.GOFORWARD);
        //robotController.goToPositionWithSpeedModulation(moveSpeed, kDistance*robotPose[2], 0.5, 3, DistanceUnit.INCH);
    }
    
    public void turnToBallAndCollect(int camWaitTime){
        // Turn towards a ball.
        turnToBall(camWaitTime);
        
        if(robotPose==null){
            //telemetry.speak("No");
            return;
        }
        
        // Collect ball.
        robotController.goForward(collectionMoveSpeed, kDistance*robotPose[2]);
        
        //wait(100);
        //robotController.goForward(collectionMoveSpeed, robotPose[2]/22.0, TimeUnit.SECONDS);
        
        //robotController.goToPosition(collectionMoveSpeed, robotPose[2], DistanceUnit.INCH, NavigationType.GOFORWARD);
        
        //robotController.goForward(collectionMoveSpeed);
    }

    public void turnToGoal(int gID, int camWaitTime){
        robotPose = limelight.getRobotPoseRelativeToGoal(gID,camWaitTime);
        if(robotPose==null){
            //telemetry.addLine("Goal out of vision!");
            return;
        }
        turnRobot();
    }

    public void turnToGoal(int gID, int camWaitTime, double kLeft, double kRight){
        robotPose = limelight.getRobotPoseRelativeToGoal(gID,camWaitTime);
        if(robotPose==null){
            //telemetry.addLine("Goal out of vision!");
            return;
        }
        turnRobot(kLeft,kRight);
    }

    public void turnToBall(int camWaitTime, double kLeft, double kRight){
        robotPose = limelight.getRobotPoseRelativeToBall(camWaitTime);
        if(robotPose==null){
            //telemetry.addLine("Ball out of vision!");
            return;
        }
        robotPose[0] = robotPose[0] + CameraOffset;
        turnRobot(kLeft,kRight); // these two are modulating factors for left/right rotation
    }


    public void turnToBall(int camWaitTime){
        robotPose = limelight.getRobotPoseRelativeToBall(camWaitTime,false); // Use true to get average info
        if(robotPose==null){
            //telemetry.addLine("Ball out of vision!");
            return;
        }
        robotPose[0] = robotPose[0] + CameraOffset;
        turnRobot(0.9,0.9); // these two are modulating factors for left/right rotation
    }

    private void turnRobot(){
        wait(100);
        if(robotPose[0]<0){
            robotController.turnLeft(correctionTurnSpeed,Math.abs(kAngle*robotPose[0]));
        }else if(robotPose[0]>0){
            robotController.turnRight(correctionTurnSpeed,Math.abs(kAngle*robotPose[0]));
        }
    }

    private void turnRobot(double k){
        wait(100);
        if(robotPose[0]<0){
            robotController.turnLeft(correctionTurnSpeed,Math.abs(k*robotPose[0]));
        }else if(robotPose[0]>0){
            robotController.turnRight(correctionTurnSpeed,Math.abs(k*robotPose[0]));
        }
    }

    private void turnRobot(double kLeft, double kRight){
        //telemetry.addData("tx",robotPose[0]);
        wait(200);
        if(robotPose[0]<0){
            //telemetry.addData("tx",kLeft*robotPose[0]);
            robotController.turnLeft(correctionTurnSpeed,Math.abs(kLeft*robotPose[0]));
        }else if(robotPose[0]>0){
            //telemetry.addData("tx",kLeft*robotPose[0]);
            robotController.turnRight(correctionTurnSpeed,Math.abs(kRight*robotPose[0]));
        }
    }
    
    public void openDoor(){
        intakeController.setShootPower();
        flyWheelController.openDoor();
    }
    
    public void wait(int ms){
        waitTimer.reset();
        loopExit = false;
        while(!loopExit && waitTimer.milliseconds()<ms);
    }
    
    public void resetRobot(){
        loopExit = true;
        robotController.stop();
        flyWheelController.closeDoor();
        intakeController.start();        
    }
}
