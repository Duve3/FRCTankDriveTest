// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;


public class Drivebase extends SubsystemBase {
  
  // The 4 motors of the base
  SparkMax frontLeftMotor = new SparkMax(1, MotorType.kBrushless);
  SparkMax backLeftMotor = new SparkMax(2, MotorType.kBrushless);
  SparkMax frontRightMotor = new SparkMax(3, MotorType.kBrushless);
  SparkMax backRightMotor = new SparkMax(4, MotorType.kBrushless);

  
  // Driver Controller
  CommandXboxController controller = null;
  // Indicates Driver Mode
  double driverMode = 0;
  boolean togglePressed = false;
  // Constants
  double movePower = 0.3;
  double turnPower = 0.3;
  double fastMovePower = 1;
  double fastTurnPower = 0.7;

  /** Creates a new ExampleSubsystem. */
  public Drivebase() {
    // Creates a configuration to apply to motors
    SparkMaxConfig config = new SparkMaxConfig();
    // Sets to brake when robot isn't given power
    config.idleMode(IdleMode.kBrake);

    // sets a current limit (default is 80, that feels quite high, CD recs 60 but im gonna chillax)
    config.smartCurrentLimit(40);

    // Inverts direction
    SparkMaxConfig invertConfig = config;
    invertConfig.inverted(true);
    // Reset mode resets the already saved flash settings to default values, Persist mode saves current settings across cycles
    frontLeftMotor.configure(invertConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    backLeftMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    frontRightMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    backRightMotor.configure(invertConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    // TESTING BRANCH
  }

  // Sets the controller
  public void setController(CommandXboxController driveController) {
    controller = driveController;
  }

  // This method will be called once per scheduler run
  @Override
  public void periodic() {
    if (controller == null) { return; }
    double leftY = controller.getLeftY();
    double rightX = controller.getRightX();
      
      // Calculates the vectors
      double rightSide = leftY + rightX;
      double leftSide = leftY - rightX;

      // Finds max power
      double max = Math.max(Math.abs(rightSide), Math.abs(leftSide));
      // Normalizes speed based on the max (making range 0-1)
      if (max > 1) {
        rightSide /= max;
        leftSide /= max;
      }

      // Sets the power of the motors
      // Makes sure it doesn't clash with auto
      if (!DriverStation.isAutonomous()){
        frontLeftMotor.set(leftSide);
        backLeftMotor.set(leftSide);
        frontRightMotor.set(rightSide);
        backRightMotor.set(rightSide);
      }
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

   /**
   * Moves the robot forward by setting power to both sides.
   * @param power Positive or negative power for the motor
   */
  public void moveForward(double power) {
    frontLeftMotor.set(power);
    backLeftMotor.set(power);
    frontRightMotor.set(power);
    backRightMotor.set(power);
  }

  /**
   * Turns the robot by spinning the motors in opposite directions.
   * @param power Power to turn (positive for clockwise, negative for counterclockwise)
   */
  public void turn(double power) {
    frontLeftMotor.set(power);
    backLeftMotor.set(power);
    frontRightMotor.set(-power);
    backRightMotor.set(-power);
  }

  /*** Stops all the motors.*/
  public void stop() {
    frontLeftMotor.set(0);
    backLeftMotor.set(0);
    frontRightMotor.set(0);
    backRightMotor.set(0);
  }


}