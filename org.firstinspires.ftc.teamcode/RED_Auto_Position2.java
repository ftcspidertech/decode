package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.decode.AutoDecode;
import org.firstinspires.ftc.teamcode.navigation.TurnMethod;

@Autonomous()
public class RED_Auto_Position2  extends AutoDecode{
    /*
    *  Controller variables from the superclass:
    *      - robotController
    *      - flyWheelController
    *      - IntakeController
    */
    
    // Navigation variables   
    private double movePower = .95;
    private double turnPower = 0.6;
    private boolean useSpeedCorrection = true;
    
    // Task variables
    // Task 1: Throw pre-loaded balls. 
    private boolean taskOneDone = false;
    // Task 2: Collect and throw another set of balls.
    private boolean taskTwoDone = false;
    private int regularFlywheelSpeed=1425;
    private int lowerFlywheelSpeed=1300;
    
     
    
    @Override
    public void loop() {
        intakeController.stopIfStalled();
        if(!taskOneDone) {
            // Go forward and turn to the goal.
            flyWheelController.setVelocity(regularFlywheelSpeed);
            robotController.goBackward(movePower*.9,50,DistanceUnit.INCH,useSpeedCorrection);
            //wait(1500);
            /*robotController.turnLeft(turnPower,173);
            wait(200); */
            
            // Open the exit door. Keep it open for long enough to throw all the balls.
            openDoor();
            wait(2000);
            flyWheelController.setVelocity(lowerFlywheelSpeed);
            wait(1000);
            flyWheelController.closeDoor();
            wait(100);
            flyWheelController.setVelocity(regularFlywheelSpeed);
            
            // Task1 done             
            taskOneDone = true;
        }else if(!taskTwoDone){
            // Go back and turn to the 2nd set of balls.
            robotController.goBackward(movePower,4,DistanceUnit.INCH,useSpeedCorrection);
            wait(100);
            robotController.turnRight(turnPower,33);
            wait(100);
            
            // Go and collect the balls. Go slowly to grab the balls.
            robotController.goForward(movePower,40,DistanceUnit.INCH,useSpeedCorrection);
            wait(100);
            
            // Go back and turn to the goal.
            robotController.goBackward(movePower,38,DistanceUnit.INCH,useSpeedCorrection);
            wait(100);
            robotController.turnLeft(turnPower,31);
            wait(100);
            
            // Open the exit door. Keep it open for long enough to throw all the balls.
            openDoor();
            wait(2000);
            flyWheelController.setVelocity(lowerFlywheelSpeed);
            wait(1000);
            flyWheelController.closeDoor();
            wait(100);
            flyWheelController.setVelocity(regularFlywheelSpeed);
            robotController.turnLeft(movePower,37); //move away from starting triangle 
            wait(100);
            robotController.goBackward(movePower,23,DistanceUnit.INCH,useSpeedCorrection);
            wait(100);
            //robotController.turnRight(turnPower,85);
            robotController.turnRight(turnPower*.9,1,TimeUnit.SECONDS);
            wait(100);
            //robotController.goForward(movePower,40,DistanceUnit.INCH,false);
            robotController.goForward(movePower,1.2,TimeUnit.SECONDS);
            wait(300);
            robotController.goBackward(movePower,1.2,TimeUnit.SECONDS);
            wait(100);
            /*robotController.goBackward(movePower);
            telemetry.addLine("Now going back");
            
            wait(1000);

            telemetry.addLine("I'm  
           back");
            telemetry.update(); */
            
            robotController.turnLeft(movePower*.9,0.4,TimeUnit.SECONDS);
            wait(100);
            robotController.slideLeft(movePower);
            wait(600);
            robotController.stop();
            openDoor();
            wait(2000);
            flyWheelController.setVelocity(lowerFlywheelSpeed);
            wait(1000);
            flyWheelController.closeDoor();
            wait(100);
            flyWheelController.setVelocity(regularFlywheelSpeed);
            robotController.slideRight(movePower);
            wait(500);
            robotController.stop();
            
            // Task2 done.
            taskTwoDone = true;
        }else{
            stop();
        }
    }
}
