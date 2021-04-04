package com.github.vase4kin.coindesk.tracker

interface Analytics {
    /**
     * Log analytic event
     *
     * @param eventName  - the name of the event
     * @param parameters - the map of parameters to send along
     */
    fun logEvent(eventName: String, parameters: Map<String, String> = emptyMap())
}
