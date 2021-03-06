package com.vk.fesswood.billdivider.ui.dialog

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import com.vk.fesswood.billdivider.R
import com.vk.fesswood.billdivider.data.model.SumPart
import java.util.*

/**
 * Created by fesswood on 20.09.15.
 */
public class AddSumDialog(context: Context) : BaseDialog(context, R.style.full_screen_dialog) {
    public var colors: IntArray? = null;


    private var fabOk: FloatingActionButton ? = null
    private var etTitle: EditText? = null
    private var etSum: EditText? = null

    private fun initView() {
        fabOk = findViewById(R.id.fabOK) as FloatingActionButton
        etTitle = findViewById(R.id.etTitle) as EditText
        etSum = findViewById(R.id.etSum) as EditText
        fabOk?.setOnClickListener {
            var realm = getRealm();
            getRealm()?.executeTransaction {
                if (!etTitle?.text.toString().isEmpty() && !etTitle?.text.toString().isEmpty()) {
                    var sum = SumPart()
                    sum.id = realm?.where(SumPart::class.java)?.maximumInt("id")!!.plus(1).toInt()
                    sum.title = etTitle?.text.toString()
                    sum.value = etSum?.text.toString().toDouble()
                    var r = Random()
                    sum.color = (colors as IntArray).get(r.nextInt((colors as IntArray).lastIndex))
                    realm?.copyToRealmOrUpdate(sum)
                }
            }
            realm?.close()
            this.dismiss()
        }
    }

    override public fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState);
        colors = context.resources.getIntArray(R.array.sumcolors)
        val colorDrawable = ColorDrawable()
        setContentView(R.layout.fragment_dialog_add_sum)
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        window.setBackgroundDrawable(colorDrawable)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        initView()
    }

}
