package com.george.android.tasker.data.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.george.android.tasker.data.repository.PasswordRepository;
import com.george.android.tasker.data.model.Password;

import java.util.List;
import java.util.Random;

public class PasswordsViewModel extends AndroidViewModel {

    private final PasswordRepository repository;
    private final LiveData<List<Password>> allPasswords;

    public PasswordsViewModel(Application application) {
        super(application);
        repository = new PasswordRepository(application);
        allPasswords = repository.getAllPasswords();
    }

    public void insert(Password password) {
        repository.insert(password);
    }

    public void update(Password password) {
        repository.update(password);
    }

    public void delete(int passwordId) {
        repository.delete(passwordId);
    }

    public LiveData<List<Password>> getAllPasswords() {
        return allPasswords;
    }

    public LiveData<List<Password>> findPassword(String search) {
        return repository.findPassword(search);
    }

    public static String randomPassword(int length, boolean isSymbols, boolean isNumbers) {
        String chars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        String numbers = "1234567890";
        String symbols = "%*)?@#$~";
        String password;

        if(isSymbols & isNumbers)
            password =  numbers + chars + symbols;
        else if(!isSymbols & isNumbers)
            password = numbers + chars + numbers;
        else if(isSymbols)
            password = symbols + chars + symbols;
        else
            password = chars;

        StringBuilder result = new StringBuilder();
        while (length > 0) {
            Random rand = new Random();
            result.append(password.charAt(rand.nextInt(password.length())));
            length--;
        }

        return result.toString();
    }

}