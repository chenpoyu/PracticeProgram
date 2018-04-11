package com.poyu.game;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Java 小程式：計算機
 * 開發時間：2018.04.02
 * 使用版本：jdk 1.7
 * @author poyuchen
 *
 */
public class Calculator {
	private static final String title = "計算機 Calculator";
	private JFrame frame;
	private JTextField text;
	private JLabel label;
	private String[] btnStr;
	private static List<String> history;

	public Calculator() {
		frame = new JFrame(title);
		text = new JTextField(0);
		label = new JLabel("", JLabel.RIGHT);
		btnStr = new String[] { "History", "", "AC", "<-", "1", "2", "3", "+", "4", "5", "6", "-", "7", "8", "9", "*",
				".", "0", "=", "/" };
		if (history == null) {
			 history = new ArrayList<String>();
		} else if (history.size() > 5) {
			clearHistory(false);
		}
	}
	
	public static void main(String[] args) {
		try {
			Calculator cal = new Calculator();
			cal.run();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void run() {
		frame.setSize(400, 400);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 3));
        for (String t : btnStr) {
        	JButton btn = new JButton();
        	btn.setText(t);
        	if ("0,1,2,3,4,5,6,7,8,9,.,+,-,*,/".contains(t)) {
        		btn.addActionListener(new ActionListener() {
	        		public void actionPerformed(ActionEvent e) {
	        			StringBuffer sb = new StringBuffer();
	        			sb.append("0".equals(text.getText()) ? "" : text.getText());
	        			sb.append(((JButton) e.getSource()).getText());
	        			text.setText(sb.toString());
	        		}
	        	});
        	} else if ("".equals(btnStr)) {
        		btn.setEnabled(false);
        	} else {
        		btn.addActionListener(new ActionListener() {
	        		public void actionPerformed(ActionEvent e) {
        				clickFunc(((JButton) e.getSource()).getText());
	        		}
	        	});
        	}
        	panel.add(btn);
        }

        Font font = new Font(null, Font.PLAIN, 36);
        label.setFont(font);
        text.setFont(font);
        text.setHorizontalAlignment(SwingConstants.RIGHT);
        text.setEditable(false);
        
        JPanel labelPanel = new JPanel(new GridLayout(2, 1));
        labelPanel.add(label);
        labelPanel.add(text);

        frame.add(labelPanel);
        frame.add(panel);
        frame.setVisible(true);

	}
	
	private void clickFunc(String func) {
		switch (func) {
		case "=":
			calculate();
			break;
		case "AC":
			text.setText(String.valueOf(0));
			label.setText(String.valueOf(0));
			break;
		case "<-":
			if (text.getText().length() > 0) {
				text.setText(text.getText().substring(0, text.getText().length() - 1));
			}
			break;
		case "History":
			listHistory();
			break;
		default:

		}
	}
	
	private void calculate() {
		String formula = text.getText().trim();
			
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		
		Double result = null;
		try {
			String script = "result = " + formula + "; result.toFixed(5);";
			result = Double.valueOf((String) engine.eval(script));
		} catch (Exception e) {
			e.printStackTrace();
			label.setText("Illegal Formula!");
			return;
		}
		
		label.setText(formula + "=" + result);
		text.setText(result.toString());
		history.add(formula + "=" + result.toString());
		clearHistory(false);
	}
	
	private void listHistory() {
		StringBuffer sb = new StringBuffer();
		for (String str : history) {
			sb.append(str);
			sb.append("\n");
		}
		String[] options={"關閉", "清除"};
		int opt = JOptionPane.showOptionDialog(frame, sb.toString(), "歷史紀錄 History", JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, "關閉");
		if (1 == opt) {
			clearHistory(true);
		}
	}

	private void clearHistory(boolean clearAll) {
		if (clearAll) {
			history.clear();
		} else {
			while (history.size() > 5) {
				history.remove(0);
			}
		}
	}
}
