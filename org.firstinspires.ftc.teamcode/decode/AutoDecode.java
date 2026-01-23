package org.firstinspires.ftc.teamcode.decode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.navigation.MecanumRobotController;
import org.firstinspires.ftc.teamcode.decode.FlyWheelController;
import org.firstinspires.ftc.teamcode.decode.IntakeController;
import org.firstinspires.ftc.teamcode.vision.GoalTagProcessor;


public abstract class AutoDecode extends OpMode{
    // Controller variables -------------------------------
    protected MecanumRobotController robotController = null;
    protected FlyWheelController flyWheelController = null;
    protected IntakeController intakeController = null;
    //protected GoalTagProcessor goalTagProcessor = null;
    
    private int flyWheelVelocity = 1425;
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
