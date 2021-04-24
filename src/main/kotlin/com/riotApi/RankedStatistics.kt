package com.riotApi

import com.google.gson.JsonObject

class RankedStatistics {

    fun calculateWinrate(rank: JsonObject): Float {
        val wins = rank.get("wins").asFloat
        val losses = rank.get("losses").asFloat
        val totalgames = wins + losses
        return wins / totalgames*100
    }
}