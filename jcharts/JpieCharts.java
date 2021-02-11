package jcharts;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.geom.Arc2D;

public class JpieCharts extends JPanel
{
    
    public class MyGraphics extends JComponent
    {
        private static final long serialVersionUID = 1L;

        private String[] legends;
        private int[] values;
        private double[] percentage;

        private int quantity;
        private int total;
        
        private int panelWidth;
        private int panelHeight;

        private int circleDiameter;
        private int margin;

        private int legendCnt;

        MyGraphics(int[] dimensions, String[] legends, int[] values)
        {
            // assigne values to members
            panelWidth  = dimensions[0];
            panelHeight = dimensions[1];
            this.legends = legends;
            this.values = values;
            quantity = values.length;

            circleDiameter = (int)Math.round((double)panelWidth*0.80);
            margin = (int)Math.round(((double)panelWidth-(double)circleDiameter)/2.0);
            
            total = 0;
            for(int i=0; i<quantity; ++i){
                total+=values[i];
            }

            percentage = new double[quantity];
            for(int i=0; i<quantity; ++i){
                percentage[i] = (double)values[i]/(double)total;
            }

            setPreferredSize(new Dimension(panelWidth,panelHeight));
        }

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g;

            // set font weight to bold
            g2d.setFont(new Font("SansSerif", Font.BOLD, 11));

            // set background to white
            g2d.setColor(new Color(255,255,255)); // white
            g2d.fillRect(0,0,panelWidth,panelHeight);

            g2d.drawLine(0,0,panelWidth,panelHeight);

            int randomAngle = rand(0,360);
            int prevDeg = 0;

            int legendsXPos = margin;
            int legendsYPos = (margin*2)+circleDiameter;

            for(int i=0; i<quantity; ++i)
            {
                int currentArea = (int)Math.round(360.0*percentage[i]);
                g2d.setColor(new Color(rand(0,225),rand(0,225),rand(0,225)));
                g2d.fillArc(margin,(int)Math.round((double)margin/1.5),circleDiameter,circleDiameter,
                    prevDeg+randomAngle,currentArea
                );
                prevDeg = prevDeg+currentArea;
                String label = legends[i]+" ("+values[i]+") - "+Math.round(percentage[i]*100.0)+"%";
                g2d.drawString(label,legendsXPos,legendsYPos+(12*i));
            }

            int innerDiameter = (int)Math.round((double)circleDiameter*0.25);
            int innerMargin = (int)Math.round((double)(circleDiameter-innerDiameter)/2.0);

            g2d.setColor(new Color(255,255,255)); // inner circle color to white
            g2d.fillOval(margin+innerMargin,(int)Math.round((double)margin/1.5)+innerMargin,innerDiameter,innerDiameter);
        }

        private int rand(int min, int max)
        {
            if (min > max || (max - min + 1 > Integer.MAX_VALUE)) {
                throw new IllegalArgumentException("Invalid range");
            }
            return new Random().nextInt(max - min + 1) + min;
        }
    }

    public JpieCharts(int[] dimensions, String[] legends, int[] values)
    {
        this.add(new MyGraphics(dimensions,legends,values));
        this.setSize(dimensions[0],dimensions[1]);
    }
}