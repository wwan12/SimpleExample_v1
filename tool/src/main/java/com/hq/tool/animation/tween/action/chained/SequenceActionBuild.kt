package com.hq.tool.animation.tween.action.chained

class SequenceActionBuild : AbstractActionBuild<SequenceActionBuild>() {
    override val type: ActionBuildTypeEnum
        get() = ActionBuildTypeEnum.SEQUENCE
}