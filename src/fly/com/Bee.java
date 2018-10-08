package fly.com;

import java.util.Random;

//������ˣ��н����ĵ���
public class Bee extends FlyingObject implements Award {
	
	private int xspeed = 1;//x�����ƶ��ٶ�
	private int yspeed = 2;//y�����ƶ��ٶ�
	private int awardType;//��������
	
	//��ʼ������
	public Bee() {
		
		this.image = shootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		y -= height;
		Random rand = new Random();
		x = rand.nextInt(shootGame.WIDTH - width);
		awardType = rand.nextInt(2);//��ʼ��ʱ������
	}
	//��������
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return awardType;
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
		// TODO Auto-generated method stub
		x += xspeed;
		y += yspeed;
		if(x > shootGame.WIDTH - width) {
			xspeed = -1;
		}
		if(x < 0) {
			xspeed = -1;
		}
	}

}
