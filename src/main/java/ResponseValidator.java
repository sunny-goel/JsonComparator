import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;

public class ResponseValidator {

    static String actual = "{\n" +
            "\t\"employees\": [{\n" +
            "\t\t\t\"name\": \"Shyam\",\n" +
            "\t\t\t\"age\": \"21\",\n" +
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
            "\t\t\t\"age\": \"21\",\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"Bob\",\n" +
            "\t\t\t\"email\": \"bob32@gmail.com\"\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"Jai\",\n" +
            "\t\t\t\"email\": \"jai87@gmxsxaail.com\"\n" +
            "\t\t}\n" +
            "\t]\n" +
            "}";

    public static void main(String[] args) {

        JSONObject actualJsonObject = new JSONObject(actual);
        JSONObject expectedJsonObject = new JSONObject(expected);
        compareResponse(actualJsonObject, expectedJsonObject);

    }

    public static boolean compareResponse(JSONObject actual, JSONObject expected) {
        LinkedList<ResponseValidationFailures> listOfFailures = compareResponse(actual, expected, new LinkedList<ResponseValidationFailures>());
        if (listOfFailures.size() > 0) {
            printErrorLog(listOfFailures);
            return false;
        }

        return true;

    }

    private static LinkedList<ResponseValidationFailures> compareResponse(
            JSONObject actual, JSONObject expected, LinkedList<ResponseValidationFailures> failures) {

        Iterator<String> expectedKeys = expected.keys();

        while (expectedKeys.hasNext()) {
            String nextKey = (String) expectedKeys.next();

            // Check if actual response object has key or not
            if (!actual.has(nextKey)) {
                failures.add(new ResponseValidationFailures(CHECK.EXISTENCE, nextKey, expected, actual));
                continue;
            }

            // Compare class types of object in actual and expected response
            if (expected.get(nextKey).getClass() == actual.get(nextKey).getClass()) {

                // Compare json object in actual and expected response
                if (expected.get(nextKey) instanceof JSONObject)
                    compareResponse(actual.getJSONObject(nextKey), expected.getJSONObject(nextKey), failures);

                    // Compare array object in actual and expected response
                else if (expected.get(nextKey) instanceof JSONArray) {
                    JSONArray expectedJSONArray = expected.getJSONArray(nextKey);
                    JSONArray actualJSONArray = actual.getJSONArray(nextKey);

                    // Check if length of array in actual and expected response are same
                    if (expectedJSONArray.length() != actualJSONArray.length())
                        failures.add(new ResponseValidationFailures(CHECK.ARRAY_LENGTH, nextKey, expected, actual));


                    for (int arrayIndex = 0; arrayIndex < expectedJSONArray.length(); arrayIndex++) {

                        if (expectedJSONArray.get(arrayIndex) instanceof JSONObject)
                            compareResponse(actualJSONArray.getJSONObject(arrayIndex), expectedJSONArray.getJSONObject(arrayIndex), failures);

                        else if (!expected.get(nextKey).equals(actual.get(nextKey)))
                            failures.add(new ResponseValidationFailures(CHECK.VALUE, nextKey, expected, actual));

                    }
                } else if (!expected.get(nextKey).equals(actual.get(nextKey)))
                    failures.add(new ResponseValidationFailures(CHECK.VALUE, nextKey, expected, actual));

            } else
                failures.add(new ResponseValidationFailures(CHECK.TYPE, nextKey, expected, actual));
        }

        return failures;
    }

    public static void printErrorLog(LinkedList<ResponseValidationFailures> failures) {
        for (ResponseValidationFailures ResponseValidationFailures : failures) {
            System.out.println("------------------    ERROR   --------------------");
            if (ResponseValidationFailures.getCheckType() == CHECK.VALUE) {
                System.out.println("VALUE OF OBJECT \"" + ResponseValidationFailures.getObjectKey() + "\" DID NOT MATCH");
                System.out.println("Expected Value : " + ResponseValidationFailures.getExpectedJsonObject().get(ResponseValidationFailures.getObjectKey()));
                System.out.println("Actual Value : " + ResponseValidationFailures.getActualJsonObject().get(ResponseValidationFailures.getObjectKey()));
            } else if (ResponseValidationFailures.getCheckType() == CHECK.TYPE) {
                System.out.println("TYPE OF OBJECT \"" + ResponseValidationFailures.getObjectKey() + "\" DID NOT MATCH");
                System.out.println("Expected Type : " + ResponseValidationFailures.getExpectedJsonObject().get(ResponseValidationFailures.getObjectKey()).getClass());
                System.out.println("Actual Type : " + ResponseValidationFailures.getActualJsonObject().get(ResponseValidationFailures.getObjectKey()).getClass());
            } else if (ResponseValidationFailures.getCheckType() == CHECK.EXISTENCE) {
                System.out.println("KEY \"" + ResponseValidationFailures.getObjectKey() + "\" IS UNAVAILABLE IN ACTUAL RESPONSE");
                System.out.println("Expected JSON : " + ResponseValidationFailures.getExpectedJsonObject().toString());
                System.out.println("Actual JSON : " + ResponseValidationFailures.getActualJsonObject().toString());
            } else if (ResponseValidationFailures.getCheckType() == CHECK.ARRAY_LENGTH) {
                System.out.println("NUMBER OF ITEMS IN OBJECT LIST \"" + ResponseValidationFailures.getObjectKey() + "\" DID NOT MATCH");
                System.out.println("Expected Length : " + ResponseValidationFailures.getExpectedJsonObject().getJSONArray(ResponseValidationFailures.getObjectKey()).length());
                System.out.println("Actual Length : " + ResponseValidationFailures.getActualJsonObject().getJSONArray(ResponseValidationFailures.getObjectKey()).length());
            }
            System.out.println("------------------    ERROR   --------------------");
        }

    }

    public static enum CHECK {
        VALUE, TYPE, EXISTENCE, ARRAY_LENGTH
    }
}
