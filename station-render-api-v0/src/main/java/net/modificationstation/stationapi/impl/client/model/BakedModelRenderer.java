package net.modificationstation.stationapi.impl.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.minecraft.sortme.GameRenderer;
import net.minecraft.util.Vec3i;
import net.modificationstation.stationapi.api.client.model.BakedModel;
import net.modificationstation.stationapi.api.client.model.Vertex;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.client.texture.StationBlockRendererProvider;
import net.modificationstation.stationapi.mixin.render.client.BlockRendererAccessor;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;

import java.util.*;

public class BakedModelRenderer {

    private static final Random random = new Random();

    public static boolean renderWorld(BlockRenderer blockRenderer, BlockBase block, BakedModel model, BlockView blockView, int x, int y, int z) {
        boolean rendered = false;
        if (model != null) {
            Vec3i pos = new Vec3i(x, y, z);
            long seed = MathHelper.hashCode(x, y, z);
            int textureOverridePosition = ((BlockRendererAccessor) blockRenderer).getTextureOverride();
            Atlas.Sprite textureOverride = null;
            Atlas atlas;
            if (textureOverridePosition >= 0) {
                atlas = Atlases.getTerrain();
                textureOverride = atlas.getTexture(textureOverridePosition);
            } else
                atlas = model.getSprite().getAtlas();
            boolean noTextureOverride = textureOverride == null;
            TessellatorAccessor originalAccessor = (TessellatorAccessor) Tessellator.INSTANCE;
            Tessellator tessellator = atlas.getTessellator();
            if (!((TessellatorAccessor) tessellator).getDrawing()) {
                ((StationBlockRendererProvider) blockRenderer).getStationBlockRenderer().activeAtlases.add(atlas);
                tessellator.start();
                tessellator.setOffset(originalAccessor.getXOffset(), originalAccessor.getYOffset(), originalAccessor.getZOffset());
            }
            int colourMultiplier = block.getColourMultiplier(blockView, x, y, z);
            float
                    colourMultiplierRed = (float)(colourMultiplier >> 16 & 255) / 255.0F,
                    colourMultiplierGreen = (float)(colourMultiplier >> 8 & 255) / 255.0F,
                    colourMultiplierBlue = (float)(colourMultiplier & 255) / 255.0F;
            if (GameRenderer.field_2340) {
                float
                        colourMultiplierGreenTmp = (colourMultiplierRed * 30.0F + colourMultiplierGreen * 70.0F) / 100.0F,
                        colourMultiplierBlueTmp = (colourMultiplierRed * 30.0F + colourMultiplierBlue * 70.0F) / 100.0F;
                colourMultiplierRed = (colourMultiplierRed * 30.0F + colourMultiplierGreen * 59.0F + colourMultiplierBlue * 11.0F) / 100.0F;
                colourMultiplierGreen = colourMultiplierGreenTmp;
                colourMultiplierBlue = colourMultiplierBlueTmp;
            }
            float
                    brightnessMiddle = blockView.getBrightness(x, y, z),
                    brightnessBottom = blockView.getBrightness(x, y - 1, z),
                    brightnessTop = blockView.getBrightness(x, y + 1, z),
                    brightnessEast = blockView.getBrightness(x, y, z - 1),
                    brightnessWest = blockView.getBrightness(x, y, z + 1),
                    brightnessNorth = blockView.getBrightness(x - 1, y, z),
                    brightnessSouth = blockView.getBrightness(x + 1, y, z);
            Direction[] directions = Arrays.copyOf(Direction.values(), Direction.values().length + 1);
            for (int vertexSet = 0, vertexSetCount = directions.length; vertexSet < vertexSetCount; vertexSet++) {
                Direction face = directions[vertexSet];
                random.setSeed(seed);
                ImmutableList<Vertex> vertexes = model.getVertexes(blockView, pos, face, random);
                if (vertexes.isEmpty() || (face != null && !block.isSideRendered(blockView, x + face.vector.x, y + face.vector.y, z + face.vector.z, vertexSet)))
                    continue;
                rendered = true;
                Vertex vertex;
                for (int i = 0, vertexesSize = vertexes.size(); i < vertexesSize; i++) {
                    vertex = vertexes.get(i);
                    if (vertex.shade)
                        tessellator.colour(
                                model.useAmbientOcclusion() ?
                                        LightingHelper.getSmoothForVertex(
                                                block, blockView, x, y, z,
                                                vertex, i % 4,
                                                colourMultiplierRed, colourMultiplierGreen, colourMultiplierBlue
                                        ) :
                                        LightingHelper.getFastForVertex(
                                                vertex,
                                                colourMultiplierRed, colourMultiplierGreen, colourMultiplierBlue,
                                                brightnessMiddle, brightnessBottom, brightnessTop, brightnessEast, brightnessWest, brightnessNorth, brightnessSouth
                                        )
                        );
                    else
                        tessellator.colour(colourMultiplierRed, colourMultiplierGreen, colourMultiplierBlue);
                    tessellator.vertex(x + vertex.x, y + vertex.y, z + vertex.z,
                            noTextureOverride ? vertex.u : (textureOverride.getX() + vertex.lightingFace.axis.get2DX(vertex.x, vertex.y, vertex.z) * textureOverride.getWidth()) / textureOverride.getAtlas().getImage().getWidth(),
                            noTextureOverride ? vertex.v : (textureOverride.getY() + vertex.lightingFace.axis.get2DY(vertex.x, vertex.y, vertex.z) * textureOverride.getHeight()) / textureOverride.getAtlas().getImage().getHeight()
                    );
                }
            }
        }
        return rendered;
    }

    public static void renderInventory(BakedModel model) {
        if (model != null) {
            Atlas atlas = model.getSprite().getAtlas();
            Tessellator tessellator = atlas.getTessellator();
            tessellator.start();
            Direction[] directions = Arrays.copyOf(Direction.values(), Direction.values().length + 1);
            //noinspection ForLoopReplaceableByForEach
            for (int vertexSet = 0; vertexSet < directions.length; vertexSet++) {
                Direction face = directions[vertexSet];
                random.setSeed(42);
                ImmutableList<Vertex> vertexes = model.getVertexes(null, null, face, random);
                Vertex vertex;
                for (int i = 0, vertexesSize = vertexes.size(); i < vertexesSize; i++) {
                    vertex = vertexes.get(i);
                    tessellator.setNormal(vertex.normalX, vertex.normalY, vertex.normalZ);
                    tessellator.vertex(vertex.x - .5, vertex.y - .5, vertex.z - .5, vertex.u, vertex.v);
                }
            }
            atlas.bindAtlas();
            tessellator.draw();
        }
    }
}