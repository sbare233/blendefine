package blendefine;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Random;

public class RotatingLayersFX extends Application {
    private static final int LAYERS = 17;
    private static final int LEAF = 6;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    
    private final Random random = new Random();
    private final PerlinNoise noise = new PerlinNoise();
    private long startTime;
    private double rotationX =2; // X轴旋转角度
    private double rotationSpeed = 0.5; // 旋转速度


    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(3);
//        gc.setFill(Color.rgb(0, 0, 0, 0.2));
//        gc.fillRect(0, 0, WIDTH, HEIGHT);
        startTime = System.currentTimeMillis();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
            	
                // 半透明背景实现拖尾效果
                gc.setFill(Color.rgb(0, 0, 0, 0.1));
                gc.fillRect(0, 0, WIDTH, HEIGHT);
                // 在动画循环中更新旋转角度

                double currentTime = (System.currentTimeMillis() - startTime) / 1000.0;
                rotationX = Math.sin(currentTime * 0.1) * Math.PI ; // 在±45度之间变化
//                rotationX += rotationSpeed * currentTime * 0.1; // 持续旋转
                for (int layer = 0; layer < LAYERS; layer++) {
                    // 关键修正：每个图层有不同的旋转速度 (layer * timer)
                    double timer = (currentTime + 2.87); 
                    double rotation = (Math.PI / LEAF) * timer * layer*10;  // 使用layer索引
                    
                    // 计算大小
                    double size = Math.min(WIDTH / 2.0, HEIGHT / 2.0);
                    double perlin = Math.pow(noise.eval(currentTime * 0.0017 + layer * 0.2, 0), 3.4);
                    double radius = size * (1 - (layer / (double) LAYERS));
                    
                    // 计算颜色
                    double epilepsy = noise.eval(currentTime * 0.0002, 0) * 400;
                    double perlin2 = noise.eval(currentTime * 0.00113 + layer * 0.07 + 10, 0) * epilepsy % 100;
                    
//                    // 描边色 (HSB)
//                    gc.setStroke(Color.hsb(
//                        perlin2,
//                        clamp(gaussian(20, 40) + noise.eval(currentTime * 0.01, 0) * 20, 0, 100) / 100.0,
//                        clamp(gaussian(100, 1), 0, 100) / 100.0,
//                        0.9
//                    ));
                    
                    // 描边色 (HSB)
                    gc.setStroke(Color.hsb(
                    	clamp(layer*17,0,255),
                    	1,
                    	1,
                        0.9
                    ));
                    
                    // 填充色
                    gc.setFill(Color.hsb(
                        100 - perlin2,
                        clamp(noise.eval(currentTime * 0.01 % 1, 0) * 20, 0, 100) / 100.0,
                        clamp(gaussian(100, 1), 0, 100) / 100.0,
                        0.84
                    ));
                    
                    for (int leafIndex = 0; leafIndex < LEAF; leafIndex++) {
                        // 计算叶子的位置（保留原始公式）
                        double leafTime = currentTime * 0.01;
                        double n1 = noise.eval(leafTime, 0);
                        double n2 = noise.eval(currentTime * 0.0001, 0);
                        
                        // 修正：使用叶子索引leafIndex而非图层索引
//                        System.out.println(rotationX);
                        double angleCos = (2 * Math.PI * (1.2 * leafIndex + 0.1 * n1) -2*rotationX)/ LEAF + rotation/ (LEAF-leafIndex/10);
                        double angleSin = (2 * Math.PI * ((1 + n2 / 2) * leafIndex) +2*rotationX)/ LEAF + rotation/ (LEAF-leafIndex/10);
                        
                        double c = Math.cos(angleCos) / 1.4;
                        double d = Math.sin(angleSin) / (1.2 * leafIndex + 0.1 * n1);
                        
                        double rectSize = radius * perlin / 3+(LAYERS-layer)/2;
                        double x = WIDTH / 2 + c * radius - rectSize / 2;
                        double y = HEIGHT / 2 + d * radius - rectSize / 2;
                        
//                        gc.fillRect(x, y, rectSize, rectSize);
                        gc.fillOval(x, y, rectSize, rectSize);
//                        gc.setEffect(new GaussianBlur());
                        gc.strokeOval(x, y, rectSize, rectSize);
                    }
                }
            }
        }.start();
        StackPane st=new StackPane(canvas);
        st.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
        stage.setScene(new Scene(st, WIDTH, HEIGHT));
        stage.setTitle("Rotating Layers with Different Speeds");
        stage.show();
    }

    // 高斯随机数生成
    private double gaussian(double mean, double sd) {
        return mean + random.nextGaussian() * sd;
    }
    
    // 值范围限制
    private double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
 // 简化版Perlin噪声实现
    class PerlinNoise {
        private final Random random = new Random();
        private final int[] permutation = new int[512];

        public PerlinNoise() {
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
    public static void main(String[] args) {
        launch(args);
    }
}
