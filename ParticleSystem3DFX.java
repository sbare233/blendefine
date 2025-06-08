package blendefine;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSystem3DFX extends Application {
    private static final int NUM_PARTICLES = 5000;
    private static final int WIDTH = 900;
    private static final int HEIGHT = 900;
    
    private double zoom = 0.0329;
    private Vector3D centerVector = new Vector3D(0.457, -1.382, 26.348);
    private Vector3D xProjVector = new Vector3D(-0.256, 0.589, 0.767);
    private Vector3D yProjVector;
    private Vector3D zProjVector = new Vector3D(-0.335, 0.690, -0.642);
    
    private boolean paused = false;
    private double scaleFactor = 1;
    private double dt = 0.3;
    
    // 系数矩阵
    private double[][][] coeffs = {
        {{0}, {-10, 10, 0}, {0, 0, 0, 0, 0, 0}},
        {{0}, {28, -1, 0}, {0, 0, -1, 0, 0, 0}},
        {{0}, {0, 0, -2.66}, {0, 1, 0, 0, 0, 0}}
    };
    
    private int[][] coeff_2o_guide = {{0,0}, {0,1}, {0,2}, {1,1}, {1,2}, {2,2}};
    
    private List<Particle> particles = new ArrayList<>();
    private Canvas canvas;
    private GraphicsContext gc;
    private long startTime;
    
    // 自定义3D向量类
    static class Vector3D {
        double x, y, z;
        
        Vector3D(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        Vector3D add(Vector3D other) {
            return new Vector3D(x + other.x, y + other.y, z + other.z);
        }
        
        Vector3D subtract(Vector3D other) {
            return new Vector3D(x - other.x, y - other.y, z - other.z);
        }
        
        Vector3D multiply(double scalar) {
            return new Vector3D(x * scalar, y * scalar, z * scalar);
        }
        
        Vector3D cross(Vector3D other) {
            return new Vector3D(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
            );
        }
        
        double dot(Vector3D other) {
            return x * other.x + y * other.y + z * other.z;
        }
        
        Vector3D normalize() {
            double mag = magnitude();
            if (mag > 0) {
                return new Vector3D(x / mag, y / mag, z / mag);
            }
            return this;
        }
        
        double magnitude() {
            return Math.sqrt(x * x + y * y + z * z);
        }
    }
    
    // 粒子类
    class Particle {
        double x, y, z;
        
        Particle(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        void render() {
            Vector3D posVec = new Vector3D(x, y, z).subtract(centerVector);
            
            double zVal = posVec.dot(zProjVector);
            double z_colorFactor = Math.max(-1, Math.min(1, zVal * zoom * 1.5));
            
            // 计算颜色（HSB转RGB）
            double hue = 255 * (1 - z_colorFactor) / 2;
            double saturation = 1.0;
            double brightness = 1.0;
            Color particleColor = Color.hsb(hue, saturation, brightness);
            gc.setFill(particleColor);
            
            // 计算投影位置
            double projX = (posVec.dot(xProjVector) * zoom )+ 1;
            double projY = (posVec.dot(yProjVector) * zoom )+ 1;
            double screenX = projX * WIDTH / 2;
            double screenY = projY * WIDTH / 2;
            
            // 计算粒子大小
            double size = WIDTH / 160 * (2 / (z_colorFactor + 2));
            
            // 绘制粒子
            gc.fillOval(screenX - size/2, screenY - size/2, size, size);
        }
        
        void update() {
            double[] xyz = {x * scaleFactor, y * scaleFactor, z * scaleFactor};
            double[] dxyz = {0, 0, 0};
            
            for (int i = 0; i < 3; i++) {
                // 常数项
                dxyz[i] += coeffs[i][0][0];
                
                // 一次项
                for (int j = 0; j < 3; j++) {
                    dxyz[i] += coeffs[i][1][j] * xyz[j];
                }
                
                // 二次项
                for (int j = 0; j < 6; j++) {
                    int idx1 = coeff_2o_guide[j][0];
                    int idx2 = coeff_2o_guide[j][1];
                    dxyz[i] += coeffs[i][2][j] * xyz[idx1] * xyz[idx2];
                }
            }
            
            // 计算导数向量长度
            Vector3D dVec = new Vector3D(dxyz[0], dxyz[1], dxyz[2]);
            double dxyz_mag = dVec.magnitude();
            
            // 更新位置
            if (dxyz_mag > 0) {
                this.x += dxyz[0] * dt / dxyz_mag;
                this.y += dxyz[1] * dt / dxyz_mag;
                this.z += dxyz[2] * dt / dxyz_mag;
            }
        }
    }

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        
        // 初始化投影向量
        yProjVector = xProjVector.cross(zProjVector).normalize();
        xProjVector = zProjVector.cross(yProjVector).normalize();
        
        // 初始化粒子
        reInit();
        
        // 设置键盘事件
        scene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));
        
        // 设置动画循环
        startTime = System.nanoTime();
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        }.start();
        
        stage.setTitle("3D Particle System - JavaFX");
        stage.setScene(scene);
        stage.show();
    }
    
    private void reInit() {
        particles.clear();
        Random rand = new Random();
        double ff = 12;
        for (int i = 0; i < NUM_PARTICLES; i++) {
            double x = (rand.nextDouble() * 2 - 1) * ff + 0.3;
            double y = (rand.nextDouble() * 2 - 1) * ff;
            double z = (rand.nextDouble() * 2 - 1) * ff + 1;
            particles.add(new Particle(x, y, z));
        }
    }
    
    private void update() {
        if (!paused) {
            for (Particle p : particles) {
                p.update();
            }
        }
    }
    
    private void render() {
        // 半透明黑色背景（创建拖尾效果）
        gc.setFill(Color.rgb(0, 0, 0, 0.15));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        
        // 绘制所有粒子
        for (Particle p : particles) {
            p.render();
        }
        
        // 绘制信息
        gc.setFill(Color.WHITE);
        gc.fillText("Particles: " + particles.size(), 20, 20);
        gc.fillText("Zoom: " + String.format("%.4f", zoom), 20, 40);
        gc.fillText("Controls: WASD - Move, RF - Zoom, ZX - Depth", 20, HEIGHT - 30);
        gc.fillText("Arrows - Rotate, QE - Roll, P - Pause", 20, HEIGHT - 10);
    }
    
    private void handleKeyPress(KeyCode code) {
        double movSpeed = 0.012 / zoom;
        double rotSpeed = 0.04;
        
        switch (code) {
            case W:
                centerVector = centerVector.add(yProjVector.multiply(movSpeed));
                break;
            case A:
                centerVector = centerVector.add(xProjVector.multiply(movSpeed));
                break;
            case S:
                centerVector = centerVector.add(yProjVector.multiply(-movSpeed));
                break;
            case D:
                centerVector = centerVector.add(xProjVector.multiply(-movSpeed));
                break;
            case R:
                zoom *= 1.04;
                break;
            case F:
                zoom *= 0.96;
                break;
            case Z:
                centerVector = centerVector.add(zProjVector.multiply(-movSpeed));
                break;
            case X:
                centerVector = centerVector.add(zProjVector.multiply(movSpeed));
                break;
            case LEFT:
                zProjVector = zProjVector.add(xProjVector.multiply(-rotSpeed));
                break;
            case RIGHT:
                zProjVector = zProjVector.add(xProjVector.multiply(rotSpeed));
                break;
            case UP:
                zProjVector = zProjVector.add(yProjVector.multiply(-rotSpeed));
                break;
            case DOWN:
                zProjVector = zProjVector.add(yProjVector.multiply(rotSpeed));
                break;
            case Q:
                Vector3D cross1 = xProjVector.cross(zProjVector);
                xProjVector = xProjVector.add(cross1.multiply(rotSpeed));
                break;
            case E:
                Vector3D cross2 = xProjVector.cross(zProjVector);
                xProjVector = xProjVector.add(cross2.multiply(-rotSpeed));
                break;
            case P:
                paused = !paused;
                break;
            case SPACE:
                printStuffs();
                break;
        }
        
        // 重新标准化向量
        zProjVector = zProjVector.normalize();
        xProjVector = xProjVector.normalize();
        yProjVector = xProjVector.cross(zProjVector).normalize();
        xProjVector = zProjVector.cross(yProjVector).normalize();
    }
    
    private void printStuffs() {
        System.out.println("Coeffs: " + formatCoeffs());
        System.out.println("xProjVector: " + xProjVector);
        System.out.println("zProjVector: " + zProjVector);
        System.out.println("centerVector: " + centerVector);
        System.out.println("Zoom: " + zoom);
    }
    
    private String formatCoeffs() {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < coeffs.length; i++) {
            sb.append("  [");
            for (int j = 0; j < coeffs[i].length; j++) {
                sb.append("[");
                for (int k = 0; k < coeffs[i][j].length; k++) {
                    sb.append(String.format("%.2f", coeffs[i][j][k]));
                    if (k < coeffs[i][j].length - 1) sb.append(", ");
                }
                sb.append("]");
                if (j < coeffs[i].length - 1) sb.append(", ");
            }
            sb.append("]");
            if (i < coeffs.length - 1) sb.append(",\n");
        }
        sb.append("\n]");
        return sb.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}