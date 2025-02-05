package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.binder;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.TextureHelper;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;

import java.awt.image.BufferedImage;

public class ArsenicClock extends StationTextureBinder {

    @SuppressWarnings("deprecation")
    private final Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
    private int[] clockTexture;
    private int[] dialTexture;
    private double currentRotation;
    private double rotationDelay;

    public ArsenicClock(Atlas.Sprite staticReference) {
        super(staticReference);
    }

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        Atlas.Sprite staticReference = getStaticReference();
        int
                textureWidth = staticReference.getWidth(),
                textureHeight = staticReference.getHeight(),
                square = textureWidth * textureHeight;
        dialTexture = new int[square];
        clockTexture = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getSprite(staticReference.getId()).getContents().getBaseFrame().makePixelArray();
        BufferedImage var2 = TextureHelper.getTexture("/misc/dial.png");
        var2.getRGB(0, 0, textureWidth, textureHeight, this.dialTexture, 0, textureWidth);
        grid = new byte[square * 4];
    }

    @Override
    public void update() {
        Atlas.Sprite staticReference = getStaticReference();
        int
                textureWidth = staticReference.getWidth(),
                textureHeight = staticReference.getHeight(),
                square = textureWidth * textureHeight;
        double var1 = 0.0D;
        if (this.minecraft.level != null && this.minecraft.player != null) {
            float var3 = this.minecraft.level.method_198(1.0F);
            var1 = -var3 * (float)Math.PI * 2.0F;
            if (this.minecraft.level.dimension.blocksCompassAndClock) {
                var1 = Math.random() * (double)(float)Math.PI * 2.0D;
            }
        }

        double var22;
        var22 = var1 - this.currentRotation;
        while (var22 < -Math.PI) {
            var22 += (Math.PI * 2D);
        }

        while(var22 >= Math.PI) {
            var22 -= (Math.PI * 2D);
        }

        if (var22 < -1.0D) {
            var22 = -1.0D;
        }

        if (var22 > 1.0D) {
            var22 = 1.0D;
        }

        this.rotationDelay += var22 * 0.1D;
        this.rotationDelay *= 0.8D;
        this.currentRotation += this.rotationDelay;
        double var5 = Math.sin(this.currentRotation);
        double var7 = Math.cos(this.currentRotation);

        for(int var9 = 0; var9 < square; ++var9) {
            int r = this.clockTexture[var9] & 255;
            int g = this.clockTexture[var9] >> 8 & 255;
            int b = this.clockTexture[var9] >> 16 & 255;
            int a = this.clockTexture[var9] >> 24 & 255;
            if (r == b && g == 0 && b > 0) {
                double var14 = -((double)(var9 % textureWidth) / (textureWidth - 1) - 0.5D);
                double var16 = (double)(var9 / textureHeight) / (textureHeight - 1) - 0.5D;
                int var18 = r;
                int var19 = (int)((var14 * var7 + var16 * var5 + 0.5D) * textureWidth);
                int var20 = (int)((var16 * var7 - var14 * var5 + 0.5D) * textureHeight);
                int var21 = (var19 & (textureWidth - 1)) + (var20 & (textureHeight - 1)) * textureWidth;
                r = (this.dialTexture[var21] >> 16 & 255) * r / 255;
                g = (this.dialTexture[var21] >> 8 & 255) * var18 / 255;
                b = (this.dialTexture[var21] & 255) * var18 / 255;
                a = this.dialTexture[var21] >> 24 & 255;
            }

            if (this.render3d) {
                int var23 = (r * 30 + g * 59 + b * 11) / 100;
                int var15 = (r * 30 + g * 70) / 100;
                int var24 = (r * 30 + b * 70) / 100;
                r = var23;
                g = var15;
                b = var24;
            }

            this.grid[var9 * 4] = (byte)r;
            this.grid[var9 * 4 + 1] = (byte)g;
            this.grid[var9 * 4 + 2] = (byte)b;
            this.grid[var9 * 4 + 3] = (byte)a;
        }
    }
}
