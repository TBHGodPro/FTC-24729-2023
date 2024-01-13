package org.firstinspires.ftc.teamcode.modules.Camera;

import android.graphics.Canvas;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.Utils.PossiblePropPosition;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

@Config
public class PropDetection implements VisionProcessor {
    // --- Constants ---
    
    public static boolean shouldDisplayBinaryImage = true;
    public static boolean shouldDisplayHSVImage = true;
    public static boolean shouldFillBox = false;
    
    public static int blueHueMin = 60;
    public static int blueHueMax = 120;
    public static int blueSatBrightMin = 0;
    public static int blueSatBrightMax = 255;

    public static int redHueLowMin = 0;
    public static int redHueLowMax = 15;
    public static int redHueHighMin = 165;
    public static int redHueHighMax = 180;
    public static int redSatBrightMin = 0;
    public static int redSatBrightMax = 255;
    
    // -----------------
    
    // Store Alliance
    public final Alliance alliance;
    
    // Static Colors
    public static final Scalar REGION_COLOR = new Scalar(0, 0, 255);
    public static final Scalar FOUND_COLOR = new Scalar(0, 255, 0);
    
    // Regions
    
    public static final int SCREEN_WIDTH = 640;
    public static final int SCREEN_HEIGHT = 480;
    
    public static final int REGION_WIDTH = SCREEN_WIDTH / 3;
    public static final int REGION_HEIGHT = SCREEN_HEIGHT - 275;
    
    public static final Point REGION1_TOPLEFT_ANCHOR = new Point(0, 0);
    public static final Point REGION2_TOPLEFT_ANCHOR = new Point(SCREEN_WIDTH / 3, 0);
    public static final Point REGION3_TOPLEFT_ANCHOR = new Point(SCREEN_WIDTH / 1.5, 0);
    
    public static final Point region1_A = new Point(
            REGION1_TOPLEFT_ANCHOR.x,
            REGION1_TOPLEFT_ANCHOR.y);
    public static final Point region1_B = new Point(
            REGION1_TOPLEFT_ANCHOR.x + REGION_WIDTH,
            REGION1_TOPLEFT_ANCHOR.y + REGION_HEIGHT);
    public static final Point region2_A = new Point(
            REGION2_TOPLEFT_ANCHOR.x,
            REGION2_TOPLEFT_ANCHOR.y);
    public static final Point region2_B = new Point(
            REGION2_TOPLEFT_ANCHOR.x + REGION_WIDTH,
            REGION2_TOPLEFT_ANCHOR.y + REGION_HEIGHT);
    public static final Point region3_A = new Point(
            REGION3_TOPLEFT_ANCHOR.x,
            REGION3_TOPLEFT_ANCHOR.y);
    public static final Point region3_B = new Point(
            REGION3_TOPLEFT_ANCHOR.x + REGION_WIDTH,
            REGION3_TOPLEFT_ANCHOR.y + REGION_HEIGHT);
    
    // Processing
    
    public Mat hsvMat = new Mat();
    public Mat binaryMat = new Mat();
    
    public Mat region1_mat, region2_mat, region3_mat;

    // Colors
    public Scalar blueLow;
    public Scalar blueHigh;

    public Scalar redLowLow;
    public Scalar redLowHigh;
    public Scalar redHighLow;
    public Scalar redHighHigh;
    
    // Store Position
    
    public volatile PossiblePropPosition position = PossiblePropPosition.CENTER;
    
    public PropDetection(Alliance alliance) {
        this.alliance = alliance;

        updateColors();
    }

    public void updateColors() {
        switch (alliance) {
            case RED: {
                redLowLow = new Scalar(redHueLowMin, redSatBrightMin, redSatBrightMin);
                redLowHigh = new Scalar(redHueLowMax, redSatBrightMax, redSatBrightMax);

                redHighLow = new Scalar(redHueHighMin, redSatBrightMin, redSatBrightMin);
                redHighHigh = new Scalar(redHueHighMax, redSatBrightMax, redSatBrightMax);

                break;
            }

            case BLUE: {
                blueLow = new Scalar(blueHueMin, blueSatBrightMin, blueSatBrightMin);
                blueHigh = new Scalar(blueHueMax, blueSatBrightMax, blueSatBrightMax);

                break;
            }
        }
    }
    
    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        // Code executed on the first frame dispatched into this VisionProcessor

        updateColors();
    }
    
    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        // Update Colors
        updateColors();

        // Rotate Frame
        Core.rotate(frame, frame, Core.ROTATE_180);
        
        // Image Conversion
        
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);

        switch (alliance) {
            case RED: {
                Mat lowMat = new Mat();
                Core.inRange(hsvMat, redLowLow, redLowHigh, lowMat);

                Mat highMat = new Mat();
                Core.inRange(hsvMat, redHighLow, redHighHigh, highMat);

                Core.bitwise_or(lowMat, highMat, binaryMat);

                break;
            }

            case BLUE: {
                Core.inRange(hsvMat, blueLow, blueHigh, binaryMat);

                break;
            }
        }
        
        if (shouldDisplayBinaryImage) {
            binaryMat.copyTo(frame);
        } else if (shouldDisplayHSVImage) {
            hsvMat.copyTo(frame);
        }
        
        // Draw Region Boundaries
        
        Imgproc.rectangle(
                frame,
                region1_A,
                region1_B,
                REGION_COLOR,
                2);
        
        Imgproc.rectangle(
                frame,
                region2_A,
                region2_B,
                REGION_COLOR,
                2);
        
        Imgproc.rectangle(
                frame,
                region3_A,
                region3_B,
                REGION_COLOR,
                2);
        
        // Locate Most Likely Region
        
        // - Set Submats
        if (region1_mat == null) {
            region1_mat = binaryMat.submat(new Rect(region1_A, region1_B));
            region2_mat = binaryMat.submat(new Rect(region2_A, region2_B));
            region3_mat = binaryMat.submat(new Rect(region3_A, region3_B));
        }
        
        // - Find Averages
        
        int avg1 = (int) Core.mean(region1_mat).val[0];
        int avg2 = (int) Core.mean(region2_mat).val[0];
        int avg3 = (int) Core.mean(region3_mat).val[0];
        
        // - Find Best Average
        
        int found = 0;

        switch (alliance) {
            case RED: {
                int avgOneTwo = Math.min(avg1, avg2);
                found = Math.min(avgOneTwo, avg3);

                break;
            }

            case BLUE: {
                int avgOneTwo = Math.max(avg1, avg2);
                found = Math.max(avgOneTwo, avg3);

                break;
            }
        }

        // Display and Record Findings
        
        if (found == avg1) {
            position = PossiblePropPosition.LEFT;
            
            Imgproc.rectangle(
                    frame,
                    region1_A,
                    region1_B,
                    FOUND_COLOR,
                    shouldFillBox ? -1 : 8);
        } else if (found == avg2) {
            position = PossiblePropPosition.CENTER;
            
            Imgproc.rectangle(
                    frame,
                    region2_A,
                    region2_B,
                    FOUND_COLOR,
                    shouldFillBox ? -1 : 8);
        } else {
            position = PossiblePropPosition.RIGHT;
            
            Imgproc.rectangle(
                    frame,
                    region3_A,
                    region3_B,
                    FOUND_COLOR,
                    shouldFillBox ? -1 : 8);
        }
        
        // Return Nothing
        
        return null;
    }
    
    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        // Cool feature: This method is used for drawing annotations onto
        // the displayed image, e.g outlining and indicating which objects
        // are being detected on the screen, using a GPU and high quality
        // graphics Canvas which allow for crisp quality shapes.
    }
}
