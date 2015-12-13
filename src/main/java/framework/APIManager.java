package framework;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import entities.Location;
import entities.Resource;
import java.util.ArrayList;
import com.jayway.restassured.response.Response;
import org.json.JSONArray;
import static com.jayway.restassured.RestAssured.given;

/**
 * Created with IntelliJ IDEA.
 * User: jhasmanyquiroz
 * Date: 12/11/15
 * Time: 5:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class APIManager {
    private String token;
    private static APIManager instance;

    protected APIManager() {
        initialize();
    }

    public static APIManager getInstance() {
        if(instance == null)
            instance = new APIManager();
        return instance;
    }
    private void initialize() {
        RestAssured.baseURI = "https://172.20.208.216:4040";
        RestAssured.useRelaxedHTTPSValidation();
        token = getToken();
    }

    private String getToken() {
        Response response = given()
                .parameters("username", "BrayanRosas", "password",
                            "Client123", "authentication", "local")
                .post("/login");

        String json = response.asString();
        JsonPath jp = new JsonPath(json);
        return jp.get("token");
    }

    private void createResourceByName(String name) {
        given()
                .header("Authorization", "jwt " + token)
                .parameters("name", name, "description", "",
                            "customName", name, "from", "",
                            "fontIcon", "")
                .post("/resources")
        ;
    }

    private void createLocationByName(String name) {
        given()
                .header("Authorization", "jwt " + token)
                .parameters("customName", name, "name", name,
                            "description", "")
                .post("/locations")
                ;
    }

    private void deleteResourceByID(String id) {
        given()
            .header("Authorization", "jwt " + token)
            .parameters("id", id)
            .delete("/resources/"+id)
        ;
    }

    private void deleteLocationByID(String _id) {
        given()
                .header("Authorization", "jwt " + token)
                .parameters("id", _id)
                .delete("/locations/"+_id)
        ;
    }

    private Resource setResource(String _id, String name, String description,
                                 String customName, String fontIcon) {
        Resource resource = new Resource();

        resource.setID(_id);
        resource.setName(name);
        resource.setDescription(description);
        resource.setDisplayName(customName);
        resource.setIcon(fontIcon);

        return resource;
    }

    private Location setLocation(String _id, String name, String description,
                                 String customName, String path) {
        Location location = new Location();

        location.setId(_id);
        location.setName(name);
        location.setDescription(description);
        location.setDisplayName(customName);
        location.setParentLocation(path);

        return location;
    }

    public void createResourcesByName(ArrayList<String> resourcesName) {
        for (String name : resourcesName)
            createResourceByName(name);
    }

    public void createLocationsByName(ArrayList<String> locationsName) {
        for (String name : locationsName)
            createLocationByName(name);
    }

    public void deleteResourcesById(ArrayList<String> resourcesID) {
        for (String id : resourcesID) {
            deleteResourceByID(id);
        }
    }

    public void deleteLocationByID(ArrayList<String> locationsID) {
        for (String _id : locationsID)
            deleteLocationByID(_id);
    }

    public Resource getResourceByID(String id) {
        Response response = given().when().get("/resources/"+id);
        String json = response.asString();
        JsonPath jp = new JsonPath(json);

        return setResource((String)jp.get("_id"), (String)jp.get("name"),
                            (String)jp.get("description"), (String)jp.get("customName"),
                            (String)jp.get("fontIcon"));
    }

    public Location getLocationByID(String _id) {
        Response response = given().when().get("/locations/"+_id);
        String json = response.asString();
        JsonPath jp = new JsonPath(json);

        return setLocation((String)jp.get("_id"), (String)jp.get("name"),
                            (String)jp.get("description"),(String)jp.get("customName"),
                            (String)jp.get("path"));
    }

    public ArrayList<Resource> getResources() {
        ArrayList<Resource> resources = new ArrayList<Resource>();

        Response response = given().when().get("/resources");
        JSONArray jsonArray = new JSONArray(response.asString());

        for (int indice = 0; indice < jsonArray.length(); indice++) {
            resources.add(setResource(jsonArray.getJSONObject(indice).getString("_id"),
                            jsonArray.getJSONObject(indice).getString("name"),
                            jsonArray.getJSONObject(indice).getString("description"),
                            jsonArray.getJSONObject(indice).getString("customName"),
                            jsonArray.getJSONObject(indice).getString("fontIcon"))
                          );
        }
        return resources;
    }

    public ArrayList<Location> getLocations() {
        ArrayList<Location> locations = new ArrayList<>();

        Response response = given().when().get("/locations");
        JSONArray jsonArray = new JSONArray(response.asString());

        for (int indice = 0; indice < jsonArray.length(); indice++) {
            locations.add(setLocation(jsonArray.getJSONObject(indice).getString("_id"),
                    jsonArray.getJSONObject(indice).getString("name"),
                    jsonArray.getJSONObject(indice).getString("description"),
                    jsonArray.getJSONObject(indice).getString("customName"),
                    jsonArray.getJSONObject(indice).getString("path"))
            );
        }
        return locations;
    }
}