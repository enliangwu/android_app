package leow.Project4Android;
/**
 * @author Enliang (Leo) Wu
 * id: enliangw
 */

import android.app.Activity;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GetActivity {
    private final OkHttpClient httpClient;

    BoringActivity boringActivity = null;
    boolean success = false;
    Activity mainActivity;
    ActivityProvider activityProvider;

    // constructor method
    // _mainActivity: Android active activity
    // _activityProvider: the activity class
    public GetActivity(Activity _mainActivity, ActivityProvider _activityProvider) {
        // build an okhttp client with timeout options to avoid failure timeout calling
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        this.mainActivity = _mainActivity;
        this.activityProvider = _activityProvider;
    }

    // call background task of action 'get'
    public void getRandomActivity(String userName) {
        new BackgroundTask(mainActivity).execute("get", null, userName);
    }

    // call background task of action 'doit'
    public void doThisActivity(BoringActivity _activity, String userName) {
        new BackgroundTask(mainActivity).execute("doit", _activity, userName);
    }

    // call background task of action 'dislike'
    public void dislikeThisActivity(BoringActivity _activity, String userName) {
        new BackgroundTask(mainActivity).execute("dislike", _activity, userName);
    }

    // Adapted from one of the answers in
    // https://stackoverflow.com/questions/58767733/the-asynctask-api-is-deprecated-in-android-11-what-are-the-alternatives
    private class BackgroundTask {
        private final Activity activity; // The UI thread

        public BackgroundTask(Activity activity) {
            this.activity = activity;
        }

        // starting a new background thread for api calling
        private void startBackground(String action, BoringActivity boringActivity, String userName) {
            new Thread(() -> {

                // calling corresponding method to action names
                switch (action) {
                    case "get":
                        doInBackgroundGetRandomActivity(userName);
                        break;
                    case "doit":
                        doInBackgroundDoThisActivity(boringActivity, userName);
                        break;
                    case "dislike":
                        doInBackgroundUnlikeThisActivity(boringActivity, userName);
                        break;
                }

                // calling onPostExecute on UI Thread
                activity.runOnUiThread(() -> onPostExecute(action));
            }).start();
        }

        private void execute(String action, BoringActivity boringActivity, String userName){
            // There could be more setup here, which is why
            //    startBackground is not called directly
            startBackground(action, boringActivity, userName);
        }

        public void onPostExecute(String action) {
            // call corresponding UI actions to action names
            switch (action) {
                case "get":
                case "dislike":
                    // the member boringActivity will have value after calling get or dislike actions
                    activityProvider.showActivity(boringActivity);
                    break;
                case "doit":
                    // the member success will have a boolean value after calling doit action
                    activityProvider.doActivity(success);
                    break;
            }
        }

        // doInBackground( ) implements whatever you need to do on
        //    the background thread.
        // Implement this method to suit your needs
        private void doInBackgroundGetRandomActivity(String userName) {
            boringActivity = getRandomActivity(userName);
        }

        private void doInBackgroundDoThisActivity(BoringActivity _boringActivity, String userName) {
            // assign the return boolean value to member success
            success = doThisActivity(_boringActivity, userName);
        }

        private void doInBackgroundUnlikeThisActivity(BoringActivity _boringActivity, String userName) {
            // assign the return value to member object
            boringActivity = dislikeThisActivity(_boringActivity, userName);
        }

        // get a random activity by api calling
        private BoringActivity getRandomActivity(String userName) {
            BoringActivity _boringActivity = null;

            String apiGetActivityUrl = "https://frozen-castle-25997.herokuapp.com/activity?user=";
            // build the request
            Request request = new Request.Builder().url(apiGetActivityUrl + userName).build();
            try {
                // do this request
                Response response = httpClient.newCall(request).execute();
                ResponseBody body = response.body();
                if (null != body) {
                    String result = body.string();
                    JSONObject jsonObject = new JSONObject(result);
                    // check result true meaning success false means failed
                    boolean success = jsonObject.getBoolean("result");
                    if (success) {
                        String activityId = jsonObject.getString("id");
                        String activityName = jsonObject.getString("name");
                        String activityType = jsonObject.getString("type");
                        // create BoringActivity Object by response values
                        _boringActivity = new BoringActivity(activityId, activityName, activityType);
                    } else {
                        String errorMessage = jsonObject.getString("message");
                        System.out.println(errorMessage);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return _boringActivity;
        }

        // do activity by api calling
        private boolean doThisActivity(BoringActivity _activity, String userName) {
            boolean success = false;

            String apiLikeActivityUrl = "https://frozen-castle-25997.herokuapp.com/activity/doit?id=";
            // build the request
            Request request = new Request.Builder().url(apiLikeActivityUrl +
                    _activity.getActivityId() + "&user=" + userName).build();
            try {
                // do this request
                Response response = httpClient.newCall(request).execute();
                ResponseBody body = response.body();
                if (null != body) {
                    String result = body.string();
                    JSONObject jsonObject = new JSONObject(result);
                    // result true meaning success false means failed
                    success = jsonObject.getBoolean("result");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return success;
        }

        // dislike activity by api calling
        private BoringActivity dislikeThisActivity(BoringActivity _activity, String userName) {
            BoringActivity _boringActivity = null;

            String apiDislikeActivityUrl = "https://frozen-castle-25997.herokuapp.com/activity/dislike?id=";
            // build the request
            Request request = new Request.Builder().url(apiDislikeActivityUrl +
                    _activity.getActivityId() + "&user=" + userName).build();
            try {
                // do this request
                Response response = httpClient.newCall(request).execute();
                ResponseBody body = response.body();
                if (null != body) {
                    String result = body.string();
                    JSONObject jsonObject = new JSONObject(result);
                    // check result true meaning success false means failed
                    if (jsonObject.getBoolean("result")) {
                        String activityId = jsonObject.getString("id");
                        String activityName = jsonObject.getString("name");
                        String activityType = jsonObject.getString("type");
                        // create BoringActivity Object by response values
                        _boringActivity = new BoringActivity(activityId, activityName, activityType);
                    } else {
                        String errorMessage = jsonObject.getString("message");
                        System.out.println(errorMessage);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return _boringActivity;
        }
    }
}
