package com.chettapps.videoeditor.videocutermerger.imageeditor;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chettapps.videoeditor.R;



public class TextEditorDialogFragment extends DialogFragment {
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String TAG = TextEditorDialogFragment.class.getSimpleName();
    private TextView mAddTextDoneTextView;
    private EditText mAddTextEditText;
    private int mColorCode;
    private InputMethodManager mInputMethodManager;
    private TextEditor mTextEditor;


    public interface TextEditor {
        void onDone(String str, int i);
    }

    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity, @NonNull String inputText, @ColorInt int colorCode) {
        Bundle args = new Bundle();
        args.putString(EXTRA_INPUT_TEXT, inputText);
        args.putInt(EXTRA_COLOR_CODE, colorCode);
        TextEditorDialogFragment fragment = new TextEditorDialogFragment();
        fragment.setArguments(args);
        fragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return fragment;
    }

    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity) {
        return show(appCompatActivity, "", ContextCompat.getColor(appCompatActivity, R.color.white));
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_text_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mAddTextEditText = (EditText) view.findViewById(R.id.add_text_edit_text);
        this.mInputMethodManager = (InputMethodManager) getActivity().getSystemService("input_method");
        this.mAddTextDoneTextView = (TextView) view.findViewById(R.id.add_text_done_tv);
        RecyclerView addTextColorPickerRecyclerView = (RecyclerView) view.findViewById(R.id.add_text_color_picker_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), 0, false);
        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
        addTextColorPickerRecyclerView.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(getActivity());
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {

            @Override

            public void onColorPickerClickListener(int colorCode) {
                TextEditorDialogFragment.this.mColorCode = colorCode;
                TextEditorDialogFragment.this.mAddTextEditText.setTextColor(colorCode);
            }
        });
        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);
        this.mAddTextEditText.setText(getArguments().getString(EXTRA_INPUT_TEXT));
        this.mColorCode = getArguments().getInt(EXTRA_COLOR_CODE);
        this.mAddTextEditText.setTextColor(this.mColorCode);
        this.mInputMethodManager.toggleSoftInput(2, 0);
        this.mAddTextDoneTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view2) {
                TextEditorDialogFragment.this.mInputMethodManager.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                TextEditorDialogFragment.this.dismiss();
                String inputText = TextEditorDialogFragment.this.mAddTextEditText.getText().toString();
                if (!TextUtils.isEmpty(inputText) && TextEditorDialogFragment.this.mTextEditor != null) {
                    TextEditorDialogFragment.this.mTextEditor.onDone(inputText, TextEditorDialogFragment.this.mColorCode);
                }
            }
        });
    }

    public void setOnTextEditorListener(TextEditor textEditor) {
        this.mTextEditor = textEditor;
    }
}
