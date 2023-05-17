package com.supriya.whoami.guardian.reminder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.supriya.whoami.R;
import com.supriya.whoami.room.DataDAO;
import com.supriya.whoami.room.ReminderEntity;
import com.supriya.whoami.room.WhoAmIDB;

import java.util.List;

public class EventAdapterG extends RecyclerView.Adapter<EventAdapterG.ViewHolder> {
    Context context;
    List<ReminderEntity> entityClasses;

    public EventAdapterG(Context context, List<ReminderEntity> entityClasses) {
        this.context = context;
        this.entityClasses = entityClasses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listings_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.eventText.setText(entityClasses.get(position).getEventname());
        holder.timeAndDateText.setText(entityClasses.get(position).getEventdate() + " | " + entityClasses.get(position).getEventtime());

        holder.deleteReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataDAO dataDAO = WhoAmIDB.getInstance(context).dataDAO();
                dataDAO.deleteById(entityClasses.get(position).getId());
                entityClasses.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return entityClasses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView eventText, timeAndDateText;
        private LinearLayout toplayout;
        private ImageView deleteReminder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventText = (TextView) itemView.findViewById(R.id.event);
            timeAndDateText = (TextView) itemView.findViewById(R.id.time_and_date);
            deleteReminder = (ImageView) itemView.findViewById(R.id.deleteReminder);
        }
    }
}
