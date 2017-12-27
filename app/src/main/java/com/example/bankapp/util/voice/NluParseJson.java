package com.example.bankapp.util.voice;


import com.example.bankapp.modle.voice.Answer;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.music.AnswerMusic;
import com.example.bankapp.util.GsonUtil;
import com.youdao.sdk.ydtranslate.Translate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

/**
 * Created by zhangyuanyuan on 2017/7/18.
 */

public class NluParseJson {
    /**
     * 解析故事json
     *
     * @param object
     */
    public static String parseStory(JSONObject object) {
        try {
            String path = "";
            JSONObject obj3 = new JSONObject(object.optString("answer"));
            String answer = obj3.optString("text");
            if (object.has("data")) {
                JSONObject data = new JSONObject(object.optString("data"));
                JSONArray jsonArray = data.optJSONArray("result");
                if (jsonArray != null) {
                    int size = jsonArray.length();
                    if (size > 0) {
                        JSONObject voice = jsonArray.optJSONObject(0);
                        path = voice.optString("playUrl");
                    }
                }
                return path;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析音乐X musicX
     *
     * @param object json
     * @return tts and url
     */
    public static String parseMusicX(JSONObject object) {
        try {
            String path = "";
            JSONObject obj4 = new JSONObject(object.optString("answer"));
            String answer = obj4.optString("text");
            if (object.has("data")) {
                JSONObject music = new JSONObject(object.optString("data"));
                JSONArray musicArray = music.optJSONArray("result");
                if (musicArray != null) {
                    int size = musicArray.length();
                    if (size > 0) {
                        JSONObject voice = musicArray.optJSONObject(0);
                        path = voice.optString("audiopath");
                    }
                }
                return path;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析音乐 musicPlayer_smartHome
     *
     * @param object json
     * @return tts and url
     */
    public static Answer parseMusicSmartHome(JSONObject object) {
        try {
            AnswerMusic bean = new AnswerMusic();
            JSONArray moreResults = object.optJSONArray("moreResults");
            if (moreResults != null) {
                if (moreResults.length() > 0) {
                    JSONObject moreObj = moreResults.optJSONObject(0);
                    if (object.has("data")) {
                        JSONObject moredata = new JSONObject(moreObj.optString("data"));
                        JSONArray moreArray = moredata.optJSONArray("result");
                        if (moreArray != null) {
                            int size = moreArray.length();
                            if (size > 0) {
                                JSONObject voice = moreArray.optJSONObject(0);
                                String path = voice.optString("audiopath");
//                                bean.setUrl(path);

                                JSONArray singerArr = voice.optJSONArray("singernames");
                                if (singerArr != null) {
                                    if (singerArr.length() > 0) {
                                        String name = singerArr.optString(0);
                                        String answer = "播放一首" + name + "的" + voice.optString("albumname");
//                                        bean.setAnswer(answer);
                                    } else {
                                        String answer = "播放一首" + voice.optString("albumname");
//                                        bean.setAnswer(answer);
                                    }
                                }
                            }
                        }
                    }
                }
            }
//            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 笑话
     */
    public static String parseJoke(JSONObject object) {

        try {
            String path = "";
            JSONObject obj4 = new JSONObject(object.optString("answer"));
            String answer = obj4.optString("text");
            if (object.has("data")) {
                JSONObject music = new JSONObject(object.optString("data"));
                JSONArray musicArray = music.optJSONArray("result");
                if (musicArray != null) {
                    int size = musicArray.length();
                    if (size > 0) {
                        JSONObject voice = musicArray.optJSONObject(0);
                        path = voice.optString("mp3Url");
                    }
                }
                return path;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 菜谱
     */
    public static String parseCookKook(JSONObject object) {

        try {
            String content = "";
//            JSONObject obj4 = new JSONObject(object.optString("answer"));
//            String answer = obj4.optString("text");
            if (object.has("data")) {
                JSONObject cook = new JSONObject(object.optString("data"));
                JSONArray cookArray = cook.optJSONArray("result");
                if (cookArray != null) {
                    int size = cookArray.length();
                    if (size > 0) {
                        JSONObject cook_result = cookArray.optJSONObject(new Random().nextInt(size));
                        content = cook_result.optString("steps");
                    }
                }
                return content;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 新闻
     *
     * @param object
     */
    public static News parseNews(JSONObject object) {
        String url = "";
        News news = null;
        try {
            if (object.has("data")) {
                JSONObject newsObject = new JSONObject(object.optString("data"));
                JSONArray newsArray = newsObject.optJSONArray("result");
                if (newsArray != null) {
                    int size = newsArray.length();
                    if (size > 0) {
                        List<News> newses = GsonUtil.GsonToArrayList(newsArray.toString(), News.class);
                        int random = new Random().nextInt(newses.size());
                        news = newses.get(random);
//                        url = news.getContent();
                    }
                }
                return news;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 每日学英语
     *
     * @param object
     */
    public static EnglishEveryday parseEdnglish(JSONObject object) {
        EnglishEveryday englishEveryday = null;
        try {
            if (object.has("data")) {
                JSONObject newsObject = new JSONObject(object.optString("data"));
                JSONArray newsArray = newsObject.optJSONArray("result");
                if (newsArray != null) {
                    int size = newsArray.length();
                    if (size > 0) {
                        List<EnglishEveryday> englishEverydays = GsonUtil.GsonToArrayList(newsArray.toString(), EnglishEveryday.class);
                        int random = new Random().nextInt(englishEverydays.size());
                        englishEveryday = englishEverydays.get(random);
//                        url = news.getContent();
                    }
                }
                return englishEveryday;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 翻译
     *
     * @param object
     */
    public static String parseTranslation(JSONObject object) {
        String translate_value = "";

        if (object.has("semantic")) {
            JSONArray newsArray = object.optJSONArray("semantic");
            if (newsArray != null) {
                int size = newsArray.length();
                if (size > 0) {
                    List<Translate> name = GsonUtil.GsonToArrayList(newsArray.toString(), Translate.class);
//                    translate_value = name.get(0).getSlots().get(0).getValue();
                }
            }
            return translate_value;
        }

        return null;
    }


    /**
     * 计算
     *
     * @param object
     */
    public static void parseCalculat(JSONObject object) {

        try {
            if (object.has("answer")) {
                JSONObject news = new JSONObject(object.optString("answer"));
                String text = news.optString("text");
            }

        } catch (JSONException e) {
            e.printStackTrace();


        }
    }

    /**
     * 古诗词
     *
     * @param object
     */
    public static String parsePoetry(JSONObject object) {
        String poetryContent = "";
        try {
            JSONObject obj_answer = new JSONObject(object.optString("answer"));
            String answer = obj_answer.optString("text");
            if (object.has("data")) {
                JSONObject proety = new JSONObject(object.optString("data"));
                JSONArray proetyArray = proety.optJSONArray("result");
                if (proetyArray != null) {
                    int size = proetyArray.length();
                    if (size > 0) {
                        JSONObject news_result = proetyArray.optJSONObject(0);
                        String author = news_result.optString("author");//作者
                        String category = news_result.optString("category");//诗的类别
                        String content = news_result.optString("content");//内容
                        String title = news_result.optString("title");//题目
                        String dynasty = news_result.optString("dynasty");//朝代
                        poetryContent = title + "," + author + "," + content;
                    }
                }
                return poetryContent;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
