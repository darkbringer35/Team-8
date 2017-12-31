package wan;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.StandardGradientPaintTransformer;
import org.jfree.ui.TextAnchor;


public class SalesChart {
       // Run As > Java Application ���� �����ϸ� �ٷ� Ȯ���� �� ����.
    public static void main(final String[] args) {
    	//SalesChart ��ü ����
    	SalesChart demo = new SalesChart();
    	//JFeeChart ��ü ����
    	JFreeChart chart = demo.getChart();
        //��Ʈ �����ӿ� �ٿ��ֱ�
    	ChartFrame frame1=new ChartFrame("���� �м� ��Ʈ",chart);
        frame1.setSize(800,400); 
        frame1.setVisible(true);
    }

    public JFreeChart getChart() {
        // ������ ����
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset(); // line chart 1

        // ������ �Է� ( ��, ����, ī�װ� )
        //���⿡ ((�ش� ���� �����), "�����", (�ش� ��¥)) DB���� �����ͼ� ������Ʈ ���ֽ� �� �� ���ƿ�!
        dataset2.addValue(9.0, "�����", "1��");
        dataset2.addValue(7.0, "�����", "2��");
        dataset2.addValue(2.0, "�����", "3��");
        dataset2.addValue(6.0, "�����", "4��");
        dataset2.addValue(6.0, "�����", "5��");
        dataset2.addValue(9.0, "�����", "6��");
        dataset2.addValue(5.0, "�����", "7��");
        dataset2.addValue(4.0, "�����", "8��");
        dataset2.addValue(8.0, "�����", "9��");
        dataset2.addValue(8.0, "�����", "10��");
        dataset2.addValue(8.0, "�����", "11��");
        dataset2.addValue(8.0, "�����", "12��");

        // ������ ���� �� ����

        // ������ ����
        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();

        // ���� �ɼ� ����
        final CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();
        final ItemLabelPosition p_center = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER);
        final ItemLabelPosition p_below = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE6, TextAnchor.TOP_LEFT );
        Font f = new Font("Gulim", Font.BOLD, 14);
        Font axisF = new Font("Gulim", Font.PLAIN, 14);

       
        // ������ ����
        // �׷��� 3       
        renderer2.setBaseItemLabelGenerator(generator);
        renderer2.setBaseItemLabelsVisible(true);
        renderer2.setBaseShapesVisible(true);
        renderer2.setDrawOutlines(true);
        renderer2.setUseFillPaint(true);
        renderer2.setBaseFillPaint(Color.WHITE);
        renderer2.setBaseItemLabelFont(f);
        renderer2.setBasePositiveItemLabelPosition(p_below);
        renderer2.setSeriesPaint(0,new Color(219,121,22));
        renderer2.setSeriesStroke(0,new BasicStroke( 2.0f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,3.0f));

        // plot ����
        final CategoryPlot plot = new CategoryPlot();

        // plot �� ������ ����
        plot.setDataset(2, dataset2);
        plot.setRenderer(2, renderer2);

        // plot �⺻ ����
        plot.setOrientation(PlotOrientation.VERTICAL);             // �׷��� ǥ�� ����
        plot.setRangeGridlinesVisible(true);                       // X�� ���̵� ���� ǥ�ÿ���
        plot.setDomainGridlinesVisible(true);                      // Y�� ���̵� ���� ǥ�ÿ���

        
        // ������ ���� ���� : dataset ��� ������� ������ ( ��, ���� ����Ѱ� �Ʒ��� �� )
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

 
        // X�� ����
        plot.setDomainAxis(new CategoryAxis());              // X�� ���� ����
        plot.getDomainAxis().setTickLabelFont(axisF); // X�� ���ݶ� ��Ʈ ����
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.STANDARD);       // ī�װ� �� ��ġ ����
 
        // Y�� ����
        plot.setRangeAxis(new NumberAxis());                 // Y�� ���� ����
        plot.getRangeAxis().setTickLabelFont(axisF);  // Y�� ���ݶ� ��Ʈ ����

        // ���õ� plot�� �������� chart ����
        final JFreeChart chart = new JFreeChart(plot);

        return chart;
    }
}
