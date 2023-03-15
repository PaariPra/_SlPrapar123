package com.chettapps.videoeditor.videocutermerger.imageeditor.tools;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.chettapps.videoeditor.R;
import java.util.ArrayList;
import java.util.List;


public class EditingToolsAdapter extends RecyclerView.Adapter<EditingToolsAdapter.ViewHolder>
{
    private OnItemSelected mOnItemSelected;
    private List<ToolModel> mToolList = new ArrayList();



    public interface OnItemSelected {
        void onToolSelected(ToolType toolType);
    }

    public EditingToolsAdapter(OnItemSelected onItemSelected) {
        this.mOnItemSelected = onItemSelected;
        this.mToolList.add(new ToolModel("Brush", R.drawable.ic_brush, ToolType.BRUSH));
        this.mToolList.add(new ToolModel("Text", R.drawable.ic_text, ToolType.TEXT));
        this.mToolList.add(new ToolModel("Eraser", R.drawable.ic_eraser, ToolType.ERASER));
        this.mToolList.add(new ToolModel("Filter", R.drawable.ic_photo_filter, ToolType.FILTER));
        this.mToolList.add(new ToolModel("Emoji", R.drawable.ic_insert_emoticon, ToolType.EMOJI));
        this.mToolList.add(new ToolModel("Sticker", R.drawable.ic_sticker, ToolType.STICKER));
    }



    public class ToolModel {
        private int mToolIcon;
        private String mToolName;
        private ToolType mToolType;

        ToolModel(String toolName, int toolIcon, ToolType toolType) {
            this.mToolName = toolName;
            this.mToolIcon = toolIcon;
            this.mToolType = toolType;
        }
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_editing_tools, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToolModel item = this.mToolList.get(position);
        holder.txtTool.setText(item.mToolName);
        holder.imgToolIcon.setImageResource(item.mToolIcon);
    }

    @Override
    public int getItemCount() {
        return this.mToolList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgToolIcon;
        TextView txtTool;

        ViewHolder(View itemView)
        {
            super(itemView);
            this.imgToolIcon = (ImageView) itemView.findViewById(R.id.imgToolIcon);
            this.txtTool = (TextView) itemView.findViewById(R.id.txtTool);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditingToolsAdapter.this.mOnItemSelected.onToolSelected(((ToolModel) EditingToolsAdapter.this.mToolList.get(ViewHolder.this.getLayoutPosition())).mToolType);
                }
            });
        }
    }
}
