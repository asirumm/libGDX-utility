package com.diagantika.Util;

import com.badlogic.gdx.utils.GdxRuntimeException;

public class AssetChecker {
    public static void checkAsset(Object asset, String assetName) {
        if (asset == null) {
            throw new GdxRuntimeException(assetName + " masih null");
        }
    }

}
