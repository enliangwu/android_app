/**
 * @author Enliang Wu
 * id: enliangw
 */

package leow.project4task1;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;


import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

// main servlet
// GET /activity: for a random activity
// GET /activity/doit?id=activityId: for choosing to do an activity specified by activityId
// GET /activity/dislike?id=activityId: dislike an activity and get another random one
@WebServlet(name = "activityProvider", urlPatterns = {"/activity", "/activity/doit", "/activity/dislike"})
public class ActivityProvider extends HttpServlet {

    public void init() {
    }

    // process all get requests
    // Each method in the first parameter of makeJSONResponse will return a json formatted String
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("user");
        if (Objects.equals(request.getServletPath(), "/activity")) {
            makeJSONResponse(getRandomActivity(), response);
        } else if (Objects.equals(request.getServletPath(), "/activity/doit")) {
            // get activityId from request parameters
            String activityId = request.getParameter("id");
            makeJSONResponse(doActivity(activityId), response);
        } else if (Objects.equals(request.getServletPath(), "/activity/dislike")) {
            // get activityId from request parameters
            String activityId = request.getParameter("id");
            makeJSONResponse(dislikeActivity(activityId), response);
        } else {
            // in case of hitting nothing there will be a 404-not-found error
            makeJSONResponse("{\"result\": false, \"message\": \"404 not found.\"}", response);
        }
    }

    // Get a random activity by calling boredapi
    private String getRandomActivity() {
        // declare the final result string
        String result = null;
        // use JSONObject to build result
        JSONObject json = new JSONObject();

        try {
            String apiUrl = "http://www.boredapi.com/api/activity/";
            // open the api url and get its streamed data
            InputStream stream = new URL(apiUrl).openStream();
            // another JSONObject to get json formatted data from 3rd party api
            JSONObject jsonObject;


            // reference: https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
            BufferedReader rd = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            String jsonText = sb.toString();
            jsonObject = new JSONObject(jsonText);

            // get useful fields
            String activityName = jsonObject.getString("activity");
            String activityId = jsonObject.getString("key");
            String activityType = jsonObject.getString("type");

            // put to result with my own format result=true
            json.put("name", activityName);
            json.put("id", activityId);
            json.put("type", activityType);
            json.put("result", true);
            json.put("message", "");
        } catch (Exception e) {
            // while getting some error result=false
            json.put("result", false);
            json.put("message", e.getMessage());
        }

        result = json.toString();
        return result;
    }

    // User choose to do an activity
    // activityId: activity ID string
    private String doActivity(String activityId) {
        String result;

        JSONObject json = new JSONObject();

        if (null == activityId) {
            // in case of not providing activityId set result=false and gives 'Activity not provided' message
            json.put("result", false);
            json.put("message", "Activity not provided");
        } else {
            json.put("id", activityId);
            json.put("result", true);
            json.put("message", "");
        }

        result = json.toString();

        return result;
    }

    // User dislike an activity
    // activityId: activity ID string
    private String dislikeActivity(String activityId) {
        String result;

        JSONObject json = new JSONObject();

        if (null == activityId) {
            // in case of not providing activityId set result=false and gives 'Activity not provided' message
            json.put("result", false);
            json.put("message", "Activity not provided");
            result = json.toString();
        } else {
            // use getRandomActivity to retrieve a random activity
            result = getRandomActivity();
        }

        return result;
    }

    // make JSON response to remote client
    // json: a JSON string
    // response: current servlet response object
    private void makeJSONResponse(String json, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        // set the content type to application/json
        response.setContentType("application/json");
        // set the char code to utf-8
        response.setCharacterEncoding("utf-8");
        // out put json string to response
        out.print(json);
        // end the output stream
        out.flush();
    }

    public void destroy() {
    }
}
