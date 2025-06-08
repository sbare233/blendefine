// 新增宝石组管理类
package blendefine;

import java.util.HashSet;
import java.util.Set;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;

public class GemGroup {
    public Set<define> gems = new HashSet<>();
    public double pivotX, pivotY,mofx,mofy,centerx,centery;
    public define nowd;
    public GemGroup(define initialGem) {
        addGem(initialGem);
//        updatePivot();
    }

    public void addGem(define gem) {
        gems.add(gem);
        gem.parentGroup = this; // 需要在define类添加parentGroup字段
//        updatePivot();
    }
    void calculateArithmeticMean() {
        double sumX = 0;
        double sumY = 0;
        int count = gems.size();

        for (define d :gems) {
        	Group g=d.g;
//            Bounds bounds = g.getBoundsInLocal();
//            sumX += g.getTranslateX() + bounds.getWidth()/2;
//            sumY += g.getTranslateY() + bounds.getHeight()/2;
            sumX += g.getTranslateX()-nowd.g.getTranslateX();
            sumY += g.getTranslateY()-nowd.g.getTranslateY();
        }
        centerx=sumX/count;
        centery=sumY/count;
    }
//
//    public void updatePivot() {
//        // 计算几何中心作为默认支点
//        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
//        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
//        
//        for (define gem : gems) {
//            Bounds bounds = gem.g.getBoundsInParent();
//            minX = Math.min(minX, bounds.getMinX());
//            maxX = Math.max(maxX, bounds.getMaxX());
//            minY = Math.min(minY, bounds.getMinY());
//            maxY = Math.max(maxY, bounds.getMaxY());
//        }
//        
//        pivotX = (minX + maxX) / 2;
//        pivotY = (minY + maxY) / 2;
//    }
}