package com.example.vacationscheduler.UI;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.R;
import com.example.vacationscheduler.entities.Excursions;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private List<Excursions> mExcursions;
    private Context context;
    private String startDate;
    private String endDate;

    private final LayoutInflater mInflater;

    class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private final TextView excursionItemView;
        private final TextView excursionItemView2;

        private ExcursionViewHolder(View itemView) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.excursionTextView1);
            excursionItemView2 = itemView.findViewById(R.id.excursionTextView2);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) { // Check if position is valid
                    Excursions current = mExcursions.get(position);
                    Intent intent = new Intent(context, ExcursionDetails.class);
                    intent.putExtra("id", current.getId());
                    intent.putExtra("title", current.getTitle());
                    intent.putExtra("date", current.getDate());
                    intent.putExtra("vacationID", current.getVacationID());
                    intent.putExtra("startDate", startDate);
                    intent.putExtra("endDate", endDate);
                    context.startActivity(intent);
                    Log.d("ExcursionAdapter", "Sending data - ID: " + current.getId() + ", Title: " + current.getTitle());
                }
            });
        }
    }
    public ExcursionAdapter(Context context, List<Excursions> excursions, String startDate, String endDate) {
        this.context = context;
        this.mExcursions = excursions;
        this.startDate = startDate;
        this.endDate = endDate;
        this.mInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.excursion_list_item,parent, false);
        return new ExcursionViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if(mExcursions != null && mExcursions.size() > position) {
            Excursions current = mExcursions.get(position);
            String title = current.getTitle();
            String date = current.getDate();
            int vacationID = current.getVacationID();

            Log.d("ExcursionAdapter", "Binding data at position " + position + ": Title=" + title + ", Date=" + date);

            holder.excursionItemView.setText(title);
            holder.excursionItemView2.setText(date);
        } else {

            Log.d("ExcursionAdapter", "No data available for position " + position);

            holder.excursionItemView.setText("No title available");
            holder.excursionItemView2.setText("No date available");
        }
    }
    public void setmExcursions(List<Excursions> excursions){
        mExcursions = excursions;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
            return mExcursions.size();
    }


}
