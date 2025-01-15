package com.example.frame.dialog.singleDialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.DialogFragment
import com.dylanc.longan.dp
import com.example.frame.databinding.FragmentSingleDialogBinding

class CommonDialogFragment : DialogFragment() {
    private var dialogBinding: FragmentSingleDialogBinding? = null
    private val binding get() = dialogBinding!!

    private var title: String? = null
    private var titleColor: Int? = null
    private var titleSize: Float? = null

    private var message: String? = null
    private var messageColor: Int? = null
    private var messageSize: Float? = null

    private var negativeButtonText: String? = null
    private var negativeButtonTextColor:Int?=null
    private var negativeButtonTextSize:Float?=null
    private var negativeBackgroundTint: Int? = null
    private var negativeCornerRadius: Int? = null
    private var negativeStrokeColor: Int? = null
    private var negativeStrokeWidth: Int? = null

    private var positiveButtonText: String? = null
    private var positiveButtonTextColor:Int?=null
    private var positiveButtonTextSize:Float?=null
    private var positiveBackgroundTint: Int? = null
    private var positiveCornerRadius: Int? = null
    private var positiveStrokeColor: Int? = null
    private var positiveStrokeWidth: Int? = null


    var dialogCancelable = true
    private var positiveButtonListener: OnClickListener? = null
    private var negativeButtonListener: OnClickListener? = null
    private var isCenter = false

    companion object {
        fun newInstance(): CommonDialogFragment {
            return CommonDialogFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialogBinding = FragmentSingleDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //标题
        title?.let {
            binding.tvTitle.visibility = View.VISIBLE
            binding.tvTitle.text = it
        } ?: kotlin.run {
            binding.tvTitle.visibility = View.GONE
        }
        titleColor?.let {
            binding.tvTitle.setTextColor(it)
        }
        titleSize?.let {
            binding.tvTitle.textSize = it
        }

        //正文部分
        message?.let {
            binding.tvMessage.visibility = View.VISIBLE
            binding.tvMessage.text = it

            val gravity = when {
                isCenter -> Gravity.CENTER_HORIZONTAL
                title.isNullOrEmpty() -> Gravity.CENTER_HORIZONTAL
                else -> Gravity.NO_GRAVITY
            }

            val marginTop =
                if (title.isNullOrEmpty()) (26).dp else (12).dp
            binding.tvMessage.gravity = gravity
            binding.tvMessage.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topMargin = marginTop.toInt()
            }
        } ?: kotlin.run {
            binding.tvMessage.visibility = View.GONE
        }
        messageColor?.let {
            binding.tvMessage.setTextColor(it)
        }
        messageSize?.let {
            binding.tvMessage.textSize = it
        }

        //底部取消按钮
        negativeButtonText?.let {
            binding.btnNegative.visibility = View.VISIBLE
            binding.btnNegative.text = it
        } ?: kotlin.run {
            binding.btnNegative.visibility = View.GONE
        }
        negativeButtonTextColor?.let {
            binding.btnNegative.setTextColor(it)
        }
        negativeButtonTextSize?.let {
            binding.btnNegative.textSize = it
        }
        negativeBackgroundTint?.let {
            binding.btnNegative.setTextColor(it)
        }
        negativeCornerRadius?.let {
            binding.btnNegative.cornerRadius = it
        }
        negativeStrokeColor?.let {
            binding.btnNegative.setStrokeColorResource(it)
        }
        negativeStrokeWidth?.let {
            binding.btnNegative.strokeWidth = it
        }

        positiveButtonText?.let {
            binding.btnPositive.visibility = View.VISIBLE
            binding.btnPositive.text = it
            if (negativeButtonText.isNullOrEmpty()) {
                binding.btnPositive.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    width = (127).dp.toInt()
                }
            }
        } ?: kotlin.run {
            binding.btnPositive.visibility = View.GONE
        }

        //底部正确按钮
        positiveButtonTextColor?.let {
            binding.btnPositive.setTextColor(it)
        }
        positiveButtonTextSize?.let {
            binding.btnPositive.textSize = it
        }
        positiveBackgroundTint?.let {
            binding.btnPositive.setTextColor(it)
        }
        positiveCornerRadius?.let {
            binding.btnPositive.cornerRadius = it
        }
        positiveStrokeColor?.let {
            binding.btnPositive.setStrokeColorResource(it)
        }
        positiveStrokeWidth?.let {
            binding.btnPositive.strokeWidth = it
        }

        binding.btnPositive.setOnClickListener {
            positiveButtonListener?.onClick(dialog) ?: kotlin.run { }

        }
        binding.btnNegative.setOnClickListener {
            negativeButtonListener?.onClick(dialog) ?: kotlin.run { dismiss() }
        }
        dialog?.setCancelable(dialogCancelable)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setDimAmount(0.5f)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onDestroy() {
        dialogBinding = null
        super.onDestroy()
    }


    fun setTitle(
        title: String,
        titleColor: Int? = Color.parseColor("#ff39393b"),
        titleSize: Float? = 16f
    ) {
        this.title = title
        this.titleColor = titleColor
        this.titleSize = titleSize
    }

    fun setMessage(
        message: String,
        messageColor: Int? = Color.parseColor("#ff39393b"),
        messageSize: Float? = 14f
    ) {
        this.message = message
        this.messageColor = messageColor
        this.messageSize = messageSize
    }

    fun setNegativeButton(
        text: String,
        textColor:Int?=Color.parseColor("#ff39393b"),
        textSize:Float?=15f,
        backgroundTint: Int? = Color.parseColor("#FFFFFFFF"),
        cornerRadius: Int? = 20,
        strokeColor: Int? = Color.parseColor("#D9DDE8"),
        strokeWidth: Int? = 1,
        listener: OnClickListener?
    ) {
        this.negativeButtonText = text
        this.negativeButtonTextColor=textColor
        this.negativeButtonTextSize=textSize
        this.negativeBackgroundTint = backgroundTint
        this.negativeCornerRadius = cornerRadius
        this.negativeStrokeColor = strokeColor
        this.negativeStrokeWidth = strokeWidth
        this.negativeButtonListener = listener
    }

    fun setPositiveButton(
        text: String,
        textColor:Int?=Color.parseColor("#FFFFFFFF"),
        textSize:Float?=15f,
        backgroundTint: Int? = Color.parseColor("#5197FF"),
        cornerRadius: Int? = 20,
        strokeColor: Int? = Color.TRANSPARENT,
        strokeWidth: Int? = 1,
        listener: OnClickListener?
    ) {
        this.positiveButtonText = text
        this.positiveButtonTextColor=textColor
        this.positiveButtonTextSize=textSize
        this.positiveBackgroundTint = backgroundTint
        this.positiveCornerRadius = cornerRadius
        this.positiveStrokeColor = strokeColor
        this.positiveStrokeWidth = strokeWidth
        this.positiveButtonListener = listener
    }

    fun isDialogShowing() = dialog?.isShowing

    interface OnClickListener {
        fun onClick(dialog: Dialog?)
    }

    fun setDialogCenter(center: Boolean) {
        this.isCenter = center
    }
}