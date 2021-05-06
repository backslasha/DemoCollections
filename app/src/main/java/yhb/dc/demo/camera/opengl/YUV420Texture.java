package yhb.dc.demo.camera.opengl;


import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import yhb.dc.R;


public class YUV420Texture {

    private final Context context;

    //顶点坐标
    static float[] vertexData = {   // in counterclockwise order:
            -1f, -1f, 0.0f, // bottom left
            1f, -1f, 0.0f, // bottom right
            -1f, 1f, 0.0f, // top left
            1f, 1f, 0.0f,  // top right
    };

    //纹理坐标
    static float[] textureData = {   // in counterclockwise order:
            0f, 1f, 0.0f, // bottom left
            1f, 1f, 0.0f, // bottom right
            0f, 0f, 0.0f, // top left
            1f, 0f, 0.0f,  // top right
    };

    //每一次取点的时候取几个点
    static final int COORDS_PER_VERTEX = 3;

    private final int vertexCount = vertexData.length / COORDS_PER_VERTEX;

    //位置
    private final FloatBuffer vertexBuffer;
    //纹理
    private final FloatBuffer textureBuffer;

    private int program;

    //顶点位置
    private int avPosition;
    //纹理位置
    private int afPosition;

    //shader  yuv变量
    private int sampler_y;
    private int sampler_u;
    private int sampler_v;
    private int[] textureId_yuv;


    //YUV数据
    private int width_yuv;
    private int height_yuv;
    private ByteBuffer yuvY;
    private ByteBuffer yuvU;
    private ByteBuffer yuvV;


    public YUV420Texture(Context context) {
        this.context = context;

        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);

        textureBuffer = ByteBuffer.allocateDirect(textureData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureData);
        textureBuffer.position(0);
    }

    public void initYUV() {
        String vertexSource = ShaderUtil.readRawTxt(context, R.raw.vertex_shader);
        String fragmentSource = ShaderUtil.readRawTxt(context, R.raw.fragment_shader);
        program = ShaderUtil.createProgram(vertexSource, fragmentSource);
        if (program > 0) {
            //获取顶点坐标字段
            avPosition = GLES20.glGetAttribLocation(program, "av_Position");
            //获取纹理坐标字段
            afPosition = GLES20.glGetAttribLocation(program, "af_Position");
            //获取yuv字段
            sampler_y = GLES20.glGetUniformLocation(program, "sampler_y");
            sampler_u = GLES20.glGetUniformLocation(program, "sampler_u");
            sampler_v = GLES20.glGetUniformLocation(program, "sampler_v");

            textureId_yuv = new int[3];
            //创建3个纹理
            GLES20.glGenTextures(3, textureId_yuv, 0);

            //绑定纹理
            for (int id : textureId_yuv) {
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id);
                //环绕（超出纹理坐标范围）  （s==x t==y GL_REPEAT 重复）
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
                //过滤（纹理像素映射到坐标点）  （缩小、放大：GL_LINEAR线性）
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            }
        }
    }

    public void setYUVData(int width, int height, byte[] y, byte[] u, byte[] v) {
        this.width_yuv = width;
        this.height_yuv = height;
        this.yuvY = ByteBuffer.wrap(y);
        this.yuvU = ByteBuffer.wrap(u);
        this.yuvV = ByteBuffer.wrap(v);
    }

    public void draw() {
        if (width_yuv > 0 && height_yuv > 0 && yuvY != null && yuvU != null && yuvV != null) {
            GLES20.glUseProgram(program);
            GLES20.glEnableVertexAttribArray(avPosition);
            //每一次取的总的点 大小
            // 4 bytes per vertex
            int vertexStride = COORDS_PER_VERTEX * 4;
            GLES20.glVertexAttribPointer(avPosition, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

            GLES20.glEnableVertexAttribArray(afPosition);
            GLES20.glVertexAttribPointer(afPosition, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, textureBuffer);

            //激活纹理0来绑定y数据
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId_yuv[0]);
            //glTexImage2D (int target,
            //                int level,
            //                int internalformat,
            //                int width,
            //                int height,
            //                int border,
            //                int format,
            //                int type,
            //                Buffer pixels)
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width_yuv, height_yuv, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, yuvY);

            //激活纹理1来绑定u数据
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId_yuv[1]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width_yuv / 2, height_yuv / 2, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, yuvU);

            //激活纹理2来绑定u数据
            GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId_yuv[2]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, width_yuv / 2, height_yuv / 2, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, yuvV);

            //给fragment_shader里面yuv变量设置值   0 1 2 标识纹理x
            GLES20.glUniform1i(sampler_y, 0);
            GLES20.glUniform1i(sampler_u, 1);
            GLES20.glUniform1i(sampler_v, 2);

            //绘制
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);

            yuvY.clear();
            yuvU.clear();
            yuvV.clear();
            yuvY = null;
            yuvU = null;
            yuvV = null;
            GLES20.glDisableVertexAttribArray(afPosition);
            GLES20.glDisableVertexAttribArray(avPosition);

        }
    }
}


