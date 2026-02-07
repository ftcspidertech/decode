package org.firstinspires.ftc.teamcode.decode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.navigation.MecanumRobotController;
import org.firstinspires.ftc.teamcode.decode.FlyWheelController;
import org.firstinspires.ftc.teamcode.decode.IntakeController;
//import org.firstinspires.ftc.teamcode.vision.GoalTagProcessor;
import org.firstinspires.ftc.teamcode.vision.LimeLight3ACamera;


public abstract class AutoDecode extends OpMode{
    // Controller variables -------------------------------
    protected MecanumRobotController robotController = null;
    protected FlyWheelController flyWheelController = null;
    protected IntakeController intakeController = null;
    //protected GoalTagProcessor goalTagProcessor = null;
    protected LimeLight3ACamera limelight = null;
    private double kTurn = 0.9;
    private double[] robotPose;
    
    private int flyWheelVelocity = 1500;
    private ElapsedTime waitTimer = new ElapsedTime();
    
    @Override
    public void init() {
        // Initialize controllers
        initControllers();
    }

    private void initControllers(){
        // Initialize controller variables.
        double leftCalib = 1.0;
        double rightCalib = 1.0; // 0.87
        robotController = new  MecanumRobotController(hardwareMap,telemetry,leftCalib,rightCalib);
        //robotController.useSingleWheelRef = true;
        flyWheelController = new FlyWheelController(hardwareMap,gamepad1,telemetry);
        intakeController = new IntakeController(hardwareMap,gamepad1,telemetry);
        //goalTagProcessor = new GoalTagProcessor(hardwareMap,24); // ID 24 for Red Gate
        limelight = new LimeLight3ACamera(hardwareMap,telemetry);
        limelight.start();
        
        // Initially pause camera stream.
        //goalTagProcessor.stopStreaming();
        
        // Close exit door.
        //flyWheelController.closeDoor();
        
        // Starting flywheel early.
        //flyWheelController.setVelocity(flyWheelVelocity);
    
        // Start intake wheels
        //intakeController.start();
        
        // Display status
        telemetry.addData("Status", "Robot controllers initialized");                
    }
    
    @Override
    public void start() {
        // Close exit door.
        flyWheelController.closeDoor();
         // Starting flywheel early.
        flyWheelController.setVelocity(1350);
           // Start intake wheels
        intakeController.start();
    }

    @Override
    public void stop() {
        robotController.stop();
        flyWheelController.stop();
        intakeController.stop();
        //goalTagProcessor.close();
        limelight.stop();
    }
    
    protected void turnToGoalAndShoot(int gID, int camWaitTime, int doorOpenTime){
        // Turn towards the goal.
        turnToGoal(gID,camWaitTime);
        
        // Wait before shoot
        wait(25);
        
        // Shoot
        openDoor();
        wait(doorOpenTime);
        flyWheelController.closeDoor();
    }

    protected void turnToGoal(int gID, int camWaitTime){
        robotPose = limelight.getRobotPoseRelativeToGoal(gID,camWaitTime);
        if(robotPose==null){
            telemetry.addLine("Goal out of vision!");
            return;
        }
        // Turn
        if(robotPose[0]<0){
            robotController.turnLeft(0.1,Math.abs(kTurn*robotPose[0]));
        }else if(robotPose[0]>0){
            robotController.turnRight(0.1,Math.abs(kTurn*robotPose[0]));
        }
    }

    protected void turnToBall(int camWaitTime){
        robotPose = limelight.getRobotPoseRelativeToBall(camWaitTime);
        if(robotPose==null){
            telemetry.addLine("Ball out of vision!");
            return;
        }
        // Turn
        if(robotPose[0]<0){
            robotController.turnLeft(0.1,Math.abs(kTurn*robotPose[0]));
        }else if(robotPose[0]>0){
            robotController.turnRight(0.1,Math.abs(kTurn*robotPose[0]));
        }
    }

    protected void openDoor(){
        flyWheelController.openDoor();
        intakeController.restartIfStalled(true);
    }
    
    protected void wait(int ms){
        waitTimer.reset();
        while(waitTimer.milliseconds()<ms);
    }
    
    protected void setCalibrationFactors(double left, double right){
        robotController.setCalibrationFactors(left, right);
    }    
}
