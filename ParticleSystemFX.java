package blendefine;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSystemFX extends Application {
    private static final int N = 10000; // 粒子数量（根据性能调整）
    private static final int WIDTH = 900;
    private static final int HEIGHT = 900;
    
    private double b = 0.19;
    private double time = 0;
    
    private List<Particle> particles = new ArrayList<>();
    private Canvas canvas;
    private GraphicsContext gc;
    private Random random = new Random();
    
    // 粒子类
    class Particle {
        double x, y, z;
        double lastX, lastY, lastZ;
        
        Particle(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.lastX = x;
            this.lastY = y;
            this.lastZ = z;
        }
        
        void update() {
            // 保存上一帧位置
            lastX = x;
            lastY = y;
            lastZ = z;
            
            // 应用12次迭代更新
            for (int i = 0; i < 12; i++) {
                double dx = (Math.sin(y) - b * x) / 128.0;
                double dy = (Math.sin(z) - b * y) / 128.0;
                double dz = (Math.sin(x) - b * z) / 128.0;
                
                x += dx;
                y += dy;
                z += dz;
            }
        }
    }

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(WIDTH, HEIGHT);//新建画布
        gc = canvas.getGraphicsContext2D();//获取绘制器
        StackPane root = new StackPane(canvas);//放画布的盘子
        Scene scene = new Scene(root, WIDTH, HEIGHT);//放盘子的场景
        scene.setFill(null);//场景设置透明
        stage.setAlwaysOnTop(true);//窗体设置置顶
      	stage.initStyle(StageStyle.TRANSPARENT);//窗体设置透明
      	canvas.setOnMouseDragged(e->{
      		stage.setX(e.getScreenX()-stage.getWidth()/2);
      		stage.setY(e.getScreenY()-stage.getHeight()/2);
      	});
        // 初始化粒子
        initializeParticles();
        
        // 设置动画循环
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
                time += 0.016; // 假设60fps
            }
        }.start();
        
        stage.setTitle("3D Particle System - JavaFX");
        stage.setScene(scene);
        stage.show();
    }
    
    private void initializeParticles() {
        particles.clear();
        for (int i = 0; i < N; i++) {
            double x = random.nextDouble() * 2 - 1;
            double y = random.nextDouble() * 2 - 1;
            double z = 2.0 * i / N - 1;
            particles.add(new Particle(x, y, z));
        }
    }
    
    private void update() {
        // 更新b值
        b = 0.14 + 0.06 * Math.sin(2 * Math.PI * time / 37.018);
        
        // 更新粒子位置
        for (Particle p : particles) {
            p.update();
        }
    }
    
    private void render() {
        // 半透明黑色背景（创建拖尾效果）
//        gc.setFill(Color.rgb(0, 0, 0, 0.15));
        gc.clearRect(0, 0, WIDTH, HEIGHT);
//        gc.fillRect(0, 0, WIDTH, HEIGHT);
    	gc.setFill(Color.rgb(0, 0, 0, 0));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        
        // 设置3D投影参数
        double cameraZ = -3.0;
        double fov = Math.PI / 3.0; // 视野
        double aspectRatio = (double) WIDTH / HEIGHT;
        double scale = 1.6 * b;
        
        // 应用旋转（绕X轴0.5弧度，绕Y轴随时间旋转）
        double rotationX = 0.5;
        double rotationY = 2 * Math.PI * 0.05 * time;
        double cosY = Math.cos(rotationY);
        double sinY = Math.sin(rotationY);
        double cosX = Math.cos(rotationX);
        double sinX = Math.sin(rotationX);
        
        // 绘制所有粒子轨迹
        for (Particle p : particles) {
            // 将当前位置和上一帧位置转换为屏幕坐标
            double[] screenPos = projectToScreen(p.x * scale, p.y * scale, p.z * scale, 
                                                cameraZ, fov, aspectRatio, cosX, sinX, cosY, sinY);
            double[] lastScreenPos = projectToScreen(p.lastX * scale, p.lastY * scale, p.lastZ * scale, 
                                                    cameraZ, fov, aspectRatio, cosX, sinX, cosY, sinY);
            
            // 计算颜色（基于粒子索引和深度）
            double hue = (double) particles.indexOf(p) / N * 360.0;
            double saturation = 0.7;
            double zFactor = Math.max(0, Math.min(1, (4.5 + p.z) / 2.5));
            double brightness = Math.pow(zFactor, 1.0);
            
            // 设置线条颜色
            gc.setStroke(Color.hsb(hue, saturation, brightness));
            
            // 绘制从上一帧位置到当前位置的线段
            gc.strokeLine(lastScreenPos[0], lastScreenPos[1], screenPos[0], screenPos[1]);
        }
        
        // 绘制信息
        drawInfo();
    }
    
    private double[] projectToScreen(double x, double y, double z, 
                                    double cameraZ, double fov, double aspectRatio,
                                    double cosX, double sinX, double cosY, double sinY) {
        // 应用旋转（先绕Y轴，再绕X轴）
        double x1 = x * cosY - z * sinY;
        double z1 = x * sinY + z * cosY;
        double y1 = y;
        
        double y2 = y1 * cosX - z1 * sinX;
        double z2 = y1 * sinX + z1 * cosX;
        
        // 应用透视投影
        double factor = fov / (cameraZ - z2);
        double screenX = WIDTH / 2 + x1 * factor * aspectRatio * WIDTH / 2;
        double screenY = HEIGHT / 2 + y2 * factor * HEIGHT / 2;
        
        return new double[]{screenX, screenY};
    }
    
    private void drawInfo() {
        gc.setFill(Color.WHITE);
        gc.fillText("Particles: " + N, 20, 20);
        gc.fillText("b value: " + String.format("%.4f", b), 20, 40);
        gc.fillText("Time: " + String.format("%.2f", time), 20, 60);
        
        gc.setFill(Color.rgb(200, 200, 255));
        gc.fillText("Press R to reset particles", WIDTH - 200, 20);
    }

    public static void main(String[] args) {
        launch(args);
    }
}