package qualteh.com.androidadminmap;

/**
 * Created by Virgil Tanase on 05.05.2016.
 */
public class JsonBuilder {


    public static String buildJson(String buildType,
                                 String settlement,
                                 String supSettlement,
                                 String county,
                                 double latitude,
                                 double longitude,
                                 String concatenated,
                                 String newConcatenated,
                                 String newWno,
                                 boolean edited,
                                 String wno,
                                 boolean deleted){

        String message="{";

        message+="\"buildType\"";
        message+=":";
        message+="\""+buildType+"\"";

        message+="\"settlement\"";
        message+=":";
        message+="\""+settlement+"\"";

        message+="\"supSettlement\"";
        message+=":";
        message+="\""+supSettlement+"\"";

        message+="\"county\"";
        message+=":";
        message+="\""+county+"\"";

        message+="\"latitude\"";
        message+=":";
        message+=""+latitude+"";

        message+="\"longitude\"";
        message+=":";
        message+=""+longitude+"";

        message+="\"concatenated\"";
        message+=":";
        message+="\""+concatenated+"\"";

        message+="\"newConcatenated\"";
        message+=":";
        message+="\""+newConcatenated+"\"";

        message+="\"newWno\"";
        message+=":";
        message+="\""+newWno+"\"";

        int myBoolean = (edited) ? 1 : 0;

        message+="\"edited\"";
        message+=":";
        message+=""+myBoolean+"";

        message+="\"wno\"";
        message+=":";
        message+="\""+wno+"\"";

        myBoolean = (deleted) ? 1 : 0;

        message+="\"deleted\"";
        message+=":";
        message+=""+myBoolean+"";

        message +="}";
        return message;

    }


}
