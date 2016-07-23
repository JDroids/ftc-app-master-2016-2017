package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class PushBotHardware extends OpMode {

    public PushBotHardware () {}

    @Override public void init () {

        v_warning_generated = false;
        v_warning_message = "Can't map; ";

        try
        {
            motorLeftFront = hardwareMap.dcMotor.get("mLeftFront");
            motorLeftRear = hardwareMap.dcMotor.get("mLeftRear");
            motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
        }
        catch (Exception p_exeception)
        {
            m_warning_message ("left_drive");
            DbgLog.msg (p_exeception.getLocalizedMessage ());
            telemetry.addData("Could not instantiate left motors", "");
            motorLeftFront = null;
        }

        try
        {
            motorRightFront = hardwareMap.dcMotor.get ("mRightFront");
            motorRightRear = hardwareMap.dcMotor.get("mRightRear");
            motorRightRear.setDirection(DcMotor.Direction.REVERSE);
        }
        catch (Exception p_exeception)
        {
            m_warning_message ("right_drive");
            DbgLog.msg (p_exeception.getLocalizedMessage());
            telemetry.addData("Could not instantiate right motors", "");
            motorRightFront = null;
        }

        try {
            odsFront = hardwareMap.opticalDistanceSensor.get("odsFront");
            odsRear = hardwareMap.opticalDistanceSensor.get("odsRear");
            gyro = hardwareMap.gyroSensor.get("gyro");
        }
        catch (Exception e) {

        }

        try {
            pusherLeft = hardwareMap.servo.get("pusherLeft");
            pusherRight = hardwareMap.servo.get("pusherRight");
            climberSwinger = hardwareMap.servo.get("climberSwinger");
            climberDropper = hardwareMap.servo.get("climberDropper");
        }
        catch (Exception e) {

        }

        odsFront.enableLed(true);
        odsRear.enableLed(true);

    }

    public double getOdsFrontValue() {
        return odsFront.getLightDetectedRaw();
    }

    public double getOdsRearValue() {
        return odsRear.getLightDetectedRaw();
    }

    public double getAngle() {
        return gyro.getHeading();
    }

    boolean a_warning_generated () {
        return v_warning_generated;
    }

    String a_warning_message () {
        return v_warning_message;
    }

    void m_warning_message (String p_exception_message) {
        if (v_warning_generated) {
            v_warning_message += ", ";
        }
        v_warning_generated = true;
        v_warning_message += p_exception_message;

    }

    @Override public void start () {}


    @Override public void loop () {}

    @Override public void stop (){}

    float scale_motor_power (float p_power) {
        float l_scale = 0.0f;

        float l_power = Range.clip (p_power, -1, 1);

        float[] l_array =
            { 0.00f, 0.05f, 0.09f, 0.10f, 0.12f
            , 0.15f, 0.18f, 0.24f, 0.30f, 0.36f
            , 0.43f, 0.50f, 0.60f, 0.72f, 0.85f
            , 1.00f, 1.00f
            };


        int l_index = (int)(l_power * 16.0);
        if (l_index < 0)
        {
            l_index = -l_index;
        }
        else if (l_index > 16)
        {
            l_index = 16;
        }

        if (l_power < 0)
        {
            l_scale = -l_array[l_index];
        }
        else
        {
            l_scale = l_array[l_index];
        }

        return l_scale;

    }
    double a_left_drive_power ()
    {
        double l_return = 0.0;

        if (motorLeftFront != null)
        {
            l_return = motorLeftFront.getPower ();
        }

        return l_return;

    }
    double a_right_drive_power ()
    {
        double l_return = 0.0;

        if (motorRightFront != null)
        {
            l_return = motorRightFront.getPower ();
        }

        return l_return;

    }
    void set_drive_power (double p_left_power, double p_right_power)

    {

            motorLeftFront.setPower(p_left_power);
            motorLeftRear.setPower(p_left_power);
            motorRightFront.setPower(p_right_power);
            motorRightRear.setPower(p_right_power);

    }
    public void run_using_left_drive_encoder ()

    {
        if (motorLeftFront != null)
        {
            motorLeftFront.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
        if (motorLeftRear != null)
        {
            motorLeftRear.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }

    }

    public void run_using_right_drive_encoder ()

    {
        if (motorRightFront != null)
        {
            motorRightFront.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
        if (motorRightRear != null)
        {
            motorRightRear.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }

    }
    public void run_using_encoders () {
        run_using_left_drive_encoder ();
        run_using_right_drive_encoder ();

    }
    public void run_without_left_drive_encoder ()

    {
        if (motorLeftFront != null) {
            if (motorLeftFront.getMode () == DcMotorController.RunMode.RESET_ENCODERS) {
                motorLeftFront.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            }
        }
        if (motorLeftRear != null) {
            if (motorLeftRear.getMode () == DcMotorController.RunMode.RESET_ENCODERS) {
                motorLeftRear.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            }
        }

    }

    public void run_without_right_drive_encoder ()

    {
        if (motorRightFront != null) {
            if (motorRightFront.getMode () == DcMotorController.RunMode.RESET_ENCODERS) {
                motorRightFront.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            }
        }
        if (motorRightRear != null) {
            if (motorRightRear.getMode () == DcMotorController.RunMode.RESET_ENCODERS) {
                motorRightRear.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            }
        }

    }
    public void run_without_drive_encoders () {
        run_without_left_drive_encoder ();
        run_without_right_drive_encoder ();
    }

    public void reset_left_drive_encoder () {
        if (motorLeftFront != null) {
            motorLeftFront.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
        if (motorLeftRear != null) {
            motorLeftRear.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }

    }

    public void reset_right_drive_encoder () {
        if (motorRightFront != null) {
            motorRightFront.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
        if (motorRightRear != null) {
            motorRightRear.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }

    }

    public void reset_drive_encoders () {
        reset_left_drive_encoder ();
        reset_right_drive_encoder ();
    }

    int a_left_encoder_count () {
        int l_return = 0;

        if (motorLeftFront != null)
        {
            l_return = motorLeftFront.getCurrentPosition ();
        }

        return l_return;

    }
    int a_right_encoder_count ()

    {
        int l_return = 0;

        if (motorRightFront != null)
        {
            l_return = motorRightFront.getCurrentPosition ();
        }

        return l_return;

    }
    boolean has_left_drive_encoder_reached (double p_count)

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        if (motorLeftFront != null)
        {
            //
            // Has the encoder reached the specified values?
            //
            // TODO Implement stall code using these variables.
            //
            if (Math.abs (motorLeftFront.getCurrentPosition ()) > p_count)
            {
                //
                // Set the status to a positive indication.
                //
                l_return = true;
            }
        }

        //
        // Return the status.
        //
        return l_return;

    }
    boolean has_right_drive_encoder_reached (double p_count)

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        if (motorRightFront != null)
        {
            //
            // Have the encoders reached the specified values?
            //
            // TODO Implement stall code using these variables.
            //
            if (Math.abs (motorRightFront.getCurrentPosition ()) > p_count)
            {
                //
                // Set the status to a positive indication.
                //
                l_return = true;
            }
        }

        //
        // Return the status.
        //
        return l_return;

    }
    boolean have_drive_encoders_reached(double p_left_count, double p_right_count) {

        boolean l_return = false;

        if (has_left_drive_encoder_reached (p_left_count) && has_right_drive_encoder_reached (p_right_count)) {
            l_return = true;
        }

        return l_return;

    }
    boolean drive_using_encoders( double p_left_power, double p_right_power, double p_left_count, double p_right_count) {

        boolean l_return = false;


        run_using_encoders ();

        //
        // Start the drive wheel motors at full power.
        //
        set_drive_power (p_left_power, p_right_power);

        //
        // Have the motor shafts turned the required amount?
        //
        // If they haven't, then the op-mode remains in this state (i.e this
        // block will be executed the next time this method is called).
        //
        if (have_drive_encoders_reached (p_left_count, p_right_count))
        {
            //
            // Reset the encoders to ensure they are at a known good value.
            //
            reset_drive_encoders ();

            //
            // Stop the motors.
            //
            set_drive_power (0.0f, 0.0f);

            //
            // Transition to the next state when this method is called
            // again.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // drive_using_encoders

    //--------------------------------------------------------------------------
    //
    // has_left_drive_encoder_reset
    //
    /**
     * Indicate whether the left drive encoder has been completely reset.
     */
    boolean has_left_drive_encoder_reset ()
    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Has the left encoder reached zero?
        //
        if (a_left_encoder_count () == 0)
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_left_drive_encoder_reset

    //--------------------------------------------------------------------------
    //
    // has_right_drive_encoder_reset
    //
    /**
     * Indicate whether the left drive encoder has been completely reset.
     */
    boolean has_right_drive_encoder_reset ()
    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Has the right encoder reached zero?
        //
        if (a_right_encoder_count () == 0)
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_right_drive_encoder_reset

    //--------------------------------------------------------------------------
    //
    // have_drive_encoders_reset
    //
    /**
     * Indicate whether the encoders have been completely reset.
     */
    boolean have_drive_encoders_reset ()
    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Have the encoders reached zero?
        //
        if (has_left_drive_encoder_reset () && has_right_drive_encoder_reset ())
        {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // have_drive_encoders_reset

    //--------------------------------------------------------------------------
    //
    // v_warning_generated
    //
    /**
     * Indicate whether a message is a available to the class user.
     */
    private boolean v_warning_generated = false;

    //--------------------------------------------------------------------------
    //
    // v_warning_message
    //
    /**
     * Store a message to the user if one has been generated.
     */
    private String v_warning_message;

    //--------------------------------------------------------------------------
    //
    // motorLeftFront
    //
    /**
     * Manage the aspects of the left drive motor.
     */
    private DcMotor motorLeftFront;
    private DcMotor motorLeftRear;

    //--------------------------------------------------------------------------
    //
    // motorRightFront
    //
    /**
     * Manage the aspects of the right drive motor.
     */
    private DcMotor motorRightFront;
    private DcMotor motorRightRear;

    private OpticalDistanceSensor odsFront;
    private OpticalDistanceSensor odsRear;

    private GyroSensor gyro;

    private ColorSensor colorSensorLeft;
    private ColorSensor colorSensorRight;

    private Servo pusherLeft;
    private Servo pusherRight;

    private Servo climberSwinger;
    private Servo climberDropper;

}