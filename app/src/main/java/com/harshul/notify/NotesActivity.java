package com.harshul.notify;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.harshul.notify.util.Utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity implements NotesRecyclerAdapter.OnNoteClickListner {


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
        db.collection("Users").whereEqualTo("userId", FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            String userName = queryDocumentSnapshots.getDocuments().get(0).getString("userName");
            setUserData(userName);
        });

        adapter = new NotesRecyclerAdapter(mActivity, noteList, this);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        binding.recyclerView.setAdapter(adapter);

        binding.buttonNewNote.setOnClickListener(view -> startActivity(new Intent(mActivity, NewNoteActivity.class)));

    }

    private void searchNote(String s) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        binding.progressBar.setVisibility(View.VISIBLE);
        gettingData();


    }

    private void gettingData() {
        collectionReference.whereEqualTo("userId", FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            binding.progressBar.setVisibility(View.GONE);
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

    private void deleteNote(Note currNote) {
        Dialog deleteNotePopup = new Dialog(mActivity);
        deleteNotePopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteNotePopup.setContentView(R.layout.delete_note_dialog);
        deleteNotePopup.setCanceledOnTouchOutside(true);
        Button buttonDelete = deleteNotePopup.findViewById(R.id.buttonDelete);
        Button buttonCancel = deleteNotePopup.findViewById(R.id.buttonCancel);
        ProgressBar progressBar = deleteNotePopup.findViewById(R.id.progressBar);

        buttonCancel.setOnClickListener(v -> deleteNotePopup.cancel());

        buttonDelete.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            collectionReference.document(currNote.getDocumentId())
                    .delete().addOnSuccessListener(unused -> {
                        progressBar.setVisibility(View.GONE);
                        deleteNotePopup.dismiss();
                        gettingData();
                        Toast.makeText(mActivity, "Note deleted", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(mActivity, "Error occurred. Please try after some time.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    });
        });

        deleteNotePopup.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        deleteNotePopup.show();
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        Window window = deleteNotePopup.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onClick(Note currNote) {
        Intent intent = new Intent(mActivity, NewNoteActivity.class);
        intent.putExtra("note", currNote);
        intent.putExtra("isEdit", true);
        startActivity(intent);

    }

    @Override
    public void onLongClick(Note currNote) {
        deleteNote(currNote);
    }

    private void setUserData(String name) {
        if (name != null && !TextUtils.isEmpty(name))
            binding.textViewHeading.setText(MessageFormat.format("Hi, {0} ! \uD83D\uDC4B", name.split(" ")[0]));
        else
            binding.textViewHeading.setText(Utils.textColor(mActivity, getString(R.string.hello), R.color.light_grey, 4, 7));

    }
}