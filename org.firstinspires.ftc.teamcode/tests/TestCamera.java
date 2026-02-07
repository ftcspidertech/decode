package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
//import java.util.Map;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.vision.GoalTagProcessor;
import org.firstinspires.ftc.teamcode.navigation.RobotController;
import org.firstinspires.ftc.teamcode.vision.LimeLight3ACamera;

@TeleOp()
public class TestCamera extends OpMode {
    //variables
    private LimeLight3ACamera limelight;
    //private RobotController robot;
    //private double[] rangeBearing;
  
    @Override
    public void init() {

        limelight = new LimeLight3ACamera(hardwareMap,telemetry);
        limelight.start();
        
        //robot = new RobotController(hardwareMap, telemetry);

        // Display status
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {
        /*
        rangeBearing = goalTagProcessor.getRangeBearing();
        if(rangeBearing!=null){
            telemetry.addData("range = ",rangeBearing[0]);
            telemetry.addData("bearing = ",rangeBearing[1]);
            
            if(rangeBearing[1]>1){
                robot.turnLeft(0.25,rangeBearing[1]*0.8);
            }else if(rangeBearing[1]<-1){
                robot.turnRight(0.25,rangeBearing[1]*0.8);
            }
            //else{
            //    robot.stop();
            //}
            
            if(rangeBearing[0]>6){
                robot.goForward(0.25,rangeBearing[0]/10.0,DistanceUnit.INCH);
            }
            //else{
            //    robot.stop();
            //
            
            if(Math.abs(rangeBearing[1])<=1 && rangeBearing[0]<=6){
                robot.stop();
            }
            
        }else{
            robot.stop();
            telemetry.addData("Out of vision","");
        }*/
        
        double[] robotPose = limelight.getRobotPoseRelativeToGoal(20,100);
        telemetry.addData(">> Robot position","tx=%f deg, ty=%f deg, tz=%f",
            robotPose[0],robotPose[1],robotPose[2]);
    }
    
    @Override
    public void stop() {
        //robot.stop();
        //goalTagProcessor.close();
        limelight.stop();
    }
}
