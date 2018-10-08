package fly.com;

import java.util.Random;

//���˶���
public class Airplane extends FlyingObject implements Enemy{
	
	private int speed = 2;	//�ƶ��ٶ�
	
	//��ʼ������
	public Airplane() {
		
		this.image = shootGame.airplane;
		width = image.getWidth();
		height = image.getHeight();
		y  = -this.height;
		Random rand = new Random();
		x = rand.nextInt(shootGame.WIDTH - this.width);
	}
	
	//Խ�紦��
	@Override
	public boolean outOfBounds() {
		
		// TODO Auto-generated method stub
		return y > shootGame.HEIGHT;
	}
	
	//�ƶ�
	@Override
	public void step() {
		y += speed;
		// TODO Auto-generated method stub

	}
	
	//��ȡ����
	@Override
	public int getScore() {
		
		// TODO Auto-generated method stub
		return 5;
	}

}
