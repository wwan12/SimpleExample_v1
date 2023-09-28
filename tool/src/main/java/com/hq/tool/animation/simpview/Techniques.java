package com.hq.tool.animation.simpview;


import com.hq.tool.animation.simpview.BaseViewAnimator;
import com.hq.tool.animation.simpview.attention.BounceAnimator;
import com.hq.tool.animation.simpview.attention.FlashAnimator;
import com.hq.tool.animation.simpview.attention.PulseAnimator;
import com.hq.tool.animation.simpview.attention.RubberBandAnimator;
import com.hq.tool.animation.simpview.attention.ShakeAnimator;
import com.hq.tool.animation.simpview.attention.StandUpAnimator;
import com.hq.tool.animation.simpview.attention.SwingAnimator;
import com.hq.tool.animation.simpview.attention.TadaAnimator;
import com.hq.tool.animation.simpview.attention.WaveAnimator;
import com.hq.tool.animation.simpview.attention.WobbleAnimator;
import com.hq.tool.animation.simpview.bouncing_entrances.BounceInAnimator;
import com.hq.tool.animation.simpview.bouncing_entrances.BounceInDownAnimator;
import com.hq.tool.animation.simpview.bouncing_entrances.BounceInLeftAnimator;
import com.hq.tool.animation.simpview.bouncing_entrances.BounceInRightAnimator;
import com.hq.tool.animation.simpview.bouncing_entrances.BounceInUpAnimator;
import com.hq.tool.animation.simpview.fading_entrances.FadeInAnimator;
import com.hq.tool.animation.simpview.fading_entrances.FadeInDownAnimator;
import com.hq.tool.animation.simpview.fading_entrances.FadeInLeftAnimator;
import com.hq.tool.animation.simpview.fading_entrances.FadeInRightAnimator;
import com.hq.tool.animation.simpview.fading_entrances.FadeInUpAnimator;
import com.hq.tool.animation.simpview.fading_exits.FadeOutAnimator;
import com.hq.tool.animation.simpview.fading_exits.FadeOutDownAnimator;
import com.hq.tool.animation.simpview.fading_exits.FadeOutLeftAnimator;
import com.hq.tool.animation.simpview.fading_exits.FadeOutRightAnimator;
import com.hq.tool.animation.simpview.fading_exits.FadeOutUpAnimator;
import com.hq.tool.animation.simpview.flippers.FlipInXAnimator;
import com.hq.tool.animation.simpview.flippers.FlipInYAnimator;
import com.hq.tool.animation.simpview.flippers.FlipOutXAnimator;
import com.hq.tool.animation.simpview.flippers.FlipOutYAnimator;
import com.hq.tool.animation.simpview.rotating_entrances.RotateInAnimator;
import com.hq.tool.animation.simpview.rotating_entrances.RotateInDownLeftAnimator;
import com.hq.tool.animation.simpview.rotating_entrances.RotateInDownRightAnimator;
import com.hq.tool.animation.simpview.rotating_entrances.RotateInUpLeftAnimator;
import com.hq.tool.animation.simpview.rotating_entrances.RotateInUpRightAnimator;
import com.hq.tool.animation.simpview.rotating_exits.RotateOutAnimator;
import com.hq.tool.animation.simpview.rotating_exits.RotateOutDownLeftAnimator;
import com.hq.tool.animation.simpview.rotating_exits.RotateOutDownRightAnimator;
import com.hq.tool.animation.simpview.rotating_exits.RotateOutUpLeftAnimator;
import com.hq.tool.animation.simpview.rotating_exits.RotateOutUpRightAnimator;
import com.hq.tool.animation.simpview.sliders.SlideInDownAnimator;
import com.hq.tool.animation.simpview.sliders.SlideInLeftAnimator;
import com.hq.tool.animation.simpview.sliders.SlideInRightAnimator;
import com.hq.tool.animation.simpview.sliders.SlideInUpAnimator;
import com.hq.tool.animation.simpview.sliders.SlideOutDownAnimator;
import com.hq.tool.animation.simpview.sliders.SlideOutLeftAnimator;
import com.hq.tool.animation.simpview.sliders.SlideOutRightAnimator;
import com.hq.tool.animation.simpview.sliders.SlideOutUpAnimator;
import com.hq.tool.animation.simpview.zooming_entrances.ZoomInAnimator;
import com.hq.tool.animation.simpview.zooming_entrances.ZoomInDownAnimator;
import com.hq.tool.animation.simpview.zooming_entrances.ZoomInLeftAnimator;
import com.hq.tool.animation.simpview.zooming_entrances.ZoomInRightAnimator;
import com.hq.tool.animation.simpview.zooming_entrances.ZoomInUpAnimator;
import com.hq.tool.animation.simpview.zooming_exits.ZoomOutAnimator;
import com.hq.tool.animation.simpview.zooming_exits.ZoomOutDownAnimator;
import com.hq.tool.animation.simpview.zooming_exits.ZoomOutLeftAnimator;
import com.hq.tool.animation.simpview.zooming_exits.ZoomOutRightAnimator;
import com.hq.tool.animation.simpview.zooming_exits.ZoomOutUpAnimator;

public enum Techniques {


    Flash(FlashAnimator.class),
    Pulse(PulseAnimator.class),
    RubberBand(RubberBandAnimator.class),
    Shake(ShakeAnimator.class),
    Swing(SwingAnimator.class),
    Wobble(WobbleAnimator.class),
    Bounce(BounceAnimator.class),
    Tada(TadaAnimator.class),
    StandUp(StandUpAnimator.class),
    Wave(WaveAnimator.class),

    BounceIn(BounceInAnimator.class),
    BounceInDown(BounceInDownAnimator.class),
    BounceInLeft(BounceInLeftAnimator.class),
    BounceInRight(BounceInRightAnimator.class),
    BounceInUp(BounceInUpAnimator.class),

    FadeIn(FadeInAnimator.class),
    FadeInUp(FadeInUpAnimator.class),
    FadeInDown(FadeInDownAnimator.class),
    FadeInLeft(FadeInLeftAnimator.class),
    FadeInRight(FadeInRightAnimator.class),

    FadeOut(FadeOutAnimator.class),
    FadeOutDown(FadeOutDownAnimator.class),
    FadeOutLeft(FadeOutLeftAnimator.class),
    FadeOutRight(FadeOutRightAnimator.class),
    FadeOutUp(FadeOutUpAnimator.class),

    FlipInX(FlipInXAnimator.class),
    FlipOutX(FlipOutXAnimator.class),
    FlipInY(FlipInYAnimator.class),
    FlipOutY(FlipOutYAnimator.class),
    RotateIn(RotateInAnimator.class),
    RotateInDownLeft(RotateInDownLeftAnimator.class),
    RotateInDownRight(RotateInDownRightAnimator.class),
    RotateInUpLeft(RotateInUpLeftAnimator.class),
    RotateInUpRight(RotateInUpRightAnimator.class),

    RotateOut(RotateOutAnimator.class),
    RotateOutDownLeft(RotateOutDownLeftAnimator.class),
    RotateOutDownRight(RotateOutDownRightAnimator.class),
    RotateOutUpLeft(RotateOutUpLeftAnimator.class),
    RotateOutUpRight(RotateOutUpRightAnimator.class),

    SlideInLeft(SlideInLeftAnimator.class),
    SlideInRight(SlideInRightAnimator.class),
    SlideInUp(SlideInUpAnimator.class),
    SlideInDown(SlideInDownAnimator.class),

    SlideOutLeft(SlideOutLeftAnimator.class),
    SlideOutRight(SlideOutRightAnimator.class),
    SlideOutUp(SlideOutUpAnimator.class),
    SlideOutDown(SlideOutDownAnimator.class),

    ZoomIn(ZoomInAnimator.class),
    ZoomInDown(ZoomInDownAnimator.class),
    ZoomInLeft(ZoomInLeftAnimator.class),
    ZoomInRight(ZoomInRightAnimator.class),
    ZoomInUp(ZoomInUpAnimator.class),

    ZoomOut(ZoomOutAnimator.class),
    ZoomOutDown(ZoomOutDownAnimator.class),
    ZoomOutLeft(ZoomOutLeftAnimator.class),
    ZoomOutRight(ZoomOutRightAnimator.class),
    ZoomOutUp(ZoomOutUpAnimator.class);



    private Class animatorClazz;

    private Techniques(Class clazz) {
        animatorClazz = clazz;
    }

    public BaseViewAnimator getAnimator() {
        try {
            return (BaseViewAnimator) animatorClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
