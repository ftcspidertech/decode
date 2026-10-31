
package org.firstinspires.ftc.teamcode.biobuzz.navigation;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.biobuzz.navigation.MecanumRobotController;

public class TeleMecanumRobotController {
    public double kMove = 0.25;
    public double kTurn = 0.125;
    private double rate = 0.01;
    private boolean leftTriggerPressed = false;
    private boolean rightTriggerPressed = false;
    
    private MecanumRobotController robot;
    private Gamepad gamepad;
    private Telemetry telemetry;
    
    public TeleMecanumRobotController(HardwareMap hardwareMap, Gamepad pad, Telemetry tmetry) {
        
        robot = new MecanumRobotController(hardwareMap,tmetry,1.0,1.0);
        gamepad = pad;
        
        telemetry = tmetry;
    }
    
    public void run(){
        // Update max speeds
        //*
        if(gamepad.right_trigger==1.0 && !rightTriggerPressed){
            rightTriggerPressed = true;
            kMove = Math.min(1,kMove+rate);
            kTurn = Math.min(1,kTurn+rate);
            telemetry.addData("Navigation","kMove=%f, kTurn=%f",kMove,kTurn);
        }else{
            rightTriggerPressed = false;
        }

        if(gamepad.left_trigger==1.0 && !leftTriggerPressed){
            leftTriggerPressed = true;
            kMove = Math.max(0.1,kMove-rate);
            kTurn = Math.max(0.1,kTurn-rate);
            telemetry.addData("Navigation","kMove=%f, kTurn=%f",kMove,kTurn);
        }else{
            leftTriggerPressed = false;
        }//*/

        // Control robot with joystick.
        float y = -gamepad.left_stick_y;
        float x = gamepad.left_stick_x;
        float rx = gamepad.right_stick_x;
        if (y!=0 || x!=0 || rx!=0){
            robot.splitStickArcadeDrive(y*kMove,x*kMove,rx*kTurn);            
            return;
        }
        
        // Control robot with arrow keys.
        if(gamepad.dpad_left){
            robot.turnLeft(kTurn);
        }else if(gamepad.dpad_right){
            robot.turnRight(kTurn);
        }else if(gamepad.dpad_up){
            robot.goForward(kMove);
        }else if(gamepad.dpad_down){
            robot.goBackward(kMove);
        }else{
            robot.stop();
        }
        
    }
    
    public MecanumRobotController getRobotController(){
        return robot;
    }
    
    public void goForward(double speed, double distanceInInch){
        robot.goForward(speed,distanceInInch);
    }

    public void turnLeft(double speed, double angle){
        robot.turnLeft(speed,angle);
    }

    public void turnRight(double speed, double angle){
        robot.turnRight(speed,angle);
    }

    public void stop(){
        robot.stop();
    }
}
