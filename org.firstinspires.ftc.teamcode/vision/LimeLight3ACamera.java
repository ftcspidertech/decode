package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.hardware.limelightvision.LLResult;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes.ColorResult;
//import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

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
    private ElapsedTime waitTimer = new ElapsedTime();

    public LimeLight3ACamera(HardwareMap hardwareMap, Telemetry tmetry){
        telemetry = tmetry;
        init(hardwareMap);
    }

    private void init(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); 
        while(!limelight.pipelineSwitch(pipelineID));    
    }

    public void start(){
        limelight.start();
    }

    public void stop(){
        limelight.stop();
    }

    public double[] getRobotPoseRelativeToGoal(int gID, int waitTime) {
        updatePipeline(gID);
        wait(50);
        
        LLResult result;// = limelight.getLatestResult();
        List<FiducialResult> fiducials;// = result.getFiducialResults();
        double[] robotPose = null;
        double ms;
        
        waitTimer.reset();
        result = limelight.getLatestResult();
        //ms = waitTimer.milliseconds();
        while(!result.isValid() && waitTimer.milliseconds()<=waitTime){
            result = limelight.getLatestResult();
            //ms = waitTimer.milliseconds();
        }
        
        if(result.isValid()){
            telemetry.addLine("Valid result found");
        }else{
            telemetry.addLine("Valid result noy found");
            return robotPose;
        }
        
        waitTimer.reset();
        fiducials = result.getFiducialResults();
        //ms = waitTimer.milliseconds();
        while(fiducials.size()==0 && waitTimer.milliseconds()<=waitTime){
            fiducials = result.getFiducialResults();
            //ms = waitTimer.milliseconds();
        }
        if(fiducials.size()>0){
            telemetry.addData("Tag data found",fiducials.size());
        }else{
            telemetry.addLine("Tag not found");
            return robotPose;
        }
        

        for (FiducialResult fiducial : fiducials) {
            if(fiducial.getFiducialId()==goalID){
                telemetry.addData("Tag ID found",fiducial.getFiducialId());
                robotPose = new double[3];
                robotPose[0] = fiducial.getTargetXDegrees();
                robotPose[1] = fiducial.getTargetYDegrees();
                robotPose[2] = (28.5-14.0)/Math.tan(Math.toRadians(robotPose[1]+8.25));
                break;
            }
        }
        return robotPose;    
    }

    public double[] getRobotPoseRelativeToBall(int waitTime, boolean useMeanFilter) {
        double[] poseP = null;
        double[] poseG = null;
        double[] robotPose=null;
        
        if(useMeanFilter){
            int step = waitTime/3;
            poseP = getAverageBallPose(1, step);
            poseG = getAverageBallPose(3, step);
        }else{
            poseP = getRobotPoseRelativeToBall(1, waitTime);
            poseG = getRobotPoseRelativeToBall(3, waitTime);
        }
        
        if(poseP==null){
            robotPose = poseG;
        }else if(poseG==null){
            robotPose = poseP;
        }else{
            if(poseP[2]<=poseG[2]){
                robotPose = poseP;
            }else{
                robotPose = poseG;
            }
        }
        
        return robotPose;
    }

    private double[] getAverageBallPose(int id, int waitTime){
        double txAvg = 0;
        double tyAvg = 0;
        double distAvg = 0;
        double count = 0;
        double[] pose=null;
        double[] tmp=null;

        for(int loop=0;loop<3;loop++){
            tmp = getRobotPoseRelativeToBall(id, waitTime);
            if(tmp!=null){
                txAvg = txAvg + tmp[0];
                tyAvg = tyAvg + tmp[1];
                distAvg = distAvg + tmp[2];
                count = count + 1;
            }
        }
        if(count>0){
            txAvg = txAvg/count;
            tyAvg = tyAvg/count;
            distAvg = distAvg/count;
            pose = new double[3];
            pose[0] = txAvg; 
            pose[1] = tyAvg; 
            pose[2] = distAvg; 
        }
        
        return pose;
    }

    public double[] getRobotPoseRelativeToBall(int waitTime) {
        double[] poseP = getRobotPoseRelativeToPurpleBall(waitTime);
        double[] poseG = getRobotPoseRelativeToGreenBall(waitTime);
        double[] robotPose;
        
        if(poseP==null){
            robotPose = poseG;
        }else if(poseG==null){
            robotPose = poseP;
        }else{
            if(poseP[2]<=poseG[2]){
                robotPose = poseP;
            }else{
                robotPose = poseG;
            }
        }
        
        return robotPose;
    }

    public double[] getRobotPoseRelativeToPurpleBall(int waitTime) {
        return getRobotPoseRelativeToBall(1, waitTime);
    }

    public double[] getRobotPoseRelativeToGreenBall(int waitTime) {
        return getRobotPoseRelativeToBall(3, waitTime);
    }

    public double[] getRobotPoseRelativeToBall(int id, int waitTime) {
        pipelineID = id;
        while(!limelight.pipelineSwitch(pipelineID));
        
        wait(waitTime);
        
        LLResult result = limelight.getLatestResult();
        List<ColorResult> colorTargets = result.getColorResults();
        double[] robotPose = null;
        
        for (ColorResult colorTarget : colorTargets) {
            
            robotPose = new double[3];
            
            robotPose[0] = colorTarget.getTargetXDegrees();
            robotPose[1] = colorTarget.getTargetYDegrees();
            robotPose[2] = (0-14)/Math.tan(Math.toRadians(robotPose[1]+3));
            
            break;
        }
        
        return robotPose;
    }



    private void updatePipeline(int gID){
        if(gID!=goalID){
            goalID = gID;
        }
        if(goalID==20){
            pipelineID = 0; // Pipeline for blue
        }else{
            pipelineID = 2; // Pipeline for red
        }
        while(!limelight.pipelineSwitch(pipelineID));
    } 
    
    /*
    private void wait(int waitTime){
        try{
            Thread.sleep(waitTime);
        } catch (Exception e) {
            telemetry.addData("Error: ", e.getMessage());
        }
    }*/
    
    private void wait(int ms){
        waitTimer.reset();
        while(waitTimer.milliseconds()<ms);
    }
    

}
