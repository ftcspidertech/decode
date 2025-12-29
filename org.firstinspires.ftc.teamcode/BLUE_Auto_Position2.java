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

/*
 * Autonomous tasks:
 *   1. Step back x inches than throw the pre-loaded balls. The robot may need to move to a suitable location from its initial position for this task.
 *   2. Use odometry or camera to move to another location to collect more balls.
 *   3. Collect balls.
 *   4. Use camera to find the designated goal tag.
 *   5. Move closer to the gate.
 *   6. Throw the collected balls.
 *   7. If possible collect more balls. 
 *   
 */

@Autonomous()
public class RED_Auto_Position2  extends OpMode{
    // Controller variables -------------------------------
    private RobotController robotController = null;
    private FlyWheelController flyWheelController = null;
    private IntakeController intakeController = null;
    //private GoalTagProcessor goalTagProcessor = null;
    
    private int flyWheelVelocity = 1400; //stable set of parameters
    private ElapsedTime autoLauncherTimer = new ElapsedTime();
    
    // State variables ------------------------------------
    // Task 1: Throw pre-loaded balls. 
    private boolean preMoveCompleteToThrowPreloadedBalls = false;
    private boolean preloadedBallsThrown = false;
    private boolean preloadedBall1Thrown = false;
    private boolean preloadedBall2Thrown = false;
    // Task 2: Collect 1st set of collected balls
    private boolean preMoveCompleteToCollect1stSetOfBalls = false;
    private boolean collectionComplete1stSetOfBalls = false;
    // Task 3: Throw 1st set of collected balls
    private boolean goalTagFound = false;
    private boolean moveCompleteToThrow1stSetOfCollectedBalls = false;
    private boolean thrown1stSetOfCollectedBalls = false;
    // Task 4: Collect 2nd set of balls
    
    
    // Test task
    private boolean testComplete = false;
    private double moveSpeed = 0.75;
    private double turnSpeed = 0.5;
    
    
    
    @Override
    public void init() {
        // Initialize controllers
        initControllers();
    }

    private void initControllers(){
        // Initialize controller variables.
        double leftCalib = 1.0;
        double rightCalib = 1.0;
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
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
        stableLoopREDPosition2();
        
        //telemetry.addData("Running ", "test loop");
        //testLoop();
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
    
    private void sleep(int ms){
        try{
            Thread.sleep(ms);
        }catch(Exception e){
            telemetry.addData("Error: ",e.getMessage());
        }
    }

    private void stableLoopREDPosition2(){
        if(!preMoveCompleteToThrowPreloadedBalls) {
            robotController.goBackward(moveSpeed,45,DistanceUnit.INCH); // 39.5 //stable set of parameters
            wait(200);
            robotController.turnLeft(turnSpeed,3);
            wait(200);
            flyWheelController.openDoor();
             wait(5000); //stable set of parameters
             flyWheelController.closeDoor();
             robotController.turnLeft(turnSpeed,30);
             wait(200);
             robotController.goForward(moveSpeed,32,DistanceUnit.INCH);
             wait(200);
             robotController.goBackward(moveSpeed,28,DistanceUnit.INCH);
             wait(200);
             robotController.turnRight(turnSpeed,40);
             wait(200);
             flyWheelController.openDoor();
             wait(5000);
             flyWheelController.closeDoor();
                         
            preMoveCompleteToThrowPreloadedBalls = true;
        }else if(!preloadedBallsThrown) {
       
            preloadedBallsThrown = true;
        }else if(!preMoveCompleteToCollect1stSetOfBalls) {
            
             
            preMoveCompleteToCollect1stSetOfBalls = true;
        }else if(!collectionComplete1stSetOfBalls) {
            
            collectionComplete1stSetOfBalls = true;
        }else if(!goalTagFound) {
            
            goalTagFound = true;
        }else if(!moveCompleteToThrow1stSetOfCollectedBalls) {
            
            moveCompleteToThrow1stSetOfCollectedBalls = true;
        }else if(!thrown1stSetOfCollectedBalls) {
            
            thrown1stSetOfCollectedBalls = true;
        }else{
            stop();
        }
    }

    
    private void stableLoopREDPosition1(){
        if(!preMoveCompleteToThrowPreloadedBalls) {
            robotController.goForward(0.75,45,DistanceUnit.INCH); // 45 //stable set of parameters
            wait(200);
            robotController.turnRight(0.5,40);
            wait(200);
            robotController.goForward(0.75,6,DistanceUnit.INCH); //6

            preMoveCompleteToThrowPreloadedBalls = true;
        }else if(!preloadedBallsThrown) {
            //*
            flyWheelController.openDoor();
            wait(5000); //stable set of parameters

            //intakeController.stop();
            flyWheelController.closeDoor();
            //*/
            preloadedBallsThrown = true;
        }else if(!preMoveCompleteToCollect1stSetOfBalls) {
            //*
             robotController.turnLeft(0.5,130);
             wait(200);
             robotController.goForward(0.5,33,DistanceUnit.INCH);
             wait(200);
             robotController.goBackward(0.5,33,DistanceUnit.INCH);
             wait(200);
             robotController.turnRight(0.5,130);
             flyWheelController.openDoor();
            telemetry.addData("Door open","");
            wait(2500);
            //*/
            preMoveCompleteToCollect1stSetOfBalls = true;
        }else if(!collectionComplete1stSetOfBalls) {
            
            collectionComplete1stSetOfBalls = true;
        }else if(!goalTagFound) {
            
            goalTagFound = true;
        }else if(!moveCompleteToThrow1stSetOfCollectedBalls) {
            
            moveCompleteToThrow1stSetOfCollectedBalls = true;
        }else if(!thrown1stSetOfCollectedBalls) {
            
            thrown1stSetOfCollectedBalls = true;
        }else{
            stop();
        }
    }
    
    private void testLoop(){
        telemetry.addData("    Inside ", "test loop");
            telemetry.addData("        testComplete",testComplete);
        if(!testComplete) {
            telemetry.addData("        Go ", "forward");
            robotController.goForward(0.5,6,DistanceUnit.INCH);
            //try{
            //    Thread.sleep(5000);
            //}catch(Exception e){
            //}
            //wait(200);
            telemetry.addData("        Turn ", "right");
            
            robotController.turnRight(0.5,45);
            telemetry.addData("        Task ", "done");
        
            testComplete = true;
            telemetry.addData("        testComplete",testComplete);
        }else{
            stop();
        }
    }
}
