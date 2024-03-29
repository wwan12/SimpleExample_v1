package com.hq.tool.animation.tween.action.chained

import android.view.View
import com.hq.tool.animation.tween.action.ActionData
import com.hq.tool.animation.tween.action.RunAction

/**
 * 顺序动画  可以执行动画的class
 */
class SequenceActionRunBuild : AbstractActionBuild<SequenceActionRunBuild> {
    override val type: ActionBuildTypeEnum
        get() = ActionBuildTypeEnum.SEQUENCE
    private val component: View

    constructor(view: View) {
        component = view
    }

    fun start(): SequenceActionRunBuild {
        RunAction.runAction(component) {
            val animationData = ActionData()
            animationData.type = ActionData.sequence
            animationData.animationDataArray = animationDataArray
            animationData
        }
        return this
    }

    fun stop(): SequenceActionRunBuild {
        RunAction.stopAction(component)
        return this
    }
}