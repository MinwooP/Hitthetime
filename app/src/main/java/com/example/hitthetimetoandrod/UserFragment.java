package com.example.hitthetimetoandrod;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import org.w3c.dom.Text;

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
    private static final int ISLOGOUT = 4;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user, container, false);
        ImageButton signOutBt = v.findViewById(R.id.signOutBt);

        double userRecord =  getArguments().getDouble("userRecord");
        TextView recordTV = v.findViewById(R.id.record_textView);
        recordTV.setText(String.valueOf(userRecord).split("\\.")[0]);

                signOutBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                int loginType = getActivity().getIntent().getIntExtra("loginType", -1);

                int flag = 0;

                switch (loginType){
                    case FACEBOOKLOGIN:

                        flag = ISLOGOUT | FACEBOOKLOGIN;

                        break;
                    case GOOGLELOGIN:

                        // google logout

                        flag = ISLOGOUT | GOOGLELOGIN;

                        break;
                    default:
                        break;
                }

                // remove auto login shared preference
                PreferenceManager.clear(getContext());

                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.putExtra("isLogOut", flag);

                startActivity(intent);

            }
        });




        // Inflate the layout for this fragment

        return v;
    }
}