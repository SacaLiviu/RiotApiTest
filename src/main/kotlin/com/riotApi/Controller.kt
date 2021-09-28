package com.riotApi

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request
import tornadofx.Controller


class BasicController : Controller()     {
     private var summoner=Summoner()

    private val apiKey=""
    fun urlBuilderSummoner(summonerName:String,region:String): String {
        val url = StringBuilder()
        return url
            .append("https://")
            .append(region.toLowerCase())
            .append("1.api.riotgames.com/lol/summoner/v4/summoners/by-name/")
            .append(summonerName.replace(" ","%20"))
            .append("?api_key=")
            .append(apiKey).toString()
    }
    fun urlBuilderMastery(region: String): String {
        val url =StringBuilder()
        return url
            .append("https://")
            .append(region.toLowerCase())
            .append("1.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/")
            .append(summoner.id)
            .append("?api_key=")
            .append(apiKey)
            .toString()
    }
    fun urlBuilderRanked(region:String):String{
        val url=StringBuilder()
        return url
            .append("https://")
            .append(region.toLowerCase())
            .append("1.api.riotgames.com/lol/league/v4/entries/by-summoner/")
            .append(summoner.id)
            .append("?api_key=")
            .append(apiKey).toString()
    }

    fun urlMatch(region:String): String {
        val url=StringBuilder()
        var continent=""
        when (region){
            "EUW"->continent="europe"
            "EUN"->continent="europe"
            "NA"->continent="americas"
        }
        return url
            .append("https://")
            .append(continent)
            .append(".api.riotgames.com/lol/match/v5/matches/by-puuid/")
            .append(summoner.puuid)
            .append("/ids?start=0&count=1&api_key=")
            .append((apiKey)).toString()
    }

    fun getLastMatchData(region: String,matchId:String):String{
        val url=StringBuilder()
        var continent=""
        when (region){
            "EUW"->continent="europe"
            "EUN"->continent="europe"
            "NA"->continent="americas"
        }
        return url
            .append("https://")
            .append(continent)
            .append(".api.riotgames.com/lol/match/v5/matches/")
            .append(matchId)
            .append("?api_key=")
            .append(apiKey).toString()
    }

    fun apiCall(url: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        return client.newCall(request).execute().body()?.string()
    }
    fun setSummoner(jsonObjectString:String):Summoner{
        val parser= JsonParser()
        val element:JsonObject=parser.parse(jsonObjectString) as JsonObject
        try {
            summoner.id=element.get("id").asString
            summoner.summonerName=element.get("name").asString
            summoner.puuid=element.get("puuid").asString
            summoner.accountId=element.get("accountId").asString
            summoner.summonerLevel=element.get("summonerLevel").asInt
        }
        catch(e:NullPointerException){
            println("failed")
        }

        return summoner
    }
    fun getMostPlayedChampion(response: String): JsonObject {
        val parser = JsonParser()
        val championArray: JsonArray = parser.parse(response) as JsonArray
        return championArray.get(0).asJsonObject
    }
    fun getRankedStatistics(response: String): JsonElement {
        val parser = JsonParser()
        val rankedArray: JsonArray = parser.parse(response) as JsonArray
        return rankedArray.get(0).asJsonObject
    }
    fun getTopCinci(response:String): Array<Int> {
        val array= arrayOf(0,0,0,0,0,0,0,0,0,0)
        val parser = JsonParser()
        val rankedArray: JsonArray = parser.parse(response) as JsonArray
        for(i in 0 until 5){
            array[i]=rankedArray.get(i).asJsonObject.get("championId").asInt
        }
        return array
    }
    fun getLastMatch(response: String):String {
        var result= response.replace("[","").replace("]","")
        result=result.substring(1, result.length-1)
        return result
    }
    fun getLastMatchChampions(response:String):Array<String>{
        val array= arrayOf("","","","","","","","","","")
        val parser = JsonParser()
        val championArray=parser.parse(response) as JsonObject
        for(i in 0 until 10){
            array[i]=championArray.get("info").asJsonObject.get("participants").asJsonArray.get(i).asJsonObject.get("championName").toString()
        }
        return array
    }
}
