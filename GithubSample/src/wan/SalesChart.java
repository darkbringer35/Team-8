package wan;




import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

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
    public void SalesChart() {
 
    }

    public JFreeChart getChart(int index, String nDay) {
        // ������ ����
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset(); // line chart 1

        // ������ �Է� ( ��, ����, ī�װ� )
        //���⿡ ((�ش� ���� �����), "�����", (�ش� ��¥)) 
        
        String day = nDay;
		int[] arrayPrice = new int[index];
		String[] arrayDay = new String[index];
		DAOManager dao = new DAOManager();
		for (int i = 0; i < index; i++) {
			String[] oneDay = day.split("-");

			// 1��1�� �϶�
			if (oneDay[1].equals("1") && oneDay[2].equals("1")) {
				oneDay[0] = String.valueOf(Integer.parseInt(oneDay[0]) - 1);
				oneDay[1] = "12";
				oneDay[2] = "31";
				day = oneDay[0] + "-" + oneDay[1] + "-" + oneDay[2];

			}
			// 2,4,6,8,9,11�� 1�� �϶�
			else if (oneDay[1].equals("2") && oneDay[2].equals("1") || oneDay[1].equals("4") && oneDay[2].equals("1")
					|| oneDay[1].equals("6") && oneDay[2].equals("1") || oneDay[1].equals("8") && oneDay[2].equals("1")
					|| oneDay[1].equals("9") && oneDay[2].equals("1")
					|| oneDay[1].equals("11") && oneDay[2].equals("1")) {
				oneDay[1] = String.valueOf(Integer.parseInt(oneDay[1]) - 1);
				oneDay[2] = "31";
				day = oneDay[0] + "-" + oneDay[1] + "-" + String.valueOf(Integer.parseInt(oneDay[2]) - 1);
			}
			// 5,7,10,12�� 1�� �϶�
			else if (oneDay[1].equals("5") && oneDay[2].equals("1") || oneDay[1].equals("7") && oneDay[2].equals("1")
					|| oneDay[1].equals("10") && oneDay[2].equals("1")
					|| oneDay[1].equals("12") && oneDay[2].equals("1")) {
				oneDay[1] = String.valueOf(Integer.parseInt(oneDay[1]) - 1);
				oneDay[2] = "30";
				day = oneDay[0] + "-" + oneDay[1] + "-" + String.valueOf(Integer.parseInt(oneDay[2]) - 1);
			}
			// 3�� 1�� �϶�
			else if (oneDay[1].equals("3") && oneDay[2].equals("1")) {
				if (Integer.parseInt(oneDay[0]) % 4 == 0 && (Integer.parseInt(oneDay[0]) % 100) != 0 // �����϶�
						&& (Integer.parseInt(oneDay[0]) % 400) == 0) {
					oneDay[1] = String.valueOf(Integer.parseInt(oneDay[1]) - 1);
					oneDay[2] = "29";
				} else if (Integer.parseInt(oneDay[0]) % 4 == 0 && (Integer.parseInt(oneDay[0]) % 100) != 0 // ������ �ƴҶ�
						&& (Integer.parseInt(oneDay[0]) % 400) != 0) {
					oneDay[1] = String.valueOf(Integer.parseInt(oneDay[1]) - 1);
					oneDay[2] = "28";
				}
				day = oneDay[0] + "-" + oneDay[1] + "-" + String.valueOf(Integer.parseInt(oneDay[2]) - 1);
			}

			else {
				day = oneDay[0] + "-" + oneDay[1] + "-" + String.valueOf(Integer.parseInt(oneDay[2]) - 1);
			}

			if (dao.getTotalDay(day)!= null) {
				
				
				arrayPrice[index-i-1] = dao.getOneTotalPrice(day);
				arrayDay[index-i-1] = dao.getTotalDay(day);
			}
			else {
				JDialog alert = new JDialog();
				JButton okBtn = new JButton();
				JLabel msg = new JLabel("�ش糯¥�� ������ �����ϴ�!!",JLabel.CENTER);
				
				alert.setLayout(new FlowLayout());
				alert.setSize(140,90);
				alert.setLocation(50,50);
				
				okBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						alert.setVisible(false);
					}
				});
				
				alert.add(msg);
				alert.add(okBtn);
			}
			
			
		}
		for(int i=0;i<index;i++) {
			dataset2.addValue(arrayPrice[i], "�����", arrayDay[i]);		
		}
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
