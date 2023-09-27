package com.hq.simpleanimation.tween.action.extend

import android.view.View
import com.hq.simpleanimation.tween.action.ActionData
import com.hq.simpleanimation.tween.action.RunAction
import com.hq.simpleanimation.tween.action.chained.SequenceActionRunBuild
import com.hq.simpleanimation.tween.action.chained.SpawnActionRunBuild

/**
 * 运行动画
 * @param action Action.fadeIn(500) 等action 动画
 */
fun View.runAction(action: () -> ActionData) {
    RunAction.runAction(this, action)
}

/**
 * 创建一个链式的action动画
 * 返回的是顺序执行动画的action
 */
fun View.createAction() : SequenceActionRunBuild {
    return SequenceActionRunBuild(this)
}

/**
 * 创建一个链式的action动画
 * 返回的是顺序执行动画的action
 */
fun View.createSequenceAction() : SequenceActionRunBuild {
    return createAction()
}

/**
 * 创建一个链式的action动画
 * 返回的是同步执行动画的action
 */
fun View.createSpawnAction() : SpawnActionRunBuild {
    return SpawnActionRunBuild(this)
}

/**
 * 停止动作
 */
fun View.stopAction() {
    RunAction.stopAction(this)
}