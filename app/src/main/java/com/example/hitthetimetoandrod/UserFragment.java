package com.example.hitthetimetoandrod;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    private static final String TAG = "UserFragment";
    private static final int FACEBOOKLOGIN = 1;
    private static final int GOOGLELOGIN = 2;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private GoogleSignInClient googleSignInClient;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onStart(){
        super.onStart();

        String idToken = getActivity().getIntent().getStringExtra("idToken");
        int loginType = getActivity().getIntent().getIntExtra("loginType", -1);
        Log.d(TAG, "onCreate:  idToken : " + idToken + " loginType : " +  (loginType == 1 ? "FACEBOOKLOGIN" : "GOOGLELOGIN"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*
    // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(LoginActivity, gso);

        Button signOut = getView().findViewById(R.id.signOutBt);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.removeExtra("idToken");
                intent.removeExtra("loginType");

                PreferenceManager.clear(getContext());
                startActivity(intent);

            }
        });
        */

        View v = inflater.inflate(R.layout.fragment_user, container, false);
        ImageButton signOutBt = v.findViewById(R.id.signOutBt);

        signOutBt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                int loginType = getActivity().getIntent().getIntExtra("loginType", -1);

                switch (loginType){
                    case FACEBOOKLOGIN:

                        // facebook logout
                        LoginManager loginM = LoginManager.getInstance();
                        loginM.logOut();

                        break;
                    case GOOGLELOGIN:
                        // google logout

                        GoogleSignInOptions gso = new
                                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build();

                        // Build a GoogleSignInClient with the options specified by gso.
                        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
                        googleSignInClient.signOut();
                        break;
                    default:

                        break;
                }
                // remove intent extrea
                getActivity().getIntent().removeExtra("idToken");
                getActivity().getIntent().removeExtra("loginType");

                // remove auto login
                PreferenceManager.clear(getContext());

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });




        // Inflate the layout for this fragment

        return v;
    }
}