package blendefine;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.image.ImageView;

public class define extends grid{
	Group g;
	int face;
	int nowpic;
	int iscf=0;
	double nowangle;
	public GemGroup parentGroup;
	define(int x, int y,Group g,String name) {
		super(x, y);
		this.g=g;
		this.name=name;
		parentGroup= new GemGroup(this);
//		parentGroup.calculateArithmeticMean();
	}
	define(int x, int y,Group g,int upf,int face,int iscf,int nowpic,double nowangle,String name) {
		super(x, y);
		this.g=g;
		this.name=name;
		this.face=face;
		this.iscf=iscf;
		this.nowangle=nowangle;
		this.nowpic=nowpic;
		this.upf=upf;
		parentGroup= new GemGroup(this);
//		parentGroup.calculateArithmeticMean();
	}
	 public define clone(define this) {
		 Group g1=new Group();
		 
		 g.getChildren().iterator().forEachRemaining(e->{
			 ImageView iv=new ImageView(((ImageView) e).getImage());
			 g1.getChildren().add(iv);
			 g1.setEffect(g.getEffect());
		 });
		 g1.setTranslateX(x*150-23);
	        if((x+y)%2==0) {
	        	g1.setTranslateY(y*260-85);
	        }
	        else {
	        	g1.setTranslateY(y*260);
	        }
	        if (upf==1)
	        	g1.setRotate(180);
		return new define(x,y,g1,upf,face,iscf,nowpic,nowangle,name);
	 }
	 public void connect(define other) {
		 	
		 	if (other == null) return;
//		 	else {
//		 		iscf=1;
//		 	}
	        if (this.parentGroup == null && other.parentGroup == null) {
	            // 创建新组
	            GemGroup newGroup = new GemGroup(this);
	            newGroup.addGem(other);
	        } else if (this.parentGroup != null && other.parentGroup == null) {
	            // 添加到现有组
	            this.parentGroup.addGem(other);
	        } else if (this.parentGroup == null && other.parentGroup != null) {
	            // 添加到对方组
	            other.parentGroup.addGem(this);
	        } else {
	            // 合并两个组
	            mergeGroups(this.parentGroup, other.parentGroup);
	        }
//	        parentGroup.calculateArithmeticMean();
	    }

	    private void mergeGroups(GemGroup g1, GemGroup g2) {
	        for (define gem : g2.gems) {
	            g1.addGem(gem);
	        }
//	        g1.updatePivot();
	    }
	void setrotate(int i){
		
	}
}

