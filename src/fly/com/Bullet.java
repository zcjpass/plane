package fly.com;

public class Bullet extends FlyingObject{
	
	private int speed = 3;//�ӵ��ƶ��ٶ�
	
	//��ʼ������
	public Bullet(int x, int y) {
		
		this.x = x;
		this.y = y;
		this.image = shootGame.bullet;
	}
	
	//�ƶ�
	@Override
	public void step() {
		y -= speed;
	}
	
	//Խ�紦��
	@Override
	public boolean outOfBounds() {
		return y < - height;
	}
	
}
