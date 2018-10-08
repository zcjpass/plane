package fly.com;

import java.util.Random;

//特殊敌人，有奖励的敌人
public class Bee extends FlyingObject implements Award {
	
	private int xspeed = 1;//x坐标移动速度
	private int yspeed = 2;//y坐标移动速度
	private int awardType;//奖励类型
	
	//初始化数据
	public Bee() {
		
		this.image = shootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		y -= height;
		Random rand = new Random();
		x = rand.nextInt(shootGame.WIDTH - width);
		awardType = rand.nextInt(2);//初始化时给奖励
	}
	//奖励类型
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return awardType;
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
