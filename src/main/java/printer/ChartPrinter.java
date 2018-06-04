package printer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.util.Map;

public class ChartPrinter {
    public static JFreeChart createPieChart(Map<String, Double> map, String title) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        return ChartFactory.createPieChart(title, dataset, true, true, false);
    }

    public static JFreeChart createBarChart(Map<String, Double> map, String title, String columnName) {
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
