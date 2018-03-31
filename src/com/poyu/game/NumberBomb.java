package com.poyu.game;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Java 小程式：終極密碼
 * 開發時間：2018.03.28
 * 使用版本：jdk 1.7
 * @author poyuchen
 *
 */
public class NumberBomb {
	private static final String title = "終極密碼  NumberBomb";
	private final String range = "0 ~ 99";
	private JFrame frame;
	private String[] btnArray;
	private JButton enter;
	private JLabel label;
	private JTextField text;
	private int answer;
	private int small = 0;
	private int large = 99;
	private ImageIcon dialogIcon;

	public NumberBomb() {
		frame = new JFrame(title);
		btnArray = new String[] {
				"1", "2", "3", "4", "5", "6", "7", "8", "9", "X", "0"
		};
		enter = new JButton("Submit");
		label = new JLabel(range, JLabel.CENTER);
		text = new JTextField();
	}
	
	public static void main(String[] args) {
		try {
			NumberBomb nb = new NumberBomb();
			nb.run();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void run() throws IOException {
		frame.setSize(400, 400);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        dialogIcon = new ImageIcon(ImageIO.read(new File("image/explode.png")));

        JPanel panel = new JPanel(new GridLayout(4, 3));
        for (int i = 0; i < btnArray.length; i++) {
        	final JButton btn = new JButton();
        	if ("X".equals(btnArray[i])) {
        		btn.setText(btnArray[i]);
	        	btn.addActionListener(new ActionListener() {
	        		public void actionPerformed(ActionEvent e) {
	        			text.setText("");
	        		}
	        	});
        	} else {
	        	btn.setText(btnArray[i]);
	        	btn.addActionListener(new ActionListener() {
	        		public void actionPerformed(ActionEvent e) {
	        			text.setText(text.getText() + btn.getText());
	        		}
	        	});
        	}
        	panel.add(btn);
        }
        
        enter.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			compareNumber();
    		}
    	});
        panel.add(enter);
        
        Font font = new Font(null, Font.PLAIN, 36);
        label.setFont(font);
        text.setFont(font);
        text.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        
        JPanel labelPanel = new JPanel(new GridLayout(2, 1));
        labelPanel.add(label);
        labelPanel.add(text);
        
        frame.add(labelPanel);
        frame.add(panel);
        
        genBombNum();
        frame.setVisible(true);
	}
	private void genBombNum() {
		answer = (int) Math.round(Math.random() * 100);
	}

	private void compareNumber() {
		String numStr = text.getText();
		if (numStr != null && !"".equals(numStr)) {
			int num = Integer.parseInt(numStr);
			if (num == answer) {
				int opt = JOptionPane.showConfirmDialog(frame, "恭喜猜中！！你輸了！！", "~結果 Result~", JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE, dialogIcon);
				if (opt == JOptionPane.YES_OPTION) {
					genBombNum();
					label.setText(range);
				} else {
					for (Component component : getAllComponents(frame)) {
						if (component instanceof JButton) {
							component.setEnabled(false);
						}
					}
				}
			} else if (num < answer) {
				if (num <= small) {
					JOptionPane.showMessageDialog(frame, "輸入的數字不可小於區間", "Error", JOptionPane.WARNING_MESSAGE);
				} else {
					small = num;
					label.setText(small + " ~ " + large);
				}
			} else if (num > answer) {
				if (num >= large) {
					JOptionPane.showMessageDialog(frame, "輸入的數字不可大於區間", "Error", JOptionPane.WARNING_MESSAGE);
				} else {
					large = num;
					label.setText(small + " ~ " + large);
				}
			}
			text.setText("");
		}
	}
	
	public List<Component> getAllComponents(Container c) {
	    Component[] comps = c.getComponents();
	    List<Component> compList = new ArrayList<Component>();
	    for (Component comp : comps) {
	        compList.add(comp);
	        if (comp instanceof Container)
	            compList.addAll(getAllComponents((Container) comp));
	    }
	    return compList;
	}
}
