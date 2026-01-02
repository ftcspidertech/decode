package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.vision.GoalTagProcessor;
import org.firstinspires.ftc.teamcode.navigation.RobotController;

@TeleOp()
public class TestCamera extends OpMode {
    //variables
    private GoalTagProcessor goalTagProcessor;
    private RobotController robot;
    private double[] rangeBearing;
  
    @Override
    public void init() {

        goalTagProcessor = new GoalTagProcessor(hardwareMap,24);
        robot = new RobotController(hardwareMap, telemetry);

        // Display status
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {
        rangeBearing = goalTagProcessor.getRangeBearing();
        if(rangeBearing!=null){
            telemetry.addData("range = ",rangeBearing[0]);
            telemetry.addData("bearing = ",rangeBearing[1]);
            
            if(rangeBearing[1]>1){
                robot.turnLeft(0.1,rangeBearing[1]/10.0);
            }else if(rangeBearing[1]<-1){
                robot.turnRight(0.1,rangeBearing[1]/10.0);
            }/*else{
                robot.stop();
            }*/
            
            if(rangeBearing[0]>6){
                robot.goForward(0.25,rangeBearing[0]/10.0,DistanceUnit.INCH);
            }/*else{
                robot.stop();
            }*/
            
            if(Math.abs(rangeBearing[1])<=1 && rangeBearing[0]<=6){
                robot.stop();
            }
            
        }else{
            robot.stop();
            telemetry.addData("Out of vision","");
        }
    }
    
    @Override
    public void stop() {
        robot.stop();
        goalTagProcessor.close();
    }
}
