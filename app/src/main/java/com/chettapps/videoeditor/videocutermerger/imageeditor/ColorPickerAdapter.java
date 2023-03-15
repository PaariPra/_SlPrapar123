package com.chettapps.videoeditor.videocutermerger.imageeditor;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.chettapps.videoeditor.R;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {
    private List<Integer> colorPickerColors;
    private Context context;
    private LayoutInflater inflater;
    private OnColorPickerClickListener onColorPickerClickListener;

    /* loaded from: classes.dex */
    public interface OnColorPickerClickListener {
        void onColorPickerClickListener(int i);
    }

    ColorPickerAdapter(@NonNull Context context, @NonNull List<Integer> colorPickerColors) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.colorPickerColors = colorPickerColors;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorPickerAdapter(@NonNull Context context) {
        this(context, getDefaultColors(context));
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.inflater.inflate(R.layout.color_picker_item_list, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.colorPickerView.setBackgroundColor(this.colorPickerColors.get(position).intValue());
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.colorPickerColors.size();
    }

    private void buildColorPickerView(View view, int colorCode) {
        view.setVisibility(View.VISIBLE);
        ShapeDrawable biggerCircle = new ShapeDrawable(new OvalShape());
        biggerCircle.setIntrinsicHeight(20);
        biggerCircle.setIntrinsicWidth(20);
        biggerCircle.setBounds(new Rect(0, 0, 20, 20));
        biggerCircle.getPaint().setColor(colorCode);
        ShapeDrawable smallerCircle = new ShapeDrawable(new OvalShape());
        smallerCircle.setIntrinsicHeight(5);
        smallerCircle.setIntrinsicWidth(5);
        smallerCircle.setBounds(new Rect(0, 0, 5, 5));
        smallerCircle.getPaint().setColor(-1);
        smallerCircle.setPadding(10, 10, 10, 10);
        Drawable[] drawables = {smallerCircle, biggerCircle};
        LayerDrawable layerDrawable = new LayerDrawable(drawables);
        view.setBackgroundDrawable(layerDrawable);
    }

    public void setOnColorPickerClickListener(OnColorPickerClickListener onColorPickerClickListener) {
        this.onColorPickerClickListener = onColorPickerClickListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        View colorPickerView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.colorPickerView = itemView.findViewById(R.id.color_picker_view);
            itemView.setOnClickListener(new View.OnClickListener() { // from class: com.chettapps.videoeditor.videocutermerger.imageeditor.ColorPickerAdapter.ViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (ColorPickerAdapter.this.onColorPickerClickListener != null) {
                        ColorPickerAdapter.this.onColorPickerClickListener.onColorPickerClickListener(((Integer) ColorPickerAdapter.this.colorPickerColors.get(ViewHolder.this.getAdapterPosition())).intValue());
                    }
                }
            });
        }
    }

    public static List<Integer> getDefaultColors(Context context)
    {

        ArrayList<Integer> colorPickerColors = new ArrayList<>();
        colorPickerColors.add(Integer.valueOf(ContextCompat.getColor(context, R.color.blue_color_picker)));
        colorPickerColors.add(Integer.valueOf(ContextCompat.getColor(context, R.color.brown_color_picker)));
        colorPickerColors.add(Integer.valueOf(ContextCompat.getColor(context, R.color.green_color_picker)));
        colorPickerColors.add(Integer.valueOf(ContextCompat.getColor(context, R.color.orange_color_picker)));
        colorPickerColors.add(Integer.valueOf(ContextCompat.getColor(context, R.color.red_color_picker)));
        colorPickerColors.add(Integer.valueOf(ContextCompat.getColor(context, R.color.black)));
        colorPickerColors.add(Integer.valueOf(ContextCompat.getColor(context, R.color.red_orange_color_picker)));
        colorPickerColors.add(Integer.valueOf(ContextCompat.getColor(context, R.color.sky_blue_color_picker)));
        colorPickerColors.add(Integer.valueOf(ContextCompat.getColor(context, R.color.violet_color_picker)));
        colorPickerColors.add(Integer.valueOf(ContextCompat.getColor(context, R.color.white)));
        colorPickerColors.add(Integer.valueOf(ContextCompat.getColor(context, R.color.yellow_color_picker)));
        colorPickerColors.add(Integer.valueOf(ContextCompat.getColor(context, R.color.yellow_green_color_picker)));
        return colorPickerColors;


    }
}
