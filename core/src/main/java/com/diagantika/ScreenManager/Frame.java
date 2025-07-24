package com.diagantika.ScreenManager;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Frame extends Table {
    public Frame(TextureAtlas atlas){

        Stack stack = new Stack();
        Table table = new Table();

        stack.add(new Image(atlas.findRegion("frameCharacter")));
        stack.add(new Image(atlas.findRegion("blink")));
        stack.add(new Image(atlas.findRegion("plateCharacter")));

        table.add().growY();// menambah actor kosong
        table.row();

        stack.add(table);

        add(stack).size(96,96);
    }
}
