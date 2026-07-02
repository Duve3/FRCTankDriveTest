package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.*;

public class Move extends Command {
    private Drivebase driveSubsystem;

    private double leftPower;
    private double rightPower;

    private Supplier<Double> leftYSupplier;
    private Supplier<Double> rightXSupplier;

    public Move(Drivebase driveSubsystem, Supplier<Double> leftYSupplier, Supplier<Double> rightXSupplier) {
        this.driveSubsystem = driveSubsystem;

        this.leftYSupplier = leftYSupplier;
        this.rightXSupplier = rightXSupplier;
        addRequirements(driveSubsystem);
    }

    private void calculatePower() {
        leftPower = leftYSupplier.get() - rightXSupplier.get();
        rightPower = leftYSupplier.get() + rightXSupplier.get();

        SmartDashboard.putNumber("leftPower", leftPower);
        SmartDashboard.putNumber("rightPower", rightPower);

        // lower values bc im scared yah yah
        leftPower /= 10;
        rightPower /= 10;
    }

    @Override
    public void execute() {
        calculatePower();
        driveSubsystem.move(leftPower, rightPower);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
