import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Iterator;

public class ResponseValidator {

    static String actual = "{\n" +
            "\t\"employees\": [{\n" +
            "\t\t\t\"name\": \"Shyam\",\n" +
            "\t\t\t\"age\": \"21\",\n" +
            "\t\t\t\"email\": \"shyamjaiswal@gmail.com\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"Bob\",\n" +
            "\t\t\t\"email\": \"bob32@gmail.com\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"Jai\",\n" +
            "\t\t\t\"email\": \"jai87@gmail.com\"\n" +
            "\t\t}\n" +
            "\t]\n" +
            "}";

    static String expected = "{\n" +
            "\t\"employees\": [{\n" +
            "\t\t\t\"name\": \"Shyam\",\n" +
            "\t\t\t\"email\": \"shyamjaiswal@gmail.com\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"Bob\",\n" +
            "\t\t\t\"email\": \"bob32@gmail.com\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"Jai\",\n" +
            "\t\t\t\"email\": \"jai87@gmail.com\"\n" +
            "\t\t}\n" +
            "\t]\n" +
            "}";

    public static void main(String[] args) {

        JSONObject actualJsonObject = new JSONObject(actual);
        JSONObject expectedJsonObject = new JSONObject(expected);
        //getKey(expectedJsonObject,"name");
        compareResponse(actualJsonObject,expectedJsonObject);

    }

    public static boolean compareResponse(JSONObject actual, JSONObject expected) {
        Iterator<String> expectedKeys = expected.keys();

        while (expectedKeys.hasNext()) {
            String nextKey = (String) expectedKeys.next();

            // Check if actual response object has key or not
            if (!actual.has(nextKey)){
                printErrorLog(CHECK.EXISTENCE,nextKey,expected,actual);
                return false;
            }

            // Compare class types of object in actual and expected response
            if (expected.get(nextKey).getClass() == actual.get(nextKey).getClass()) {

                // Compare json object in actual and expected response
                if (expected.get(nextKey) instanceof JSONObject) {
                    boolean matched = compareResponse(actual.getJSONObject(nextKey), expected.getJSONObject(nextKey));
                    if (matched == false){
                        printErrorLog(CHECK.VALUE,nextKey,expected,actual);
                        return false;
                    }

                }

                // Compare array object in actual and expected response
                else if (expected.get(nextKey) instanceof JSONArray) {
                    JSONArray expectedJSONArray =  expected.getJSONArray(nextKey);
                    JSONArray actualJSONArray =  actual.getJSONArray(nextKey);

                    // Check if length of array in actual and expected response are same
                    if (expectedJSONArray.length() != actualJSONArray.length()) {
                        printErrorLog(CHECK.ARRAY_LENGTH,nextKey,expected,actual);
                        return false;
                    }

                    for (int arrayIndex = 0 ; arrayIndex < expectedJSONArray.length() ;arrayIndex ++){

                        if (expectedJSONArray.get(arrayIndex) instanceof JSONObject){ {
                            boolean matched = compareResponse(actualJSONArray.getJSONObject(arrayIndex), expectedJSONArray.getJSONObject(arrayIndex));
                            if (matched == false){
                                return false;
                            }
                        }
                        } else if (!expected.get(nextKey).equals(actual.get(nextKey))) {
                            printErrorLog(CHECK.VALUE,nextKey,expected,actual);
                            return false;
                        }
                    }
                }

                else if (!expected.get(nextKey).equals(actual.get(nextKey))) {
                    printErrorLog(CHECK.VALUE,nextKey,expected,actual);
                    return false;
                }

            } else {
                printErrorLog(CHECK.TYPE,nextKey,expected,actual);
                return false;
            }
        }

        return true;
    }

    public static void printErrorLog(CHECK checkType, String objectKey, JSONObject expectedJson , JSONObject actualJsonObject ) {

        System.out.println("------------------    ERROR   --------------------");
        if (checkType == CHECK.VALUE) {
            System.out.println("VALUE OF OBJECT \"" + objectKey + "\" DID NOT MATCH");
            System.out.println("Expected Value : " + expectedJson.get(objectKey));
            System.out.println("Actual Value : " + actualJsonObject.get(objectKey));
        } else if (checkType == CHECK.TYPE) {
            System.out.println("TYPE OF OBJECT \"" + objectKey + "\" DID NOT MATCH");
            System.out.println("Expected Type : " + expectedJson.get(objectKey).getClass());
            System.out.println("Actual Type : " + actualJsonObject.get(objectKey).getClass());
        }else if (checkType == CHECK.EXISTENCE) {
            System.out.println("KEY \"" + objectKey + "\" IS UNAVAILABLE IN ACTUAL RESPONSE");
            System.out.println("Expected JSON : " + expectedJson.toString());
            System.out.println("Actual JSON : " + actualJsonObject.toString());
        }
        else if (checkType == CHECK.ARRAY_LENGTH) {
            System.out.println("NUMBER OF ITEMS IN OBJECT LIST \"" + objectKey + "\" DID NOT MATCH");
            System.out.println("Expected Length : " + expectedJson.getJSONArray(objectKey).length());
            System.out.println("Actual Length : " + actualJsonObject.getJSONArray(objectKey).length());
        }
        System.out.println("------------------    ERROR   --------------------");

    }

    public enum CHECK {
        VALUE,TYPE,EXISTENCE,ARRAY_LENGTH
    }
}
