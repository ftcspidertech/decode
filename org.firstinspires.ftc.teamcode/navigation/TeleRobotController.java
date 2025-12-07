package org.firstinspires.ftc.teamcode.navigation;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.navigation.RobotController;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class TeleRobotController {
    private RobotController robot;
    private Gamepad gamepad;
    private double maxMoveSpeed;
    private double maxTurnSpeed;
    private double currSpeed;
    private double accel;
    private int motionMode;
    private boolean yOn=false,aOn=false;
    private Telemetry telemetry;
    
    public TeleRobotController(HardwareMap hardwareMap, Gamepad pad, Telemetry tmetry) {
        
        robot = new RobotController(hardwareMap,tmetry);
        gamepad = pad;
        
        maxMoveSpeed = 0.75;
        maxTurnSpeed = 0.5;
        currSpeed = 0;
        accel = 1.0/50.0;
        motionMode = 0; // stop
        
        telemetry = tmetry;
    }
    
    public void run(){
        // Control robot with arrow keys.
        double speed, curvature;
        
        if(gamepad.dpad_left){
            currSpeed += accel;
            currSpeed = Math.min(maxTurnSpeed,currSpeed);
            motionMode = 3; // Turn left
        }else if(gamepad.dpad_right){
            currSpeed += accel;
            currSpeed = Math.min(maxTurnSpeed,currSpeed);
            motionMode = 4; // Turn right
        }else if(gamepad.dpad_up){
            currSpeed += accel;
            currSpeed = Math.min(maxMoveSpeed,currSpeed);
            motionMode = 1; // Move forward
        }else if(gamepad.dpad_down){
            currSpeed += accel;
            currSpeed = Math.min(maxMoveSpeed,currSpeed);
            motionMode = 2; // Move backward
        }else{
            currSpeed -= accel;
            currSpeed = Math.max(0,currSpeed);
            if(currSpeed==0){
                motionMode = 0;
            }
        }
        
        curvature = -gamepad.right_stick_x;
        if(motionMode==1){
            speed = sigmoid(currSpeed,15,maxMoveSpeed/2.0,maxMoveSpeed);
            if(curvature<0){
                robot.driveMotorCurveRight(speed,Math.abs(curvature));
            }else if(curvature>0){
                robot.driveMotorCurveLeft(speed,curvature);
            }else{
                robot.goForward(speed);
            }
        }else if(motionMode==2){
            speed = sigmoid(currSpeed,15,maxMoveSpeed/2.0,maxMoveSpeed);
            //goBackward(sfilter(currSpeed,0,maxMoveSpeed));
            if(curvature<0){
                robot.driveMotorCurveRight(-speed,Math.abs(curvature));
            }else if(curvature>0){
                robot.driveMotorCurveLeft(-speed,curvature);
            }else{
                robot.goBackward(speed);
            }
        }else if(motionMode==3 && curvature==0){
            robot.turnLeft(sfilter(currSpeed,0,maxTurnSpeed));
        }else if(motionMode==4 && curvature==0){
            robot.turnRight(sfilter(currSpeed,0,maxTurnSpeed));
        }else{
            robot.stopMotor();
        }
        
        /*
        if(gamepad.y){
            telemetry.addData("Gamepad Y:",gamepad.y);
            if(!robot.isRobotMoving()){
                yOn = false;
            }
            telemetry.addData("    yOn:",yOn);
            if(!yOn){
                yOn = true;
                telemetry.addData("    yOn:",yOn);
                //robot.turnLeft(0.2,45);
                robot.goForward(0.2,1,DistanceUnit.INCH);
            }
        }

        if(gamepad.a){
            telemetry.addData("Gamepad A:",gamepad.a);
            if(!robot.isRobotMoving()){
                aOn = false;
            }
            telemetry.addData("    aOn:",aOn);
            if(!aOn){
                aOn = true;
                telemetry.addData("    aOn:",aOn);
                //robot.turnRight(0.2,45);
                robot.goBackward(0.2,1,DistanceUnit.INCH);
            }
        }*/
        
    }
    
    private double sfilter(double x, double a, double b){
        double y;
        if(x<=a){
            y = 0;
        }else if(x>a && x<=(a+b)/2.0){
            y = 2.0*(Math.pow((x-a)/(b-a),2));
        }else if(x>(a+b)/2.0 && x<=b){
            y = 1.0 - 2.0*(Math.pow((x-b)/(b-a),2));
        }else{ // x>b
            y = 1.0;
        }
        return y*b;
    }
    
    private double sigmoid(double x, double a, double c, double scale){
        return scale*(1.0/(1+Math.exp(-a*(x-c))));
    }

}
