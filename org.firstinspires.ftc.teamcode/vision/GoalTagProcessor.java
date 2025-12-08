package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
//import org.firstinspires.ftc.robotcore.external.navigation.Position;
//import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
//import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D

import java.util.List;

public class GoalTagProcessor {
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;
    private int goalID;

    public GoalTagProcessor(HardwareMap hardwareMap, int id) {
    	    	    	
    	goalID = id;
    	
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        aprilTagProcessor = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = VisionPortal.easyCreateWithDefaults(webcamName,aprilTagProcessor);    	
    	
    }
    
    public Pose3D getRobotPose() {
        List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();
        Pose3D robotPose = null;
        for (AprilTagDetection detection : currentDetections) {
        	if(detection.id==goalID) {
        		robotPose = detection.robotPose;
        	}
        }
        return robotPose;
    }
    
    public double[] getRangeBearing() {
    	
    	List<AprilTagDetection> currentDetections = aprilTagProcessor.getDetections();
    	double rangeBearing = null;
        for (AprilTagDetection detection : currentDetections) {
        	if(detection.id==goalID) {
         		rangeBearing = new double[2];
         		rangeBearing[0] = detection.ftcPose.range;
         		rangeBearing[0] = detection.ftcPose.bearing;
         		break;
        	}
        }
    	 
        return rangeBearing;
    }
    
    public void stopStreaming() {
    	visionPortal.stopStreaming();
    }
    
    public void resumeStreaming() {
    	visionPortal.resumeStreaming();
    }
    
    public void close() {
    	visionPortal.close();
    }
}
