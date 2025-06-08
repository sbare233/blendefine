package blendefine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.scene.effect.Blend;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.ImageInput;

public class blendefine extends Application {
	private static final int MAPWIDTH = 81; // 地图宽度
	private static final int MAPHEIGHT = 49; // 地图高度
	private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	Screen screen = Screen.getPrimary();
	double screenwidth = screen.getBounds().getWidth(), screenheight = screen.getBounds().getHeight();

	public Image rooti, zhengjini, blend1i,blend2i,taiyangi,jinwui, 
			blackup = new Image(getClass().getResourceAsStream("gridpic/blackup.png")),
			blackdown = new Image(getClass().getResourceAsStream("gridpic/blackdown.png")),
			greyup = new Image(getClass().getResourceAsStream("gridpic/greyup.png")),
			greydown = new Image(getClass().getResourceAsStream("gridpic/greydown.png")),
			whiteup = new Image(getClass().getResourceAsStream("gridpic/whiteup.png")),
			whitedown = new Image(getClass().getResourceAsStream("gridpic/whitedown.png")),
			huanyuiconi = new Image(getClass().getResourceAsStream("otherpic/huanyuicon.png")),
			hyzji = new Image(getClass().getResourceAsStream("otherpic/hunyuanzhengjin.png")),
			cailani = new Image(getClass().getResourceAsStream("otherpic/cailan1.png")),
			
			darken = new Image(getClass().getResourceAsStream("mousepic/darken.png")),
			yangyu = new Image(getClass().getResourceAsStream("mousepic/yangyu.png")),
			hunyuanzhengjini = new Image(getClass().getResourceAsStream("otherpic/hunyuanzhengjin.png")),
			crystalm = new Image(getClass().getResourceAsStream("mousepic/crystal.png")),
			metalm = new Image(getClass().getResourceAsStream("mousepic/metal.png")),
//    		shizhuani=new Image(getClass().getResourceAsStream("shizhuan.png"))
			shizhuani = new Image(getClass().getResourceAsStream("otherpic/shizhuan.png"), screenwidth, screenheight,false, false),
					longzhuax1i,longzhuax2i,longzhuani,
			baoshitopi = new Image(getClass().getResourceAsStream("definepic/宝石top.png")),
			shixubi = new Image(getClass().getResourceAsStream("shixupic/暂停a1.png"));
			
	Group guangbiaog = new Group();
	Circle mousetestc1;
	Polygon checkcoli,checkcoli0;
	
		public Image[] triiu = new Image[10], triid = new Image[10], zhengjins = new Image[10], wuzang = new Image[6],
			baoshibs = new Image[12], baoshims = new Image[12], zhuabis = new Image[12], zhuazis = new Image[12],
			zhuazigs = new Image[12],shixuyys = new Image[24],luoshus=new Image[10],shixuani = new Image[12],fuzhii = new Image[10];

		ImageView out = new ImageView(new Image(getClass().getResourceAsStream("mousepic/mouse2.png"))),
				in = new ImageView(new Image(getClass().getResourceAsStream("mousepic/mouse1.png"))), yy = new ImageView(yangyu),
						mouseiv = new ImageView(wi1), rootv = new ImageView(), alli = new ImageView(),
						zhengjin = new ImageView(hunyuanzhengjini), zhengjin1 = new ImageView(), lz1 = null,
						shixuyinyu;

	private static WritableImage CURSOR_BUFFER = new WritableImage(72, 66), wi1 = CURSOR_BUFFER, wi2;
	ImageView [] caozuoan=new ImageView[10];
	ImageView[][] gridv = new ImageView[49][81];
	shixuan[] shixuans = new shixuan[12],charans=new shixuan[12];
	HashMap<String,shixuan> shixuuses = new HashMap<String,shixuan>();
	HashMap<String,Image> charsi = new HashMap<String,Image>(),charsil = new HashMap<String,Image>();
	HashMap<String,Integer> animalindex = new HashMap<String,Integer>();
	HashMap<String,Color> rectcolor = new HashMap<String,Color>();
	HashMap<String, Long> offscreenTimestamps= new HashMap<String,Long>();
	HashMap<String,Circle> colics= new HashMap<String,Circle>();
	ArrayList<grid> machines = new ArrayList<grid>();
	private boolean leftmubanf = false, rightmubanf = false, canscroll = true,
			canmakecloneblend = true, canputf = false,pause=false,effectf=true,lowgrahf=false;

	private int frameCount = 0, mouserollcount = 0, mousetype = 1, isopenfinishf = 0, nowpage = 0, pixelsize = 4,
			hovergridx = -1, hovergridy = -1,shouldchangepicf=0,nowlookshixu,nowlookshixuy,nowplayshixupos,shixulength;
	private long lastTime = System.nanoTime(), pressStartTime;
	private final Text fpsText = new Text(10, 20, "FPS: 0");
	private double mouseX = 0, mouseY = 0, transX = 0, transY = 0, nowmousex = 0, nowmousey = 0,
			nowshixutransy,dragoff,dragoff1;
	private double suofang = screenheight / 1080, scale = 0.4 * suofang, mouserotate1 = 0, mouserotate2 = 0,
			mouserotate3 = 0, mouserollspeed1 = Math.random() * 3 + 0.3, mouserollspeed2 = Math.random() * 3 + 0.3,
			mouserollspeed3 = Math.random() * 3 + 0.3,nowpicvalue=-20*suofang;
	// 在类中声明动画对象
	private double scalemubanl = screenheight / 688 ;

	double maxoffsetx = 100 * scalemubanl - screenwidth / 2 * (1 - scale),
			maxoffsety = 25 * scalemubanl - screenheight / 2 * (1 - scale),
			minoffsetx = maxoffsetx - 200 * scalemubanl - 12300 * scale + screenwidth,
			minoffsety = maxoffsety - 250 * scalemubanl - 12740 * scale + screenheight;
	
	DropShadow ds1 = new DropShadow();
	DropShadow ds = new DropShadow();
	DropShadow ds0 = new DropShadow(30*suofang, 20*suofang, 10*suofang, Color.color(0, 0, 0, 0.9));
	DropShadow ds4 = new DropShadow();
	DropShadow ds3 = new DropShadow();
	DropShadow ds2 = new DropShadow(58*suofang, 3*suofang,0, Color.color(0, 0, 0, 0.9));
	
	private final Timeline scaleTimeline = new Timeline();

	public Pane gpane,blpane,lzpane,dfupane,hypane,dfpane,xzpane;
	public Pane rootlis;
	public Pane mubanleft;
	public Pane mubanright;
	public Pane shixu;
	public Pane openpane = new Pane();
	public StackPane root, game,ui;
	GridPane sxgp=new GridPane();
	public Rotate leftclock, leftanti, rightclock, rightanti, downclock, downanti;
	public Rotate[] rolls;
	public int[][] rolln = { { 0, -1 }, { -1, 0 }, { -1, 0 }, { 0, 1 }, { 0, 1 }, { 0, -1 } };
	public int scaleindex = 1;
	public double[] scales = { 0.19, 0.24, 0.3, 0.37, 0.5, 0.7, 1 };
	public String[] animalname = {"狗", "鹿", "鲶", "牛", "蛇", "狮", "虾", "鹰", "鱼" },
		shixuanname = { "擒获","围绕","逸释","安息","始恸","递归","纵横","蓄势","抄录"},
		shixuannamee = { "Quarry","Whirl","Emit","Abort","Start","Duplicate","Zero","Xtreme","Copy"},
		ncis={"Q","W","E","A","S","D","Z","X","C"};
	private double orgSceneX, orgSceneY,initialCenterX, initialCenterY,initialDistance;
	Blend blend = new Blend();
//	grid[][] grids = new grid[MAPHEIGHT][MAPWIDTH];
	List<Segment>[][] colis=new List[MAPHEIGHT][MAPWIDTH];//碰撞体数组，实时更新
	grid[][] db = new grid[MAPHEIGHT][MAPWIDTH],//存储上层机器和宝石
			db0 = new grid[MAPHEIGHT][MAPWIDTH];//存储玄武负和朱雀炉
	Node nowgrab;
	// 新增双缓冲相关变量
//	private WritableImage backBuffer;
	RotateTransition[] rttts=new RotateTransition[8];
	private Timeline charlight,opentimeline,process=new Timeline();//所有机器运作的时间轴process
    // 平滑运动系统参数
//    private final Timeline smoothTimeline = new Timeline(
//        new KeyFrame(Duration.millis(4), e -> updateSmoothPosition())
//    );
    private double targetX = 0, targetY = 0;
    private final double SMOOTH_FACTOR = 0.08;
    

    @SuppressWarnings({ "unchecked", "unused" })
	public void start(Stage primaryStage) {
    	primaryStage.setTitle("混元灵印");
    	blend.setMode(BlendMode.OVERLAY);
    	blend.setTopInput(new ImageInput(baoshitopi));
//    	System.setProperty("prism.order","d3d,sw");
    	for(int i=0;i<10;i++) {
    		if(lowgrahf) {
    			triiu[i]=new Image(getClass().getResourceAsStream("gridpic/gridu"+i+".png"),300*scale,260*scale,false,false);
    			triid[i]=new Image(getClass().getResourceAsStream("gridpic/gridd"+i+".png"),300*scale,260*scale,false,false);
    		}
    		else {
    			triiu[i]=new Image(getClass().getResourceAsStream("gridpic/gridu"+i+".png"));
        		triid[i]=new Image(getClass().getResourceAsStream("gridpic/gridd"+i+".png"));
    		}
    		int l=i+1;
    		luoshus[i]=new Image(getClass().getResourceAsStream("shixupic/洛书"+l+".png"),300*suofang,200*suofang, true, false);
    	}
    	for(int i=0;i<5;i++) {
    		zhengjins[i]=new Image(getClass().getResourceAsStream("otherpic/hunyuanzhengjiny"+i+".png"));
    	}
    	for(int i=0;i<26;i++) {
    		charsi.put(String.valueOf((char)(65+i)),new Image(getClass().getResourceAsStream("charpic/"+(char)(65+i)+".png")));
    		charsil.put(String.valueOf((char)(65+i)),new Image(getClass().getResourceAsStream("charpic/"+(char)(65+i)+"1.png")));
    	}
    	for(int i=0;i<6;i++) {
    		wuzang[i]=new Image(getClass().getResourceAsStream("blendpic/五脏"+i+".png"));
    	}
    	if(lowgrahf) {
    		blend1i = new Image(getClass().getResourceAsStream("blendpic/三足混元器.png"),300*scale,289*scale,false,false);
    		blend2i = new Image(getClass().getResourceAsStream("blendpic/六合周天混元阵.png"),1200*scale,1040*scale,false,false);
    		taiyangi = new Image(getClass().getResourceAsStream("blendpic/太阳.png"),259*scale,268*scale,false,false);
    		jinwui = new Image(getClass().getResourceAsStream("blendpic/金乌.png"),365*scale,365*scale,false,false);
    		longzhuax1i = new Image(getClass().getResourceAsStream("grabpic/a龙爪下盘.png"),173*scale,173*scale,false,false);
    		longzhuax2i = new Image(getClass().getResourceAsStream("grabpic/a龙爪下盘上.png"),166*scale,146*scale,false,false);
    		longzhuani = new Image(getClass().getResourceAsStream("grabpic/a龙爪钮.png"),112*scale,98*scale,false,false);
	    	for(int i=0;i<12;i++) {
	    		baoshibs[i]=new Image(getClass().getResourceAsStream("definepic/宝石a"+i+".png"),346*scale,346*scale,false,false);
	    		zhuabis[i]=new Image(getClass().getResourceAsStream("grabpic/a爪臂"+i+".png"),1200*scale,1200*scale,false,false);
	    		zhuazis[i]=new Image(getClass().getResourceAsStream("grabpic/a爪子"+i+".png"),346*scale,346*scale,false,false);
	    		zhuazigs[i]=new Image(getClass().getResourceAsStream("grabpic/a爪子g"+i+".png"),346*scale,346*scale,false,false);
	    	}
	    	for(int i=0;i<animalname.length;i++) {
	    		baoshims[i]=new Image(getClass().getResourceAsStream("definepic/宝石"+animalname[i]+".png"),346*scale,346*scale,false,false);
	    	}
    	}
    	else {
    		blend1i = new Image(getClass().getResourceAsStream("blendpic/三足混元器.png"));
    		blend2i = new Image(getClass().getResourceAsStream("blendpic/六合周天混元阵.png"));
    		taiyangi = new Image(getClass().getResourceAsStream("blendpic/太阳.png"));
    		jinwui = new Image(getClass().getResourceAsStream("blendpic/金乌.png"));
    		longzhuax1i = new Image(getClass().getResourceAsStream("grabpic/a龙爪下盘.png"));
    		longzhuax2i = new Image(getClass().getResourceAsStream("grabpic/a龙爪下盘上.png"));
    		longzhuani = new Image(getClass().getResourceAsStream("grabpic/a龙爪钮.png"));
    		for(int i=0;i<12;i++) {
	    		baoshibs[i]=new Image(getClass().getResourceAsStream("definepic/宝石a"+i+".png"));
	    		zhuabis[i]=new Image(getClass().getResourceAsStream("grabpic/a爪臂"+i+".png"));
	    		zhuazis[i]=new Image(getClass().getResourceAsStream("grabpic/a爪子"+i+".png"));
	    		zhuazigs[i]=new Image(getClass().getResourceAsStream("grabpic/a爪子g"+i+".png"));
	    	}
    		for(int i=0;i<animalname.length;i++) {
	    		baoshims[i]=new Image(getClass().getResourceAsStream("definepic/宝石"+animalname[i]+".png"));
	    	}
    	}
    	
    	for(int i=0;i<shixuyys.length;i++) {
    		shixuyys[i]=new Image(getClass().getResourceAsStream("shixupic/暂停ay"+i+".png"));
    	}
    	for(int i=0;i<9;i++) {
    		shixuani[i] = new Image(getClass().getResourceAsStream("shixupic/时序按钮"+i+".png"),70*suofang,45*suofang,true,true);
    	}
    	for(int i=0;i<8;i++) {
    		fuzhii[i] = new Image(getClass().getResourceAsStream("shixupic/符纸"+i+".png"),60*suofang,100*suofang,true,true);
    	}
    	for(int i=0;i<animalname.length;i++) {
    		animalindex.put(animalname[i], i);
    	}
    	rectcolor.put("青龙爪", Color.color(0.3, 1, 0.5,0.2));
    	 // 初始化双缓冲
//    	backBuffer = new WritableImage((int) screenwidth, (int) screenheight);
//    	rootv.setImage(backBuffer);
//    	smoothTimeline.setCycleCount(Animation.INDEFINITE);
//    	smoothTimeline.play();
    
    	StackPane stp=new StackPane();
    	stp.getChildren().add(game=new StackPane());
    	game.setPickOnBounds(false);
    	stp.setPrefSize(screenwidth,screenheight+50);
    	stp.setAlignment(Pos.TOP_LEFT);
        root = new StackPane();	
        root.setAlignment(Pos.TOP_LEFT);

        xzpane=new Pane();
        lzpane=new Pane();
        dfupane=new Pane();
        hypane=new Pane();
        blpane=new Pane();
        blpane.getChildren().add(hypane);
        blpane.getChildren().add(dfupane);
        blpane.getChildren().add(lzpane);
        dfpane=new Pane();
       
        root.setScaleX(scale);
        root.setScaleY(scale);
        root.setTranslateX(maxoffsetx);
        root.setTranslateY(maxoffsety);
        shizhuani =imgmake.process(shizhuani,11,2);
        ImageView shizhuan=new ImageView(shizhuani);
        shizhuan.setSmooth(false);
        shizhuan.setCache(true);
        game.getChildren().add(shizhuan);
//        shizhuan.setTranslateY(-game.getTranslateY());
        gpane=new Pane();
        root.getChildren().add(gpane);  
//        
//以上为操作层

        
        mousetestc1=new Circle(0,0,1);
		wi2=mousetestc1.snapshot(null, null);
		wi2.getPixelWriter().setColor(0, 0, Color.color(1,1,1,0.1));
		wi2.getPixelWriter().setColor(0, 1, Color.TRANSPARENT);
		wi2.getPixelWriter().setColor(1, 0, Color.TRANSPARENT);
		wi2.getPixelWriter().setColor(1,1, Color.TRANSPARENT);
		out.setScaleX(0.08*suofang);
		out.setScaleY(0.08*suofang);
		in.setScaleX(0.08*suofang);
		in.setScaleY(0.08*suofang);
		out.setTranslateX(-250);
		out.setTranslateY(-250);
		in.setTranslateX(-190);
		in.setTranslateY(-190);
		yy.setTranslateX(-149);
		yy.setTranslateY(-150);
		yy.setScaleX(0.08*suofang);
		yy.setScaleY(0.08*suofang);
	    SnapshotParameters sn1 = new SnapshotParameters();
		sn1.setFill(Color.TRANSPARENT);
		sn1.setViewport(new Rectangle2D(-40, -30,70,60));//防止方形截图旋转导致的斜向移动
		guangbiaog.getChildren().add(out);
		guangbiaog.getChildren().add(in);
		guangbiaog.getChildren().add(yy);
		guangbiaog.setScaleX(0.9);
		guangbiaog.setRotate(50);
        
		
        stp.setStyle("-fx-background:transparent;");
        Scene scene = new Scene(stp, Color.TRANSPARENT);
        
        
        // 计算密铺位置
    	   for (int i = 0; i < MAPHEIGHT; i++) {
               for (int j = 0; j < MAPWIDTH; j++) {
                   grid nowgrid=new grid(j,i);
                   ImageView triangleView=null;
                   int r=(int)(Math.sqrt(Math.random())*10);
                   if(nowgrid.upf==1) {      	
                   	triangleView = new ImageView(triid[r]);  	
                   }
                   else {
                   	triangleView = new ImageView(triiu[r]);  	
                   }
                   triangleView.setSmooth(false);
                   triangleView.setTranslateY(nowgrid.posy);
                   triangleView.setTranslateX(nowgrid.posx);
                   triangleView.setFitWidth(300);
                   triangleView.setFitHeight(260);
                   gridv[i][j]=triangleView;
                   gpane.getChildren().add(triangleView);
                   triangleView.setOnMouseEntered(e1->{
                   	hovergridx=nowgrid.x;
                   	hovergridy=nowgrid.y;
                   });
               }
           }
        game.setVisible(false);
       
        if(effectf) {
        	DropShadow is = new DropShadow();
            is.setOffsetX(50);
            is.setOffsetY(100);
            is.setSpread(0.2);
            is.setRadius(500);
            DropShadow is1 = new DropShadow(50, Color.color(0, 0, 0,0.8));
            is1.setOffsetX(15);
            is1.setOffsetY(30);
            is1.setSpread(0.2);
            DropShadow is2 = new DropShadow(20, Color.color(0, 0, 0,0.7));
            is2.setOffsetX(7);
            is2.setOffsetY(15);
//            is2.setSpread(0.2);
	        root.setEffect(is);
	        blpane.setEffect(is1);
	        lzpane.setEffect(is2);
	        dfpane.setEffect(is2);
        }
       
        game.getChildren().add(root);
        root.setPickOnBounds(false);
        // 动画定时器优化，使用多线程更新位置
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long currentTime = System.nanoTime();
                // 更新 FPS 计算
                frameCount++;
                if (currentTime - lastTime >= 1_000_000_000) {
                	fpsText.setText("FPS:" + frameCount+"坐标x"+nowmousex+"坐标y"+nowmousey+"格子x"+hovergridx+"格子y"+hovergridy+"缩放"+scale);
                    frameCount = 0;
                    lastTime = currentTime;
                }
            }
        };
        timer.start();
             
        
//以下为时序界面
        ui = new StackPane();
		shixu=new StackPane();
		shixu.setPickOnBounds(false);
		ui.setPickOnBounds(false);
		ui.getChildren().add(shixu);
		
//		ds0.setSpread(0.1);
		
			
		game.getChildren().add(ui);
		
		shixu.setTranslateY(screenheight/2-80*suofang);
        Group zantb=new Group();
		zantb.getChildren().add(new ImageView(shixubi));
		shixuyinyu=new ImageView(shixuyys[0]);
		shixuyinyu.setScaleX(-1);
		shixuyinyu.getTransforms().add(new Rotate(0,118.5,118.5));
		zantb.getChildren().add(shixuyinyu);
		
		shixuyinyu.setTranslateX(178);
		shixuyinyu.setTranslateY(145);
		zantb.setScaleX(suofang/2.5);
		zantb.setScaleY(suofang/2.5);
		zantb.setTranslateY(10*suofang);

		shixuyinyu.rotateProperty().addListener(e->{
			int np1=(int) Math.floor(((-shixuyinyu.getRotate()+630)%360)/15);
			ds3.setOffsetX(10*Math.sin(Math.toRadians(-shixuyinyu.getRotate()-40)));
			ds3.setOffsetY(10*Math.cos(Math.toRadians(-shixuyinyu.getRotate()-40)));
			if(shixuyinyu.getImage()!=shixuyys[np1]) {
				shixuyinyu.setImage(shixuyys[np1]);	
			}
		});
        Timeline sst = new Timeline(new KeyFrame(Duration.ZERO,new KeyValue(shixuyinyu.rotateProperty(),0)),
				new KeyFrame(Duration.seconds(2),new KeyValue(shixuyinyu.rotateProperty(),180)),
				new KeyFrame(Duration.seconds(4),new KeyValue(shixuyinyu.rotateProperty(),360)));
        
        zantb.setOnMouseClicked(e->{	
        	double currentPos = shixu.getTranslateY();
        	if(Math.abs(currentPos-nowshixutransy)<2*suofang) {
				if(!pause) {
//					try {
//						process.pause();
//					}catch(Exception e1) {}
	        		pause=true;
	        		sst.pause();
	        	}
	        	else {
	        		try {
	        			startup();
//	        			process.play();	
	        		}catch(Exception e1) {}
	        		pause=false;
	        		sst.play();
	        		
	        	}
        	}
        });
        
		TranslateTransition ssoutTransition = new TranslateTransition(Duration.millis(150), shixu);
		ssoutTransition.setToY(screenheight/2-270*suofang);
		ssoutTransition.setInterpolator(Interpolator.EASE_BOTH);

		TranslateTransition ssinTransition = new TranslateTransition(Duration.millis(150), shixu);
		ssinTransition.setToY(screenheight/2-80*suofang);
		ssinTransition.setInterpolator(Interpolator.EASE_BOTH);
		sst.setCycleCount(Timeline.INDEFINITE);
        sst.play();

        shixu.setOnMousePressed(e->{
        	nowshixutransy=shixu.getTranslateY();
        	pressStartTime = System.currentTimeMillis();
        	double psy=e.getScreenY()-screenheight/2;
        	dragoff=shixu.getTranslateY()-psy;
        	dragoff1=e.getScreenY();
        });
        shixu.setOnMouseDragged(e -> {
        	nowmousex = e.getScreenX();
			nowmousey = e.getScreenY() + 30;
				shixu.setTranslateY(Math.max(screenheight/2-270*suofang,e.getScreenY()-screenheight/2+dragoff));
//			e.consume();
		});

        shixu.setOnMouseReleased(e -> {
			double currentPos = shixu.getTranslateY();
			long duration = System.currentTimeMillis() - pressStartTime;
			if (Math.abs(currentPos-nowshixutransy)>2*suofang) {
//				if (currentPos < screenheight/2-150*suofang) { 
				if(e.getScreenY()-dragoff1<0) {
					ssinTransition.stop();
					ssoutTransition.setFromY(currentPos);
					ssoutTransition.play();
				} else {
					ssoutTransition.stop();
					ssinTransition.setFromY(currentPos);
					ssinTransition.play();

				}
			}
		});

        root.getChildren().add(xzpane);
		root.getChildren().add(dfpane);
		root.getChildren().add(blpane);
		xzpane.setPickOnBounds(false);
		dfpane.setPickOnBounds(false);
		blpane.setPickOnBounds(false);
		
		SVGPath huanyutitle = new SVGPath();
		ImageView huanyuicon = new ImageView(huanyuiconi);

		byte[] bytes;
		try {
			bytes = Files.readAllBytes(Paths.get("src/blendefine/huanyutitle.txt"));
			String content = new String(bytes, StandardCharsets.UTF_8);
			huanyutitle.setContent(content);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// 存储luoshus的ImageView以便后续操作
		Pane ls1=new StackPane();
		ls1.setPickOnBounds(false);
		List<ImageView> luoshusList = new ArrayList<>();
				for (int i = 9; i < 29; i++) {
				    ImageView iv = new ImageView(luoshus[i%10]);
				    iv.setTranslateX(iv.getBoundsInLocal().getWidth() * (i - 8.1) - screenwidth / 2);
				    iv.setTranslateY(180 * suofang);
				    ls1.getChildren().add(iv);
				    luoshusList.add(iv);
				}
				shixu.getChildren().add(ls1);
		Image juanzhou = new Image(getClass().getResourceAsStream("shixupic/juanzhou5.png"),60*scalemubanl,40*scalemubanl,true,true);
		Image juanzhoux1 = new Image(getClass().getResourceAsStream("shixupic/juanzhoux5.png"),60*scalemubanl,10*scalemubanl,true,true);
		Image shixuapi = new Image(getClass().getResourceAsStream("shixupic/时序按钮盘3.png"),600*suofang,200*suofang,true,true);
		
		

		//符纸
		double[] transx= {-375,-300,-225,-150,150,225,300,375},
				transy= {50,38,28,20,20,28,38,50};
		for(int i=0;i<8;i++) {
			ImageView fz0=new ImageView(fuzhii[i]);
			double tx=transx[i],ty=transy[i];
			caozuoan[i]=fz0;
			caozuoan[i].setTranslateX(tx*suofang);
			caozuoan[i].setTranslateY(ty*suofang);
			shixu.getChildren().add(caozuoan[i]);
			TranslateTransition outTransition = new TranslateTransition(Duration.millis(400), fz0);
			outTransition.setToY(0);
			outTransition.setInterpolator(Interpolator.EASE_BOTH);
			TranslateTransition inTransition = new TranslateTransition(Duration.millis(400), fz0);
			inTransition.setToY(ty*suofang);
			inTransition.setInterpolator(Interpolator.EASE_BOTH);
			
			RotateTransition rt=new RotateTransition(Duration.millis(700), fz0);
			rt.setToAngle(i<4?360:-360);
			rt.setInterpolator(Interpolator.EASE_IN);
			rttts[i]=rt;
			caozuoan[i].setOnMouseEntered(e->{
				inTransition.stop();
				outTransition.setFromY(fz0.getTranslateY());
				outTransition.play();
			});
			caozuoan[i].setOnMouseExited(e->{
				outTransition.stop();
				inTransition.setFromY(fz0.getTranslateY());
				inTransition.play();
			});
			caozuoan[i].setOnMouseClicked(e->{
				rt.play();
				rt.setOnFinished(e1->{fz0.setRotate(0);});
			});
		}
		
		//横条
		for(int i=0;i<4;i++) {
			Rectangle rect=new Rectangle(screenwidth,49*suofang);
			rect.setFill(Color.color((double)i/4,(double)i/4,(double)i/4,0.2));
			rect.setTranslateY((110+49*i)*suofang);
			shixu.getChildren().add(rect);
		}
		
		//竖条
		Pane zongsx=new StackPane();
		zongsx.setPickOnBounds(false);
		shixu.getChildren().add(zongsx);
		for(int i=0;i<screenwidth/140/suofang;i++) {
			Rectangle rect=new Rectangle(70*suofang,200*suofang);
			rect.setFill(Color.color(0,0,0,0.1));
			if(effectf)
				rect.setEffect(new GaussianBlur());
			rect.setTranslateX((440+140*i)*suofang- screenwidth / 2);
			rect.setTranslateY(182*suofang);
			zongsx.getChildren().add(rect);
		}
		
		//横杆
		for(int i=0;i<screenwidth/60/scalemubanl;i++) {
			for(int j=0;j<3;j++) {
				ImageView iv1=new ImageView(juanzhoux1);
				iv1.setTranslateX(iv1.getBoundsInLocal().getWidth()*(i+1)-screenwidth/2);
				iv1.setTranslateY((133+49*j)*suofang);
				shixu.getChildren().add(iv1);
			}		
		}
		
		//右侧时序放置
//		sxgp.setPickOnBounds(false);
		shixu.getChildren().add(sxgp);
		int cols= (int)(screenwidth/70/suofang);
		setFixedGridSize(sxgp, 4,cols, 70*suofang, 49*suofang);
		sxgp.setTranslateX(405*suofang);
		sxgp.setTranslateY(screenheight/2+108*suofang);
		
//		盘面
		ImageView iv1=new ImageView(shixuapi);
		iv1.setTranslateX(115*suofang-screenwidth/2);
		iv1.setTranslateY(182*suofang);
		shixu.getChildren().add(iv1);
		
		//字母
		for(int j=0;j<9;j++) {
			shixuan iv=new shixuan(charsi.get(ncis[j]));
			charans[j]=iv;
			iv.setTranslateX(197*suofang+(j%3)*96.5*suofang-screenwidth/2);
			iv.setTranslateY(110*suofang+(j/3)*47*suofang);
			iv.setScaleX(0.5*suofang);
			iv.setScaleY(0.5*suofang);
			shixu.getChildren().add(iv);
		}
		
		//云纹横条
		for(int i=0;i<screenwidth/60/scalemubanl;i++) {
			ImageView iv=new ImageView(juanzhou);
			iv.setTranslateX(iv.getBoundsInLocal().getWidth()*(i+1)-screenwidth/2);
			iv.setTranslateY(70*suofang);
			shixu.getChildren().add(iv);	
		}

		Pane ls2=new StackPane();
		ls2.setPickOnBounds(false);
		shixu.getChildren().add(ls2);

		//左侧符石
		for(int i=0;i<9;i++) {
			Image ni=shixuani[i];
			shixuan iv2=new shixuan(ni);
			iv2.i=i;
			iv2.t=shixuanname[i];
			shixuans[i]=iv2;
//			sxgp.add(iv2, i%3, i/3);
			iv2.setTranslateX(148*suofang+(i%3)*96.5*suofang-screenwidth/2);
			iv2.setTranslateY(108*suofang+(i/3)*49*suofang);
			ls2.getChildren().add(iv2);
			iv2.setOnMousePressed(e->{
				iv2.toFront();
			});
			iv2.setOnMouseDragged(e->{
			
				nowmousex= e.getScreenX();
				nowmousey= e.getScreenY()+30;
				((ImageView)e.getSource()).setTranslateX(e.getScreenX()-screenwidth/2);
				((ImageView)e.getSource()).setTranslateY(e.getScreenY()-screenheight/2-shixu.getTranslateY());
				e.consume();
			});
			iv2.setOnMouseReleased(e->{
				nowmousex= e.getScreenX();
				nowmousey= e.getScreenY()+30;
				int x=(int) ((nowmousex-405*suofang)/70/suofang);
				int y=(int) ((nowmousey-shixu.getTranslateY()-110*suofang-screenheight/2)/49/suofang);
				
				if(nowmousex>405*suofang&&nowmousey>shixu.getTranslateY()+110*suofang+screenheight/2) {
					shixuan iv12=new shixuan(ni);
					int nx=nowlookshixu+x,ny=nowlookshixuy+y;
					shixuuses.remove(nx+","+ny);
					shixuuses.put(nx+","+ny,iv12);
					iv12.x= nx;
					iv12.y= ny;
					iv12.t=iv2.t;
//					System.out.println(nx+"  "+ny);
					sxgp.add(iv12, x, y);
					refreshshixupane();
					iv12.setOnMouseClicked(e1->{
						shixuuses.remove(nx+","+ny,iv12);
						refreshshixupane();
					});
				}					
				int in=((shixuan)e.getSource()).i;
				iv2.setTranslateX(148*suofang+(in%3)*96.5*suofang-screenwidth/2);
				iv2.setTranslateY(108*suofang+(in/3)*49*suofang);
			});
		}

		shixu.getChildren().add(zantb);
		shixu.setPickOnBounds(false);
		// 拖动处理逻辑
		final double[] dragStart = new double[2];
		sxgp.setOnMousePressed(e -> {
		    dragStart[0] = e.getSceneX();
		    dragStart[1] = e.getSceneY();
		});
		sxgp.setOnScroll(e -> {
			double deltaY = e.getDeltaY();
			int changesxf = 0, changesyf = 0;
			if (Math.abs(deltaY) > 0) {
				dragStart[0] = e.getSceneX();
				dragStart[1] = e.getSceneY();
				if (deltaY > 0 && nowlookshixuy > 0) { // 向下拖动
					// 将最下面的矩形移到最上面
					nowlookshixuy--;
					changesyf--;
				} else if (deltaY < 0) { // 向上拖动
					changesxf = 1;
					nowlookshixuy++;
					changesyf++;	
				}
				grabshixuupdown();
			}
			if (nowlookshixu % 2 == 0) {
				zongsx.setTranslateX(0);
			} else {
				zongsx.setTranslateX(-70 * suofang);
			}
			if (changesxf != 0 || changesyf != 0) {
				refreshshixupane();
			}
		});
		sxgp.setOnMouseDragged(e->{
			e.consume();
			nowmousex=e.getScreenX();
			nowmousey=e.getScreenY()+30;

			 	double deltaX = e.getSceneX() - dragStart[0];
			    double deltaY = e.getSceneY() - dragStart[1];
			    int changesxf=0,changesyf=0;
			        if (deltaX < -70*suofang) { // 向右拖动
			        	shouldchangepicf--;
		            	nowpicvalue+=70*suofang;
		            	nowlookshixu++;
		            	changesxf++;
			 		   if (shouldchangepicf==-5) {
			 		    	shouldchangepicf=0;
			            	 ImageView l =luoshusList.remove(0);
			            	luoshusList.add(l);
			            	double w=l.getBoundsInLocal().getWidth();
			                double msx=0;
			            	for (Node node : ls1.getChildren()) {
			                    double tsx = node.getTranslateX();
			                    if(tsx>msx)msx=tsx;
			                }
			            	l.setTranslateX(msx+w);
			            	if(msx+w<9*w) {
			            		l =luoshusList.remove(0);
					            luoshusList.add(l);
					            l.setTranslateX(msx+w*2);
			            	}
			            } 
			 		   for(int i=0;i<20;i++) {
		            		luoshusList.get(i).setTranslateX(luoshusList.get(i).getTranslateX() -70*suofang);
		            	}
			 		  dragStart[0] = e.getSceneX();
			 		    dragStart[1] = e.getSceneY();
			        } else if (deltaX >70*suofang) { // 向左拖动
			        	if(nowpicvalue>-1) {
			        		shouldchangepicf++;
			        		nowpicvalue-=70*suofang;
			        		nowlookshixu--;
			        		changesxf--;
				 		    if (shouldchangepicf==5) {
				 		    	shouldchangepicf=0;
				 		    	ImageView l=luoshusList.remove(luoshusList.size()-1);
				 		    	luoshusList.add(0, l);
				            	double w=l.getBoundsInLocal().getWidth();
				            	double msx=0;
				            	for (Node node : ls1.getChildren()) {
				                    double tsx = node.getTranslateX();
				                    if(tsx<msx)msx=tsx;
				                }
				            	l.setTranslateX(msx-w);
				            	if(msx-w>-9*w) {
				            		l=luoshusList.remove(luoshusList.size()-1);
					 		    	luoshusList.add(0, l);
						            l.setTranslateX(msx-w*2);
				            	}
				 		    }
				 		    for(int i=0;i<20;i++) {
			            		luoshusList.get(i).setTranslateX(luoshusList.get(i).getTranslateX()+70*suofang);
			            	}
			        	}
				 		dragStart[0] = e.getSceneX();
				 		dragStart[1] = e.getSceneY();
			        }
			        if (Math.abs(deltaY) > 40*suofang) {
			        	dragStart[0] = e.getSceneX();
			 		    dragStart[1] = e.getSceneY();
			            if (deltaY > 40*suofang&&nowlookshixuy>0) { // 向下拖动
			                // 将最下面的矩形移到最上面
			            	nowlookshixuy--;
			            	changesyf--;
			            } else if (deltaY < -40*suofang) { // 向上拖动
			            	changesxf=1;
			            	nowlookshixuy++;
			            	changesyf++;
			            }
			            grabshixuupdown();
			            // 更新Y坐标
			        }
			        if(nowlookshixu%2==0) {
			        	  zongsx.setTranslateX(0);
			        }
			        else {
			        	zongsx.setTranslateX(-70*suofang);
			        }
			      if(changesxf!=0||changesyf!=0) {
			    	  refreshshixupane(); 
			      }
		});
		
		
		Image mubani1 = new Image(getClass().getResourceAsStream("otherpic/mubanl1.png"), 278 * scalemubanl,
				688 * scalemubanl, true, true),
				mubani2 = new Image(getClass().getResourceAsStream("otherpic/mubanr1.png"), 360 * scalemubanl,
						688 * scalemubanl, true, true);
		ImageView mubanl1 = new ImageView(mubani1);
		mubanl1.setSmooth(false);
		mubanleft=new Pane();
		mubanleft.getChildren().add(mubanl1);
		mubanleft.setCache(true);
		mubanleft.setTranslateY(30);
		ds1.setOffsetY(13);
		ds1.setOffsetX(8);
		ds1.setColor(Color.color(0.02, 0.02, 0.02, 0.9));
		mubanleft.getChildren().add(zhengjin);
		closebook();
		ui.getChildren().add(mubanleft);
		mubanleft.setTranslateX(-200 * scalemubanl);
		TranslateTransition outTransition = new TranslateTransition(Duration.millis(400), mubanleft);
		outTransition.setToX(-40 * scalemubanl);
		outTransition.setInterpolator(Interpolator.EASE_BOTH);
		TranslateTransition inTransition = new TranslateTransition(Duration.millis(400), mubanleft);
		inTransition.setToX(-200 * scalemubanl);
		inTransition.setInterpolator(Interpolator.EASE_BOTH);
		mubanl1.setOnMousePressed(e -> {
			outTransition.stop();
			inTransition.stop();
			pressStartTime = System.currentTimeMillis();
			dragoff=mubanleft.getTranslateX()-e.getScreenX();
			dragoff1=e.getScreenX();
		});
		mubanl1.setOnMouseEntered(e -> {
			hovergridx = -1;
			hovergridy = -1;
			e.consume();
		});
		// 添加拖拽支持
		mubanl1.setOnMouseDragged(e -> {
			mubanleft.setTranslateX(Math.min(-40 * scalemubanl, e.getSceneX()+dragoff));
			nowmousex = e.getScreenX();
			nowmousey = e.getScreenY() + 25;
			if (!mubanl1.getBoundsInParent().contains(e.getX(), e.getY())) {
				pressStartTime = 0;
			}
			e.consume();
		});

		mubanl1.setOnMouseReleased(e -> {
			double currentPos = mubanleft.getTranslateX();
			long duration = System.currentTimeMillis() - pressStartTime;

			if (duration > 100) {
//				if (currentPos > -120 * screenheight / 688) { // 阈值判断
				if (e.getScreenX()-dragoff1>0) {
					inTransition.stop();
					outTransition.setFromX(currentPos);
					outTransition.play();
					leftmubanf = true;
				} else {
					outTransition.stop();
					inTransition.setFromX(currentPos);
					inTransition.play();
					leftmubanf = false;
				}
			}
			else {
				if (leftmubanf) {
					outTransition.stop();
					inTransition.setFromX(mubanleft.getTranslateX());
					inTransition.play();

				} else {
					inTransition.stop();
					outTransition.setFromX(mubanleft.getTranslateX());
					outTransition.play();
				}
				leftmubanf = !leftmubanf;
			}
		});

		ImageView mubanl2 = new ImageView(mubani2);
		mubanl2.setSmooth(false);
		mubanright = new Pane();
		mubanright.setTranslateY(30);
		
		mubanright.getChildren().add(mubanl2);
		mubanright.setTranslateX(screenwidth - 90 * scalemubanl);
		ui.getChildren().add(mubanright);
//		mubanright.setCache(true);
		TranslateTransition inTransition1 = new TranslateTransition(Duration.millis(400), mubanright);
		inTransition1.setToX(screenwidth - 90 * scalemubanl);
		inTransition1.setInterpolator(Interpolator.EASE_BOTH);
		TranslateTransition outTransition1 = new TranslateTransition(Duration.millis(400), mubanright);
		outTransition1.setToX(screenwidth - 300 * scalemubanl);
		outTransition1.setInterpolator(Interpolator.EASE_BOTH);
		mubanl2.setOnMouseEntered(e -> {
			hovergridx = -1;
			hovergridy = -1;
			e.consume();
		});
		mubanl2.setOnMousePressed(e -> {
			outTransition1.stop();
			inTransition1.stop();
			pressStartTime = System.currentTimeMillis();
			dragoff=mubanright.getTranslateX()-e.getScreenX();
			dragoff1=e.getScreenX();
		});
		mubanl2.setOnMouseDragged(e -> {
			nowmousex = e.getScreenX();
			nowmousey = e.getScreenY() + 25;
			mubanright.setTranslateX(Math.max(screenwidth - 300 * scalemubanl,nowmousex +dragoff));
			if (!mubanl2.getBoundsInParent().contains(e.getX(), e.getY())) {
				pressStartTime = 0;
			}
			e.consume();
		});
		mubanl2.setOnMouseReleased(e -> {
			double currentPos = mubanright.getTranslateX();
			long duration = System.currentTimeMillis() - pressStartTime;
			if (duration > 100) {
//				if (currentPos < screenwidth - 180 * scalemubanl) { // 阈值判断
				if (e.getScreenX()-dragoff1<0) {
					inTransition1.stop();
					outTransition1.setFromX(currentPos);
					outTransition1.play();
					rightmubanf = true;
				} else {
					outTransition1.stop();
					inTransition1.setFromX(currentPos);
					inTransition1.play();
					rightmubanf = false;
				}
			} else {
				if (rightmubanf) {
					outTransition1.stop();
					inTransition1.setFromX(mubanright.getTranslateX());
					inTransition1.play();
				} else {
					inTransition1.stop();
					outTransition1.setFromX(mubanright.getTranslateX());
					outTransition1.play();
				}
				rightmubanf = !rightmubanf;
			}
		});
		mubanleft.setPickOnBounds(false);
		mubanright.setPickOnBounds(false);
        
        openpane.setMouseTransparent(true);   
        huanyuicon.setScaleX(screenheight/4000);
        huanyuicon.setScaleY(screenheight/4000);
        huanyuicon.setTranslateX(screenwidth/2-650*suofang-1837/2*(1-screenheight/4000));
        huanyuicon.setTranslateY(screenheight/2-250*suofang-1861/2*(1-screenheight/4000));
        openpane.getChildren().add(huanyuicon);
        huanyutitle.setScaleX(screenheight/200);
        huanyutitle.setScaleY(screenheight/200);
        huanyutitle.setTranslateX(screenwidth/2-555*suofang+5*screenheight/200);
        huanyutitle.setTranslateY(screenheight/2+215*suofang+10*screenheight/200);
		openpane.getChildren().add(huanyutitle);
		openpane.setPrefSize(screenwidth,screenheight+50);
		ImageView cailan = new ImageView(cailani);
		cailan.setScaleX(screenheight/1600);
		cailan.setScaleY(screenheight/1600);
		cailan.setTranslateX(screenwidth/2-1061/2*(1-screenheight/1600)-50*suofang);
		cailan.setTranslateY(screenheight/2-420/2*(1-screenheight/1600)-100*suofang);
        openpane.getChildren().add(cailan);
		stp.getChildren().add(openpane);
		openpane.setBackground(new Background(new BackgroundFill(new Color(1,1,1,0), null, null)));
		Image w1=imgmake.pixeler(openpane,3);
		w1=imgmake.sharpenimage(w1);
		openpane.getChildren().clear();
		ImageView openwv1=new ImageView(w1);
		openpane.getChildren().add(openwv1);		
		opentimeline = new Timeline(new KeyFrame(Duration.ZERO,new KeyValue(openpane.opacityProperty(),0)),
				new KeyFrame(Duration.seconds(1),new KeyValue(openpane.opacityProperty(),1)),
				new KeyFrame(Duration.seconds(2.2),e->{
					isopenfinishf=1;
					game.setVisible(true);
					},new KeyValue(openpane.opacityProperty(),1),new KeyValue(game.opacityProperty(),0)),
				new KeyFrame(Duration.seconds(3.8),new KeyValue(openpane.opacityProperty(),0)),
				new KeyFrame(Duration.seconds(4.4),new KeyValue(game.opacityProperty(),1)));  
		opentimeline.setOnFinished(e->{
			isopenfinishf=2;
			stp.getChildren().remove(openpane);	
		});
		opentimeline.play();
		
		
		alli.setMouseTransparent(true);
		stp.getChildren().add(alli);	
		
	    fpsText.setFill(Color.RED);
	    fpsText.setFont(new Font(20));
	    fpsText.setTranslateX(25);
	    fpsText.setTranslateY(25);
	    stp.getChildren().add(fpsText);
	
		mouseiv.setMouseTransparent(true);
		stp.getChildren().add(mouseiv);//鼠标
		Timeline pixer = new Timeline(new KeyFrame(Duration.millis(8), e->{
			if(nowgrab!=null) {
				nowgrab.setScaleX(scale);
				nowgrab.setScaleY(scale);
			}
			if(isopenfinishf>0) {
				optimizeNode(root);
			}}));
		pixer.setCycleCount(Timeline.INDEFINITE);
		pixer.setAutoReverse(true);
		pixer.play();	
		
		Timeline t1 = new Timeline(new KeyFrame(Duration.millis(16), e->{
			if(isopenfinishf>0) {
//				startpixelProcessing();
			}
			if(scaleTimeline.getStatus() == Animation.Status.STOPPED) {
				canscroll=true;
			}
			if (mouserollcount > 60) {
				mouserollcount=0;				
				mouserollspeed1=Math.random()*7+0.3;
				mouserollspeed2=Math.random()*7+0.3;
				mouserollspeed3=Math.random()*7+0.3;
			}
			else
				mouserollcount+=1;
			if (mouserotate1 > 360)
				mouserotate1 = 0;
			else
				mouserotate1+=mouserollspeed1;
			if (mouserotate2 > 360)
				mouserotate2 = 0;
			else
				mouserotate2+=mouserollspeed2;
			if (mouserotate3 > 360)
				mouserotate3 = 0;
			else
				mouserotate3+=mouserollspeed3;	

			out.setRotate(mouserotate1);
			in.setRotate(-mouserotate2);
			yy.setRotate(mouserotate3);

			Platform.runLater(() -> {
			   // 交换缓冲区
			wi1=guangbiaog.snapshot(sn1, CURSOR_BUFFER);
			
			if(mousetype==0)for (int i = 0; i <18; i++) {
				for (int j = 0; j <24; j++) {
					Color c=crystalm.getPixelReader().getColor(j, i);
					wi1.getPixelWriter().setColor(j, i,c);
				}
			}
			if(mousetype==1)for (int i = 0; i <18; i++) {
				for (int j = 0; j <24; j++) {
					Color c=metalm.getPixelReader().getColor(j, i);
					wi1.getPixelWriter().setColor(j, i,c);
				}
			}
			if(mousetype==2)
			for (int i = 0; i < 18; i++) {
				for (int j = 0; j < 24; j++) {
					if (i + j*0.8 < 22&&(i+j*0.8)%3!=0&&(i+(j-1)*0.8)%3!=0&&(i+(j-2)*0.8)%3!=0&&(i+(j-3)*0.8)%3!=0&&(i<6||j<9)) {
						double c=1-((double)i+(double)j)/26;
						wi1.getPixelWriter().setColor(j, i, Color.color(c,c,c));
					}
				}
			}
			for(int i = 18; i < wi1.getHeight(); i++) {
				for(int j = 24; j < wi1.getWidth(); j++) {
					int c1=wi1.getPixelReader().getArgb(j, i),c2=darken.getPixelReader().getArgb((int)((j*14)/screenheight*screenheight), (int)((i*14+80)/screenheight*screenheight));
					int a1 = (c1 >> 24) & 0xFF;
					int a2 = (c2 >> 24) & 0xFF;
					double c3=1-(double)a1*(double)a2/256/256;
					wi1.getPixelWriter().setColor(j, i, Color.TRANSPARENT);
					
					if(a1>200)
						wi1.getPixelWriter().setColor(j, i, Color.color(c3,c3,c3));
				}
			}
			});
			ImageCursor icursor1=new ImageCursor(wi2);
			scene.setCursor(icursor1);

		}));
		t1.setCycleCount(Timeline.INDEFINITE);
		t1.setAutoReverse(true);
		t1.play();
		
		Timeline mouseanict = new Timeline(new KeyFrame(Duration.millis(1),e->{
			mouseiv.setTranslateX(nowmousex);
			mouseiv.setTranslateY(nowmousey);
		}));
		
		mouseanict.setCycleCount(Timeline.INDEFINITE);
		mouseanict.setAutoReverse(true);
		mouseanict.play();
		
		 
        scene.setOnMouseMoved(e->{
        	nowmousex=e.getX();
        	nowmousey=e.getY();
        });
        // 修改后的滚轮事件：以鼠标位置为中心缩放
        root.setOnScroll(event -> {
        	if(canscroll&&scaleTimeline.getStatus() == Animation.Status.STOPPED) {
        		
	            // 获取滚动方向和当前时间
	            double deltaY = event.getDeltaY();
	            if(!(deltaY<0&&scale<0.17*suofang)&&!(deltaY>0&&scale>0.9*suofang)){

	            double currentScale = root.getScaleX();
	            double scaleFactor = (deltaY > 0) ? 1.25 : 0.8;
	            double targetScale = currentScale * scaleFactor;
	            targetScale=(double)Math.round(targetScale*1000)/1000;
	            // 计算鼠标坐标
	            scale=targetScale;
	            
	            maxoffsetx=100*scalemubanl-screenwidth/2*(1-scale);
	          	maxoffsety=25*scalemubanl-screenheight/2*(1-scale);
	            minoffsetx=maxoffsetx-200*scalemubanl-12300*scale+screenwidth;
	            minoffsety=maxoffsety-750-12740*scale+screenheight;
	            double mouseX = nowmousex - screenwidth / 2;
	            double mouseY = nowmousey - screenheight / 2;
	            // 计算目标平移值
	            double currentTranslateX = root.getTranslateX();
	            double currentTranslateY = root.getTranslateY();
	            double targetTranslateX = currentTranslateX + (1 - scaleFactor) * (mouseX - currentTranslateX);
	            double targetTranslateY = currentTranslateY + (1 - scaleFactor) * (mouseY - currentTranslateY);
	            double sx=targetTranslateX>minoffsetx?targetTranslateX:minoffsetx,sy=targetTranslateY>minoffsety?targetTranslateY:minoffsety;
	            sx=sx<maxoffsetx?sx:maxoffsetx;
	            sy=sy<maxoffsety?sy:maxoffsety;
	            // 创建新动画
	        	canscroll=false;
	            scaleTimeline.getKeyFrames().setAll(
	                new KeyFrame(Duration.seconds(0.5),
	                    new KeyValue(root.scaleXProperty(), targetScale, Interpolator.LINEAR),
	                    new KeyValue(root.scaleYProperty(), targetScale, Interpolator.LINEAR),
	                    new KeyValue(root.translateXProperty(), sx, Interpolator.LINEAR),
	                    new KeyValue(root.translateYProperty(), sy, Interpolator.LINEAR)
	                )
	            );
	            if(scaleTimeline.getStatus() == Animation.Status.STOPPED)
	            	scaleTimeline.play();
	            
	            scaleTimeline.setOnFinished(e->{
	            	canscroll=true;
	            });

	            }
	            else canscroll=true;    
        	}  
        });
        scene.setOnKeyPressed(e->{
//        	if(e.getCode()==KeyCode.W) {
//        		pixelsize++;
//        	}
//        	if(e.getCode()==KeyCode.S) {
//        		if(pixelsize>1)
//        			pixelsize--;
//        	}
        	for(int i=0;i<9;i++) {
        		if(e.getCode().toString().equals(ncis[i])) {
        			int x=(int) ((nowmousex-405*suofang)/70/suofang);
    				int y=(int) ((nowmousey-shixu.getTranslateY()-110*suofang-screenheight/2)/49/suofang);
//    				System.out.println(x+"  "+y);
    				if(nowmousex>405*suofang&&nowmousey>shixu.getTranslateY()+110*suofang+screenheight/2) {
    					shixuan k=charans[i];
            			Image i0=charsil.get(ncis[i]),i1=charsi.get(ncis[i]);
            			shixuan nn=shixuans[i];
            			if(effectf) {
            			try {
	            			charlight.stop();
	            			}catch(Exception e1) {}
	            			charlight=new Timeline(new KeyFrame(Duration.ZERO,e1->{k.setImage(i0);nn.setEffect(new Bloom());}),
	            					new KeyFrame(Duration.seconds(0.2),e1->{k.setImage(i1);nn.setEffect(null);}));
	            			charlight.play();
            			}
    					shixuan iv12=new shixuan(shixuani[i]);
//    					sxgp.add(iv12, x, y);
    					int nx=nowlookshixu+x,ny=nowlookshixuy+y;
    					iv12.x= nx;
    					iv12.y= ny;
    					iv12.t=shixuanname[i];
    					if(shixuuses.containsKey(nx+","+ny)&&shixuuses.get(nx+","+ny).t.equals(iv12.t)) {
    						shixuuses.remove(nx+","+ny);
    						refreshshixupane();
    					}
    					else {
    						shixuuses.remove(nx+","+ny);
    						shixuuses.put(nx+","+ny,iv12);
    						refreshshixupane();
    					}
    					iv12.setOnMouseClicked(e1->{
    						shixuuses.remove(nx+","+ny,iv12);
    						refreshshixupane();
    					});
    				}
        		}
        	}
        	if(e.getCode().toString().equals("ESCAPE")) {
        		rttts[4].stop();
        		rttts[4].setFromAngle(caozuoan[4].getRotate());
        		rttts[4].play();
        		ImageView iv=caozuoan[4];
        		rttts[4].setOnFinished(e1->{iv.setRotate(0);});
        	}
        	if(e.getCode().toString().equals("DELETE")) {
        		rttts[7].stop();
        		rttts[7].setFromAngle(caozuoan[7].getRotate());
        		rttts[7].play();
        		ImageView iv=caozuoan[7];
        		rttts[7].setOnFinished(e1->{iv.setRotate(0);});
        	}
        	for(int i=0;i<6;i++) {
	        	if(e.getCode().toString().equals("F"+(i+1))) {
	        		int j=i<4?i:i+1;
	        		rttts[j].stop();
	        		rttts[j].setFromAngle(caozuoan[j].getRotate());
	        		rttts[j].play();
	        		ImageView iv=caozuoan[j];
	        		rttts[j].setOnFinished(e1->{iv.setRotate(0);});
	        	}
        	}
        });
		root.setOnMouseDragged(event -> {
			nowmousex = event.getScreenX();
			nowmousey = event.getScreenY()+30;
			scaleTimeline.stop();
			if (mouseX == 0) {
				mouseX = event.getScreenX();
				mouseY = event.getScreenY();
				transX = root.getTranslateX();
				transY = root.getTranslateY();
			}

			double x = event.getScreenX() - mouseX + transX;
			double y = event.getScreenY() - mouseY + transY;

			double sx = x > minoffsetx ? x : minoffsetx, sy = y > minoffsety ? y : minoffsety;
			sx = sx < maxoffsetx ? sx : maxoffsetx;
			sy = sy < maxoffsety ? sy : maxoffsety;

			root.setTranslateX(sx);
			root.setTranslateY(sy);
		});

		scene.setOnMouseReleased(e -> {
			mouseX = 0;
			mouseY = 0;
			transX = 0;
			transY = 0;
			if (isopenfinishf == 0) {
				game.setVisible(true);
				isopenfinishf = 2;
				stp.getChildren().remove(openpane);
				opentimeline.jumpTo(Duration.seconds(5));
			}
		});
		
		seteffect();
		inirolls();	
//设置透明GPU性能消耗巨大
//		primaryStage.setFullScreen(true);
//		primaryStage.centerOnScreen();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(scene);
//        primaryStage.setAlwaysOnTop(true);
		primaryStage.show();
		
		inilongzhua();
		inibaoshi("狗");
        define d0=makebaoshi(7,5,"鹿");
    	define d1=makebaoshi(8,5,"狗");
		define d2=makebaoshi(9,5,"鹿");
//		
		define d4=makebaoshi(7,4,"鹿");
		define d5=makebaoshi(6,4,"鹿");
		define d3=makebaoshi(10,5,"鹿");
		define d7=makebaoshi(12,5,"鹿");
		define d8=makebaoshi(12,6,"鹿");

//		makeliuhehunyuan(1,8);
		makehunyuan(10,3);
		makewuzang(13,4, 0);
//		defineroll(d8,5);
//		d0.connect(d2);
		longzhua l=makelongzhua(7,7);
		longzhua l1=makelongzhua(13,10);
//		process = new Timeline(new KeyFrame(Duration.seconds(0),e->{xuanzhuanlzb(l,7,5,0);}),
//				new KeyFrame(Duration.seconds(2),e->{l.grab((define)d0);
//				xuanzhuanlzb(l,8,8,2);}),
//				new KeyFrame(Duration.seconds(4),e->{l.put();
//				xuanzhuanlzb(l,7,9,2);}),
//				new KeyFrame(Duration.seconds(6),e->{l.put();l.grab((define)d2);xuanzhuanlzb(l1,9,9,1);
//				xuanzhuanlzb(l,7,7,2);}),
//				new KeyFrame(Duration.seconds(8),e->{l.put();l.grab((define)d2);
//				xuanzhuanlzb(l,9,9,0);}),
//				new KeyFrame(Duration.seconds(10),e->{l.put();l1.grab(d2);
//				xuanzhuanlzb(l1,15,11,2);}));
//		process.play();	

    }
    
    
    public void startup() {
//    	System.out.println(1);
    	System.out.println(shixulength);
    	for(int i=0;i<shixulength;i++) {
    		int k=i;
    		System.out.println(k);
    		process.getKeyFrames().add(new KeyFrame(Duration.seconds(k*2),e->{
    			act(k);
    		}));
    	}
    	process.play();	
    }
    public void act(int k) {
//    	System.out.println(2);
    	for(int i=0;i<machines.size();i++) {
    		if(shixuuses.containsKey(k+","+i)) {
//    			shixuanname = { "擒获","围绕","逸释","安息","始恸","递归","纵横","蓄势","抄录"};
    			grid ma=machines.get(i);
    			shixuan ml=shixuuses.get(k+","+i);
    			String type=ml.t;
    			System.out.println(type+" "+ma.name);
    			switch(ma.name) {
	    			case "青龙爪":{
	    				switch(type) {
	    					case "擒获":{
//	    						((longzhua) (ma)).grab((define)db[ml.aimy][ml.aimx]);
	    						((longzhua) (ma)).grab((define)db[5][7]);
	    					}break;
	    					case "围绕":{
//	    						((longzhua) (ma)).grab((define)db[ml.aimy][ml.aimx]);
	    						xuanzhuanlzb(((longzhua) (ma)),6,8,2);
	    					}break;
	    					default:break;
	    				}
	    			}break;
	    			
	    			default:break;
    			}
//    			if(ma.name.equals("青龙爪")) {
//    				if(type.equals("擒获")) {
//    					((longzhua) (ma)).grab((define)db[ml.aimy][ml.aimx]);
//    				}
//    			}
    		}
    	}
    }
    private void seteffect() {
    	if(effectf) {
			ui.setEffect(ds0);
			ds4.setColor(Color.color(0, 0, 0,1));
			ds4.setOffsetY(-10*suofang);
			ds4.setRadius(20*suofang);
			shixu.setEffect(ds4);
			ds3.setColor(Color.color(0, 0, 0));
			shixuyinyu.setEffect(ds3);
			ds.setOffsetY(3*suofang);
			ds.setColor(Color.color(0, 0, 0));
			mouseiv.setEffect(ds);
			ds2.setSpread(0.4);
			mubanleft.setEffect(ds2);
			mubanright.setEffect(ds2);
		}
    	else {
    		ui.setEffect(null);
    		shixu.setEffect(null);
    		shixuyinyu.setEffect(null);
    		mouseiv.setEffect(null);
    		mubanleft.setEffect(null);
			mubanright.setEffect(null);
    	}
	}


	private void refreshshixupane() {
    	 double wn=screenwidth/70/suofang;
	   	  sxgp.getChildren().clear();
	   	  for(int i=0;i<4;i++) {
	   		  for(int j=0;j<wn;j++) {
	   			  int nx=nowlookshixu+j;
	   			  int ny=nowlookshixuy+i;
	   			  String k=nx+","+ny;
	   			  if(shixuuses.containsKey(k))
	   				  sxgp.add(shixuuses.get(k),j,i);
	   		  }
	   	  }
	   	  shixulength=0;
	   	  shixuuses.keySet().forEach(e->{
//	   		  System.out.println(e);
	   		  int m=Integer.parseInt(e.split(",")[0])+1;
	   		  if(m>shixulength)shixulength=m;
	   	  });
//	   	  System.out.println(shixulength);
	}


	public Group makeliuhehunyuan(int x,int y) {

    	Group hunyuan=new Group();
		ImageView taiyang=new ImageView(taiyangi);
		ImageView jinwu=new ImageView(jinwui);
		ImageView liuhe=new ImageView(blend2i);
		liuhe.setFitHeight(1040);
		liuhe.setFitWidth(1200);
		taiyang.setTranslateX(177+300);
		taiyang.setTranslateY(127+260);
		jinwu.setTranslateX(125+300);
		jinwu.setTranslateY(78+260);
		taiyang.setFitHeight(268);
		taiyang.setFitWidth(259);
		jinwu.setFitHeight(365);
		jinwu.setFitWidth(365);
		taiyang.setScaleX(2);
		taiyang.setScaleY(2);
		jinwu.setScaleX(2);
		jinwu.setScaleY(2);
		taiyang.getTransforms().add(new Rotate(0,129.5,134));
		jinwu.getTransforms().add(new Rotate(0,182.5,182.5));
		Timeline t = new Timeline(new KeyFrame(Duration.ZERO,new KeyValue(taiyang.rotateProperty(),0),new KeyValue(jinwu.rotateProperty(),0)),
				new KeyFrame(Duration.seconds(2),new KeyValue(taiyang.rotateProperty(),180),new KeyValue(jinwu.rotateProperty(),-180)),
				new KeyFrame(Duration.seconds(4),new KeyValue(taiyang.rotateProperty(),360),new KeyValue(jinwu.rotateProperty(),-360)));
      
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
        
        hunyuan.getChildren().addAll(liuhe,taiyang,jinwu);

        hunyuan.setTranslateX(x*150-150);
        hunyuan.setTranslateY(y*260);
		hypane.getChildren().add(hunyuan);
		return hunyuan;
    }
    
    public ImageView makewuzang(int x,int y,int i) {
		ImageView w0=new ImageView(wuzang[i]);
		w0.setTranslateX(x*150);
		w0.setTranslateY(y*260);
		hypane.getChildren().add(w0);
		return w0;
    }
    
    public Group makehunyuan(int x,int y) {
    	GaussianBlur gb1= new GaussianBlur();
        gb1.setRadius(80);
    	Group hunyuan=new Group();
		ImageView xuan=new ImageView(blend1i);
		hunyuan.setTranslateX(x*150);
		hunyuan.setTranslateY(y*260);
        hunyuan.getChildren().add(xuan);
        hypane.getChildren().add(hunyuan);
		return hunyuan;
    }
    @Override
    public void stop() {
        executor.shutdown();
    }
    @SuppressWarnings("unused")
	private void startpixelProcessing() {
        // 1. 创建处理任务
    	SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
//        params.setTransform(new javafx.scene.transform.Affine()); // 禁用变换缓存
//        params.setDepthBuffer(false); // 禁用深度缓冲
        params.setViewport(new Rectangle2D(0, 0, screenwidth, screenheight+50));
//        alli.setScaleX(pixelsize);
//        alli.setScaleY(pixelsize);
        Image snapshot = game.snapshot(params, null);
        Task<Image> processingTask = new Task<Image>() {
            protected Image call() throws Exception {
                // 在后台线程执行（此处可替换为实际参数）
                return imgmake.well(snapshot,pixelsize);
//                iv.setSmooth(true); // 启用抗锯齿平滑
//            	return imgmake.fastpixeler(snapshot,pixelsize);
            }
        };
        // 3. 处理完成回调
        processingTask.setOnSucceeded(e -> {
        		targetX=root.getTranslateX();
        		targetY=root.getTranslateY();
        		rooti=processingTask.getValue(); 
          		alli.setImage(rooti);
        });
        // 4. 异常处理
        processingTask.setOnFailed(e -> {
            Throwable ex = processingTask.getException();
            ex.printStackTrace();
//            showErrorDialog("处理失败: " + ex.getMessage());
        });
        // 5. 启动任务
        new Thread(processingTask).start();

    }
    public Effect makeblend(BlendMode bm,Effect e1,Effect e2,double op) {
		Blend b=new Blend();
		b.setMode(bm);
		b.setOpacity(op);
		b.setTopInput(e1);
		b.setBottomInput(e2);
		return b;
	}
    
    public void defineroll(define b,int roll) {
    	Group img=b.g;
    	Rotate r=rolls[roll];
    	b.face=(b.face+1)%6;
        // 创建时间轴动画   	
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(r.angleProperty(), 0)),
            new KeyFrame(Duration.seconds(0.001),e->{img.getTransforms().add(r);}),
            new KeyFrame(Duration.seconds(1),e->{b.y+=rolln[roll][0];b.x+=rolln[roll][1];}, new KeyValue(r.angleProperty(),r.getAngle()))
        );
        timeline.setCycleCount(1);
        timeline.play();
        timeline.setOnFinished(e->{	
        });
    }
    
//    private void updateSmoothPosition() {
//        double deltaX =  root.getTranslateX()-targetX;
//        double deltaY = root.getTranslateY()-targetY;
// 
//        rootv.setTranslateX(deltaX * SMOOTH_FACTOR);
//        rootv.setTranslateY(deltaY * SMOOTH_FACTOR);
//    }
    public void inirolls() {
    	leftclock=new Rotate();
    	leftclock.setPivotX(150);
    	leftclock.setPivotY(260);
        leftclock.setAngle(60);
        leftanti=new Rotate();
        leftanti.setAngle(-60);
        rightclock=new Rotate();
        rightclock.setPivotX(300);
        rightclock.setAngle(60);
        rightanti=new Rotate();
        rightanti.setPivotX(300);
        rightanti.setAngle(-60);
        downclock=new Rotate();
        downclock.setPivotX(150);
        downclock.setPivotY(260);
        downclock.setAngle(60);
        downanti=new Rotate();
        downanti.setPivotX(150);
        downanti.setPivotY(260);
        downanti.setAngle(-60);
        rolls=new Rotate[] {leftclock,leftanti,rightclock,rightanti,downclock,downanti};
    }
    
    public void openbook(){
    	ds1.setOffsetY(13);
		ds1.setOffsetX(8);
    	double sc=screenheight/601;
    	Rotate r1= (Rotate) zhengjin.getTransforms().get(0);
        r1.setAngle(0);
    	
    	zhengjin.setOnMouseDragged(null);
    	zhengjin.setOnMouseClicked(e->{
    		if(e.getButton()==MouseButton.PRIMARY) {
    			nowpage=(nowpage+1)%5;
    			zhengjin.setImage(zhengjins[nowpage]);
    		}
    		else {
    			if(nowpage>0) {
    				nowpage=(nowpage-1)%5;
    				zhengjin.setImage(zhengjins[nowpage]);
    			}
    			else {
    				closebook();
    			}
    		}
    	});
    	zhengjin.setScaleX(sc);
    	zhengjin.setScaleY(sc);
    	if(leftmubanf)
    		zhengjin.setTranslateX(screenwidth/2-433*sc/2+40 * scalemubanl-50);
    	else
    		zhengjin.setTranslateX(screenwidth/2-433*sc/2+200 * scalemubanl-50);
    	zhengjin.setTranslateY(601/2-30);
    	
    }
    
    public void closebook() {
    	zhengjin.setImage(hunyuanzhengjini);
    	zhengjin.setScaleX(scalemubanl*0.4);
        zhengjin.setScaleY(scalemubanl*0.4);
        Rotate r=new Rotate(0,433/ 2,601/ 2);
        zhengjin.getTransforms().add(r);
        if(effectf)
        	zhengjin.setEffect(ds1);
        ds1.setOffsetY(13);
		ds1.setOffsetX(8);

   // 鼠标按下事件：记录初始位置
		zhengjin.setTranslateX(-433/2*(1-scalemubanl*0.4)+100*suofang);
		zhengjin.setTranslateY(-601/2*(1-scalemubanl*0.4)+50*suofang);
		zhengjin.setOnMouseClicked(null);
		zhengjin.setOnMousePressed(event -> {
            orgSceneX = event.getSceneX();
            orgSceneY = event.getSceneY();
            pressStartTime= System.currentTimeMillis(); 
            // 记录组件中心初始位置
            initialCenterX = zhengjin.getTranslateX()+ 433/ 2+mubanleft.getTranslateX();
            initialCenterY = zhengjin.getTranslateY()+ 601/ 2;
            
            // 计算初始距离

            initialDistance = Math.hypot(
                initialCenterX - orgSceneX,
                initialCenterY - orgSceneY
            );
            event.consume();
        });

        // 鼠标拖动事件：计算反向延长线位置和旋转角度
		zhengjin.setOnMouseDragged(event -> {
        	nowmousex=event.getScreenX();
        	nowmousey=event.getScreenY();
        	 double currentX = event.getSceneX();
             double currentY = event.getSceneY();

             // 计算从初始点到当前点的向量
             double deltaX = currentX - orgSceneX;
             double deltaY = currentY - orgSceneY;
             double distance = Math.hypot(deltaX, deltaY);

             if (distance == 0) return; // 防止除以零

             // 计算反向单位向量
             double unitX = -deltaX / distance;
             double unitY = -deltaY / distance;

             // 计算新中心位置
             double newCenterX = currentX + unitX * initialDistance;
             double newCenterY = currentY + unitY * initialDistance;

             // 更新组件位置
             zhengjin.setTranslateX(newCenterX - 433/ 2-mubanleft.getTranslateX());
             zhengjin.setTranslateY(newCenterY - 601 / 2);

             // 计算旋转角度（组件指向拖动点）
             double angle = Math.toDegrees(Math.atan2(
                 currentY - newCenterY,   // Y方向分量（注意坐标系方向）
                 currentX - newCenterX    // X方向分量
             ));
             
             // 设置旋转角度（JavaFX角度系统需要调整）
             Rotate r1= (Rotate) zhengjin.getTransforms().get(0);
             r1.setAngle(angle ); // 补偿坐标系差异
             event.consume();
            
            ds1.setOffsetX(20*Math.sin(Math.toRadians(angle+30)));
            ds1.setOffsetY(20*Math.cos(Math.toRadians(angle+30)));

            event.consume();
        });
		zhengjin.setOnMouseReleased(e->{
        	double deltaX = e.getSceneX()- orgSceneX;
            double deltaY = e.getSceneY()- orgSceneY;
        	long duration = System.currentTimeMillis() - pressStartTime;
        	if(e.getScreenX()>278*scalemubanl) openbook();
        	else if(deltaX==0&&deltaY==0&&duration<300)openbook();
        });
    }

    public longzhua makelongzhua(int x,int y) {
    	if(x>-1&&y>-1) {
    	ImageView baseImage = new ImageView(longzhuax1i);
    	baseImage.setFitHeight(173);
    	baseImage.setFitWidth(173);
		baseImage.setTranslateX(63);
		ImageView middleImage1 = new ImageView(zhuazis[0]);
		middleImage1.setTranslateX(-23);
		middleImage1.setTranslateY(-520-87);
		middleImage1.setFitHeight(346);
		middleImage1.setFitWidth(346);
		ImageView middleImage = new ImageView(zhuabis[0]);
		middleImage.setTranslateX(-600+150);
		middleImage.setTranslateY(-600+95);
		middleImage.setFitHeight(1200);
		middleImage.setFitWidth(1200);
		ImageView topImage = new ImageView(longzhuax2i);
		topImage.setFitHeight(146);
		topImage.setFitWidth(166);
		topImage.setTranslateX(68);
		topImage.setTranslateY(15);
		ImageView upImage = new ImageView(longzhuani);
		upImage.setFitHeight(98);
		upImage.setFitWidth(112);
		upImage.setTranslateX(97);
		upImage.setTranslateY(-520+45);

        Group g1=new Group();
        g1.getChildren().add(new Group());
        baseImage.setSmooth(false);
        middleImage1.setSmooth(false);
        middleImage.setSmooth(false);
        topImage.setSmooth(false);
        upImage.setSmooth(false);
        g1.getChildren().addAll(baseImage,middleImage1,middleImage,topImage,upImage);
        g1.setTranslateX(150*x);
        if((x+y)%2==0) {
        	g1.setTranslateY(260*y);
        }
        else {
        	middleImage1.setRotate(180);
        	g1.setTranslateY(260*y+260/3);
        }
        	
//        g1.setCache(true);
//        g1.setTranslateX(i%81*150);
//        g1.setTranslateY((i/81)*260);
        lzpane.getChildren().add(g1);
        longzhua l=new longzhua(x,y,g1);
        l.name="青龙爪";//取名
        machines.add(l);//添加到机器链表
        db[y][x]=l;//添加到网格
        grabshixuupdown();//刷新时序面板
        colis[y][x]=new ArrayList<Segment>();//添加碰撞体
        double ox=x*150,oy=y*260+((x+y)%2==0?0:94.8);
       
        colis[y][x].add(new Segment(new Point(ox+115.8,oy),new Point(ox+184.2,oy)));
        colis[y][x].add(new Segment(new Point(ox+184.2,oy),new Point(ox+232.6,oy+48.4)));
        colis[y][x].add(new Segment(new Point(ox+232.6,oy+48.4),new Point(ox+232.6,oy+116.8)));
        colis[y][x].add(new Segment(new Point(ox+232.6,oy+116.8),new Point(ox+184.2,oy+165.2)));
        colis[y][x].add(new Segment(new Point(ox+184.2,oy+165.2),new Point(ox+115.8,oy+165.2)));
        colis[y][x].add(new Segment(new Point(ox+115.8,oy+165.2),new Point(ox+67.4,oy+116.8)));
        colis[y][x].add(new Segment(new Point(ox+67.4,oy+116.8),new Point(ox+67.4,oy+48.4)));
        colis[y][x].add(new Segment(new Point(ox+67.4,oy+48.4),new Point(ox+115.8,oy)));
		return l;
    	}
    	return null;
    }
    
    private void updateClawPosition(longzhua lz,Group baoshi,ImageView zhuabi,ImageView zhuazi,ImageView niu) {
        // 获取爪臂当前状态
	        double angle = zhuabi.getRotate();
	        double angle1 = zhuazi.getRotate();
	        double angle2 = baoshi.getRotate();
	        double scale = zhuabi.getScaleY();
	        // 计算爪臂顶端坐标（基于旋转中心）
	        double ofx=0,ofy=0,ofx1=0,ofy1=0;
	        Point2D armEnd = calculateArmEnd(angle, scale);
        	List<Segment> lzshape = new ArrayList<>();
        	double lx0=lz.x*150+150,ly0=lz.y*260+((lz.x+lz.y)%2==0?86.667:173.334);
        	double sin0=Math.sin(Math.toRadians(angle)),cos0=Math.cos(Math.toRadians(angle));
        	double ofx0=30*cos0,ofy0=30*sin0,ofx2=40*cos0+130*sin0,ofx3=-40*cos0+130*sin0,ofy2=40*sin0-130*cos0,ofy3=-40*sin0-130*cos0;
        	Point lp0=new Point(armEnd.getX()+lx0+ofx0,armEnd.getY()+ly0+ofy0),
        		lp1=new Point(lx0+ofx2,ly0+ofy2),
        		lp2=new Point(lx0+ofx3,ly0+ofy3),
        		lp3=new Point(armEnd.getX()+lx0-ofx0,armEnd.getY()+ly0-ofy0);	
        	lzshape.add(new Segment(lp0,lp1));
        	lzshape.add(new Segment(lp1,lp2));
        	lzshape.add(new Segment(lp2,lp3));
        	lzshape.add(new Segment(lp3,lp1));
//        	try {
//        	lzpane.getChildren().remove(checkcoli);
//        	}catch(Exception e1) {}
//        	checkcoli=new Polygon(armEnd.getX()+lx0+ofx0,armEnd.getY()+ly0+ofy0,lx0+ofx2,ly0+ofy2,lx0+ofx3,ly0+ofy3,
//        			armEnd.getX()+lx0-ofx0,armEnd.getY()+ly0-ofy0);
//        	checkcoli.setFill(Color.color(0, 1, 1,0.4));
//        	lzpane.getChildren().add(checkcoli);
        	for(int i=Math.max(0,lz.x-6);i<Math.min(81,lz.x+6);i++) {
				for(int j=Math.max(0,lz.y-6);j<Math.min(81,lz.y+6);j++) {
					try {
						if(doShapesIntersect(lzshape,colis[j][i])) {
							if(!colics.containsKey(i+","+j)) {
								Circle cx0=new Circle(150,(i+j)%2==0?87:174,90,Color.color(1,0,0,0.4));
								cx0.setTranslateX(i*150);	
								cx0.setTranslateY(j*260);
								lzpane.getChildren().add(cx0);
								colics.put(i+","+j, cx0);
							}
						}
						else {
							if(colics.containsKey(i+","+j)) {
								lzpane.getChildren().remove(colics.get(i+","+j));
								colics.remove(i+","+j);
							}
						}
					}catch(Exception e1) {}
				}
			}
        	
	        try {
	        if(baoshi.getBoundsInParent().getWidth()!=-1) {
	    		double s=Math.sin(Math.toRadians(angle2)),c=Math.cos(Math.toRadians(angle2));
//	    		double size=lz.d.parentGroup.gems.size();
	    		double mx=lz.d.parentGroup.mofx,my=lz.d.parentGroup.mofy;
	    		double x0=lz.d.g.getBoundsInParent().getWidth()/2-mx,
	    				y0=lz.d.g.getBoundsInParent().getHeight()/2-my;
	    		double x1=baoshi.getBoundsInLocal().getWidth()/2,y1=baoshi.getBoundsInLocal().getHeight()/2;
	    		double x=x1-x0,y=y1-y0;
	    		ofx=x*c-y*s;
	    		ofy=x*s+y*c;
	    		ofx1=x0-x1-23;ofy1=y0-y1-87.5;

	    		baoshi.setTranslateX(lz.g.getTranslateX()+armEnd.getX()+ofx+ofx1);
	    		baoshi.setTranslateY(lz.g.getTranslateY()+armEnd.getY()+ofy+ofy1);
				int gs=lz.d.parentGroup.gems.size();
				define[] ds=lz.d.parentGroup.gems.toArray(new define[gs]);
				double upofy=(lz.d.upf)%2==0?86.667:173.334;
				double upofy1=(lz.x+lz.y)%2==0?0:86.667;
				double ax=armEnd.getX()+lz.x*150+150,ay=armEnd.getY()+lz.y*260+87.5;
				int wi=(int) ((Math.max(baoshi.getBoundsInLocal().getWidth(),baoshi.getBoundsInLocal().getHeight())*2)/300);
				int mwx=(int) ((lz.g.getTranslateX()+armEnd.getX()+ofx+ofx1)/150)+1,
						mwy=(int) ((lz.g.getTranslateY()+armEnd.getY()+ofy+ofy1)/260)+1;
				List<Segment> baoshishape = new ArrayList<>();
//				try {
//		        	lzpane.getChildren().remove(checkcoli0);
//		        	}catch(Exception e1) {}
				checkcoli0=new Polygon();
				for(int i=0;i<gs;i++) {
					int soffx=ds[i].x-lz.d.x;
					int soffy=ds[i].y-lz.d.y;
					double sofx0=soffx*150-150,sofx1=soffx*150,sofx2=soffx*150+150;
					double sofy0=(soffy+ds[i].upf)*260-upofy,sofy1=(soffy-ds[i].upf+1)*260-upofy,sofy2=sofy0;
					double sofx01=sofx0*c-sofy0*s+ax,sofx11=sofx1*c-sofy1*s+ax,sofx21=sofx2*c-sofy2*s+ax;
					double sofy01=sofx0*s+sofy0*c+ay+upofy1,sofy11=sofx1*s+sofy1*c+ay+upofy1,sofy21=sofx2*s+sofy2*c+ay+upofy1;
					Segment s0=new Segment(new Point(sofx01,sofy01),new Point(sofx11,sofy11));
					Segment s1=new Segment(new Point(sofx21,sofy21),new Point(sofx11,sofy11));
					Segment s2=new Segment(new Point(sofx01,sofy01),new Point(sofx21,sofy21));
					baoshishape.add(s0);baoshishape.add(s1);baoshishape.add(s2);
					
//					checkcoli0.getPoints().addAll(sofx01,sofy01,sofx11,sofy11,sofx21,sofy21);
//					Circle c0=new Circle(0,0,10,Color.RED);
//					c0.setTranslateX(sofx01);	
//					c0.setTranslateY(sofy01);
//					lzpane.getChildren().add(c0);
//					Circle c1=new Circle(0,0,10,Color.BLUE);
//					c1.setTranslateX(sofx11);	
//					c1.setTranslateY(sofy11);
//					lzpane.getChildren().add(c1);
//					Circle c2=new Circle(0,0,10,Color.GREEN);
//					c2.setTranslateX(sofx21);	
//					c2.setTranslateY(sofy21);
//					lzpane.getChildren().add(c2);
				}
//	        	checkcoli0.setFill(Color.color(0, 1, 1,0.4));
//	        	lzpane.getChildren().add(checkcoli0);
				
				for(int i=Math.max(0,mwx-wi);i<Math.min(81,mwx+wi);i++) {
					for(int j=Math.max(0,mwy-wi);j<Math.min(81,mwy+wi);j++) {
						try {
							if(doShapesIntersect(baoshishape,colis[j][i])) {
								if(!colics.containsKey(i+","+j)) {
									Circle cx0=new Circle(150,(i+j)%2==0?87:174,86,Color.color(1,0,0,0.4));
									cx0.setTranslateX(i*150);	
									cx0.setTranslateY(j*260);
									lzpane.getChildren().add(cx0);
									colics.put(i+","+j, cx0);
								}
							}
							else {
								if(colics.containsKey(i+","+j)) {
									lzpane.getChildren().remove(colics.get(i+","+j));
									colics.remove(i+","+j);
								}
							}
						}catch(Exception e1) {}
					}
				}
	        }
	        }catch(Exception e2) {}
//			long lasttime=lz.nowtime,nowtime=System.currentTimeMillis();
//	    	if(nowtime-lasttime>16) {
//	    		lz.nowtime=nowtime;
//	    		try {
	    		 // 更新爪子位置（考虑爪臂自身坐标系偏移）

		        zhuazi.setTranslateX(armEnd.getX() - 23);  // 补偿爪子自身宽度
		        zhuazi.setTranslateY(armEnd.getY() - 87);  // 补偿爪子自身高度
				niu.setTranslateX(97+armEnd.getX());
				niu.setTranslateY(45+armEnd.getY());
		
//			}catch(Exception e1) {}
//	    	}
			int np1=(int) Math.floor(((angle+360)%360)/30),np2=(int) Math.floor(((angle1+360)%360)/30);
			if(lz.nowpic1!=np1) {
				lz.nowpic1=np1;
				zhuabi.setImage(zhuabis[np1]);
			}
			if(lz.nowpic2!=np2) {
				lz.nowpic2=np2;
				 if(lz.d!=null) {
					 zhuazi.setImage(zhuazigs[np1]);
				 }
				 else {
					 zhuazi.setImage(zhuazis[np1]);
				 }
			}
    }

    private Point2D calculateArmEnd(double angleDeg, double scale) {
        // 将角度转换为弧度
        double angleRad = Math.toRadians(-angleDeg+90);
        // 计算实际长度（考虑缩放）
        double length = 520 * scale;
        // 计算顶端坐标（基于旋转中心）
        double endX = length * Math.cos(angleRad);
        double endY = -length * Math.sin(angleRad); // Y轴向下为正，需取反
        return new Point2D(endX,endY);
    }
	public void xuanzhuanlzb(longzhua lz, int x, int y,int roll) {
//		if(!pause) {
		 int lx=lz.x,ly=lz.y;
	        int offgridx=x-lx,offgridy=y-ly;
	        double offx=150*offgridx,offy=260*offgridy+((offgridy+offgridx)%2==0?0:((lx+ly)%2==0?260/3:-260/3));
	        double length=Math.sqrt(offx*offx+offy*offy);
			double radians = Math.atan2(offy, offx);
			double degrees = Math.toDegrees(radians)%360;
			double rd=Math.abs(degrees + 90-lz.rotater)>180?degrees-270:degrees + 90;
			ImageView zhuazi = (ImageView) lz.g.getChildren().get(2);
			ImageView zhuabi = (ImageView) lz.g.getChildren().get(3);
			ImageView niu = (ImageView) lz.g.getChildren().get(5);
			Group ng=new Group();
			dfupane.getChildren().add(ng);
        if(lz.movef==1) {
        	lz.movef=0;
        	
        	try {
        		lz.d.parentGroup.nowd=lz.d;
        	}catch(Exception e1) {}
        	
        	int l=lz.d.parentGroup.gems.size();
        	define[] ds=lz.d.parentGroup.gems.toArray(new define[l]); 
        	double ofx=lz.d.g.getTranslateX(),ofy=lz.d.g.getTranslateY();
        	double mofx=0,mofy=0;
        	for(int i=0;i<l;i++) {
//        		define nd=ds[i].clone();
//        		dfpane.getChildren().add(nd.g);
        		ng.getChildren().add(ds[i].g);
        		double nofx=ds[i].g.getTranslateX()-ofx,nofy=ds[i].g.getTranslateY()-ofy;
        		ds[i].g.setTranslateX(nofx);
        		ds[i].g.setTranslateY(nofy);
        		if(nofx<mofx)mofx=nofx;
        		if(nofy<mofy)mofy=nofy;
        	}
        	updateClawPosition(lz,ng, zhuabi, zhuazi, niu);
        	lz.d.parentGroup.mofx=mofx;
        	lz.d.parentGroup.mofy=mofy;
        	ng.rotateProperty().addListener(obs ->{
        		try {
        			if(lz.d==null)
        				lz.g.getChildren().set(0,null); 

	        		for(int i=0;i<l;i++) {
	        			int np1=(int) Math.floor(((ng.getRotate()+(ds[i].face)*60+375)%360)/30);
			        	if(ds[i].nowpic!=np1) {
			        		ds[i].nowpic=np1;
			        		 int k=np1%12;
			        		((ImageView) ds[i].g.getChildren().get(1)).setImage(baoshibs[k]);
			        	}
	        		}
        		}catch(Exception e1) {}
	        });
        
//	        lz.g.getChildren().set(0,ng);  
        	
        }
//        Group baoshi=ng;
//		Group baoshi = (Group) lz.g.getChildren().get(0);
//		BorderStroke stroke = new BorderStroke(
//		            Color.RED,                   // 颜色
//		            BorderStrokeStyle.SOLID,       // 样式（SOLID/DASHED/DOTTED）
//		            CornerRadii.EMPTY,             // 圆角
//		            new BorderWidths(10)            // 宽度（左/右/上/下）
//		        );

		zhuabi.rotateProperty().addListener(obs -> updateClawPosition(lz,ng,zhuabi,zhuazi,niu));
		zhuabi.scaleYProperty().addListener(obs -> updateClawPosition(lz,ng,zhuabi,zhuazi,niu));

    	  // 配置动画参数
        final Duration DURATION = Duration.seconds(1.7);
        final Interpolator EASING = Interpolator.EASE_BOTH; // 缓动效果
//        TranslateTransition niuAnim = new TranslateTransition(DURATION, niu);
//        niuAnim.setToX(97 + offx);
//        niuAnim.setToY(45 + offy);
     

        RotateTransition zhuabiRotateAnim = new RotateTransition(DURATION, zhuabi);
        zhuabiRotateAnim.setToAngle(rd);
        ScaleTransition zhuabiScaleAnim = new ScaleTransition(DURATION, zhuabi);
        zhuabiScaleAnim.setToY(length / 520);
        RotateTransition zhuaziRotateAnim = new RotateTransition(DURATION, zhuazi);
        RotateTransition baoshiRotateAnim = new RotateTransition(DURATION, ng);
        
        int grabangleto=(lz.grabr+roll)%6;
//        System.out.println(lz.grabr);
        double ra1=zhuazi.getRotate()+roll*60,ra2=roll*60;
        zhuaziRotateAnim.setFromAngle(zhuazi.getRotate());
        double r1=ra2>180+zhuazi.getRotate()?ra1-360:ra1;
        zhuaziRotateAnim.setToAngle(r1);
        baoshiRotateAnim.setToAngle(ra2>180?ra2-360:ra2);

        lz.ani = new ParallelTransition(
//            niuAnim,
            zhuabiRotateAnim,
            zhuaziRotateAnim,
            zhuabiScaleAnim,
            baoshiRotateAnim
        );
        if(ng.getChildren().size()!=0) {
	        int np2=(int) Math.floor(((zhuazi.getRotate()+360)%360)/30);
	        zhuazi.setImage(zhuazigs[np2]);
        }
        lz.ani.setInterpolator(EASING);
        lz.ani.play();
        lz.ani.setOnFinished(e->{
        	int np3=(int) Math.floor(((zhuazi.getRotate()+360)%360)/30);
            zhuazi.setImage(zhuazis[np3]);
            lz.rotater=rd;
            lz.grabx=x;
            lz.graby=y;
            lz.grabr=grabangleto;
            try {
            	define d1=lz.d;
            	movebaoshi(d1,x,y,roll);
            	lz.d=null;
                lz.movef = 0;  	
            }catch(Exception e1){
//            	e1.printStackTrace();
            }
        });
//		}
	}
	
	 public void inilongzhua() {
	    	Image longzhuai=new Image(getClass().getResourceAsStream("grabpic/a爪子预览.png"));
	        ImageView lz=new ImageView(longzhuai);
	        lz.setSmooth(false);
	        if(effectf)
	        	lz.setEffect(ds1);
	        double iniscale=scale;
	        lz.setScaleX(iniscale);
	        lz.setScaleY(iniscale);
	        lz.setTranslateX(-211/2*(1-iniscale));
	        lz.setTranslateY(-190/2*(1-iniscale));
	        lz.setOnMouseDragged(e->{
	        	nowmousex=e.getScreenX();
	        	nowmousey=e.getScreenY()+25;
	        	double px=211-105*scale,py=190+90*scale;
	        	double sx=e.getSceneX()-mubanright.getTranslateX()-px,sy=e.getSceneY()-py;
	        	
	        	if((rightmubanf&&e.getScreenX()<screenwidth-300 * scalemubanl||!rightmubanf&&e.getScreenX()<screenwidth-90 * scalemubanl)&&
	        			(leftmubanf&&e.getScreenX()>238 * scalemubanl||!leftmubanf&&e.getScreenX()>78 * scalemubanl)) {
	        		if(canmakecloneblend) {  
	        			lz.setOpacity(0.01);
	        			canmakecloneblend=false;
	        			nowgrab=new ImageView(longzhuai);
	        			((ImageView) nowgrab).setSmooth(false);
	                	mubanright.getChildren().add(nowgrab);
	                	nowgrab.setScaleX(scale);
	                	nowgrab.setScaleY(scale);
	                	nowgrab.setMouseTransparent(true);
	        		}
	        		if(nowgrab!=null) {
	        			if (gpane != null) {
	        			    Point2D localPoint = gpane.sceneToLocal(e.getSceneX(), e.getSceneY());
	        			    int gridX0 = (int) (localPoint.getX() / 150);
	        			    int gridX1 = (int) (localPoint.getX() % 150);
	        			    int gridY = (int) (localPoint.getY() / 260);
	        			    int gridY1 = (int) (localPoint.getY() % 260);
	        			    int off=(gridX0+gridY)%2==1?(gridX1>(260-gridY1)/Math.sqrt(3)?0:-1):(gridX1>gridY1/Math.sqrt(3)?0:-1);
	        			    int gridX=gridX0+off;
	        			    if(gridX<0||gridY<0||gridX>=MAPWIDTH||gridY>=MAPHEIGHT)canputf=false;
	        			    else canputf=true;
	        			    gridX = Math.max(0, Math.min(gridX, MAPWIDTH - 1));
	        			    gridY = Math.max(0, Math.min(gridY, MAPHEIGHT - 1));
	        			    ImageView targetView = gridv[gridY][gridX]; // 注意行列顺序
	        			    targetView.setSmooth(false);
	        			    if (targetView != null) {
								MouseEvent simulatedEvent = new MouseEvent(MouseEvent.MOUSE_ENTERED, localPoint.getX(),
										localPoint.getY(), e.getScreenX(), e.getScreenY(), MouseButton.NONE, 0,
										e.isShiftDown(), e.isControlDown(), e.isAltDown(), e.isMetaDown(), false, false,
										false, false, false, false, null);
	        			        Platform.runLater(() -> {
	        			            targetView.fireEvent(simulatedEvent);
	        			        });
	        			    }
	        			}
	        			nowgrab.setTranslateX(e.getSceneX()-mubanright.getTranslateX()-px);
	        			nowgrab.setTranslateY(e.getSceneY()-py-30);
	        		}	
	        		
	        	}
	        	else if(leftmubanf&&e.getScreenX()<238 * scalemubanl||!leftmubanf&&e.getScreenX()<78 * scalemubanl) {
	        		hovergridx=-1;
	            	hovergridy=-1;
	        	}
	        	else {
	        		hovergridx=-1;
	            	hovergridy=-1;
	        		lz.setOpacity(1);
	        		canmakecloneblend=true;
	            	mubanright.getChildren().remove(nowgrab);
	        		lz.setTranslateX(Math.max(sx,-211*(1-iniscale)));
	            	lz.setTranslateY(sy-30);
	        	}
	        	
	        });
	        lz.setOnMouseReleased(e->{
	        	lz.setOpacity(1);
	        	if(canputf) {
	        		canputf=false;
	        		makelongzhua(hovergridx,hovergridy);
	        	}
	        	canmakecloneblend=true;
	        	mubanright.getChildren().remove(nowgrab);
	        });
	        lz.setOnMouseClicked(e->{
	        	nowgrab=new ImageView(longzhuai);
	        	((ImageView) nowgrab).setSmooth(false);
	        	nowgrab.setScaleX(scale);
	        	nowgrab.setScaleY(scale);
	        	nowgrab.setMouseTransparent(true);
	        	double px=211-110*scale,py=190+100*scale;
	        	nowgrab.setTranslateX(e.getSceneX()-mubanright.getTranslateX()-px);
	        	nowgrab.setTranslateY(e.getSceneY()-py);
	        });
	        mubanright.getChildren().add(lz);
	    }
	public void grabshixuupdown() {
		List<Rectangle> rects = shixu.getChildren().stream().filter(node -> node instanceof Rectangle)
				.map(node -> (Rectangle) node).collect(Collectors.toList());
		
		for (int i = 0; i < 4; i++) {
			double c=(nowlookshixuy+i)%4;
			try {
			String name=machines.get(nowlookshixuy+i).name;
			if(rectcolor.containsKey(name));
			Color c1=rectcolor.get(name);
			Color c2=Color.color(Math.min(1,c1.getRed()*(0.4+c/2)),Math.min(1,c1.getGreen()*(0.4+c/2)),Math.min(1,c1.getBlue()*(0.4+c/2)),c1.getOpacity());
			rects.get(i).setFill(c2);
			}catch(Exception e1) {	
				rects.get(i).setFill(Color.color(c/4,c/4,c/4,0.2));	
			}
		}
	}
	public void removebaoshi(define d,int x,int y) {
        db[y][x]=null;
        Group g2=d.g;
        dfpane.getChildren().remove(g2);
	}
	public void movebaoshi(define d,int x,int y,int roll) {
		int dx=d.x,dy=d.y;
//		int roll1=roll-d.face;在没有d1.x=nowx,d1.y=nowy时使用
		int roll1=roll;
		int s=d.parentGroup.gems.size();
		define[] ds=d.parentGroup.gems.toArray(new define[s]);
		double[] offxs=new double[s], offys=new double[s];
		double[] offxs1=new double[s], offys1=new double[s];
		int[] offxgs=new int[s], offygs=new int[s];
		double sin=Math.sin(Math.toRadians(roll1*60)),cos=Math.cos(Math.toRadians(roll1*60));
		for(int i=0;i<s;i++) {
			offxs[i]=(ds[i].x-dx)*150;
			offys[i]=(ds[i].y-dy)*260+((ds[i].x-dx+ds[i].y-dy)%2==0?0:((ds[i].x+ds[i].y)%2==0?-260/3:260/3));	  
			offxs1[i] = offxs[i] * cos - offys[i] * sin;  
			offys1[i] = offxs[i] * sin + offys[i] * cos;  
			offxgs[i]=(int) Math.floor((offxs1[i]+75)/150.0);
			offygs[i]=(int) Math.floor((offys1[i]+130)/260.0);
			int nowx=x+offxgs[i],nowy=y+offygs[i];
			define d1=ds[i];
			db[nowy][nowx]=d1;
			d1.x=nowx;
			d1.y=nowy;
	        Group g2=d1.g;
	        dfpane.getChildren().add(g2);
	        g2.setTranslateX((nowx)*150-23);
	        int toroll=(d1.face+roll)%6;
	        d1.face=toroll;
	        d1.nowangle=d1.face*60;
	        if((nowx+nowy)%2==0) {
	        	d1.upf=0;
	        	g2.setTranslateY(nowy*260-85);
	        }
	        else {
	        	d1.upf=1;
	        	g2.setTranslateY(nowy*260);
	        }	 
	        g2.setRotate(toroll*60);
		}	
	}
    public define makebaoshi(int x,int y,String name){
    	if(x>=0&&x<MAPWIDTH&&y>=0&&y<MAPHEIGHT) {
	        Group g2=new Group();
	        g2.setEffect(blend);      
	        define d=new define(x,y,g2,name);
	        db[y][x]=d;
	        dfpane.getChildren().add(g2);

	        g2.setTranslateX(x*150-23);
	        if((x+y)%2==0) {
	        	d.face=0;
	        	g2.setTranslateY(y*260-85);
	        }
	        else {
	        	d.face=3;
	        	g2.setTranslateY(y*260);
	        }

	        ImageView baseImage2;
	 	   	ImageView middleImage2 = new ImageView(baoshims[animalindex.get(name)]);	
	 	   	
	        if (d.upf==1) {
	        	g2.setRotate(180);
	        	baseImage2= new ImageView(baoshibs[6]);
	        }
	        else baseImage2 = new ImageView(baoshibs[0]);
	        baseImage2.setSmooth(false);
	        middleImage2.setSmooth(false);
	        baseImage2.setFitHeight(346);
	        baseImage2.setFitWidth(346);
	        middleImage2.setFitHeight(346);
	        middleImage2.setFitWidth(346);
	        g2.getChildren().addAll(middleImage2,baseImage2);
	        int[][] dirs=null;
	        if((x+y)%2==0) dirs=new int[][]{ {1,0}, {0,-1}, {-1,0}};
	        else dirs=new int[][]{{0,1}, {1,0}, {-1,0}};	 
	        for (int[] dir : dirs) {
	            int nx = x + dir[0];
	            int ny = y + dir[1];
	            if (nx >= 0 && nx < MAPWIDTH && ny >= 0 && ny < MAPHEIGHT) {
	                if (db[ny][nx] instanceof define) {
	                    define neighbor = (define) db[ny][nx];
	                    d.connect(neighbor);
	                }
	            }
	        }
			return d;
    	}
    	return null;
    }
    
   
    public void inibaoshi(String name) {
    	Group g0=new Group();
    	g0.setEffect(blend);
        ImageView baseImage2;
 	   	ImageView middleImage2 = new ImageView(baoshims[1]);
        baseImage2 = new ImageView(baoshibs[0]);
        baseImage2.setSmooth(false);
        middleImage2.setSmooth(false);
        baseImage2.setFitHeight(346);
        baseImage2.setFitWidth(346);
        middleImage2.setFitHeight(346);
        middleImage2.setFitWidth(346);
        g0.getChildren().addAll(middleImage2,baseImage2);
    
        
        SnapshotParameters sn1 = new SnapshotParameters();
		sn1.setFill(Color.TRANSPARENT);
        ImageView g2=new ImageView(g0.snapshot(sn1,null));
        g2.setSmooth(false);
        g2.setEffect(ds1);
        double iniscale=scale;
        g2.setScaleX(iniscale);
        g2.setScaleY(iniscale);
        g2.setTranslateX(50*scalemubanl);
        g2.setTranslateY(100*scalemubanl);
        mubanleft.getChildren().add(g2);
        g2.setPickOnBounds(false);
        g2.setOnMouseDragged(e->{
			if(g0.intersects(zhengjin.getBoundsInParent())) {
				fpsText.setText("ssss");
			}
			else {
				fpsText.setText("a");
			}
        	nowmousex=e.getScreenX();
        	nowmousey=e.getScreenY()+30;
        	double px=173,py=173;
        	double sx=e.getSceneX()-mubanleft.getTranslateX()-px,sy=e.getSceneY()-py;
        	
        	if((rightmubanf&&e.getScreenX()<screenwidth-300 * scalemubanl||!rightmubanf&&e.getScreenX()<screenwidth-90 * scalemubanl)&&
        			(leftmubanf&&e.getScreenX()>225 * scalemubanl||!leftmubanf&&e.getScreenX()>65 * scalemubanl)) {
        		if(canmakecloneblend) {  
        			g2.setOpacity(0.01);
        			canmakecloneblend=false;
        			Group g1=new Group();
        	        g1.setEffect(blend);
        	        g1.getChildren().addAll(middleImage2,baseImage2);
        			nowgrab=g1;
        			mubanleft.getChildren().add(nowgrab);
                	nowgrab.setScaleX(scale);
                	nowgrab.setScaleY(scale);
                	nowgrab.setMouseTransparent(true);
        		}
        		if(nowgrab!=null) {
        			if (gpane != null) {
        			    Point2D localPoint = gpane.sceneToLocal(e.getSceneX(), e.getSceneY());
        			    int gridX0 = (int) (localPoint.getX() / 150);
        			    int gridX1 = (int) (localPoint.getX() % 150);
        			    int gridY = (int) (localPoint.getY() / 260);
        			    int gridY1 = (int) (localPoint.getY() % 260);
        			    int off=(gridX0+gridY)%2==1?(gridX1>(260-gridY1)/Math.sqrt(3)?0:-1):(gridX1>gridY1/Math.sqrt(3)?0:-1);
        			    int gridX=gridX0+off;
        			    if(gridX<0||gridY<0||gridX>=MAPWIDTH||gridY>=MAPHEIGHT)canputf=false;
        			    else canputf=true;
        			    gridX = Math.max(0, Math.min(gridX, MAPWIDTH - 1));
        			    gridY = Math.max(0, Math.min(gridY, MAPHEIGHT - 1));
        			    ImageView targetView = gridv[gridY][gridX]; // 注意行列顺序
        			    targetView.setSmooth(false);
        			    if (targetView != null) {
							MouseEvent simulatedEvent = new MouseEvent(MouseEvent.MOUSE_ENTERED, localPoint.getX(),
									localPoint.getY(), e.getScreenX(), e.getScreenY(), MouseButton.NONE, 0,
									e.isShiftDown(), e.isControlDown(), e.isAltDown(), e.isMetaDown(), false, false,
									false, false, false, false, null);
        			        Platform.runLater(() -> {
        			            targetView.fireEvent(simulatedEvent);
        			        });
        			    }
        			}
        			nowgrab.setTranslateX(e.getSceneX()-mubanleft.getTranslateX()-px);
        			nowgrab.setTranslateY(e.getSceneY()-py-30);
        		}	
        	}
        	else if(rightmubanf&&e.getScreenX()>screenwidth-300 * scalemubanl||!rightmubanf&&e.getScreenX()>screenwidth-90 * scalemubanl) {
        		hovergridx=-1;
            	hovergridy=-1;
        	}
        	else {
        		hovergridx=-1;
            	hovergridy=-1;
            	g0.setEffect(blend);
                g2.setEffect(ds1);
        		g2.setOpacity(1);
        		canmakecloneblend=true;
            	mubanleft.getChildren().remove(nowgrab);
        		g2.setTranslateX(Math.max(sx,-211*(1-iniscale)));
            	g2.setTranslateY(sy-30);
        	}
        });
        g2.setOnMouseReleased(e->{
        	g2.setOpacity(1);
        	if(canputf) {
        		canputf=false;
        		makebaoshi(hovergridx,hovergridy,name);
        	}
        	canmakecloneblend=true;
        	mubanleft.getChildren().remove(nowgrab);
        });
        g2.setOnMouseClicked(e->{
        	Group g1=new Group();
	        g1.setEffect(blend);
	        g1.getChildren().addAll(middleImage2,baseImage2);
			nowgrab=g1;
//        	game.getChildren().add(g21);
        	nowgrab.setScaleX(scale);
        	nowgrab.setScaleY(scale);
        	nowgrab.setMouseTransparent(true);
        	double px=211-110*scale,py=190+100*scale;
        	nowgrab.setTranslateX(e.getSceneX()-mubanleft.getTranslateX()-px);
        	nowgrab.setTranslateY(e.getSceneY()-py);
        });
    }
   
    public static void setBordersToAllPanes(Parent root) {
        // 遍历所有子节点
        for (Node node : root.getChildrenUnmodifiable()) {
            if (node instanceof Pane) {
                applyBorder((Pane) node);
            }
            // 递归处理子节点
            if (node instanceof Parent) {
                setBordersToAllPanes((Parent) node);
            }
        }
    }
    
    private static void applyBorder(Pane pane) {
        // 定义边框样式
        BorderStroke stroke = new BorderStroke(
            Color.RED,                   // 颜色
            BorderStrokeStyle.SOLID,       // 样式（SOLID/DASHED/DOTTED）
            CornerRadii.EMPTY,             // 圆角
            new BorderWidths(10)            // 宽度（左/右/上/下）
        );
        pane.setBorder(new Border(stroke));
    }
    // 递归遍历所有子节点
    public void optimizeScene(Scene scene) {
        optimizeNode(scene.getRoot(), scene.getWindow());
    }
   
    private void optimizeNode(Node node, Window ownerWindow) {
        // 跳过非渲染节点和不可见节点
        // 计算屏幕坐标系下的节点边界
        Bounds screenBounds = node.localToScreen(node.getBoundsInLocal());
        // 判断是否完全在屏幕外
        if (isCompletelyOffscreen(screenBounds)) {
            node.setVisible(false);
            return; // 隐藏后无需处理子节点
        } 
        else node.setVisible(true);
        // 递归处理子节点（使用BFS避免栈溢出）
        try {
	        for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
	            optimizeNode(child, ownerWindow);
	        }
        }catch(Exception e) {}
    }
    public void optimizeNode(Node node) {
    	 Bounds screenBounds = node.localToScreen(node.getBoundsInLocal());
         // 判断是否完全在屏幕外
//    	 System.out.println(node.getClass());
    	 String name=node.toString();
         if (isCompletelyOffscreen(screenBounds)){
//        	 System.out.print(node.toString());
        	 if(offscreenTimestamps.containsKey(name)) {//2秒隐藏
        		 if(System.currentTimeMillis()-offscreenTimestamps.get(name)>4000) {
        			 node.setVisible(false);
        			 return; // 隐藏后无需处理子节点
        		 }
        	 }
        	 else offscreenTimestamps.put(name, System.currentTimeMillis());
         } 
         else {
        	 if (offscreenTimestamps.remove(name) != null) {
        		 node.setVisible(true);
        	 } 
         }
         // 递归处理子节点（使用BFS避免栈溢出）
         try {
 	        for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
 	            optimizeNode(child);
 	        }
         }catch(Exception e) {}
    }
    
    // 判断是否完全在屏幕外（包含所有屏幕）
    private boolean isCompletelyOffscreen(Bounds nodeBounds) {
        // 获取所有屏幕的可见区域
    	double 
           x0= nodeBounds.getMinX(),
           y0= nodeBounds.getMinY(), 
           x1= nodeBounds.getWidth(), 
           y1= nodeBounds.getHeight();
        if(x0>screenwidth||x0+x1<0||y0>screenheight|y0+y1<0) return true;
        return false;   
    }
    private void setFixedGridSize(GridPane grid, int rows, int cols, 
            double cellWidth, double cellHeight) {
		// 设置列约束
		for (int i = 0; i < cols; i++) {
		ColumnConstraints column = new ColumnConstraints();
		column.setPrefWidth(cellWidth);
		column.setMinWidth(cellWidth);
		column.setMaxWidth(cellWidth);
		column.setHgrow(Priority.NEVER); // 禁止水平拉伸
		grid.getColumnConstraints().add(column);
		}
		
		// 设置行约束
		for (int i = 0; i < rows; i++) {
		RowConstraints row = new RowConstraints();
		row.setPrefHeight(cellHeight);
		row.setMinHeight(cellHeight);
		row.setMaxHeight(cellHeight);
		row.setVgrow(Priority.NEVER); // 禁止垂直拉伸
		grid.getRowConstraints().add(row);
		}	
		// 可选：设置网格间距
//		grid.setHgap(5);
//		grid.setVgap(5);
	}
    //点与线段判断碰撞
    static class Point {
        double x, y;
        Point(double x, double y) {this.x = x;this.y = y;}
    }
    static class Segment {
        Point start, end;
        Segment(Point start, Point end) {this.start = start;this.end = end;}
    }
    private static boolean isOverlap(Segment s1, Segment s2) {
        return Math.max(s1.start.x, s1.end.x) >= Math.min(s2.start.x, s2.end.x) &&
               Math.max(s2.start.x, s2.end.x) >= Math.min(s1.start.x, s1.end.x) &&
               Math.max(s1.start.y, s1.end.y) >= Math.min(s2.start.y, s2.end.y) &&
               Math.max(s2.start.y, s2.end.y) >= Math.min(s1.start.y, s1.end.y);
    }
    private static double crossProduct(Point o, Point a, Point b) {
        return (a.x - o.x) * (b.y - o.y) - (a.y - o.y) * (b.x - o.x);
    }
    private static boolean isCrossing(Segment s1, Segment s2) {
        double c1 = crossProduct(s1.start, s1.end, s2.start);
        double c2 = crossProduct(s1.start, s1.end, s2.end);
        double c3 = crossProduct(s2.start, s2.end, s1.start);
        double c4 = crossProduct(s2.start, s2.end, s1.end);
        return (c1 * c2 <= 1e-8) && (c3 * c4 <= 1e-8);
    }
    private static boolean isPointOnSegment(Point p, Segment s) {
        if (p.x < Math.min(s.start.x, s.end.x) - 1e-8 || 
            p.x > Math.max(s.start.x, s.end.x) + 1e-8 ||
            p.y < Math.min(s.start.y, s.end.y) - 1e-8 || 
            p.y > Math.max(s.start.y, s.end.y) + 1e-8) {
            return false;
        }
        double t = ((p.x - s.start.x) * (s.end.x - s.start.x) + 
                   (p.y - s.start.y) * (s.end.y - s.start.y)) / 
                  (Math.pow(s.end.x - s.start.x, 2) + Math.pow(s.end.y - s.start.y, 2));
        return t >= -1e-8 && t <= 1 + 1e-8;
    }

    // 对外接口方法
    public static boolean isIntersect(Segment s1, Segment s2) {
        if (!isOverlap(s1, s2)) return false;
        if (isCrossing(s1, s2)) return true;
        
        return isPointOnSegment(s2.start, s1) || isPointOnSegment(s2.end, s1) ||
               isPointOnSegment(s1.start, s2) || isPointOnSegment(s1.end, s2);
    }

    public static boolean doShapesIntersect(List<Segment> shape1, List<Segment> shape2) {
        for (Segment s1 : shape1) {
            for (Segment s2 : shape2) {
                if (isIntersect(s1, s2)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void main(String[] args) {
//    	// 禁用自动脉冲（需谨慎，可能导致界面无响应）
//    	System.setProperty("javafx.pulseLogger", "true");
//    	System.setProperty("javafx.pulseInterval", "0");
    	System.setProperty("prism.vsync", "true");
//    	System.setProperty("prism.forceGPU", "true");
    	System.setProperty("prism.order", "GPU");
    	    launch(args);
    }
}
