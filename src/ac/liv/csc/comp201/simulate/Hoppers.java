package ac.liv.csc.comp201.simulate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ac.liv.csc.comp201.model.IHoppers;

public class Hoppers extends JPanel implements IHoppers {
	
	public static final int COFFEE=0;
	public static final int MILK=1;
	public static final int SUGAR=2;
	public static final int CHOCOLATE=3;
		
	private static final int MAX_HOPPERS=4;
	private static final int padding=8;
	private static final int height=200;
	private static final int width=500;
	private static final int x=20;
	private static final int y=500;
	private static JFrame frame;
	


	private static final String captions[]={"Coffee","Milk (powder)","Sugar","Chocolate"};
	
	private static final float amounts[]={0.1f,0.11f,0.073f,1f}; 
	
	private static final String fileName="hopperState.txt";

	
	public double getHopperLevelsGrams(int index) {
		return(this.hopperLevelsGrams[index]);
	}
	
	private double hopperLevelsGrams[]=new double[MAX_HOPPERS];
	
	private boolean hoppersOn[]=new boolean[MAX_HOPPERS];
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.IHoppers#setHopperOn(int)
	 */
	@Override
	public void setHopperOn(int drink) {
	//	System.out.println("hopper on...");
		hoppersOn[drink]=true;
	}
	
	
	private void save() {
		PrintWriter pw=null;
		try {
			pw = new PrintWriter(fileName);
		} catch (FileNotFoundException e) {
			return;
		}
		for (int index=0;index<this.hopperLevelsGrams.length;index++) {
			pw.println(""+this.hopperLevelsGrams[index]);
		}
		pw.close();		
	}
	
	private void load() {
		BufferedReader reader=null;
		boolean ok=true;
		try {
			reader=new BufferedReader(new FileReader(fileName));			
		} catch (IOException e) {
			ok=false;
		}
		if (!ok) {
			System.out.println("Creating new state file... for hoppers");
			for (int index=0;index<this.hopperLevelsGrams.length;index++) {
				hopperLevelsGrams[index]=1000;
			}
			save();		// create state if not run before	
			return;
		}
		try {
		String hopperLevel=reader.readLine();
		int index=0;
		while (hopperLevel!=null) {
			double level=Double.parseDouble(hopperLevel);
			hopperLevelsGrams[index]=level;
			hopperLevel=reader.readLine();
			index++;
			if (index==hopperLevelsGrams.length) break;
		}
		reader.close();
		}
		catch (IOException e) {
			
		} 
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.IHoppers#updateHoppers(ac.liv.csc.comp201.simulate.Cup)
	 */
	@Override
	public void updateHoppers(Cup cup) {
		float amount=1;
		for (int index=0;index<MAX_HOPPERS;index++) {
			amount=amounts[index];
			if (hoppersOn[index]) {
				//hoppersOn[index]=false;
				switch (index) {
					
					case COFFEE :
						if (cup!=null) {
							cup.addCoffee(amount);
						}
						hopperLevelsGrams[COFFEE]-=amount;
						break;
					case CHOCOLATE :
						if (cup!=null) {
							cup.addChocolate(amount);
						}
						hopperLevelsGrams[CHOCOLATE]-=amount;
						break;
					case MILK :
						if (cup!=null) {
							cup.addMilk(amount);
						}
						hopperLevelsGrams[MILK]-=amount;
						break;
					case SUGAR :
						if (cup!=null) {
							cup.addSugar(amount);
						}
						hopperLevelsGrams[SUGAR]-=amount;
						break;
					

				}
			}
		}
		save();
		frame.invalidate();
		frame.validate();
		frame.repaint();		
	}
	
	
	
	public Hoppers() {
		load();
		frame=new JFrame();
		frame.setBounds(x, y, width,height);		
		frame.setSize(width, height);
		frame.setVisible(true);
		frame.add(this);
		frame.invalidate();
		frame.validate();
	}
	
	
	
	

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		int titleH=30;
		Insets insets=frame.getInsets();
		int height=frame.getHeight()-insets.bottom-insets.top;
		int width=frame.getWidth()-insets.left-insets.right;
		int hopperWidth=((width-padding)/MAX_HOPPERS)-padding;
		int hopperHeight=(height)-padding*2;
		int x=padding;
		int y=height-padding;
		for (int index=0;index<MAX_HOPPERS;index++) {
			if (index<captions.length) {
				g2d.drawString(captions[index],x+padding*2,insets.top);
			}
			int h=(int)(((hopperHeight-titleH)*hopperLevelsGrams[index])/1000);
			g2d.fillRect(x,y-h,hopperWidth,h);
			x=x+hopperWidth+padding;
		}
		//g2d.fillRect(0,100,200,padding);
		
		//System.out.println("h is "+frame.getHeight()+" inset top is "+frame.getInsets().top+" bottom is "+frame.getInsets().bottom);
		//g2d.fillRect(0,height-insets.top-insets.bottom-padding,width,padding);
//		g2d.fillRect(0,0,width,padding);
	}


	@Override
	public void setHopperOff(int hopperIndex) {
		hoppersOn[hopperIndex]=false;
	}
}
