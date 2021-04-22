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

package com.github.vase4kin.shared.timetravelmachine

import com.github.vase4kin.shared.coindesk.service.CoinDeskServiceImpl
import com.github.vase4kin.shared.database.LocalDatabaseImpl
import com.github.vase4kin.shared.repository.RepositoryImpl

/**
 * Factory to create TimeTravelMachine instance
 */
object TimeTravelMachineFactory {
    /**
     * @param eventWithAbsentPrice - the default event placeholder that needs to be provided in the case of the bitcoin price being absent
     */
    fun create(
        eventWithAbsentPrice: TimeTravelMachine.Event.RealWorldEvent
    ): TimeTravelMachine {
        return TimeTravelMachineImpl(
            repository = RepositoryImpl(
                coinDeskService = CoinDeskServiceImpl(),
                localDatabase = LocalDatabaseImpl()
            ),
            eventWithNoPrice = eventWithAbsentPrice
        )
    }
}
