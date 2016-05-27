package qualteh.com.androidadminmap.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Virgil Tanase on 18.05.2016.
 */
public class JsonModel {

    @SerializedName( "_id" ) long id;
    @SerializedName( "buildType" ) String buildType;
    @SerializedName( "settlement" ) String settlement;
    @SerializedName( "supSettlement" ) String supSettlement;
    @SerializedName( "county" ) String county;
    @SerializedName( "latitude" ) double latitude;
    @SerializedName( "longitude" ) double longitude;
    @SerializedName( "concatenated" ) String concatenated;
    @SerializedName( "edited" ) int edited;
    @SerializedName( "wno" ) String wno;
    @SerializedName( "deleted" ) int deleted;
    @SerializedName( "blockNo" ) String blockNo;
    @SerializedName( "blockWno" ) String blockWno;
    @SerializedName( "entranceName" ) String entranceName;

    public JsonModel(long id, String buildType, String settlement, String supSettlement, String county, double latitude, double longitude, String concatenated, int edited,
                     String wno, int deleted, String blockNo, String blockWno, String entranceName){
        this.id=id;
        this.buildType=buildType;
        this.settlement=settlement;
        this.supSettlement=supSettlement;
        this.county=county;
        this.latitude=latitude;
        this.longitude=longitude;
        this.concatenated=concatenated;
        this.edited=edited;
        this.wno=wno;
        this.deleted=deleted;
        this.blockNo=blockNo;
        this.blockWno=blockWno;
        this.entranceName=entranceName;
    }

    public String getBuildType () {
        return buildType;
    }

    public void setBuildType ( String buildType ) {
        this.buildType = buildType;
    }

    public String getSettlement () {
        return settlement;
    }

    public void setSettlement ( String settlement ) {
        this.settlement = settlement;
    }

    public String getSupSettlement () {
        return supSettlement;
    }

    public void setSupSettlement ( String supSettlement ) {
        this.supSettlement = supSettlement;
    }

    public String getCounty () {
        return county;
    }

    public void setCounty ( String county ) {
        this.county = county;
    }

    public double getLatitude () {
        return latitude;
    }

    public void setLatitude ( double latitude ) {
        this.latitude = latitude;
    }

    public double getLongitude () {
        return longitude;
    }

    public void setLongitude ( double longitude ) {
        this.longitude = longitude;
    }

    public String getConcatenated () {
        return concatenated;
    }

    public void setConcatenated ( String concatenated ) {
        this.concatenated = concatenated;
    }

    public int getEdited () {
        return edited;
    }

    public void setEdited ( int edited ) {
        this.edited = edited;
    }

    public String getWno () {
        return wno;
    }

    public void setWno ( String wno ) {
        this.wno = wno;
    }

    public int getDeleted () {
        return deleted;
    }

    public void setDeleted ( int deleted ) {
        this.deleted = deleted;
    }

    public String getBlockNo () {
        return blockNo;
    }

    public void setBlockNo ( String blockNo ) {
        this.blockNo = blockNo;
    }

    public String getBlockWno () {
        return blockWno;
    }

    public void setBlockWno ( String blockWno ) {
        this.blockWno = blockWno;
    }

    public String getEntranceName () {
        return entranceName;
    }

    public void setEntranceName ( String entranceName ) {
        this.entranceName = entranceName;
    }
}
