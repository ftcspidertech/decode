package org.firstinspires.ftc.teamcode.decode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.util.ElapsedTime;

public class IntakeControllerPS5 {
    private DcMotorEx intakeMotor;
    private Gamepad gamepad;
    private double currentPower = 0.8;
    private Telemetry telemetry;
    private boolean isRunning = false;
    private boolean isIntakeMode = true;
    private ElapsedTime stallTimer = new ElapsedTime();
    private final int MIN_VELOCITY = 1000; // ticks
    private final double MIN_WAIT = 0.25; // seconds
    
    public IntakeControllerPS5(HardwareMap hardwareMap, Gamepad pad, Telemetry tmetry){
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        gamepad = pad;
        telemetry = tmetry;
    }
    
    public void run(){
        // OPTIONS button (start) = intake mode toggle
        // CREATE button (back) = outtake mode toggle
        // These map to the PS5's Options and Create buttons
        
        if (gamepad.startWasPressed()) {  // Options button on PS5
            if(!isRunning){
                setPower(currentPower);
            }else{
                if(isIntakeMode){
                    stop();
                }else{
                    stop();
                    setPower(currentPower);
                }
            }
            isIntakeMode = true;
        }else if(gamepad.backWasPressed()){  // Create button on PS5
            if(!isRunning){
                setPower(-currentPower);
            }else{
                if(isIntakeMode){
                    stop();
                    setPower(-currentPower);
                }else{
                    stop();
                }
            }
            isIntakeMode = false;
        }
        
        // Triangle button (y) restarts intake if stalled
        restartIfStalled(gamepad.y);
        
        // Check if the motor stalled for multiple balls
        stopIfStalled();
    }
    
    private void setPower(double power){
        intakeMotor.setPower(power);
        isRunning = true;
        stallTimer.reset();
    }
    
    public void start(){
        if(isIntakeMode){
            setPower(currentPower);
        }else{
            setPower(-currentPower);
        }        
    } 
    
    public void stop(){
        intakeMotor.setPower(0.0);
        isRunning = false;
    }
    
    public void stopIfStalled(){
        if(stallTimer.seconds()>MIN_WAIT && intakeMotor.getPower()!=0 && 
           Math.abs(intakeMotor.getVelocity())<MIN_VELOCITY){
            stop();
        }
    }
    
    public void restartIfStalled(boolean youCanRestart){
        if(youCanRestart && intakeMotor.getPower()==0){
            start();
        }
    }    
}
