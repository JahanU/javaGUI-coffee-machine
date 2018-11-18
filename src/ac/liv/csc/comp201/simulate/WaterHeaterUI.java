package ac.liv.csc.comp201.simulate;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ac.liv.csc.comp201.model.IMachine;

public class WaterHeaterUI extends JFrame {
	
	/**
	 * Create the Water heater UI, a Java JFrame is used to show the temperature of the water heater
	 */
		
	private float temperatureDegreesC=21f;
	private int topGauge=0;
	
	private int padding=10;
	private JPanel gauge;
	private int width=100;
	private int height=400;
	private JLabel tempInfo=new JLabel();
	
	
	
	
	public void setTemperatureDegreesC(float temperatureDegreesC) {
		this.temperatureDegreesC=temperatureDegreesC;
		Insets insets = getInsets();
		gauge.setBackground(Color.RED);
		float perc=temperatureDegreesC/100;
		if (perc>1) {
			perc=1;
		}
		float heightGauge=height-topGauge-padding;		
		int tempScale=Math.round(heightGauge)-Math.round(heightGauge*perc);		
		gauge.setBounds(insets.left+padding, topGauge+tempScale, width-padding*2,Math.round(heightGauge-tempScale));
		tempInfo.setText(""+Math.round(this.temperatureDegreesC*100.0)/100.0+" C");		
	}
	
	public WaterHeaterUI() {
		float perc=temperatureDegreesC/100;
		if (perc>1) {
			perc=1;
		}
		int tempScale=height-Math.round(height*perc);
		setTitle("Water Heater");
		setLayout(null);
		Insets insets = getInsets();
		int titleHeight=20;

		JLabel title=new JLabel();
		title.setText("Water Heater");
		title.setBounds(insets.left+padding, insets.top+padding, width-padding*2,titleHeight);
		topGauge=titleHeight;
		this.add(title);
		tempInfo.setText(""+Math.floor(this.temperatureDegreesC)+" C");
		tempInfo.setBounds(insets.left+padding, insets.top+padding+this.topGauge, width-padding*2,titleHeight);
		this.add(tempInfo);
		topGauge=topGauge+titleHeight+padding*2;
		
		
		
		gauge=new JPanel();
		gauge.setBackground(Color.RED);
		gauge.setBounds(insets.left+padding, insets.top+padding+topGauge+tempScale, width-padding*2,height-topGauge-tempScale);
		this.add(gauge);
		
		
		JPanel tempPanel=new JPanel();
		tempPanel.setSize(40,320);
		this.add(tempPanel);
		double screenWidth=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		
		setBounds((int)(screenWidth-width-50), 50, width,height);
		this.setUndecorated(true);
		setVisible(true);
		
	}

}
