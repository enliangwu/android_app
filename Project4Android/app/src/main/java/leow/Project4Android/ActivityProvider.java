package leow.Project4Android;
/**
 * @author Enliang (Leo) Wu
 * id: enliangw
 */
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// The main android activity
public class ActivityProvider extends AppCompatActivity {
    // a BoringActivity member to store the current Boring Activity
    private BoringActivity currentActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);

        // save this to me
        ActivityProvider me = this;
        Button findActivityButton = findViewById(R.id.findActivityButton);
        EditText userNameTextbox = findViewById(R.id.userNameTextbox);


        // Find Activity Button click event
        findActivityButton.setOnClickListener(view -> {
            String userName = userNameTextbox.getText().toString();
            if (userName.trim().isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        "Please input your name, thanks.",
                        Toast.LENGTH_LONG).show();
            } else {
                // disable the find activity button to avoid duplicate calling
                findActivityButton.setEnabled(false);
                userNameTextbox.setEnabled(false);
                // use GetActivity to get a random activity
                GetActivity activityGetter = new GetActivity(ActivityProvider.this, me);
                activityGetter.getRandomActivity(userName);
            }
        });

        Button backButton = findViewById(R.id.backButton);
        // Go back button click event
        backButton.setOnClickListener(view -> {
            // show the activity view and reenable the find activity button
            View activityView = findViewById(R.id.actvityView);
            activityView.setVisibility(View.INVISIBLE);
            findActivityButton.setEnabled(true);
            userNameTextbox.setEnabled(true);
        });

        Button doItButton = findViewById(R.id.doItButton);
        Button tryAnotherButton = findViewById(R.id.tryAnotherButton);

        // do it button click event
        doItButton.setOnClickListener(view -> {
            if (null != currentActivity) {
                // disable do it button and try another button to avoid duplicate calling
                tryAnotherButton.setEnabled(false);
                doItButton.setEnabled(false);
                // do action 'doit'
                GetActivity activityGetter = new GetActivity(ActivityProvider.this, me);
                String userName = userNameTextbox.getText().toString();
                activityGetter.doThisActivity(currentActivity, userName);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Something went wrong, there is no activity at all!",
                        Toast.LENGTH_LONG).show();
            }
        });

        // try another button click event
        tryAnotherButton.setOnClickListener(view -> {
            if (null != currentActivity) {
                // disable do it button and try another button to avoid duplicate calling
                tryAnotherButton.setEnabled(false);
                doItButton.setEnabled(false);
                // do action 'dislike'
                GetActivity activityGetter = new GetActivity(ActivityProvider.this, me);
                String userName = userNameTextbox.getText().toString();
                activityGetter.dislikeThisActivity(currentActivity, userName);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Something went wrong, there is no activity at all!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // show activity information
    public void showActivity(BoringActivity activity) {
        currentActivity = activity;
        if (null != currentActivity) {
            // show activity view and put activity information into views
            View activityView = findViewById(R.id.actvityView);
            TextView activityNameTextbox = findViewById(R.id.activityNameTextbox);
            TextView activityTypeTextbox = findViewById(R.id.activityTypeTextbox);
            activityNameTextbox.setText("Name: " + currentActivity.getActivityName());
            activityTypeTextbox.setText("Type: " + currentActivity.getActivityType());
            // reenable try another button and do it button
            Button tryAnotherButton = findViewById(R.id.tryAnotherButton);
            tryAnotherButton.setEnabled(true);
            Button doItButton = findViewById(R.id.doItButton);
            doItButton.setEnabled(true);
            activityView.setVisibility(View.VISIBLE);
        }
    }

    // after do an activity
    public void doActivity(boolean success) {
        if (success) {
            // hide out the activity view and reenable the find activity button
            currentActivity = null;
            View activityView = findViewById(R.id.actvityView);
            activityView.setVisibility(View.INVISIBLE);
            Button findActivityButton = findViewById(R.id.findActivityButton);
            EditText userNameTextbox = findViewById(R.id.userNameTextbox);
            findActivityButton.setEnabled(true);
            userNameTextbox.setEnabled(true);
            Toast.makeText(getApplicationContext(),
                    "You just made a good choice I think!",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Something went wrong, try it later.",
                    Toast.LENGTH_LONG).show();
        }
    }
}