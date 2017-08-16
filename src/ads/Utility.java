package ads;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzc on 8/14/17.
 */
public class Utility {
    private static final Version LUCENE_VERSION = Version.LUCENE_40;
    private static String stopWords = "a,able,about,across,after,all,almost,also,am,among,an,and,any,are,as,at,be,because,been,but,by,can,cannot,could,dear,did,do,does,either,else,ever,every,for,from,get,got,had,has,have,he,her,hers,him,his,how,however,i,if,in,into,is,it,its,just,least,let,like,likely,may,me,might,most,must,my,neither,no,nor,not,of,off,often,on,only,or,other,our,own,rather,said,say,says,she,should,since,so,some,than,that,the,their,them,then,there,these,they,this,tis,to,too,twas,us,wants,was,we,were,what,when,where,which,while,who,whom,why,will,with,would,yet,you,your";

    public static String strJoin(List<String> list, String spaceMark) {
        StringBuffer joinedStr = new StringBuffer();
        for (String str : list) {
            joinedStr.append(str + ",");
        }
        joinedStr.deleteCharAt(joinedStr.length() - 1);
        return joinedStr.toString();
    }

    // remove stop words, tokenize, stem in keywords
    public static List<String> cleanedUselessTokens(String keyWords) {
        StringReader reader = new StringReader(keyWords.toLowerCase());
        Tokenizer tokenizer = new StandardTokenizer(LUCENE_VERSION, reader);
        TokenStream tokenStream = new StandardFilter(LUCENE_VERSION, tokenizer);
        tokenStream = new StopFilter(LUCENE_VERSION, tokenStream, getStopWords(stopWords));
        tokenStream = new KStemFilter(tokenStream);
        StringBuilder stringBuilder = new StringBuilder();
        CharTermAttribute charTermAttribute = tokenizer.addAttribute(CharTermAttribute.class);
        List<String> tokens = new ArrayList<String>();

        try {
            tokenStream.reset();
            while(tokenStream.incrementToken()) {
                String term = charTermAttribute.toString();
                tokens.add(term);
                stringBuilder.append(term + " ");
            }
            tokenStream.end();
            tokenStream.close();
            tokenizer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(stringBuilder.toString());
        return tokens;
    }

    private static CharArraySet getStopWords(String stopWords) {
        List<String> stopWordsList = new ArrayList<String>();
        for (String stopWord : stopWords.split(",")) {
            stopWordsList.add(stopWord);
        }
        return new CharArraySet(LUCENE_VERSION, stopWordsList, true);
    }
}
