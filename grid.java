package blendefine;

public class grid {
	int x;
	int y;
	int upf;
	double posx;
	double posy;
	double ptx1,ptx2,ptx3,pty1,pty2,pty3;
//	blend b=null;
//	define d=null;
	String name;
	grid(int x,int y){
		double s3=Math.sqrt(3);
		this.x=x;
		this.y=y;
		upf=(x+y)%2;
		posx=150*x;
		posy=150*s3*y;
		ptx1=0;
		ptx2=150;
		ptx3=300;
		pty1=150*s3*upf;
		pty2=150*s3-pty1;
		pty2=pty1;
	}
	double[] getcenter(){
		if(upf==0)//倒三角
			return new double[]{posx+150,posy+260/3};
		else
			return new double[]{posx+150,posy+520/3};
	}
}
