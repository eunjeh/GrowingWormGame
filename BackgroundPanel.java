import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class BackgroundPanel extends JPanel{
	public static int COL_COUNT=40;
	public static int ROW_COUNT=40;
	public static int TILE_SIZE=10;
	
	private static final int EYE_LARGE_INSET=TILE_SIZE/3;
	private static final int EYE_SMALL_INSET=TILE_SIZE/6;
	private static final int EYE_LENGTH=TILE_SIZE/5;
	
	private static final Font FONT=new Font("나눔고딕", Font.BOLD, 28);
	
	private GrowingWorm game;
	
	ImageIcon title=new ImageIcon("Images/title.png");
	
	//tiles array
	private TileType[] tiles;
	
	public BackgroundPanel(GrowingWorm game) {
		this.game=game;
		this.tiles=new TileType[ROW_COUNT*COL_COUNT];
		
		setPreferredSize(new Dimension(COL_COUNT*TILE_SIZE, ROW_COUNT*TILE_SIZE));
		setBackground(Color.WHITE);
	}
	
	//배경판 모두 clear, 값들을 다 null로 저장
	public void clearBg() {
		for(int i=0; i<tiles.length; i++)
			tiles[i]=null;
	}
	
	public void setTile(Point point, TileType type) {
		setTile(point.x, point.y, type);
	}
	
	public void setTile(int x, int y, TileType type) {
		tiles[y*ROW_COUNT+x]=type;
	}
	
	public TileType getTile(int x, int y) {
		return tiles[y*ROW_COUNT+x];
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//루프를 돌면서 배경판의 각 타일들이 비어있지 않으면 draw
		for(int x=0; x<COL_COUNT; x++) {
			for(int y=0; y<ROW_COUNT; y++) {
				TileType type=getTile(x, y);
				
				if(type!=null) {
					drawTile(x*TILE_SIZE, y*TILE_SIZE, type, g);
				}
			}
		}
		
		//배경판에 격자 그리기
		g.setColor(Color.DARK_GRAY);
		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
		for(int x=0; x<COL_COUNT; x++) {
			for(int y=0; y<ROW_COUNT; y++) {
				g.drawLine(x*TILE_SIZE, 0, x*TILE_SIZE, getHeight());
				g.drawLine(0, y*TILE_SIZE, getWidth(), y*TILE_SIZE);
			}
		}
		
		//현재의 게임 상태(종료, 새 게임, 중단)를 알려주는 메세지 출력
		if(game.isGameOver()||game.isNewGame()||game.isPaused()) {
			g.setColor(Color.BLACK);
			
			int centerX=getWidth()/2;
			int centerY=getHeight()/2;
			
			String largeMessage=null;
			String smallMessage=null;
			
			if(game.isNewGame()) {
				//largeMessage="꿈틀이의 성장기";
				//smallMessage="시작하려면 <Enter>를 누르세요!";
				title=new ImageIcon("Images/startImage.png");
			}else if(game.isGameOver()) {
				//largeMessage="게임 오버!";
				//smallMessage="재시작하려면 <Enter>를 누르세요!";
				title=new ImageIcon("Images/endImage.png");
			}else if(game.isPaused()) {
				//largeMessage="게임 중단 상태";
				//smallMessage="재개하려면 <P>를 누르세요!";
				title=new ImageIcon("Images/stopImage.png");
			}
			
			//g.setFont(FONT);
			//g.drawString(largeMessage, centerX-g.getFontMetrics().stringWidth(largeMessage)/2, centerY-50);
			//g.drawString(smallMessage, centerX-g.getFontMetrics().stringWidth(smallMessage)/2, centerY+50);
			title.paintIcon(this, g, centerX-title.getIconWidth()/2, centerY-100);
		}
	}
	
	
	
	//배경판에 tile 그리기
	private void drawTile(int x, int y, TileType type, Graphics g) {
		switch(type) {
		case Hw:{
			Image img = Toolkit.getDefaultToolkit().getImage("Images/hw.png");
			g.drawImage(img, x, y, TILE_SIZE, TILE_SIZE, this);
			break;
		}
		case WormBody:{
			g.setColor(Color.PINK);
			g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
			break;
		}
		case WormHead:{
			g.setColor(Color.PINK);
			g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
			
			//꿈틀이의 눈 그리기
			g.setColor(Color.BLACK);
			
			switch(game.getDirection()) {
			case North: {
				int baseY = y + EYE_SMALL_INSET;
				g.drawLine(x + EYE_LARGE_INSET, baseY, x + EYE_LARGE_INSET, baseY + EYE_LENGTH);
				g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET, baseY, x + TILE_SIZE - EYE_LARGE_INSET, baseY + EYE_LENGTH);
				break;
			}
				
			case South: {
				int baseY = y + TILE_SIZE - EYE_SMALL_INSET;
				g.drawLine(x + EYE_LARGE_INSET, baseY, x + EYE_LARGE_INSET, baseY - EYE_LENGTH);
				g.drawLine(x + TILE_SIZE - EYE_LARGE_INSET, baseY, x + TILE_SIZE - EYE_LARGE_INSET, baseY - EYE_LENGTH);
				break;
			}
			
			case West: {
				int baseX = x + EYE_SMALL_INSET;
				g.drawLine(baseX, y + EYE_LARGE_INSET, baseX + EYE_LENGTH, y + EYE_LARGE_INSET);
				g.drawLine(baseX, y + TILE_SIZE - EYE_LARGE_INSET, baseX + EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET);
				break;
			}
				
			case East: {
				int baseX = x + TILE_SIZE - EYE_SMALL_INSET;
				g.drawLine(baseX, y + EYE_LARGE_INSET, baseX - EYE_LENGTH, y + EYE_LARGE_INSET);
				g.drawLine(baseX, y + TILE_SIZE - EYE_LARGE_INSET, baseX - EYE_LENGTH, y + TILE_SIZE - EYE_LARGE_INSET);
				break;
			}
			
			}
			break;
		}
			
		}
	}
}
