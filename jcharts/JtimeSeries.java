package jcharts;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class JtimeSeries extends JPanel
{
    private String[] PItemNames;
    private String[] PXLabels;
    private int[][] PValues;

    private int[] PProductTotal;
    private int POverAllTotal;

    public int getProductTotal(int i){
        return PProductTotal[i];
    }

    public int getOverAllTotal(){
        return POverAllTotal;
    }

    public int getValueAt(int i, int j){
        return PValues[i][j];
    }

    public int getProductSize(){
        return PItemNames.length;
    }

    public String getProductName(int i){
        return PItemNames[i];
    }

    public class MyGraphics extends JComponent {

        private int xAdjPlc = 20;
        private int yAdjPlc = 20;
        private int legendWidth = 100;

        private int PadValue = 45;

        private int width;
        private int height;

        private int xinterval;
        private int yinterval;

        private String[] xlabels;
        private String[] ylabels;
        private String[] legends;

        private int graphWidth;
        private int graphHeight;

        private int[][] xyValues;

        private int mini;
        private int maxi;

        private int graphMinVal;
        private int graphMaxVal;

        private int legendPos;
        private int legendCnt;

        private static final long serialVersionUID = 1L;

        MyGraphics(int[] dimensions, int[] adjustmentPlacing, String[] xlabels, String[] legends, int[][] xyValues)
        {
            setPreferredSize(new Dimension(dimensions[0], dimensions[1]));

            legendCnt = 0;
            this.legends = legends;
            // finds the max value
            int tempMax = 0;
            for(int i=0;i<xyValues.length;++i){
                for(int j=0;j<xyValues[i].length; ++j){
                    if(xyValues[i][j] > tempMax){
                        tempMax = xyValues[i][j];
                    }
                }
            }
            maxi = tempMax+(int)Math.round((double)tempMax*0.045);
            
            // finds the lowest
            int tempMin = tempMax;
            for(int i=0;i<xyValues.length;++i){
                for(int j=0;j<xyValues[i].length;++j){
                    if(xyValues[i][j]<tempMin){
                        tempMin = xyValues[i][j];
                    }
                }
            }
            mini = tempMin;

            graphMaxVal = (maxi+PadValue)-(mini-PadValue);
            graphMinVal = 0;
            
            this.xyValues = xyValues;

            xAdjPlc = adjustmentPlacing[0];
            yAdjPlc = adjustmentPlacing[1];

            this.width = dimensions[0];
            this.height = dimensions[1];

            
            this.xlabels = xlabels;

            int startYL = graphMaxVal/10;
            int yValInterval = startYL;
            ylabels = new String[10];
            for(int i=0; i<10; ++i){
                ylabels[i] = Integer.toString(startYL);
                startYL+=yValInterval;                
            }

            legendWidth = adjustmentPlacing[2];
            graphWidth = (dimensions[0] - adjustmentPlacing[0])-legendWidth;
            graphHeight = dimensions[1] - adjustmentPlacing[1];

            yinterval = (int)Math.round((double)graphHeight/10.0);
            xinterval = (int)Math.round((double)graphWidth/(double)xlabels.length);

            legendPos = xAdjPlc+graphWidth+10;
        }

        private int rand(int min, int max)
        {
            if (min > max || (max - min + 1 > Integer.MAX_VALUE)) {
                throw new IllegalArgumentException("Invalid range");
            }
            return new Random().nextInt(max - min + 1) + min;
        }

        private void plotter(Graphics g, int x, int y){
            g.fillOval(xAdjPlc+x,graphHeight-y,2,2);
        }

        private void lineSeries(Graphics g, int[] rgb,int[] x, int[] y)
        {
            
            if(legendCnt==legends.length) legendCnt = 0;
            
            g.setColor(new Color(rgb[0],rgb[1],rgb[2]));
            g.drawString(legends[legendCnt],legendPos,(15*(legendCnt+1)));
            legendCnt++;

            int prevx = x[0]-(xinterval/2);
            int prevy = 0;
            int pointRad = 8;

            int xlength = x.length;
            for(int i=0; i<xlength; ++i)
            {
                g.setColor(new Color(rgb[0],rgb[1],rgb[2]));
                
                // 0,0 to next coordinate
                double plotMult = (double)y[i]/(double)graphMaxVal;
                int yplotVal = (int)Math.round((double)graphHeight * plotMult);
                
                g.drawLine(prevx,graphHeight-prevy,x[i],graphHeight-yplotVal);            
                g.fillOval(x[i]-(pointRad/2),graphHeight-yplotVal-(pointRad/2),pointRad,pointRad);    

                g.setFont(new Font("default", Font.PLAIN,9));
                g.drawString(""+y[i],x[i]-(pointRad/2)+5,graphHeight-yplotVal-(pointRad/2)-5);

                g.setFont(new Font("SansSerif", Font.BOLD, 11));
                
                prevx = x[i];
                prevy = yplotVal;
            }
        }

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g;
            
            // set font weight to bold
            g2d.setFont(new Font("SansSerif", Font.BOLD, 10));

            // set background to white
            g2d.setColor(new Color(255,255,255));
            g2d.fillRect(0,0,width,height);

            g2d.setStroke(new BasicStroke(1.65f));
            g2d.setColor(new Color(0,0,0)); // black

            // graph border
            g2d.drawLine(xAdjPlc,0,xAdjPlc,graphHeight);
            g2d.drawLine(xAdjPlc,graphHeight,graphWidth+xAdjPlc,graphHeight);
            g2d.drawLine(graphWidth+xAdjPlc,0,graphWidth+xAdjPlc,graphHeight);

            System.out.println("paint()");

            int[] xLabelsPos = new int[xlabels.length];
            g2d.setColor(new Color(rand(0,150),rand(0,150),rand(0,150))); // random color
            int xint = 0;
            for(int i=0; i<xlabels.length; ++i){
                g2d.drawString(xlabels[i],xint+xAdjPlc+(xinterval/2)-xlabels[i].length(),graphHeight+20);
                xLabelsPos[i] = xint+xAdjPlc+(xinterval/2);
                xint+=xinterval;
            }

            g2d.setColor(new Color(rand(0,150),rand(0,150),rand(0,150))); // random color
            int yint = 0;
            for(int i=0; i<ylabels.length; ++i){
                String label = ylabels[ylabels.length-1-i];
                g2d.drawString(label,(xAdjPlc/2)-label.length(),yint);
                              
                g.setColor(new Color(235,235,235));
                g.drawLine(xAdjPlc,yint,xAdjPlc+graphWidth,yint);
                g.setColor(new Color(0,0,0));
                
                yint+=yinterval;
            }

            int[] x = xLabelsPos;
            int xlength = x.length;
            for(int i=0; i<xlength; ++i)
            {
                // lines of x
                g.setColor(new Color(0,0,0));
                g.drawLine(x[i],graphHeight-5,x[i],graphHeight+5);

                g.setColor(new Color(235,235,235));
                g.drawLine(x[i],graphHeight-5,x[i],graphHeight-graphHeight);
            }

            for(int i=0; i<xyValues.length; ++i){
                lineSeries(
                    g,
                    new int[]{rand(0,230),rand(0,230),rand(0,230)}, // colors
                    xLabelsPos, // position of x labels to match the placement of data
                    xyValues[i]
                );
            }
        }
    }

    public JtimeSeries(int[] dimensions, int[] adjustmentPlacing, String[] xlabels, String[] legends, int[][] xyValues)
    {
        PItemNames = legends;
        PXLabels = xlabels;
        PValues = xyValues;

        POverAllTotal = 0;
        PProductTotal = new int[xyValues.length];

        for(int i=0;i<xyValues.length; ++i){
            for(int j=0;j<xyValues[i].length;++j){
                POverAllTotal+=xyValues[i][j];
                PProductTotal[i]+=xyValues[i][j];
            }
        }

        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(new MyGraphics(dimensions,adjustmentPlacing,xlabels,legends,xyValues));
        this.setSize(dimensions[0],dimensions[1]);
    }

}