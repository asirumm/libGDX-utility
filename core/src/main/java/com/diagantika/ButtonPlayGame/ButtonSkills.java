package com.diagantika.ButtonPlayGame;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ButtonSkills extends Button {
    private boolean isPressed=false;

    public ButtonSkills(Skin skin  , Label label){
        super(skin);

        Table table = new Table();
        table.add().expandX().height(getHeight()-label.getHeight()).growY().row();
        table.add(label).right().expandX().row();

        table.setTouchable(Touchable.enabled);
        setTouchable(Touchable.enabled);
        table.setFillParent(true);
        addActor(table);
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    @Override
    protected Drawable getBackgroundDrawable() {
        if (this.isPressed) {
            if (getStyle().down != null) return getStyle().down;
        }
        return getStyle().up;
    }
}
