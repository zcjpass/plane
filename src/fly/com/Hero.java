package fly.com;

import java.awt.image.BufferedImage;

//����Ӣ�ۻ���̳з�����
public class Hero extends FlyingObject {

	private BufferedImage[] images = {};	//Ӣ�ۻ�ͼƬ
	private int index = 0;		//Ӣ�ۻ�ͼƬ����
	
	private int doubleFire;	//˫������
	private int life;	//��
	
	//��ʼ��
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
	
	//��ȡ˫������
	public int isDoubleFire() {
		
		return doubleFire;
	}
	
	//����˫������
	public void setDoubleFire(int doubleFire) {
		
		this.doubleFire = doubleFire;
	}
	
	//���ӻ���
	public void addDoubleFire() {
		
		doubleFire = 40;
	}
	
	//��������
	public void addLife() {
		life++;
	}
	
	//��������
	public void subtractLife() {
		
		life --;
	}
	
	public int getLife() {
		
		return life;
	}
	
	//��ǰ�����ƶ�����Ծ���
	public void moveTo(int x, int y) {
		
		this.x = x - this.width / 2;
		this.y = y - this.height / 2;
	}
	
	//Խ�紦��
	@Override
	public boolean outOfBounds() {
		
		return false;
	}
	
	//�����ӵ�
	public Bullet[] shoot() {
		
		int xstep = width / 4;	//4��
		int ystep = 20;		//��
		if(doubleFire > 0) {	//˫������
			Bullet[] bullets = new Bullet[2];
			bullets[0] = new Bullet(x + xstep, y - ystep);//�ӵ���ɻ�λ��
			bullets[1] = new Bullet(x + 3 * xstep, y - ystep);
			return bullets;
		}else {	//��������
			Bullet[] bullets = new Bullet[1];
			bullets[0] = new Bullet(x + 2 * xstep, y - ystep);
			return bullets; 
 		}
	}
	
	//�ƶ�
	@Override
	public void step() {
		// TODO Auto-generated method stub
		if(images.length > 0) {
			image = images[index++ / 10 % images.length];	//�л�ͼƬ
		}
	}
	
	//��ײ
	public boolean hit(FlyingObject other) {
		
		int x1 = other.x - this.width / 2;	//x������С����
		int x2 = other.x + other.width + this.width / 2;	//x����������
		int y1 = other.y - this.height / 2;	//y������С����
		int y2 = other.y + other.height + this.height / 2;	//y����������
		
		int herox = this.x + this.width / 2;	//Ӣ�ۻ�x�������ĵ����
		int heroy = this.y + this.height / 2;	//Ӣ�ۻ�y�������ĵ����
		
		return herox > x1 && herox < x2 && heroy > y1 && heroy < y2;//����Χ��ײ����
	}
	
}
