package blendefine;

import javafx.scene.Node;

public class blend extends grid{
	Node img;
	int face;
	int nowpic;
	blend(int x, int y,Node img,String name) {
		super(x, y);
		this.img=img;
		this.name=name;
	}

}
