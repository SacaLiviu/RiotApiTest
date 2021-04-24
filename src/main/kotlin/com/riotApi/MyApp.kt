package com.riotApi

import javafx.stage.Stage
import tornadofx.App



class MyApp: App(MyView::class){
    override fun start(stage: Stage) {
        super.start(stage)
        stage.width = 1000.0
        stage.height = 600.0
        stage.isResizable=false
    }
}
