package com.chettapps.videoeditor.videocutermerger.imageeditor


import com.chettapps.videoeditor.videocutermerger.imageeditor.base.BaseActivity
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import com.chettapps.videoeditor.videocutermerger.imageeditor.EmojiBSFragment.EmojiListener
import com.chettapps.videoeditor.videocutermerger.imageeditor.StickerBSFragment.StickerListener
import com.chettapps.videoeditor.videocutermerger.imageeditor.tools.EditingToolsAdapter.OnItemSelected
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.graphics.Typeface
import com.chettapps.videoeditor.videocutermerger.imageeditor.tools.EditingToolsAdapter
import com.chettapps.videoeditor.videocutermerger.imageeditor.filters.FilterViewAdapter
import androidx.constraintlayout.widget.ConstraintSet
import android.os.Bundle
import com.chettapps.videoeditor.R
import android.content.Intent
import com.chettapps.videoeditor.videocutermerger.activities.Swap_Edit_Activity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chettapps.videoeditor.videocutermerger.imageeditor.TextEditorDialogFragment.TextEditor
import ja.burhanrashid52.photoeditor.ViewType
import android.annotation.SuppressLint
import android.app.AlertDialog
import ja.burhanrashid52.photoeditor.SaveSettings
import ja.burhanrashid52.photoeditor.PhotoEditor.OnSaveListener
import android.graphics.Bitmap
import android.content.DialogInterface
import android.net.Uri
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import ja.burhanrashid52.photoeditor.PhotoFilter
import com.chettapps.videoeditor.videocutermerger.imageeditor.tools.ToolType
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageView
import com.chettapps.videoeditor.videocutermerger.imageeditor.filters.FilterListener
import com.chettapps.videoeditor.videocutermerger.utils.FileUtils

import java.io.File
import java.io.IOException
import java.lang.Exception
import java.util.*

/* loaded from: classes.dex */
class EditImageActivity() : BaseActivity(), OnPhotoEditorListener, View.OnClickListener,
    PropertiesBSFragment.Properties, EmojiListener, StickerListener, OnItemSelected,
    FilterListener {
    var imageUri: Uri? = null
    private var mEmojiBSFragment: EmojiBSFragment? = null
    private var mIsFilterVisible = false
    private var mPhotoEditor: PhotoEditor? = null
    private var mPhotoEditorView: PhotoEditorView? = null
    private var mPropertiesBSFragment: PropertiesBSFragment? = null
    private var mRootView: ConstraintLayout? = null
    private var mRvFilters: RecyclerView? = null
    private var mRvTools: RecyclerView? = null
    private var mStickerBSFragment: StickerBSFragment? = null
    private var mTxtCurrentTool: TextView? = null
    private var mWonderFont: Typeface? = null
    var position = 0
    private val mEditingToolsAdapter = EditingToolsAdapter(this)
    private val mFilterViewAdapter = FilterViewAdapter(this)
    private val mConstraintSet = ConstraintSet()

    /* JADX INFO: Access modifiers changed from: protected */
    // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        setContentView(R.layout.activity_edit_image)
        initViews()
        val data = intent
        position = data.getIntExtra("id", 0)
        Log.d("bbbbb.....", "edit....rcv..****....position = " + position)
        imageUri = Uri.fromFile(File(Swap_Edit_Activity.imageList[position].path))
        Log.e("bbbbb.....", "edit....rcv..****....imagesPath = " + imageUri)
        mPhotoEditorView!!.source.setImageURI(imageUri)
        val r = Random().nextInt(2)
        mWonderFont = Typeface.createFromAsset(assets, "beyond_wonderland.ttf")
        mPropertiesBSFragment = PropertiesBSFragment()
        mEmojiBSFragment = EmojiBSFragment()
        mStickerBSFragment = StickerBSFragment()
        mStickerBSFragment!!.setStickerListener(this)
        mEmojiBSFragment!!.setEmojiListener(this)
        mPropertiesBSFragment!!.setPropertiesChangeListener(this)
        val llmTools = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        mRvTools!!.layoutManager = llmTools
        mRvTools!!.adapter = mEditingToolsAdapter
        val llmFilters = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        mRvFilters!!.layoutManager = llmFilters
        mRvFilters!!.adapter = mFilterViewAdapter
        mPhotoEditor =
            PhotoEditor.Builder(this, (mPhotoEditorView)!!).setPinchTextScalable(true).build()
        mPhotoEditor!!.setOnPhotoEditorListener(this)
    }

    private fun initViews() {
        mPhotoEditorView = findViewById<View>(R.id.photoEditorView) as PhotoEditorView
        mTxtCurrentTool = findViewById<View>(R.id.txtCurrentTool) as TextView
        mRvTools = findViewById<View>(R.id.rvConstraintTools) as RecyclerView
        mRvFilters = findViewById<View>(R.id.rvFilterView) as RecyclerView
        mRootView = findViewById<View>(R.id.rootView) as ConstraintLayout
        val imgUndo = findViewById<View>(R.id.imgUndo) as ImageView
        imgUndo.setOnClickListener(this)
        val imgRedo = findViewById<View>(R.id.imgRedo) as ImageView
        imgRedo.setOnClickListener(this)
        val imgSave = findViewById<View>(R.id.imgSave) as ImageView
        imgSave.setOnClickListener(this)
        val imgClose = findViewById<View>(R.id.imgClose) as ImageView
        imgClose.setOnClickListener(this)
        val btnBack = findViewById<View>(R.id.btnBack) as ImageView
        btnBack.setOnClickListener(this)
    }

    override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
        val textEditorDialogFragment = TextEditorDialogFragment.show(this, (text)!!, colorCode)
        textEditorDialogFragment.setOnTextEditorListener { inputText, colorCode2 ->
            mPhotoEditor!!.editText((rootView)!!, inputText, colorCode2)
            mTxtCurrentTool!!.setText(R.string.label_text)
        }
    }

    override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        Log.d(
            TAG,
            "onAddViewListener() called with: viewType = [$viewType], numberOfAddedViews = [$numberOfAddedViews]"
        )
    }

    // ja.burhanrashid52.photoeditor.OnPhotoEditorListener
    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        Log.d(
            TAG,
            "onRemoveViewListener() called with: viewType = [$viewType], numberOfAddedViews = [$numberOfAddedViews]"
        )
    }

    // ja.burhanrashid52.photoeditor.OnPhotoEditorListener
    override fun onStartViewChangeListener(viewType: ViewType?) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [$viewType]")
    }

    // ja.burhanrashid52.photoeditor.OnPhotoEditorListener
    override fun onStopViewChangeListener(viewType: ViewType?) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [$viewType]")
    }

    // android.view.View.OnClickListener
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnBack -> {
                onBackPressed()
                return
            }
            R.id.imgUndo -> {
                mPhotoEditor!!.undo()
                return
            }
            R.id.imgRedo -> {
                mPhotoEditor!!.redo()
                return
            }
            R.id.imgClose -> {
                onBackPressed()
                return
            }
            R.id.imgSave -> {
                saveImage()
                return
            }
            else -> return
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint("MissingPermission")
    fun saveImage() {
        if (requestPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
            showLoading("Saving...")
            if (!FileUtils.EDIT_IMG_DIRECTORY.exists()) {
                FileUtils.EDIT_IMG_DIRECTORY.mkdirs()
            }
            val file =
                File(FileUtils.EDIT_IMG_DIRECTORY, System.currentTimeMillis().toString() + ".png")
            Log.d("bbbbb.....", "*********************....****....file = $file")
            try {
                file.createNewFile()
                val saveSettings =
                    SaveSettings.Builder().setClearViewsEnabled(true).setTransparencyEnabled(true)
                        .build()
                mPhotoEditor!!.saveAsFile(file.absolutePath, saveSettings, object : OnSaveListener {
                    // from class: com.chettapps.videoeditor.videocutermerger.imageeditor.EditImageActivity.2
                    // ja.burhanrashid52.photoeditor.PhotoEditor.OnSaveListener
                    override fun onSuccess(imagePath: String) {
                        hideLoading()
                        showSnackbar("Image Saved Successfully")
                        Log.d(
                            "bbbbb.....",
                            "edit....****....imageUri = " + imageUri.toString() + "\nedit.....****....imagePath = " + imagePath
                        )
                        Log.d(
                            "bbbbb.....",
                            "*********************....****....imageList = " + Swap_Edit_Activity.imageList
                        )
                        Swap_Edit_Activity.imageList.get(position).path = imagePath
                        Log.d(
                            "bbbbb.....",
                            "********...imgpaths...*************....****....imagePath = $imagePath"
                        )
                        mPhotoEditorView!!.source.setImageURI(Uri.fromFile(File(imagePath)))
                        onBackPressed()
                    }

                    // ja.burhanrashid52.photoeditor.PhotoEditor.OnSaveListener
                    override fun onFailure(exception: Exception) {
                        hideLoading()
                        showSnackbar("Failed to save Image")
                    }
                })
            } catch (e: IOException) {
                e.printStackTrace()
                hideLoading()
                showSnackbar((e.message)!!)
            }
        }
    }

    // com.chettapps.videoeditor.videocutermerger.imageeditor.PropertiesBSFragment.Properties
    override fun onColorChanged(colorCode: Int) {
        mPhotoEditor!!.brushColor = colorCode
        mTxtCurrentTool!!.setText(R.string.label_brush)
    }

    // com.chettapps.videoeditor.videocutermerger.imageeditor.PropertiesBSFragment.Properties
    override fun onOpacityChanged(opacity: Int) {
        mPhotoEditor!!.setOpacity(opacity)
        mTxtCurrentTool!!.setText(R.string.label_brush)
    }

    // com.chettapps.videoeditor.videocutermerger.imageeditor.PropertiesBSFragment.Properties
    override fun onBrushSizeChanged(brushSize: Int) {
        mPhotoEditor!!.brushSize = brushSize.toFloat()
        mTxtCurrentTool!!.setText(R.string.label_brush)
    }

    // com.chettapps.videoeditor.videocutermerger.imageeditor.EmojiBSFragment.EmojiListener
    override fun onEmojiClick(emojiUnicode: String) {
        mPhotoEditor!!.addEmoji(emojiUnicode)
        mTxtCurrentTool!!.setText(R.string.label_emoji)
    }

    // com.chettapps.videoeditor.videocutermerger.imageeditor.StickerBSFragment.StickerListener
    override fun onStickerClick(bitmap: Bitmap) {
        mPhotoEditor!!.addImage(bitmap)
        mTxtCurrentTool!!.setText(R.string.label_sticker)
    }

    // com.chettapps.videoeditor.videocutermerger.imageeditor.base.BaseActivity
    override fun isPermissionGranted(isGranted: Boolean, permission: String) {
        if (isGranted) {
            saveImage()
        }
    }

    private fun showSaveDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you want to exit without saving image ?")
        builder.setPositiveButton("Save", DialogInterface.OnClickListener { dialog, which ->
            // from class: com.chettapps.videoeditor.videocutermerger.imageeditor.EditImageActivity.3
            // android.content.DialogInterface.OnClickListener
            saveImage()
        })
        builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            // from class: com.chettapps.videoeditor.videocutermerger.imageeditor.EditImageActivity.4
            // android.content.DialogInterface.OnClickListener
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
            }
        })
        builder.setNeutralButton("Discard", object : DialogInterface.OnClickListener {
            // from class: com.chettapps.videoeditor.videocutermerger.imageeditor.EditImageActivity.5
            // android.content.DialogInterface.OnClickListener
            override fun onClick(dialog: DialogInterface, which: Int) {
                val data = Intent(this@EditImageActivity, Swap_Edit_Activity::class.java)
                val bundle = Bundle()
                bundle.putParcelableArrayList("IMAGE", Swap_Edit_Activity.imageList)
                Log.d("bbbb.....", ".....imageList....." + Swap_Edit_Activity.imageList)
                data.putExtra("IMAGE", bundle)
                this@EditImageActivity.startActivity(data)
                finish()
            }
        })
        builder.create().show()
    }

    // com.chettapps.videoeditor.videocutermerger.imageeditor.filters.FilterListener
    override fun onFilterSelected(photoFilter: PhotoFilter) {
        mPhotoEditor!!.setFilterEffect(photoFilter)
    }

    // com.chettapps.videoeditor.videocutermerger.imageeditor.tools.EditingToolsAdapter.OnItemSelected
    override fun onToolSelected(toolType: ToolType) {
        when (toolType) {
            ToolType.BRUSH -> {
                mPhotoEditor!!.setBrushDrawingMode(true)
                mTxtCurrentTool!!.setText(R.string.label_brush)
                mPropertiesBSFragment!!.show(supportFragmentManager, mPropertiesBSFragment!!.tag)
                return
            }
            ToolType.TEXT -> {
                val textEditorDialogFragment = TextEditorDialogFragment.show(this@EditImageActivity)
                textEditorDialogFragment.setOnTextEditorListener(object : TextEditor {
                    // from class: com.chettapps.videoeditor.videocutermerger.imageeditor.EditImageActivity.6
                    // com.chettapps.videoeditor.videocutermerger.imageeditor.TextEditorDialogFragment.TextEditor
                    override fun onDone(inputText: String, colorCode: Int) {
                        mPhotoEditor!!.addText(inputText, colorCode)
                        mTxtCurrentTool!!.setText(R.string.label_text)
                    }
                })
                return
            }
            ToolType.ERASER -> {
                mPhotoEditor!!.brushEraser()
                mTxtCurrentTool!!.setText(R.string.label_eraser)
                return
            }
            ToolType.FILTER -> {
                mTxtCurrentTool!!.setText(R.string.label_filter)
                showFilter(true)
                return
            }
            ToolType.EMOJI -> {
                mEmojiBSFragment!!.show(supportFragmentManager, mEmojiBSFragment!!.tag)
                return
            }
            ToolType.STICKER -> {
                mStickerBSFragment!!.show(supportFragmentManager, mStickerBSFragment!!.tag)
                return
            }
            else -> return
        }
    }

    fun showFilter(isVisible: Boolean) {
        mIsFilterVisible = isVisible
        mConstraintSet.clone(mRootView)
        if (isVisible) {
            mConstraintSet.clear(mRvFilters!!.id, 6)
            mConstraintSet.connect(mRvFilters!!.id, 6, 0, 6)
            mConstraintSet.connect(mRvFilters!!.id, 7, 0, 7)
        } else {
            mConstraintSet.connect(mRvFilters!!.id, 6, 0, 7)
            mConstraintSet.clear(mRvFilters!!.id, 7)
        }
        val changeBounds = ChangeBounds()
        changeBounds.duration = 350L
        changeBounds.interpolator = AnticipateOvershootInterpolator(1.0f)
        TransitionManager.beginDelayedTransition(mRootView, changeBounds)
        mConstraintSet.applyTo(mRootView)
    }

    // android.support.v4.app.FragmentActivity, android.app.Activity
    override fun onBackPressed() {
        if (mIsFilterVisible) {
            showFilter(false)
            mTxtCurrentTool!!.setText(R.string.app_name)
        } else if (!mPhotoEditor!!.isCacheEmpty) {
            showSaveDialog()
        } else {
            val data = Intent(this, Swap_Edit_Activity::class.java)
            val bundle = Bundle()
            bundle.putParcelableArrayList("IMAGE", Swap_Edit_Activity.imageList)
            Log.d("bbbb.....", ".....imageList....." + Swap_Edit_Activity.imageList)
            data.putExtra("IMAGE", bundle)
            startActivity(data)
            super.onBackPressed()
        }
    }

    override fun onTouchSourceImage(event: MotionEvent?) {}

    companion object {
        private val TAG = "bbbbb..EDA..."
        var editback = false
    }
}