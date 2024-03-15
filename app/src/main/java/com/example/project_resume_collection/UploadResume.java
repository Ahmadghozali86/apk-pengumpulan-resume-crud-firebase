package com.example.project_resume_collection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadResume extends AppCompatActivity {

    private static final int[] FILE_REQUEST_CODES = {101, 102, 103};
    private TextView[] textViewUploadedFiles;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseFirestore db;
    private ProgressDialog[] progressDialogs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_resume);

        progressDialogs = new ProgressDialog[]{
                new ProgressDialog(this),
                new ProgressDialog(this),
                new ProgressDialog(this)
        };
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = FirebaseFirestore.getInstance();

        Button[] buttonChooseFiles = new Button[]{
                findViewById(R.id.buttonChooseFile1),
                findViewById(R.id.buttonChooseFile2),
                findViewById(R.id.buttonChooseFile3)
        };

        for (int i = 0; i < buttonChooseFiles.length; i++) {
            int finalI = i;
            buttonChooseFiles[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickPDFFile(FILE_REQUEST_CODES[finalI]);
                }
            });
        }
    }

    private void pickPDFFile(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Pilih File PDF"), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();
            uploadFileToFirebaseStorage(selectedFileUri, requestCode);
        }
    }

    private void uploadFileToFirebaseStorage(Uri fileUri, int requestCode) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            StorageReference pdfRef = storageRef.child("tugas/" + userId + "/file_minggu" + (requestCode % 100) + ".pdf");

            UploadTask uploadTask = pdfRef.putFile(fileUri);

            ProgressDialog progressDialog = progressDialogs[requestCode % 100 - 1];
            progressDialog.setMessage("Mengunggah File Minggu " + (requestCode % 100) + "...");

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                pdfRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    saveUrlToFirestore(downloadUrl, requestCode);
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }


                });
            }).addOnFailureListener(e -> {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                // Gagal mengunggah file
            });
            progressDialog.show();
        }
    }

    private void saveUrlToFirestore(String downloadUrl, int requestCode) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference docRef = db.collection("tugas").document(userId);

            docRef.update("urlFileMinggu" + (requestCode % 100), downloadUrl)
                    .addOnSuccessListener(aVoid -> {
                        textViewUploadedFiles[requestCode % 100 - 1].setText(" Sudah diunggah");
                    })
                    .addOnFailureListener(e -> {
                        // Gagal menyimpan URL unduhan
                    });
        }
    }


}