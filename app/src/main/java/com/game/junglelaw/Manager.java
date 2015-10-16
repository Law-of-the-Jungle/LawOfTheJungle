package com.game.junglelaw;

import com.game.junglelaw.circlecc.*;
import java.util.Random;

/**
 * Created by apple on 10/15/15.
 */
public class Manager {

    private Random rand;
    private CircleFactory circleFactory;

    public Manager() {
        rand = new Random();
        circleFactory = new CircleFactory();
    }

    // TODO 监控是否需要添加新的static circle
    // TODO (如有必要)生成新的static circle
    // TODO 判断，circle的相互吃情况
}
