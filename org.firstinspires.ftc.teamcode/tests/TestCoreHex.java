package org.firstinspires.ftc.teamcode.practice;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp()
public class TestCoreHex extends OpMode {
    //variables
    private DcMotor coreHex;
  
    @Override
    public void init() {
        // Get motor
        coreHex = hardwareMap.get(DcMotor.class, "coreHex");
        coreHex.setDirection(DcMotor.Direction.REVERSE);

        // Display status
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {
      coreHex.setPower(1.0);
    }
    
}
