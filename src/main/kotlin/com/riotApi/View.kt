package com.riotApi

import com.google.gson.JsonObject
import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.chart.PieChart
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.text.FontWeight
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tornadofx.*
import java.lang.IndexOutOfBoundsException
import java.lang.NullPointerException


class MyView: View("Program Riot Api PASI") {
    private var summoner:Summoner=Summoner()
    private val booleanProperty = SimpleBooleanProperty()
    private var temp: JsonObject= JsonObject()
    private var rankedStatistic:JsonObject= JsonObject()
    private val controller: BasicController by inject()
    private var champIcon:ImageView by singleAssign()
    private var summTextField: TextField by singleAssign()
    private var labelTextChampion:Label by singleAssign()
    private var labelCufar:Label by singleAssign()
    private var checkboxCufar:CheckBox by singleAssign()
    private var piechartRanked:PieChart by singleAssign()
    private val selectedRegion = SimpleStringProperty()
    private val regions: ObservableList<String> = FXCollections.observableArrayList("EUN", "EUW","NA")
    private var tierIcon:ImageView by singleAssign()
    private var labelTier:Label by singleAssign()
    private var labelRank:Label by singleAssign()
    private var labelTopCampioni:Label by singleAssign()
    private var labelWinrate:Label by singleAssign()
    private var topCinci= arrayOf(0,0,0,0,0)
    private var image1:ImageView by singleAssign()
    private var image2:ImageView by singleAssign()
    private var image3:ImageView by singleAssign()
    private var image4:ImageView by singleAssign()
    private var image5:ImageView by singleAssign()
    private var image11:ImageView by singleAssign()
    private var image12:ImageView by singleAssign()
    private var image13:ImageView by singleAssign()
    private var image14:ImageView by singleAssign()
    private var image15:ImageView by singleAssign()
    private var image16:ImageView by singleAssign()
    private var image17:ImageView by singleAssign()
    private var image18:ImageView by singleAssign()
    private var image19:ImageView by singleAssign()
    private var image20:ImageView by singleAssign()
    private var lastMatchId=""
    private var lastMatchChampions= arrayOf("","","","","","","","","","")
    private var labelLastMatch:Label by singleAssign()
    private var labelVs:Label by singleAssign()



    override val root =vbox {

        hbox{
            combobox(selectedRegion,regions) {
                this.selectionModel.selectFirst()
                hboxConstraints {
                    marginTop=20.0
                    marginLeft=380.0
                }
            }
            button("Search summoner") {
                hboxConstraints {
                    marginTop=20.0
                }
                action {
                    shortcut("Ctrl+S")
                     GlobalScope.launch{
                             try {
                                 controller.apiCall(
                                     controller.urlBuilderSummoner(
                                         summTextField.text,
                                         selectedRegion.value.toString()
                                     )
                                 )
                                     ?.let { summoner = controller.setSummoner(it) }
                             } catch (e: IndexOutOfBoundsException) {
                                 println("Erorare la setarea summonerului")
                             }
                             try {
                                 controller.apiCall(controller.urlBuilderMastery(selectedRegion.value.toString()))
                                     ?.let {
                                         temp = controller.getMostPlayedChampion(it)
                                         topCinci = controller.getTopCinci(it)
                                     }
                             } catch (e: IndexOutOfBoundsException) {
                                 println("most played champion error")
                             }
                         try {
                             controller.apiCall(controller.urlBuilderRanked(selectedRegion.value.toString()))
                                 ?.let { rankedStatistic = controller.getRankedStatistics(it) as JsonObject }
                         }
                         catch(e: IndexOutOfBoundsException){
                             println("ranked statistics error")
                         }
                         try {
                             controller.apiCall(controller.urlMatch(selectedRegion.value.toString()))
                                 ?.let { lastMatchId = controller.getLastMatch(it) }
                         }
                         catch(e: IndexOutOfBoundsException){
                             println("last match")
                         }
                         try {
                             controller.apiCall(controller.getLastMatchData(selectedRegion.value.toString(),lastMatchId))
                                 ?.let { lastMatchChampions = controller.getLastMatchChampions(it) }
                         }
                         catch(e: IndexOutOfBoundsException){
                             println("last match data")
                         }
                         var winrate =0f
                         try {
                              winrate = RankedStatistics().calculateWinrate(rankedStatistic)

                         }
                         catch (e:NullPointerException){
                             println("Erorare la detectarea statisticilor rank")
                         }

                        val championText:String=championId(temp.get("championId").asInt)
                         val topCinciText= arrayOf("","","","","")
                         for(i in 0 until 5) {
                             topCinciText[i]= championId(topCinci[i])
                         }
                         GlobalScope.launch{
                         image1.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${topCinciText[0].replace(" ","")}.png")
                         image2.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${topCinciText[1].replace(" ","")}.png")
                         image3.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${topCinciText[2].replace(" ","")}.png")
                         image4.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${topCinciText[3].replace(" ","")}.png")
                         image5.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${topCinciText[4].replace(" ","")}.png")
                             }
                         checkboxCufar.isVisible=true
                         labelCufar.isVisible=true
                         booleanProperty.set(temp.get("chestGranted").asBoolean)
                         champIcon.fitWidth=20.0
                         champIcon.fitHeight=20.0
                        champIcon.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${championText.replace(" ","")}.png")
                         tierIcon.image=Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\img\\${rankedStatistic.get("tier").asString.toLowerCase()}.png")

                         GlobalScope.launch{
                         image11.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${lastMatchChampions[0].substring(1, lastMatchChampions[0].length-1).replace(" ","")}.png")
                         image12.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${lastMatchChampions[1].substring(1, lastMatchChampions[1].length-1).replace(" ","")}.png")
                         image13.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${lastMatchChampions[2].substring(1, lastMatchChampions[2].length-1).replace(" ","")}.png")
                         image14.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${lastMatchChampions[3].substring(1, lastMatchChampions[3].length-1).replace(" ","")}.png")
                         image15.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${lastMatchChampions[4].substring(1, lastMatchChampions[4].length-1).replace(" ","")}.png")
                         image16.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${lastMatchChampions[5].substring(1, lastMatchChampions[5].length-1).replace(" ","")}.png")
                         image17.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${lastMatchChampions[6].substring(1, lastMatchChampions[6].length-1).replace(" ","")}.png")
                         image18.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${lastMatchChampions[7].substring(1, lastMatchChampions[7].length-1).replace(" ","")}.png")
                         image19.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${lastMatchChampions[8].substring(1, lastMatchChampions[8].length-1).replace(" ","")}.png")
                         image20.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${lastMatchChampions[9].substring(1, lastMatchChampions[9].length-1).replace(" ","")}.png")
                             }
                         val rankedItems: Map<String, Double> = mapOf(
                             "Wins=${rankedStatistic.get("wins").asInt}" to rankedStatistic.get("wins").asDouble,
                             "Losses=${rankedStatistic.get("losses").asInt}" to rankedStatistic.get("losses").asDouble
                         )
                         image1.image= Image("File:\\C:\\Users\\kiv\\Desktop\\dragontail-11.8.1\\11.8.1\\img\\champion\\${championText.replace(" ","")}.png")
                         Platform.runLater {
                             piechartRanked.data.clear()
                             piechartRanked.data(rankedItems)
                             labelTextChampion.text="Cel mai jucat campion este ${championId(temp.get("championId").asInt)} \nCu ${temp.get("championPoints")} puncte"
                             piechartRanked.title="Ranked Win/Loss"
                             piechartRanked.startAngle=30.0
                             labelTier.text="                       TIER"
                             labelRank.text="                          ${rankedStatistic.get("rank").asString}"
                             labelWinrate.text="                WINRATE=${winrate}%"
                             labelTopCampioni.text="      TOP 5 CEI MAI JUCATI CAMPIONI SUNT"
                             labelLastMatch.text="                          LastMatch"
                             labelVs.text="                                    VS"
                         }
                    }
                }
            }
        }
        summTextField=textfield {
            tooltip("Enter Summoner name")
            vboxConstraints {
                marginLeft=380.0
                marginRight=419.0
            }
            setPrefSize(80.0, 20.0)
        }
        vbox {
            vboxConstraints {
            marginLeft=380.0}
            labelTextChampion = label()

            hbox{
                labelCufar=label("Cufar Luat?")
                labelCufar.isVisible=false
                checkboxCufar=checkbox("",booleanProperty)
                checkboxCufar.isVisible=false
                checkboxCufar.disableProperty().set(true)
                champIcon=imageview(lazyload = true)
            }
        }
        hbox {
            vbox{
                label(" ")
                label(" ")
                labelTier=label ()
                tierIcon=imageview()
                labelRank=label()
                labelWinrate=label()
            }
            label("                ")
            vbox {
                label(" ")
                piechartRanked = piechart ()
            }
            vbox {
                label(" ")
                label(" ")
                labelTopCampioni = label()
                hbox {
                    image1 = imageview {
                        fitWidth = 50.0
                        fitHeight = 50.0
                    }
                    image2 = imageview {
                        fitWidth = 50.0
                        fitHeight = 50.0
                    }
                    image3 = imageview {
                        fitWidth = 50.0
                        fitHeight = 50.0
                    }
                    image4 = imageview {
                        fitWidth = 50.0
                        fitHeight = 50.0
                    }
                    image5 = imageview {
                        fitWidth = 50.0
                        fitHeight = 50.0
                    }
                }
                label("")
                label("")
                label("")
                labelLastMatch=label {
                    style{
                        fontWeight = FontWeight.EXTRA_BOLD
                        fontSize=13.px
                    }
                }
                hbox{
                    image11 = imageview {
                        fitWidth = 50.0
                        fitHeight = 50.0
                    }
                    image12 = imageview {
                        fitWidth = 50.0
                        fitHeight = 50.0
                    }
                    image13 = imageview {
                        fitWidth = 50.0
                        fitHeight = 50.0
                    }
                    image14 = imageview {
                        fitWidth = 50.0
                        fitHeight = 50.0
                    }
                    image15 = imageview {
                        fitWidth = 50.0
                        fitHeight = 50.0
                    }
                }
                labelVs=label {
                    style{
                        fontWeight = FontWeight.EXTRA_BOLD
                    }
                }
                hbox{
                    image16 = imageview {
                        fitWidth = 50.0
                        fitHeight = 50.0
                    }
                    image17 = imageview {
                        fitWidth = 50.0
                        fitHeight = 50.0
                    }
                    image18 = imageview {
                        fitWidth = 50.0
                        fitHeight = 50.0
                    }
                    image19 = imageview {
                        fitWidth = 50.0
                        fitHeight = 50.0
                    }
                    image20 = imageview {
                        fitWidth = 50.0
                        fitHeight = 50.0
                    }
                }
            }
        }
    }
    init {
        addStageIcon(Image("file:///C:\\iconApi.png"))
    }
}
