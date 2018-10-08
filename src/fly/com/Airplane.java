package fly.com;

import java.util.Random;

//敌人对象
public class Airplane extends FlyingObject implements Enemy{
	
	private int speed = 2;	//移动速度
	
	//初始化数据
	public Airplane() {
		
		this.image = shootGame.airplane;
		width = image.getWidth();
		height = image.getHeight();
		y  = -this.height;
		Random rand = new Random();
		x = rand.nextInt(shootGame.WIDTH - this.width);
	}
	
	//越界处理
	@Override
	public boolean outOfBounds() {
		
		// TODO Auto-generated method stub
		return y > shootGame.HEIGHT;
	}
	
	//移动
	@Override
	public void step() {
		y += speed;
		// TODO Auto-generated method stub

	}
	
	//获取分数
	@Override
	public int getScore() {
		
		// TODO Auto-generated method stub
		return 5;
	}

}
