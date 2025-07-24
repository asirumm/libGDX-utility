package com.diagantika.Services;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.utils.Disposable;
import com.diagantika.Util.Bean;
import com.diagantika.Util.Logger;
import com.diagantika.Util.LoggerConfig;

/**
 * Class handle insance yang sering digunakan
 * NOTe : jangan lupa dispose
 */
public class ApplicationContext implements Disposable {
    private Logger<ApplicationContext> log = new Logger<>(ApplicationContext.class, Bean.getLogConfigInstance());
    private final AssetManager assetManager;
    private Skin widgetSkin;

    public ApplicationContext(AssetManager assetManager) {

        log.debug("instance application context telah dibuat");
        this.assetManager = assetManager;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void setWidgetSkin(Skin widgetSkin) {
        log.debug("widget skin telah di inject");
        this.widgetSkin = widgetSkin;
    }

    public Skin getWidgetSkin() {
        return widgetSkin;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
