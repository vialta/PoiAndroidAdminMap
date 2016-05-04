package qualteh.com.androidadminmap.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Virgil Tanase on 28.04.2016.
 */
public class AdminMapModel {
    private Boolean success;
    private Data data;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The success
     */
    public Boolean getSuccess() {
            return success;
            }

    /**
     *
     * @param success
     * The success
     */
    public void setSuccess(Boolean success) {
            this.success = success;
            }

    /**
     *
     * @return
     * The data
     */
    public Data getData() {
            return data;
            }

    /**
     *
     * @param data
     * The data
     */
    public void setData(Data data) {
            this.data = data;
            }

    public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
            }

    public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
            }

}