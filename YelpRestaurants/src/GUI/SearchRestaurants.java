package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;

import org.apache.lucene.queryparser.classic.ParseException;

import lucene.Caller;

import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class SearchRestaurants{

	private JFrame frmRestaurants;
	private JTextField txtState;
	private JPanel panel_1;
	private JTextField state;
	private JPanel panel_2;
	private JTextField txtCity;
	private JTextField city;
	private JPanel panel_3;
	private JTextField txtAddress;
	private JTextField address;
	private JPanel panel_4;
	private JPanel panel_5;
	private JTextField txtText;
	private JTextField text;
	private JPanel panel_6;
	private JRadioButton rdbtnSortBy;
	private JRadioButton rdbtnSortByStars;
	private JPanel panel_7;
	private JCheckBox chckbxDifferentLocations;
	private JCheckBox chckbxDifferentStarNumbers;
	private JPanel panel_8;
	private JButton btnS;
	private JButton btnNew;
	private JRadioButton rdbtnNoSort;
	private JPanel panel_9;
	private JCheckBox chckbxDifferentAddress;

	/**
	 * Launch the application.
	 */
	public void setRestaurants() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchRestaurants window = new SearchRestaurants();
					window.frmRestaurants.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SearchRestaurants() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private int selectSort() {
		if (rdbtnSortBy.isSelected()) {
			return 1;
		}
		else if (rdbtnSortByStars.isSelected()) {
			return 2;
		}
		return 3;
	}
	
	boolean geographicalSearch() {
		if (!state.getText().equals("")|| !city.getText().equals("") ||!address.getText().equals("")) {
			return true;
		}
		return false;
	}
	
	boolean textSearch() {
		if (!text.getText().equals("")) {
			return true;
		}
		return false;
	}
	
	private void initialize() {
		ButtonGroup group = new ButtonGroup();
		frmRestaurants = new JFrame();
		frmRestaurants.setTitle("Restaurants");
		frmRestaurants.setBounds(100, 100, 800, 300);
		frmRestaurants.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmRestaurants.getContentPane().add(panel, BorderLayout.CENTER);
		
		panel_4 = new JPanel();
		panel.add(panel_4);
		
		panel_1 = new JPanel();
		panel_4.add(panel_1);
		
		txtState = new JTextField();
		txtState.setEditable(false);
		panel_1.add(txtState);
		txtState.setEnabled(false);
		txtState.setText("State : ");
		txtState.setColumns(6);
		
		state = new JTextField();
		panel_1.add(state);
		state.setColumns(10);
		
		panel_2 = new JPanel();
		panel_4.add(panel_2);
		
		txtCity = new JTextField();
		txtCity.setEditable(false);
		txtCity.setEnabled(false);
		txtCity.setText("City : ");
		panel_2.add(txtCity);
		txtCity.setColumns(6);
		
		city = new JTextField();
		panel_2.add(city);
		city.setColumns(10);
		
		panel_3 = new JPanel();
		panel_4.add(panel_3);
		
		txtAddress = new JTextField();
		txtAddress.setEditable(false);
		txtAddress.setEnabled(false);
		txtAddress.setText("Address : ");
		panel_3.add(txtAddress);
		txtAddress.setColumns(8);
		
		address = new JTextField();
		panel_3.add(address);
		address.setColumns(10);
		
		panel_5 = new JPanel();
		panel.add(panel_5);
		
		txtText = new JTextField();
		txtText.setEditable(false);
		txtText.setEnabled(false);
		txtText.setText("Text : ");
		panel_5.add(txtText);
		txtText.setColumns(6);
		
		text = new JTextField();
		panel_5.add(text);
		text.setColumns(10);
		
		panel_9 = new JPanel();
		panel.add(panel_9);
		
		panel_6 = new JPanel();
		panel.add(panel_6);
		
		rdbtnSortBy = new JRadioButton("Sort By Review Count");
		panel_6.add(rdbtnSortBy);
		group.add(rdbtnSortBy);
		
		rdbtnSortByStars = new JRadioButton("Sort By Stars");
		panel_6.add(rdbtnSortByStars);
		group.add(rdbtnSortByStars);
		
		rdbtnNoSort = new JRadioButton("No Sort");
		panel_6.add(rdbtnNoSort);
		group.add(rdbtnNoSort);
		
		panel_7 = new JPanel();
		panel.add(panel_7);
		
		chckbxDifferentLocations = new JCheckBox("Different Cities");
		panel_7.add(chckbxDifferentLocations);
		
		chckbxDifferentStarNumbers = new JCheckBox("Different Star Numbers");
		panel_7.add(chckbxDifferentStarNumbers);
		
		chckbxDifferentAddress = new JCheckBox("Different Address");
		panel_7.add(chckbxDifferentAddress);
		
		
		panel_8 = new JPanel();
		frmRestaurants.getContentPane().add(panel_8, BorderLayout.NORTH);
		
		btnS = new JButton("Search");
		btnS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Caller call = new Caller();
				try {
					call.callRestaurants(state.getText(),city.getText(),address.getText(),
							text.getText(),selectSort(),chckbxDifferentLocations.isSelected(),
							chckbxDifferentStarNumbers.isSelected(),chckbxDifferentAddress.isSelected(),geographicalSearch(),
							textSearch());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		panel_8.add(btnS);
		
		btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmRestaurants.setVisible(false);
				frmRestaurants.dispose();
				SearchYelp yelp = new SearchYelp();
		        yelp.search();
			}
		});
		panel_8.add(btnNew);
	}

}
