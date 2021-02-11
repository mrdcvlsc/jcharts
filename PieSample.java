import java.util.*;
import javax.swing.*;
import jcharts.*;

public class PieSample{
	public static void main(String[] args){
		int width = 400;
        int height = 600;	

    	// create your pie
        JpieCharts pie = new JpieCharts(
            new int[]{width,height},
            new String[]{"intel","amd","nvidia","arm","risc","cisc"}, // labels
            new int[]{180,90,90,23,10,30} // data
        );

        // a JPanel container
        JPanel p = new JPanel();
        p.setSize(width,height);

        // add the pie chart to the container JPanel
        p.add(pie);

        JFrame f = new JFrame();
        f.add(p);
        f.pack();
        f.setSize(width,height);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // you can also add the chart directly to frame if you want
	}
}