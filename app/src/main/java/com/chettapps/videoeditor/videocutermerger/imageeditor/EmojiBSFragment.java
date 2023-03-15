package com.chettapps.videoeditor.videocutermerger.imageeditor;

import android.annotation.SuppressLint;
import android.app.Dialog;


import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chettapps.videoeditor.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.PhotoEditor;

/* loaded from: classes.dex */
public class EmojiBSFragment extends BottomSheetDialogFragment {
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() { // from class: com.chettapps.videoeditor.videocutermerger.imageeditor.EmojiBSFragment.1
        @Override // android.support.design.widget.BottomSheetBehavior.BottomSheetCallback
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == 5) {
                EmojiBSFragment.this.dismiss();
            }
        }

        @Override // android.support.design.widget.BottomSheetBehavior.BottomSheetCallback
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };
    private EmojiListener mEmojiListener;

    /* loaded from: classes.dex */
    public interface EmojiListener {
        void onEmojiClick(String str);
    }

    @Override
    // android.support.v7.app.AppCompatDialogFragment, android.support.v4.app.DialogFragment
    @SuppressLint({"RestrictedApi", "ResourceType"})
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sticker_emoji_dialog, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        if (behavior != null && (behavior instanceof BottomSheetBehavior)) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(this.mBottomSheetBehaviorCallback);
        }
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(17170445));
        RecyclerView rvEmoji = (RecyclerView) contentView.findViewById(R.id.rvEmoji);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        rvEmoji.setLayoutManager(gridLayoutManager);
        EmojiAdapter emojiAdapter = new EmojiAdapter();
        rvEmoji.setAdapter(emojiAdapter);
    }

    public void setEmojiListener(EmojiListener emojiListener) {
        this.mEmojiListener = emojiListener;
    }

    public static ArrayList<String> getEmojis(Context context) {
        ArrayList<String> convertedEmojiList = new ArrayList<String>();
        String[] emojiList = context.getResources().getStringArray(R.array.photo_editor_emoji);


        for (int i = 0; i < emojiList.length; i++) {
            convertedEmojiList.add(convertedEmojiList.get(i));
        }


        return convertedEmojiList;
    }



    public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {
        ArrayList<String> emojisList;

        public EmojiAdapter() {
            this.emojisList = getEmojis(EmojiBSFragment.this.getActivity());
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_emoji, parent, false);
            return new ViewHolder(view);
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.txtEmoji.setText(this.emojisList.get(position));
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.emojisList.size();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtEmoji;

            ViewHolder(View itemView) {
                super(itemView);
                this.txtEmoji = (TextView) itemView.findViewById(R.id.txtEmoji);
                itemView.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger.imageeditor.EmojiBSFragment.EmojiAdapter.ViewHolder.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        if (EmojiBSFragment.this.mEmojiListener != null) {
                            EmojiBSFragment.this.mEmojiListener.onEmojiClick(EmojiAdapter.this.emojisList.get(ViewHolder.this.getLayoutPosition()));
                        }
                        EmojiBSFragment.this.dismiss();
                    }
                });
            }
        }
    }
}
