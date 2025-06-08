package blendefine;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Random;

public class ElasticRotationFX extends Application {
    private static final int LAYERS = 13;
    private static final int LEAF = 5;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private static final int time = 2700;
    private final Random random = new Random();
    private final PerlinNoise1 noise = new PerlinNoise1();
    private long startTime;
    private double bghue = 0;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(3);
        
        startTime = System.currentTimeMillis();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // 计算弹性缓动值
                long millis = System.currentTimeMillis() - startTime;
                double t =(double) (millis % time) /(double)time;
                double bounce = Math.sqrt(easeOutElastic(t));
                
                // 设置黑色背景
                gc.setFill(Color.color(0,0,0,0.1));
//                gc.clearRect(0, 0, WIDTH, HEIGHT);
//                gc.setFill(Color.color(0,0,0,0));
                gc.fillRect(0, 0, WIDTH, HEIGHT);
                
                for (int layer = 0; layer < LAYERS; layer++) {
                    // 计算时间参数
                    long tMillis = System.currentTimeMillis() - startTime;
                    double mod = (tMillis % 1000) / 1000.0;
                    double denominator = mod * 1000 + 300;
                    double timer = tMillis / denominator * 0.1;
                    
                    // 计算旋转角度
                    double rotation = (Math.PI / LEAF) * timer * layer * 2;
                    
                    // 计算大小
                    double size = Math.min(WIDTH / 4.0, HEIGHT / 4.0);
                    double perlin = Math.pow(noise.eval(millis / 600.0 + layer * 0.2, 0), 2.4);
                    double radius = size * (1 - (layer / (double) LAYERS)) * 1.5;
                    
                    // 计算颜色
                    double epilepsy = noise.eval(millis / 5000.0, 0) * 400;
                    double perlin2 = noise.eval(millis / 888.0 + layer * 0.07 + 10000, 0) * epilepsy % 100;
                    
                    // 设置描边色 (HSB)
//                    gc.setStroke(Color.hsb(
//                        perlin2 / 5 + 20,
//                        clamp(gaussian(60, 3) + noise.eval(millis/100.0, 0) * 20, 0, 100) / 100.0,
//                        clamp(gaussian(100, 1), 0, 100) / 100.0,
//                        0.2
//                    ));
                    gc.setStroke(Color.hsb(
                        	clamp(layer*17,0,255),
                        	1,
                        	1,
                            0.9
                        ));
                    
                    // 设置填充色
                    gc.setFill(Color.hsb(
                        perlin2,
                        clamp(100 + gaussian(60, 3) + noise.eval(millis/100.0, 0) * 20, 0, 100) / 100.0,
                        clamp(-100 - gaussian(100, 1), 0, 100) / 100.0,
                        0.2
                    ));
                    
                    for (int leafIndex = 0; leafIndex < LEAF; leafIndex++) {
                        // 计算矩形位置
                        double angle = 2 * Math.PI * leafIndex / LEAF + rotation;
                        double c = Math.cos(angle) * bounce;
                        double d = Math.sin(angle) * bounce;
                        
                        // 计算矩形大小
                        double rectSize = radius * perlin;
                        
                        // 计算矩形中心位置
                        double x = WIDTH / 2 + c * radius;
                        double y = HEIGHT / 2 + d * radius;
//                        gc.setFill(Color.WHITE);
//                        gc.fillText("佬", x - rectSize / 2, y + rectSize / 2);
                        // 绘制矩形（中心模式）
                        gc.fillRect(x - rectSize / 2, y - rectSize / 2, rectSize, rectSize);
                        gc.strokeRect(x - rectSize / 2, y - rectSize / 2, rectSize, rectSize);
//                        gc.setFont(Font.font(rectSize));
//                        
                    }
                }
                // 绘制标题和说明
                drawInfo(gc);
            }
        }.start();
        Scene scene=new Scene(new StackPane(canvas),WIDTH, HEIGHT);
        scene.setFill(null);
      	stage.initStyle(StageStyle.TRANSPARENT);
    	canvas.setOnMouseDragged(e->{
      		stage.setX(e.getScreenX()-stage.getWidth()/2);
      		stage.setY(e.getScreenY()-stage.getHeight()/2);
      	});
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.setTitle("Elastic Rotation Animation - JavaFX");
        stage.show();
    }
    
    // 弹性缓动函数
    private double easeOutElastic(double t) {
        double c4 = (2 * Math.PI) / 3;

        if (t == 0) return 0;
        if (t == 1) return 1;

        return Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * c4) + 1;
    }
    
    // 绘制信息文本
    private void drawInfo(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillText("Elastic Rotation Animation - JavaFX", 20, 30);
        
        gc.setFill(Color.rgb(200, 200, 255));
        gc.fillText("Layers: " + LAYERS + " | Leaves: " + LEAF, 20, 50);
        
        gc.setFill(Color.rgb(255, 200, 200));
        gc.fillText("Perlin Noise & Elastic Easing Function", 20, HEIGHT - 30);
    }
    
    // 高斯随机数生成
    private double gaussian(double mean, double sd) {
        return mean + random.nextGaussian() * sd;
    }
    
    // 值范围限制
    private double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// Perlin噪声实现
class PerlinNoise1 {
    private final Random random = new Random();
    private final int[] permutation = new int[512];

    public PerlinNoise1() {
        for (int i = 0; i < 256; i++) permutation[i] = i;
        // 洗牌算法
        for (int i = 0; i < 256; i++) {
            int j = random.nextInt(256);
            int temp = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = temp;
        }
        System.arraycopy(permutation, 0, permutation, 256, 256);
    }

    public double eval(double x, double y) {
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;
        x -= Math.floor(x);
        y -= Math.floor(y);
        double u = fade(x);
        double v = fade(y);
        
        int a = permutation[X] + Y;
        int b = permutation[X + 1] + Y;
        
        return lerp(v, lerp(u, grad(permutation[a], x, y),
                        grad(permutation[b], x - 1, y)),
                lerp(u, grad(permutation[a + 1], x, y - 1),
                        grad(permutation[b + 1], x - 1, y - 1)));
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private double grad(int hash, double x, double y) {
        int h = hash & 3;
        double u = h < 2 ? x : y;
        double v = h < 2 ? y : x;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
}