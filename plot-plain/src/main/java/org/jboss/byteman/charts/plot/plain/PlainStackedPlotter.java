package org.jboss.byteman.charts.plot.plain;

import org.jboss.byteman.charts.data.DataRecord;
import org.jboss.byteman.charts.filter.ChartFilter;
import org.jboss.byteman.charts.plot.Plotter;
import org.jboss.byteman.charts.ui.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static org.jboss.byteman.charts.utils.StringUtils.EMPTY_STRING;

/**
 * User: alexkasko
 * Date: 9/26/15
 */
public class PlainStackedPlotter {

//    public static final Gson GSON = new Gson();
//
//    private Config cf;
//
//    @Override
//    public Collection<? extends ChartConfigEntry<?>> availableConfig() {
//        return cf.toEntries();
//    }
//
//    @Override
//    public PlainStackedPlotter applyConfig(Map<String, ChartConfigEntry<?>> entries) {
//        // todo
//        return this;
//    }
//
//    @Override
//    public JFreeChart createPlot(Iterator<DataRecord> data, Collection<? extends ChartFilter> filters) {
//        return ChartFactory.createStackedBarChart(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, new DefaultCategoryDataset(), PlotOrientation.VERTICAL, false, true, false);
//    }

    private static class Config {
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

          String valueAttributeName = "value";
          String categoryAttributeName = "category";
          int step = 1000;

          Collection<? extends ChartConfigEntry<?>> toEntries() {
              return Arrays.asList(
                      // todo: name vs label
                      new StringConfigEntry("valueAttributeName", valueAttributeName),
                      new StringConfigEntry("categoryAttributeName", categoryAttributeName),
                      new IntConfigEntry("step", step, 10, Integer.MAX_VALUE, 1),

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
      }
}
