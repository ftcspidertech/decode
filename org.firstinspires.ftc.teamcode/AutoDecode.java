package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

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
    
    private int flyWheelVelocity = 1300;
    
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
            robotController.goForward(0.5,12,DistanceUnit.INCH);
            robotController.turnRight(0.5,45);
            
            flyWheelController.setVelocity(flyWheelVelocity);
            flyWheelController.closeDoor();
            
            preMoveCompleteToThrowPreloadedBalls = true;
        }else if(!preloadedBallsThrown) {
            if(!preloadedBall1Thrown) {
                
            }else if(!preloadedBall1Thrown) {
                
            }
        }else if(!preMoveCompleteToCollect1stSetOfBalls) {
            
        }else if(!collectionComplete1stSetOfBalls) {
            
        }else if(!goalTagFound) {
            
        }else if(!moveCompleteToThrow1stSetOfCollectedBalls) {
            
        }else if(!thrown1stSetOfCollectedBalls) {
            
        }
    }
    
    @Override
    public void stop() {
        robotController.stop();
        flyWheelController.stop();
        intakeController.stop();
        goalTagProcessor.close();
    }    
}
