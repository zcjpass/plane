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

	//面板宽高
	public static final int WIDTH = 400;
	public static final int HEIGHT = 650;
	
	//游戏状态
	private int state;
	private static final int START = 0;
	private static final int RUNNING = 1;
	private static final int PAUSE = 2;
	private static final int GAME_OVER = 3;
	
	private int score = 0;//得分
	private Timer timer;//定时器
	private int intervel = 1000 / 100;//时间间隔
	
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	
	private FlyingObject[] flyings = {};//敌机数组
	private Bullet[] bullets = {};//子弹数组
	private Hero hero = new Hero();//英雄机
	
	//静态代码块，初始化图片资源
	static {
		try {
			background = ImageIO.read(new File("E:\\java2\\plane\\src\\background.jpg"));//读取背景图
			start = ImageIO.read(new File("E:\\java2\\plane\\src\\start.png"));//读取开始图片
			airplane = ImageIO.read(new File("E:\\java2\\plane\\src\\airplane.png"));//读取敌机图片
			bee = ImageIO.read(new File("E:\\java2\\plane\\src\\bee.png"));//读取小蜜蜂图
			bullet = ImageIO.read(new File("E:\\java2\\plane\\src\\bullet.png"));//读取子弹图
			hero0 = ImageIO.read(new File("E:\\java2\\plane\\src\\hero0.png"));//读取英雄机0图
			hero1 = ImageIO.read(new File("E:\\java2\\plane\\src\\hero1.png"));//读取英雄机1图
			pause = ImageIO.read(new File("E:\\java2\\plane\\src\\pause.png"));//读取暂停图
			gameover = ImageIO.read(new File("E:\\java2\\plane\\src\\gameover.png"));//读取结束图
		}catch(IOException e) {
			
			e.printStackTrace();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		
		g.drawImage(background, 0, 0, null);//背景图
		paintHero(g);//英雄机
		paintBullets(g);//子弹
		paintFlyingObjects(g);//飞行物
		paintScore(g);//分数
		paintState(g);//游戏状态
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
		g.drawString("SCORE:" + score, x, y);//画分数
		y += 20;
		g.drawString("LIFE:" + hero.getLife(), x, y);//画命
	}
	
	public void paintState(Graphics g) {
		switch(state) {
		case START://启动状态
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE://暂停状态
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER://结束状态
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}
	
	//启动
	private void action() {
		// TODO Auto-generated method stub
		
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {//鼠标进入
				if(state == PAUSE) {//暂停状态下运行
					state = RUNNING;
				}
				if(state == RUNNING) {
					int x = e.getX();//获取鼠标坐标
					int y = e.getY();
					hero.moveTo(x, y);//英雄机随着鼠标移动
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {//鼠标推出
				if(state != GAME_OVER && state != START) {//设置为暂停
					state = PAUSE;
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {//鼠标点击
				switch(state) {
				case START:
					state = RUNNING;//启动
					break;
				case GAME_OVER://游戏结束，清理
					flyings = new FlyingObject[0];//清空飞行物
					bullets = new Bullet[0];//清空子弹
					hero = new Hero();//重新创建英雄机
					score = 0;//清空成绩
					state = START;//设置启动
					break;
				}	
			}
		};
		this.addMouseListener(l);//处理鼠标点击事件
		this.addMouseMotionListener(l);//处理鼠标滑动事件
		
		timer = new Timer();//主流程控制，定时执行任务
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(state == RUNNING) {
					enterAction();//飞行物入场
					stepAction();//移动一步
					shootAction();//英雄机射击
					bangAction();//子弹击打飞行物
					outOfBoundsAction();//删除越界子弹及飞行物
					checkGameOverAction();//检查游戏结束
				}
				repaint();//重绘
			}
			
		}, intervel, intervel);
	}
	
	int flyEnteredIndex = 0;
	public void enterAction() {
		
		flyEnteredIndex++;
		if(flyEnteredIndex % 40 == 0) {//400ms生成一个飞行物
			FlyingObject obj = nextOne();//随机生成一个飞行物
			flyings = Arrays.copyOf(flyings, flyings.length + 1);
			flyings[flyings.length - 1] = obj;
		}
	}
	
	//随机生成一个飞行物
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
		
		hero.step();//英雄机走一步
		for(int i = 0; i < flyings.length; i++) {//飞行物走一步
			FlyingObject f = flyings[i];
			f.step();
		}
		for(int i = 0; i < bullets.length; i++) {//子弹走一步
			Bullet b = bullets[i];
			b.step();
		}
		hero.step();//英雄机走一步
	}
	
	int shootIndex = 0;//射击数
	
	public void shootAction() {
		
		shootIndex++;
		if(shootIndex % 30 == 0) {//300ms发射一颗
			Bullet[] bs = hero.shoot();//英雄机打出子弹
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length);//扩容
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length);//追加数组
		}
	}
	
	public void bangAction() {
		
		for(int i = 0; i < bullets.length; i++) {//遍历所有子弹
			Bullet b = bullets[i];
			bang(b);//判断是否发生爆炸
		}
	}
	
	private void bang(Bullet b) {
		// TODO Auto-generated method stub
		int index = -1;							//击中的飞行物索引
		for(int i = 0; i < flyings.length; i++) {
			FlyingObject obj = flyings[i];
			if(obj.shootBy(b)) {//判断是否击中
				index = i; //记录被击中的索引
				break;
			}
		}
		if(index != -1) {//有击中的飞行物
			FlyingObject one = flyings[index];//记录被击中的飞行物
			FlyingObject temp = flyings[index];//被击中的飞行物与最后一个交换
			flyings[index] = flyings[flyings.length - 1];
			flyings[flyings.length - 1] = temp;
			flyings = Arrays.copyOf(flyings, flyings.length - 1);//删除最后一个飞行物
			
			if(one instanceof Enemy) {//是敌人加分
				Enemy e = (Enemy) one;
				score += e.getScore();
			}else if(one instanceof Award) {//是奖励设置奖励
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

	//删除越界飞行物及子弹
	public void outOfBoundsAction() {
		
		int index = 0;
		FlyingObject[] flyings1 = new FlyingObject[flyings.length];
		for(int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if(!f.outOfBounds()) {
				flyings1[index++] = f;//不越界的留着
			}
		}
		flyings = Arrays.copyOf(flyings1, index);//不越界的飞行物都留着
		
		index = 0;//索引重置为0
		Bullet[] bullets1 = new Bullet[bullets.length];
		for(int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if(!b.outOfBounds()) {
				bullets1[index++] = b;
			}
		}
		bullets = Arrays.copyOf(bullets1, index);//将不越界的子弹都留着
	}
	
	//检查游戏结束
	public void checkGameOverAction() {
		
		if(isGameOver()) {
			state = GAME_OVER;//改变状态
		}
	}

	//判断游戏是否结束
	private boolean isGameOver() {
		// TODO Auto-generated method stub
		for(int i = 0; i < flyings.length; i++) {
			int index = -1;
			FlyingObject obj = flyings[i];
			if(hero.hit(obj)) {//检查英雄机与飞行物是否碰撞
				hero.subtractLife();//减命
				hero.setDoubleFire(0);//双倍火力解除
				index = i;//记录碰上的飞行物索引
			}
			if(index != -1) {
				FlyingObject t = flyings[index];
				flyings[index] = flyings[flyings.length - 1];//碰上的与最后一个飞行物交换
				flyings[flyings.length - 1] = t;
				
				flyings = Arrays.copyOf(flyings, flyings.length - 1);//删除碰上的飞行物
			}
		}
		return hero.getLife() <= 0;
	}
	
public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("Fly");
		shootGame game = new shootGame();//面板对向
		frame.add(game);//将面板添加到JFrame中
		frame.setSize(WIDTH, HEIGHT);//设置大小
		frame.setAlwaysOnTop(true);//设置其总在最上
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//默认开关方式
		frame.setIconImage(new ImageIcon("images/icon.jpg").getImage());//设置窗体
		frame.setLocationRelativeTo(null);//设置窗体初始位置
		frame.setVisible(true);//可视化，尽快调用paint
		
		game.action();
	}
	
}
