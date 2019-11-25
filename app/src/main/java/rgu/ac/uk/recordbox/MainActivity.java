package rgu.ac.uk.recordbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;


public class MainActivity extends AppCompatActivity {

    // Spotify fields
    private static final String CLIENT_ID = "550008f745f044b7a3a4897a11dd0958";
    private static final String REDIRECT_URI = "http://localhost:4002/";
    private static final int REQUEST_CODE = 1337;

    // Local Storage
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Token from Spotify
        getTokenFromSpotify();

        //Logo Text colorSpan
        //select the word box and make it orange
        String app = getApplicationInfo().loadLabel(getPackageManager()).toString();
        SpannableString ss = new SpannableString(app);
        ForegroundColorSpan fcsOrange = new ForegroundColorSpan(getResources().getColor(R.color.colorOrange));
        ss.setSpan(fcsOrange,6,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setTitle(ss);

        //bottom nav
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new home_frag()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.home_nav:
                            selectedFragment = new home_frag();
                            break;
                        case R.id.search_nav:
                            selectedFragment = new search_frag();
                            break;
                        case R.id.contact_Nav:
                            selectedFragment = new contact_frag();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,
                            selectedFragment).commit();

                    return true;
                }
            };


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Aaand we will finish off here.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Toast.makeText(getApplicationContext(),"Successfully logged into Spotify",Toast.LENGTH_SHORT).show();
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("STARTING", "GOT AUTH TOKEN");
                    editor.apply();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Try Again
                    getTokenFromSpotify();
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    private void getTokenFromSpotify(){

        // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

}