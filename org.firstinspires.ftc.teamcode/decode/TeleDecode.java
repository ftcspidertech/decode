package org.firstinspires.ftc.teamcode.decode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.navigation.TeleMecanumRobotController;
import org.firstinspires.ftc.teamcode.decode.FlyWheelController;
import org.firstinspires.ftc.teamcode.decode.IntakeController;
import org.firstinspires.ftc.teamcode.vision.LimeLight3ACamera;
import org.firstinspires.ftc.teamcode.decode.Utilities;

public abstract class TeleDecode extends OpMode {
    //variables
    private TeleMecanumRobotController teleRobotController;
    private FlyWheelController flyWheelController;
    private IntakeController intakeController;
    private ElapsedTime runTimer = new ElapsedTime();
    private LimeLight3ACamera limelight = null;
    protected Utilities utilities;
    private double[] robotPose;
    private boolean doorOpen = false;
    private boolean collectionStarted = false;
    private boolean oneByOneShootStarted = false;
    private boolean triggerPressed = false;
    
    protected int goalID = 24;
    protected double kMove = 0.9;
    protected double kTurn = 0.9;
    protected double kCorrection = 0.9;
    protected double correctionTurnSpeed = 0.1;
    protected double collectionMoveSpeed = 0.25;

    protected abstract void initTeleDecode();

    @Override
    public void init() {
        // Controllers
        teleRobotController = new TeleMecanumRobotController(hardwareMap,gamepad1,telemetry);
        flyWheelController = new FlyWheelController(hardwareMap,gamepad1,telemetry);
        intakeController = new IntakeController(hardwareMap,gamepad1,telemetry);
        limelight = new LimeLight3ACamera(hardwareMap,telemetry);
        limelight.start();
        
        initTeleDecode();
        teleRobotController.kMove = kMove;
        teleRobotController.kTurn = kTurn;

        utilities = new Utilities(teleRobotController.getRobotController(),
            flyWheelController,intakeController,limelight,telemetry);

        // Display status
        telemetry.addData("Status", "Alhamdulillah, Robot controllers initialized");
    }

    @Override
    public void start(){
        telemetry.addData("Status","Bismillah, starting now.");
        
        // Start the flywheel at the beginning. The operator can only change its speed. 
        // NOTE: The operator can't stop the flywheel anymore.
        flyWheelController.startWithDefaultVelocity();

        // Start the intake controller at the beginning. The operator, however, can start/stop and
        // change its direction.
        intakeController.start();
        
        runTimer.reset();
    }
    
    @Override
    public void loop() {
        double runtime = runTimer.seconds();//getRuntime();
        telemetry.addData("Remaining time",(120.0-runtime));            
        //*
        if(runtime>=120.0){
            telemetry.addData("Status","Alhamdulillah, done!");            
            requestOpModeStop();
        }//*/
        
        // Turn towards a goal and shoot the balls at once.
        if (gamepad1.aWasPressed()) {
            //telemetry.addLine("A was pressed");
            if(doorOpen){
                // Toggle action
                //utilities.resetRobot();
                //doorOpen = false;
            }else{
                doorOpen = true;
                //turnToGoalAndShoot(100);
                turnToGoalAndShoot(100,2250);
                doorOpen = false;
            }
            return;
        }else{
            if(doorOpen){
                if(keyPressed()){
                    utilities.resetRobot();
                    //flyWheelController.closeDoor();
                    //intakeController.resetPower();
                    doorOpen = false;
                }else{
                    return;
                }
            }
        }
        
        // Turn towards a goal and shoot the balls one by one.
        if (gamepad1.bWasPressed()) {    
            if(oneByOneShootStarted){
                // Toggle action
                //utilities.resetRobot();
                //oneByOneShootStarted = false;
            }else{
                oneByOneShootStarted = true;
                turnToGoalAndShootOneByOne(100);
                oneByOneShootStarted = false;
            }
            return;
        }else{
            if(oneByOneShootStarted){
                if(keyPressed()){
                    utilities.resetRobot();
                    //flyWheelController.closeDoor();
                    //intakeController.start();
                    oneByOneShootStarted = false;
                }else{
                    return;
                }
            }
        }
        
        
        // Turn towards a ball and collect it.
        //*
        if(gamepad1.right_trigger==1.0 && !triggerPressed){
            triggerPressed = true;
            if(collectionStarted){
                // Toggle action
                //utilities.resetRobot();
                //collectionStarted = false;
            }else{
                collectionStarted = true;
                turnToBallAndCollect(100);
                //turnToBall(100);
                collectionStarted = false;
            }
            return;
        }else{
            triggerPressed = false;
            if(collectionStarted){
                if(keyPressed()){
                    utilities.resetRobot();
                    //teleRobotController.stop();
                    collectionStarted = false;
                }else{
                    return;
                }
            }
        }//*/
        
        // General controls.
        teleRobotController.run();
        flyWheelController.run();
        intakeController.run();
        
        doorOpen = false;
        oneByOneShootStarted = false;
        collectionStarted = false;
        triggerPressed = false;
    }

    private void turnToGoalAndShoot(int camWaitTime){
        utilities.turnToGoalAndShoot(goalID,camWaitTime);
    }

    private void turnToGoalAndShoot(int camWaitTime, int doorOpenTime){
        utilities.turnToGoalAndShoot(goalID,camWaitTime,doorOpenTime);
    }

    private void turnToGoalAndShootOneByOne(int camWaitTime){
        utilities.turnToGoalAndShootOneByOne(goalID,camWaitTime,800,200);
    }

    private void turnToBallAndCollect(int camWaitTime){
        utilities.turnToBallAndCollect(camWaitTime);
    }
    
    private void turnToBall(int camWaitTime){
        utilities.turnToBall(camWaitTime);
    }
    
    private boolean keyPressed(){
        boolean yes = false;
        // 
        if (gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y ||
            gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_right ||
            gamepad1.left_bumper || gamepad1.right_bumper ||
            gamepad1.left_stick_button || gamepad1.right_stick_button ||
            gamepad1.start || gamepad1.back ||
            gamepad1.left_trigger==1.0 || gamepad1.right_trigger==1.0 ||
            Math.abs(gamepad1.left_stick_x)>0 || Math.abs(gamepad1.left_stick_y)>0 ||
            Math.abs(gamepad1.right_stick_x)>0 || Math.abs(gamepad1.right_stick_y)>0) {
                
                yes = true;

        }

        return yes;        
    }

    private void wait(int waitTime){
        try{
            Thread.sleep(waitTime);
        } catch (Exception e) {
            telemetry.addData("Error: ", e.getMessage());
        }
    }

    @Override
    public void stop(){
        teleRobotController.stop();
        flyWheelController.stop();
        intakeController.stop();
        limelight.stop();
        telemetry.addData("Status ","Robot controllers stopped."); 
    }
    
}
