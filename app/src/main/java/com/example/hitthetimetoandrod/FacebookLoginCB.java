package com.example.hitthetimetoandrod;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;


public class FacebookLoginCB implements FacebookCallback<LoginResult>{

    private static final String TAG = "FacebookLoginCB";

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
                    String name = object.getString("name");
                    String birthday = object.getString("birthday");

                    Log.i(TAG, "onCompleted: Name: " + name);
                    Log.i(TAG, "onCompleted: Birthday: " + birthday);


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


    private void handleFacebookAccessToken(AccessToken token) {

        FirebaseAuth mFirebaseAuth_facebook = FirebaseAuth.getInstance();

        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth_facebook.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
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
                            //Toast.makeText(, "Authentication failed.",
                            //Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}
