package com.azx.mybuildassistant.santiyun.doc;

import com.azx.mybuildassistant.santiyun.SanTiYunBaseTask;
import com.azx.mybuildassistant.santiyun.bean.DocClassBean;
import com.azx.mybuildassistant.utils.MyLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentBuildTask extends SanTiYunBaseTask {

    private static final boolean DEBUG = false;

    private static final String DOC_API_PATH = "/Users/wangzhiguo/Downloads/Learns/Guo_Company_Svn/GitHub/Document/live/client-api/android";
    private static final String DOC_API_OVERVIEW_FILE_NAME = "overview.md";
    private static final String DOC_API_DETAIL_FILE_NAME = "methods.md";
    private static final String DOC_API_CALLBACK_FILE_NAME = "callbacks.md";
    private static final String DOC_API_CONSTANT_FILE_NAME = "other.md";

    private static final String SPACE = "  ";
    private static final String TAG = DocumentBuildTask.class.getSimpleName();

    private static final String TTTRTC_INTER_FILE = WSTECHAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/wstechapi/TTTRtcEngine.java";
    private static final String TTTRTC_CALLBACK_FILE = ENTERCONFAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/expansion/inter/TTTRtcEngineEventInter.java";
    private static final String TTTRTC_CONSTANT_FILE = ENTERCONFAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/library/Constants.java";

    private DocClassBean[] classBeans = new DocClassBean[]{
            new DocClassBean("TTTVideoCanvas", "视频属性", ENTERCONFAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/expansion/bean/TTTVideoCanvas.java"),
            new DocClassBean("TTTVideoFrame", "视频帧", ENTERCONFAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/bean/TTTVideoFrame.java"),
            new DocClassBean("VideoCompositingLayout", "视频合成布局", ENTERCONFAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/expansion/bean/VideoCompositingLayout.java"),
            new DocClassBean("VideoCompositingLayout.Region", "视频位置信息", ENTERCONFAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/expansion/bean/VideoCompositingLayout.java"),
            new DocClassBean("LocalAudioStats", "本地音频统计信息", ENTERCONFAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/expansion/bean/LocalAudioStats.java"),
            new DocClassBean("LocalVideoStats", "本地视频统计信息", ENTERCONFAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/expansion/bean/LocalVideoStats.java"),
            new DocClassBean("RemoteAudioStats", "远端音频统计信息", ENTERCONFAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/expansion/bean/RemoteAudioStats.java"),
            new DocClassBean("RemoteVideoStats", "远端视频统计信息", ENTERCONFAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/expansion/bean/RemoteVideoStats.java"),
            new DocClassBean("RtcStats", "通话相关的统计信息", ENTERCONFAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/expansion/bean/RtcStats.java"),
            new DocClassBean("PublisherConfiguration", "直播推流配置", WSTECHAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/wstechapi/model/PublisherConfiguration.java"),
            new DocClassBean("ScreenRecordConfig", "屏幕录制/分享配置信息", ENTERCONFAPI_MODULE_PATH + "/src/main/java/com/wushuangtech/expansion/bean/ScreenRecordConfig.java")};

    private List<DocumentInitData.FunCodeBlock> funCodeBlocks, callbackFunCodeBlocks, constantsCodeBlocks;
    private List<ApiDetailBean> apiDetailBeans, callBackApiDetailBeans;
    private DocumentBuildApiDetailOverview documentBuildApiDetailOverview;
    private DocumentApiOverView documentApiOverView;
    private DocumentCallBack documentCallBack;
    private DocumentOther documentOther;

    private DocumentFileHolder documentFileHolder;

    @Override
    public int start() {
        DocumentInitData documentInitData = new DocumentInitData();
        funCodeBlocks = documentInitData.initInterDatas(TTTRTC_INTER_FILE);
        if (DEBUG) {
            for (DocumentInitData.FunCodeBlock funCommentCodeBlock : funCodeBlocks) {
                System.out.println(funCommentCodeBlock.toString());
            }
        }

        callbackFunCodeBlocks = documentInitData.initCallBackInterDatas(TTTRTC_CALLBACK_FILE);
        if (DEBUG) {
            for (DocumentInitData.FunCodeBlock funCommentCodeBlock : callbackFunCodeBlocks) {
                System.out.println(funCommentCodeBlock.toString());
            }
        }

        constantsCodeBlocks = documentInitData.initConstantDatas(TTTRTC_CONSTANT_FILE);
        if (DEBUG) {
            for (DocumentInitData.FunCodeBlock funCommentCodeBlock : constantsCodeBlocks) {
                System.out.println(funCommentCodeBlock.toString());
            }
        }

        documentFileHolder = new DocumentFileHolder();
        documentBuildApiDetailOverview = new DocumentBuildApiDetailOverview();
        documentApiOverView = new DocumentApiOverView();
        documentCallBack = new DocumentCallBack();
        documentOther = new DocumentOther();

        apiDetailBeans = buildApiDetailBean(funCodeBlocks);
        callBackApiDetailBeans = buildApiDetailBean(callbackFunCodeBlocks);

        buildApiOverViewDoc();
        buildApiDetailDoc();
        buildCallBackDoc();
        buildConstantDoc();
        return 0;
    }

    private void buildConstantDoc() {
        String str = documentOther.buildDocument(classBeans, constantsCodeBlocks);
        documentFileHolder.createDocFile(DOC_API_PATH, DOC_API_CONSTANT_FILE_NAME, str);
    }

    private void buildApiOverViewDoc() {
        String str = documentApiOverView.buildDocument(funCodeBlocks, callbackFunCodeBlocks);
        documentFileHolder.createDocFile(DOC_API_PATH, DOC_API_OVERVIEW_FILE_NAME, str);
    }

    private void buildApiDetailDoc() {
        for (DocumentInitData.FunCodeBlock mFunCommentCodeBlock : funCodeBlocks) {
            documentBuildApiDetailOverview.produceOverViewBean(mFunCommentCodeBlock.funFullName);
        }
        StringBuilder result = new StringBuilder();
        result.append(documentBuildApiDetailOverview.buildOverViewDoc());
        result.append("\n## 详细描述\n");
        for (ApiDetailBean apiDetailBean : apiDetailBeans) {
            result.append(apiDetailBean.toString());
        }
        DocumentFileHolder holder = new DocumentFileHolder();
        holder.createDocFile(DOC_API_PATH, DOC_API_DETAIL_FILE_NAME, result.toString());
    }

    private void buildCallBackDoc() {
        String str = documentCallBack.buildDocument(callbackFunCodeBlocks, callBackApiDetailBeans);
        documentFileHolder.createDocFile(DOC_API_PATH, DOC_API_CALLBACK_FILE_NAME, str);
    }

    private List<ApiDetailBean> buildApiDetailBean(List<DocumentInitData.FunCodeBlock> funCodeBlocks) {
        List<ApiDetailBean> apiDetailBeans = new ArrayList<>();
        for (DocumentInitData.FunCodeBlock mFunCommentCodeBlock : funCodeBlocks) {
            String s = mFunCommentCodeBlock.funComment;
            String s1 = s.replaceAll("/\\*\\*", "");
            String s2 = s1.replaceAll("\\*/", "");
            mFunCommentCodeBlock.funComment = s2.replaceAll("\\*", "");

            String funName = getFunName(mFunCommentCodeBlock.funFullName);
            if (funName == null) {
                MyLog.e(TAG, "Get fun name failed! " + mFunCommentCodeBlock.funFullName);
                return null;
            }

            String chineseName = getChineseName(mFunCommentCodeBlock.funComment);
            if (chineseName == null) {
                MyLog.e(TAG, "Get chinese name failed! " + mFunCommentCodeBlock.funFullName);
                return null;
            }

            ApiDetailBean bean = new ApiDetailBean();
            bean.func_name = funName;
            bean.func_chinese = chineseName.trim();
            if (mFunCommentCodeBlock.funFullName.contains("//IJK_MODULE_CreateIjkRendererView")) {
                bean.func_full_name = mFunCommentCodeBlock.funFullName.replaceAll("//IJK_MODULE_CreateIjkRendererView", "").trim();
            } else {
                bean.func_full_name = mFunCommentCodeBlock.funFullName.trim();
            }
            bean.func_params_names = getFunParamName(mFunCommentCodeBlock.funFullName);
            bean.func_detail = mFunCommentCodeBlock.funComment;
            apiDetailBeans.add(bean);
        }
        return apiDetailBeans;
    }

    private String getFunName(String funFullName) {
        String[] s = funFullName.split(" ");
        for (String s1 : s) {
            if (s1.contains("(")) {
                int index = s1.indexOf("(");
                return s1.substring(0, index);
            }
        }
        return null;
    }

    private String getChineseName(String mFuncFullName) {
        String[] split = mFuncFullName.split("<p/>");
        return split[0];
    }

    private String[] getFunParamName(String mFuncFullName) {
        if (mFuncFullName.length() == 0) {
            return new String[0];
        }
        int index1 = mFuncFullName.indexOf("(");
        int index2 = mFuncFullName.indexOf(")");
        if (DEBUG) {
            MyLog.d(TAG, "funFullName -> src content : " + mFuncFullName);
        }
        String prams_str = mFuncFullName.substring(index1 + 1, index2);
        if (prams_str.length() == 0) {
            return new String[0];
        }
        String[] param_arr = prams_str.split(", ");
        String[] target = new String[param_arr.length];
        int count = 0;

        if (DEBUG) {
            MyLog.d(TAG, "getFunParamName -> param_arr : " + Arrays.toString(param_arr));
        }
        for (String param_element : param_arr) {
            String[] param_split = param_element.split(" ");
            target[count] = param_split[1];
            count++;
        }

        if (DEBUG) {
            System.out.println("wzgtst ------- : " + Arrays.toString(param_arr));
        }
        return target;
    }

    class ApiDetailBean {

        String func_name, func_chinese;
        String func_full_name;
        String[] func_params_names;
        String func_detail;
        private FuncDetail mFuncDetail;

        ApiDetailBean() {
            mFuncDetail = new FuncDetail();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (!func_name.contains("create") || !func_name.contains("onJoinChannelSuccess")) {
                sb.append("\n").append("---").append("\n");
            }
            // 第一行
            sb.append("### ").append("<div id=").append(func_name).append(">").append(func_name).append("()</div>").append("\n");
            sb.append("### ").append(func_chinese).append("\n");
            // 第二行
            sb.append("```").append("\n");
            sb.append(func_full_name).append("\n");
            sb.append("```").append("\n");

            // 第三行
            FuncDetail funcDetail = handleFuncDetail(func_detail);

            if ("".equals(funcDetail.introduce.toString().trim())) {
                sb.append(funcDetail.introduceTitle).append("\n");
            } else {
                String introduce = funcDetail.introduce.toString().replaceAll("<br/>", "  ");
                String introduce2 = introduce.replaceAll("_", "\\\\_");
                sb.append(introduce2).append("\n");
            }

            sb.append(funcDetail.notices.toString()).append("\n");
            List<FunArgsBean> paramsBeans = new ArrayList<>();
            for (String func_params_name : func_params_names) {
                String params_content = funcDetail.params.toString();
                String[] params_content_arr = params_content.split("\n");
                for (String params_content_arr_child : params_content_arr) {
                    String temp = params_content_arr_child.replaceAll("@param", "").trim();
                    if (temp.startsWith(func_params_name)) {
//                        System.out.println("wzgtest --------" + temp);
                        String target = temp.replaceAll(func_params_name, "");
//                        System.out.println("wzgtest --------" + target);
                        paramsBeans.add(new FunArgsBean(func_params_name, target));
                        break;
                    }
                }
            }

            if (paramsBeans.size() > 0) {
                sb.append("### ").append("参数").append("\n");
                String table_content = buildArgsBlock(paramsBeans);
                sb.append(table_content).append("\n");
            }

            String retValue = funcDetail.retVale.toString();
            if (!retValue.equals("")) {
                sb.append("\n").append("### ").append("返回").append("\n");
                String retValueTrim = retValue.replaceAll("@return", "").trim();
                sb.append(retValueTrim).append("\n");
            }
            return sb.toString();
        }

        private FuncDetail handleFuncDetail(String func_detail) {
            if (DEBUG) {
                System.out.println(func_detail);
            }
            String s1 = func_detail.replaceAll("      ", "");
            if (DEBUG) {
                System.out.println("\n// --- --- --- --- --- --- --- --- --- --- \n");
                System.out.println(s1);
                System.out.println("\n// --- --- --- --- --- --- --- --- --- --- \n");
            }

            boolean isParams = false;
            boolean isNotice = false;
            boolean isReturn = false;
            boolean isFirst = true;
            String[] split = s1.split("<p/>");
            for (String s : split) {
                String[] split1 = s.split("\\n");
                for (String s2 : split1) {
                    String trim = s2.trim();
                    if (trim.length() == 0) {
                        continue;
                    }

                    if (s2.contains("使用注意")) {
                        mFuncDetail.notices.append("### Note: ").append("\n");
                        isNotice = true;
                        isParams = false;
                        isReturn = false;
                    } else if (s2.contains("@param")) {
                        isParams = true;
                        isNotice = false;
                        isReturn = false;
                        mFuncDetail.params.append(s2).append("\n");
                    } else if (s2.contains("@return")) {
                        isReturn = true;
                        isParams = false;
                        isNotice = false;
                        mFuncDetail.retVale.append("* ").append(s2).append("\n");
                    } else {
                        String s3 = s2.trim();
                        if (isParams) {
                            mFuncDetail.params.delete(mFuncDetail.params.length() - 2, mFuncDetail.params.length());
                            mFuncDetail.params.append(s3).append("\n");
                        } else if (isReturn) {
                            mFuncDetail.retVale.append("* ").append(s3).append("\n");
                        } else if (isNotice) {
                            Pattern pattern = Pattern.compile("^\\d\\.");
                            Matcher matcher = pattern.matcher(s2);
                            boolean isMatcher = matcher.find();
                            if (isMatcher) {
                                s2 = s2.substring(2);
                                mFuncDetail.notices.append("* ").append(s2).append("\n");
                            }
                        } else {
                            if (isFirst) {
                                mFuncDetail.introduceTitle = s3 + "   \n";
                                isFirst = false;
                            } else {
                                mFuncDetail.introduce.append(s3).append("  ").append("\n");
                            }
                        }
                    }
                }
            }

            if (DEBUG) {
                System.out.println("introduce : " + mFuncDetail.introduce.toString());
                System.out.println("notices : " + mFuncDetail.notices.toString());
                System.out.println("params : " + mFuncDetail.params.toString());
                System.out.println("retVale : " + mFuncDetail.retVale.toString());
            }
            return mFuncDetail;
        }

        private String buildArgsBlock(List<FunArgsBean> args) {
            StringBuilder sb = new StringBuilder();
            sb.append("<table width=100% >").append("\n");
            for (FunArgsBean arg : args) {
                sb.append(SPACE).append("<tr>").append("\n");
                sb.append(SPACE).append(SPACE).append("<td>").append(arg.mName).append("</td>").append("\n");
                sb.append(SPACE).append(SPACE).append("<td>").append(arg.mDesc).append("</td>").append("\n");
                sb.append(SPACE).append("</tr>").append("\n");
            }
            sb.append("</table>");
            return sb.toString();
        }

        class FuncDetail {

            String introduceTitle;
            StringBuilder introduce = new StringBuilder();
            StringBuilder notices = new StringBuilder();
            StringBuilder params = new StringBuilder();
            StringBuilder retVale = new StringBuilder();
        }

        class FunArgsBean {

            String mName;
            String mDesc;

            FunArgsBean(String mName, String mDesc) {
                this.mName = mName;
                this.mDesc = mDesc;
            }
        }
    }
}
