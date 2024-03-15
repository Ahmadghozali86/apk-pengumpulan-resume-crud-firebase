package com.example.project_resume_collection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;

public class LengkapiDataMahasiswa extends AppCompatActivity {

    TextInputEditText editTextEmailku, editTextNama,editTextKelas, editTextNim,editTextProdi, editTextJurusan, editTextWhatsapp;
    Button buttonSimpan;
    ProgressBar progressBar;
    String Email;
    DataMahasiswa userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lengkapi_data_mahasiswa);
        editTextEmailku = findViewById(R.id.emailku);
        editTextNama = findViewById(R.id.nama);
        editTextKelas = findViewById(R.id.kelas);
        editTextNim = findViewById(R.id.nim);
        editTextProdi = findViewById(R.id.prodi);
        editTextJurusan = findViewById(R.id.jurusan);
        editTextWhatsapp = findViewById(R.id.whatsapp);


        buttonSimpan = findViewById(R.id.btn_simpan);
        progressBar = findViewById(R.id.progressBar);

        //Email = getIntent().getExtras().getString("email");
        //editTextEmailku.setText(""+Email);

        getData();

        buttonSimpan.setOnClickListener((v -> {
            setNama();
        }));
    }
    void setNama(){
        String Emailku = editTextEmailku.getText().toString();
        String Namaku = editTextNama.getText().toString();
        String Kelasku = editTextKelas.getText().toString();
        String Nimku = editTextNim.getText().toString();
        String Prodiku = editTextProdi.getText().toString();
        String Jurusanku = editTextJurusan.getText().toString();
        String Whatsapp = editTextWhatsapp.getText().toString();

        userModel= new DataMahasiswa(Namaku, Kelasku, Nimku, Prodiku, Jurusanku, Emailku, Whatsapp);
        setInProgress(true);

        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    Intent intent = new Intent(LengkapiDataMahasiswa.this,Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity(intent);
                }
            }
        });
    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            buttonSimpan.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            buttonSimpan.setVisibility(View.VISIBLE);
        }
    }
    void getData(){
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    userModel =    task.getResult().toObject(DataMahasiswa.class);
                    if(userModel!=null){
                        editTextNama.setText(userModel.getNama());
                        editTextKelas.setText(userModel.getKelas());
                        editTextNim.setText(userModel.getNim());
                        editTextProdi.setText(userModel.getProdi());
                        editTextJurusan.setText(userModel.getJurusan());
                        editTextEmailku.setText(userModel.getEmail());
                        editTextWhatsapp.setText(userModel.getWhatsapp());
                    }
                }
            }
        });
    }
}