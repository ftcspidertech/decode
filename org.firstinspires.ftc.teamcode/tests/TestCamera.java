package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
//import java.util.Map;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.vision.GoalTagProcessor;
import org.firstinspires.ftc.teamcode.navigation.MecanumRobotController;
import org.firstinspires.ftc.teamcode.vision.LimeLight3ACamera;

//import com.qualcomm.robotcore.util.ReadWriteFile;
//import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
//import java.io.File;

@TeleOp()
public class TestCamera extends OpMode {
    //variables
    private LimeLight3ACamera limelight;
    private MecanumRobotController robot;
    private boolean turnTaskDone = false;
    private boolean moveTaskDone = false;
    private double kTurn = 0.9;
    private double kMove = 0.8;
    private double[] robotPose = null;
    //private double[] rangeBearing;
    //StringBuilder sb = null;

    @Override
    public void init() {

        limelight = new LimeLight3ACamera(hardwareMap,telemetry);
        limelight.start();

        robot = new MecanumRobotController(hardwareMap, telemetry);
        
        //sb = new StringBuilder();

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
        
        /*
        robotPose = limelight.getRobotPoseRelativeToGoal(20,100);
        if(robotPose==null){
            robotPose = limelight.getRobotPoseRelativeToGoal(24,100);
            if(robotPose==null){
                telemetry.addLine("Goal out of vision!");
                return;
            }
        }*/
        //robotPose = limelight.getRobotPoseRelativeToBall(4,100);
        robotPose = limelight.getAverageBallPose(4,100);
        if(robotPose==null){
            telemetry.addLine("Ball out of vision!");
            return;
        }else{
            robotPose[0] = robotPose[0] + 5;
        }
        
        telemetry.addData(">> Robot position","tx=%f deg, ty=%f deg, tz=%f inch",
            robotPose[0],robotPose[1],robotPose[2]);
        //sb.append(robotPose[0]+","+robotPose[1]+","+robotPose[2]).append("\n"); 
        //*/

        if(!turnTaskDone){
            
            //*
            if(robotPose[0]<0){
                robot.turnLeft(0.1,Math.abs(0.9*robotPose[0]));
            }else if(robotPose[0]>0){
                robot.turnRight(0.1,Math.abs(0.9*robotPose[0]));
            }
            //*/
            
            turnTaskDone = true;
        }
        
        //*
        if(!moveTaskDone){
            //robot.goForward(0.1,Math.abs(kMove*robotPose[2]));
            
            //robot.saveFile = true;
            //robot.filename = "withmodulation_s0p25_b45_k1p25_t10.txt";
            //robot.timeout = 2000;
            //robot.tickTolerance = 10;
            
            robot.timeout =2000;
            robot.goToPositionWithSpeedModulation(0.125, robotPose[2], 0.5, 3, DistanceUnit.INCH); // forward:0.35  backward:1.25
            
            //robot.goForward(0.25, 45.0, false);
            
            //robot.goForward(0.25, 45.0, true);
            
            //robot.goBackward(0.25, 45.0, true);
            
            telemetry.addLine("Move task done!");
            
            moveTaskDone = true;
        }//*/
        
    }
    
    @Override
    public void stop() {
        robot.stop();
        //goalTagProcessor.close();
        limelight.stop();
        
        //String filename = "robotpose.txt";
        //File file = AppUtil.getInstance().getSettingsFile(filename);
        //ReadWriteFile.writeFile(file,sb.toString());
    }
}
