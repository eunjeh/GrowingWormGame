import java.awt.*;
import javax.swing.*;

public class ScorePanel extends JPanel{
	private static final Font LARGE_FONT=new Font("�������", Font.BOLD, 30);
	private static final Font MEDIUM_FONT=new Font("�������", Font.BOLD, 20);
	private static final Font SMALL_FONT=new Font("�������", Font.BOLD, 15);
	
	//GrowingWorm �ν��Ͻ�
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
		//��Ʈ ������ Black����
		g.setColor(Color.BLACK);
		
		//ScorePanel�� ���� �̸� ǥ��
		g.setFont(LARGE_FONT);
		g.drawString("", getWidth()/2-g.getFontMetrics().stringWidth("��Ʋ���� �����")/2, 100);
		
		//���ھ� ǥ��â�� ī�װ� ǥ���ϱ�(����â, ���۹�)
		scoreTitle.paintIcon(this, g, 10, 115);
		playTitle.paintIcon(this, g, 10, 280);
		g.setFont(MEDIUM_FONT);
		g.drawString("", SMALL_OFFSET, SCORE_OFFSET);
		g.drawString("", SMALL_OFFSET, CONTROLS_OFFSET);
		
		//ī�װ��� ���� ǥ���ϱ�
		g.setFont(SMALL_FONT);
		
		int drawY=SCORE_OFFSET;
		g.drawString("�� ����: "+game.getScore(), LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
		g.drawString("�س� ������ ��: "+game.getHwEaten(), LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
		g.drawString("���� ������ ����: "+game.getNextHwScore(), LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
		
		drawY=CONTROLS_OFFSET;
		g.drawString("���� ����: <W> or <��> key", LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
		g.drawString("�Ʒ��� ����: <S> or <��> key", LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
		g.drawString("�������� ����: <A> or <��> key", LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
		g.drawString("���������� ����: <D> or <��> key", LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
		g.drawString("���� �Ͻ������ϱ�: <P> key", LARGE_OFFSET, drawY+=MESSAGE_STRIDE);
	}
}
