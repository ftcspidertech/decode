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
    
    private ElapsedTime waitTimer = new ElapsedTime();
    
    private MecanumRobotController robotController;
    private FlyWheelController flyWheelController;
    private IntakeController intakeController;
    private LimeLight3ACamera limelight;
    private Telemetry telemetry;
    
    private boolean loopExit = false;
    
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

        // Set fly wheel velocity.
        double distance = robotPose[2];
        flyWheelController.convertToVelocity(distance);
        
        // Shoot
        if (distance<96){
            shootOnce();
        }else{
            shootOneByOne(800, 200); //800 ms: the gate will be open; 200ms: intake will be stopped in between two shots 
        }        
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

        // Set fly wheel velocity.
        double distance = robotPose[2];
        flyWheelController.convertToVelocity(distance);
        
        // Shoot
        if (distance<96){
            shootOnce();
        }else{
            shootOneByOne(800, 200); //800 ms: the gate will be open; 200ms: intake will be stopped in between two shots 
        }        
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

        // Set fly wheel velocity.
        double distance = robotPose[2];
        flyWheelController.convertToVelocity(distance);
        
        // Wait before shoot
        //wait(25);
        
        // Shoot
        if (distance<96){
            shootOnce(doorOpenTime);
        }else{
            shootOneByOne(800, 200);
        }        
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

        // Set fly wheel velocity.
        double distance = robotPose[2];
        flyWheelController.convertToVelocity(distance);
        
        // Wait before shoot
        //wait(25);
        
        // Shoot
        if (distance<96){
            shootOnce(doorOpenTime);
        }else{
            shootOneByOne(800, 200);
        }        
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

        // Set fly wheel velocity.
        double distance = robotPose[2];
        flyWheelController.convertToVelocity(distance);
        
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

        // Set fly wheel velocity.
        double distance = robotPose[2];
        flyWheelController.convertToVelocity(distance);
        
        // Shoot
        shootOneByOne(intakeOnTime, intakeOffTime);
    }

    public void shootOnce(){
        flyWheelController.openDoor();
        wait(666);
        flyWheelController.convertToVelocity(0.95*robotPose[2]);
        wait(666);
        flyWheelController.convertToVelocity(0.9*robotPose[2]);
        wait(666);
    }
    
    public void shootOnce(int doorOpenTime){
        intakeController.setShootPower();
        flyWheelController.openDoor();
        
        wait(doorOpenTime/3);
        flyWheelController.convertToVelocity(0.9*robotPose[2]);
        wait(doorOpenTime/3);
        flyWheelController.convertToVelocity(0.8*robotPose[2]);
        wait(doorOpenTime/3);
        
        flyWheelController.closeDoor();
        intakeController.resetPower();
    }
    
    public void shootOneByOne(int intakeOnTime, int intakeOffTime){
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
            }
        }
        flyWheelController.closeDoor();
        intakeController.resetPower();// 
    }

    public void turnToBallAndCollect(int camWaitTime, double kLeft, double kRight, 
        double moveSpeed, double kDist){
        // Turn towards a ball.
        turnToBall(camWaitTime,kLeft,kRight);
        
        if(robotPose==null){
            //telemetry.speak("No");
            return;
        }
        
        // Collect ball.
        //kDistance = 0.9;
        //robotController.goForward(collectionMoveSpeed, kDistance*robotPose[2]);
        robotController.goToPosition(moveSpeed, kDist*robotPose[2], DistanceUnit.INCH, NavigationType.GOFORWARD, timeout);
    }

    
    public void turnToBallAndCollect(int camWaitTime, double kLeft, double kRight, 
        double moveSpeed, double kDist, int timeout){
        // Turn towards a ball.
        turnToBall(camWaitTime,kLeft,kRight);
        
        if(robotPose==null){
            //telemetry.speak("No");
            return;
        }
        
        // Collect ball.
        //kDistance = 0.9;
        //robotController.goForward(collectionMoveSpeed, kDistance*robotPose[2]);
        robotController.goToPosition(moveSpeed, kDist*robotPose[2], DistanceUnit.INCH, NavigationType.GOFORWARD, timeout);
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
        
        turnRobot(kLeft,kRight); // these two are modulating factors for left/right rotation
    }


    public void turnToBall(int camWaitTime){
        robotPose = limelight.getRobotPoseRelativeToBall(camWaitTime,false); // Use true to get average info
        if(robotPose==null){
            //telemetry.addLine("Ball out of vision!");
            return;
        }
        
        turnRobot(0.5,1.0); // these two are modulating factors for left/right rotation
    }

    private void turnRobot(){
        if(robotPose[0]<0){
            robotController.turnLeft(correctionTurnSpeed,Math.abs(kAngle*robotPose[0]));
        }else if(robotPose[0]>0){
            robotController.turnRight(correctionTurnSpeed,Math.abs(kAngle*robotPose[0]));
        }
    }

    private void turnRobot(double k){
        if(robotPose[0]<0){
            robotController.turnLeft(correctionTurnSpeed,Math.abs(k*robotPose[0]));
        }else if(robotPose[0]>0){
            robotController.turnRight(correctionTurnSpeed,Math.abs(k*robotPose[0]));
        }
    }

    private void turnRobot(double kLeft, double kRight){
        if(robotPose[0]<0){
            robotController.turnLeft(correctionTurnSpeed,Math.abs(kLeft*robotPose[0]));
        }else if(robotPose[0]>0){
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
