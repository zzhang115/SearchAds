package ads;

import org.apache.lucene.util.Version;

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
}
