package com.cv.cvsnaker;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.cv.cvsnaker.CvSnaker.Direction;
import com.cv.cvsnaker.CvSnaker.State;

public class CvMain
{
	private static CvSnaker snaker;
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("疯狂贪吃蛇");
		snaker = new CvSnaker();
		frame.add(snaker);
		frame.setSize(CvSnaker.WIDTH+5, CvSnaker.HEIGHT+25);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.addKeyListener(new KeyListener()
			{
				@Override
				public void keyTyped(KeyEvent e)
				{
				}
				@Override
				public void keyReleased(KeyEvent e)
				{
					if(e.getKeyCode() == KeyEvent.VK_ENTER){
						if(snaker.getState() == State.开始){
							snaker.setState(State.暂停);
						}else{
							snaker.setState(State.开始);
						}
					} 
				}
				@Override
				public void keyPressed(KeyEvent e)
				{
					if(e.getKeyCode() == KeyEvent.VK_UP) snaker.setDir(Direction.上);
					if(e.getKeyCode() == KeyEvent.VK_DOWN) snaker.setDir(Direction.下);
					if(e.getKeyCode() == KeyEvent.VK_LEFT) snaker.setDir(Direction.左);
					if(e.getKeyCode() == KeyEvent.VK_RIGHT) snaker.setDir(Direction.右);
				}
		});
		snaker.action();
	}
}
