package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JRadioButton;

import lucene.Caller;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SearchYelp{

	private JFrame frmSearchFor;
	/**
	 * Launch the application.
	 */
	public void search() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchYelp window = new SearchYelp();
					window.frmSearchFor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SearchYelp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		ButtonGroup group = new ButtonGroup();
		frmSearchFor = new JFrame();
		frmSearchFor.setTitle("Search for");
		frmSearchFor.setBounds(100, 100, 450, 300);
		frmSearchFor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel_1 = new JPanel();
		frmSearchFor.getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		JRadioButton chooseRestaurants = new JRadioButton("Restaurants");
		group.add(chooseRestaurants);
		
		JRadioButton chooseReviews = new JRadioButton("Reviews");
		group.add(chooseReviews);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Caller call = new Caller();
				if(chooseRestaurants.isSelected()) {
					call.searchFor(1);
				}
				else if (chooseReviews.isSelected()) {
					call.searchFor(2);
				}
				frmSearchFor.setVisible(false);
				frmSearchFor.dispose();
				
			}
		});
		panel_1.add(btnOk);
		
		JPanel panel = new JPanel();
		frmSearchFor.getContentPane().add(panel, BorderLayout.NORTH);
		
		JPanel panel_2 = new JPanel();
		frmSearchFor.getContentPane().add(panel_2, BorderLayout.CENTER);
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3);
		
		panel_3.add(chooseRestaurants);
		
		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4);
		
		panel_4.add(chooseReviews);
	}

}
