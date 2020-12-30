package com.minwooseonno.hitthetimetoandrod;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankFragment#newInstance} factory method tos
 * create an instance of this fragment.
 */
public class RankFragment extends Fragment {

    // 리사이클러뷰를 위한 변수 정의
    RecyclerView mRecyclerView = null;
    RecyclerTextAdapter mAdapter = null;
    ArrayList<RecyclerItem> mList = new ArrayList<RecyclerItem>();
    private BottomNavigationView mBottomNV;

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    //private ArrayList<double> arraySocre;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "RankRagment";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankFragment newInstance(String param1, String param2) {
        RankFragment fragment = new RankFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        List<FirebasePost> arrayList = bundle.getParcelableArrayList("arrayList");


        View view1 = inflater.inflate(R.layout.fragment_rank, container, false);
        /* activity에서 fragment로 넘어오면서 mRecyclerView = findViewById(R.id.recycler1);
         * 아래 문장으로 바꾸어줌 */
        SwipeRefreshLayout mSwipeRefreshLayout = view1.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.commit();


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });

        mRecyclerView = (RecyclerView) view1.findViewById(R.id.recycler1);
        /* mRecyclerView = container.findViewById(R.id.recycler1) ;
           원래 이 코드로 해서 오류 났었음 ;;
         */

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        mAdapter = new RecyclerTextAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        // 리사이클러뷰에 LinearLayoutManager 지정. (vertical)
        /* Activity에서 fragment로 코드 이동할 때, this -> getActivity로 바꿔주었음 */
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // DB와 연결해서 기록들 비교한다음에 TOP 10 가져온뒤 여기서 정렬 후 아이템 추가
        // 아이템 추가.
        int count = 1;
        for(FirebasePost FP : arrayList) {
            if(count > 10 || FP.getRecord() == Double.MAX_VALUE){
                break;
            }
            mAdapter.addItem(String.valueOf(count), FP.getName(), Double.toString(FP.getRecord()).split("\\.")[0]);
            count++;
        }
        // Inflate the layout for this fragment
        return view1;
    }
}