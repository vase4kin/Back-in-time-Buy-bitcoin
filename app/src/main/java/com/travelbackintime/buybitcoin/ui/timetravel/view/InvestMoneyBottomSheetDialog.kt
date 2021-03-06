/*
 * Copyright 2021  Andrey Tolpeev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.travelbackintime.buybitcoin.ui.timetravel.view

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.vase4kin.shared.tracker.Tracker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

private const val MAX_MONEY = 1000000

@AndroidEntryPoint
class InvestMoneyBottomSheetDialog : BottomSheetDialogFragment() {

    @Inject
    lateinit var numberFormat: NumberFormat

    @Inject
    lateinit var tracker: Tracker

    private var listener: InvestMoneyListener? = null
    private var errorRichCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_amount, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextWrapper = view.findViewById<TextInputLayout>(R.id.edit_text_wrapper)

        val hint = getString(
            R.string.hint_set_amount,
            numberFormat.currency?.getDisplayName(Locale.ENGLISH)
        )
        editTextWrapper.hint = hint

        editTextWrapper.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveInvestedMoney(editTextWrapper)
                val imm =
                    context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                return@setOnEditorActionListener true
            }
            false
        }

        view.findViewById<View>(R.id.button_set_invested_money).setOnClickListener {
            saveInvestedMoney(editTextWrapper)
        }

        view.findViewById<View>(R.id.button_set_1).setOnClickListener {
            val amount = resources.getString(R.string.text_1)
            editTextWrapper.editText?.setText(amount)
            tracker.trackUserChooseMoneySuggestion(amount)
            saveInvestedMoney(editTextWrapper)
        }

        view.findViewById<View>(R.id.button_set_10).setOnClickListener {
            val amount = resources.getString(R.string.text_10)
            editTextWrapper.editText?.setText(amount)
            tracker.trackUserChooseMoneySuggestion(amount)
            saveInvestedMoney(editTextWrapper)
        }

        view.findViewById<View>(R.id.button_set_100).setOnClickListener {
            val amount = resources.getString(R.string.text_100)
            editTextWrapper.editText?.setText(amount)
            tracker.trackUserChooseMoneySuggestion(amount)
            saveInvestedMoney(editTextWrapper)
        }

        editTextWrapper.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val window = dialog?.window
                window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
        }
    }

    private fun saveInvestedMoney(editTextWrapper: TextInputLayout) {
        editTextWrapper.error = null
        val investedMoney = editTextWrapper.editText?.text.toString()
        if (!TextUtils.isEmpty(investedMoney)) {
            val investedMoneyAsDouble = investedMoney.toDouble()
            when {
                investedMoneyAsDouble < 1 -> {
                    editTextWrapper.error = getString(
                        R.string.error_set_amount_zero,
                        numberFormat.currency?.getDisplayName(Locale.ENGLISH)
                    )
                    tracker.trackUserSeesAtLeastDollarError()
                }
                investedMoneyAsDouble >= MAX_MONEY && errorRichCount == 0 -> {
                    editTextWrapper.error = getString(R.string.error_set_amount_rich)
                    errorRichCount++
                    tracker.trackUserSeesYouReachError()
                }
                else -> {
                    listener?.onInvestedMoneySet(investedMoneyAsDouble)
                    errorRichCount = 0
                    dismiss()
                }
            }
        } else {
            editTextWrapper.error = getString(R.string.error_set_amount_empty)
            tracker.trackUserSeesEmptyAmountError()
        }
    }

    interface InvestMoneyListener {
        fun onInvestedMoneySet(investedMoney: Double)
    }

    fun setListener(listener: InvestMoneyListener) {
        this.listener = listener
    }
}
