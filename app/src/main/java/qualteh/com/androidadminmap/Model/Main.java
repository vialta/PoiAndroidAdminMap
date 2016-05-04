package qualteh.com.androidadminmap.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Virgil Tanase on 28.04.2016.
 */
public class Main {

    private String name;
    private String no;
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