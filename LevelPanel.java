import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

import java.awt.*;
import javax.swing.*;

public class LevelPanel extends JPanel implements ActionListener{
   private static final Font LARGE_FONT=new Font("³ª´®°íµñ", Font.BOLD, 30);
   private static final Font MEDIUM_FONT=new Font("³ª´®°íµñ", Font.BOLD, 20);
   private static final Font SMALL_FONT=new Font("³ª´®°íµñ", Font.BOLD, 15);
   
   private static final int SCORE_OFFSET=150;
   private static final int CONTROLS_OFFSET=320;
   private static final int MESSAGE_STRIDE = 30;
   private static final int SMALL_OFFSET = 30;
   private static final int LARGE_OFFSET = 50;
   
   //GrowingWorm ÀÎ½ºÅÏ½º
   private GrowingWorm game;
   JButton lv1, lv2, lv3;
   JPanel lvs;
   
   public LevelPanel(GrowingWorm game) {
      ImageIcon logo1 = new ImageIcon("Images/lev1.png");
      ImageIcon logo2 = new ImageIcon("Images/lev2.png");
      ImageIcon logo3 = new ImageIcon("Images/lev3.png");
      
      Color color=new Color(195, 185, 224);
      
      lvs = new JPanel(); lvs.setLayout(new BorderLayout(10,50));
      lv1=new JButton(logo1); lv1.setBackground(Color.white);
      lv2=new JButton(logo2); lv2.setBackground(Color.white);
      lv3=new JButton(logo3); lv3.setBackground(Color.white);
      
      lv1.addActionListener(this);
      lv2.addActionListener(this);
      lv3.addActionListener(this);
      
      lv1.setFocusable(false);
      lv2.setFocusable(false);
      lv3.setFocusable(false);
      
      lvs.add("North",lv1);
      lvs.add("Center",lv2);
      lvs.add("South",lv3);
      lvs.setBackground(color);
      add("Center",lvs);

      setFocusable(true);
      
      this.game=game;
      //setPreferredSize(new Dimension(300, BackgroundPanel.ROW_COUNT*BackgroundPanel.TILE_SIZE));
      setBackground(color);
   }
   
   public void actionPerformed(ActionEvent e) {
      if(e.getSource()==lv1) {
         //background.COL_COUNT=50;
         //background.ROW_COUNT=50;
         game.background.TILE_SIZE=22;
         game.resize(game.background.COL_COUNT*game.background.TILE_SIZE, game.background.ROW_COUNT*game.background.TILE_SIZE);
         game.background.setPreferredSize(new Dimension(game.background.COL_COUNT*game.background.TILE_SIZE, game.background.ROW_COUNT*game.background.TILE_SIZE));
         //System.out.println("°¡·Î: "+game.background.COL_COUNT*game.background.TILE_SIZE+" ¼¼·Î: "+game.background.ROW_COUNT*game.background.TILE_SIZE);
         game.pack();
         game.setLocationRelativeTo(null);
         setVisible(true);
      }
      else if(e.getSource()==lv2) {
         //background.COL_COUNT=30;
         //background.ROW_COUNT=30;
         game.background.TILE_SIZE=15;
         game.background.setPreferredSize(new Dimension(game.background.COL_COUNT*game.background.TILE_SIZE, game.background.ROW_COUNT*game.background.TILE_SIZE));
         game.background.resize(game.background.COL_COUNT*game.background.TILE_SIZE, game.background.ROW_COUNT*game.background.TILE_SIZE);
         //System.out.println("°¡·Î: "+game.background.COL_COUNT*game.background.TILE_SIZE+" ¼¼·Î: "+game.background.ROW_COUNT*game.background.TILE_SIZE);
         //background.validate();
         game.pack();
         game.setLocationRelativeTo(null);
         setVisible(true);
      }
      else if(e.getSource()==lv3) {
         //background.COL_COUNT=25;
         //background.ROW_COUNT=25;
         game.background.TILE_SIZE=10;
         game.background.resize(game.background.COL_COUNT*game.background.TILE_SIZE, game.background.ROW_COUNT*game.background.TILE_SIZE);
         game.background.setPreferredSize(new Dimension(game.background.COL_COUNT*game.background.TILE_SIZE, game.background.ROW_COUNT*game.background.TILE_SIZE));
         //System.out.println("°¡·Î: "+game.background.COL_COUNT*game.background.TILE_SIZE+" ¼¼·Î: "+game.background.ROW_COUNT*game.background.TILE_SIZE);
         //background.validate();
         game.pack();
         game.setLocationRelativeTo(null);
         setVisible(true);
      }
   }
}

