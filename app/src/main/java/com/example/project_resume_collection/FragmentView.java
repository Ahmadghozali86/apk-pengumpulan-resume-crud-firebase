package com.example.project_resume_collection;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentView extends Fragment {
    private RecyclerView recyclerView;
    private FileAdapter fileAdapter;
    private List<StorageReference> fileRefs;
    private Context context;

    public FragmentView() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view, container, false);
        context = view.getContext();

        recyclerView = view.findViewById(R.id.recyclerViewFiles);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fileRefs = new ArrayList<>();
        fileAdapter = new FileAdapter(fileRefs);
        recyclerView.setAdapter(fileAdapter);

        loadFilesFromStorage();

        return view;
    }

    private void loadFilesFromStorage() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("tugas").child(userId);

            storageRef.listAll().addOnSuccessListener(listResult -> {
                fileRefs.addAll(listResult.getItems());
                fileAdapter.notifyDataSetChanged();
            }).addOnFailureListener(e -> {
                Toast.makeText(getActivity(), "Gagal memuat file.", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
        private List<StorageReference> fileList;

        public FileAdapter(List<StorageReference> fileList) {
            this.fileList = fileList;
        }

        @NonNull
        @Override
        public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
            return new FileViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
            StorageReference fileRef = fileList.get(position);
            holder.bind(fileRef);
        }

        @Override
        public int getItemCount() {
            return fileList.size();
        }

        public class FileViewHolder extends RecyclerView.ViewHolder {

            private TextView fileNameTextView;
            private TextView uploadDateTextView;
            private TextView fileSizeTextView;

            public FileViewHolder(@NonNull View itemView) {
                super(itemView);
                fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
                uploadDateTextView = itemView.findViewById(R.id.uploadDateTextView);
                fileSizeTextView = itemView.findViewById(R.id.fileSizeTextView);

                itemView.setOnClickListener(v -> {
                    StorageReference selectedFileRef = fileList.get(getAdapterPosition());
                    downloadFile(selectedFileRef);
                });
            }

            public void bind(StorageReference fileRef) {
                fileRef.getMetadata().addOnSuccessListener(storageMetadata -> {
                    fileNameTextView.setText(storageMetadata.getName());

                    long uploadTimeMillis = storageMetadata.getCreationTimeMillis();
                    Date uploadDate = new Date(uploadTimeMillis);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                    String formattedDate = sdf.format(uploadDate);

                    uploadDateTextView.setText("Di Kirim       : " + formattedDate);

                    long fileSizeBytes = storageMetadata.getSizeBytes();
                    String fileSizeStr = android.text.format.Formatter.formatShortFileSize(context, fileSizeBytes);
                    fileSizeTextView.setText(  "Ukuran File : " + fileSizeStr);
                }).addOnFailureListener(e -> {
                });
            }
        }
    }

    private void downloadFile(StorageReference fileRef) {
        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Uri downloadUri = Uri.parse(uri.toString());

            Intent viewIntent = new Intent(Intent.ACTION_VIEW);
            viewIntent.setDataAndType(downloadUri, "application/pdf");
            viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(viewIntent);
        }).addOnFailureListener(exception -> {
            // Gagal mendapatkan URL unduhan file
            Toast.makeText(getActivity(), "Gagal mengunduh file.", Toast.LENGTH_SHORT).show();
        });
    }
}