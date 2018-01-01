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
       // Run As > Java Application 으로 실행하면 바로 확인할 수 있음.
    public void SalesChart() {
 
    }

    public JFreeChart getChart(int index, String nDay) {
        // 데이터 생성
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset(); // line chart 1

        // 데이터 입력 ( 값, 범례, 카테고리 )
        //여기에 ((해당 날의 매출액), "매출액", (해당 날짜)) 
        
        String day = nDay;
		int[] arrayPrice = new int[index];
		String[] arrayDay = new String[index];
		DAOManager dao = new DAOManager();
		for (int i = 0; i < index; i++) {
			String[] oneDay = day.split("-");

			// 1월1일 일때
			if (oneDay[1].equals("1") && oneDay[2].equals("1")) {
				oneDay[0] = String.valueOf(Integer.parseInt(oneDay[0]) - 1);
				oneDay[1] = "12";
				oneDay[2] = "31";
				day = oneDay[0] + "-" + oneDay[1] + "-" + oneDay[2];

			}
			// 2,4,6,8,9,11월 1일 일때
			else if (oneDay[1].equals("2") && oneDay[2].equals("1") || oneDay[1].equals("4") && oneDay[2].equals("1")
					|| oneDay[1].equals("6") && oneDay[2].equals("1") || oneDay[1].equals("8") && oneDay[2].equals("1")
					|| oneDay[1].equals("9") && oneDay[2].equals("1")
					|| oneDay[1].equals("11") && oneDay[2].equals("1")) {
				oneDay[1] = String.valueOf(Integer.parseInt(oneDay[1]) - 1);
				oneDay[2] = "31";
				day = oneDay[0] + "-" + oneDay[1] + "-" + String.valueOf(Integer.parseInt(oneDay[2]) - 1);
			}
			// 5,7,10,12월 1일 일때
			else if (oneDay[1].equals("5") && oneDay[2].equals("1") || oneDay[1].equals("7") && oneDay[2].equals("1")
					|| oneDay[1].equals("10") && oneDay[2].equals("1")
					|| oneDay[1].equals("12") && oneDay[2].equals("1")) {
				oneDay[1] = String.valueOf(Integer.parseInt(oneDay[1]) - 1);
				oneDay[2] = "30";
				day = oneDay[0] + "-" + oneDay[1] + "-" + String.valueOf(Integer.parseInt(oneDay[2]) - 1);
			}
			// 3월 1일 일때
			else if (oneDay[1].equals("3") && oneDay[2].equals("1")) {
				if (Integer.parseInt(oneDay[0]) % 4 == 0 && (Integer.parseInt(oneDay[0]) % 100) != 0 // 윤년일때
						&& (Integer.parseInt(oneDay[0]) % 400) == 0) {
					oneDay[1] = String.valueOf(Integer.parseInt(oneDay[1]) - 1);
					oneDay[2] = "29";
				} else if (Integer.parseInt(oneDay[0]) % 4 == 0 && (Integer.parseInt(oneDay[0]) % 100) != 0 // 윤년이 아닐때
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
				JLabel msg = new JLabel("해당날짜의 정보가 없습니다!!",JLabel.CENTER);
				
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
			dataset2.addValue(arrayPrice[i], "매출액", arrayDay[i]);		
		}
        // 렌더링 생성 및 세팅

        // 렌더링 생성
        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();

        // 공통 옵션 정의
        final CategoryItemLabelGenerator generator = new StandardCategoryItemLabelGenerator();
        final ItemLabelPosition p_center = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER);
        final ItemLabelPosition p_below = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE6, TextAnchor.TOP_LEFT );
        Font f = new Font("Gulim", Font.BOLD, 14);
        Font axisF = new Font("Gulim", Font.PLAIN, 14);

       
        // 렌더링 세팅
        // 그래프 3       
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

        // plot 생성
        final CategoryPlot plot = new CategoryPlot();

        // plot 에 데이터 적재
        plot.setDataset(2, dataset2);
        plot.setRenderer(2, renderer2);

        // plot 기본 설정
        plot.setOrientation(PlotOrientation.VERTICAL);             // 그래프 표시 방향
        plot.setRangeGridlinesVisible(true);                       // X축 가이드 라인 표시여부
        plot.setDomainGridlinesVisible(true);                      // Y축 가이드 라인 표시여부

        
        // 렌더링 순서 정의 : dataset 등록 순서대로 렌더링 ( 즉, 먼저 등록한게 아래로 깔림 )
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

 
        // X축 세팅
        plot.setDomainAxis(new CategoryAxis());              // X축 종류 설정
        plot.getDomainAxis().setTickLabelFont(axisF); // X축 눈금라벨 폰트 조정
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.STANDARD);       // 카테고리 라벨 위치 조정
 
        // Y축 세팅
        plot.setRangeAxis(new NumberAxis());                 // Y축 종류 설정
        plot.getRangeAxis().setTickLabelFont(axisF);  // Y축 눈금라벨 폰트 조정

        // 세팅된 plot을 바탕으로 chart 생성
        final JFreeChart chart = new JFreeChart(plot);

        return chart;
    }
}
