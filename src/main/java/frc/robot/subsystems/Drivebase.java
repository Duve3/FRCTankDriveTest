// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

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

  /** Creates a new Drivebase subsystem, configures motors */
  public Drivebase() {
    // here we are defining configs and making them more and more exact as we define more

    // Creates a configuration to apply to motors
    SparkMaxConfig config = new SparkMaxConfig();
    // Sets to brake when robot isn't given power
    config.idleMode(IdleMode.kBrake);

    // sets a current limit (default is 80, that feels quite high, CD recs 60 but im gonna chillax)
    config.smartCurrentLimit(40);

    // Inverts direction (bc some of them need to be inverted)
    SparkMaxConfig invertConfig = config;
    invertConfig.inverted(true);

    // Reset mode resets the already saved flash settings to default values, Persist mode saves current settings across cycles
    frontLeftMotor.configure(invertConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    backLeftMotor.configure(config.follow(frontLeftMotor), ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    frontRightMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    backRightMotor.configure(invertConfig.follow(frontRightMotor), ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
  }

   /**
   * Moves the robot using the power provided
   * @param leftPower the power to put on left side
   * @param rightPower the power to put on right side
   * @return command
   */
  public void move(double leftPower, double rightPower) {
    frontLeftMotor.set(leftPower);
    frontRightMotor.set(rightPower);
  }

  /**
   * Sets all motor power to 0, stopping them
   * @return command
   */
  public void stop() {
      frontLeftMotor.set(0);
      frontRightMotor.set(0);
  }
}