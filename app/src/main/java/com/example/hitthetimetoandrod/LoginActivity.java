package com.example.hitthetimetoandrod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.os.SystemClock.sleep;


public class LoginActivity extends AppCompatActivity {

    private static final int FACEBOOKLOGIN = 1;
    private static final int GOOGLELOGIN = 2;
    private static final int ISLOGOUT = 4;


    private static final String TAG = "LoginActivity";
    private CallbackManager mCallbackManager;
    private FirebaseAuth mFirebaseAuth_facebook;

    private FirebaseAuth mFirebaseAuth_google;
    private ImageButton googleSignInBtn;
    private GoogleSignInClient googleSignInClient;

    private int RESULT_CODE_SINGIN=999;

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private DataSnapshot arrayToken;

    private Context currentCtx;

    private Button blind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        currentCtx = this;

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();


        blind = findViewById(R.id.blind);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("LoginActivity", "Single ValueEventListener : " + snapshot.getValue());
                }
                arrayToken = dataSnapshot;
                autoLogin();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("LoginActivity", "Single ValueEventListener Error");
            }
        });


        /*
         * google login
         */

        //Initialization
        googleSignInBtn = findViewById(R.id.googleSignInBtn);

        //signout button
        //googleSignOutBtn = findViewById(R.id.googleSignOutBtn);
        //googleSignOutBtn.setVisibility(View.INVISIBLE);

        mFirebaseAuth_google = FirebaseAuth.getInstance();



        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this,gso);

        //Attach a onClickListener
        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInM();
            }
        });



        /* google signout code
        //Attach a onClickListener
        googleSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignInClient.signOut();
                googleSignOutBtn.setVisibility(View.INVISIBLE);
                googleSignInBtn.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this,"you are logged out",Toast.LENGTH_LONG).show();
            }
        });
        */


        /*
         * facebook login
         */
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mFirebaseAuth_facebook = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        ImageButton loginButton = findViewById(R.id.facebookBtn);

        //Registering callback!
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Sign in completed
                Log.i(TAG, "onSuccess: logged in successfully");

                //handling the token for Firebase Auth
                handleFacebookAccessToken(loginResult.getAccessToken());

                //Getting the user information
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        Log.i(TAG, "onCompleted: response: " + response.toString());
                        try {
                            //check object value, accesstoken
                            String name = object.getString("name");
                            String uid = mFirebaseAuth_facebook.getUid();
                            Log.i(TAG, "onCompleted: Name: " + name);

                            if (!isExistUser(uid)) { // 최초 로그인
                                FirebasePost post = new FirebasePost(name, Double.MAX_VALUE);
                                databaseRef.child("users").child(uid).setValue(post.toMap());
                            }

                            PreferenceManager.setString(currentCtx, "idToken", uid);
                            PreferenceManager.setInt(currentCtx, "loginType", FACEBOOKLOGIN);

                            Intent intent = new Intent(LoginActivity.this, GameActivity.class);
                            intent.putExtra("idToken", uid);
                            intent.putExtra("loginType", FACEBOOKLOGIN);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(TAG, "onCompleted: JSON exception");
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Setting the permission that we need to read
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email", "user_birthday"));

            }
        });

        LogOut();

    }

    //when the signIn Button is clicked then start the signIn Intent
    private void signInM() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RESULT_CODE_SINGIN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {

        //we use try catch block because of Exception.
        try {
            //googleSignInBtn.setVisibility(View.INVISIBLE);
            GoogleSignInAccount account = task.getResult(ApiException.class); //error
            Toast.makeText(LoginActivity.this,"Signed In successfully",Toast.LENGTH_LONG).show();
            //SignIn successful now show authentication
            FirebaseGoogleAuth(account);

        } catch (ApiException e) {
            e.printStackTrace();
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this,"SignIn Failed!!!",Toast.LENGTH_LONG).show();
            //FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        //here we are checking the Authentication Credential and checking the task is successful or not and display the message
        //based on that.
        mFirebaseAuth_google.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"successful",Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser = mFirebaseAuth_google.getCurrentUser();
                    UpdateUI(firebaseUser);
                }
                else {
                    Toast.makeText(LoginActivity.this,"Failed!",Toast.LENGTH_LONG).show();
                    UpdateUI(null);
                }
            }
        });
    }

    //Inside UpdateUI we can get the user information and display it when required
    private void UpdateUI(FirebaseUser fUser) {
        //googleSignOutBtn.setVisibility(View.VISIBLE);

        //getLastSignedInAccount returned the account
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account !=null){

            String personName = fUser.getDisplayName();
            String personUid = fUser.getUid();

            if (!isExistUser(personUid)) { // 최초로그인
                FirebasePost post = new FirebasePost(personName, Double.MAX_VALUE);
                databaseRef.child("users").child(personUid).setValue(post.toMap());
                Log.d("LoginActivity", "First Login : " + personUid);
                //Toast.makeText(LoginActivity.this,personName + "  " + personEmail,Toast.LENGTH_LONG).show();
            }


            PreferenceManager.setString(currentCtx, "idToken", personUid);
            PreferenceManager.setInt(currentCtx, "loginType", GOOGLELOGIN);

            Intent intent = new Intent(LoginActivity.this, GameActivity.class);
            intent.putExtra("idToken", personUid);
            intent.putExtra("loginType", GOOGLELOGIN); //error

            startActivity(intent);

        }
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth_facebook.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mFirebaseAuth_facebook.getCurrentUser();
                            Log.i(TAG, "onComplete: login completed with user: " + user.getDisplayName());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    // onActivityResult (Here we handle the result of the Activity )
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == RESULT_CODE_SINGIN) {        //just to verify the code
            //create a Task object and use GoogleSignInAccount from Intent and write a separate method to handle singIn Result.
            super.onActivityResult(requestCode, resultCode, data);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data); // task error
            handleSignInResult(task);
        }
        else{

            mCallbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);

        }


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mFirebaseAuth_facebook.getCurrentUser();
        if (currentUser != null) {
            Log.i(TAG, "onStart: Someone logged in <3");
        } else {
            Log.i(TAG, "onStart: No one logged in :/");
        }

    }

    public boolean isExistUser(final String token){
        return arrayToken.hasChild("/users/" + token);
    }

    public void autoLogin(){

        String idToken = PreferenceManager.getString(currentCtx, "idToken");
        int loginType = PreferenceManager.getInt(currentCtx, "loginType");

        if(!idToken.equals("") && loginType != -1 && isExistUser(idToken)){


            Log.i(TAG, "PreferenceManager | idToken : " + idToken + "loginType : " + (loginType == 1 ? "FACEBOOKLOGIN" : "GOOGLELOGIN"));
            Intent intent = new Intent(LoginActivity.this, GameActivity.class);
            intent.putExtra("idToken", idToken);
            intent.putExtra("loginType", loginType);
            startActivity(intent);
        }
        else{
            blind.setVisibility(View.INVISIBLE);
        }


    }


    public void LogOut(){

        int f_LogOut = getIntent().getIntExtra("isLogOut", 0);

        if((f_LogOut & ISLOGOUT) == ISLOGOUT){

            if((f_LogOut & FACEBOOKLOGIN) == FACEBOOKLOGIN){
                LoginManager.getInstance().logOut();
            } else if((f_LogOut & GOOGLELOGIN) == GOOGLELOGIN){
                googleSignInClient.signOut();
            }
        }
    }


}

