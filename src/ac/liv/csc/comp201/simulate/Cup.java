package ac.liv.csc.comp201.simulate;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Stroke;

import javax.swing.JPanel;

public class Cup extends JPanel {
	
	public static final int SMALL_CUP=1;
	public static final int MEDIUM_CUP=2;
	public static final int LARGE_CUP=3;
	
	
	public static final double SMALL=0.34;
	public static final double MEDIUM=0.45;
	public static final double LARGE=0.56;
		
	
	private double waterLevelLitres;	// amount of water in Litres
	private double coffeeGrams;			// amount of coffee in grams
	private double sugarGrams;			// amount of sugar in grams
	private double milkGrams;			// amount of milk powder in grams
	private double chocolateGrams;		// amount of chocolate in litres
	private double temperatureInC;		// temperature degrees C
	
	private double scale=1.0;
	
	private double capacity=0;
	
	public Cup(int type) {
		switch (type) {
			case SMALL_CUP :
				capacity=SMALL;
				break;
			case MEDIUM_CUP :
				capacity=MEDIUM;
				break;
			case LARGE_CUP :
				capacity=LARGE;
				break;						
		}
		this.scale=capacity/SMALL;
	}
	
	
	protected void addCoffee(double amount) {
		coffeeGrams+=amount;
	}
	
	protected void addSugar(double amount) {
		sugarGrams+=amount;
	}
	
	protected void addMilk(double amount) {
		milkGrams+=amount;
	}
	
	protected void addChocolate(double amount) {
		chocolateGrams+=amount;
	}
	
	
	
	
	public void addWater(double amount,double temperature) {
		double liquidLevel=waterLevelLitres;
		amount=Math.min(amount,capacity-liquidLevel);		//don't overfill cup
		double heat1=liquidLevel*temperatureInC;
		double heat2=amount*temperature;
		double heat=heat1+heat2;
		liquidLevel=waterLevelLitres+amount;
		temperatureInC=heat/liquidLevel;
		waterLevelLitres+=amount;
	}
	
	
	
	/**
	 * @return the waterLevelLitres
	 */
	public double getWaterLevelLitres() {
		return waterLevelLitres;
	}
	/**
	 * @param waterLevelLitres the waterLevelLitres to set
	 */
	public void setWaterLevelLitres(double waterLevelLitres) {
		this.waterLevelLitres = waterLevelLitres;
	}
	/**
	 * @return the coffeeGrams
	 */
	public double getCoffeeGrams() {
		return coffeeGrams;
	}
/**
	 * @return the sugarGrams
	 */
	public double getSugarGrams() {
		return sugarGrams;
	}

    /**
	 * @return the milkLitres
	 */
	public double getMilkGrams() {
		return milkGrams;
	}
	
	/**
	 * @return the temperatureInC
	 */
	public double getTemperatureInC() {
		return temperatureInC;
	}
	/**
	 * @param temperatureInC the temperatureInC to set
	 */
	public void setTemperatureInC(double temperatureInC) {
		this.temperatureInC = temperatureInC;
	}
	
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.drawString("Temp "+temperatureInC, 20, 200);
		Insets insets=getInsets();
		int padding=20;
		int height=getHeight()-insets.bottom-insets.top;
		int width=getWidth()-insets.left-insets.right;
		int cupWidth=(int)(30*scale);
		int cupHeight=(int)(100*scale);
		double cupFill=waterLevelLitres/capacity;
		double r1=cupWidth;
		double r2=cupWidth*2/3;
		double h=cupHeight;
		double hbase=h*r1/(r1-r2);
		double a=r2/hbase;
		double vbase=(0.333333333333)*Math.PI*(Math.pow(a, 2))*(Math.pow(hbase,3));
		double hc=hbase+h;
		double vtotal=(0.333333333333)*Math.PI*(Math.pow(a, 2))*(Math.pow(hc,3));
		hc=Math.pow(3*vtotal/(Math.PI*Math.pow(a,2)),0.333333333);
		double vcup=vtotal-vbase;
		vcup=vcup*cupFill;		// fill up to percentage
		double vcone=vcup+vbase;
		h=Math.pow(3*vcone/(Math.PI*Math.pow(a,2)),0.333333333);
		h=h-hbase;
		double cwidth=(h+hbase)*a;
		g2d.drawLine((int)(width/2-cwidth), (int)(cupHeight-h+padding), (int)(width/2+cwidth),(int)(cupHeight-h+padding));
		Polygon poly=new Polygon();
		poly.addPoint((int)(width/2-cwidth), (int)(cupHeight-h+padding));
		poly.addPoint(width/2-cupWidth*2/3,cupHeight+padding);
		poly.addPoint(width/2+cupWidth*2/3,cupHeight+padding);
		poly.addPoint((int)(width/2+cwidth),(int)(cupHeight-h+padding));
		Color fillColor=Color.BLACK;
		if ((coffeeGrams==0) && (this.chocolateGrams==0) && (this.milkGrams==0)) {
			fillColor=new Color(220,220,240);
			
		} else {
			if ((coffeeGrams!=0) && (this.chocolateGrams==0)) {
				fillColor=Color.BLACK;
				if (this.milkGrams!=0) {
					fillColor=new Color(0x9e,0x7e,0x78);
				}
				
			} 
			if ((coffeeGrams==0) && (this.chocolateGrams!=0)) {
				fillColor=new Color(0x4e,0x2e,0x28);
				if (this.milkGrams!=0) {
					fillColor=new Color(0x9e,0x7e,0x78);
				}
			}
		}		
		g2d.setColor(fillColor);
		g2d.fillPolygon(poly);
		Stroke s=new BasicStroke((float) 4.5);
		g2d.setStroke(s);
		g2d.setColor(new Color(100,100,100));
		g2d.drawLine((int)(width/2-r1),padding,(int)(width/2-r2),cupHeight+padding);
		g2d.drawLine((int)(width/2-r2),cupHeight+padding,(int)(width/2+r2),cupHeight+padding);
		g2d.drawLine((int)(width/2+r2),cupHeight+padding,(int)(width/2+r1),padding);
		
	}


	public double getChocolateGrams() {
		return chocolateGrams;
	}
	
	
	

}
