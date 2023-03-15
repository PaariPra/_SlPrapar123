package com.chettapps.videoeditor.videocutermerger.imageeditor;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import com.chettapps.videoeditor.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;



public class PropertiesBSFragment extends BottomSheetDialogFragment implements SeekBar.OnSeekBarChangeListener
{

    private Properties mProperties;
    public interface Properties
    {
        void onBrushSizeChanged(int i);
        void onColorChanged(int i);
        void onOpacityChanged(int i);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_properties_dialog, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvColor = (RecyclerView) view.findViewById(R.id.rvColors);
        SeekBar sbOpacity = (SeekBar) view.findViewById(R.id.sbOpacity);
        SeekBar sbBrushSize = (SeekBar) view.findViewById(R.id.sbSize);
        sbOpacity.setOnSeekBarChangeListener(this);
        sbBrushSize.setOnSeekBarChangeListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rvColor.setLayoutManager(layoutManager);
        rvColor.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(getActivity());
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                if (PropertiesBSFragment.this.mProperties != null) {
                    PropertiesBSFragment.this.dismiss();
                    PropertiesBSFragment.this.mProperties.onColorChanged(colorCode);
                }
            }
        });
        rvColor.setAdapter(colorPickerAdapter);
    }

    public void setPropertiesChangeListener(Properties properties) {
        this.mProperties = properties;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.sbOpacity :
                if (this.mProperties != null) {
                    this.mProperties.onOpacityChanged(i);
                    return;
                }
                return;
            case R.id.txtOpacity :
            default:
                return;
            case R.id.sbSize :
                if (this.mProperties != null) {
                    this.mProperties.onBrushSizeChanged(i);
                    return;
                }
                return;
        }
    }



    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }


}
