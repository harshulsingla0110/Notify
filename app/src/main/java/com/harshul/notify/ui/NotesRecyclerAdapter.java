package com.harshul.notify.ui;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.harshul.notify.R;
import com.harshul.notify.model.Note;

import java.util.List;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {

    private final Context context;
    private final List<Note> noteList;
    private final OnNoteClickListner onNoteClickListner;

    public NotesRecyclerAdapter(Context context, List<Note> noteList, OnNoteClickListner onNoteClickListner) {
        this.context = context;
        this.noteList = noteList;
        this.onNoteClickListner = onNoteClickListner;

    }

    @NonNull
    @Override
    public NotesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesRecyclerAdapter.ViewHolder holder, int position) {
        Note note = noteList.get(position);
        if (note.getTitle() == null || TextUtils.isEmpty(note.getTitle())) {
            holder.title.setVisibility(View.GONE);
        } else {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(note.getTitle());
        }

        holder.note.setText(note.getNote());
        String time = (String) DateUtils.getRelativeTimeSpanString(note.getTimeAdded().getSeconds() * 1000);
        holder.timeAgo.setText(time);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public interface OnNoteClickListner {
        void onClick(Note currNote);

        void onLongClick(Note currNote);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView note;
        private final TextView timeAgo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitle);
            note = itemView.findViewById(R.id.tvNote);
            timeAgo = itemView.findViewById(R.id.tvTimeAgo);
            ConstraintLayout constraintLayoutNote = itemView.findViewById(R.id.constraintLayoutNote);

            constraintLayoutNote.setOnClickListener(view -> {
                Note currNote = noteList.get(getAdapterPosition());
                onNoteClickListner.onClick(currNote);
            });

            constraintLayoutNote.setOnLongClickListener(view -> {
                Note currNote = noteList.get(getAdapterPosition());
                onNoteClickListner.onLongClick(currNote);
                return true;
            });
        }
    }
}
