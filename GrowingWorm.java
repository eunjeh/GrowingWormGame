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
	
	//꿈틀이의 최소 length (This allows the worm to grow right when the game starts,
	//so that we are not just a head moving around the board.
	private static final int MIN_WORM_LENGTH=5;
	
	//direction list에 polling할 수 있는 최대 directions의 수
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
	
	//꿈틀이의 Point를 가지고 있는 리스트
	private LinkedList<Point> worm;
	
	//queued directions를 가지고 있는 리스트
	private LinkedList<Direction> directions;
	
	private int score;
	private int hwEaten;
	//다음 과제가 award할 points
	private int nextHwScore;
	
	JDialog dialogResult;
	JLabel labelResultText;
	JLabel labelResultScore;
	JButton buttonResultOK;
	
	JButton lv1, lv2, lv3;
	JPanel levelPanel;
	
	//생성자_new window, set up the controller input
	public GrowingWorm() {
		super("꿈틀이의 성장기");
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

		//panel 초기화, window에 panel 붙이기
		this.background=new BackgroundPanel(this);
		this.sp=new ScorePanel(this);
		this.lv=new LevelPanel(this);
		
		add(background, BorderLayout.CENTER);
		add(sp, BorderLayout.WEST);
		//add(levelPanel, BorderLayout.EAST);
		add(lv, BorderLayout.EAST);
		
			
		//input을 처리하기 위해서 프레임에 새로운 key listener를 추가
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e2) {
				switch(e2.getKeyCode()) {
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					if(!isPaused && !isGameOver) {
						if(directions.size()<MAX_DIRECTIONS) {
							//directions list의 마지막 요소를 리턴, 만약 list가 비어있으면 null을 리턴
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
					
				//게임 reset
				case KeyEvent.VK_ENTER:
					//new game이거나 game over 상태이면 game reset
					if(isNewGame || isGameOver) {
						resetGame();
					}
					break;
				}
			}
		});

		//window 사이즈를 appropriate size로 resize, screen의 정중앙으로
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
		
	//게임 시작
	public void startGame() {
		//게임시작에 앞서 사용될 것들 초기화
		this.random=new Random();
		this.worm=new LinkedList<>();
		this.directions=new LinkedList<>();
		this.logicTimer=new Clock(9.0f);
		this.isNewGame=true;
		
		//처음에 timer가 일지정지 상태가 되어있도록 setting하기
		logicTimer.setPaused(true);
		
		//game loop
		while(true) {
			//현재 frame의 start time을 get
			long startTime=System.nanoTime();
			
			//logicTimer update
			logicTimer.update();
			
			//logicTimer의 cycle이 경과한 경우, 게임 update
			if(logicTimer.hasElapsedCycle())
				updateGame();
			
			//backgroundPanel과 ScorePanel을 새로운 content로 repaint
			background.repaint();
			sp.repaint();
			lv.repaint();
			//levelPanel.repaint();
			
			//time 계산
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
	
	//게임 logic update
	public void updateGame() {
		TileType collision=updateWorm();
		
		//과제와 collision이 발생했을 때
		if(collision==TileType.Hw) {
			hwEaten++;
			score+=nextHwScore;
			spawnHw();			
		}
		//WormBody와 collision이 발생했을 때
		else if(collision==TileType.WormBody) {
			isGameOver=true;
			logicTimer.setPaused(true);
		}
		//다음 과제가 award할 점수가 10점이 넘을 때
		else if(nextHwScore>10) {
			nextHwScore--;
		}
	}
	
	//꿈틀이의 위치, 크기 update
	//꿈틀이의 머리쪽이 움직인 쪽의 tile 타입을 return
	public TileType updateWorm() {
		//direction list의 제일 첫 요소를 리턴
		Direction direction=directions.peekFirst();
		
		//update 후 꿈틀이의 머리쪽이 존재할 새 point를 계산
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
		
		//꿈틀이의 머리가 존재하는 x좌표 or y좌표가 범위를 넘어가면 벽에 부딪힌 것이기 때문에 꿈틀이 Body 타일타입 리턴
		if(head.x<0 || head.x>=BackgroundPanel.COL_COUNT || head.y<0 || head.y>=BackgroundPanel.ROW_COUNT) {
			return TileType.WormBody; //꿈틀이의 body와 충돌한 것이라고 가장
		}
		
		TileType old=background.getTile(head.x, head.y);
		if(old!=TileType.Hw && worm.size()>MIN_WORM_LENGTH) {
			//tail 삭제
			Point tail=worm.removeLast();
			//tail Point를 null로 set
			background.setTile(tail, null);
			//update ?
			old=background.getTile(head.x, head.y);
		}
		
		//꿈틀이가 자기 몸통의 꼬리와 닿지 않았을 경우 꿈틀이의 위치를 update 해줌
		//1. 기존의 머리쪽 부분을 body tile로 set
		//2. 꿈틀이에게 새로운 머리쪽을 추가
		//3. 새로운 머리쪽에 head tile을 set
		
		if(old!=TileType.WormBody) {
			background.setTile(worm.peekFirst(), TileType.WormBody);
			worm.push(head);
			background.setTile(head, TileType.WormHead);
			
			if(directions.size()>1) {
				directions.poll();
			}
		}
		
		//old는 head가 위치하고 있는 tileType
		return old;
	}
	
	//게임에 사용되는 변수들 reset, 새로운 게임 시작
	public void resetGame() {
		this.score=0;
		this.hwEaten=0;
		
		this.isNewGame=false;
		this.isGameOver=false;
		
		//head를 생성하는데, 바탕 panel의 정중앙에 오게 생성
		Point head=new Point(BackgroundPanel.COL_COUNT/2, BackgroundPanel.ROW_COUNT/2);
		
		worm.clear();
		worm.add(head);
		
		background.clearBg();
		background.setTile(head, TileType.WormHead);
		
		//default direction은 North
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
		//100으로 score reset
		this.nextHwScore=100;
		
		//random index를 추출하여 빈 공간을 찾기(Hw를 spawn할)
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