package com.example.project_resume_collection;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class FragmentAkun extends Fragment {

    ImageView fotoMahasiswa;
    EditText viewNama;
    EditText viewNim;
    EditText viewJurusan;
    EditText viewKelas;
    EditText viewProdi;
    EditText viewWhatsapp;
    Button updateProfileBtn;
    ProgressBar progressBar;
    TextView logoutBtn;

    DataMahasiswa currentuserModel;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;
    public FragmentAkun() {

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedImageUri = data.getData();
                            AndroidUtil.setProfilePic(getContext(),selectedImageUri,fotoMahasiswa);
                        }
                    }
                }
        );
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.akun, container, false);

        fotoMahasiswa = view.findViewById(R.id.profile_image_view);
        viewNama = view.findViewById(R.id.nama);
        viewNim = view.findViewById(R.id.nim);
        viewKelas = view.findViewById(R.id.kelas);
        viewProdi = view.findViewById(R.id.prodi);
        viewJurusan = view.findViewById(R.id.jurusan);
        viewWhatsapp = view.findViewById(R.id.whatsapp);

        updateProfileBtn = view.findViewById(R.id.profle_update_btn);
        progressBar = view.findViewById(R.id.profile_progress_bar);
        logoutBtn = view.findViewById(R.id.logout_btn);

        getUserData();


        updateProfileBtn.setOnClickListener((v -> {
            updateBtnClick();
        }));
        logoutBtn.setOnClickListener((v)->{

            FirebaseUtil.logout();
            Intent intent = new Intent(getContext(),Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        });
        fotoMahasiswa.setOnClickListener((v)->{
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });
        return view;
    }
    void updateBtnClick(){
        String newnama = viewNama.getText().toString();
        if(newnama.isEmpty() || newnama.length()<3){
            viewNama.setError("Username length should be at least 3 chars");
            return;
        }
        currentuserModel.setNama(newnama);
        setInProgress(true);


        if(selectedImageUri!=null){
            FirebaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImageUri)
                    .addOnCompleteListener(task -> {
                        updateToFirestore();
                    });
        }else{
            updateToFirestore();
        }

    }
    void updateToFirestore(){
        FirebaseUtil.currentUserDetails().set(currentuserModel)
                .addOnCompleteListener(task -> {
                    setInProgress(false);
                    if(task.isSuccessful()){
                        AndroidUtil.showToast(getContext(),"Updated successfully");
                    }else{
                        AndroidUtil.showToast(getContext(),"Updated failed");
                    }
                });
    }

    void getUserData(){
        setInProgress(true);

        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri  = task.getResult();
                        AndroidUtil.setProfilePic(getContext(),uri,fotoMahasiswa);
                    }
                });

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            currentuserModel = task.getResult().toObject(DataMahasiswa.class);
            viewNama.setText(currentuserModel.getNama());
            viewNim.setText(currentuserModel.getNim());
            viewKelas.setText(currentuserModel.getKelas());
            viewProdi.setText(currentuserModel.getProdi());
            viewJurusan.setText(currentuserModel.getJurusan());
            viewWhatsapp.setText(currentuserModel.getWhatsapp());
        });
    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            updateProfileBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            updateProfileBtn.setVisibility(View.VISIBLE);
        }
    }
}
