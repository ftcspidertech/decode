package org.firstinspires.ftc.teamcode.decode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.navigation.MecanumRobotController;
import org.firstinspires.ftc.teamcode.decode.FlyWheelController;
import org.firstinspires.ftc.teamcode.decode.IntakeController;
//import org.firstinspires.ftc.teamcode.vision.GoalTagProcessor;
import org.firstinspires.ftc.teamcode.vision.LimeLight3ACamera;
import org.firstinspires.ftc.teamcode.decode.Utilities;


public abstract class AutoDecode extends OpMode{
    // Controller variables -------------------------------
    protected MecanumRobotController robotController = null;
    protected FlyWheelController flyWheelController = null;
    protected IntakeController intakeController = null;
    //protected GoalTagProcessor goalTagProcessor = null;
    protected LimeLight3ACamera limelight = null;
    protected double[] robotPose;
    protected double kCorrection = 0.9;
    protected double correctionTurnSpeed = 0.1;
    protected double collectionMoveSpeed = 0.125;
    protected int goalID = 24; // Default is Red
    protected int goalPipelineID = 2; // For Red
    protected int ballPipelineID = 1; // For purple ball

    private int flyWheelVelocity = 1500;
    //private ElapsedTime waitTimer = new ElapsedTime();
    
    protected Utilities utilities;
    
    protected abstract void initAutoDecode();
    
    @Override
    public void init() {
        // Initialize controllers
        initControllers();
        // Goal specific initialization.
        initAutoDecode();
        utilities.correctionTurnSpeed = correctionTurnSpeed;
        utilities.collectionMoveSpeed = collectionMoveSpeed;
        // Display status
        telemetry.addData("Status", "Alhamdulillah, Robot controllers initialized");
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
        
        utilities = new Utilities(robotController,flyWheelController,intakeController,limelight,telemetry);
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
    
    protected void turnToGoalAndShoot(int camWaitTime, int doorOpenTime){
        utilities.turnToGoalAndShoot(goalID,camWaitTime,doorOpenTime);
    }

    protected void turnToGoalAndShootOneByOne(int camWaitTime,int intakeOnTime, int intakeOffTime){
        utilities.turnToGoalAndShootOneByOne(goalID, camWaitTime, intakeOnTime, intakeOffTime);    
    }

    protected void turnToBallAndCollect(int camWaitTime){
        utilities.turnToBallAndCollect(camWaitTime);
    }
    
    protected void openDoor(){
        utilities.openDoor();
    }
    
    protected void wait(int ms){
        utilities.wait(ms);
    }
    
    protected void setCalibrationFactors(double left, double right){
        robotController.setCalibrationFactors(left, right);
    }

    protected void setGoalPipelineID(int waitTime){
        limelight.setPipelineID(goalPipelineID,waitTime);
    }

    protected void setBallPipelineID(int waitTime){
        limelight.setPipelineID(ballPipelineID,waitTime);
    }    
}
