package fly.com;

import java.awt.image.BufferedImage;

//����������
public abstract class FlyingObject {
	
	protected int x;	//x����
	protected int y;	//y����
	protected int width;	//��
	protected int height;	//��
	protected BufferedImage image;	//ͼƬ
	
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
	
	public abstract boolean outOfBounds();	//����Ƿ�Խ��
	public abstract void step();	//�������ƶ�
	
	//���������Ƿ��ӵ�����
	public boolean shootBy(Bullet bullet) {
		
		int x = bullet.x;	//�ӵ�������
		int y = bullet.y;	//�ӵ�������
		return this.x < x && x < this.x + width && this.y < y && y < this.y + height; 
	}
}

