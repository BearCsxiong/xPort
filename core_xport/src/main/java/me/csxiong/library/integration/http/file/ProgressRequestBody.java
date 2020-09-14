package me.csxiong.library.integration.http.file;

import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

/**
 * @Desc : 带进度上传的RequestBody
 * @Author : Bear - 2020/4/3
 */
public class ProgressRequestBody extends RequestBody {

    /**
     * 原requestBody
     */
    private RequestBody requestBody;

    /**
     * 内容总长度
     */
    private long totalLength = -1;

    /**
     * 当前进度
     */
    private long currentLenght = 0;

    /**
     * 进度监听
     */
    private OnProgressListener onProgressListener;

    public ProgressRequestBody(RequestBody requestBody, OnProgressListener onProgressListener) {
        this.requestBody = requestBody;
        this.onProgressListener = onProgressListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength()
        throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink)
        throws IOException {
        ForwardingSink proxySink = new ForwardingSink(sink) {
            @Override
            public void write(Buffer source, long byteCount)
                throws IOException {
                if (totalLength == -1) {
                    if (onProgressListener != null) {
                        onProgressListener.onStart();
                    }
                    totalLength = contentLength();
                }
                currentLenght += byteCount;
                if (onProgressListener != null) {
                    onProgressListener.onProgress(currentLenght, totalLength);
                }
                super.write(source, byteCount);
            }
        };
        BufferedSink buffer = Okio.buffer(proxySink);
        requestBody.writeTo(buffer);
        buffer.flush();
    }
}
