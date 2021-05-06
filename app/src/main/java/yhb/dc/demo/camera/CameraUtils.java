package yhb.dc.demo.camera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.hardware.Camera;
import android.util.Pair;
import android.view.Surface;

import java.util.Arrays;
import java.util.List;


@TargetApi(14)
public class CameraUtils {

    private static final String TAG = "CameraUtils";

    public static int[] adaptPreviewFps(int expectedFps, List<int[]> fpsRanges) {
        expectedFps *= 1000;
        int[] closestRange = fpsRanges.get(0);
        int measure = Math.abs(closestRange[0] - expectedFps) + Math.abs(closestRange[1] - expectedFps);
        for (int[] range : fpsRanges) {
            if (range[0] <= expectedFps && range[1] >= expectedFps) {
                int curMeasure = Math.abs(range[0] - expectedFps) + Math.abs(range[1] - expectedFps);
                if (curMeasure < measure) {
                    closestRange = range;
                    measure = curMeasure;
                }
            }
        }
        return closestRange;
    }

    public static void setOrientation(int cameraId, boolean isLandscape, Camera camera) {
        int orientation = getDisplayOrientation(cameraId);
        if (isLandscape) {
            orientation = orientation - 90;
        }
        camera.setDisplayOrientation(orientation);
    }

    public static boolean supportTouchFocus(Camera camera) {
        if (camera != null) {
            return (camera.getParameters().getMaxNumFocusAreas() != 0);
        }
        return false;
    }

    public static void setAutoFocusMode(Camera camera) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.size() > 0 && focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                camera.setParameters(parameters);
            } else if (focusModes.size() > 0) {
                parameters.setFocusMode(focusModes.get(0));
                camera.setParameters(parameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTouchFocusMode(Camera camera) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.size() > 0 && focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                camera.setParameters(parameters);
            } else if (focusModes.size() > 0) {
                parameters.setFocusMode(focusModes.get(0));
                camera.setParameters(parameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getFixRotation(Activity activity,
                                     int cameraId, Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
//            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }


    public static Camera.Size getOptimalPreviewSize(Camera camera, final int width, final int height) {
        final List<Camera.Size> sizes = camera.getParameters().getSupportedPreviewSizes();
        if (sizes == null) {
            return null;
        }
        final Camera.Size[] sizesArray = new Camera.Size[sizes.size()];
        for (int i = 0; i < sizesArray.length; i++) {
            sizesArray[i] = sizes.get(i);
        }
        Arrays.sort(sizesArray, (lhs, rhs) -> {
            // 按照视频的宽排序，宽一样的按照高排序
            if (lhs.width != rhs.width) {
                return lhs.width - rhs.width;
            } else {
                return lhs.height - rhs.height;
            }
        });
        Camera.Size minDeltaSize = null;
        int minDelta = Integer.MAX_VALUE;
        final Pair<Integer, Integer> debugVideoResolution = new Pair<>(720, 1280);
        final int maxWidth = debugVideoResolution.second;
        final int maxHeight = debugVideoResolution.first;
        for (Camera.Size size : sizesArray) {
            final int delta = Math.abs(size.height * size.width - height * width);
            if (delta < minDelta && size.width <= maxWidth && size.height <= maxHeight && !doNotUse(size.height)) {
                minDelta = delta;
                minDeltaSize = size;
            }
        }
        return minDeltaSize;
    }

    /**
     * 目前发现部分机型使用 540 height 尺寸采集像素，yuv420 经 rotate90 度后会出现图像异常，暂不深究，
     * 此处屏蔽该尺寸
     * 相关 bug：http://zendao.bigo.tv:88/zentao/bug-view-147661.html
     */
    private static boolean doNotUse(int height) {
        return 540 == height;
    }

    public static int getDisplayOrientation(int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation + 360) % 360;
        }
        return result;
    }

    public static boolean supportFlash(Camera camera) {
        Camera.Parameters params = camera.getParameters();
        List<String> flashModes = params.getSupportedFlashModes();
        if (flashModes == null) {
            return false;
        }
        for (String flashMode : flashModes) {
            if (Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
                return true;
            }
        }
        return false;
    }


}
