package com.example.project_resume_collection;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentTugas extends Fragment {
    Button resumeUpload;
    Button petunjukResume;
    public FragmentTugas() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tugas, container, false);

        resumeUpload = view.findViewById(R.id.uploadResume);
        petunjukResume = view.findViewById(R.id.petunukResume);

        resumeUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UploadResume.class);
                startActivity(intent);
            }
        });


        petunjukResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PetunjukResume.class);
                startActivity(intent);
            }
        });
        return view;

    }
}