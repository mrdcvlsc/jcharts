package jcharts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class JpieCharts extends JPanel
{
    private final MyGraphics myGraphics;  // Add this field

    public JpieCharts(int[] dimensions, String[] legends, int[] values)
    {
        myGraphics = new MyGraphics(dimensions, legends, values);  // Store reference
        this.add(myGraphics);
        this.setSize(dimensions[0], dimensions[1]);
    }

    // Add method to update data
    public void updateData(int[] newValues) {
        myGraphics.updateValues(newValues);
        repaint();  // Trigger repaint of the component
    }

    public class MyGraphics extends JComponent
    {
        private static final long serialVersionUID = 1L;

        private final String[] legends;
        private int[] values;
        private double[] percentage;

        private final int quantity;
        private int total;
        
        private final int panelWidth;
        private final int panelHeight;

        private final int circleDiameter;
        private final int margin;

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

        // Add method to update values
        public void updateValues(int[] newValues) {
            if (newValues.length != quantity) {
                throw new IllegalArgumentException("New values array must be same length as original");
            }
            
            this.values = newValues;
            total = 0;
            for(int i = 0; i < quantity; ++i) {
                total += values[i];
            }

            percentage = new double[quantity];
            for(int i = 0; i < quantity; ++i) {
                percentage[i] = total > 0 ? (double)values[i]/(double)total : 0;
            }
        }
    }
}