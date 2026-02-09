package org.firstinspires.ftc.teamcode.decode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.navigation.TeleMecanumRobotController;
import org.firstinspires.ftc.teamcode.decode.FlyWheelController;
import org.firstinspires.ftc.teamcode.decode.IntakeController;
import org.firstinspires.ftc.teamcode.vision.LimeLight3ACamera;

public abstract class TeleDecode extends OpMode {
    //variables
    private TeleMecanumRobotController teleRobotController;
    private FlyWheelController flyWheelController;
    private IntakeController intakeController;
    private ElapsedTime runTimer = new ElapsedTime();
    private LimeLight3ACamera limelight = null;
    private double[] robotPose;
    private boolean doorOpen = false;
    
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
        
        if (gamepad1.aWasPressed()) {
            //telemetry.addLine("A was pressed");
            if(doorOpen){
                //telemetry.addLine("Door was open");
                flyWheelController.closeDoor();
                doorOpen = false;
            }else{
                //telemetry.addLine("Door was close");
                turnToGoalAndShoot(goalID,100);
                doorOpen = true;
            }
            return;
        }else{
            if(doorOpen){
                if(keyPressed()){
                    flyWheelController.closeDoor();
                    doorOpen = false;
                }else{
                    return;
                }
            }
        }
        
        teleRobotController.run();
        flyWheelController.run();
        intakeController.run();
    }

    private void turnToGoalAndShoot(int gID, int camWaitTime){
        // Turn towards the goal.
        turnToGoal(gID,camWaitTime);
        
        if(robotPose==null){
            //telemetry.speak("No");
            //telemetry.update();
            return;
        }
        
        // Set fly wheel velocity.
        double x = robotPose[2];
        flyWheelController.convertToVelocity(x);
        //double velocity = 0.027392*x*x + 3.0918*x + 1386.40407;
        //velocity = Math.min(2150, velocity);
        //flyWheelController.setVelocity((int)velocity);
        if (x>96){
            return;
        }
        // Wait before shoot, to give the robot time to orient itself
        //wait(25);
        
        // Shoot
        openDoor();
    }

    private void turnToBallAndCollect(int camWaitTime){
        // Turn towards a ball.
        turnToBall(camWaitTime);
        
        if(robotPose==null){
            return;
        }
        
        // Collect ball.
        teleRobotController.goForward(collectionMoveSpeed, robotPose[2]);
    }
    
    private void turnToGoal(int gID, int camWaitTime){
        robotPose = limelight.getRobotPoseRelativeToGoal(gID,camWaitTime);
        if(robotPose==null){
            telemetry.addLine("Goal out of vision!");
            return;
        }
        turnRobot();
    }

    private void turnToBall(int camWaitTime){
        robotPose = limelight.getRobotPoseRelativeToBall(camWaitTime);
        if(robotPose==null){
            telemetry.addLine("Ball out of vision!");
            return;
        }
        turnRobot();
    }

    private void turnRobot(){
        if(robotPose[0]<0){
            teleRobotController.turnLeft(correctionTurnSpeed,Math.abs(kCorrection*robotPose[0]));
        }else if(robotPose[0]>0){
            teleRobotController.turnRight(correctionTurnSpeed,Math.abs(kCorrection*robotPose[0]));
        }
    }
    
    private void openDoor(){
        flyWheelController.openDoor();
        intakeController.restartIfStalled(true);
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
