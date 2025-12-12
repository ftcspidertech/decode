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
	
	// State variables ------------------------------------
	// Task 1: Throw pre-loaded balls. 
	private boolean preloadedBallsThrown = false;
	private boolean preloadedBall1Thrown = false;
	private boolean preloadedBall2Thrown = false;
	private boolean preMoveCompleteToThrowPreloadedBalls = false;
	// Task 2: Collect 1st set of collected balls
	private private boolean preMoveCompleteToCollect1stSetOfBalls = false;
	private boolean collectionComplete1stSetOfBalls = false;
	// Task 3: Throw 1st set of collected balls
	private boolean goalTagFound = false;
	private boolean moveCompleteToThrow1stSetOfCollectedBalls = false;
	private boolean thrown1stSetOfCollectedBalls = false;
	// Task 4: Collect 2nd set of balls
	
	
	
	
    @Override
    public void init() {
    	    	
    	// Initialize controllers.
    	
    	    	
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
    }
}
