package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import org.firstinspires.ftc.teamcode.navigation.RobotController;
import org.firstinspires.ftc.teamcode.decode.FlyWheelController;
import org.firstinspires.ftc.teamcode.decode.IntakeController;
import org.firstinspires.ftc.teamcode.vision.GoalTagProcessor;


@Autonomous()
public class BLUE_Auto_Position2  extends OpMode{
    // Controller variables -------------------------------
    private RobotController robotController = null;
    private FlyWheelController flyWheelController = null;
    private IntakeController intakeController = null;
    //private GoalTagProcessor goalTagProcessor = null;
    
    private int flyWheelVelocity = 1350; //stable set of parameters
    private ElapsedTime autoLauncherTimer = new ElapsedTime();
    private double movePower = 0.75;
    private double turnPower = 0.5;
    private boolean useSpeedCorrection = false;
    
    // State variables ------------------------------------
    // Task 1: Throw pre-loaded balls. 
    private boolean taskOneDone = false;
    // Task 2: Collect and throw another set of balls.
    private boolean taskTwoDone = false;
    
    
    
    @Override
    public void init() {
        // Initialize controllers
        initControllers();
    }

    private void initControllers(){
        // Initialize controller variables.
        double leftCalib = 1.0;
        double rightCalib = 0.87;
        robotController = new  RobotController(hardwareMap,telemetry,leftCalib,rightCalib);
        flyWheelController = new FlyWheelController(hardwareMap,gamepad1,telemetry);
        intakeController = new IntakeController(hardwareMap,gamepad1,telemetry);
        //goalTagProcessor = new GoalTagProcessor(hardwareMap,24); // ID 24 for Red Gate
        flyWheelController.setCurrentVelocity(flyWheelVelocity);
        
        // Initially pause camera stream.
        //goalTagProcessor.stopStreaming();
        
        // Close exit door.
        flyWheelController.closeDoor();
        
        // Starting flywheel early.
        flyWheelController.setVelocity(flyWheelVelocity);
        
        // Start intake wheels
        intakeController.start();
        
        // Display status
        telemetry.addData("Status", "Robot controllers initialized");                
    }

    @Override
    public void loop() {
        frontRedPosition2();
    }
    
    @Override
    public void stop() {
        robotController.stop();
        flyWheelController.stop();
        intakeController.stop();
        //goalTagProcessor.close();
    }
    
    private void wait(int ms){
        autoLauncherTimer.reset();
        while(autoLauncherTimer.milliseconds()<ms);
    }
    
    private void frontRedPosition2(){
        if(useSpeedCorrection){
            movePower = 0.8;
        }
        if(!taskOneDone) {
            // Go forward and turn to the goal.
            robotController.goForward(movePower,50,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            robotController.turnLeft(turnPower,165);
            wait(200);
            
            // Open the exit door. Keep it open for long enough to throw all the balls.
            flyWheelController.openDoor();
            wait(6000);
            flyWheelController.closeDoor();
             
            // Task1 done             
            taskOneDone = true;
        }else if(!taskTwoDone) {
            // Go back and turn to the 2nd set of balls.
            robotController.goBackward(movePower,8,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            robotController.turnLeft(turnPower,35);// turn to balls
            wait(200);
            
            // Go and collect the balls. Go slowly to grab the balls.
            robotController.goForward(movePower*0.8,37,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            
            // Go back and turn to the goal.
            robotController.goBackward(movePower*0.8,37,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            robotController.turnRight(turnPower,35);
            wait(200);
            
            // Open the exit door. Keep it open for long enough to throw all the balls.
            flyWheelController.openDoor();
            wait(10000);
            flyWheelController.closeDoor();
       
            // Task2 done.
            taskTwoDone = true;
        }else{
            stop();
        }
    }
}
