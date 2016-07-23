package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.Timer;

public class BlueAutonomous extends OpMode {

    int state = 0;

    double whiteLineEncoderCount = 0;

    DcMotor motorLeftFront;
    DcMotor motorLeftRear;
    DcMotor motorRightFront;
    DcMotor motorRightRear;

    OpticalDistanceSensor odsFront;
    OpticalDistanceSensor odsRear;

    ColorSensor csLeft;

    Servo climberSwinger;
    Servo climberDropper;
    Servo rightPusher;
    float hsvValues[] = {0F,0F,0F};

    public boolean hasEncoderReset() {
        if (motorRightFront.getCurrentPosition() == 0) {
            return true;
        }
        return false;
    }

    public void runUsingEncoders() {
        motorLeftFront.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorLeftRear.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRightFront.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRightRear.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    public boolean hasRightEncoderReached(double count) {
        if (Math.abs (motorRightFront.getCurrentPosition ()) > count) {
            return true;
        }
        return false;
    }

    public void resetDriveEncoders() {
        motorLeftFront.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorLeftRear.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorRightFront.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorRightRear.setMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    public double getOdsFrontValue() {
        return odsFront.getLightDetectedRaw();
    }

    public double getOdsRearValue() {
        return odsRear.getLightDetectedRaw();
    }

    public void set_drive_power(double left, double right) {
        motorLeftFront.setPower(left);
        motorLeftRear.setPower(left);
        motorRightFront.setPower(right);
        motorRightRear.setPower(right);
    }

    @Override
    public void init() {

        motorLeftFront = hardwareMap.dcMotor.get("mLeftFront");
        motorLeftRear = hardwareMap.dcMotor.get("mLeftRear");
        motorRightFront = hardwareMap.dcMotor.get("mRightFront");
        motorRightRear = hardwareMap.dcMotor.get("mRightRear");

        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
        motorRightRear.setDirection(DcMotor.Direction.REVERSE);

        odsFront = hardwareMap.opticalDistanceSensor.get("odsFront");
        odsRear = hardwareMap.opticalDistanceSensor.get("odsRear");

        csLeft = hardwareMap.colorSensor.get("colorSensorRight");

        //leftPusher = hardwareMap.servo.get("leftPusher");
        rightPusher = hardwareMap.servo.get("rightPusher");
        //climberSwinger = hardwareMap.servo.get("climberSwinger");
        //climberDropper = hardwareMap.servo.get("climberDropper");

    }

    public void start() {
        super.start();

    }

    public void loop() {
        Color.RGBToHSV((csLeft.red() * 255) / 800, (csLeft.green() * 255) / 800, (csLeft.blue() * 255) / 800, hsvValues);
        telemetry.addData("Hue", hsvValues[0]);

        telemetry.addData("ODS Front", getOdsFrontValue());
        telemetry.addData("ODS Back", getOdsRearValue());

        telemetry.addData("State", state);
        telemetry.addData("Encoder", Math.abs(motorRightFront.getCurrentPosition()));

        switch(state) {
            case 0:
                set_drive_power(.2, .2);
                state++;
                break;

            case 1:
                if (getOdsFrontValue() > 30) {
                    state++;
                    break;
                }
                else {
                    break;
                }

            case 2:
                set_drive_power(.5, -.5);
                state++;

            case 3:
                if (odsRear.getLightDetectedRaw() > 30) {
                    runUsingEncoders();
                    resetDriveEncoders();
                    state++;
                    break;
                }
                else {
                    break;
                }

            case 4:
                if (hasEncoderReset()) {
                    state++;
                    break;
                }
                else {
                    break;
                }

            case 5:
                runUsingEncoders();
                if (hasRightEncoderReached(200)) {
                    set_drive_power(0,0);
                    state++;
                    break;
                }
                else {
                    break;
                }

            case 6:

                if (hsvValues[0] > 220 && hsvValues[0] < 300) {
                    rightPusher.setPosition(1);
                }
                else {
                    break;
                }

            default:
                telemetry.addData("Default state","");

                set_drive_power(0,0);

        }

    }

}
