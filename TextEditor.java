package com.lime;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.text.Element;

import javax.swing.filechooser.FileNameExtensionFilter;

public class TextEditor extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	private JTextArea lines;
	private JScrollPane scrollPane;
	private JSpinner fontSizeSpinner;
	private JLabel fontLabel;
	private JComboBox<String> fontBox;

	private String currentFile = "";

	JMenuBar menuBar;
	
	JMenu fileMenu;
	JMenuItem openItem, saveItem, exitItem;
	
	JMenu pythonMenu;
	JMenuItem runItem;
	JMenuItem guiItem;
	
	JMenu optionMenu;
	JMenu appearanceMenu;
	JMenuItem lightModeItem, darkModeItem;
	

	TextEditor() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Lime Editor 1.0");
		this.setSize(500, 500);
		this.setLayout(new FlowLayout());
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("Arial", Font.PLAIN, 20));
		textArea.setTabSize(1);
		textArea.setAlignmentY(1);
		textArea.setAutoscrolls(true);
		
	     lines = new JTextArea("1");
	      lines.setBackground(Color.LIGHT_GRAY);
	      lines.setEditable(false);
	      lines.setFont(new Font("Arial", Font.PLAIN, 20));
		
	    textArea.getDocument().addDocumentListener(new DocumentListener() {
	         public String getText() {
	            int caretPosition = textArea.getDocument().getLength();
	            Element root = textArea.getDocument().getDefaultRootElement();
	            String text = "1" + System.getProperty("line.separator");
	               for(int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
	                  text += i + System.getProperty("line.separator");
	               }
	            return text;
	         }
	         @Override
	         public void changedUpdate(DocumentEvent de) {
	            lines.setText(getText());
	         }
	         @Override
	         public void insertUpdate(DocumentEvent de) {
	            lines.setText(getText());
	         }
	         @Override
	         public void removeUpdate(DocumentEvent de) {
	            lines.setText(getText());
	         }
	      });
		
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(450, 450));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setRowHeaderView(lines);


		fontLabel = new JLabel("Font: ");

		fontSizeSpinner = new JSpinner();
		fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
		fontSizeSpinner.setValue(20);
		fontSizeSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue()));
				lines.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue()));
			}

		});

		String[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		fontBox = new JComboBox<String>(availableFonts);
		fontBox.addActionListener(this);
		fontBox.setSelectedItem("Arial");

		// ------------- menu

		menuBar = new JMenuBar();

		// File
		fileMenu = new JMenu("File");
		openItem = new JMenuItem("Open");
		saveItem = new JMenuItem("Save");
		exitItem = new JMenuItem("Exit");

		// Python
		pythonMenu = new JMenu("Python");
		runItem = new JMenuItem("Run");
		guiItem = new JMenuItem("Run as GUI");
		
		// Options
		optionMenu = new JMenu("Options");
		appearanceMenu = new JMenu("Appearance");
		
		darkModeItem = new JMenuItem("Dark Theme");
		lightModeItem = new JMenuItem("Light Theme");

		
		openItem.addActionListener(this);
		saveItem.addActionListener(this);
		runItem.addActionListener(this);
		exitItem.addActionListener(this);
		guiItem.addActionListener(this);
		darkModeItem.addActionListener(this);
		lightModeItem.addActionListener(this);
		
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(exitItem);

		pythonMenu.add(runItem);
		pythonMenu.add(guiItem);
		
		appearanceMenu.add(lightModeItem);
		appearanceMenu.add(darkModeItem);
		optionMenu.add(appearanceMenu);
		
		menuBar.add(fileMenu);
		menuBar.add(pythonMenu);
		menuBar.add(optionMenu);

		// ------------- /menu

		textArea.setBackground(new Color(200, 200, 200));
		textArea.setForeground(new Color(50, 50, 50));
		textArea.setCaretColor(textArea.getForeground());
		
		menuBar.setBackground(new Color(200, 200, 200));
		for(Component c : menuBar.getComponents()) {
			c.setBackground(new Color(200, 200, 200));
			c.setForeground(new Color(50, 50, 50));
		}
		
		scrollPane.getVerticalScrollBar().setBackground(new Color(200, 200, 200));
		scrollPane.getVerticalScrollBar().setForeground(new Color(50, 50, 50));
		
		this.setJMenuBar(menuBar);
		this.add(fontLabel);
		this.add(fontSizeSpinner);
		this.add(fontBox);
		this.add(scrollPane);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == fontBox) {
			textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
		}

		if (e.getSource() == openItem) {

			textArea.setText("");

			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("."));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Files", "py");
			chooser.setFileFilter(filter);

			int response = chooser.showOpenDialog(null);

			if (response == JFileChooser.APPROVE_OPTION) {
				File file = new File(chooser.getSelectedFile().getAbsolutePath());
				currentFile = chooser.getSelectedFile().getAbsolutePath();
				this.setTitle("Lime Python Editor Beta : " + chooser.getSelectedFile().getName());
				Scanner fileIn = null;

				try {
					fileIn = new Scanner(file);
					if (file.isFile()) {
						while (fileIn.hasNextLine()) {
							String line = fileIn.nextLine() + "\n";
							textArea.append(line);
						}
					}
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					fileIn.close();
				}
			}
		}
		if (e.getSource() == saveItem) {
			save();
		}
		if(e.getSource() == guiItem) {
			if(!currentFile.equals(""))
				save();
			
			String command = "python " + currentFile;
			System.out.println(command);
			try {
				Runtime.getRuntime().exec(new String[] {"python", currentFile });
				

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (e.getSource() == runItem) {
			if(!currentFile.equals(""))
				save();
			
			String command = "python " + currentFile;
			System.out.println(command);
			try {
				Runtime.getRuntime().exec(new String[] { "cmd", "/K", "Start", "python", currentFile });
				

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		// Themes 
		
		if(e.getSource() == darkModeItem) {
			textArea.setBackground(new Color(50, 50, 50));
			textArea.setForeground(new Color(200, 200, 200));
			textArea.setCaretColor(textArea.getForeground());
			
			menuBar.setBackground(new Color(50, 50, 50));
			for(Component c : menuBar.getComponents()) {
				c.setBackground(new Color(50, 50, 50));
				c.setForeground(new Color(200, 200, 200));
			}
			
			scrollPane.getVerticalScrollBar().setBackground(new Color(50, 50, 50));
			scrollPane.getVerticalScrollBar().setForeground(new Color(200, 200, 200));

		} 
		if (e.getSource() == lightModeItem) {
			textArea.setBackground(new Color(200, 200, 200));
			textArea.setForeground(new Color(50, 50, 50));
			textArea.setCaretColor(textArea.getForeground());
			
			menuBar.setBackground(new Color(200, 200, 200));
			for(Component c : menuBar.getComponents()) {
				c.setBackground(new Color(200, 200, 200));
				c.setForeground(new Color(50, 50, 50));
			}
			
			scrollPane.getVerticalScrollBar().setBackground(new Color(200, 200, 200));
			scrollPane.getVerticalScrollBar().setForeground(new Color(50, 50, 50));

		}
		
		if (e.getSource() == exitItem) {
			System.exit(0);
		}
	}

	private void save() {
		File file;
		PrintWriter fileOut = null;
		if(currentFile.equals("")) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("."));

			int response = chooser.showSaveDialog(null);

			if(response == JFileChooser.APPROVE_OPTION) {
				
				try {
					file = new File(chooser.getSelectedFile().getAbsolutePath());
					fileOut = new PrintWriter(file);
					fileOut.println(textArea.getText());
					

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					currentFile = chooser.getSelectedFile().getAbsolutePath();
					fileOut.close();
				}
			}
		} else {
			try {
				file = new File(currentFile);
				fileOut = new PrintWriter(file);
				fileOut.println(textArea.getText());
			

			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				fileOut.close();
			}
		}
	}
}
