
package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.concurrent.TimeUnit;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.navigation.MecanumRobotController;
import org.firstinspires.ftc.teamcode.navigation.NavigationType;
import org.firstinspires.ftc.teamcode.navigation.TurnMethod;
import org.firstinspires.ftc.teamcode.decode.FlyWheelController;
import org.firstinspires.ftc.teamcode.decode.IntakeController;
import org.firstinspires.ftc.teamcode.vision.GoalTagProcessor;


//@Autonomous()
@TeleOp()    
public class TestDecode  extends OpMode{
    // Controller variables -------------------------------
    private MecanumRobotController robotController = null;
    private FlyWheelController flyWheelController = null;
    private IntakeController intakeController = null;
    //private GoalTagProcessor goalTagProcessor = null;
    
    private int flyWheelVelocity = 1500; //stable set of parameters
    private ElapsedTime autoLauncherTimer = new ElapsedTime();
    private double movePower = 1.0;
    private double turnPower = 0.5;
    
    // State variables ------------------------------------
    private boolean testComplete = false;
    
    
    
    @Override
    public void init() {
        // Initialize controllers
        initControllers();
    }

    private void initControllers(){
        // Initialize controller variables.
        robotController = new MecanumRobotController(hardwareMap,telemetry);
        flyWheelController = new FlyWheelController(hardwareMap,gamepad1,telemetry);
        intakeController = new IntakeController(hardwareMap,gamepad1,telemetry);
        //goalTagProcessor = new GoalTagProcessor(hardwareMap,24); // ID 24 for Red Gate
        
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
    public void loop() {
        telemetry.addData("Running ", "test loop");
        testLoop();
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
    
    private void testLoop(){
        telemetry.addLine("    Inside test loop:");
        telemetry.addData("        testComplete",testComplete);
        if(!testComplete) {
            //*
            boolean useSpeedCorrection = false;
            robotController.setCalibrationFactors(1.0,1.0);
            telemetry.addLine("        Go forward with speed correction");
            robotController.goForward(movePower,1,TimeUnit.SECONDS);
            //robotController.goForward(movePower,48,DistanceUnit.INCH,useSpeedCorrection);
            //robotController.goForward(movePower,45,useSpeedCorrection);
            //wait(200);
            telemetry.addLine("        Go backward with speed correction");
            robotController.goBackward(movePower,1,TimeUnit.SECONDS);
            //robotController.goBackward(movePower,45,useSpeedCorrection);
            //wait(200);
            telemetry.addLine("        Turn right with IMU (default)");            
            robotController.turnRight(turnPower,1,TimeUnit.SECONDS);
            //wait(200);
            telemetry.addLine("        Turn left with IMU (default)");            
            robotController.turnLeft(turnPower,1,TimeUnit.SECONDS);
            /*
            wait(200);
            telemetry.addLine("        Go forward without speed correction (default)");
            robotController.setCalibrationFactors(1.0,0.85);
            robotController.goForward(movePower,45,DistanceUnit.INCH);
            //robotController.goForward(movePower,45);
            wait(200);
            telemetry.addLine("        Go backward without speed correction (default)");
            robotController.goBackward(movePower,45,DistanceUnit.INCH);
            //robotController.goBackward(movePower,45);
            wait(200);
            telemetry.addLine("        Turn right with odometry");            
            robotController.turnRight(turnPower,180,TurnMethod.ODOMETRY);
            wait(200);
            telemetry.addLine("        Turn left with odometry");            
            robotController.turnLeft(turnPower,180,TurnMethod.ODOMETRY);
            */
            
            telemetry.addData("        Task ", "done");        
            testComplete = true;
            telemetry.addData("        testComplete",testComplete);
        }else{
            stop();
        }
       telemetry.update();
        wait(1000);
    }
}
