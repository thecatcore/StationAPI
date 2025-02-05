package net.modificationstation.stationapi.impl.vanillafix.client.gui.screen;

import lombok.RequiredArgsConstructor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.client.gui.screen.StationScreen;
import net.modificationstation.stationapi.api.client.gui.widget.ButtonWidgetAttachedContext;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@RequiredArgsConstructor
public class WarningScreen extends StationScreen {

    public static final String
            ROOT_KEY = MODID.id("warning").toString(),
            WARNING_KEY = ROOT_KEY + "." + MODID.id("warning");

    private static final int CONFIRMATION_TIMEOUT = FabricLoader.getInstance().isDevelopmentEnvironment() ? 3000 : 25000;

    private final ScreenBase parent;
    private final Runnable action;
    private final String
            explanationKey,
            confirmKey;
    private ButtonWidgetAttachedContext confirmButton;
    private final long createdTimestamp = System.currentTimeMillis();

    @Override
    public void init() {
        super.init();
        confirmButton = btns.attach(
                id -> {
                    Button button = new Button(id, width / 2 - 100, height - height / 10 - 44, I18n.translate(confirmKey));
                    button.active = false;
                    return button;
                },
                button -> action.run()
        );
        btns.attach(
                id -> new Button(id, width / 2 - 100, height - height / 10 - 20, I18n.translate("gui.cancel")),
                button -> minecraft.openScreen(parent)
        );
    }

    private void renderDirtBackground(long time) {
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        Tessellator tessellator = Tessellator.INSTANCE;
        GL11.glBindTexture(3553, minecraft.textureManager.getTextureId("/gui/background.png"));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        float f = 32.0f;
        int color = 48 + (int) (MathHelper.abs(MathHelper.sin((float) ((time % 50000L) / 50000D * Math.PI * 2))) * 32);
        color = color << 16 | color << 8 | color;
        double offsetU = MathHelper.sin((float) ((time % 30000L) / 30000D * Math.PI * 2)) * 10;
        double offsetV = MathHelper.sin((float) ((time % 5000L) / 5000D * Math.PI * 2));
        tessellator.start();
        tessellator.colour(color);
        tessellator.vertex(0.0, height, 0.0, offsetU, height / f + offsetV);
        tessellator.vertex(width, height, 0.0, width / f + offsetU, height / f + offsetV);
        tessellator.vertex(width, 0.0, 0.0, width / f + offsetU, offsetV);
        tessellator.vertex(0.0, 0.0, 0.0, offsetU, offsetV);
        tessellator.draw();
    }

    @Override
    public void render(int i, int j, float f) {
        long time = System.currentTimeMillis();
        renderDirtBackground(time);
        GL11.glPushMatrix();
        String warning = I18n.translate(WARNING_KEY);
        double warningScale = 2 + MathHelper.sin((float) ((time % 5000L) / 5000D * Math.PI * 2)) * 0.1F;
        GL11.glTranslated(width / 2D - textManager.getTextWidth(warning) * warningScale / 2D, height / 10D, 0);
        GL11.glScaled(warningScale, warningScale, 1);
        drawTextWithShadow(textManager, warning, 0, 0, Color.YELLOW.hashCode());
        GL11.glPopMatrix();
        String[] info = I18n.translate(explanationKey).split("\\n");
        int curHeight = height / 3;
        for (String line : info) {
            int lineWidth = textManager.getTextWidth(line);
            if (lineWidth > width - 20) {
                int begin = 0;
                int lastSpace = -1;
                for (int cur = 0, lineLength = line.length(); cur < lineLength; cur++) {
                    boolean isSpace = line.charAt(cur) == ' ';
                    boolean isEnd = cur + 1 == lineLength;
                    if (isSpace || isEnd) {
                        String newLine = isEnd ? line.substring(begin) : line.substring(begin, cur);
                        int newLineWidth = textManager.getTextWidth(newLine);
                        if (newLineWidth > width - 20) {
                            drawTextWithShadowCentred(textManager, line.substring(begin, lastSpace), width / 2, curHeight, Color.WHITE.hashCode());
                            curHeight += 10;
                            begin = lastSpace + 1;
                        }
                        if (isSpace)
                            lastSpace = cur;
                        if (isEnd) {
                            drawTextWithShadowCentred(textManager, line.substring(begin), width / 2, curHeight, Color.WHITE.hashCode());
                            curHeight += 10;
                        }
                    }
                }
            } else {
                drawTextWithShadowCentred(textManager, line, width / 2, curHeight, Color.WHITE.hashCode());
                curHeight += 10;
            }
        }
        long confirmationTimeout = time - createdTimestamp;
        if (confirmationTimeout > CONFIRMATION_TIMEOUT) {
            if (!confirmButton.button().active) {
                confirmButton.button().active = true;
                confirmButton.button().text = I18n.translate(confirmKey);
            }
        } else confirmButton.button().text = I18n.translate(confirmKey) + " (" + (CONFIRMATION_TIMEOUT - confirmationTimeout) / 1000 + "s)";
        super.render(i, j, f);
    }
}
