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
       // Run As > Java Application 으로 실행하면 바로 확인할 수 있음.
    public static void main(final String[] args) {
    	//SalesChart 객체 생성
    	SalesChart demo = new SalesChart();
    	//JFeeChart 객체 생성
    	JFreeChart chart = demo.getChart();
        //차트 프레임에 붙여넣기
    	ChartFrame frame1=new ChartFrame("매출 분석 차트",chart);
        frame1.setSize(800,400); 
        frame1.setVisible(true);
    }

    public JFreeChart getChart() {
        // 데이터 생성
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset(); // line chart 1

        // 데이터 입력 ( 값, 범례, 카테고리 )
        //여기에 ((해당 날의 매출액), "매출액", (해당 날짜)) DB에서 가져와서 업데이트 해주심 될 것 같아요!
        dataset2.addValue(9.0, "매출액", "1일");
        dataset2.addValue(7.0, "매출액", "2일");
        dataset2.addValue(2.0, "매출액", "3일");
        dataset2.addValue(6.0, "매출액", "4일");
        dataset2.addValue(6.0, "매출액", "5일");
        dataset2.addValue(9.0, "매출액", "6일");
        dataset2.addValue(5.0, "매출액", "7일");
        dataset2.addValue(4.0, "매출액", "8일");
        dataset2.addValue(8.0, "매출액", "9일");
        dataset2.addValue(8.0, "매출액", "10일");
        dataset2.addValue(8.0, "매출액", "11일");
        dataset2.addValue(8.0, "매출액", "12일");

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
