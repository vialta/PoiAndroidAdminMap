package qualteh.com.androidadminmap.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Virgil Tanase on 28.04.2016.
 */
public class Address {

    private String name;
    private Double lat;
    private Double lng;
    private String no;
    private String sc;
    private Double dist;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The lat
     */
    public Double getLat() {
        return lat;
    }

    /**
     *
     * @param lat
     * The lat
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     *
     * @return
     * The lng
     */
    public Double getLng() {
        return lng;
    }

    /**
     *
     * @param lng
     * The lng
     */
    public void setLng(Double lng) {
        this.lng = lng;
    }

    /**
     *
     * @return
     * The no
     */
    public String getNo() {
        return no;
    }

    /**
     *
     * @param no
     * The no
     */
    public void setNo(String no) {
        this.no = no;
    }

    /**
     *
     * @return
     * The sc
     */
    public String getSc() {
        return sc;
    }

    /**
     *
     * @param sc
     * The sc
     */
    public void setSc(String sc) {
        this.sc = sc;
    }

    /**
     *
     * @return
     * The dist
     */
    public Double getDist() {
        return dist;
    }

    /**
     *
     * @param dist
     * The dist
     */
    public void setDist(Double dist) {
        this.dist = dist;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}