package com.example.project_resume_collection;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn(){
        if(currentUserId()!=null){
            return true;
        }
        return false;
    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("Mahasiswa").document(currentUserId());
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("Mahasiswa");
    }


    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }

    public static StorageReference  getCurrentProfilePicStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("Foto Mahasiswa")
                .child(FirebaseUtil.currentUserId());
    }

    public static StorageReference  getOtherProfilePicStorageRef(String otherUserId){
        return FirebaseStorage.getInstance().getReference().child("Foto Mahasiswa")
                .child(otherUserId);
    }
}










