package com.harshul.notify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.harshul.notify.databinding.ActivityNotesBinding;
import com.harshul.notify.model.Note;
import com.harshul.notify.ui.NotesRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {


    private final Activity mActivity = NotesActivity.this;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Notes");
    private ActivityNotesBinding binding;
    private List<Note> noteList = new ArrayList<>();
    private NotesRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notes);

        //get data and set name

        adapter = new NotesRecyclerAdapter(mActivity, noteList);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        binding.recyclerView.setAdapter(adapter);

        binding.buttonNewNote.setOnClickListener(view -> startActivity(new Intent(mActivity, NewNoteActivity.class)));
    }


    @Override
    protected void onStart() {
        super.onStart();

        collectionReference.whereEqualTo("userId", FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                noteList.clear();
                for (QueryDocumentSnapshot notes : queryDocumentSnapshots) {
                    Note note = notes.toObject(Note.class);
                    noteList.add(note);
                }

                adapter.notifyDataSetChanged();
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.constraintLayoutBanner.setVisibility(View.GONE);
            } else {
                binding.recyclerView.setVisibility(View.GONE);
                binding.constraintLayoutBanner.setVisibility(View.VISIBLE);
            }
        });

    }
}