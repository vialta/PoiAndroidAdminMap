package qualteh.com.androidadminmap.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Virgil Tanase on 28.04.2016.
 */
public class Data {

    private Main main;
    private List<Address> addresses = new ArrayList<Address>();
    private List<Poi> pois = new ArrayList<Poi>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The main
     */
    public Main getMain() {
        return main;
    }

    /**
     *
     * @param main
     * The main
     */
    public void setMain(Main main) {
        this.main = main;
    }

    /**
     *
     * @return
     * The addresses
     */
    public List<Address> getAddresses() {
        return addresses;
    }

    /**
     *
     * @param addresses
     * The addresses
     */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    /**
     *
     * @return
     * The pois
     */
    public List<Poi> getPois() {
        return pois;
    }

    /**
     *
     * @param pois
     * The pois
     */
    public void setPois(List<Poi> pois) {
        this.pois = pois;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
