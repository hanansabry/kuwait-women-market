package com.android.kwm.data.storage;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import androidx.annotation.NonNull;

public class ImagesStorageImpl implements ImagesStorage {

    private FirebaseStorage firebaseStorage;

    public ImagesStorageImpl() {
        firebaseStorage = FirebaseStorage.getInstance();

    }

    @Override
    public void uploadImage(Uri filePath, String categoryId, String modelId, final UploadImageCallback callback) {
        if (filePath != null && !filePath.toString().contains("https://firebasestorage.googleapis")) {
            String storagePath = "categories/" + categoryId + "/models/" + modelId + "/" + UUID.randomUUID().toString();
            final StorageReference storageRef = firebaseStorage.getReference().child(storagePath);
            storageRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            callback.onSuccessfullyImageUploaded(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Upload image", e.getMessage());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onImageUploadedFailed(e.getMessage());
                }
            });
        }
    }
}
