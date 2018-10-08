package fly.com;

import java.awt.image.BufferedImage;

//创建英雄机类继承飞行类
public class Hero extends FlyingObject {

	private BufferedImage[] images = {};	//英雄机图片
	private int index = 0;		//英雄机图片索引
	
	private int doubleFire;	//双倍火力
	private int life;	//命
	
	//初始化
	public Hero() {
		
		life = 3;	
		doubleFire = 0;
		images = new BufferedImage[]{shootGame.hero0, shootGame.hero1};
		image = shootGame.hero0;
		width = image.getWidth();
		height = image.getHeight();
		x = 150;
		y = 400;
	}
	
	//获取双倍火力
	public int isDoubleFire() {
		
		return doubleFire;
	}
	
	//设置双倍火力
	public void setDoubleFire(int doubleFire) {
		
		this.doubleFire = doubleFire;
	}
	
	//增加火力
	public void addDoubleFire() {
		
		doubleFire = 40;
	}
	
	//增加生命
	public void addLife() {
		life++;
	}
	
	//减少生命
	public void subtractLife() {
		
		life --;
	}
	
	public int getLife() {
		
		return life;
	}
	
	//当前物体移动后相对距离
	public void moveTo(int x, int y) {
		
		this.x = x - this.width / 2;
		this.y = y - this.height / 2;
	}
	
	//越界处理
	@Override
	public boolean outOfBounds() {
		
		return false;
	}
	
	//发射子弹
	public Bullet[] shoot() {
		
		int xstep = width / 4;	//4半
		int ystep = 20;		//步
		if(doubleFire > 0) {	//双倍火力
			Bullet[] bullets = new Bullet[2];
			bullets[0] = new Bullet(x + xstep, y - ystep);//子弹距飞机位置
			bullets[1] = new Bullet(x + 3 * xstep, y - ystep);
			return bullets;
		}else {	//单倍火力
			Bullet[] bullets = new Bullet[1];
			bullets[0] = new Bullet(x + 2 * xstep, y - ystep);
			return bullets; 
 		}
	}
	
	//移动
	@Override
	public void step() {
		// TODO Auto-generated method stub
		if(images.length > 0) {
			image = images[index++ / 10 % images.length];	//切换图片
		}
	}
	
	//碰撞
	public boolean hit(FlyingObject other) {
		
		int x1 = other.x - this.width / 2;	//x坐标最小距离
		int x2 = other.x + other.width + this.width / 2;	//x坐标最大距离
		int y1 = other.y - this.height / 2;	//y坐标最小距离
		int y2 = other.y + other.height + this.height / 2;	//y坐标最大距离
		
		int herox = this.x + this.width / 2;	//英雄机x坐标中心点距离
		int heroy = this.y + this.height / 2;	//英雄机y坐标中心点距离
		
		return herox > x1 && herox < x2 && heroy > y1 && heroy < y2;//区域范围内撞上了
	}
	
}
