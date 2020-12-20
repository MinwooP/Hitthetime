package com.example.hitthetimetoandrod;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerTextAdapter extends RecyclerView.Adapter<RecyclerTextAdapter.ViewHolder> {
    private ArrayList<RecyclerItem> mData = null ;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    RecyclerTextAdapter(ArrayList<RecyclerItem> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecyclerTextAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recycler_item, parent, false) ;
        // ViewHolder에 view를 집어넣고, ViewHolder return
        RecyclerTextAdapter.ViewHolder vh = new RecyclerTextAdapter.ViewHolder(view) ;
        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    // - 뷰 홀더가 실제로 연결될 때 호출됨.
    @Override
    public void onBindViewHolder(RecyclerTextAdapter.ViewHolder holder, int position) {

        RecyclerItem item = mData.get(position) ;

        holder.rank.setText(item.getRank());
        holder.name.setText(item.getName()) ;
        holder.record.setText(item.getRecord());
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public void addItem(String rank, String name, String record){
        RecyclerItem item = new RecyclerItem();

        item.setRank(rank);
        item.setName(name);
        item.setRecord(record);

        mData.add(item);
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rank ;
        TextView name ;
        TextView record ;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            rank = itemView.findViewById(R.id.rank) ;
            name = itemView.findViewById(R.id.name) ;
            record = itemView.findViewById(R.id.record) ;
        }
    }


}