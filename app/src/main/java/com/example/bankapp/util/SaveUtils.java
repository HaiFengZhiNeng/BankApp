package com.example.bankapp.util;

import com.example.bankapp.common.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangyuanyuan on 2017/11/12.
 */

public class SaveUtils {

    private static SaveUtils mSaveUtil = null;

    private ThreadPoolExecutor executorService;

    public static SaveUtils getInstance() {
        if (mSaveUtil == null) {
            mSaveUtil = new SaveUtils();
        }
        return mSaveUtil;
    }

    public void appendJson(String question) {

        getExecutorService().execute(new JsonThread(question));


    }

    private class JsonThread extends Thread {

        private String mQuestion;

        public JsonThread(String question) {
            this.mQuestion = question;
        }

        @Override
        public void run() {
            super.run();
            List<String> questionList = null;
            String readStr = FileUtil.read(Constants.PROJECT_PATH + "question.txt");
            if (readStr != null) {
                questionList = GsonUtil.GsonToList(readStr, String.class);
            }
            if (questionList != null) {
                questionList.add(mQuestion);
            } else {
                questionList = new ArrayList<>();
                questionList.add(mQuestion);
            }
            String saveStr = GsonUtil.GsonString(questionList);
            FileUtil.saveFile(saveStr, Constants.PROJECT_PATH + "question.txt");
        }
    }


    public synchronized ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(3, Integer.MAX_VALUE, 10, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>());
        }
        return executorService;
    }


}
