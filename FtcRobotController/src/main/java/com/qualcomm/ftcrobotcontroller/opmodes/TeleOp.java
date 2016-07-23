package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class TeleOp extends OpMode {

    boolean tapeReversed = false;

    DcMotor motorRightFront;
    DcMotor motorRightRear;
    DcMotor motorLeftFront;
    DcMotor motorLeftRear;

    DcMotor winch;
    Servo tapeMeasure;
    //DcMotor pivot;

    DcMotor churroWheels;

    double rightThrottle;
    double leftThrottle;

    String orientation = "FORWARD";

    @Override
    public void init() {

        motorRightFront = hardwareMap.dcMotor.get("mRightFront");
        motorRightRear = hardwareMap.dcMotor.get("mRightRear");
        motorLeftFront = hardwareMap.dcMotor.get("mLeftFront");
        motorLeftRear = hardwareMap.dcMotor.get("mLeftRear");

        churroWheels = hardwareMap.dcMotor.get("mChurro");

        winch = hardwareMap.dcMotor.get("winch");

        //pivot = hardwareMap.dcMotor.get("mPivot");
        tapeMeasure = hardwareMap.servo.get("mTapeMeasure");

        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
        motorRightRear.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {

        if (orientation.equals("FORWARD")) {
            rightThrottle = gamepad1.right_stick_y;
            leftThrottle = gamepad1.left_stick_y;
        }

        else {
            rightThrottle = -gamepad1.right_stick_y;
            leftThrottle = -gamepad1.left_stick_y;
        }

        rightThrottle = Range.clip(rightThrottle, -1, 1);
        leftThrottle = Range.clip(leftThrottle, -1, 1);

        rightThrottle = scaleInput(rightThrottle);
        leftThrottle = scaleInput(leftThrottle);

        motorRightFront.setPower(rightThrottle);
        motorRightRear.setPower(rightThrottle);

        motorLeftFront.setPower(leftThrottle);
        motorLeftRear.setPower(leftThrottle);

        if (gamepad1.dpad_right) {
            orientation = "FORWARD";
        }
        if (gamepad1.dpad_left) {
            orientation = "BACKWARD";
        }
        if (gamepad1.dpad_up) {
            churroWheels.setPower(1);
        }
        if (gamepad1.dpad_down) {
            churroWheels.setPower(-1);
        }
        if (gamepad1.x) {
            churroWheels.setPower(0);
        }
        if (gamepad2.right_bumper) {
            winch.setPower(1);
        }
        if (gamepad2.left_bumper) {
            winch.setPower(-1);
        }
        if (gamepad2.x) {
            winch.setPower(0);
        }

        if (gamepad2.a) {
            if (tapeReversed) {
                tapeMeasure.setDirection(Servo.Direction.REVERSE);
                tapeReversed = false;
            }
            tapeMeasure.setPosition(1);
        }
        if (gamepad2.b) {
            if (!tapeReversed) {
                tapeMeasure.setDirection(Servo.Direction.REVERSE);
                tapeReversed = true;
            }
            tapeMeasure.setPosition(1);
        }
        if (gamepad2.y) {
            tapeMeasure.setPosition(.5);
        }

        telemetry.addData("TeleOp Started", "");

    }

    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}
