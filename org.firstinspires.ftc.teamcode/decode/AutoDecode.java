package org.firstinspires.ftc.teamcode.decode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.navigation.NavigationType;
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
    protected int shootTime = 2000;

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
        intakeController.MIN_VELOCITY = 250;
        intakeController.regularPower = 1.0;
        
        //goalTagProcessor = new GoalTagProcessor(hardwareMap,24); // ID 24 for Red Gate
        limelight = new LimeLight3ACamera(hardwareMap,telemetry);
        limelight.start();
        
        utilities = new Utilities(robotController,flyWheelController,intakeController,limelight,telemetry);
        utilities.closeDoorAfterShooting = false;
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
    
    protected void resetRobotIfDoorNotClosedAfterShooting(){
        if(!utilities.closeDoorAfterShooting){
            utilities.resetRobot();
        }
    }
    
    protected void goForward(double speed, double runTime, TimeUnit unit){
        resetRobotIfDoorNotClosedAfterShooting();
        robotController.goForward(speed,runTime,unit);
    }
    
    protected void goForward(double moveSpeed, double distance, int timeout){
        //robotController.goToPosition(moveSpeed, distance, DistanceUnit.INCH, NavigationType.GOFORWARD);
        int preTimeout = robotController.timeout;
        
        resetRobotIfDoorNotClosedAfterShooting();
        
        robotController.timeout = timeout;
        robotController.goToPositionWithSpeedModulation(moveSpeed,Math.abs(distance),0.5,3,DistanceUnit.INCH);
        robotController.timeout = preTimeout;
    }

    protected void goForward(double moveSpeed, double distance, int timeout, boolean slowDownBeforeStop){
        int preTimeout = robotController.timeout;
        boolean preSlowDownBeforeStop = robotController.slowDownBeforeStop;
        
        resetRobotIfDoorNotClosedAfterShooting();
        
        robotController.timeout = timeout;
        robotController.slowDownBeforeStop = slowDownBeforeStop;
        
        robotController.goToPositionWithSpeedModulation(moveSpeed,Math.abs(distance),0.5,3,DistanceUnit.INCH);
        
        robotController.slowDownBeforeStop = preSlowDownBeforeStop;
        robotController.timeout = preTimeout;
    }

    protected void goBackward(double speed, double runTime, TimeUnit unit){
        robotController.goBackward(speed,runTime,unit);
    }
    
    protected void goBackward(double moveSpeed, double distance, int timeout){
        //robotController.goToPosition(moveSpeed, distance, DistanceUnit.INCH, NavigationType.GOBACKWARD);
        int preTimeout = robotController.timeout;
        robotController.timeout = timeout;
        robotController.goToPositionWithSpeedModulation(moveSpeed,-Math.abs(distance),1.25,3,DistanceUnit.INCH);
        robotController.timeout = preTimeout;
    }

    protected void goBackward(double moveSpeed, double distance, int timeout, boolean slowDownBeforeStop){
        int preTimeout = robotController.timeout;
        boolean preSlowDownBeforeStop = robotController.slowDownBeforeStop;
        
        robotController.timeout = timeout;
        robotController.slowDownBeforeStop = slowDownBeforeStop;
        
        robotController.goToPositionWithSpeedModulation(moveSpeed,-Math.abs(distance),1.25,3,DistanceUnit.INCH);
        
        robotController.slowDownBeforeStop = preSlowDownBeforeStop;
        robotController.timeout = preTimeout;
    }

    protected void slideLeft(double speed, double runTime, TimeUnit unit){
        robotController.slideLeft(speed, runTime, unit);
    }

    protected void slideRight(double speed, double runTime, TimeUnit unit){
        robotController.slideRight(speed, runTime, unit);
    }

    protected void turnLeft(double speed, double runTime, TimeUnit unit){
        robotController.turnLeft(speed, runTime, unit);
    }

    protected void turnLeft(double speed, double angle){
        robotController.turnLeft(speed, angle);
    }
    
    protected void turnRight(double speed, double runTime, TimeUnit unit){
        robotController.turnRight(speed, runTime, unit);
    }

    protected void turnRight(double speed, double angle){
        robotController.turnRight(speed, angle);
    }
    
    protected void shootWithoutCamera(int doorOpenTime){
        intakeController.setShootPower();
        flyWheelController.openDoor();
        
        wait(doorOpenTime);

        if(utilities.closeDoorAfterShooting){
            flyWheelController.closeDoor();
            intakeController.resetPower();
        }
    }
}
