package com.example.frame.dialog.permissionDialog

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.frame.databinding.ItemExplainDialogBinding
import com.example.frame.entity.PermissionEntity

class PermissionExplainAdapter(
    private val context: Context,
    private val dataList: List<PermissionEntity>
) : RecyclerView.Adapter<PermissionExplainAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemExplainDialogBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemExplainDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.imgTitle.isVisible=dataList[position].icon!=null
        dataList[position].icon?.let {
         holder.binding.imgTitle.load(it)
        }
        holder.binding.tvTitle.text = dataList[position].title
        holder.binding.tvMessage.text = dataList[position].msg
    }

}