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

package com.travelbackintime.buybitcoin.utils

import java.text.NumberFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

interface FormatterUtils {

    fun formatPrice(amount: Double): String

    fun formatPriceAsOnlyDigits(amount: Double): String

    fun formatDate(date: Date): String

    fun formatDateToShareText(date: Date): String

    fun formatMonth(date: Date): String

    fun formatYear(date: Date): String
}

class FormatterUtilsImpl @Inject constructor(private val numberFormat: NumberFormat) :
    FormatterUtils {

    override fun formatPrice(amount: Double): String {
        return numberFormat.format(amount)
    }

    override fun formatPriceAsOnlyDigits(amount: Double): String {
        return String.format(Locale.US, "%,.2f", amount)
    }

    override fun formatDate(date: Date): String {
        return String.format(Locale.US, "%1\$td %1\$tb %1\$tY", date)
    }

    override fun formatDateToShareText(date: Date): String {
        return String.format(Locale.US, "%1\$tB %1\$tY", date)
    }

    override fun formatMonth(date: Date): String {
        return String.format(Locale.US, "%1\$tb", date)
    }

    override fun formatYear(date: Date): String {
        return String.format(Locale.US, "%1\$tY", date)
    }
}
