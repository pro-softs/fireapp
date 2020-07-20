package com.devlomi.fireapp.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.fireapp.R;
import com.devlomi.fireapp.model.ClassifiedModel;

import java.util.List;


public class ClassifiedAdapter extends RecyclerView.Adapter<ClassifiedAdapter.MyViewHolder> {

    private List<ClassifiedModel> ClassifiedModelsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date, description;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            date = (TextView) view.findViewById(R.id.date);
        }
    }


    public ClassifiedAdapter(List<ClassifiedModel> ClassifiedModelsList) {
        this.ClassifiedModelsList = ClassifiedModelsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_classified, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ClassifiedModel ClassifiedModel = ClassifiedModelsList.get(position);
        holder.title.setText(ClassifiedModel.getName());
        //holder.description.setText(ClassifiedModel.getName()+" Classified");
        holder.date.setText(ClassifiedModel.getDate());
    }

    @Override
    public int getItemCount() {
        return ClassifiedModelsList.size();
    }
}

