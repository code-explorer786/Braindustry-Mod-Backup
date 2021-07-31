package braindustry.ui;

import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.ScaledNinePatchDrawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.util.Log;
import braindustry.gen.ModTex;
import mindustry.gen.Tex;
import mindustry.ui.Styles;

import static mindustry.gen.Tex.*;

public class ModStyles {
    private static final Color black = new Color(0f, 0f, 0f, 1f), black9 = new Color(0f, 0f, 0f, 0.9f), black8 = new Color(0f, 0f, 0f, 0.8f), black6 = new Color(0f, 0f, 0f, 0.6f), black5 = new Color(0f, 0f, 0f, 0.5f), black3 = new Color(0f, 0f, 0f, 0.3f), none = new Color(0f, 0f, 0f, 0f);

    public static ImageButton.ImageButtonStyle buttonSquarei, alphai;
    public static TextButton.TextButtonStyle buttonEdge3,buttonPane,buttonPaneTop,buttonPaneBottom,buttonColor;

    public static void load() {
        buttonColor=new TextButton.TextButtonStyle(Styles.defaultt){{
            over = ModTex.buttonColorOver;
            down = ModTex.buttonColorDown;
            up = disabled = ModTex.buttonColor;
        }};
        buttonEdge3 = new TextButton.TextButtonStyle(Styles.defaultt) {{
            over = ModTex.buttonEdge3Over;
            down = ModTex.buttonEdge3Down;
            up = disabled = Tex.buttonEdge3;
        }};
        buttonPane = new TextButton.TextButtonStyle(Styles.defaultt) {{
            over = ModTex.buttonPaneOver;
            down = ModTex.buttonPaneDown;
            up = disabled = pane;
        }};
        buttonPaneTop = new TextButton.TextButtonStyle(Styles.defaultt) {{
            over = ModTex.buttonPaneTopOver;
            down = ModTex.buttonPaneTopDown;
            up = disabled = ModTex.buttonPaneTop;
        }};
        buttonPaneBottom = new TextButton.TextButtonStyle(Styles.defaultt) {{
            over = ModTex.buttonPaneBottomOver;
            down = ModTex.buttonPaneBottomDown;
            up = disabled = ModTex.buttonPaneBottom;
        }};
        buttonSquarei = new ImageButton.ImageButtonStyle() {
            {
                imageDisabledColor = Color.gray;
                imageUpColor = Color.white;
                over = buttonSquareOver;
                disabled = buttonDisabled;
                down = buttonSquareDown;
                up = buttonSquare;
            }
        };
        alphai = new ImageButton.ImageButtonStyle() {
            {
                imageDisabledColor = Color.gray;
                imageUpColor = Color.white;
                over = ((ScaledNinePatchDrawable) buttonDisabled).tint(black3);
                disabled = ((ScaledNinePatchDrawable) buttonDisabled).tint(none);
                down = ((ScaledNinePatchDrawable) buttonSquareDown).tint(none);
                up = ((ScaledNinePatchDrawable) buttonSquare).tint(none);
            }
        };
    }
}
