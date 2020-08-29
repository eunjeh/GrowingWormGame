import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.*;

public class GrowingWorm extends JFrame{
	//number of milliseconds that should pass between each frame
	private static final long FRAME_TIME=10000L/50L;
	
	//��Ʋ���� �ּ� length (This allows the worm to grow right when the game starts,
	//so that we are not just a head moving around the board.
	private static final int MIN_WORM_LENGTH=5;
	
	//direction list�� polling�� �� �ִ� �ִ� directions�� ��
	private static final int MAX_DIRECTIONS=3;
	
	BackgroundPanel background;
	ScorePanel sp;
	LevelPanel lv;
	private Random random;
	private Clock logicTimer;
	
	//Whether or not the game is (new game, over, paused)
	boolean isNewGame;
	boolean isGameOver;
	boolean isPaused;
	
	//��Ʋ���� Point�� ������ �ִ� ����Ʈ
	private LinkedList<Point> worm;
	
	//queued directions�� ������ �ִ� ����Ʈ
	private LinkedList<Direction> directions;
	
	private int score;
	private int hwEaten;
	//���� ������ award�� points
	private int nextHwScore;
	
	JDialog dialogResult;
	JLabel labelResultText;
	JLabel labelResultScore;
	JButton buttonResultOK;
	
	JButton lv1, lv2, lv3;
	JPanel levelPanel;
	
	//������_new window, set up the controller input
	public GrowingWorm() {
		super("��Ʋ���� �����");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(true);
		setFocusable(true);
		
		//lv1=new JButton("Level 1");
		//lv2=new JButton("Level 2");
		//lv3=new JButton("Level 3");
		
		//lv1.addActionListener(this);
		//lv2.addActionListener(this);
		//lv3.addActionListener(this);
		
		//lv1.setFocusable(false);
		//lv2.setFocusable(false);
		//lv3.setFocusable(false);
		
		//levelPanel=new JPanel();
		//levelPanel.add(lv1);
		//levelPanel.add(lv2);
		//levelPanel.add(lv3);
		//levelPanel.setFocusable(false);

		//panel �ʱ�ȭ, window�� panel ���̱�
		this.background=new BackgroundPanel(this);
		this.sp=new ScorePanel(this);
		this.lv=new LevelPanel(this);
		
		add(background, BorderLayout.CENTER);
		add(sp, BorderLayout.WEST);
		//add(levelPanel, BorderLayout.EAST);
		add(lv, BorderLayout.EAST);
		
			
		//input�� ó���ϱ� ���ؼ� �����ӿ� ���ο� key listener�� �߰�
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e2) {
				switch(e2.getKeyCode()) {
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					if(!isPaused && !isGameOver) {
						if(directions.size()<MAX_DIRECTIONS) {
							//directions list�� ������ ��Ҹ� ����, ���� list�� ��������� null�� ����
							Direction last=directions.peekLast();
							if(last!=Direction.South && last!=Direction.North) {
								directions.addLast(Direction.North);
							}
						}
					}
					break;
					
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
					if(!isPaused && !isGameOver) {
						if(directions.size()<MAX_DIRECTIONS) {
							Direction last=directions.peekLast();
							if(last!=Direction.North && last!=Direction.South) {
								directions.addLast(Direction.South);
							}
						}
					}
					break;
					
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					if(!isPaused && !isGameOver) {
						if(directions.size()<MAX_DIRECTIONS) {
							Direction last=directions.peekLast();
							if(last!=Direction.East && last!=Direction.West) {
								directions.addLast(Direction.West);
							}
						}
					}
					break;
					
				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					if(!isPaused && !isGameOver) {
						if(directions.size()<MAX_DIRECTIONS) {
							Direction last=directions.peekLast();
							if(last!=Direction.West && last!=Direction.East) {
								directions.addLast(Direction.East);
							}
						}
					}
					break;
					
				case KeyEvent.VK_P:
					if(!isGameOver) {
						isPaused=!isPaused;
						logicTimer.setPaused(isPaused);
					}
					break;
					
				//���� reset
				case KeyEvent.VK_ENTER:
					//new game�̰ų� game over �����̸� game reset
					if(isNewGame || isGameOver) {
						resetGame();
					}
					break;
				}
			}
		});

		//window ����� appropriate size�� resize, screen�� ���߾�����
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
		
	//���� ����
	public void startGame() {
		//���ӽ��ۿ� �ռ� ���� �͵� �ʱ�ȭ
		this.random=new Random();
		this.worm=new LinkedList<>();
		this.directions=new LinkedList<>();
		this.logicTimer=new Clock(9.0f);
		this.isNewGame=true;
		
		//ó���� timer�� �������� ���°� �Ǿ��ֵ��� setting�ϱ�
		logicTimer.setPaused(true);
		
		//game loop
		while(true) {
			//���� frame�� start time�� get
			long startTime=System.nanoTime();
			
			//logicTimer update
			logicTimer.update();
			
			//logicTimer�� cycle�� ����� ���, ���� update
			if(logicTimer.hasElapsedCycle())
				updateGame();
			
			//backgroundPanel�� ScorePanel�� ���ο� content�� repaint
			background.repaint();
			sp.repaint();
			lv.repaint();
			//levelPanel.repaint();
			
			//time ���
			//long delta=(System.nanoTime()-startTime)/1000000L;
			long delta=(System.nanoTime()-startTime)/10L;
			if(delta<FRAME_TIME) {
				try {
					Thread.sleep(FRAME_TIME-delta);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//���� logic update
	public void updateGame() {
		TileType collision=updateWorm();
		
		//������ collision�� �߻����� ��
		if(collision==TileType.Hw) {
			hwEaten++;
			score+=nextHwScore;
			spawnHw();			
		}
		//WormBody�� collision�� �߻����� ��
		else if(collision==TileType.WormBody) {
			isGameOver=true;
			logicTimer.setPaused(true);
		}
		//���� ������ award�� ������ 10���� ���� ��
		else if(nextHwScore>10) {
			nextHwScore--;
		}
	}
	
	//��Ʋ���� ��ġ, ũ�� update
	//��Ʋ���� �Ӹ����� ������ ���� tile Ÿ���� return
	public TileType updateWorm() {
		//direction list�� ���� ù ��Ҹ� ����
		Direction direction=directions.peekFirst();
		
		//update �� ��Ʋ���� �Ӹ����� ������ �� point�� ���
		Point head=new Point(worm.peekFirst());
		switch(direction) {
		case North:
			head.y--;
			break;
		
		case South:
			head.y++;
			break;
			
		case West:
			head.x--;
			break;
			
		case East:
			head.x++;
			break;
		}
		
		//��Ʋ���� �Ӹ��� �����ϴ� x��ǥ or y��ǥ�� ������ �Ѿ�� ���� �ε��� ���̱� ������ ��Ʋ�� Body Ÿ��Ÿ�� ����
		if(head.x<0 || head.x>=BackgroundPanel.COL_COUNT || head.y<0 || head.y>=BackgroundPanel.ROW_COUNT) {
			return TileType.WormBody; //��Ʋ���� body�� �浹�� ���̶�� ����
		}
		
		TileType old=background.getTile(head.x, head.y);
		if(old!=TileType.Hw && worm.size()>MIN_WORM_LENGTH) {
			//tail ����
			Point tail=worm.removeLast();
			//tail Point�� null�� set
			background.setTile(tail, null);
			//update ?
			old=background.getTile(head.x, head.y);
		}
		
		//��Ʋ�̰� �ڱ� ������ ������ ���� �ʾ��� ��� ��Ʋ���� ��ġ�� update ����
		//1. ������ �Ӹ��� �κ��� body tile�� set
		//2. ��Ʋ�̿��� ���ο� �Ӹ����� �߰�
		//3. ���ο� �Ӹ��ʿ� head tile�� set
		
		if(old!=TileType.WormBody) {
			background.setTile(worm.peekFirst(), TileType.WormBody);
			worm.push(head);
			background.setTile(head, TileType.WormHead);
			
			if(directions.size()>1) {
				directions.poll();
			}
		}
		
		//old�� head�� ��ġ�ϰ� �ִ� tileType
		return old;
	}
	
	//���ӿ� ���Ǵ� ������ reset, ���ο� ���� ����
	public void resetGame() {
		this.score=0;
		this.hwEaten=0;
		
		this.isNewGame=false;
		this.isGameOver=false;
		
		//head�� �����ϴµ�, ���� panel�� ���߾ӿ� ���� ����
		Point head=new Point(BackgroundPanel.COL_COUNT/2, BackgroundPanel.ROW_COUNT/2);
		
		worm.clear();
		worm.add(head);
		
		background.clearBg();
		background.setTile(head, TileType.WormHead);
		
		//default direction�� North
		directions.clear();
		directions.add(Direction.North);
		
		logicTimer.reset();
		
		spawnHw();		
	}
	
	public boolean isNewGame() {
		return isNewGame;
	}
	
	public boolean isGameOver() {
		return isGameOver;
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	private void spawnHw() {
		//100���� score reset
		this.nextHwScore=100;
		
		//random index�� �����Ͽ� �� ������ ã��(Hw�� spawn��)
		int index=random.nextInt(BackgroundPanel.COL_COUNT*BackgroundPanel.ROW_COUNT-worm.size());
		
		int freeFound=-1;
		for(int x=0; x<BackgroundPanel.COL_COUNT; x++) {
			for(int y=0; y<BackgroundPanel.ROW_COUNT; y++) {
				TileType type=background.getTile(x, y);
				if(type==null || type==TileType.Hw) {
					if(++freeFound == index) {
						background.setTile(x, y, TileType.Hw);
						break;
					}
				}
			}
		}
	}
	
	public int getScore() {
		return score;
	}
	
	public int getHwEaten() {
		return hwEaten;
	}
	
	public int getNextHwScore() {
		return nextHwScore;
	}
	
	public Direction getDirection() {
		return directions.peek();
	}
}