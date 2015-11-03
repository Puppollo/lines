package lines;

import java.util.Random;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Lines {

  public static int BOARD_SIZE = 10;
  public static int BOARD_X_OFFSET = 20; //смещение по х в окне игры
  public static int BOARD_Y_OFFSET = 20; //смещение по y в окне игры
  public static int COLOR_COUNT = 5;
  public static int BALL_SIZE = 20;
  public static int BALL_MIN_SIZE = 10;
  public static int ANIMATION_PAUSE = 10;

  private Ball[][] board; //игровое поле
  private int[][] wayBoard; // массив для определения пути
  private Ball Ball; // выбраный шарик
  private int score;
  private boolean mouse_click; // определяет было нажатие или нет
  private boolean mouse_press; // определяет была нажата кнопка или нет
  private int mouse_x, mouse_y; // координаты мышки на поле
  private int state; // определяет текущее состояние игры
  private int balls_count; // кол-во шариков на поле
  private long current_time;
  private int ball_animation_type;

  private Graphics graphics;
//================================================

  Lines() {
    // инициализация данных игры
    mouse_click = false;
    mouse_press = false;
    mouse_x = 0;
    mouse_y = 0;

    current_time = getTime();
    score = 0;
    balls_count = 0;
    Ball = null;
    state = 0; // 0 - place balls
    ball_animation_type = 0;

    board = new Ball[BOARD_SIZE][BOARD_SIZE];
    wayBoard = new int[BOARD_SIZE][BOARD_SIZE];
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        board[i][j] = new Ball();
        wayBoard[i][j] = 0;
        board[i][j].x = i;
        board[i][j].y = j;
        //board[i][j].dsize=new Random().nextInt(4)*-1;//DEBUG
      }
    }

		//board[0][0].color = 1;
    // инициализация openGL
    try {
      graphics = new Graphics(450, 450, "Lines");
    } catch (LWJGLException e) {
      e.printStackTrace();
      System.exit(0);
    }
  }
//================================================

  private int WayCheck(int x, int y, int dest_x, int dest_y) {
    for (int xi = 0; xi < BOARD_SIZE; xi++) {
      for (int yi = 0; yi < BOARD_SIZE; yi++) {
        if (board[xi][yi].color > 0) {
          wayBoard[xi][yi] = -1;
        } else {
          wayBoard[xi][yi] = 0;
        }
      }
    }

    int step = 1;
    wayBoard[dest_x][dest_y] = step;
    boolean end = false;
    while (!end) {
      for (int xi = 0; xi < BOARD_SIZE; xi++) {
        for (int yi = 0; yi < BOARD_SIZE; yi++) {
          if (wayBoard[xi][yi] == step) {
            if (xi - 1 >= 0 && wayBoard[xi - 1][yi] == 0) {
              wayBoard[xi - 1][yi] = step + 1;
            }
            if (xi + 1 < BOARD_SIZE && wayBoard[xi + 1][yi] == 0) {
              wayBoard[xi + 1][yi] = step + 1;
            }
            if (yi - 1 >= 0 && wayBoard[xi][yi - 1] == 0) {
              wayBoard[xi][yi - 1] = step + 1;
            }
            if (yi + 1 < BOARD_SIZE && wayBoard[xi][yi + 1] == 0) {
              wayBoard[xi][yi + 1] = step + 1;
            }
          }
        }
      }
      if (wayBoard[x][y] == step) {
        return 1;
      }
      step++;
      if (step >= BOARD_SIZE * BOARD_SIZE) {
        end = true;
      }
    }
    return 0;
  }
//================================================

  private void ClearBoard() {
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) {
        board[i][j].color = 0;
        board[i][j].clear = false;
        board[i][j].dsize = 0;
        board[i][j].x = i;
        board[i][j].y = j;
        wayBoard[i][j] = 0;
      }
    }
    score = 0;
    balls_count = 0;
  }
//================================================
// проверить доску на наличие составленных линий

  private int CheckLine(int x, int y) {
    int color = board[x][y].color;
    int count = 0; // кол-во шариков в линии
    int result = 0;
    int start_x = x, start_y = y, end_x = 0, end_y = 0;
    int xi, yi;

    // left-right direction check
    for (xi = x; xi >= 0; xi--)//left
    {
      if (board[xi][y].color == color) {
        count++;
        start_x = xi;
      } else {
        break;
      }
    }
    for (xi = x; xi < BOARD_SIZE; xi++)//right
    {
      if (board[xi][y].color == color) {
        count++;
        end_x = xi;
      } else {
        break;
      }
    }
    if (count > 5) {
      for (xi = start_x; xi <= end_x; xi++) {
        board[xi][y].color = 0;
        result = 1;
        balls_count--;
        score += count;
      } // change to clear=true
    }
    count = 0;
    // up-down direction check
    for (yi = y; yi >= 0; yi--)//up
    {
      if (board[x][yi].color == color) {
        count++;
        start_y = yi;
      } else {
        break;
      }
    }
    for (yi = y; yi < BOARD_SIZE; yi++)//down
    {
      if (board[x][yi].color == color) {
        count++;
        end_y = yi;
      } else {
        break;
      }
    }
    if (count > 5) {
      for (yi = start_y; yi <= end_y; yi++) {
        board[x][yi].color = 0;
        result = 1;
        balls_count--;
        score += count;
      } // change to .clear = true
    }
    count = 0;
    // diag1 direction check
    for (xi = x, yi = y; xi >= 0 && yi >= 0; xi--, yi--) {
      if (board[xi][yi].color == color) {
        count++;
        start_y = yi;
        start_x = xi;
      } else {
        break;
      }
    }
    for (xi = x, yi = y; xi < BOARD_SIZE && yi < BOARD_SIZE; xi++, yi++) {
      if (board[xi][yi].color == color) {
        count++;
        end_y = yi;
        end_x = xi;
      } else {
        break;
      }
    }
    if (count > 5) {
      for (xi = start_x, yi = start_y; xi <= end_x && yi <= end_y; yi++, xi++) {
        board[xi][yi].color = 0;
        result = 1;
        balls_count--;
        score += count;
      } // change to .clear = true
    }
    count = 0;
    // diag2 direction check
    for (xi = x, yi = y; xi >= 0 && yi < BOARD_SIZE; xi--, yi++) {
      if (board[xi][yi].color == color) {
        count++;
        start_y = yi;
        start_x = xi;
      } else {
        break;
      }
    }
    for (xi = x, yi = y; xi < BOARD_SIZE && yi >= 0; xi++, yi--) {
      if (board[xi][yi].color == color) {
        count++;
        end_y = yi;
        end_x = xi;
      } else {
        break;
      }
    }
    if (count > 5) {
      for (xi = start_x, yi = start_y; xi <= end_x && yi >= end_y; yi--, xi++) {
        board[xi][yi].color = 0;
        result = 1;
        balls_count--;
        score += count;
      } // change to .clear = true
    }
    return result;
  }
//================================================
// поставить новые шарики на поле

  private int PlaceBall() {
    // проверка на Game Over
    if ((balls_count + 3) >= (BOARD_SIZE * BOARD_SIZE)) {
      return 0;
    }
    int[][] xy = new int[2][3]; // хранить координаты поставленных шариков
    //поставить три шарика
    for (int i = 0; i < 3; i++) {
      int x = new Random().nextInt(BOARD_SIZE);
      int y = new Random().nextInt(BOARD_SIZE);
      if (board[x][y].color == 0)// свободное место
      {
        board[x][y].color = new Random().nextInt(COLOR_COUNT) + 1;
        balls_count++;
        //wayBoard[x][y]=-1;
      } else {
        // действия по определению нового места для шарика
        boolean end = false;
        while (!end) {
          x++;
          if (x >= BOARD_SIZE) {
            x = 0;
            y++;
            if (y >= BOARD_SIZE) {
              y = 0;
            }
          }
          if (board[x][y].color == 0) {
            board[x][y].color = new Random().nextInt(COLOR_COUNT) + 1;
            balls_count++;
            //wayBoard[x][y]=-1;
            end = true;
          }
        }
      }
      xy[0][i] = x; //сохранить координаты для проверки на линию
      xy[1][i] = y; //--
    }
    //... проверка на линию
    return 1;
  }
//================================================

  private long getTime() {
    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
  }
//================================================

  private void CheckMouse() {
    mouse_click = false;
    if (Mouse.isButtonDown(0)) {
      int x = Mouse.getX();
      int y = Mouse.getY();
      //Display.setTitle(x+" "+y+" inGame: "+mouse_x+" "+mouse_y);//DEBUG
      mouse_press = true;
    } else {
      if (mouse_press) {
        mouse_click = true;
        if (Mouse.getX() < BOARD_X_OFFSET || Mouse.getX() > BOARD_SIZE * BALL_SIZE * 2 + BOARD_X_OFFSET
                || Mouse.getY() < BOARD_Y_OFFSET || Mouse.getY() > BOARD_SIZE * BALL_SIZE * 2 + BOARD_Y_OFFSET) {
          mouse_click = false;
        }
        mouse_x = (Mouse.getX() - BOARD_X_OFFSET) / (BALL_SIZE * 2);
        mouse_y = (Mouse.getY() - BOARD_Y_OFFSET) / (BALL_SIZE * 2);
        if (mouse_x >= BOARD_SIZE) {
          mouse_x = BOARD_SIZE - 1;
        }
        if (mouse_y >= BOARD_SIZE) {
          mouse_y = BOARD_SIZE - 1;
        }
        mouse_press = false;
      }
    }
  }
//================================================

  public void Loop() // запуск цикла игры
  {
    while (!Display.isCloseRequested()) {
      switch (state) {
        case -1://game over
          ClearBoard();
          state++;
          break;
        case 0: // ... поставить шарики и проверить на линии
          if (PlaceBall() == 0) {
            state = -1;
            break;
          }
          //... CheckLines();
          state++;
          break;
        case 1: //ожидаем действий игрока
          if (Ball != null) // анимация выбора
          {
            if (Ball.selected) {
              if (getTime() - current_time > ANIMATION_PAUSE) {
                switch (ball_animation_type) {
                  case 0:
                    Ball.dsize--;
                    if (Ball.dsize <= BALL_MIN_SIZE * (-1)) {
                      ball_animation_type = 1;
                    }
                    break;
                  case 1:
                    Ball.dsize++;
                    if (Ball.dsize >= 0) {
                      ball_animation_type = 0;
                    }
                    break;
                }
                current_time = getTime();
              }
            }
          }
          if (mouse_click) {
            if (board[mouse_x][mouse_y].color > 0)//нажатие на шарик
            {
              if (Ball != null) {
                Ball.selected = false;
                Ball.dsize = 0;
              }
              Ball = board[mouse_x][mouse_y];
              Ball.selected = true;
            } else if (Ball != null && Ball.selected)// шарик выбран, нажатие на поле
            {
              Ball.selected = false;
              Ball.dsize = 0;

              if (WayCheck(mouse_x, mouse_y, Ball.x, Ball.y) == 1) {
                board[mouse_x][mouse_y].color = Ball.color;
                Ball.color = 0;
                if (CheckLine(mouse_x, mouse_y) == 0) {
                  state++;
                }
              }
              // и если путь свободен то state++
            }
          }
          break;
        case 2: //... анимация перемещения шарика
          state = 0;
          break;
      }

      CheckMouse();
      Display.setTitle(Integer.toString(score));
      graphics.Draw(board);
    }

    graphics.Release();
  }
//================================================

  public static void main(String[] args) {
    //...
    Lines lines = new Lines();
    lines.Loop();
  }
}
