package com.aisino.simpleanimation.load;


import com.aisino.simpleanimation.load.ball.ElasticBallBuilder;
import com.aisino.simpleanimation.load.ball.InfectionBallBuilder;
import com.aisino.simpleanimation.load.ball.IntertwineBuilder;
import com.aisino.simpleanimation.load.circle.DoubleCircleBuilder;
import com.aisino.simpleanimation.load.circle.PacManBuilder;
import com.aisino.simpleanimation.load.circle.RotateCircleBuilder;
import com.aisino.simpleanimation.load.circle.SingleCircleBuilder;
import com.aisino.simpleanimation.load.circle.SnakeCircleBuilder;
import com.aisino.simpleanimation.load.clock.CircleBuilder;
import com.aisino.simpleanimation.load.clock.ClockBuilder;
import com.aisino.simpleanimation.load.path.SearchPathBuilder;
import com.aisino.simpleanimation.load.star.LeafBuilder;
import com.aisino.simpleanimation.load.star.StarBuilder;
import com.aisino.simpleanimation.load.text.TextBuilder;

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
