package com.example.catalogapp.cataloglist

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import com.example.catalogapp.R
import com.example.catalogapp.TuneListener
import com.example.catalogapp.dataclasses.Tune
import com.example.catalogapp.dataclasses.TuneSingleton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout

class TuneBottomSheet : BottomSheetDialogFragment() {
    private lateinit var category: TextInputLayout
    private lateinit var chosenCategory: AutoCompleteTextView
    private lateinit var applyFiltersButton: MaterialButton
    private lateinit var resetFiltersButton: MaterialButton
    private lateinit var tuneListener: TuneListener


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.tune_bottom_sheet, container, false)
        category = view.findViewById(R.id.category)
        chosenCategory = view.findViewById(R.id.chosenCategory)
        applyFiltersButton = view.findViewById(R.id.applyFiltersButton)
        resetFiltersButton = view.findViewById(R.id.resetFiltersButton)

        chosenCategory.text =
            Editable.Factory.getInstance().newEditable(TuneSingleton.selectedCategory)
        (category.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(R.array.categories)


        resetFiltersButton.setOnClickListener {
            val tune = Tune(
                category = ""
            )
            tuneListener.onTuneCreated(tune)
            TuneSingleton.selectedCategory = ""
            dismiss()
        }

        applyFiltersButton.setOnClickListener {
            val tune = Tune(
                category = chosenCategory.text.toString()
            )
            tuneListener.onTuneCreated(tune)
            TuneSingleton.selectedCategory = chosenCategory.text.toString()
            dismiss()
        }
        return view
    }


    fun setTuneListener(listener: TuneListener) {
        tuneListener = listener
    }

    companion object {
        const val TAG = "TuneBottomSheet"
    }
}