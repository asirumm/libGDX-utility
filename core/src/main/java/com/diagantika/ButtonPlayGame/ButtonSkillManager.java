package com.diagantika.ButtonPlayGame;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class ButtonSkillManager {
    private Array<ButtonSkills> buttonSkills = new Array<>();
    private ButtonSkills currentButtonSkillActive;
    private ButtonSkills lastButtonSkillActive;

    public ButtonSkillManager(Skin skin, Stage stage) {
        // Create separate labels for each button
        Label label1 = new Label("A", skin);
        Label label2 = new Label("B", skin);
        Label label3 = new Label("C", skin);

        ButtonSkills buttonSkills1 = new ButtonSkills(skin, label1);
        buttonSkills1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectButton(buttonSkills1);
            }
        });

        ButtonSkills buttonSkills2 = new ButtonSkills(skin, label2);
        buttonSkills2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectButton(buttonSkills2);
            }
        });

        ButtonSkills buttonSkills3 = new ButtonSkills(skin, label3);
        buttonSkills3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectButton(buttonSkills3);
            }
        });

        buttonSkills.add(buttonSkills1);
        buttonSkills.add(buttonSkills2);
        buttonSkills.add(buttonSkills3);

        HorizontalGroup group = new HorizontalGroup();
        group.space(10f); // Add some spacing between buttons
        group.addActor(buttonSkills1);
        group.addActor(buttonSkills2);
        group.addActor(buttonSkills3);

        group.setPosition(30, 40);
        stage.addActor(group);
    }

    private void selectButton(ButtonSkills newSelectedButton) {
        // Store the last active button
        lastButtonSkillActive = currentButtonSkillActive;

        // Deselect the previously active button
        if (lastButtonSkillActive != null && lastButtonSkillActive != newSelectedButton) {
            lastButtonSkillActive.setPressed(false);
        }

        // Set the new active button
        currentButtonSkillActive = newSelectedButton;

        // Activate the new button
        if (currentButtonSkillActive != null) {
            currentButtonSkillActive.setPressed(true);
        }

        // Optional: Print debug info
        System.out.println("Selected button: " + getCurrentSelectedSkill());
    }

    public void clearSelection() {
        if (currentButtonSkillActive != null) {
            currentButtonSkillActive.setPressed(false);
            lastButtonSkillActive = currentButtonSkillActive;
            currentButtonSkillActive = null;
        }
    }

    public boolean hasSelection() {
        return currentButtonSkillActive != null;
    }

    public String getCurrentSelectedSkill() {
        if (currentButtonSkillActive != null) {
            // Assuming ButtonSkills has a way to get its label text
            // You might need to modify this based on your ButtonSkills implementation
            return "Skill " + (buttonSkills.indexOf(currentButtonSkillActive, true) + 1);
        }
        return null;
    }

    public ButtonSkills getCurrentButtonSkillActive() {
        return currentButtonSkillActive;
    }

    public ButtonSkills getLastButtonSkillActive() {
        return lastButtonSkillActive;
    }

    // Call this method to enable/disable all buttons (useful for turn management)
    public void setButtonsEnabled(boolean enabled) {
        for (ButtonSkills button : buttonSkills) {
            button.setDisabled(!enabled);
        }

        if (!enabled) {
            clearSelection();
        }
    }
}
