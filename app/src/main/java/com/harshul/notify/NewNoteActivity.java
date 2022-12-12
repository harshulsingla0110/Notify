package com.harshul.notify;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harshul.notify.databinding.ActivityNewNoteBinding;
import com.harshul.notify.model.Note;
import com.harshul.notify.util.NotesApi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NewNoteActivity extends AppCompatActivity {


    private final Activity mActivity = NewNoteActivity.this;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Notes");
    private ActivityNewNoteBinding binding;
    private String userid;
    private Boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_note);

        Note currNote = getIntent().getParcelableExtra("note");
        isEdit = getIntent().getBooleanExtra("isEdit", false);

        if (currNote != null) {
            binding.etTitle.setText(currNote.getTitle());
            binding.etNote.setText(currNote.getNote());
        }

        String todayDate = new SimpleDateFormat("dd MMMM yyyy, hh:mm aaa", Locale.getDefault()).format(new Date());
        binding.tvDate.setText(todayDate);

        if (NotesApi.getInstance() != null) {
            userid = NotesApi.getInstance().getUserId();
        }

        binding.etNote.requestFocus();

        binding.buttonBack.setOnClickListener(view -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            String title = binding.etTitle.getText().toString().trim();
            String note = binding.etNote.getText().toString().trim();

            if (TextUtils.isEmpty(title) && TextUtils.isEmpty(note)) {
                finish();
            } else {
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("title", title);
                updateData.put("note", note);
                if (isEdit && currNote != null) {
                    collectionReference.document(currNote.getDocumentId()).update(updateData).addOnSuccessListener(unused -> finish());
                } else {
                    Note noteData = new Note();
                    noteData.setTitle(title);
                    noteData.setNote(note);
                    noteData.setTimeAdded(new Timestamp(new Date()));
                    noteData.setUserId(FirebaseAuth.getInstance().getUid());

                    collectionReference.add(noteData).addOnSuccessListener(documentReference -> {
                        Map<String, Object> data = new HashMap<>();
                        data.put("documentId", documentReference.getId());
                        documentReference.update(data).addOnSuccessListener(unused -> {
                            finish();
                        });

                    });
                }
            }
        });


    }
}