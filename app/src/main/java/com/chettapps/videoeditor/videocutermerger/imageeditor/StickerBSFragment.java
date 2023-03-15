package com.chettapps.videoeditor.videocutermerger.imageeditor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chettapps.videoeditor.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/* loaded from: classes.dex */
public class StickerBSFragment extends BottomSheetDialogFragment {
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() { // from class: com.chettapps.videoeditor.videocutermerger .imageeditor.StickerBSFragment.1
        @Override // android.support.design.widget.BottomSheetBehavior.BottomSheetCallback
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == 5) {
                StickerBSFragment.this.dismiss();
            }
        }

        @Override // android.support.design.widget.BottomSheetBehavior.BottomSheetCallback
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };
    private StickerListener mStickerListener;

    /* loaded from: classes.dex */
    public interface StickerListener {
        void onStickerClick(Bitmap bitmap);
    }

    public void setStickerListener(StickerListener stickerListener) {
        this.mStickerListener = stickerListener;
    }

    @Override // android.support.v7.app.AppCompatDialogFragment, android.support.v4.app.DialogFragment
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvEmoji.setLayoutManager(gridLayoutManager);
        StickerAdapter stickerAdapter = new StickerAdapter();
        rvEmoji.setAdapter(stickerAdapter);
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /* loaded from: classes.dex */
    public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {
        int[] stickerList = {R.drawable.aa, R.drawable.bb};

        public StickerAdapter() {
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sticker, parent, false);
            return new ViewHolder(view);
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.imgSticker.setImageResource(this.stickerList[position]);
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.stickerList.length;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSticker;

            ViewHolder(View itemView) {
                super(itemView);
                this.imgSticker = (ImageView) itemView.findViewById(R.id.imgSticker);
                itemView.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger .imageeditor.StickerBSFragment.StickerAdapter.ViewHolder.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        if (StickerBSFragment.this.mStickerListener != null) {
                            StickerBSFragment.this.mStickerListener.onStickerClick(BitmapFactory.decodeResource(StickerBSFragment.this.getResources(), StickerAdapter.this.stickerList[ViewHolder.this.getLayoutPosition()]));
                        }
                        StickerBSFragment.this.dismiss();
                    }
                });
            }
        }
    }

    private String convertEmoji(String emoji) {
        try {
            int convertEmojiToInt = Integer.parseInt(emoji.substring(2), 16);
            String returnedEmoji = getEmojiByUnicode(convertEmojiToInt);
            return returnedEmoji;
        } catch (NumberFormatException e) {
            return "";
        }
    }

    private String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }
}
