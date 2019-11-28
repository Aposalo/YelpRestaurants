package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import lucene.Caller;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class SearchReview{

	private JFrame frmReviews;
	private JTextField txtRestaurantName;
	private JTextField restaurantName;
	private JTextField txtKeywords;
	private JTextField keywords;
	private JRadioButton radioButton;
	private JRadioButton radioButton_1;
	private JRadioButton radioButton_3;
	private JRadioButton radioButton_2;
	private JTextField txtCity;
	private JTextField city;
	private JTextField txtAddress;
	private JTextField address;

	/**
	 * Launch the application.
	 */
	public void setReviews() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchReview window = new SearchReview();
					window.frmReviews.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SearchReview() {
		initialize();
	}
	
	private int selectSort() {
		
		if(radioButton.isSelected()) {
			return 1;
		}
		
		else if (radioButton_1.isSelected()) {
			return 2;
		}
		
		else if (radioButton_3.isSelected()) {
			return 4;
		}
		
		return 3;
	}
	
	private boolean searchString(String search) {
		if (!search.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		ButtonGroup group = new ButtonGroup();
		frmReviews = new JFrame();
		frmReviews.setTitle("Reviews");
		frmReviews.setBounds(100, 100, 450, 300);
		frmReviews.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmReviews.getContentPane().add(panel, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		
		txtRestaurantName = new JTextField();
		txtRestaurantName.setText("Restaurant Name : ");
		txtRestaurantName.setEditable(false);
		txtRestaurantName.setEnabled(false);
		panel_1.add(txtRestaurantName);
		txtRestaurantName.setColumns(12);
		
		restaurantName = new JTextField();
		panel_1.add(restaurantName);
		restaurantName.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		
		txtKeywords = new JTextField();
		txtKeywords.setEnabled(false);
		txtKeywords.setEditable(false);
		txtKeywords.setText("Keywords :");
		panel_2.add(txtKeywords);
		txtKeywords.setColumns(8);
		
		keywords = new JTextField();
		panel_2.add(keywords);
		keywords.setColumns(10);
		
		JPanel panel_6 = new JPanel();
		panel.add(panel_6);
		
		txtCity = new JTextField();
		txtCity.setText("City :");
		txtCity.setEnabled(false);
		txtCity.setEditable(false);
		panel_6.add(txtCity);
		txtCity.setColumns(5);
		
		city = new JTextField();
		panel_6.add(city);
		city.setColumns(10);
		
		JPanel panel_7 = new JPanel();
		panel.add(panel_7);
		
		txtAddress = new JTextField();
		txtAddress.setEnabled(false);
		txtAddress.setEditable(false);
		txtAddress.setText("Address : ");
		panel_7.add(txtAddress);
		txtAddress.setColumns(8);
		
		address = new JTextField();
		panel_7.add(address);
		address.setColumns(10);
		
		JPanel panel_4 = new JPanel();
		panel.add(panel_4);
		
		radioButton = new JRadioButton("Sort By Useful");
		panel_4.add(radioButton);
		group.add(radioButton);
		
		radioButton_1 = new JRadioButton("Sort By Date");
		panel_4.add(radioButton_1);
		group.add(radioButton_1);
		
		radioButton_3 = new JRadioButton("Sort By Stars");
		panel_4.add(radioButton_3);
		group.add(radioButton_3);
		
		radioButton_2 = new JRadioButton("No Sort");
		panel_4.add(radioButton_2);
		group.add(radioButton_2);
		
		JPanel panel_5 = new JPanel();
		panel.add(panel_5);
		
		JCheckBox checkBox = new JCheckBox("Good And Bad Reviews");
		panel_5.add(checkBox);
		
		JCheckBox checkBox_1 = new JCheckBox("Old And New Reviews");
		panel_5.add(checkBox_1);
		
		JPanel panel_3 = new JPanel();
		frmReviews.getContentPane().add(panel_3, BorderLayout.NORTH);
		
		JButton button = new JButton("Search");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Caller call = new Caller();
				try {
					call.callReviews(restaurantName.getText(),keywords.getText(),city.getText(),address.getText(),selectSort(),
							checkBox.isSelected(),checkBox_1.isSelected(),searchString(restaurantName.getText()),
							searchString(keywords.getText()),searchString(city.getText()),searchString(address.getText()));
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidTokenOffsetsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		panel_3.add(button);
		
		JButton button_1 = new JButton("New");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmReviews.setVisible(false);
				frmReviews.dispose();
				SearchYelp yelp = new SearchYelp();
		        yelp.search();
			}
		});
		panel_3.add(button_1);
	}

}
