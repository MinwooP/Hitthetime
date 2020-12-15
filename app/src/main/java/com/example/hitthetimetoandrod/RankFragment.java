package com.example.hitthetimetoandrod;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankFragment extends Fragment {

    // 리사이클러뷰를 위한 변수 정의
    RecyclerView mRecyclerView = null ;
    RecyclerTextAdapter mAdapter = null ;
    ArrayList<RecyclerItem> mList = new ArrayList<RecyclerItem>();


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
        View view1 = inflater.inflate(R.layout.fragment_rank, container, false);
        /* activity에서 fragment로 넘어오면서 mRecyclerView = findViewById(R.id.recycler1);
        * 아래 문장으로 바꾸어줌 */

        mRecyclerView = (RecyclerView) view1.findViewById(R.id.recycler1);
        /* mRecyclerView = container.findViewById(R.id.recycler1) ;
           원래 이 코드로 해서 오류 났었음 ;;
         */

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        mAdapter = new RecyclerTextAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        // 리사이클러뷰에 LinearLayoutManager 지정. (vertical)
        /* Activity에서 fragment로 코드 이동할 때, this -> getActivity로 바꿔주었음 */
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())) ;


        // DB와 연결해서 기록들 비교한다음에 TOP 10 가져온뒤 여기서 정렬 후 아이템 추가
        // 아이템 추가.
        mAdapter.addItem("1", "Box", "109957") ;
        mAdapter.addItem("2", "Circle", "109957") ;
        mAdapter.addItem("3", "Ind", "109957") ;
        mAdapter.addItem("4", "Ind", "109957") ;
        mAdapter.addItem("5", "Ind", "109957") ;
        mAdapter.addItem("6", "Ind", "109957") ;
        mAdapter.addItem("7", "Ind", "109957") ;
        mAdapter.addItem("8", "Ind", "109957") ;
        mAdapter.addItem("9", "Ind", "109957") ;
        mAdapter.addItem("10", "Ind", "109957") ;

        // Inflate the layout for this fragment
        return view1;
    }
}