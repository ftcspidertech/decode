package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.decode.AutoDecode;
import org.firstinspires.ftc.teamcode.navigation.TurnMethod;

@Autonomous()
public class BLUE_Auto_Position2  extends AutoDecode{
    /*
    *  Controller variables from the superclass:
    *      - robotController
    *      - flyWheelController
    *      - IntakeController
    */
    
    // Navigation variables   
    private double movePower = 0.9;
    private double turnPower = 0.6;
    private boolean useSpeedCorrection = true;
    
    // Task variables
    // Task 1: Throw pre-loaded balls. 
    private boolean taskOneDone = false;
    // Task 2: Collect and throw another set of balls.
    private boolean taskTwoDone = false;
    
     
    
    @Override
    public void loop() {
        //double left=1.0;
        //double right=0.87;
        //setCalibrationFactors(left,right);
        
        if(!taskOneDone) {
            // Go forward and turn to the goal.
            flyWheelController.setVelocity(1425);
            robotController.goBackward(movePower*.8,50,DistanceUnit.INCH,useSpeedCorrection);
            wait(1500);
            /*robotController.turnLeft(turnPower,173);
            wait(200); */
            
            // Open the exit door. Keep it open for long enough to throw all the balls.
            flyWheelController.openDoor();
            wait(3000);
            flyWheelController.closeDoor();
            wait(200);
            
            // Task1 done             
            taskOneDone = true;
        }else if(!taskTwoDone){
            // Go back and turn to the (1st row) 2nd set of balls.
            robotController.goBackward(movePower,4,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            robotController.turnLeft(turnPower,36);
            wait(200);
            
            // Go and collect the balls. Go slowly to grab the balls.
            robotController.goForward(movePower,42,DistanceUnit.INCH,useSpeedCorrection);
            wait(400);
            
            // Go back and turn to the goal.
            robotController.goBackward(movePower,38,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            robotController.turnRight(turnPower,37);
            wait(200);
            
            // Open the exit door. Keep it open for long enough to throw all the balls.
            flyWheelController.openDoor();
            wait(3000);
            flyWheelController.closeDoor();
            wait(200);
            robotController.turnRight(movePower,37); //move away from starting triangle 
            wait(200);
            robotController.goBackward(movePower,20,DistanceUnit.INCH,useSpeedCorrection);
            wait(200);
            //robotController.turnRight(turnPower,85);
            robotController.turnLeft(turnPower,1.1,TimeUnit.SECONDS);//turn to balls
            wait(200);
            //robotController.goForward(movePower,40,DistanceUnit.INCH,false);
            robotController.goForward(movePower,1.5,TimeUnit.SECONDS);
            wait(200);
            robotController.goBackward(movePower,1.5,TimeUnit.SECONDS);
            wait(300);
            /*robotController.goBackward(movePower);
            telemetry.addLine("Now going back");
            
            wait(1000);

            telemetry.addLine("I'm  
           back");
            telemetry.update(); */
            
            robotController.turnRight(movePower,0.45,TimeUnit.SECONDS);
            wait(200);
            robotController.slideRight(movePower);
            wait(500);
            robotController.stop();
            flyWheelController.openDoor();
            wait(3000);
            flyWheelController.closeDoor();
            wait(100);
            robotController.slideLeft(movePower);
            wait(500);
            robotController.stop();
            
            // Task2 done.
            taskTwoDone = true;
        }else{
            stop();
        }
    }
}
