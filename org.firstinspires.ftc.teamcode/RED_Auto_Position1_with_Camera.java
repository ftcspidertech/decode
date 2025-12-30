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
 *   8. Throw the collected balls.
 */

@Autonomous()
public class RED_Auto_Position1_with_Camera extends OpMode{
	// Controller variables -------------------------------
	private RobotController robotController = null;
	private FlyWheelController flyWheelController = null;
	private IntakeController intakeController = null;
	//private GoalTagProcessor goalTagProcessor = null;

	private int flyWheelVelocity = 1425; //stable set of parameters
	private ElapsedTime autoLauncherTimer = new ElapsedTime();
	private double movePower = 0.75;
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
		double leftCalib = 1.0;
		double rightCalib = 1.0;
		robotController = new  RobotController(hardwareMap,telemetry,leftCalib,rightCalib);
		flyWheelController = new FlyWheelController(hardwareMap,gamepad1,telemetry);
		intakeController = new IntakeController(hardwareMap,gamepad1,telemetry);
		goalTagProcessor = new GoalTagProcessor(hardwareMap,24); // ID 24 for Red Gate

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
		runAutoTasks();

		//telemetry.addData("Running ", "test loop");
		//testLoop();
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

	private void runAutoTasks(){
		private double[] rangeBearing;

		if(!preMoveCompleteToThrowPreloadedBalls) {
			robotController.goForward(movePower,48,DistanceUnit.INCH); // 70
			wait(200);
			robotController.turnRight(turnPower,30); // 35
			wait(200);
			//robotController.goForward(movePower,6,DistanceUnit.INCH); //6

			preMoveCompleteToThrowPreloadedBalls = true;
		}else if(!preloadedBallsThrown) {
			//*
			flyWheelController.openDoor();
			wait(6000); //stable set of parameters

			//intakeController.stop();
			flyWheelController.closeDoor();
			//*/
			preloadedBallsThrown = true;
		}else if(!preMoveCompleteToCollect1stSetOfBalls) {
			robotController.turnRight(turnPower,40);//turn to get second set of balls
			robotController.goForward(movePower,30,DistanceUnit.INCH);//move forward to aqquire balls
			wait(200);
			robotController.goBackward(movePower,30,DistanceUnit.INCH);
			wait(200);
			// Resume camera streaming
			//goalTagProcessor.resumeStreaming();
			robotController.turnLeft(turnPower,40);
			// Get goal tag location
			rangeBearing = goalTagProcessor.getRangeBearing();
			//goalTagProcessor.stopStreaming();
			if(rangeBearing!=null){
				telemetry.addData("range = ",rangeBearing[0]);
				telemetry.addData("bearing = ",rangeBearing[1]);

				if(rangeBearing[1]>1){
					robotController.turnLeft(turnPower,rangeBearing[1]/1.0);
				}else if(rangeBearing[1]<-1){
					robot.turnRight(turnPower,rangeBearing[1]/1.0);
				}                
			}            
			flyWheelController.openDoor();
			wait(6000);
			flyWheelController.closeDoor();

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
			robotController.goForward(movePower,45,DistanceUnit.INCH);
			//try{
			//    Thread.sleep(5000);
			//}catch(Exception e){
			//}
			//wait(200);
			telemetry.addData("        Turn ", "right");

			robotController.turnRight(turnPower,45);
			telemetry.addData("        Task ", "done");

			testComplete = true;
			telemetry.addData("        testComplete",testComplete);
		}else{
			stop();
		}
	}
}
