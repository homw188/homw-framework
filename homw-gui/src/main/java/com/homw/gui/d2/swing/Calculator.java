package com.homw.gui.d2.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * @description 简易计算器实现
 * @author Hom
 * @version 1.0
 * @since 2020-12-03
 */
public class Calculator {
	/**
	 * 计算方法接口
	 */
	static interface Method {
		float calc(float a, float b);
	}

	/**
	 * 计算器符号类型
	 */
	static enum SymbolType {
		/**
		 * 数字符号
		 */
		NUM,
		/**
		 * 计算方法符号
		 */
		METHOD,
		/**
		 * 等于符号
		 */
		EQUAL,
		/**
		 * 清空符号
		 */
		CLEAR,
		/**
		 * 删除符号
		 */
		DELETE,
		/**
		 * 小数点符号
		 */
		DOT,
		/**
		 * 正负号
		 */
		SIGN,
		/**
		 * 中间结果符号
		 */
		TEMP
	}

	/**
	 * 计算器符号
	 */
	static class Symbol {
		String label;// 符号显示
		SymbolType type;// 类型
		Method method;// 计算方法
		boolean edged = true;// 符号按钮是否有外边线框

		public Symbol() {
		}

		public Symbol(String label, SymbolType type) {
			this(label, type, null, true);
		}

		public Symbol(String label, SymbolType type, boolean edged) {
			this(label, type, null, edged);
		}

		public Symbol(String label, SymbolType type, Method method) {
			this(label, type, method, true);
		}

		public Symbol(String label, SymbolType type, Method method, boolean edged) {
			this.label = label;
			this.type = type;
			this.method = method;
			this.edged = edged;
		}
	}

	/**
	 * 计算器面板
	 */
	static class Panel {
		private JFrame window;
		private Font btnFont;// 符号按钮字体
		private Dimension size;
		private JLabel inputLabel;// 计算数字输入框
		private JLabel infoLabel;// 计算符号显示框

		private Stack<Symbol> optStack;// 操作数栈
		private boolean calculable = false;// 是否可计算

		private static final Color OPT_BTN_COLOR = new Color(220, 220, 220);
		private static final Color EQUAL_BTN_COLOR = new Color(121, 205, 205);

		public Panel(String title) {
			this(title, new Dimension(400, 500));
		}

		public Panel(String title, Dimension size) {
			this.size = size;
			optStack = new Stack<>();
			btnFont = new Font("System", Font.BOLD, 18);
			initWindow(title);
		}

		protected void initWindow(String title) {
			window = new JFrame(title);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JPanel content = new JPanel(new BorderLayout(0, 0));
			content.setPreferredSize(size);
			content.add(drawConsolePanel(), BorderLayout.NORTH);
			content.add(drawOptPanel(), BorderLayout.CENTER);
			window.getContentPane().add(content);
		}

		protected JPanel drawConsolePanel() {
			infoLabel = new JLabel(" ");// 初始空格占位
			infoLabel.setForeground(Color.GRAY);// 字体颜色
			infoLabel.setFont(new Font("Sytem", Font.PLAIN, 16));
			infoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

			inputLabel = new JLabel("0");
			inputLabel.setFont(new Font("Sytem", Font.BOLD, 36));
			inputLabel.setHorizontalAlignment(SwingConstants.RIGHT);

			JPanel consolePanel = new JPanel(new BorderLayout());
			consolePanel.setSize(size.width, 100);
			consolePanel.add(infoLabel, BorderLayout.NORTH);
			consolePanel.add(inputLabel, BorderLayout.CENTER);
			return consolePanel;
		}

		protected JPanel drawOptPanel() {
			JPanel optPanel = new JPanel(new GridLayout(5, 4, 0, 0));// 网格布局
			optPanel.add(drawOptBtn(new Symbol("C", SymbolType.CLEAR)));
			optPanel.add(drawOptBtn(new Symbol("D", SymbolType.DELETE)));
			optPanel.add(drawOptBtn(new Symbol("%", SymbolType.METHOD, new Method() {
				@Override
				public float calc(float a, float b) {
					return a % b;
				}
			})));
			optPanel.add(drawOptBtn(new Symbol("÷", SymbolType.METHOD, new Method() {
				@Override
				public float calc(float a, float b) {
					return a / b;
				}
			}, false)));

			optPanel.add(drawOptBtn(new Symbol("7", SymbolType.NUM)));
			optPanel.add(drawOptBtn(new Symbol("8", SymbolType.NUM)));
			optPanel.add(drawOptBtn(new Symbol("9", SymbolType.NUM)));
			optPanel.add(drawOptBtn(new Symbol("x", SymbolType.METHOD, new Method() {
				@Override
				public float calc(float a, float b) {
					return a * b;
				}
			}, false)));

			optPanel.add(drawOptBtn(new Symbol("4", SymbolType.NUM)));
			optPanel.add(drawOptBtn(new Symbol("5", SymbolType.NUM)));
			optPanel.add(drawOptBtn(new Symbol("6", SymbolType.NUM)));
			optPanel.add(drawOptBtn(new Symbol("-", SymbolType.METHOD, new Method() {
				@Override
				public float calc(float a, float b) {
					return a - b;
				}
			}, false)));

			optPanel.add(drawOptBtn(new Symbol("1", SymbolType.NUM)));
			optPanel.add(drawOptBtn(new Symbol("2", SymbolType.NUM)));
			optPanel.add(drawOptBtn(new Symbol("3", SymbolType.NUM)));
			optPanel.add(drawOptBtn(new Symbol("+", SymbolType.METHOD, new Method() {
				@Override
				public float calc(float a, float b) {
					return a + b;
				}
			}, false)));

			optPanel.add(drawOptBtn(new Symbol("+/-", SymbolType.SIGN)));
			optPanel.add(drawOptBtn(new Symbol("0", SymbolType.NUM)));
			optPanel.add(drawOptBtn(new Symbol(".", SymbolType.DOT)));
			optPanel.add(drawOptBtn(new Symbol("=", SymbolType.EQUAL, false)));
			return optPanel;
		}

		protected JButton drawOptBtn(final Symbol symbol) {
			final JButton btn = new JButton(symbol.label);
			btn.setFont(btnFont);
			btn.setContentAreaFilled(false);// 消除按钮默认样式
			btn.setFocusPainted(false);// 消除按钮选中效果
			btn.setOpaque(true);
			if (symbol.type == SymbolType.CLEAR || symbol.type == SymbolType.DELETE
					|| symbol.type == SymbolType.METHOD) {
				btn.setBackground(OPT_BTN_COLOR);
			} else if (symbol.type == SymbolType.EQUAL) {
				btn.setBackground(EQUAL_BTN_COLOR);
			}
			// 设置按钮边框样式
			btn.setBorder(BorderFactory.createMatteBorder(1, 0, 0, symbol.edged ? 1 : 0, Color.GRAY));
			btn.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					btn.setBackground(Color.LIGHT_GRAY);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					if (symbol.type == SymbolType.CLEAR || symbol.type == SymbolType.DELETE
							|| symbol.type == SymbolType.METHOD) {
						btn.setBackground(OPT_BTN_COLOR);
					} else if (symbol.type == SymbolType.EQUAL) {
						btn.setBackground(EQUAL_BTN_COLOR);
					} else {
						btn.setBackground(null);
					}
				}
			});
			// 绑定按钮点击事件
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dealOptSymbol(symbol);
				}
			});
			return btn;
		}

		/**
		 * 刷新操作数栈内容至提示框
		 */
		protected void updateInfoLabel() {
			StringBuilder info = new StringBuilder(" ");
			for (Symbol symbol : optStack) {
				if (symbol.type == SymbolType.NUM || symbol.type == SymbolType.METHOD
						|| symbol.type == SymbolType.EQUAL) {
					info.append(symbol.label);
					info.append(" ");
				}
			}
			infoLabel.setText(info.toString());
		}

		/**
		 * 小数的整型格式化
		 * 
		 * @param num
		 * @return
		 */
		protected String formatNum(float num) {
			int temp = (int) num;
			if (temp < num) {
				return String.valueOf(num);
			}
			return String.valueOf(temp);
		}

		/**
		 * 处理计算符号点击事件
		 * 
		 * @param symbol
		 */
		protected void dealOptSymbol(Symbol symbol) {
			String inpTxt = inputLabel.getText();
			float curNum = Float.valueOf(inpTxt);
			if (symbol.type == SymbolType.NUM) {
				if (!calculable) {
					inputLabel.setText(symbol.label);
					calculable = true;
					updateInfoLabel();// 删除 -> 点击数字
				} else {
					inputLabel.setText(inpTxt + symbol.label);
				}
			} else if (symbol.type == SymbolType.METHOD) {
				if (optStack.isEmpty()) {
					optStack.push(new Symbol(formatNum(curNum), SymbolType.NUM));
					optStack.push(symbol);
					updateInfoLabel();
					calculable = false;
					return;
				}
				Symbol top = optStack.peek();
				if (calculable && optStack.size() > 1) {
					Symbol prev = optStack.get(optStack.size() - 2);
					float temp = top.method.calc(Float.valueOf(prev.label), curNum);
					String label = formatNum(temp);
					inputLabel.setText(label);
					optStack.push(new Symbol(formatNum(curNum), SymbolType.NUM));
					// 缓存中间结果，持续计算
					optStack.push(new Symbol(label, SymbolType.TEMP));
					optStack.push(symbol);
					calculable = false;
				} else {
					// 连续点击切换方法符号
					optStack.pop();
					optStack.push(symbol);
				}
				updateInfoLabel();
			} else if (symbol.type == SymbolType.EQUAL) {
				if (optStack.isEmpty()) {
					optStack.push(new Symbol(formatNum(curNum), SymbolType.NUM));
					optStack.push(symbol);
					updateInfoLabel();
					optStack.clear();
				} else {
					if (optStack.size() > 1) {
						Symbol top = optStack.peek();
						Symbol prev = optStack.get(optStack.size() - 2);
						float result = top.method.calc(Float.valueOf(prev.label), curNum);
						inputLabel.setText(formatNum(result));
						// 计算过程回显->清除
						optStack.push(new Symbol(formatNum(curNum), SymbolType.NUM));
						optStack.push(symbol);
						updateInfoLabel();
						optStack.clear();
					}
				}
				calculable = false;
			} else if (symbol.type == SymbolType.CLEAR) {
				inputLabel.setText("0");
				optStack.clear();
				updateInfoLabel();
			} else if (symbol.type == SymbolType.DELETE) {
				if (inpTxt.length() > 1) {
					inputLabel.setText(inpTxt.substring(0, inpTxt.length() - 1));
				} else {
					inputLabel.setText("0");
				}
			} else if (symbol.type == SymbolType.SIGN) {
				inputLabel.setText(curNum > 0 ? "-" + curNum : String.valueOf(Math.abs(curNum)));
			} else if (symbol.type == SymbolType.DOT) {
				if (!inpTxt.contains(".")) {
					inputLabel.setText(inpTxt + ".");
					calculable = true;
				}
			}
		}

		public void show() {
			window.pack();
			// 窗口居中显示
			window.setLocationRelativeTo(null);
			window.setVisible(true);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Panel("计算器").show();
			}
		});
	}
}
