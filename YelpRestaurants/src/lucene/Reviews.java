package lucene;

import java.io.IOException;
import java.sql.Date;

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
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import org.apache.lucene.search.highlight.*;

public class Reviews extends Caller {
	
	private int hitsPerPageRestaurant = 20000;
	private int hitsPerPageReview = 2000000;
	private String name;
	private String keywords;
	private String city;
	private String address;
	private boolean nameSearch;
	private boolean keywordSearch;
	private boolean citySearch;
	private boolean addressSearch;
	private DocumentAdd add = new DocumentAdd();
	
	public Reviews(String name, String keywords,String city,String address, boolean nameSearch, boolean keywordSearch,
			boolean citySearch,boolean stateSearch) {
		this.name = name;
		this.keywords = keywords;
		this.city = city;
		this.address = address;
		this.nameSearch = nameSearch;
		this.keywordSearch = keywordSearch;
		this.citySearch = citySearch;
		this.addressSearch = stateSearch;
	}
	
	public void searchReview(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {
    	if (nameSearch && keywordSearch) {
    		searchAllReview(args);
    	}
    	else if (nameSearch) {
    		if (citySearch||addressSearch) {
    			Directory indexTemp = new RAMDirectory();
    			IndexWriterConfig configTemp = new IndexWriterConfig(analyzer);
    			Restaurants restaurants = new Restaurants("",city,address,"",true,false);
    			restaurants.searchGeographicalPosition(indexRestaurants,indexTemp,configTemp);
    			searchReviewRestaurant(indexReview,indexTemp,indexSubset,configSubset);
    		}
    		else {
    			searchReviewRestaurant(indexReview,indexRestaurants,indexSubset,configSubset);
    		}
         }
    	else if (keywordSearch) {
    		if (citySearch||addressSearch) {
    			geographicReviewSearch(args);
    		}
    		else {
    			searchReviewKeyword(args,indexReview,indexSubset, configSubset);
    		}
    	}
    	else if (citySearch||addressSearch) {
			geographicReviewSearch(args);
    	}
    	
	}
	
	public void geographicReviewSearch(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {
		Directory index = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		Restaurants restaurants = new Restaurants("",city,address,"",true,false);
		restaurants.searchGeographicalPosition(indexRestaurants,index,config);
		Query q = null;
		Highlighter highlighter = null;
		if (keywordSearch) {
			String querystr = args.length > 0 ? args[0] : keywords;
			q = new QueryParser("text", analyzer).parse(querystr);
		}
		
		IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        Query query = new MatchAllDocsQuery();
        TopDocs docs = searcher.search(query, 2000000);
        ScoreDoc[] hits = docs.scoreDocs;
		IndexWriter w = new IndexWriter(indexSubset, configSubset);
		for(int i=0;i<hits.length;++i) {
        	int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            IndexReader reader2 = DirectoryReader.open(indexReview);
	        IndexSearcher searcher2 = new IndexSearcher(reader2);
	        Query query2 = new TermQuery(new Term("business",d.get("id")));
	        TopDocs docs2;
	        if (keywordSearch) {
	        	BooleanQuery booleanQuery = new BooleanQuery.Builder()
	        			.add(query2, Occur.MUST)
	        			.add(q, Occur.MUST).build();
	        	SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
	        	highlighter = new Highlighter(htmlFormatter,new QueryScorer(q));
	        	docs2 = searcher2.search(booleanQuery, 2000000);
	        }
	        else {
	        	docs2 = searcher2.search(query2, 2000000);
	        }
	        ScoreDoc[] hits2 = docs2.scoreDocs;
	        for(int j=0;j<hits2.length;++j) {
	        	int docId2 = hits2[j].doc;
	        	Document d2 = searcher2.doc(docId2);
	        	Date date = Date.valueOf(d2.get("date").substring(0,4)+"-"+d2.get("date").substring(4,6)+"-"+d2.get("date").substring(6,8));
	        	String text;
	        	if (keywordSearch) {
	        		text = highlighter.getBestFragment(analyzer, "text", d2.get("text"));
	        	}
	        	else {
	        		text = d2.get("text");
	        	}
            	add.addDoc(w, text, d2.get("business"),date,Integer.parseInt(d2.get("useful")),Integer.parseInt(d2.get("stars")));
	        }
        }
		w.close();
	}
	
	public void searchReviewRestaurant(Directory indexRead,Directory indexReadRestaurants,Directory indexWrite,IndexWriterConfig configWrite) throws IOException {
		Query qName = new TermQuery(new Term("name",name));
		IndexReader reader = DirectoryReader.open(indexReadRestaurants);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(qName, hitsPerPageRestaurant);
        ScoreDoc[] hits = docs.scoreDocs;
        IndexWriter w = new IndexWriter(indexWrite, configWrite);
        IndexWriter w1 = null;
        if (!keywordSearch) {
        	w1 = new IndexWriter(indexMinMax, configMinMax);
        }
        for (int k = 0;k < hits.length; ++k) {
        	int docId = hits[k].doc;
            Document d = searcher.doc(docId);
            Query q2 = new TermQuery(new Term("business",d.get("id")));
            IndexReader reader2 = DirectoryReader.open(indexRead);
            IndexSearcher searcher2 = new IndexSearcher(reader2);
            TopDocs docs2 = searcher2.search(q2, hitsPerPageReview);
            ScoreDoc[] hits2 = docs2.scoreDocs;
            for(int i=0;i<hits2.length;++i) {
            	int docId2 = hits2[i].doc;
                Document d2 = searcher2.doc(docId2);
                Date date = Date.valueOf(d2.get("date").substring(0,4)+"-"+d2.get("date").substring(4,6)+"-"+d2.get("date").substring(6,8));
                add.addDoc(w, d2.get("text"), d2.get("business"),date,Integer.parseInt(d2.get("useful")),Integer.parseInt(d2.get("stars")));
                if (!keywordSearch) {
                	add.addDoc(w1, d2.get("text"), d2.get("business"),date,Integer.parseInt(d2.get("useful")),Integer.parseInt(d2.get("stars")));
                }
            }
        }
        
        w.close();
        if (!keywordSearch) {
        	w1.close();
        }
        
	}
	
	public void searchReviewKeyword(String[]args,Directory indexRead,Directory indexWrite,IndexWriterConfig configWrite) throws IOException, ParseException, InvalidTokenOffsetsException {
		String querystr = args.length > 0 ? args[0] : keywords;
		Query q = new QueryParser("text", analyzer).parse(querystr);
		IndexReader reader = DirectoryReader.open(indexRead);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPageReview);
        ScoreDoc[] hits = docs.scoreDocs;
        
        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
        Highlighter highlighter = new Highlighter(htmlFormatter,new QueryScorer(q));
        
        IndexWriter w = new IndexWriter(indexWrite, configWrite);
        IndexWriter w1 = new IndexWriter(indexMinMax, configMinMax);
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            String frag = highlighter.getBestFragment(analyzer, "text", d.get("text"));
            Date date = Date.valueOf(d.get("date").substring(0,4)+"-"+d.get("date").substring(4,6)+"-"+d.get("date").substring(6,8));
            add.addDoc(w, frag, d.get("business"),date,Integer.parseInt(d.get("useful")),Integer.parseInt(d.get("stars")));
            add.addDoc(w1, frag, d.get("business"),date,Integer.parseInt(d.get("useful")),Integer.parseInt(d.get("stars")));

        }
        w.close();
        w1.close();
	}
	
	public void searchAllReview(String[]args) throws ParseException, IOException, InvalidTokenOffsetsException {
		
		Directory indexTemp = new RAMDirectory();
		IndexWriterConfig configTemp = new IndexWriterConfig(analyzer);
		if (citySearch||addressSearch) {
			Directory indexTemp2 = new RAMDirectory();
			IndexWriterConfig configTemp2 = new IndexWriterConfig(analyzer);
			Restaurants restaurants = new Restaurants("",city,address,"",true,false);
			restaurants.searchGeographicalPosition(indexRestaurants,indexTemp2,configTemp2);
			searchReviewRestaurant(indexReview,indexTemp2,indexTemp,configTemp);
			searchReviewKeyword(args,indexTemp,indexSubset, configSubset);
		}
		else {
			searchReviewRestaurant(indexReview,indexRestaurants,indexTemp,configTemp);
			searchReviewKeyword(args,indexTemp,indexSubset, configSubset);
		}
		
		
	}
}
