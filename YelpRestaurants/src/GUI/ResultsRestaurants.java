package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import lucene.SearchMain;

public class ResultsRestaurants extends SearchMain {

	private JFrame frmResults;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public void result() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ResultsRestaurants window = new ResultsRestaurants();
					window.frmResults.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public ResultsRestaurants() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frmResults = new JFrame();
		frmResults.setTitle("Results");
		frmResults.setBounds(100, 100, 640, 300);
		frmResults.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"id", "name", "address", "city", "state", "review_count", "stars"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(43);
		table.getColumnModel().getColumn(1).setPreferredWidth(160);
		table.getColumnModel().getColumn(2).setPreferredWidth(140);
		table.getColumnModel().getColumn(3).setPreferredWidth(92);
		table.getColumnModel().getColumn(4).setPreferredWidth(43);
		table.getColumnModel().getColumn(5).setPreferredWidth(80);
		table.getColumnModel().getColumn(6).setPreferredWidth(40);
		IndexReader reader = DirectoryReader.open(indexFinal);
        IndexSearcher searcher = new IndexSearcher(reader);
        Query query = new MatchAllDocsQuery();
        TopDocs docs = searcher.search(query, 200000);
        ScoreDoc[] hits = docs.scoreDocs;
		for (int i=0;i<hits.length;++i) {
			int docId = hits[i].doc;
            Document d = searcher.doc(docId);
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.addRow(new Object[]{i+1,d.get("name"),d.get("address"),d.get("city"),d.get("state"),d.get("review_count"),d.get("stars")});
		}
		frmResults.getContentPane().add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		JPanel panel = new JPanel();
		frmResults.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Close");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmResults.setVisible(false);
				frmResults.dispose();
			}
		});
		panel.add(btnNewButton);
	}

}
