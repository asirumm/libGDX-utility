package com.diagantika.Animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

/**
 * Class manager animasi
 *
 * Cara penggunaan :
 * <pre>
 *     // gambar saat ini yang harus di render
 *     TextureRegion currentFrame;
 *
 *  AnimationManager<TextureRegion> am  = new AnimationManager();
 *  AnimationHelper help = new AnimationHelper();
 *
 *  Array<TextureRegion> anim = help.getAnimationTextureRegion("data-",atlas);
 *
 *  am.addAnimation("idle",anim);
 *  am.currentAnimation= "idle";
 *
 *  currentFrame = getFirstTexture();
 *
 *  // di render
 *  am.statetime  += deltaTime;
 *  currentFrame = am.getCurrentFrame(true);
 *
 * </pre>
 *
 */
public class AnimationManager<T> {
    private HashMap<String, Animation<T>> animations;
    private String currentAnimation;
    public float stateTime;

    public AnimationManager() {
        animations = new HashMap<>();
    }

    /**
     * pengecekan apakah animasi sudah selesai pada current animation
     */
    public boolean isAnimationFinished(){
        return animations
            .get(currentAnimation)
            .isAnimationFinished(stateTime);
    }

    /**
     * Memberikan textureRegion saat ini berdasarkan stateTime
     * untuk menjalankan animasi
     */
    public T getCurrentFrame(boolean looping) {
        if (animations.isEmpty()){
            StringBuilder text = new StringBuilder();
            text.append("data pada array animasi kosong, ");
            text.append("tidak dapat menjalankan animasi pada current frame : ");
            text.append(currentAnimation).append("silahkan cek kembali");
            throw new RuntimeException(text.toString());
        }

        return animations
            .get(currentAnimation)
            .getKeyFrame(stateTime, looping);
    }

    public void addAnimation(String name, Animation<T> animation){
        animations.put(name,animation);
    }

    /**
     * @return Texture pertama
     */
   public T getFirstTexture(){
        return animations.get(currentAnimation).getKeyFrame(stateTime);
   }
}
