/*
 *
 */
package lines;

import graphics.RendererLite;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Graphics extends RendererLite {

  public Graphics(int displayWidth, int displayHeight, String title) throws LWJGLException {
    super(displayWidth, displayHeight, title);
  }

  public void Draw(Ball[][] board) {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    // отрисовка последних линий поля
    GL11.glColor3f(1f, 1f, 1f);
    GL11.glBegin(GL11.GL_LINE);
    GL11.glVertex2i(Lines.BOARD_X_OFFSET + Lines.BOARD_SIZE * Lines.BALL_SIZE * 2, Lines.BOARD_Y_OFFSET);
    GL11.glVertex2i(Lines.BOARD_X_OFFSET + Lines.BOARD_SIZE * Lines.BALL_SIZE * 2, Lines.BOARD_Y_OFFSET + Lines.BOARD_SIZE * Lines.BALL_SIZE * 2);
    GL11.glVertex2i(Lines.BOARD_X_OFFSET, Lines.BOARD_Y_OFFSET + Lines.BOARD_SIZE * Lines.BALL_SIZE * 2);
    GL11.glVertex2i(Lines.BOARD_X_OFFSET + Lines.BOARD_SIZE * Lines.BALL_SIZE * 2, Lines.BOARD_Y_OFFSET + Lines.BOARD_SIZE * Lines.BALL_SIZE * 2);
    GL11.glEnd();
//    System.out.println("");
    for (int x = 0; x < Lines.BOARD_SIZE; x++) {
      // отрисовка линий поля
      GL11.glColor3f(1f, 1f, 1f);
      GL11.glBegin(GL11.GL_LINE);
      GL11.glVertex2i(Lines.BOARD_X_OFFSET + x * Lines.BALL_SIZE * 2, Lines.BOARD_Y_OFFSET);
      GL11.glVertex2i(Lines.BOARD_X_OFFSET + x * Lines.BALL_SIZE * 2, Lines.BOARD_Y_OFFSET + Lines.BOARD_SIZE * Lines.BALL_SIZE * 2);
      GL11.glVertex2i(Lines.BOARD_X_OFFSET, Lines.BOARD_Y_OFFSET + x * Lines.BALL_SIZE * 2);
      GL11.glVertex2i(Lines.BOARD_X_OFFSET + Lines.BOARD_SIZE * Lines.BALL_SIZE * 2, Lines.BOARD_Y_OFFSET + x * Lines.BALL_SIZE * 2);
      GL11.glEnd();
      //отрисовка шариков
      for (int y = 0; y < Lines.BOARD_SIZE; y++) {
        if (board[x][y].color > 0) {
          board[x][y].Draw();
        }
      }
    }
    Display.update();
  }
}
