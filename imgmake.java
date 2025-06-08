package blendefine;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class imgmake extends Application {
//    private static final int BLOCK_SIZE = 2; // 像素块大小
    private ImageView processedView;
    private Image processedImage;
    static SnapshotParameters sn = new SnapshotParameters();
	private static final int[][] QUANTIZATION_TABLES = {
			{127},
			{127,255},//2
			{85,170,255},//3
			{63,127,191,255},//4
			{51,102,153,204,255},//5
			{43,86,129,172,215,255},//6
			{36,73,109,146,182,219,255},//7层量化表（255/7≈36.4）
			{32,64,96,128,160,192,224,255},// 8层量化表（255/8=31.875）
			{0,32,64,96,128,160,192,224,255},//9
			{0,26,52,78,104,130,156,182,208,255},//10
			{0,21,43,64,86,107,129,150,172,193,214,255},//12
			{0,16,32,48,64,80,96,112,128,144,160,176,192,208,224,255},//16
			{0,8,16,24,32,40,48,56,64,72,80,88,96,104,112,120,128,136,144,152,160,168,176,184,192,200,208,216,224,232,255}//32
	};

	private static final int layer=9;//色调分离层数
	
	// 颜色量化方法
	private static int quantize(int value, int levels) {
		int[] table = QUANTIZATION_TABLES[levels];
		for (int q : table) {
			if (value <= q)
				return q;
		}
		return 255;
	}

	@Override
    public void start(Stage stage) {
        Group root = new Group();
        
        // 创建目标节点（示例使用圆形）
        Circle targetNode = new Circle(100, Color.BLUE);
        root.getChildren().add(targetNode);

        // 初始化处理后的视图
        processedImage = new WritableImage(200, 200);
        processedView = new ImageView(processedImage);
        processedView.setTranslateX(200);
        processedView.setTranslateY(200);
        root.getChildren().add(processedView);

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.show();

        // 使用 AnimationTimer 实现定时更新
        AnimationTimer at1=new AnimationTimer() {
            private long lastUpdate = 0;
            
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 16_666_666) { // 约60FPS
                	targetNode.setFill(Color.color(Math.random(), 0, 0));
//                	processedImage=fastpixeler(targetNode);
                	processedView.setImage(processedImage);
                    lastUpdate = now;
                }
            }
        };
        at1.start();
    }
    public static Image sharpenimage(Image original) {
        // 1. 将JavaFX Image转为BufferedImage
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(original, null);
        
        // 2. 定义锐化卷积核
        float[] kernelData = {
            0, -0.25f, 0,
            -0.25f, 2, -0.25f,
            0, -0.25f, 0
        };
        Kernel kernel = new Kernel(3, 3, kernelData);
        
        // 3. 应用卷积操作
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage sharpenedImage = op.filter(bufferedImage, null);
        
        // 4. 转回JavaFX Image
        return SwingFXUtils.toFXImage(sharpenedImage, null);
    }
    public static Image fastpixeler(Image img) {
    	return fastpixeler(img,2);
    }
    public static Image fastpixeler(Image img,double BLOCK_SIZE) {
    	return fastpixeler(img,BLOCK_SIZE,1920,1080);
    }
    public static Image fastpixeler(Image img,double BLOCK_SIZE,double w,double h) {
    	BufferedImage originalImage = SwingFXUtils.fromFXImage(img, null);
    	int targetWidth = (int)(w/BLOCK_SIZE),targetHeight=(int)(h/BLOCK_SIZE);
    	BufferedImage scaledImage = new BufferedImage(targetWidth,targetHeight, BufferedImage.TYPE_INT_RGB);
            // 4. 配置高质量渲染参数
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            // 5. 执行缩放绘制
            g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
            g2d.dispose();
       
		return SwingFXUtils.toFXImage(scaledImage, null);
    }
    public static Image pixeler(Node node) {
    	return pixeler(node,2);
    }
    public static Image pixeler(Node node,int BLOCK_SIZE) {
    	return pixeler(node,BLOCK_SIZE,0,0);
    }
    public static Image pixeler(Node node, int BLOCK_SIZE, double w, double h) {
        // 获取节点快照
        SnapshotParameters sn = new SnapshotParameters();
        sn.setFill(Color.TRANSPARENT);
        if (w != 0 && h != 0)
            sn.setViewport(new Rectangle2D(0, 0, w, h));
        Image snapshot = node.snapshot(sn, null);

        int width = (int) snapshot.getWidth();
        int height = (int) snapshot.getHeight();
        WritableImage output = new WritableImage(width, height);
 
        // 读取像素到int数组
        PixelReader reader = snapshot.getPixelReader();
        int[] pixels = new int[width * height];
        reader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0, width);
 
        // 多线程处理配置
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int stripHeight = (height + numThreads - 1) / numThreads;
// System.out.println(numThreads);
        // 任务分解与执行
        List<Future<?>> futures = new ArrayList<>();
        for (int t = 0; t < numThreads; t++) {
            final int startY = t * stripHeight;
            final int endY = Math.min(startY + stripHeight, height);
            
            futures.add(executor.submit(() -> processStrip(
                pixels, width, height, BLOCK_SIZE, startY, endY
            )));
        }
 
        // 等待所有任务完成
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("像素处理中断", e);
            }
        }
 
        executor.shutdown();
 
        // 写回处理后的像素
        PixelWriter writer = output.getPixelWriter();
        writer.setPixels(0, 0, width, height, 
            PixelFormat.getIntArgbInstance(), pixels, 0, width);
 
        return output;
    }
    public static Image pixeler1(Image i, int BLOCK_SIZE) {
    	int width = (int) i.getWidth();
        int height = (int) i.getHeight();
        WritableImage output = new WritableImage(width, height);
 
        // 读取像素到int数组
        PixelReader reader = i.getPixelReader();
        int[] pixels = new int[width * height];
        reader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0, width);   
        processStrip(pixels, width, height, BLOCK_SIZE, 0, height);
        // 写回处理后的像素
        PixelWriter writer = output.getPixelWriter();
        writer.setPixels(0, 0, width, height, 
            PixelFormat.getIntArgbInstance(), pixels, 0, width);
        return output;
    }
    private static void processStrip(int[] pixels, int width, int height,
                                    int blockSize, int startY, int endY) {
        final int alphaThreshold = (int) (0.2 * 255); // 预计算透明度阈值
 
        for (int y = startY; y < endY; y += blockSize) {
            for (int x = 0; x < width; x += blockSize) {
                // 计算块边界
                int blockWidth = Math.min(blockSize, width - x);
                int blockHeight = Math.min(blockSize, endY - y);
                if (blockHeight <= 0) continue;
 
                // 获取参考像素
                int basePixel = pixels[y * width + x];
                int alpha = (basePixel >> 24) & 0xFF;
 
                // 透明度检查（直接操作ARGB值）
                if (alpha > alphaThreshold) {
                    int color = basePixel & 0x00FFFFFF; // 提取RGB分量
 
                    // 填充整个块
                    for (int dy = 0; dy < blockHeight; dy++) {
                        int py = y + dy;
                        if (py >= endY) break;
                        
                        int rowStart = py * width;
                        for (int dx = 0; dx < blockWidth; dx++) {
                            pixels[rowStart + x + dx] = (alpha << 24) | color;
                        }
                    }
                }
            }
        }
    }
    public static Image doudong(Image i, int BLOCK_SIZE) {
    	int width = (int) i.getWidth();
        int height = (int) i.getHeight();
        WritableImage output = new WritableImage(width, height);
 
        // 读取像素到int数组
        PixelReader reader = i.getPixelReader();
        int[] pixels = new int[width * height];
        reader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0, width);   
        processStrip1(pixels, width, height, BLOCK_SIZE, 0, height);
        // 写回处理后的像素
        PixelWriter writer = output.getPixelWriter();
        writer.setPixels(0, 0, width, height, 
            PixelFormat.getIntArgbInstance(), pixels, 0, width);
        return output;
    }
	private static void processStrip1(int[] pixels, int width, int height, int blockSize, int startY, int endY) {
		final int alphaThreshold = (int) (0.2 * 255); // 预计算透明度阈值

		for (int y = startY; y < endY; y += blockSize) {
			for (int x = 0; x < width; x += blockSize) {
// 计算块边界
				int blockWidth = Math.min(blockSize, width - x);
				int blockHeight = Math.min(blockSize, endY - y);
				if (blockHeight <= 0)
					continue;

// 获取参考像素
				int basePixel = pixels[y * width + x];
				int alpha = (basePixel >> 24) & 0xFF;

// 透明度检查（直接操作ARGB值）
				if (alpha > alphaThreshold) {
					int color = basePixel & 0x00FFFFFF; // 提取RGB分量

// 填充整个块
					for (int dy = 0; dy < blockHeight; dy++) {
						int py = y + dy;
						if (py >= endY)
							break;

						int rowStart = py * width;
						for (int dx = 0; dx < blockWidth-1; dx++) {
							pixels[rowStart + x + dx] = (alpha << 24) | color;
						}
						pixels[rowStart + x ] = 0x00000000;
					}
				}
			}
		}
	}
	   public static Image well(Image i, int BLOCK_SIZE) {
	    	int width = (int) i.getWidth();
	        int height = (int) i.getHeight();
	        WritableImage output = new WritableImage(width, height);
	 
	        // 读取像素到int数组
	        PixelReader reader = i.getPixelReader();
	        int[] pixels = new int[width * height];
	        reader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0, width);   
	        processStrip3(pixels, width, height, BLOCK_SIZE, 0, height);
//	        processStrip1(pixels, width, height, BLOCK_SIZE, 0, height);
	        // 写回处理后的像素
	        PixelWriter writer = output.getPixelWriter();
	        writer.setPixels(0, 0, width, height, 
	            PixelFormat.getIntArgbInstance(), pixels, 0, width);
	        return output;
	    }
	private static void processStrip3(int[] pixels, int width, int height, int blockSize, int startY, int endY) {
		final int alphaThreshold = (int) (0.2 * 255);
		for (int y = startY; y < endY; y += blockSize) {
			for (int x = 0; x < width; x += blockSize) {
				int blockWidth = Math.min(blockSize, width - x);
				int blockHeight = Math.min(blockSize, endY - y);
				if (blockHeight <= 0)
					continue;
				int basePixel = pixels[y * width + x];
				int alpha = (basePixel >> 24) & 0xFF;
				if (alpha > alphaThreshold) {
					int color = basePixel & 0x00FFFFFF;
					for (int dy = 0; dy < blockHeight; dy++) {
						int py = y + dy;
						if (py >= endY)
							break;
						int rowStart = py * width;
						for (int dx = 0; dx < blockWidth; dx++) {
							int pos = rowStart + x + dx;
							if (dy == blockHeight - 1||dy == blockHeight - 2) {
								// 最后一行使用变暗后的颜色
								int darkColor = darkenColor(color);
								pixels[pos] = (alpha << 24) | darkColor;
							} else {
								pixels[pos] = (alpha << 24) | color;
							}
						}
					}
				}
			}
		}
	}

//新增颜色变暗方法（通过降低RGB亮度实现）
	private static int darkenColor(int color) {
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;

// 通过乘法降低亮度（保留原始alpha通道）
		r = (int) (r * 0.6); // 调整系数可控制变暗程度（0.0-1.0）
		g = (int) (g * 0.6);
		b = (int) (b * 0.6);

// 确保颜色分量不低于0
		r = Math.max(r, 0);
		g = Math.max(g, 0);
		b = Math.max(b, 0);

		return (r << 16) | (g << 8) | b;
	}
    public static Image process(Image snapshot) {
    	return process(snapshot,layer,2);
    }
    public static Image process(Image snapshot, int blockSize) {
    	return process(snapshot,layer,blockSize);
    }
    public static Image process(Image snapshot,int layer, int blockSize) {
        // 2. 直接操作像素数组
        int width = (int) snapshot.getWidth();
        int height = (int) snapshot.getHeight();
        PixelReader reader = snapshot.getPixelReader();
        int[] pixels = new int[width * height];
        reader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0, width);
        
     // 新增：色调分离处理
        int[] colorSeparated = parallelColorSeparate(pixels, layer,width, height);
        
        // 4. 并行像素化处理
        int[] pixelated = parallelPixelate(colorSeparated, width, height, blockSize);
        
// 3. 并行锐化处理
//        int[] sharpened = parallelSharpen(colorSeparated, width, height);
// 
       
 
        // 5. 写回结果
        WritableImage output = new WritableImage(width, height);
        output.getPixelWriter().setPixels(0, 0, width, height,
            PixelFormat.getIntArgbInstance(), pixelated, 0, width);
 
        return output;
    }
    public static Image process1(Image snapshot,int blockSize) {
        // 2. 直接操作像素数组
        int width = (int) snapshot.getWidth();
        int height = (int) snapshot.getHeight();
        PixelReader reader = snapshot.getPixelReader();
        int[] pixels = new int[width * height];
        reader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0, width);
        // 4. 并行像素化处理
        int[] pixelated = parallelPixelate(pixels, width, height, blockSize);
        // 3. 并行锐化处理
        int[] sharpened = parallelSharpen(pixelated, width, height);
 
       
 
        // 5. 写回结果
        WritableImage output = new WritableImage(width, height);
        output.getPixelWriter().setPixels(0, 0, width, height,
            PixelFormat.getIntArgbInstance(), sharpened, 0, width);
 
        return output;
    }
    	
    	private static int[] parallelColorSeparate(int[] src, int levels,int width ,int height) {
    	    final int[] dest = new int[src.length];
    	    
    	    // 多线程配置
    	    int numThreads = Runtime.getRuntime().availableProcessors();
    	    ExecutorService pool = Executors.newFixedThreadPool(numThreads);
    	    int stripHeight = (height + numThreads - 1) / numThreads;

    	    List<Future<?>> tasks = new ArrayList<>();
    	    for (int t = 0; t < numThreads; t++) {
    	        final int startY = t * stripHeight;
    	        final int endY = Math.min(startY + stripHeight, height);

    	        tasks.add(pool.submit(() -> {
    	        	
    	            for (int y = startY; y < endY; y++) {
    	                for (int x = 0; x < width; x++) {
    	                    int idx = y * width + x;
    	                    int pixel = src[idx];
    	                    
    	                    // 分解颜色分量
    	                    int a = (pixel >> 24) & 0xFF;
    	                    int r = (pixel >> 16) & 0xFF;
    	                    int g = (pixel >> 8) & 0xFF;
    	                    int b = pixel & 0xFF;

    	                    // 量化处理
    	                    r = quantize(r, levels);
    	                    g = quantize(g, levels);
    	                    b = quantize(b, levels);

    	                    // 重新组合像素
    	                    dest[idx] = (a << 24) | (r << 16) | (g << 8) | b;
    	                }
    	            }
    	        }));
    	    }

    	    tasks.forEach(t -> {
    	        try { t.get(); } 
    	        catch (Exception e) { throw new RuntimeException("处理失败", e); }
    	    });
    	    pool.shutdown();
    	    return dest;
    	}
    	
    	
    // 并行锐化处理（优化版）
    private static int[] parallelSharpen(int[] src, int width, int height) {
        final float[] kernel = {0, -0.25f, 0, -0.25f, 2, -0.25f, 0, -0.25f, 0};
        final int[] dest = new int[src.length];
        final int kernelSize = 3;
        final int halfK = kernelSize / 2;
 
        // 多线程配置
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(numThreads);
        int stripHeight = (height + numThreads - 1) / numThreads;
 
        List<Future<?>> tasks = new ArrayList<>();
        for (int t = 0; t < numThreads; t++) {
            final int startY = t * stripHeight;
            final int endY = Math.min(startY + stripHeight, height);
 
            tasks.add(pool.submit(() -> {
                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < width; x++) {
                        float r = 0, g = 0, b = 0, a = 0;
                        // 卷积计算（优化内存访问模式）
                        for (int ky = -halfK; ky <= halfK; ky++) {
                            for (int kx = -halfK; kx <= halfK; kx++) {
                                int px = x + kx;
                                int py = y + ky;
                                if (px >= 0 && px < width && py >= 0 && py < height) {
                                    int idx = py * width + px;
                                    int pixel = src[idx];
                                    float weight = kernel[(ky + halfK) * kernelSize + (kx + halfK)];
                                    a += ((pixel >> 24) & 0xFF) * weight;
                                    r += ((pixel >> 16) & 0xFF) * weight;
                                    g += ((pixel >> 8) & 0xFF) * weight;
                                    b += (pixel & 0xFF) * weight;
                                }
                            }
                        }
                        // 颜色分量合并（带饱和度保护）
                        int outIdx = y * width + x;
                        dest[outIdx] = packPixel(
                            Math.round(clamp(a)),
                            Math.round(clamp(r)),
                            Math.round(clamp(g)),
                            Math.round(clamp(b))
                        );
                    }
                }
            }));
        }
 
        tasks.forEach(t -> {
            try { t.get(); } 
            catch (Exception e) { throw new RuntimeException("处理失败", e); }
        });
        pool.shutdown();
        return dest;
    }
 
    // 并行像素化处理（优化版）
    private static int[] parallelPixelate(int[] src, int width, int height, int blockSize) {
        final int[] dest = new int[src.length];
        final int alphaThreshold = (int) (0.2 * 255);
 
        // 多线程配置
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(numThreads);
        int stripHeight = (height + numThreads - 1) / numThreads;
 
        List<Future<?>> tasks = new ArrayList<>();
        for (int t = 0; t < numThreads; t++) {
            final int startY = t * stripHeight;
            final int endY = Math.min(startY + stripHeight, height);
 
            tasks.add(pool.submit(() -> {
                for (int y = startY; y < endY; y += blockSize) {
                    for (int x = 0; x < width; x += blockSize) {
                        // 计算块边界
                        int blockWidth = Math.min(blockSize, width - x);
                        int blockHeight = Math.min(blockSize, endY - y);
                        if (blockHeight <= 0) continue;
 
                        // 获取基准像素
                        int baseIdx = y * width + x;
                        int alpha = (src[baseIdx] >> 24) & 0xFF;
 
                        if (alpha > alphaThreshold) {
                            int color = src[baseIdx] & 0x00FFFFFF;
                            // 填充色块（优化内存写入模式）
                            for (int dy = 0; dy < blockHeight; dy++) {
                                int rowStart = (y + dy) * width;
                                int destRow = rowStart + x;
                                Arrays.fill(dest, destRow, destRow + blockWidth, 
                                    (alpha << 24) | color);
                            }
                        } else {
                            // 透明区域直接复制
                            for (int dy = 0; dy < blockHeight; dy++) {
                                int rowStart = (y + dy) * width;
                                System.arraycopy(src, rowStart + x, 
                                    dest, rowStart + x, blockWidth);
                            }
                        }
                    }
                }
            }));
        }
 
        tasks.forEach(t -> {
            try { t.get(); } 
            catch (Exception e) { throw new RuntimeException("处理失败", e); }
        });
        pool.shutdown();
        return dest;
    }
 
    // 辅助方法：像素分量打包
    private static int packPixel(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
 
    // 辅助方法：颜色分量钳位
    private static float clamp(float value) {
        return Math.min(Math.max(value, 0), 255);
    }
    public static void main(String[] args) {
        launch(args);
    }
}