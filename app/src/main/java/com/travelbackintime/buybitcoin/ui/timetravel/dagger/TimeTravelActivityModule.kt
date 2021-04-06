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

package com.travelbackintime.buybitcoin.ui.timetravel.dagger

import com.travelbackintime.buybitcoin.ui.router.InternalRouter
import com.travelbackintime.buybitcoin.ui.router.InternalRouterImpl
import com.travelbackintime.buybitcoin.ui.timetravel.view.TimeTravelActivity
import dagger.Module
import dagger.Provides

@Module
class TimeTravelActivityModule {

    @Provides
    fun provideInternalRouter(activity: TimeTravelActivity): InternalRouter {
        return InternalRouterImpl(activity)
    }
}
