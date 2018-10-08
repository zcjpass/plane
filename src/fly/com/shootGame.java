package fly.com;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Year;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class shootGame extends JPanel {

	//�����
	public static final int WIDTH = 400;
	public static final int HEIGHT = 650;
	
	//��Ϸ״̬
	private int state;
	private static final int START = 0;
	private static final int RUNNING = 1;
	private static final int PAUSE = 2;
	private static final int GAME_OVER = 3;
	
	private int score = 0;//�÷�
	private Timer timer;//��ʱ��
	private int intervel = 1000 / 100;//ʱ����
	
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	
	private FlyingObject[] flyings = {};//�л�����
	private Bullet[] bullets = {};//�ӵ�����
	private Hero hero = new Hero();//Ӣ�ۻ�
	
	//��̬����飬��ʼ��ͼƬ��Դ
	static {
		try {
			background = ImageIO.read(new File("E:\\java2\\plane\\src\\background.jpg"));//��ȡ����ͼ
			start = ImageIO.read(new File("E:\\java2\\plane\\src\\start.png"));//��ȡ��ʼͼƬ
			airplane = ImageIO.read(new File("E:\\java2\\plane\\src\\airplane.png"));//��ȡ�л�ͼƬ
			bee = ImageIO.read(new File("E:\\java2\\plane\\src\\bee.png"));//��ȡС�۷�ͼ
			bullet = ImageIO.read(new File("E:\\java2\\plane\\src\\bullet.png"));//��ȡ�ӵ�ͼ
			hero0 = ImageIO.read(new File("E:\\java2\\plane\\src\\hero0.png"));//��ȡӢ�ۻ�0ͼ
			hero1 = ImageIO.read(new File("E:\\java2\\plane\\src\\hero1.png"));//��ȡӢ�ۻ�1ͼ
			pause = ImageIO.read(new File("E:\\java2\\plane\\src\\pause.png"));//��ȡ��ͣͼ
			gameover = ImageIO.read(new File("E:\\java2\\plane\\src\\gameover.png"));//��ȡ����ͼ
		}catch(IOException e) {
			
			e.printStackTrace();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		
		g.drawImage(background, 0, 0, null);//����ͼ
		paintHero(g);//Ӣ�ۻ�
		paintBullets(g);//�ӵ�
		paintFlyingObjects(g);//������
		paintScore(g);//����
		paintState(g);//��Ϸ״̬
	}
	
	public void paintHero(Graphics g) {
		
		g.drawImage(hero.getImgae(), hero.getX(), hero.getY(), null);
	}
	
	public void paintBullets(Graphics g) {
		
		for(int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			g.drawImage(b.getImgae(), b.getX() - b.getWidth() / 2, b.getY(), null);
		}
	}
	
	public void paintFlyingObjects(Graphics g) {
		
		for(int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			g.drawImage(f.getImgae(), f.getX(), f.getY(), null);
		}
	}
	
	public void paintScore(Graphics g) {
		
		int x = 10;
		int y = 25;
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 14);
		g.setColor(new Color(0x3A3B3B));
		g.setFont(font);
		g.drawString("SCORE:" + score, x, y);//������
		y += 20;
		g.drawString("LIFE:" + hero.getLife(), x, y);//����
	}
	
	public void paintState(Graphics g) {
		switch(state) {
		case START://����״̬
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE://��ͣ״̬
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER://����״̬
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}
	
	//����
	private void action() {
		// TODO Auto-generated method stub
		
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {//������
				if(state == PAUSE) {//��ͣ״̬������
					state = RUNNING;
				}
				if(state == RUNNING) {
					int x = e.getX();//��ȡ�������
					int y = e.getY();
					hero.moveTo(x, y);//Ӣ�ۻ���������ƶ�
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {//����Ƴ�
				if(state != GAME_OVER && state != START) {//����Ϊ��ͣ
					state = PAUSE;
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {//�����
				switch(state) {
				case START:
					state = RUNNING;//����
					break;
				case GAME_OVER://��Ϸ����������
					flyings = new FlyingObject[0];//��շ�����
					bullets = new Bullet[0];//����ӵ�
					hero = new Hero();//���´���Ӣ�ۻ�
					score = 0;//��ճɼ�
					state = START;//��������
					break;
				}	
			}
		};
		this.addMouseListener(l);//����������¼�
		this.addMouseMotionListener(l);//������껬���¼�
		
		timer = new Timer();//�����̿��ƣ���ʱִ������
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(state == RUNNING) {
					enterAction();//�������볡
					stepAction();//�ƶ�һ��
					shootAction();//Ӣ�ۻ����
					bangAction();//�ӵ����������
					outOfBoundsAction();//ɾ��Խ���ӵ���������
					checkGameOverAction();//�����Ϸ����
				}
				repaint();//�ػ�
			}
			
		}, intervel, intervel);
	}
	
	int flyEnteredIndex = 0;
	public void enterAction() {
		
		flyEnteredIndex++;
		if(flyEnteredIndex % 40 == 0) {//400ms����һ��������
			FlyingObject obj = nextOne();//�������һ��������
			flyings = Arrays.copyOf(flyings, flyings.length + 1);
			flyings[flyings.length - 1] = obj;
		}
	}
	
	//�������һ��������
	private FlyingObject nextOne() {
		// TODO Auto-generated method stub
		Random random = new Random();
		int type = random.nextInt(20);
		if(type == 0) {
			return new Bee();
		}else {
			return new Airplane();
		}
	}

	public void stepAction() {
		
		hero.step();//Ӣ�ۻ���һ��
		for(int i = 0; i < flyings.length; i++) {//��������һ��
			FlyingObject f = flyings[i];
			f.step();
		}
		for(int i = 0; i < bullets.length; i++) {//�ӵ���һ��
			Bullet b = bullets[i];
			b.step();
		}
		hero.step();//Ӣ�ۻ���һ��
	}
	
	int shootIndex = 0;//�����
	
	public void shootAction() {
		
		shootIndex++;
		if(shootIndex % 30 == 0) {//300ms����һ��
			Bullet[] bs = hero.shoot();//Ӣ�ۻ�����ӵ�
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length);//����
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length);//׷������
		}
	}
	
	public void bangAction() {
		
		for(int i = 0; i < bullets.length; i++) {//���������ӵ�
			Bullet b = bullets[i];
			bang(b);//�ж��Ƿ�����ը
		}
	}
	
	private void bang(Bullet b) {
		// TODO Auto-generated method stub
		int index = -1;							//���еķ���������
		for(int i = 0; i < flyings.length; i++) {
			FlyingObject obj = flyings[i];
			if(obj.shootBy(b)) {//�ж��Ƿ����
				index = i; //��¼�����е�����
				break;
			}
		}
		if(index != -1) {//�л��еķ�����
			FlyingObject one = flyings[index];//��¼�����еķ�����
			FlyingObject temp = flyings[index];//�����еķ����������һ������
			flyings[index] = flyings[flyings.length - 1];
			flyings[flyings.length - 1] = temp;
			flyings = Arrays.copyOf(flyings, flyings.length - 1);//ɾ�����һ��������
			
			if(one instanceof Enemy) {//�ǵ��˼ӷ�
				Enemy e = (Enemy) one;
				score += e.getScore();
			}else if(one instanceof Award) {//�ǽ������ý���
				Award award = (Award) one;
				int type = award.getType();
				switch(type) {
				case Award.DOUBLE_FIRE:
					hero.addDoubleFire();
					break;
				case Award.LIFE:
					hero.addLife();
					break;
				}
			}
		}
	}

	//ɾ��Խ������Ｐ�ӵ�
	public void outOfBoundsAction() {
		
		int index = 0;
		FlyingObject[] flyings1 = new FlyingObject[flyings.length];
		for(int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if(!f.outOfBounds()) {
				flyings1[index++] = f;//��Խ�������
			}
		}
		flyings = Arrays.copyOf(flyings1, index);//��Խ��ķ����ﶼ����
		
		index = 0;//��������Ϊ0
		Bullet[] bullets1 = new Bullet[bullets.length];
		for(int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if(!b.outOfBounds()) {
				bullets1[index++] = b;
			}
		}
		bullets = Arrays.copyOf(bullets1, index);//����Խ����ӵ�������
	}
	
	//�����Ϸ����
	public void checkGameOverAction() {
		
		if(isGameOver()) {
			state = GAME_OVER;//�ı�״̬
		}
	}

	//�ж���Ϸ�Ƿ����
	private boolean isGameOver() {
		// TODO Auto-generated method stub
		for(int i = 0; i < flyings.length; i++) {
			int index = -1;
			FlyingObject obj = flyings[i];
			if(hero.hit(obj)) {//���Ӣ�ۻ���������Ƿ���ײ
				hero.subtractLife();//����
				hero.setDoubleFire(0);//˫���������
				index = i;//��¼���ϵķ���������
			}
			if(index != -1) {
				FlyingObject t = flyings[index];
				flyings[index] = flyings[flyings.length - 1];//���ϵ������һ�������ｻ��
				flyings[flyings.length - 1] = t;
				
				flyings = Arrays.copyOf(flyings, flyings.length - 1);//ɾ�����ϵķ�����
			}
		}
		return hero.getLife() <= 0;
	}
	
public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("Fly");
		shootGame game = new shootGame();//������
		frame.add(game);//�������ӵ�JFrame��
		frame.setSize(WIDTH, HEIGHT);//���ô�С
		frame.setAlwaysOnTop(true);//��������������
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Ĭ�Ͽ��ط�ʽ
		frame.setIconImage(new ImageIcon("images/icon.jpg").getImage());//���ô���
		frame.setLocationRelativeTo(null);//���ô����ʼλ��
		frame.setVisible(true);//���ӻ����������paint
		
		game.action();
	}
	
}
