package com.poyu.game;

import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Java 小程式：剪刀石頭布
 * 開發時間：2018.03.20
 * 使用版本：jdk 1.7
 * @author poyuchen
 *
 */
public class RockPaperScissors {
	private static final String title = "剪刀、石頭、布  Rock, Paper, Scissors";
	private JFrame frame;
	private List<TypeButton> types;
	private String[][] options;
	private ImageIcon questionIcon;
	private JLabel opponentLabel;
	private final int imgWeight = 120;
	private final int imgHeight = 120;
	
	public RockPaperScissors() throws IOException {
		frame = new JFrame(title);
		types = new ArrayList<TypeButton>();
        options = new String[][] {
        		{ "image/rock.png", "0" }, { "image/paper.png", "5" }, { "image/scissors.png", "2" }
        };
	}
	
	public static void main(String[] args) {
		try {
			RockPaperScissors pg = new RockPaperScissors();
			pg.run();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void run() throws IOException {
		// 開啟介面與介面設定
		frame.setSize(600, 400);
		frame.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        questionIcon = new ImageIcon(ImageIO.read(new File("image/question.png")));
        resizeImage(questionIcon);
        opponentLabel = new JLabel(questionIcon, JLabel.CENTER);
        GridBagConstraints opponentCon = new GridBagConstraints(1, 0, 1, 1, 3, 3, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0);
        frame.add(opponentLabel, opponentCon);
        
        for (int i = 0; i < options.length; i++) {
        	final TypeButton btn = new TypeButton();
        	btn.setBtnImage(new ImageIcon(ImageIO.read(new File(options[i][0]))));
        	btn.setBtnCode(Integer.parseInt(options[i][1]));
        	resizeImage(btn.getBtnImage());
        	btn.setIcon(btn.getBtnImage());
        	btn.setBorderPainted(false);
        	btn.addActionListener(new ActionListener() { 
            	public void actionPerformed(ActionEvent e) {
            		clickType(btn.getBtnCode());
            	}
            });
        	types.add(btn);
        	
    		GridBagConstraints cons = new GridBagConstraints(i, 1, 1, 1, 3, 3, GridBagConstraints.CENTER,
    				GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0);
            frame.add(btn, cons);
        }

        frame.setVisible(true);
	}
	
	class TypeButton extends JButton {
		private static final long serialVersionUID = 1L;
		
		private ImageIcon btnImage;
		private int btnCode;
		private void setBtnImage(ImageIcon btnImage) {
			this.btnImage = btnImage;
		}
		private ImageIcon getBtnImage() {
			return btnImage;
		}
		private void setBtnCode(int btnCode) {
			this.btnCode = btnCode;
		}
		private int getBtnCode() {
			return btnCode;
		}
	}
	
	private void resizeImage(ImageIcon image) {
		BufferedImage resizedImg = new BufferedImage(imgWeight, imgHeight, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(image.getImage(), 0, 0, imgWeight, imgHeight, null);
	    g2.dispose();
	    
	    image.setImage(resizedImg);
	}
	
	private void clickType(int myType) {
		for (TypeButton btn : types) {
			if (btn.getBtnCode() != myType) {
				btn.setEnabled(false);
			}
		}
		
		TypeButton oppType = getOppType();
		opponentLabel.setIcon(oppType.getBtnImage());
		
		int result = compare(myType, oppType.getBtnCode());
		String resultMsg;
		if (result == 0) {
			resultMsg = "你輸了......You lose....";
		} else if (result == 2) {
			resultMsg = "你贏了!!!!!You win!!!!!";
		} else {
			resultMsg = "~~ 平手 ~~ Tie ~~";
		}
		
		JOptionPane.showMessageDialog(frame, resultMsg);
		restartGame();
	}
	
	private void restartGame() {
		opponentLabel.setIcon(questionIcon);
		for (TypeButton btn : types) {
			btn.setEnabled(true);
		}
	}
	
	private TypeButton getOppType() {
		int rd = new Random().nextInt(3);
		return types.get(rd);
	}
	
	// 0:lose; 1:tie; 2:win
	private int compare(int myType, int oppType) {
		int isWin = 1;
		switch (myType) {
			case 0:
				if (oppType == 2) {
					isWin = 2;
				} else if (oppType == 5) {
					isWin = 0;
				}
				break;
			case 2:
				if (oppType == 0) {
					isWin = 0;
				} else if (oppType == 5) {
					isWin = 2;
				}
				break;
			case 5:
				if (oppType == 0) {
					isWin = 2;
				} else if (oppType == 2) {
					isWin = 0;
				}
				break;
		};
		return isWin;
	}
}
