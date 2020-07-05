package com.homw.gui.d2.swing;

import com.homw.gui.d2.swing.util.CursorUtil;

import java.awt.*;

/**
 * @description Swing Window Toolkit，包括颜色，字体，图标等
 * @author Hom
 * @version 1.0
 * @since 2020-07-05
 */
public class SWT {

    public static final class ColorConst {
        public static final Color LIGHT_BACK = new Color(200, 255, 255);
        public static final Color GRAY_BACK = new Color(233, 233, 233);
        public static final Color GREEN_BACK = new Color(141, 238, 238);

        public static final Color LIGHT_RED = new Color(255, 69, 0);
        public static final Color DEEP_BLUE = new Color(9, 81, 244);
        public static final Color GRAY_BLACK = new Color(45, 45, 45);
        public static final Color SPRING_GREEN = new Color(0, 255, 118);
        public static final Color LIGHT_YELLOW = new Color(255, 200, 0);
        public static final Color SKY_BLUE = new Color(0, 191, 255);
    }

    public static final class FontConst {
        public static final Font PLAIN_9 = new Font("system", Font.PLAIN, 9);
        public static final Font PLAIN_10 = new Font("system", Font.PLAIN, 10);
        public static final Font PLAIN_11 = new Font("system", Font.PLAIN, 11);
        public static final Font PLAIN_12 = new Font("system", Font.PLAIN, 12);
        public static final Font PLAIN_14 = new Font("system", Font.PLAIN, 14);
        public static final Font PLAIN_16 = new Font("system", Font.PLAIN, 16);
        public static final Font PLAIN_18 = new Font("system", Font.PLAIN, 18);
        public static final Font PLAIN_20 = new Font("system", Font.PLAIN, 20);
        public static final Font PLAIN_22 = new Font("system", Font.PLAIN, 22);
        public static final Font PLAIN_24 = new Font("system", Font.PLAIN, 24);
        public static final Font PLAIN_26 = new Font("system", Font.PLAIN, 26);

        public static final Font BLOD_10 = new Font("system", Font.BOLD, 10);
        public static final Font BLOD_11 = new Font("system", Font.BOLD, 11);
        public static final Font BLOD_12 = new Font("system", Font.BOLD, 12);
        public static final Font BLOD_14 = new Font("system", Font.BOLD, 14);
        public static final Font BLOD_16 = new Font("system", Font.BOLD, 16);
        public static final Font BLOD_18 = new Font("system", Font.BOLD, 18);
        public static final Font BLOD_20 = new Font("system", Font.BOLD, 20);
        public static final Font BLOD_22 = new Font("system", Font.BOLD, 22);
        public static final Font BLOD_24 = new Font("system", Font.BOLD, 24);
        public static final Font BLOD_26 = new Font("system", Font.BOLD, 26);

        public static final Font PLAIN_12_SONG = new Font("宋体", Font.PLAIN, 12);
        public static final Font PLAIN_14_SONG = new Font("宋体", Font.PLAIN, 14);
        public static final Font PLAIN_16_SONG = new Font("宋体", Font.PLAIN, 16);

        public static final Font BLOD_12_SONG = new Font("宋体", Font.BOLD, 12);
        public static final Font BLOD_16_SONG = new Font("宋体", Font.BOLD, 16);
        public static final Font BLOD_24_SONG = new Font("宋体", Font.BOLD, 24);
    }

    public static final class CursorConst {
        public static final Cursor DEFAULT = CursorUtil.getCursor("images/mouse/mouse.png");
        public static final Cursor LEFT_ROT = CursorUtil.getCursor("images/mouse/leftRot.png");
        public static final Cursor RIGHT_ROT = CursorUtil.getCursor("images/mouse/rightRot.png");
        public static final Cursor MOVE = CursorUtil.getCursor("images/mouse/move.png");
        public static final Cursor BIG = CursorUtil.getCursor("images/mouse/big.png");
        public static final Cursor SMALL = CursorUtil.getCursor("images/mouse/small.png");

        public static final Cursor UP = CursorUtil.getCursor("images/mouse/up.png");
        public static final Cursor DOWN = CursorUtil.getCursor("images/mouse/down.png");
        public static final Cursor LEFT = CursorUtil.getCursor("images/mouse/left.png");
        public static final Cursor RIGHT = CursorUtil.getCursor("images/mouse/right.png");
    }

    public static final class Size {
        private static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        public static Dimension getScreen() {
            return screen;
        }

        public static Dimension getScreen(float scale) {
            return new Dimension((int) (screen.width * scale), (int) (screen.height * scale));
        }

        public static void updateScreen() {
            screen = Toolkit.getDefaultToolkit().getScreenSize();
        }
    }
}