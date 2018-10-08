package fly.com;

import java.awt.image.BufferedImage;

//建立飞行类
public abstract class FlyingObject {
	
	protected int x;	//x坐标
	protected int y;	//y坐标
	protected int width;	//宽
	protected int height;	//高
	protected BufferedImage image;	//图片
	
	public int getX() {
		
		return x;
	}
	public void setX(int x) {
		
		this.x = x;
	}
	public int getY() {
		
		return y;
	}
	public void setY(int y) {
		
		this.y = y;
	}
	public int getWidth() {
		
		return width;
	}
	public void setWidth(int width) {
		
		this.width = width;
	}
	public int getHeight() {
		
		return height;
	}
	public void setHeight(int height) {
		
		this.height = height;
	}
	public BufferedImage getImgae() {
		
		return image;
	}
	public void setImgae(BufferedImage image) {
		
		this.image = image;
	}
	
	public abstract boolean outOfBounds();	//检查是否越界
	public abstract void step();	//飞行物移动
	
	//检查飞行物是否被子弹击中
	public boolean shootBy(Bullet bullet) {
		
		int x = bullet.x;	//子弹横坐标
		int y = bullet.y;	//子弹纵坐标
		return this.x < x && x < this.x + width && this.y < y && y < this.y + height; 
	}
}

