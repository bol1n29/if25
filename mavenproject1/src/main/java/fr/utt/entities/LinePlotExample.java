package fr.utt.entities;

import javax.swing.*;
 
import org.math.plot.*;
 
public class LinePlotExample {
        public static void main(String[] args) {
 
                // define your data
                double[] x = { 0.0,0.0, 0.0, 0.0,0.0,0.0 };
                double[] y = { 0.05,0.0, 0.66, 0.0,0.55,0.0 };
                double[] z = { 0.163,0.2, 0.0, 0.5,0.0,0.16663 };

 
                // create your PlotPanel (you can use it as a JPanel)
                Plot3DPanel plot = new Plot3DPanel();
 
                // define the legend position
                plot.addLegend("SOUTH");
 
                // add a line plot to the PlotPanel
                plot.addScatterPlot("my plot", x, y,z);
                plot.setAxisLabel(0, "Kevin");
 
                // put the PlotPanel in a JFrame like a JPanel
                JFrame frame = new JFrame("a plot panel");
                frame.setSize(600, 600);
                frame.setContentPane(plot);
                frame.setVisible(true);
 
        }
}
