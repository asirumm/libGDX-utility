package com.diagantika.Services;

import com.badlogic.gdx.assets.AssetManager;
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

    public ApplicationContext(AssetManager assetManager) {

        log.debug("instance application context telah dibuat");
        this.assetManager = assetManager;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
