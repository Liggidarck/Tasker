package com.george.android.tasker.ui.passwords;

import static com.george.android.tasker.ui.passwords.PasswordsViewModel.randomPassword;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.george.android.tasker.databinding.FragmentPasswordGeneratorBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class PasswordGeneratorFragment extends Fragment {

    FragmentPasswordGeneratorBinding generatorBinding;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        generatorBinding = FragmentPasswordGeneratorBinding.inflate(inflater, container, false);
        View root = generatorBinding.getRoot();
        generatorBinding.toolbarGenerator.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        generatorBinding.lengthPasswordSlider.setStepSize(1);
        generatorBinding.lengthPasswordSlider.setValueFrom(1);
        generatorBinding.lengthPasswordSlider.setValueTo(32);
        generatorBinding.lengthPasswordSlider.setValue(16);

        generatorBinding.passwordText.setText(randomPassword(16, true, true));

        generatorBinding.generatePassword.setOnClickListener(v -> {
            int length = (int) generatorBinding.lengthPasswordSlider.getValue();
            boolean isSymbols = generatorBinding.checkBoxSymbols.isChecked();
            boolean isNumbers = generatorBinding.checkBoxNumbers.isChecked();
            generatorBinding.passwordText.setText(randomPassword(length, isSymbols, isNumbers));
        });

        generatorBinding.lengthPasswordSlider.addOnChangeListener((slider, value, fromUser) -> {
            boolean isSymbols = generatorBinding.checkBoxSymbols.isChecked();
            boolean isNumbers = generatorBinding.checkBoxNumbers.isChecked();

            generatorBinding.passwordText.setText(randomPassword((int) value, isSymbols, isNumbers));

            Vibrator vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.EFFECT_TICK));
            } else {
                vibrator.vibrate(100);
            }
        });

        generatorBinding.copyPassword.setOnClickListener(this::onCopyBtnClick);

        return root;
    }

    public void onCopyBtnClick(View view) {
        if (Objects.requireNonNull(generatorBinding.passwordText.getText()).toString().equals("Сгенерируй меня!")) {
            Snackbar.make(view, "Сгенерируй пароль что бы его скопировать", Snackbar.LENGTH_SHORT).setAction("error", null).show();
        } else {
            ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", generatorBinding.passwordText.getText().toString());
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Snackbar.make(view, "Пароль скопирован", Snackbar.LENGTH_SHORT).setAction("done", null).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        generatorBinding = null;
    }
}