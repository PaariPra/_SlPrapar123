package com.chettapps.videoeditor.videocutermerger;

import android.util.Log;


import com.chettapps.videoeditor.R;
import com.chettapps.videoeditor.videocutermerger.mask.FinalMaskBitmap3D;

import java.util.ArrayList;

/* loaded from: classes.dex */
public enum THEMES {
    Shine("Shine") { // from class: com.chettapps.videoeditor.videocutermerger.THEMES.1
        @Override // com.chettapps.videoeditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            Log.d("bbbbb....THEMES....", "...........******......getTheme...............");
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();


            mEffects.add(FinalMaskBitmap3D.EFFECT.Whole3D_BT);
            mEffects.add(FinalMaskBitmap3D.EFFECT.Whole3D_TB);
            mEffects.add(FinalMaskBitmap3D.EFFECT.Whole3D_LR);
            mEffects.add(FinalMaskBitmap3D.EFFECT.Whole3D_RL);
            mEffects.add(FinalMaskBitmap3D.EFFECT.SepartConbine_BT);
            mEffects.add(FinalMaskBitmap3D.EFFECT.SepartConbine_TB);
            mEffects.add(FinalMaskBitmap3D.EFFECT.SepartConbine_LR);
            mEffects.add(FinalMaskBitmap3D.EFFECT.SepartConbine_RL);
            mEffects.add(FinalMaskBitmap3D.EFFECT.RollInTurn_BT);
            mEffects.add(FinalMaskBitmap3D.EFFECT.RollInTurn_TB);
            mEffects.add(FinalMaskBitmap3D.EFFECT.RollInTurn_LR);
            mEffects.add(FinalMaskBitmap3D.EFFECT.RollInTurn_RL);
            mEffects.add(FinalMaskBitmap3D.EFFECT.Jalousie_BT);
            mEffects.add(FinalMaskBitmap3D.EFFECT.Jalousie_LR);


            mEffects.add(FinalMaskBitmap3D.EFFECT.Roll2D_BT);
            mEffects.add(FinalMaskBitmap3D.EFFECT.Roll2D_TB);
            mEffects.add(FinalMaskBitmap3D.EFFECT.Roll2D_LR);
            mEffects.add(FinalMaskBitmap3D.EFFECT.Roll2D_RL);


            mEffects.add(FinalMaskBitmap3D.EFFECT.CIRCLE_D);




            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._1;
        }
    },
    Jalousie_Down_Up("Jalousie Down Up") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.2
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.Jalousie_BT);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._2;
        }
    },
    Jalousie_Left_Right("Jalousie Left Right") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.3
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.Jalousie_LR);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._4;
        }
    },
    Whole3D_Down_Up("Whole3D Down Up") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.4
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.Whole3D_BT);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._5;
        }
    },
    Whole3D_Up_Down("Whole3D Up Down") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.5
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.Whole3D_TB);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._1;
        }
    },
    Whole3D_Left_Right("Whole3D Left Right") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.6
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.Whole3D_LR);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._2;
        }
    },
    Whole3D_Right_Left("Whole3D Right Left") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.7
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.Whole3D_RL);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._3;
        }
    },
    SepartConbine_Down_Up("SepartConbine Down Up") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.8
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.SepartConbine_BT);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._4;
        }
    },
    SepartConbine_Up_Down("SepartConbine Up Down") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.9
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.SepartConbine_TB);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._5;
        }
    },
    SepartConbine_Left_Right("SepartConbine Left Right") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.10
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.SepartConbine_LR);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._1;
        }
    },
    SepartConbine_Right_Left("SepartConbine Right Left") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.11
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.SepartConbine_RL);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._2;
        }
    },
    RollInTurn_Down_Up("RollInTurn Down Up") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.12
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.RollInTurn_BT);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._3;
        }
    },
    RollInTurn_Up_Down("RollInTurn Up Down") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.13
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.RollInTurn_TB);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._4;
        }
    },
    RollInTurn_Left_Right("RollInTurn Left Right") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.14
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.RollInTurn_LR);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._5;
        }
    },
    RollInTurn_Right_Left("RollInTurn Right Left") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.15
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.RollInTurn_RL);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._1;
        }
    },



    Roll2D_Down_Up("Roll2D Down Up") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.16
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.Roll2D_BT);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._2;
        }
    },



    Roll2D_Up_Down("Roll2D Up Down") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.17
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.Roll2D_TB);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._3;
        }
    },





    Roll2D_Left_Right("Roll2D Left Right") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.18
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.Roll2D_LR);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._4;
        }
    },






    Roll2D_Right_Left("Roll2D Right Left") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.19
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.Roll2D_RL);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._5;
        }
    },



    CIRCLE_D("Circle") { // from class: com.photovideomaker.pictovideditor.videocutermerger.THEMES.19
        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme() {
            ArrayList<FinalMaskBitmap3D.EFFECT> mEffects = new ArrayList<>();
            mEffects.add(FinalMaskBitmap3D.EFFECT.CIRCLE_D);
            return mEffects;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList) {
            return null;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeDrawable() {
            return R.drawable.ic_shine_theme;
        }

        @Override // com.photovideomaker.pictovideditor.videocutermerger.THEMES
        public int getThemeMusic() {
            return R.raw._5;
        }
    };



    
    String name;

    public abstract ArrayList<FinalMaskBitmap3D.EFFECT> getTheme();

    public abstract ArrayList<FinalMaskBitmap3D.EFFECT> getTheme(ArrayList<FinalMaskBitmap3D.EFFECT> arrayList);

    public abstract int getThemeDrawable();

    public abstract int getThemeMusic();

    THEMES(String string) {
        this.name = "";
        this.name = string;
    }
}
