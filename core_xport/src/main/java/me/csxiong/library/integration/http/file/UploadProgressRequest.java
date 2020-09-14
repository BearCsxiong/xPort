package me.csxiong.library.integration.http.file;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;

import me.csxiong.library.integration.http.AbsRequest;
import me.csxiong.library.integration.http.ResponseListener;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @Desc : 文件上传
 * @Author : Bear - 2020/4/2
 */
public class UploadProgressRequest extends AbsRequest {

    /**
     * method
     */
    private String method = "post";

    /**
     * 媒体类型
     */
    private String mediaType;

    /**
     * 目标文件
     */
    private File file;

    /**
     * 进度监听
     */
    private ProgressResponseListener responseListener;

    public UploadProgressRequest(String requestMapping) {
        this("post", requestMapping);
    }

    public UploadProgressRequest(String method, String requestMapping) {
        super(requestMapping);
        this.method = method;
    }

    public UploadProgressRequest addMediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public UploadProgressRequest addFile(File file) {
        this.file = file;
        return this;
    }

    @Override
    protected String getMethodType() {
        return method;
    }

    @Override
    protected Request onCreateRequest(Request.Builder requestBuilder, String fullRequestMapping,
                                      Map<String, Object> parameters) {
        requestBuilder.url(fullRequestMapping);
        // 多表单生成
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        // 表单部分参数添加
        for (String key : parameters.keySet()) {
            builder.addFormDataPart(key, String.valueOf(parameters.get(key)));
        }
        // 目标源requestBody建立
        builder.addPart(new ProgressRequestBody(RequestBody.create(MediaType.parse(mediaType), file), responseListener));
        return requestBuilder.url(fullRequestMapping).post(builder.build()).build();
    }

    /**
     * 目标接口在此结构中不继续使用
     *
     * @param responseListener 响应参数
     */
    @Override
    @Deprecated
    public final void execute(@Nullable ResponseListener responseListener) {
        super.execute(responseListener);
    }

    public final void execute(@Nullable ProgressResponseListener responseListener) {
        this.responseListener = responseListener;
        super.execute(responseListener);
    }

}
