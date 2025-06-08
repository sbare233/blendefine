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

public class CircularBoundaryParticleSystem extends Application {
    private static final int NUM_PARTICLES = 600; // 粒子数量
    private static final int WIDTH = 900;
    private static final int HEIGHT = 700;
    private static final double MAX_SPEED = 2.5; // 最大速度
    private static final double MIN_SPEED = 0.5; // 最小速度
    private static final double CIRCLE_RADIUS = Math.min(WIDTH, HEIGHT) * 0.4; // 圆形边界半径
    
    private List<Particle> particles = new ArrayList<>();
    private Canvas canvas;
    private GraphicsContext gc;
    private Random random = new Random();
    private double time = 0;
    private double globalHue = 0;

    // 粒子类
    class Particle {
        double x, y; // 位置
        double vx, vy; // 速度
        double size; // 大小
        double originalSize; // 原始大小
        double hue; // 颜色色调
        double life; // 生命周期
        double maxLife; // 最大生命周期
        
        Particle() {
            reset();
        }
        
        void reset() {
            // 从中心区域开始
            double angle = random.nextDouble() * 2 * Math.PI;
            double distance = random.nextDouble() * CIRCLE_RADIUS * 0.5;
            
            x = WIDTH / 2 + Math.cos(angle) * distance;
            y = HEIGHT / 2 + Math.sin(angle) * distance;
            
            // 随机速度方向
            angle = random.nextDouble() * 2 * Math.PI;
            double speed = MIN_SPEED + random.nextDouble() * (MAX_SPEED - MIN_SPEED);
            vx = Math.cos(angle) * speed;
            vy = Math.sin(angle) * speed;
            
            originalSize = 3 + random.nextDouble() * 8;
            size = originalSize;
            hue = random.nextDouble() * 360;
            life = 0;
            maxLife = 100 + random.nextDouble() * 400;
        }
        
        void update() {
            // 更新位置
            x += vx;
            y += vy;
            
            // 计算到中心的距离
            double centerX = WIDTH / 2;
            double centerY = HEIGHT / 2;
            double dx = x - centerX;
            double dy = y - centerY;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            // 超出圆形边界时逐渐变小
            if (distance > CIRCLE_RADIUS) {
                // 计算超出边界的比例 (0-1)
                double exceedRatio = (distance - CIRCLE_RADIUS) / (CIRCLE_RADIUS * 0.5);
                exceedRatio = Math.min(1, Math.max(0, exceedRatio));
                
                // 粒子大小随超出比例减小
                size = originalSize * (1 - exceedRatio);
                
                // 如果完全超出边界，重置粒子
                if (exceedRatio >= 1) {
                    reset();
                    return;
                }
            } else {
                // 在边界内时恢复原始大小
                size = originalSize;
            }
            
            // 添加一点随机扰动
            vx += (random.nextDouble() - 0.5) * 0.2;
            vy += (random.nextDouble() - 0.5) * 0.2;
            
            // 速度限制
            double speed = Math.sqrt(vx * vx + vy * vy);
            if (speed > MAX_SPEED) {
                vx = vx * MAX_SPEED / speed;
                vy = vy * MAX_SPEED / speed;
            }
            
            // 更新生命周期
            life++;
            if (life > maxLife) {
                reset();
            }
            
            // 随机改变大小（在边界内）
            if (distance < CIRCLE_RADIUS) {
                size += (random.nextDouble() - 0.5) * 0.1;
                size = Math.max(1, Math.min(originalSize * 1.5, size));
            }
        }
        
        void draw() {
            // 计算颜色（基于生命周期和全局色调）
            double ageFactor = 1 - (life / maxLife);
            double saturation = 0.6 + 0.4 * Math.sin(time * 0.5);
            double brightness = 0.7 + 0.3 * Math.cos(time * 0.7);
            
            // 动态颜色混合
            double mixedHue = (hue + globalHue) % 360;
            
            // 计算到中心的距离（用于透明度）
            double centerX = WIDTH / 2;
            double centerY = HEIGHT / 2;
            double dx = x - centerX;
            double dy = y - centerY;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            // 距离中心越远，透明度越低
            double distanceAlpha = 1.0 - Math.min(1, Math.max(0, (distance - CIRCLE_RADIUS * 0.8) / (CIRCLE_RADIUS * 0.2)));
            
            Color color = Color.hsb(mixedHue, saturation, brightness, ageFactor * distanceAlpha * 0.8);
            gc.setFill(color);
            
            // 绘制粒子
            gc.fillOval(x - size/2, y - size/2, size, size);
            
            // 绘制粒子轨迹（仅在边界内）
            if (random.nextDouble() < 0.3 && distance < CIRCLE_RADIUS) {
                double trailSize = size * 0.6;
                gc.setFill(Color.hsb(mixedHue, saturation * 0.8, brightness * 0.9, ageFactor * distanceAlpha * 0.3));
                gc.fillOval(x - vx - trailSize/2, y - vy - trailSize/2, trailSize, trailSize);
            }
        }
    }

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(null);
      	stage.initStyle(StageStyle.TRANSPARENT);
      	stage.setAlwaysOnTop(true);
        // 初始化粒子
        for (int i = 0; i < NUM_PARTICLES; i++) {
            particles.add(new Particle());
        }
    	canvas.setOnMouseDragged(e->{
      		stage.setX(e.getScreenX()-stage.getWidth()/2);
      		stage.setY(e.getScreenY()-stage.getHeight()/2);
      	});
        // 设置动画循环
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
                time += 0.016; // 假设60fps
            }
        }.start();
        
        stage.setTitle("Circular Boundary Particle System");
        stage.setScene(scene);
        stage.show();
    }
    
    private void update() {
        // 更新全局色调
        globalHue = (globalHue + 0.2) % 360;
        
        // 更新粒子
        for (Particle p : particles) {
            p.update();
        }
    }
    
    private void render() {
        // 深色背景
//        gc.setFill(Color.rgb(10, 5, 20));
    	gc.clearRect(0, 0, WIDTH, HEIGHT);
    	 gc.setFill(Color.rgb(0, 0, 0,0));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        
        // 绘制圆形边界（半透明）
//        gc.setStroke(Color.rgb(100, 100, 150, 0.3));
//        gc.setLineWidth(1);
//        gc.strokeOval(WIDTH/2 - CIRCLE_RADIUS, HEIGHT/2 - CIRCLE_RADIUS, 
//                     CIRCLE_RADIUS * 2, CIRCLE_RADIUS * 2);
        
        // 绘制所有粒子
        for (Particle p : particles) {
            p.draw();
        }
        
        // 绘制信息
        drawInfo();
    }
    
    private void drawInfo() {
        gc.setFill(Color.WHITE);
        gc.fillText("Circular Boundary Particle System", 20, 20);
        gc.fillText("Particles: " + NUM_PARTICLES, 20, 40);
        gc.fillText("Press R to reset", 20, HEIGHT - 20);
        
        // 绘制标题装饰
        double wave = Math.sin(time * 3) * 5;
        gc.setFill(Color.hsb((globalHue + 180) % 360, 0.8, 0.9));
        gc.fillText("BOUNDARY FADE EFFECT", WIDTH/2 - 120, 30 + wave);
    }

    public static void main(String[] args) {
        launch(args);
    }
}