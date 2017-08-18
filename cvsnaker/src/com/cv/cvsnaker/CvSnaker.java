package com.cv.cvsnaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class CvSnaker extends JPanel
{

	/**
	 * 贪吃蛇
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 定义合适的规格
	 */
	private static final int CELL = 20;
	public  static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	/**
	 * 方向
	 * @author xiaowei
	 *
	 */
	enum Direction{
		上, 下, 左, 右, 停
	}
	/**
	 * 状态
	 * @author xiaowei
	 *
	 */
	enum State{
		开始, 暂停, 死亡, 打开
	}
	
	/*
	 * 变量
	 */
	private int speed;//速度
	private Point p;//蛇头
	private Direction dir;
	private State state;
	private LinkedList<Point> list;//蛇主体
	private boolean f;//食物是否存在，turn 存在，false 不存在
	private Point food;//食物
	private int score;//得分
	private int score_c;
	
	public CvSnaker(){
		p = new Point();
		score_c = 0;
		state = State.打开;
		f = false;
		food = new Point(CELL, CELL);
		list = new LinkedList<Point>();
		newSnaker();
	}
		
	public Direction getDir()
	{
		return dir;
	}


	public void setDir(Direction dir)
	{
		//防止反向运动
		this.dir = (dir==Direction.上)?((this.dir!=Direction.下)?dir:this.dir):this.dir;
		this.dir = (dir==Direction.下)?((this.dir!=Direction.上)?dir:this.dir):this.dir;
		this.dir = (dir==Direction.左)?((this.dir!=Direction.右)?dir:this.dir):this.dir;
		this.dir = (dir==Direction.右)?((this.dir!=Direction.左)?dir:this.dir):this.dir;
	}

	public State getState()
	{
		return state;
	}

	public void setState(State state)
	{
		this.state = state;
	}

	/**
	 * 初始化游戏
	 * 
	 * @author xiaowei
	 */
	public void newSnaker(){
		list = new LinkedList<Point>();
		list.add(new Point(120,60));
		list.add(new Point(100,60));
		list.add(new Point(80,60));
		list.add(new Point(60,60));
		list.add(new Point(40,60));
		dir = Direction.右;
		score = 0;
		speed = 3;
	}
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		if(state == State.开始){
			snake(g);
		}else if(state == State.暂停){
			pause(g);
		}else if(state == State.死亡){
			gameOver(g);
		}else if(state == State.打开){
			germ(g);
		}
		
	}
	/**
	 * 让蛇动起来
	 * 
	 * @author xiaowei
	 */
	public void action(){
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			
			@Override
			public void run()
			{
				if(state == State.开始){
					switch (dir)
					{
						case 上: shang();break;
						case 下: xia();break;
						case 左: zuo();break;
						case 右: you();break;
						default:
							break;
					}
					list.pollLast();
					eat();
					die();
				}
				try
				{
					Thread.sleep(1000/speed);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}, 1, 1);
		while(true){
			switch (score)
			{
				case 3: speed = 5;break;
				case 5: speed = 6;break;
				case 7: speed = 7;break;
				case 9: speed = 9;break;
				case 11: speed = 11;break;
				case 17: speed = 15;break;
				case 25: speed = 17;break;
				case 35: speed = 20;break;
				case 50: speed = 27;break;
				case 60: speed = 34;break;
				case 80: speed = 42;break;
				case 100: speed = 50;break;
			}
			repaint();
		} 
	}
	/**
	 * 控制蛇向上运动
	 * 
	 * @author xiaowei
	 */
	public void shang(){
		p = list.getFirst();
		list.addFirst(new Point((int)p.getX(), (int)p.getY()-CELL));
		dir = Direction.上;
	}
	/**
	 * 控制蛇向下运动
	 * 
	 * @author xiaowei
	 */
	public void xia(){
		p = list.getFirst();
		list.addFirst(new Point((int)p.getX(), (int)p.getY()+CELL));
		dir = Direction.下;
	}
	/**
	 * 控制蛇向左运动
	 * 
	 * @author xiaowei
	 */
	public void zuo(){
		p = list.getFirst();
		list.addFirst(new Point((int)p.getX()-CELL, (int)p.getY()));
		dir = Direction.左;
	}
	/**
	 * 控制蛇向右运动
	 * 
	 * @author xiaowei
	 */
	public void you(){
		p = list.getFirst();
		list.addFirst(new Point((int)p.getX()+CELL, (int)p.getY()));
		dir = Direction.右;
	}
	/**
	 * 画食物
	 * @param g 画笔参数
	 * @author xiaowei
	 */
	public void food(Graphics g){
		if(f == false){
			Random r = new Random(System.currentTimeMillis());
			while(true){
				int x = r.nextInt(800-20);
				int y = r.nextInt(600-20);
				for(ListIterator<Point> it = list.listIterator(); it.hasNext(); ){
					Point p = it.next();
					Point p1 = new Point(x, y);
					if(p1.equals(p))continue;
				}
				if(x%20 == 0 && y%20 == 0){
					food.x = x;
					food.y = y;
					break;
				}
			}
			f = true;
		}
		g.fillRect((int)food.getX(), (int)food.getY(), CELL, CELL);
		
	}
	/**
	 * 执行eat行为
	 * @return true 被吃了， false 没被吃
	 * @author xiaowei
	 */
	public boolean eat(){
		if(list.getFirst().equals(food)){
			list.add(p);
			score ++;
			f = false;
			return true;
		} 
		return false;
	}
	/**
	 * 判断死亡
	 * 
	 * @author xiaowei
	 */
	public void die(){
		Point p = list.getFirst();
		if(p.getX()<0 || p.getX()>WIDTH-20 || p.getY()<0 || p.getY()>HEIGHT-20){
			state = State.死亡;
			score_c = score;
			newSnaker();
		}
		
	}
	/**
	 * 画gameOver面板
	 * @param g
	 * @author xiaowei
	 */
	public void gameOver(Graphics g){
		g.setFont(new Font("微软雅黑", Font.BOLD, 80));
		g.drawString("GameOver", 170, 300);
		g.setFont(new Font("微软雅黑", Font.BOLD, 30));
		g.drawString("Enter->", 500, 350);
		g.setFont(new Font("微软雅黑", Font.BOLD, 30));
		g.drawString(String.valueOf(score_c), 350, 400);
		
	}
	/**
	 * 画蛇, 网格, 得分,  以及食物
	 * @param g
	 * @author xiaowei
	 */
	public void snake(Graphics g){
		//得分
		if(score<10){
			g.setFont(new Font("微软雅黑", Font.BOLD, 700));
			g.setColor(new Color(255, 255, 255));
			g.drawString(String.valueOf(score), 170, 550);
		}else if(score<100){
			g.setFont(new Font("微软雅黑", Font.BOLD, 700));
			g.setColor(new Color(255, 255, 255));
			g.drawString(String.valueOf(score), -40, 580);
		}else if(score<1000){
			g.setFont(new Font("微软雅黑", Font.BOLD, 450));
			g.setColor(new Color(255, 255, 255));
			g.drawString(String.valueOf(score), -20, 480);
		}
		//网格
		g.setColor(new Color(192, 192, 192));
		for(int i=CELL; i<WIDTH; i += CELL){
			g.drawLine(i, 0, i, HEIGHT);
		}
		for(int i=CELL; i<HEIGHT; i += CELL){
			g.drawLine(0, i, WIDTH, i);
		}
		//蛇
		for(ListIterator<Point> it = list.listIterator(); it.hasNext(); ){
			Point p = it.next();
			if(p.equals(list.getFirst())){
				g.setColor(new Color(255, 0, 0));
				g.fillRect((int)p.getX(), (int)p.getY(), CELL+1, CELL+1);
			}else{
				g.setColor(new Color(0, 0, 0));
				g.fillRect((int)p.getX(), (int)p.getY(), CELL, CELL);
				g.setColor(new Color(65, 65, 65));
				g.drawRect((int)p.getX(), (int)p.getY(), CELL, CELL);
			}
			
		}
		g.setColor(new Color(0, 0, 0));
		food(g);
	}
	/**
	 * 画暂停面板
	 * @param g
	 * @author xiaowei
	 */
	public void pause(Graphics g){
		g.setFont(new Font("微软雅黑", Font.BOLD, 80));
		g.drawString("暂停", 170, 300);
		g.setFont(new Font("微软雅黑", Font.BOLD, 30));
		g.drawString("Enter->", 500, 350);
	}
	/**
	 * 画初始面板
	 * @param g
	 * @author xiaowei
	 */
	public void germ(Graphics g){
		g.setFont(new Font("微软雅黑", Font.BOLD, 80));
		g.drawString("疯狂贪吃蛇", 170, 300);
		g.setFont(new Font("微软雅黑", Font.BOLD, 30));
		g.drawString("Enter->", 500, 350);
		g.setFont(new Font("楷体", Font.BOLD, 18));
		g.drawString("因为速度比较快，难度比较高，所以可以穿身体", 200, 450);
		g.drawString("by Cv", 500, 500);
	}
	
}
