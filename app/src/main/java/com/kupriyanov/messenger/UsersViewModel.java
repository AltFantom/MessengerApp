package com.kupriyanov.messenger;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends AndroidViewModel {

    private final MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    private final FirebaseAuth auth;
    private MutableLiveData <List<User>> users = new MutableLiveData<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersReference;

    public UsersViewModel(@NonNull Application application) {
        super(application);
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user.setValue(firebaseAuth.getCurrentUser());
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("Users");
        setUsers();
    }

    private void setUsers() {
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser == null) {
                    return;
                }
                List<User> usersList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user == null) {
                        return;
                    }
                    if (!user.getId().equals(currentUser.getUid())) {
                        usersList.add(user);
                    }
                }
                users.setValue(usersList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void setUserOnline(boolean isOnline) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        usersReference.child(firebaseUser.getUid()).child("online").setValue(isOnline);
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public void logout() {
        setUserOnline(false);
        auth.signOut();
    }
}
