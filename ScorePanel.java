import java.awt.*;
import javax.swing.*;

public class ScorePanel extends JPanel{
	private static final Font LARGE_FONT=new Font("나눔고딕", Font.BOLD, 30);
	private static final Font MEDIUM_FONT=new Font("나눔고딕", Font.BOLD, 20);
	private static final Font SMALL_FONT=new Font("나눔고딕", Font.BOLD, 15);
	
	//GrowingWorm 인스턴스
	private GrowingWorm game;
	JLabel logo;
	ImageIcon hwIcon=new ImageIcon("Images/hw.png");
	ImageIcon title=new ImageIcon("Images/title.png");
	ImageIcon scoreTitle=new ImageIcon("Images/scoretitle.png");
	ImageIcon playTitle=new ImageIcon("Images/playtitle.png");
	
	public ScorePanel(GrowingWorm game) {
		this.game=game;
		setPreferredSize(new Dimension(300, BackgroundPanel.ROW_COUNT*BackgroundPanel.TILE_SIZE));
		Color color=new Color(255, 198, 215);
		setBackground(color);

		//logo=new JLabel(hwIcon);
		//add(logo);
	}
	
	private static final int SCORE_OFFSET=150;
	private static final int CONTROLS_OFFSET=320;
	private static final int MESSAGE_STRIDE = 30;
	private static final int SMALL_OFFSET = 30;
	private static final int LARGE_OFFSET = 50;
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		hwIcon.paintIcon(this, g, 100, 10);
		title.paintIcon(this, g, 5, 50);
		//폰트 색깔을 Black으로
		g.setColor(Color.BLACK);
		
		//ScorePanel에 게임 이름 표기
		g.setFont(LARGE_FONT);
		g.drawString("", getWidth()/2-g.getFontMetrics().stringWidth("꿈틀이의 성장기")/2, 100);
		
		//스코어 표시창에 카테고리 표시하기(점수창, 조작법)
		scoreTitle.paintIcon(this, g, 10, 115);
		playTitle.paintIcon(this, g, 10, 280);
		g.setFont(MEDIUM_FONT);
		g.drawString("", SMALL_OFFSET, SCORE_OFFSET);
		g.drawString("", SMALL_OFFSET, CONTROLS_OFFSET);
		
		//카테고리의 내용 표시하기
		g.setFont(SMALL_FONT);
		
		int drawY=SCORE_OFFSET;
		g.drawString("총 점수: "+game.getScore(), LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
		g.drawString("해낸 과제의 수: "+game.getHwEaten(), LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
		g.drawString("다음 과제의 점수: "+game.getNextHwScore(), LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
		
		drawY=CONTROLS_OFFSET;
		g.drawString("위로 가기: <W> or <↑> key", LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
		g.drawString("아래로 가기: <S> or <↓> key", LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
		g.drawString("왼쪽으로 가기: <A> or <←> key", LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
		g.drawString("오른쪽으로 가기: <D> or <→> key", LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
		g.drawString("게임 일시정지하기: <P> key", LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
	}
}
