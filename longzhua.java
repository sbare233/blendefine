package blendefine;

import javafx.animation.ParallelTransition;
import javafx.scene.Group;

public class longzhua extends grid{
	Group g;
	define d;
	ParallelTransition ani;
	long nowtime;
	int movef=0;
	double rotater=0;
	int grabx,graby,grabr,nowpic1=0,nowpic2=0;
	longzhua(int x, int y,Group g) {
		super(x, y);
		grabx=x;
		graby=y-2;
		grabr=(x+y)%2==0?0:3;
		this.g=g;
	}
	longzhua(int x, int y,int grabx,int graby,Group g) {
		super(x, y);
		this.grabx=grabx;
		this.graby=graby;
		this.g=g;
	}
	    public void grab(define d) {
	    	this.d=d;
	    	movef=1;
	    }
	    public void put() {
	    	this.d=null;
	    	movef=0;
	    }
}
