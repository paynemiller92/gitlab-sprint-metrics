package printer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.util.Map;

/**
 * The Printer class that prints {@link JFreeChart} objects from given Data Sets,
 * which are typically {@link Map} objects.
 */

class ChartPrinter {

    /**
     * Produces a Pie Chart with the given title from the given input.
     * @param map The Data Set used to populate the chart.
     * @param title The title of the chart.
     * @return the chart to be printed.
     */

    static JFreeChart createPieChart(Map<String, Double> map, String title) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        return ChartFactory.createPieChart(title, dataset, true, true, false);
    }

    /**
     * Produces a Bar Chart with the given title and column name from the given Data Set.
     * @param map The Data Set used to populate the chart.
     * @param title The title of the chart.
     * @param columnName the column name.
     * @return the chart to be printed.
     */

    static JFreeChart createBarChart(Map<String, Double> map, String title, String columnName) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            dataset.setValue(entry.getValue(), entry.getKey(), "Points");
        }
        return ChartFactory.createBarChart(
                title,
                columnName, "Points",
                dataset,PlotOrientation.VERTICAL,
                true, true, false);
    }
}
