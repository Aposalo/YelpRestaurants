package GUI;

import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import lucene.SearchMain;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ResultsReviews extends SearchMain {

	private JFrame frmResults;
	private JTable table;
	private JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public void result() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ResultsReviews window = new ResultsReviews();
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
	public ResultsReviews() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frmResults = new JFrame();
		frmResults.setTitle("Results");
		frmResults.setBounds(100, 100, 1184, 300);
		frmResults.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"id", "text", "date", "stars", "useful", "name", "state", "city", "address"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(37);
		table.getColumnModel().getColumn(1).setPreferredWidth(550);
		table.getColumnModel().getColumn(2).setPreferredWidth(108);
		table.getColumnModel().getColumn(3).setPreferredWidth(40);
		table.getColumnModel().getColumn(4).setPreferredWidth(41);
		table.getColumnModel().getColumn(5).setPreferredWidth(125);
		table.getColumnModel().getColumn(6).setPreferredWidth(47);
		table.getColumnModel().getColumn(7).setPreferredWidth(110);
		table.getColumnModel().getColumn(8).setPreferredWidth(150);
		IndexReader reader = DirectoryReader.open(indexFinal);
        IndexSearcher searcher = new IndexSearcher(reader);
        Query query = new MatchAllDocsQuery();
        TopDocs docs = searcher.search(query, 200000);
        ScoreDoc[] hits = docs.scoreDocs;
		for (int i=0;i<hits.length;++i) {
			int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            
            IndexReader reader2 = DirectoryReader.open(indexRestaurants);
            IndexSearcher searcher2 = new IndexSearcher(reader2);
            Query query2 = new TermQuery(new Term("id",d.get("business")));
            TopDocs docs2 = searcher2.search(query2, 1);
            ScoreDoc[] hits2 = docs2.scoreDocs;
            int docId2 = hits2[0].doc;
            Document d2 = searcher2.doc(docId2);
            
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			String date =(d.get("date").substring(0,4)+"-"+d.get("date").substring(4,6)+"-"+d.get("date").substring(6,8));
			String text = "<HTML>" + d.get("text") + "</HTML>";
			model.addRow(new Object[]{i+1,text,date,d.get("stars"),d.get("useful"),d2.get("name"),d2.get("state"),d2.get("city"),d2.get("address")});
		}
		frmResults.getContentPane().add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		JPanel panel = new JPanel();
		frmResults.getContentPane().add(panel, BorderLayout.SOUTH);
		
		btnNewButton = new JButton("Close");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmResults.setVisible(false);
				frmResults.dispose();
			}
		});
		panel.add(btnNewButton);
		
	}

}
