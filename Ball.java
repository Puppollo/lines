package lines;

import org.lwjgl.opengl.GL11;

public class Ball 
{
	public int color;					// цвет шарика
	public boolean clear;			// определяет подлежит ли шарик удалению
	public int x, y;					// координаты шарика
	public int next_x, next_y;// координаты точки анимации
	public int dsize;					// переменная для изменения размера(анимация) шарика
	public boolean selected;	// определяет выбран ли шарик
	public Ball()
	{
		color = 0;
		clear = false;
		x=0;y=0;
		next_x=0;
		next_y=0;
		dsize=0;
		selected = false;
	}
	
	public void Draw()
	{
		GL11.glPushMatrix();
		int size=Lines.BALL_SIZE/2+dsize;
		switch(color)
		{
			case 1: GL11.glColor3f(1f,0,0); break; //red
			case 2: GL11.glColor3f(0,0,1f); break; //blue
			case 3: GL11.glColor3f(0,1f,0); break; //green
			case 4: GL11.glColor3f(1f,1f,0.7f); break; //yellow
			case 5: GL11.glColor3f(1f,0,1f); break; //purple
		}
		GL11.glTranslatef(x*Lines.BALL_SIZE*2+Lines.BOARD_X_OFFSET+Lines.BALL_SIZE, y*Lines.BALL_SIZE*2+Lines.BOARD_Y_OFFSET+Lines.BALL_SIZE, 0);
		GL11.glBegin(GL11.GL_QUADS);
		 GL11.glVertex2i(-size,-size);
		 GL11.glVertex2i(size,-size);
		 GL11.glVertex2i(size,size);
		 GL11.glVertex2i(-size,size);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
}
