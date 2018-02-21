package ru.isu.compmodels.imitation;

import javafx.scene.chart.Chart;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Show implements Runnable {

    CategoryChart chart;
    Boolean shouldStop=false;
    ArrayList<Number> fff=new ArrayList<Number>((Arrays.asList(new Number[]{0,0,0})));

    ArrayList<Balancer> list;
    public Show(){

        list=new ArrayList<Balancer>();
    }

    public void setBalancer(ArrayList<Balancer> balancer){
        list=balancer;
    }


    @Override
    public void run() {
        chart = getChart();

        SwingWrapper<CategoryChart> hello = new SwingWrapper<CategoryChart>(chart);
        hello.displayChart();
        while(!shouldStop) {

            updateChart();
            hello.repaintChart();


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateChart(){
        chart.updateCategorySeries("server1",
                new ArrayList<String>(Arrays.asList(new String[]{"balancer1", "balancer2", "balancer3"})),
                new ArrayList<Number>(Arrays.asList(new Number[]{
                        list.get(0).getServerPool().get(0).getCurrentLoad(),
                        list.get(0).getServerPool().get(1).getCurrentLoad(),
                        list.get(0).getServerPool().get(2).getCurrentLoad()})), fff );

        chart.updateCategorySeries("server2",
                new ArrayList<String>(Arrays.asList(new String[] { "balancer1", "balancer2", "balancer3"})),
                new ArrayList<Number>(Arrays.asList(new Number[] {
                        list.get(1).getServerPool().get(0).getCurrentLoad(),
                        list.get(1).getServerPool().get(1).getCurrentLoad(),
                        list.get(1).getServerPool().get(2).getCurrentLoad() })),fff);
        chart.updateCategorySeries("server3",
                new ArrayList<String>(Arrays.asList(new String[] { "balancer1", "balancer2", "balancer3"})),
                new ArrayList<Number>(Arrays.asList(new Number[] {
                        list.get(2).getServerPool().get(0).getCurrentLoad(),
                        list.get(2).getServerPool().get(1).getCurrentLoad(),
                        list.get(2).getServerPool().get(2).getCurrentLoad() })),fff);

    }

    public CategoryChart getChart() {

        // Create Chart
        CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Temperature vs. Color").xAxisTitle("Time").yAxisTitle("loading").theme(Styler.ChartTheme.GGPlot2).build();

        // Customize Chart

        // Series
        chart.addSeries("server1",
                new ArrayList<String>(Arrays.asList(new String[] { "balancer1", "balancer2", "balancer3"})),
                new ArrayList<Number>(Arrays.asList(new Number[] {
                        list.get(0).getServerPool().get(0).getCurrentLoad(),
                        list.get(1).getServerPool().get(0).getCurrentLoad(),
                        list.get(2).getServerPool().get(0).getCurrentLoad() })));
        chart.addSeries("server2",
                new ArrayList<String>(Arrays.asList(new String[] {"balancer1", "balancer2", "balancer3"})),
                new ArrayList<Number>(Arrays.asList(new Number[] {
                        list.get(0).getServerPool().get(1).getCurrentLoad(),
                        list.get(1).getServerPool().get(1).getCurrentLoad(),
                        list.get(2).getServerPool().get(1).getCurrentLoad() })));
        chart.addSeries("server3",
                new ArrayList<String>(Arrays.asList(new String[] { "balancer1", "balancer2", "balancer3"})),
                new ArrayList<Number>(Arrays.asList(new Number[] {
                        list.get(0).getServerPool().get(2).getCurrentLoad(),
                        list.get(1).getServerPool().get(2).getCurrentLoad(),
                        list.get(2).getServerPool().get(2).getCurrentLoad() })));

        return chart;
    }

    public void shutDown() {
        shouldStop = true;
        //Будим, если он уснул по take на пустой очереди
        Thread.currentThread().interrupt();
    }
}
