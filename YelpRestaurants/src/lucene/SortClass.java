package lucene;

import java.io.IOException;
import java.sql.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortedNumericSortField;
import org.apache.lucene.search.TopDocs;

public class SortClass extends SearchMain {
	
	private static DocumentAdd add = new DocumentAdd();

	public static void sortLong(String field,String sortedNumeric,int type) throws IOException {
		Sort sort = new Sort(new SortedNumericSortField(sortedNumeric, SortField.Type.LONG, true));
		IndexReader reader = DirectoryReader.open(indexSort);
        IndexSearcher searcher = new IndexSearcher(reader);
        Query query = new MatchAllDocsQuery();
        TopDocs docs = searcher.search(query, 2000000, sort);
        ScoreDoc[] hits = docs.scoreDocs;
        IndexWriter w = new IndexWriter(indexFinal, configFinal);
        for(int i=0;i<hits.length;++i) {
        	int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            if (type == 1) {
            	add.addRestaurantDoc(w,d.get("id"),d.get("name"),d.get("address"),d.get("city"),d.get("state"),
            			Float.parseFloat(d.get("latitude")),Float.parseFloat(d.get("longtitude")),
            			Integer.parseInt(d.get("review_count")),Float.parseFloat(d.get("stars")));
            }
            else if (type == 2) {
            	Date date = Date.valueOf(d.get("date").substring(0,4)+"-"+d.get("date").substring(4,6)+"-"+d.get("date").substring(6,8));
            	add.addDoc(w, d.get("text"), d.get("business"),date,Integer.parseInt(d.get("useful")),Integer.parseInt(d.get("stars")));
            }
        }
        w.close();
	}
	
	public static void sortChoice(String title,String field1,String field2,int type,int choice) throws IOException {
		if (choice == 1 ) {
    		sortLong(title,field1,type);
    	}
    	else if (choice == 2) {
    		sortLong(title,field2,type);
    	}
    	else if (choice == 4){
    		sortLong(title,"stars",type);
    	}
    	else {
    		sortLong(title, "", type);
    	}
	}
	
	public void sort(int sortChoice,int choice) throws IOException {
		if (sortChoice == 1) {
        	sortChoice("name", "review_count", "stars", sortChoice,choice);
        }
        
        else if (sortChoice == 2) {
        	sortChoice("text", "useful", "date", sortChoice,choice);
        }
	}

}
