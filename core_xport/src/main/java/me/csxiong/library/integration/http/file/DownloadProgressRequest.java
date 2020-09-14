package me.csxiong.library.integration.http.file;

import android.text.TextUtils;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;

import me.csxiong.library.integration.http.AbsRequest;
import me.csxiong.library.integration.http.ResponseListener;
import me.csxiong.library.utils.FileUtils;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @Desc : 下载请求
 * @Author : Bear - 2020/9/14
 */
public class DownloadProgressRequest extends AbsRequest {

    /**
     * 请求方式
     */
    private String method = "post";

    /**
     * 保存路径
     */
    private String savePath;

    /**
     * 总长度
     */
    private long totalLength = -1;

    public DownloadProgressRequest(String requestMapping) {
        this("post", requestMapping);
    }

    public DownloadProgressRequest(String method, String requestMapping) {
        super(requestMapping);
        this.method = method;
    }

    public DownloadProgressRequest addSavePath(String savePath) {
        this.savePath = savePath;
        return this;
    }

    @Deprecated
    @Override
    public void execute(@Nullable ResponseListener responseListener) {
        super.execute(responseListener);
    }

    @Override
    protected String getMethodType() {
        return method;
    }

    @Override
    protected Request onCreateRequest(Request.Builder requestBuilder, String fullRequestMapping,
                                      Map<String, Object> parameters) {
        return requestBuilder.url(fullRequestMapping).build();
    }

    /**
     * 创建代理接管response
     *
     * @param progressResponseListener
     */
    public void execute(@Nullable ProgressResponseListener progressResponseListener) {
        super.execute(new ResponseListener() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public boolean onConvertResponse(Response response) {
                if (response == null) {
                    return false;
                }
                if (TextUtils.isEmpty(savePath)) {
                    return false;
                }
                byte[] buffer = new byte[getBufferSize()];
                long write = 0;
                int read = 0;
                ResponseBody responseBody = response.body();
                RandomAccessFile randomAccessFile = null;
                totalLength = parserFileSize(response);
                try {
                    if (!FileUtils.exists(savePath)) {
                        FileUtils.createNewFile(savePath);
                    }
                    randomAccessFile = new RandomAccessFile(new File(savePath), "rw");
                    if (progressResponseListener != null) {
                        progressResponseListener.onStart();
                    }
                    while ((read = responseBody.source().read(buffer)) > 0) {
                        randomAccessFile.write(buffer);
                        write += read;
                        if (progressResponseListener != null) {
                            progressResponseListener.onProgress(write, totalLength);
                        }
                    }
                    if (progressResponseListener != null) {
                        progressResponseListener.onNext(savePath);
                        progressResponseListener.onComplete();
                    }
                } catch (Exception e) {
                    if (progressResponseListener != null) {
                        progressResponseListener.onError(e);
                    }
                } finally {
                    responseBody.close();
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (IOException e) {
                            if (progressResponseListener != null) {
                                progressResponseListener.onError(e);
                            }
                        }
                    }
                }
                return true;
            }
        });
    }

    private long parserFileSize(Response okhttpRespone) {
        String range = okhttpRespone.request().header("Range");
        if (null == range) {
            String CL = okhttpRespone.header("Content-Length");
            if (null != CL) {
                return Long.valueOf(CL);
            }
        } else {
            String CR = okhttpRespone.header("Content-Range");
            if (null != CR && CR.contains("/")) {
                return Long.valueOf(CR.substring(CR.indexOf("/") + 1, CR.length()));
            }
        }
        return -1;
    }

    private int getBufferSize() {
        return 8 * 1024 * 32;
    }
}
