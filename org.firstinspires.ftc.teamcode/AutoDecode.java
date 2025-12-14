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
 *   1. Throw the pre-loaded balls. The robot may need to move to a suitable location from its initial position for this task.
 *   2. Use odometry or camera to move to another location to collect more balls.
 *   3. Collect balls.
 *   4. Use camera to find the designated goal tag.
 *   5. Move closer to the gate.
 *   6. Throw the collected balls.
 *   7. If possible collect more balls. 
 *   
 */

@Autonomous()
public class AutoDecode  extends OpMode{
    // Controller variables -------------------------------
    private RobotController robotController = null;
    private FlyWheelController flyWheelController = null;
    private IntakeController intakeController = null;
    private GoalTagProcessor goalTagProcessor = null;
    
    private int flyWheelVelocity = 1750;
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
    
    
    
    
    @Override
    public void init() {
                
        // Initialize controllers.
        robotController = new  RobotController(hardwareMap,telemetry);
        flyWheelController = new FlyWheelController(hardwareMap,gamepad1,telemetry);
        intakeController = new IntakeController(hardwareMap,gamepad1,telemetry);
        goalTagProcessor = new GoalTagProcessor(hardwareMap,24); // ID 24 for Red Gate
        
        
        // Initially pause camera stream.
        goalTagProcessor.stopStreaming();
        
        // Starting flywheel early.
        flyWheelController.setVelocity(flyWheelVelocity);
        
        // Close exit door.
        flyWheelController.closeDoor();    
        
        // Display status
        telemetry.addData("Status", "Initialized");                
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
        if(!preMoveCompleteToThrowPreloadedBalls) {
            robotController.goForward(0.75,45,DistanceUnit.INCH); // 45
            wait(0);
            robotController.turnRight(0.5,40);
            robotController.goForward(0.75,6,DistanceUnit.INCH); //6

            preMoveCompleteToThrowPreloadedBalls = true;
            
            intakeController.start();
            
            //wait(1000);
        }else if(!preloadedBallsThrown) {
            //*
            flyWheelController.openDoor();
            wait(3000);
            //flyWheelController.closeDoor();
            //wait(1000);
            //flyWheelController.openDoor();
            //wait(1000);
            
            flyWheelController.stopFlyWheel();
            
            intakeController.stop();
            flyWheelController.closeDoor();
            //*/
            
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
}
