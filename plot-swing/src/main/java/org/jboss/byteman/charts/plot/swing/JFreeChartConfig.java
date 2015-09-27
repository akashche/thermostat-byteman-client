package org.jboss.byteman.charts.plot.swing;

import org.jboss.byteman.charts.plot.PlotConfig;
import org.jboss.byteman.charts.ui.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * User: alexkasko
 * Date: 9/27/15
 */
public class JFreeChartConfig implements PlotConfig {
    String valueAttributeName = "value";
    String categoryAttributeName = "category";

    private int maxRecords = 24;
    private boolean ignoreAbsentCategory = true;
    private boolean ignoreAbsentValue = true;
    private boolean ignoreInvalidValue = true;

    String rangeAxisLabel = "Number of entries";
    String domainAxisLabel = "Elapsed time intervals";
    boolean domainGridlinesVisible = true;
    double domainAxisLowerMargin = 0.01d;
    double domainAxisUpperMargin = 0.01d;
    double domainAxisLabelAngle = 0.12d;
    double rendered3dXOffset = 16.0d;
    double rendered3dYOffset = 12.0d;
    boolean baseItemLabelsVisible = false;
    boolean shadowVisible = false;
    double itemMargin = 0.0d;
    boolean outlineVisible = false;

    String backgroundPaint = "#FFFFFFFF";
    double backgroundImageAlpha = 0.0d;
    String rangeGridlinePaint = "#FFAAAAAA";
    String seriesPaint0 = "#BB669900";
    String seriesPaint1 = "#BBFF8800";
    String wallPaint = "#FFEEEEEE";
    String axisLinePaint = "#FFAAAAAA";
    String tickMarkPaint = "#FFAAAAAA";
    String tickLabelPaint = "#FF222222";


    @Override
    public String getValueAttributeName() {
        return valueAttributeName;
    }

    @Override
    public String getCategoryAttributeName() {
        return categoryAttributeName;
    }

    @Override
    public int getMaxRecords() {
        return maxRecords;
    }

    @Override
    public boolean isIgnoreAbsentCategory() {
        return ignoreAbsentCategory;
    }

    @Override
    public boolean isIgnoreAbsentValue() {
        return ignoreAbsentValue;
    }

    @Override
    public boolean isIgnoreInvalidValue() {
        return ignoreInvalidValue;
    }

    Collection<? extends ChartConfigEntry<?>> toEntries() {
        return Arrays.asList(
                // todo: name vs label
                new StringConfigEntry("valueAttributeName", valueAttributeName),
                new StringConfigEntry("categoryAttributeName", categoryAttributeName),

                new StringConfigEntry("rangeAxisLabel", rangeAxisLabel),
                new StringConfigEntry("domainAxisLabel", domainAxisLabel),
                new BoolConfigEntry("domainGridlinesVisible", domainGridlinesVisible),
                new DoubleConfigEntry("domainAxisLowerMargin", domainAxisLowerMargin, 0, 1, 0.1),
                new DoubleConfigEntry("domainAxisUpperMargin", domainAxisUpperMargin, 0, 1, 0.1),
                new DoubleConfigEntry("domainAxisLabelAngle", domainAxisLabelAngle, 0, 1, 0.1),
                new DoubleConfigEntry("rendered3dXOffset", rendered3dXOffset, 0, 100, 0.1),
                new DoubleConfigEntry("rendered3dYOffset", rendered3dYOffset, 0, 100, 0.1),
                new BoolConfigEntry("baseItemLabelsVisible", baseItemLabelsVisible),
                new BoolConfigEntry("shadowVisible", shadowVisible),
                new DoubleConfigEntry("itemMargin", itemMargin, 0, 100, 0.1),
                new BoolConfigEntry("outlineVisible", outlineVisible),
                new StringConfigEntry("backgroundPaint", backgroundPaint),
                new DoubleConfigEntry("backgroundImageAlpha", backgroundImageAlpha, 0, 1, 0.1),
                new StringConfigEntry("rangeGridlinePaint", rangeGridlinePaint),
                new StringConfigEntry("seriesPaint0", seriesPaint0),
                new StringConfigEntry("seriesPaint1", seriesPaint1),
                new StringConfigEntry("wallPaint", wallPaint),
                new StringConfigEntry("axisLinePaint", axisLinePaint),
                new StringConfigEntry("tickMarkPaint", tickMarkPaint),
                new StringConfigEntry("tickLabelPaint", tickLabelPaint)
        );
    }

    public void applyConfig(Map<String, ChartConfigEntry<?>> entries) {
        // todo
    }

    // data filters:
//    timestamp from
//    timestamp to
//    string category regex
//    double value from
//    double value to
//    string attribute name:regex format


}
