package com.diagantika.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.diagantika.Actor.Card;
import com.diagantika.Actor.CardComparisonManager;
import com.diagantika.Actor.CardManager;
import com.diagantika.Actor.Cardx;
import com.diagantika.ScreenManager.AbstractScreen;
import org.tinylog.Logger;

import java.util.List;

public class FirstScreen extends AbstractScreen {
    private CardManager manager;
    @Override

    public void show() {
        Logger.debug("start scereen");

         manager = new CardManager(context.getAssetManager().get("card.atlas"));

        manager.generateRandomCard((byte)1);

        Array<Card> cards = manager.getCards();

        HorizontalGroup group = new HorizontalGroup();
group.setFillParent(true);
        for (int i = 0;i<=cards.size-1;i++){

            group.addActor(cards.get(i));
        }

        group.validate();
        group.pack();
        stageUI.addActor(group);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        manager.render();
    }

    @Override
    protected Color getColor() {
        return Color.RED;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
