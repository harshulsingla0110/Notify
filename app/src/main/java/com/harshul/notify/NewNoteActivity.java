package com.harshul.notify;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harshul.notify.databinding.ActivityNewNoteBinding;
import com.harshul.notify.model.Note;
import com.harshul.notify.util.NotesApi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewNoteActivity extends AppCompatActivity {


    private final Activity mActivity = NewNoteActivity.this;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Notes");
    private ActivityNewNoteBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private String userName;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_note);

        firebaseAuth = FirebaseAuth.getInstance();

        String todayDate = new SimpleDateFormat("dd MMMM yyyy, hh:mm aaa", Locale.getDefault()).format(new Date());
        binding.tvDate.setText(todayDate);

        if (NotesApi.getInstance() != null) {
            userid = NotesApi.getInstance().getUserId();
        }

        authStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if (user != null) {
                //user logged in
            } else {
                //user not logged in
            }
        };

        binding.buttonBack.setOnClickListener(view -> {
            String title = binding.etTitle.getText().toString().trim();
            String note = binding.etNote.getText().toString().trim();
            if (TextUtils.isEmpty(title) && TextUtils.isEmpty(note)) {
                finish();
            } else {
                Note noteData = new Note();
                noteData.setTitle(title);
                noteData.setNote(note);
                noteData.setTimeAdded(new Timestamp(new Date()));
                noteData.setUserId(firebaseAuth.getUid());

                collectionReference.add(noteData).addOnSuccessListener(documentReference -> {
                    finish();
                });
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null) firebaseAuth.removeAuthStateListener(authStateListener);
    }
}