package com.hq.simpleanimation.simpview;


import com.hq.simpleanimation.simpview.attention.BounceAnimator;
import com.hq.simpleanimation.simpview.attention.FlashAnimator;
import com.hq.simpleanimation.simpview.attention.PulseAnimator;
import com.hq.simpleanimation.simpview.attention.RubberBandAnimator;
import com.hq.simpleanimation.simpview.attention.ShakeAnimator;
import com.hq.simpleanimation.simpview.attention.StandUpAnimator;
import com.hq.simpleanimation.simpview.attention.SwingAnimator;
import com.hq.simpleanimation.simpview.attention.TadaAnimator;
import com.hq.simpleanimation.simpview.attention.WaveAnimator;
import com.hq.simpleanimation.simpview.attention.WobbleAnimator;
import com.hq.simpleanimation.simpview.bouncing_entrances.BounceInAnimator;
import com.hq.simpleanimation.simpview.bouncing_entrances.BounceInDownAnimator;
import com.hq.simpleanimation.simpview.bouncing_entrances.BounceInLeftAnimator;
import com.hq.simpleanimation.simpview.bouncing_entrances.BounceInRightAnimator;
import com.hq.simpleanimation.simpview.bouncing_entrances.BounceInUpAnimator;
import com.hq.simpleanimation.simpview.fading_entrances.FadeInAnimator;
import com.hq.simpleanimation.simpview.fading_entrances.FadeInDownAnimator;
import com.hq.simpleanimation.simpview.fading_entrances.FadeInLeftAnimator;
import com.hq.simpleanimation.simpview.fading_entrances.FadeInRightAnimator;
import com.hq.simpleanimation.simpview.fading_entrances.FadeInUpAnimator;
import com.hq.simpleanimation.simpview.fading_exits.FadeOutAnimator;
import com.hq.simpleanimation.simpview.fading_exits.FadeOutDownAnimator;
import com.hq.simpleanimation.simpview.fading_exits.FadeOutLeftAnimator;
import com.hq.simpleanimation.simpview.fading_exits.FadeOutRightAnimator;
import com.hq.simpleanimation.simpview.fading_exits.FadeOutUpAnimator;
import com.hq.simpleanimation.simpview.flippers.FlipInXAnimator;
import com.hq.simpleanimation.simpview.flippers.FlipInYAnimator;
import com.hq.simpleanimation.simpview.flippers.FlipOutXAnimator;
import com.hq.simpleanimation.simpview.flippers.FlipOutYAnimator;
import com.hq.simpleanimation.simpview.rotating_entrances.RotateInAnimator;
import com.hq.simpleanimation.simpview.rotating_entrances.RotateInDownLeftAnimator;
import com.hq.simpleanimation.simpview.rotating_entrances.RotateInDownRightAnimator;
import com.hq.simpleanimation.simpview.rotating_entrances.RotateInUpLeftAnimator;
import com.hq.simpleanimation.simpview.rotating_entrances.RotateInUpRightAnimator;
import com.hq.simpleanimation.simpview.rotating_exits.RotateOutAnimator;
import com.hq.simpleanimation.simpview.rotating_exits.RotateOutDownLeftAnimator;
import com.hq.simpleanimation.simpview.rotating_exits.RotateOutDownRightAnimator;
import com.hq.simpleanimation.simpview.rotating_exits.RotateOutUpLeftAnimator;
import com.hq.simpleanimation.simpview.rotating_exits.RotateOutUpRightAnimator;
import com.hq.simpleanimation.simpview.sliders.SlideInDownAnimator;
import com.hq.simpleanimation.simpview.sliders.SlideInLeftAnimator;
import com.hq.simpleanimation.simpview.sliders.SlideInRightAnimator;
import com.hq.simpleanimation.simpview.sliders.SlideInUpAnimator;
import com.hq.simpleanimation.simpview.sliders.SlideOutDownAnimator;
import com.hq.simpleanimation.simpview.sliders.SlideOutLeftAnimator;
import com.hq.simpleanimation.simpview.sliders.SlideOutRightAnimator;
import com.hq.simpleanimation.simpview.sliders.SlideOutUpAnimator;
import com.hq.simpleanimation.simpview.zooming_entrances.ZoomInAnimator;
import com.hq.simpleanimation.simpview.zooming_entrances.ZoomInDownAnimator;
import com.hq.simpleanimation.simpview.zooming_entrances.ZoomInLeftAnimator;
import com.hq.simpleanimation.simpview.zooming_entrances.ZoomInRightAnimator;
import com.hq.simpleanimation.simpview.zooming_entrances.ZoomInUpAnimator;
import com.hq.simpleanimation.simpview.zooming_exits.ZoomOutAnimator;
import com.hq.simpleanimation.simpview.zooming_exits.ZoomOutDownAnimator;
import com.hq.simpleanimation.simpview.zooming_exits.ZoomOutLeftAnimator;
import com.hq.simpleanimation.simpview.zooming_exits.ZoomOutRightAnimator;
import com.hq.simpleanimation.simpview.zooming_exits.ZoomOutUpAnimator;

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
