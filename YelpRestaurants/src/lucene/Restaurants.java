package lucene;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class Restaurants extends Caller {
	
	private int hitsPerPageRestaurant = 20000;
	private int hitsPerPageReview = 2000000;
	private String state;
	private String city;
	private String address;
	private String text;
	private boolean geographicalSearch;
	private boolean textSearch;	
	private DocumentAdd add = new DocumentAdd();
	
	public Restaurants(String state,String city,String address,String text,
			boolean geographicalSearch,boolean textSearch) {
		this.state = state;
		this.city = city;
		this.address = address;
		this.text = text;
		this.geographicalSearch = geographicalSearch;
		this.textSearch = textSearch;
	}
	
	public void searchRestaurant() throws NumberFormatException, IOException, ParseException {
    	
		if (geographicalSearch&&textSearch) {
			searchAll();
    	}
		else if (geographicalSearch) {
    		searchGeographicalPosition(indexRestaurants,indexSubset,configSubset);
    	}
    	else if (textSearch) {
    		searchText(indexRestaurants,indexSubset,configSubset);
    	}
    	
	}
	
	public void searchAll() throws ParseException, IOException {
		Directory indexTemp = new RAMDirectory();
		IndexWriterConfig configTemp = new IndexWriterConfig(analyzer);
		searchGeographicalPosition(indexRestaurants,indexTemp,configTemp);
        searchText(indexTemp,indexSubset,configSubset);
        
	}
	
	public void searchGeographicalPosition(Directory indexRead,Directory indexWrite,IndexWriterConfig configWrite) throws IOException {
		
		Query qState = new TermQuery(new Term("state",state));
		Query qCity = new TermQuery(new Term("city",city));
        Query qAddress = new TermQuery(new Term("address",address));
        
        BooleanQuery booleanQuery = new BooleanQuery.Builder()
        		.add(qState, getOccur(state))
        		.add(qCity, getOccur(city))
        		.add(qAddress, getOccur(address)).build();
        
        IndexReader reader = DirectoryReader.open(indexRead);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(booleanQuery, hitsPerPageRestaurant);
        ScoreDoc[] hits = docs.scoreDocs;
        IndexWriter w = new IndexWriter(indexWrite, configWrite);
        
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            add.addRestaurantDoc(w,d.get("id"),d.get("name"),d.get("address"),d.get("city"),d.get("state"),
            		Float.parseFloat(d.get("latitude")),Float.parseFloat(d.get("longtitude")),Integer.parseInt(d.get("review_count")),
            		Float.parseFloat(d.get("stars")));
            
        }
        w.close();
	}
	
	private Occur getOccur(String term) {
		if (term.equals("")) {
        	return Occur.SHOULD;
		}
		return Occur.MUST;
	}
	
	public void searchText(Directory indexRead,Directory indexWrite,IndexWriterConfig configWrite) throws ParseException, IOException {
		ArrayList <String>  business = new ArrayList<String>();
		Query q = new QueryParser("text", analyzer).parse(text);
		IndexReader reader = DirectoryReader.open(indexReview);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPageReview);
        ScoreDoc[] hits = docs.scoreDocs;
        IndexWriter w = new IndexWriter(indexWrite, configWrite);
        for (int i=0;i<hits.length;++i) {
        	int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            int size = business.size();
            int j=0;
            boolean check = true;
            while (j < size && check) {
            		if (d.get("business").equals(business.get(j))){
            			check = false;
            		}
            		else {
            			j++;
            		}
            }
            if(check) {
            	business.add(d.get("business"));
                Query q2 = new TermQuery(new Term("id",d.get("business")));
            	IndexReader reader2 = DirectoryReader.open(indexRead);
            	IndexSearcher searcher2 = new IndexSearcher(reader2);
            	TopDocs docs2 = searcher2.search(q2, hitsPerPageRestaurant);
            	ScoreDoc[] hits2 = docs2.scoreDocs;
            	 for(int k=0;k<hits2.length;++k) {
            		 int docId2 = hits2[k].doc;
                 	Document d2 = searcher2.doc(docId2);
                 	add.addRestaurantDoc(w,d2.get("id"),d2.get("name"),d2.get("address"),d2.get("city"),d2.get("state"),
                         Float.parseFloat(d2.get("latitude")),Float.parseFloat(d2.get("longtitude")),Integer.parseInt(d2.get("review_count")),
                         Float.parseFloat(d2.get("stars")));
            	 }
            	
            	
            }
        }
        w.close();
	}
}