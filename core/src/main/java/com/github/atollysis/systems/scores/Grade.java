package com.github.atollysis.systems.scores;

import com.badlogic.gdx.graphics.Color;
import com.github.atollysis.systems.Assets;

public enum Grade {
    D(20f, "quackred"),
    C(40f, "quackorange"),
    B(60f, "quackyellow"),
    A(80f, "quackgreen"),
    S(100f, "quackgold");

    // Fields
    private static final Grade[] orderedGrades = {
        D, C, B, A, S
    };
    private static final float error = 0.00001f;

    private final float thresh;
    private final String uiColor;

    // Constructor
    private Grade(float thresh, String uiColor) {
        this.thresh = thresh;
        this.uiColor = uiColor;
    }

    public static Grade getGrade(float percentSorted) {
        if (percentSorted < 0)
            return null;

        for (Grade g : orderedGrades) {
            if (percentSorted <= g.thresh + error)
                return g;
        }
        return null; // > 100
    }

    public Color getColor(Assets assets) {
        return assets.skin().getColor(uiColor);
    }

}
