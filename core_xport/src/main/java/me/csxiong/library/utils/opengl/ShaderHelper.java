package me.csxiong.library.utils.opengl;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.support.annotation.RawRes;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 着色器帮助类。
 */
public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    /**
     * 构造Program。
     *
     * @param context
     * @param vertexShaderSourceId
     * @param fragmentShaderSourceId
     * @return
     */
    public static int buildProgram(Context context, @RawRes int vertexShaderSourceId, @RawRes int fragmentShaderSourceId) {
        return buildProgram(readTextFileFromResource(context, vertexShaderSourceId), readTextFileFromResource(context, fragmentShaderSourceId));
    }

    /**
     * 构造Progrom。
     *
     * @param vertexShaderSource
     * @param fragmentShaderSource
     * @return
     */
    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {
        // 读取顶点着色器、片段着色器。
        int vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource);
        int fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource);
        // 链接程序
        int program = linkProgram(vertexShader, fragmentShader);
        validateProgram(program);
        return program;
    }


    /**
     * 编译着色器文本。
     * @param type
     * @param shaderCode
     * @return
     */
    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = GLES20.glCreateShader(type);

        if (shaderObjectId == 0) {
            Log.d(TAG, "Could not create new shader.");
            return 0;
        }

        GLES20.glShaderSource(shaderObjectId, shaderCode);
        GLES20.glCompileShader(shaderObjectId);

        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        Log.d(TAG, "Result of compile source:" + GLES20.glGetShaderInfoLog(shaderObjectId));

        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderObjectId);
            Log.d(TAG, "Compilation of shader failed");
            return 0;
        }

        return shaderObjectId;
    }

    /**
     * 链接程序。
     * @param vertexShaderId
     * @param fragmentShaderId
     * @return
     */
    private static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = GLES20.glCreateProgram();
        if (programObjectId == 0) {
            Log.d(TAG, "Could not create new program");
            return 0;
        }

        GLES20.glAttachShader(programObjectId, vertexShaderId);
        GLES20.glAttachShader(programObjectId, fragmentShaderId);
        GLES20.glLinkProgram(programObjectId);

        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0);

        Log.d(TAG, "Result of linking program: " + linkStatus[0] + ", Log:" + GLES20.glGetProgramInfoLog(programObjectId));

        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programObjectId);
            Log.d(TAG, "Linking of program failed.");
            return 0;
        }

        return programObjectId;
    }

    /**
     * 判断Program是否有效。
     * todo：后续看看要不要删除。https://www.cnblogs.com/time-is-life/p/7595475.html
     * @param programObjectId
     * @return
     */
    private static boolean validateProgram(int programObjectId) {
        GLES20.glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        return validateStatus[0] != 0;
    }

    /**
     * 从R.raw中读取着色器脚本。
     * @param context
     * @param resourceId
     * @return
     */
    private static String readTextFileFromResource(Context context, int resourceId) {
        StringBuilder body = new StringBuilder();

        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.defaultCharset());

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;

            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }
        } catch (IOException e) {
//            Debug.w(e);
        } catch (Resources.NotFoundException nfe) {
//            Debug.w(nfe);
            throw new RuntimeException("Resource not found" + resourceId, nfe);
        }

        return body.toString();
    }

}
