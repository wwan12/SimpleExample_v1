package com.hq.tool.animation.load;


import com.hq.tool.animation.load.ZLoadingBuilder;
import com.hq.tool.animation.load.ball.ElasticBallBuilder;
import com.hq.tool.animation.load.ball.InfectionBallBuilder;
import com.hq.tool.animation.load.ball.IntertwineBuilder;
import com.hq.tool.animation.load.circle.DoubleCircleBuilder;
import com.hq.tool.animation.load.circle.PacManBuilder;
import com.hq.tool.animation.load.circle.RotateCircleBuilder;
import com.hq.tool.animation.load.circle.SingleCircleBuilder;
import com.hq.tool.animation.load.circle.SnakeCircleBuilder;
import com.hq.tool.animation.load.clock.CircleBuilder;
import com.hq.tool.animation.load.clock.ClockBuilder;
import com.hq.tool.animation.load.path.SearchPathBuilder;
import com.hq.tool.animation.load.star.LeafBuilder;
import com.hq.tool.animation.load.star.StarBuilder;
import com.hq.tool.animation.load.text.TextBuilder;

public enum LOAD_TYPE
{
    CIRCLE(CircleBuilder.class),
    CIRCLE_CLOCK(ClockBuilder.class),
    STAR_LOADING(StarBuilder.class),
    LEAF_ROTATE(LeafBuilder.class),
    DOUBLE_CIRCLE(DoubleCircleBuilder.class),
    PAC_MAN(PacManBuilder.class),
    ELASTIC_BALL(ElasticBallBuilder.class),
    INFECTION_BALL(InfectionBallBuilder.class),
    INTERTWINE(IntertwineBuilder.class),
    TEXT(TextBuilder.class),
    SEARCH_PATH(SearchPathBuilder.class),
    ROTATE_CIRCLE(RotateCircleBuilder.class),
    SINGLE_CIRCLE(SingleCircleBuilder.class),
    SNAKE_CIRCLE(SnakeCircleBuilder.class),
    ;

    private final Class<?> mBuilderClass;

    LOAD_TYPE(Class<?> builderClass)
    {
        this.mBuilderClass = builderClass;
    }

    <T extends ZLoadingBuilder>T newInstance(){
        try
        {
            return (T) mBuilderClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
