package org.firstinspires.ftc.teamcode.biobuzz.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.biobuzz.navigation.MecanumRobotController;
import org.firstinspires.ftc.teamcode.biobuzz.navigation.TeleMecanumRobotController;

//@Autonomous()
@TeleOp(name = "BioBuzz Tele Robot")
public class TestBioBuzz  extends OpMode{
     private MecanumRobotController robotController = null;
     private ElapsedTime autoLauncherTimer = new ElapsedTime();
     private double movePower = 0.075;
     private boolean testComplete = false;

     private TeleMecanumRobotController teleRobot = null;

    @Override
    public void init() {
         initControllers();
     }
 
     private void initControllers(){
         robotController = new MecanumRobotController(hardwareMap,telemetry);
         robotController.tickTolerance = 0;
         teleRobot = new TeleMecanumRobotController(hardwareMap,gamepad1,telemetry);
     }
     @Override
     public void loop() {
         telemetry.addData("Running ", "test loop");
//         testLoop();
         teleRobot.run();
     }
     
     @Override
     public void stop() {
//         robotController.stop();
         teleRobot.stop();
     }
     
     private void wait(int ms){
         autoLauncherTimer.reset();
         while(autoLauncherTimer.milliseconds()<ms);
     }
    
     private void testLoop(){
         telemetry.addLine("Inside test loop:");
         telemetry.addData("testComplete",testComplete);
         if(!testComplete) {

/*
             robotController.goForward(movePower,1250,TimeUnit.MILLISECONDS);
             wait(1000);
             robotController.goBackward(movePower,1250,TimeUnit.MILLISECONDS);
             wait(1000);
*/

             robotController.goForward(movePower,12,DistanceUnit.INCH);
             wait(1000);
             robotController.goBackward(movePower,12,DistanceUnit.INCH);
             wait(1000);

/*
             robotController.slideTopRight(movePower,1250,TimeUnit.MILLISECONDS);
             wait(1000);
             robotController.slideBottomLeft(movePower,1250,TimeUnit.MILLISECONDS);
             wait(1000);

             robotController.slideTopLeft(movePower,1250,TimeUnit.MILLISECONDS);
             wait(1000);
             robotController.slideBottomRight(movePower,1250,TimeUnit.MILLISECONDS);
             wait(1000);
*/

/*
             robotController.slideLeft(movePower,1500,TimeUnit.MILLISECONDS);
             wait(1000);
             robotController.slideRight(movePower,1500,TimeUnit.MILLISECONDS);
             wait(1000);
*/

             robotController.slideLeft(movePower,12);
             wait(1000);
             robotController.slideRight(movePower,12);
             wait(1000);


/*
             robotController.turnLeft(movePower,1500,TimeUnit.MILLISECONDS);
             wait(1000);
             robotController.turnRight(movePower,1500,TimeUnit.MILLISECONDS);
             wait(1000);
*/

/*
            robotController.turnLeft(0.05,90);
            wait(1000);
            robotController.turnRight(0.05,90);
*/
//            robotController.leftCalibrationFactor = 1.0;
            /*robotController.slideRight(movePower,18);
            wait(1000);
            robotController.slideLeft(movePower,18);
*/

/*
             robotController.splitStickArcadeDrive(0.1,0,0); // forward
             wait(500);
             robotController.stop();
             wait(1000);

             robotController.splitStickArcadeDrive(-0.1,0,0); // backward
             wait(500);
             robotController.stop();
             wait(1000);

             robotController.splitStickArcadeDrive(0,0.1,0); // slide right
             wait(1000);
             robotController.stop();
             wait(1000);

             robotController.splitStickArcadeDrive(0,-0.1,0); // slide left
             wait(1000);
             robotController.stop();
             wait(1000);

             robotController.splitStickArcadeDrive(0,0,0.1); // turn right
             wait(1000);
             robotController.stop();
             wait(1000);

             robotController.splitStickArcadeDrive(0,0,-0.1); // turn left
             wait(1000);
*/

             testComplete = true;
         }else{
             stop();
         }
        telemetry.update();
        
     }
 }
