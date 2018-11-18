package ac.liv.csc.comp201.simulate;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import ac.liv.csc.comp201.model.ICoinHandler;
import ac.liv.csc.comp201.model.IMachine;

public class CoinHandler extends JFrame implements ICoinHandler, Temp {
	
	
	private static final String coinCodes[]={"ab","ac","ba","bc","bd","ef","zz" };
	
	private static final String coinNames[]={"1p","5p","10p","20p","50p","100p" };
	
	private static final String fileName="coinHandlerState.txt";
	
	private String coinTray="";
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#getCoinTray()
	 */
	@Override
	public String getCoinTray() {
		return(coinTray);
	}
	
	private Vector <String> coinCodeBuffer=new Vector <String> ();
	
	private int coinHopperLevels[]=new int[coinCodes.length];
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#coinAvailable(java.lang.String)
	 */
	@Override
	public boolean coinAvailable(String coinCode) {
		for (int index=0;index<coinCodes.length;index++) {
			if (coinCode.equals(coinCodes[index])) {
				if (coinHopperLevels[index]==0) {
					return(false);
				}
				return(true);
			}
		}
		return(false);		
	}
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#setHopperLevel(java.lang.String, int)
	 */
	@Override
	public void setHopperLevel(String coinCode,int level) {
		for (int index=0;index<coinCodes.length;index++) {
			if (coinCode.equals(coinCodes[index])) {
				coinHopperLevels[index]=level;
				save();
			}
		}
	}
	
	private void save() {
		PrintWriter pw=null;
		try {
			pw = new PrintWriter(fileName);
		} catch (FileNotFoundException e) {
			return;
		}
		pw.println(coinTray);
		for (int index=0;index<coinHopperLevels.length;index++) {
			pw.println(""+coinHopperLevels[index]);
		}
		pw.close();		
	}
	
	private void load() {
		BufferedReader reader=null;
		String tray=null;
		try {
			reader=new BufferedReader(new FileReader(fileName));
			tray = reader.readLine();
		} catch (IOException e) {
			tray=null;
		}
		if (tray==null) {
			System.out.println("Creating new state file...");
			this.coinTray=tray;
			for (int index=0;index<coinHopperLevels.length;index++) {
				coinHopperLevels[index]=10;
			}
			save();		// create state if not run before	
			return;
		}
		System.out.println("Tray is "+tray);
		try {
		String coinLevel=reader.readLine();
		int index=0;
		while (coinLevel!=null) {
			int level=Integer.parseInt(coinLevel);
			this.coinHopperLevels[index]=level;
			coinLevel=reader.readLine();
			index++;
			if (index==coinHopperLevels.length) break;
		}
		reader.close();
		}
		catch (IOException e) {
			tray=null;
		} 
	}
	
	private void addCoin(String coinCode) {
		for (int index=0;index<coinCodes.length;index++) {
			if (coinCode.equals(coinCodes[index])) {
				coinHopperLevels[index]++;
				save();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#getCoinHopperLevel(java.lang.String)
	 */
	@Override
	public int getCoinHopperLevel(String coinCode) {
		for (int index=0;index<coinCodes.length;index++) {
			if (coinCode.equals(coinCodes[index])) {
				return (coinHopperLevels[index]);
			}
		}
		return(0);
	}
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#dispenseCoin(java.lang.String)
	 */
	@Override
	public boolean dispenseCoin(String coinCode) {
		for (int index=0;index<coinCodes.length;index++) {
			if (coinCode.equals(coinCodes[index])) {
				if (coinHopperLevels[index]==0) {
					return(false);
				}
				coinTray=coinTray+coinNames[index]+" ";
				coinHopperLevels[index]-=1;
				save();
				return(true);
			}
		}
		return(false);
	}
	
	private int coinWidth=120;
	
	private boolean coinHandlerOpen=true;
	
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.ICoinHandler#getCoinKeyCode()
	 */
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#getCoinKeyCode()
	 */
	@Override
	public synchronized String getCoinKeyCode() {
		if (coinCodeBuffer.size()>0) {
			String code=coinCodeBuffer.get(0);
			coinCodeBuffer.remove(0);
			return(code);
		}	
		return(null);
	}
	
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#lockCoinHandler()
	 */
	@Override
	public void lockCoinHandler() {
		coinHandlerOpen=false;
	}
	
	/* (non-Javadoc)
	 * @see ac.liv.csc.comp201.simulate.Temp#unlockCoinHandler()
	 */
	@Override
	public void unlockCoinHandler() {
		coinHandlerOpen=true;
	}
	
	
	public static void main(String args[]) {
		System.out.println("starting...");
		CoinHandler instance=new CoinHandler(null);
		instance.setVisible(true);		
	}
	
	
	private static final String coinImageNames[]={"coin1p.jpg","coin5p.jpg","coin10p.jpg","coin20p.jpg","coin50p.jpg","pound.jpg"};

	public static BufferedImage scale(BufferedImage sbi, int imageType, float dWidth, float dHeight) {
	    BufferedImage dbi = null;
	    
	    float fWidth=dWidth/sbi.getWidth();
	    float fHeight=dHeight/sbi.getHeight();
	    if(sbi != null) {
	        dbi = new BufferedImage((int)dWidth, (int)dHeight, imageType);
	        Graphics2D g = dbi.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
	        g.drawRenderedImage(sbi, at);
	    }
	    return dbi;
	}
	
	public CoinHandler(IMachine machine) {
		load();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		setBounds(10,screenSize.height-coinWidth-30,coinWidth*6+20,coinWidth+30);
		//this.setUndecorated(true);
		setTitle("Coin Handler");
		setAlwaysOnTop(true);
	//	System.out.println("starting..");
		setLayout(new GridLayout(1, 6, 0, 0));
		for (int index=0;index<6;index++) {
			JButton button = new JButton("");
			try {
			//	System.out.println("Image is "+"/images/"+coinImageNames[index]);
				InputStream is=getClass().getResourceAsStream("/images/"+coinImageNames[index]);
				
			    BufferedImage img = ImageIO.read(is);
			    BufferedImage i=scale(img,img.getType(),coinWidth,coinWidth);
			    ImageIcon icon=new ImageIcon(i);
			    
			    button.setIcon(icon);
			    
			  } catch (Exception ex) {
			    ex.printStackTrace();
			  }
			//buttons[index]=button;
			button.setActionCommand(coinCodes[index]);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					if (coinHandlerOpen) {
					   addCoin(actionEvent.getActionCommand());
					   coinCodeBuffer.add(actionEvent.getActionCommand());				}
				    }
				
			});
			this.getContentPane().add(button);
		}
		this.setVisible(true);

	}
	
	public void close() {
		this.setVisible(false);
		this.dispose();
	}

	@Override
	public void clearCoinTry() {
		coinTray="";
		save();
	}

}
