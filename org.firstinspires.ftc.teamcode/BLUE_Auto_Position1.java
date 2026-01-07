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
public class BLUE_Auto_Position1 extends OpMode{
    // Controller variables -------------------------------
    private RobotController robotController = null;
    private FlyWheelController flyWheelController = null;
    private IntakeController intakeController = null;
    //private GoalTagProcessor goalTagProcessor = null;
    
    private int flyWheelVelocity = 1425; //stable set of parameters
    private ElapsedTime autoLauncherTimer = new ElapsedTime();
    private double movePower = 0.65;
    private double turnPower = 0.5;
    private boolean useSpeedCorrection = true;
    
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
        stableLoop();
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
    
    private void stableLoop(){
        if(!taskOneDone) {
            // Go forward and turn towards the goal.
            robotController.goForward(movePower,49,DistanceUnit.INCH,useSpeedCorrection);
            wait(300);
            robotController.turnLeft(turnPower,19);
            wait(200);

            // Open the exit door. Keep it open for long enough to throw all the balls.
            flyWheelController.openDoor();
            wait(6500);
            flyWheelController.closeDoor();
            
            // Task1 is done.
            taskOneDone = true;
        }else if(!taskTwoDone) {
            // Turn to get second set of balls.
            robotController.turnLeft(turnPower,45);
            
            //Move forward to aqquire balls
            robotController.goForward(movePower*0.7,37,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            
            // Go back and turn to the scoring location.
            robotController.goBackward(movePower*0.7,37,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            robotController.turnRight(turnPower,45);
            
            // Open the exit door. Keep it open for long enough to throw all the balls.
            flyWheelController.openDoor();
            wait(6000);
            flyWheelController.closeDoor();
            
            // Task2 is done.
            taskTwoDone = true;
       }else{
            stop();
        }
    }
}
