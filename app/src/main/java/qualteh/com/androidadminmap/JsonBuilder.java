package qualteh.com.androidadminmap;

/**
 * Created by Virgil Tanase on 05.05.2016.
 */
public class JsonBuilder {


    public static String buildJsonDelete(int id){
        String message = "{";
        message+="\"_id\":"+id+",";
        message+="\"deleted\":"+1;
        message+="}";
        return message;
    }


//    {"_id":1463557467711,
//            "buildType":"scara",
//            "settlement":"Timişoara",
//            "supSettlement":"Timişoara",
//            "county":"Timiş",
//            "latitude":45.7258604408142,
//            "longitude":21.2412413876091,
//            "concatenated":"Strada Anișoara Odeanu",
//            "edited":1,
//            "wno":"",
//            "deleted":0,
//            "blockNo":"23",
//            "blockWno":"23",
//            "entranceName":"C”}

    public static String buildJsonBloc(int id, String blockNo, String blockWno, String entranceName, String buildType, String settlement, String supSettlement, String county,
                                       double latitude, double longitude, String concatenated, String v ){
        String message = "{";

        message+="\"_id\"";
        message+=":"+id+",";

        message+="\"blockNo\"";
        message+=":\""+blockNo+"\",";
        message+="\"blockWno\"";
        message+=":\""+blockWno+"\",";
        message+="\"entranceName\"";
        message+=":\""+entranceName+"\",";
        message+="\"buildType\"";
        message+=":\""+buildType+"\",";
        message+="\"settlement\"";
        message+=":\""+settlement+"\",";
        message+="\"supSettlement\"";
        message+=":\""+supSettlement+"\",";
        message+="\"county\"";
        message+=":\""+county+"\",";
        message+="\"latitude\"";
        message+=":"+latitude+",";
        message+="\"longitude\"";
        message+=":"+longitude+",";
        message+="\"concatenated\"";
        message+=":\""+concatenated+"\",";
        message+="\"__v\"";
        message+=":"+v+",";
        message+="\"geoLocation\"";
        message+=":["+longitude+" , "+latitude+" ]";
        message+="}";
        return message;
    }

    public static String buildJsonCasa(){
        String message="";
        return message;
    }

    public static String buildJsonPoi(){
        String message="";
        return message;
    }

    public static String buildJson(String id,
                                    String buildType,
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
