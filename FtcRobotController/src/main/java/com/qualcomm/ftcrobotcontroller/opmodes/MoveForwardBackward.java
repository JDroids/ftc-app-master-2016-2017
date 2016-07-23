package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by arush on 7/9/2016.
 */
public class MoveForwardBackward extends PushBotHardware {

    DcMotor mtrRightFront;
    DcMotor mtrLeftFront;
    DcMotor mtrLeftBack;
    DcMotor mtrRightBack;

    @Override
    public void init() {
        mtrRightFront= hardwareMap.dcMotor.get("motor_1");
        mtrLeftFront= hardwareMap.dcMotor.get("motor_2");
        mtrLeftBack= hardwareMap.dcMotor.get("motor_3");
        mtrRightBack= hardwareMap.dcMotor.get("motor_4");

    }

    @Override
    public void loop() {

        run_using_encoders();

        while(mtrLeftFront.getCurrentPosition() < 4000) {
            mtrLeftFront.setPower(1);
            mtrLeftBack.setPower(1);
            mtrRightFront.setPower(1);
            mtrRightFront.setPower(1);
        }

        mtrLeftFront.setPower(0);
        mtrLeftBack.setPower(0);
        mtrRightFront.setPower(0);
        mtrRightFront.setPower(0);

        while (mtrLeftFront.getCurrentPosition() > 0) {
            mtrLeftFront.setPower(-1);
            mtrLeftBack.setPower(-1);
            mtrRightFront.setPower(-1);
            mtrRightFront.setPower(-1);
        }
    }
}
