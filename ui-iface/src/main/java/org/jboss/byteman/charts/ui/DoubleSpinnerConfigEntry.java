package org.jboss.byteman.charts.ui;

/**
 * User: alexkasko
 * Date: 6/3/15
 */
public class DoubleSpinnerConfigEntry extends ConfigEntryBase<Double> {

    protected double defaultValue;
    protected double minValue;
    protected double maxValue;
    protected double step;

    public DoubleSpinnerConfigEntry() {
    }

    public DoubleSpinnerConfigEntry(String label, double defaultValue, double minValue, double maxValue, double step) {
        super("org.jboss.byteman.charts.ui.swing.DoubleSpinnerControl", label);
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;
    }

    public double getDefaultValue() {
        return defaultValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getStep() {
        return step;
    }
}
