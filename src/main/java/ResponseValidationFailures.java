import org.json.JSONObject;

public class ResponseValidationFailures {

    private ResponseValidator.CHECK checkType;
    private String objectKey;
    private JSONObject expectedJsonObject ;
    private JSONObject actualJsonObject ;

    public ResponseValidationFailures(
            ResponseValidator.CHECK checkType,
            String objectKey,
            JSONObject expectedJson ,
            JSONObject actualJsonObject ){

        this.checkType = checkType;
        this.objectKey = objectKey;
        this.expectedJsonObject = expectedJson;
        this.actualJsonObject = actualJsonObject;

    }

    public ResponseValidator.CHECK getCheckType() {
        return checkType;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public JSONObject getExpectedJsonObject() {
        return expectedJsonObject;
    }

    public JSONObject getActualJsonObject() {
        return actualJsonObject;
    }
}
