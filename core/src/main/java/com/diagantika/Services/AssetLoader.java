package com.diagantika.Services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.diagantika.Constant;
import com.diagantika.Util.Bean;
import com.diagantika.Util.Logger;
import com.diagantika.Util.LoggerConfig;

public class AssetLoader implements Disposable {
    private Logger<AssetLoader> logger = new Logger<>(AssetLoader.class, Bean.getLogConfigInstance());
    private AssetManager assetManager;
    private Skin widgetSkin;// spesial karena load freetypefont berbeda

    public AssetLoader() {
        assetManager = new AssetManager();
        setErrorListener();
    }

    public AssetLoader(FileHandleResolver resolver) {
        assetManager = new AssetManager(resolver);

        setErrorListener();
    }

    private void setErrorListener(){
        assetManager.setErrorListener(new AssetErrorListener() {
            @Override
            public void error(AssetDescriptor assetDescriptor, Throwable throwable) {
                logger.error("gagal saat memuat aset %s",throwable,assetDescriptor.fileName);
                Gdx.app.exit();
            }
        });
    }

    public void load(){
        logger.info("memulai load aset");
        loadAtlases();
        loadWidgetSkin();
    }

    public Skin getWidgetSkin() {
        return widgetSkin;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    private void loadAtlases() {
    }

    private void loadMusic(){

    }

    /**
     * Load skin asset dengan freetype generator
     * agar font tidak blur
     */
    private void loadWidgetSkin(){
        this.widgetSkin = new Skin(Gdx.files.internal(Constant.widgetSkin)) {
            //Override json loader to process FreeType fonts from skin JSON
            @Override
            protected Json getJsonLoader(final FileHandle skinFile) {
                Json json = super.getJsonLoader(skinFile);
                final Skin skin = this;

                json.setSerializer(FreeTypeFontGenerator.class, new Json.ReadOnlySerializer<FreeTypeFontGenerator>() {
                    @Override
                    public FreeTypeFontGenerator read(Json json,
                                                      JsonValue jsonData, Class type) {
                        String path = json.readValue("font", String.class, jsonData);
                        jsonData.remove("font");

                        FreeTypeFontGenerator.Hinting hinting = FreeTypeFontGenerator.Hinting.valueOf(json.readValue("hinting",
                            String.class, "AutoMedium", jsonData));
                        jsonData.remove("hinting");

                        Texture.TextureFilter minFilter = Texture.TextureFilter.valueOf(
                            json.readValue("minFilter", String.class, "Nearest", jsonData));
                        jsonData.remove("minFilter");

                        Texture.TextureFilter magFilter = Texture.TextureFilter.valueOf(
                            json.readValue("magFilter", String.class, "Nearest", jsonData));
                        jsonData.remove("magFilter");

                        FreeTypeFontGenerator.FreeTypeFontParameter parameter = json.readValue(FreeTypeFontGenerator.FreeTypeFontParameter.class, jsonData);
                        parameter.hinting = hinting;
                        parameter.minFilter = minFilter;
                        parameter.magFilter = magFilter;
                        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(skinFile.parent().child(path));
                        BitmapFont font = generator.generateFont(parameter);
                        skin.add(jsonData.name, font);
                        if (parameter.incremental) {
                            generator.dispose();
                            return null;
                        } else {
                            return generator;
                        }
                    }
                });

                return json;
            }
        };
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        logger.info("dispose");
    }
}
