package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.hardware.limelightvision.LLResult;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;
//import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

public class LimeLight3ACamera{

    //private IMU imu;
    private Telemetry telemetry;
    private Limelight3A limelight;
    private int goalID = 20; // Blue
    private int pipelineID = 0; // Pipeline for blue


    public LimeLight3ACamera(HardwareMap hardwareMap, Telemetry tmetry){
        telemetry = tmetry;
        init(hardwareMap);
    }

    private void init(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); 
        limelight.pipelineSwitch(pipelineID);    
    }

    public void start(){
        limelight.start();
    }

    public void stop(){
        limelight.stop();
    }

    /*
    public Pose3D getRobotPoseRelativeToGoal(int gID, int waitTime) {
        if(gID!=goalID){
            goalID = gID;
            if(goalID==20){
                pipelineID = 0;
            }else{
                pipelineID = 3; // Pipeline for red
            }
            limelight.pipelineSwitch(pipelineID);
        }

        try{
            Thread.sleep(waitTime);
        } catch (Exception e) {
            telemetry.addData("Error: ", e.getMessage());
        }

        LLResult result = limelight.getLatestResult();
        Pose3D robotPose = null;
        if (result != null && result.isValid()) {
            robotPose = result.getBotpose();
        }

        return robotPose;        
    }*/

    public double[] getRobotPoseRelativeToGoal(int gID, int waitTime) {
        if(gID!=goalID){
            goalID = gID;
            if(goalID==20){
                pipelineID = 0;
            }else{
                pipelineID = 3; // Pipeline for red
            }
            limelight.pipelineSwitch(pipelineID);
        }

        try{
            Thread.sleep(waitTime);
        } catch (Exception e) {
            telemetry.addData("Error: ", e.getMessage());
        }

        LLResult result = limelight.getLatestResult();
        List<FiducialResult> fiducials = result.getFiducialResults();
        double[] robotPose = null;

        for (FiducialResult fiducial : fiducials) {
            if(fiducial.getFiducialId()==goalID){
                robotPose = new double[3];
                robotPose[0] = fiducial.getTargetXDegrees();
                robotPose[1] = fiducial.getTargetYDegrees();
                //Pose3D robotPose3D = fiducial.getRobotPoseTargetSpace();
                //telemetry.addData("Robot pose",robotPose3D.toString());
                robotPose[2] = fiducial.getRobotPoseTargetSpace().getPosition().y; // Distance in meters
                break;
            }
        }
        return robotPose;    
    }

}
