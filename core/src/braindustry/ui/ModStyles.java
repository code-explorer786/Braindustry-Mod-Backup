package braindustry.ui;

import arc.graphics.Color;
import arc.scene.style.ScaledNinePatchDrawable;
import arc.scene.ui.Button;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import braindustry.gen.BDTex;
import braindustry.gen.BDTex;
import mindustry.gen.Tex;
import mindustry.ui.Styles;

import static mindustry.gen.Tex.*;

public class ModStyles {
    private static final Color black = new Color(0f, 0f, 0f, 1f), black9 = new Color(0f, 0f, 0f, 0.9f), black8 = new Color(0f, 0f, 0f, 0.8f), black6 = new Color(0f, 0f, 0f, 0.6f), black5 = new Color(0f, 0f, 0f, 0.5f), black3 = new Color(0f, 0f, 0f, 0.3f), none = new Color(0f, 0f, 0f, 0f);

    public static ImageButton.ImageButtonStyle buttonSquarei, alphai,buttonPanei;
    public static TextButton.TextButtonStyle buttonEdge3t, buttonPanet, buttonPaneTopt, buttonPaneBottomt;
    public static Button.ButtonStyle buttonColor;

    public static void load() {
        buttonColor = new Button.ButtonStyle(Styles.defaultb) {{
            over = BDTex.buttonColorOver;
            down = BDTex.buttonColorDown;
            up = disabled = BDTex.buttonColor;
        }};
        buttonEdge3t = new TextButton.TextButtonStyle(Styles.defaultt) {{
            over = BDTex.buttonEdge3Over;
            down = BDTex.buttonEdge3Down;
            up = disabled = Tex.buttonEdge3;
        }};
        buttonPanet = new TextButton.TextButtonStyle(Styles.defaultt) {{
            over = BDTex.buttonPaneOver;
            down = BDTex.buttonPaneDown;
            up = disabled = pane;
            checked=BDTex.buttonPaneOver;
        }};
        buttonPaneTopt = new TextButton.TextButtonStyle(Styles.defaultt) {{
            over = BDTex.buttonPaneTopOver;
            down = BDTex.buttonPaneTopDown;
            up = disabled = BDTex.buttonPaneTop;
        }};
        buttonPaneBottomt = new TextButton.TextButtonStyle(Styles.defaultt) {{
            over = BDTex.buttonPaneBottomOver;
            down = BDTex.buttonPaneBottomDown;
            up = disabled = BDTex.buttonPaneBottom;
        }};
        buttonSquarei = new ImageButton.ImageButtonStyle() {{
            imageDisabledColor = Color.gray;
            imageUpColor = Color.white;
            over = buttonSquareOver;
            disabled = buttonDisabled;
            down = buttonSquareDown;
            up = buttonSquare;
        }};
        alphai = new ImageButton.ImageButtonStyle() {{
            imageDisabledColor = Color.gray;
            imageUpColor = Color.white;
            over = ((ScaledNinePatchDrawable) buttonDisabled).tint(black3);
            disabled = ((ScaledNinePatchDrawable) buttonDisabled).tint(none);
            down = ((ScaledNinePatchDrawable) buttonSquareDown).tint(none);
            up = ((ScaledNinePatchDrawable) buttonSquare).tint(none);
        }};
        buttonPanei=new ImageButton.ImageButtonStyle(Styles.defaulti){{
            over = BDTex.buttonPaneOver;
            down = BDTex.buttonPaneDown;
            up = disabled = pane;
            checked=BDTex.buttonPaneOver;
        }};
    }
}
