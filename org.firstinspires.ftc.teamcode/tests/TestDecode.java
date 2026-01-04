package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.navigation.RobotController;
import org.firstinspires.ftc.teamcode.navigation.NavigationType;
import org.firstinspires.ftc.teamcode.decode.FlyWheelController;
import org.firstinspires.ftc.teamcode.decode.IntakeController;
import org.firstinspires.ftc.teamcode.vision.GoalTagProcessor;

/*
 * Autonomous tasks:
 *   1. Throw the pre-loaded balls. The robot may need to move to a suitable location from its initial position for this task.
 *   2. Use odometry or camera to move to another location to collect more balls.
 *   3. Collect balls.
 *   4. Use camera to find the designated goal tag.
 *   5. Move closer to the gate.
 *   6. Throw the collected balls.
 *   7. If possible collect more balls. 
 *   
 */

//@Autonomous()
@TeleOp()    
public class TestDecode  extends OpMode{
    // Controller variables -------------------------------
    private RobotController robotController = null;
    private FlyWheelController flyWheelController = null;
    private IntakeController intakeController = null;
    private GoalTagProcessor goalTagProcessor = null;
    
    private int flyWheelVelocity = 1500; //stable set of parameters
    private ElapsedTime autoLauncherTimer = new ElapsedTime();
    private double movePower = 0.8;
    private double turnPower = 0.5;
    
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
    
    
    
    @Override
    public void init() {
        // Initialize controllers
        initControllers();
    }

    private void initControllers(){
        // Initialize controller variables.
        robotController = new  RobotController(hardwareMap,telemetry);
        flyWheelController = new FlyWheelController(hardwareMap,gamepad1,telemetry);
        intakeController = new IntakeController(hardwareMap,gamepad1,telemetry);
        goalTagProcessor = new GoalTagProcessor(hardwareMap,24); // ID 24 for Red Gate
        
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
        //stableLoopDec14();
        
        telemetry.addData("Running ", "test loop");
        testLoop();
    }
    
    @Override
    public void stop() {
        robotController.stop();
        flyWheelController.stop();
        intakeController.stop();
        goalTagProcessor.close();
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
    
    private void stableLoopDec14(){
        if(!preMoveCompleteToThrowPreloadedBalls) {
            robotController.goForward(movePower,45,DistanceUnit.INCH); // 45 //stable set of parameters
            wait(200);
            robotController.turnRight(turnPower,45);
            wait(200);
            robotController.goForward(movePower,6,DistanceUnit.INCH); //6

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
             robotController.turnLeft(turnPower,135);// turn left to the balls
             wait(200);
             robotController.goForward(movePower,39,DistanceUnit.INCH); // move forward to get balls
             wait(200);
             robotController.goBackward(movePower,39,DistanceUnit.INCH);// go back to the throwing position
             wait(200);
             robotController.turnRight(turnPower,139); // turn right to gate to shoot
             flyWheelController.openDoor();
             wait(4500); // wait for balls to shoot
             flyWheelController.closeDoor(); // close door after done 
             robotController.turnLeft(turnPower,175);// turn to retrieve balls from other side 
             wait(200);
             robotController.goForward(movePower,45,DistanceUnit.INCH);// aqquire the balls 
             //robotController.driveMotorCurveLeft(movePower,0.5);
             wait(200);
             robotController.turnLeft(turnPower,90); 
             wait(200);
             robotController.goForward(movePower,12,DistanceUnit.INCH); 
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
        telemetry.addLine("    Inside test loop:");
        telemetry.addData("        testComplete",testComplete);
        if(!testComplete) {
            boolean useSpeedCorrection = true;
            telemetry.addLine("        Go forward with speed correction ...");
            robotController.goForward(movePower,60,DistanceUnit.INCH,useSpeedCorrection);

            telemetry.addLine("        Go backward with speed correction ...");
            robotController.goBackward(movePower,60,DistanceUnit.INCH,useSpeedCorrection);
            
            telemetry.addLine("        Go forward without speed correction ...");
            robotController.goForward(movePower,60,DistanceUnit.INCH);
            
            telemetry.addLine("        Go backward without speed correction ...");
            robotController.goBackward(movePower,60,DistanceUnit.INCH);
            
            telemetry.addLine("        Turn right with IMU");            
            robotController.turnRight(turnPower,45);

            telemetry.addLine("        Turn left with IMU");            
            robotController.turnLeft(turnPower,45);

            telemetry.addLine("        Turn right with odometry");            
            robotController.turnRight(turnPower,45,false);

            telemetry.addLine("        Turn left with odometry");            
            robotController.turnLeft(turnPower,45,false);
            
            telemetry.addData("        Task ", "done");        
            testComplete = true;
            telemetry.addData("        testComplete",testComplete);
        }else{
            stop();
        }
    }
}
