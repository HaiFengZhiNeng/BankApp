package com.example.bankapp.asr;

import com.iflytek.aiui.AIUIListener;

import com.iflytek.aiui.AIUIListener;

import android.app.Activity;
import android.widget.Toast;

import com.example.bankapp.R;
import com.example.bankapp.modle.voice.Answer;
import com.example.bankapp.modle.voice.Content;
import com.example.bankapp.modle.voice.Cookbook;
import com.example.bankapp.modle.voice.Data;
import com.example.bankapp.modle.voice.EnglishEveryday;
import com.example.bankapp.modle.voice.Flight;
import com.example.bankapp.modle.voice.Joke;
import com.example.bankapp.modle.voice.News;
import com.example.bankapp.modle.voice.Poetry;
import com.example.bankapp.modle.voice.Translation;
import com.example.bankapp.modle.voice.Weather;
import com.example.bankapp.modle.voice.XInfo;
import com.example.bankapp.modle.voice.cmd.Cmd;
import com.example.bankapp.modle.voice.cmd.Slots;
import com.example.bankapp.modle.voice.constellation.Constellation;
import com.example.bankapp.modle.voice.music.AnswerMusic;
import com.example.bankapp.modle.voice.music.MusicX;
import com.example.bankapp.modle.voice.music.Semantic;
import com.example.bankapp.modle.voice.music.SlotsMusic;
import com.example.bankapp.modle.voice.radio.Radio;
import com.example.bankapp.modle.voice.riddle.Riddle;
import com.example.bankapp.modle.voice.stock.Stock;
import com.example.bankapp.modle.voice.story.Story;
import com.example.bankapp.modle.voice.train.Train;
import com.example.bankapp.modle.voice.wordFinding.WordFinding;
import com.example.bankapp.util.GsonUtil;
import com.example.bankapp.util.SaveUtils;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

/**
 * Created by dell on 2017/9/20.
 */

public class MyAiuiListener implements AIUIListener {

    private Activity activity;

    public MyAiuiListener(Activity activity, AiListener aiListener) {
        this.activity = activity;
        this.aiListener = aiListener;
    }

    private int noResultCount;

    @Override
    public void onEvent(AIUIEvent event) {
        switch (event.eventType) {
            case AIUIConstant.EVENT_WAKEUP:
                break;
            case AIUIConstant.EVENT_SLEEP:
                break;
            case AIUIConstant.EVENT_RESULT:
                String question = null;
                try {
                    XInfo xInfo = GsonUtil.GsonToBean(event.info, XInfo.class);
                    Data data = xInfo.getData().get(0);
                    Content content = data.getContent().get(0);

                    JSONObject cntJson = new JSONObject(new String(event.data.getByteArray(content.getCnt_id()), "utf-8"));
                    JSONObject jsonIntent = cntJson.getJSONObject("intent");
                    question = jsonIntent.getString("text");

                    String service = jsonIntent.getString("service");
                    JSONArray jsonResult = null;
                    String text = null;
                    Cmd cmd = null;
                    if (jsonIntent.has("data")) {
                        JSONObject jsonData = jsonIntent.getJSONObject("data");
                        jsonResult = jsonData.getJSONArray("result");
                    }
                    if (jsonIntent.has("answer")) {
                        JSONObject jsonAnswer = jsonIntent.getJSONObject("answer");
                        text = jsonAnswer.getString("text");
                    }
                    if (jsonIntent.has("semantic")) {
                        JSONArray jsonSemantic = jsonIntent.getJSONArray("semantic");
                        List<Cmd> cmds = GsonUtil.GsonToArrayList(jsonSemantic.toString(), Cmd.class);
                        if (cmds == null || cmds.size() == 0) {
                            aiListener.onDoAnswer(question, "cmd error");
                            return;
                        }
                        cmd = cmds.get(0);
                    }

                    if (service.equals("weather")) {
                        Weather weather = analysisRandom(jsonResult.toString(), Weather.class);
                        aiListener.onDoAnswer(question, text);
                    } else if (service.equals("news")) {
                        News news = analysisRandom(jsonResult.toString(), News.class);
                        aiListener.onDoAnswer(question, text, news);
                    } else if (service.equals("cookbook")) {
                        Cookbook cookbook = analysisRandom(jsonResult.toString(), Cookbook.class);
                        aiListener.onDoAnswer(question, text, cookbook);
                    } else if (service.equals("poetry")) {
                        Poetry poetry = analysisRandom(jsonResult.toString(), Poetry.class);
                        aiListener.onDoAnswer(question, poetry);
                    } else if (service.equals("joke")) {
                        Joke joke = analysisRandom(jsonResult.toString(), Joke.class);
                        aiListener.onDoAnswer(question, text, joke);
                    } else if (service.equals("story")) {
                        List<Story> stories = GsonUtil.GsonToArrayList(jsonResult.toString(), Story.class);
                        int random = new Random().nextInt(stories.size());
                        Story story = stories.get(random);
                        aiListener.onDoAnswer(question, text, story);
                    } else if (service.equals("radio")) {
                        if (jsonIntent.has("error")) {
                            Error error = GsonUtil.GsonToBean(jsonIntent.getJSONObject("error").toString(), Error.class);
                            aiListener.onDoAnswer(question, error.getMessage());
                        } else {
                            List<Radio> radios = GsonUtil.GsonToArrayList(jsonResult.toString(), Radio.class);
                            int random = new Random().nextInt(radios.size());
                            Radio radio = radios.get(random);
                            aiListener.onDoAnswer(question, text, radio);
                        }
                    } else if (service.equals("train")) {
                        if (jsonIntent.has("data")) {
                            List<Train> trains = GsonUtil.GsonToArrayList(jsonResult.toString(), Train.class);
                            if (trains != null && trains.size() > 0) {
                                aiListener.onDoAnswer(question, text + " 详情请查看列表中数据", trains, null);
                            } else {
                                aiListener.onDoAnswer(question, text);
                            }
                        } else {
                            aiListener.onDoAnswer(question, text);
                        }
                    } else if (service.equals("flight")) {
                        if (jsonIntent.has("data")) {
                            List<Flight> flights = GsonUtil.GsonToArrayList(jsonResult.toString(), Flight.class);
                            if (flights != null && flights.size() > 0) {
                                aiListener.onDoAnswer(question, text + " 详情请查看列表中数据", flights, null);
                            } else {
                                aiListener.onDoAnswer(question, text);
                            }
                        } else {
                            aiListener.onDoAnswer(question, text);
                        }
                    } else if (service.equals("translation")) {
                        if (jsonIntent.has("data")) {
                            List<Translation> translates = GsonUtil.GsonToArrayList(jsonResult.toString(), Translation.class);
                            aiListener.onTranslation(question, translates.get(0).getTranslated());
                        }

                    } else if (service.equals("cmd")) {
                        if (cmd != null && cmd.getIntent().equals("INSTRUCTION")) {
                            List<Slots> slots = cmd.getSlots();
                            if (slots != null && slots.size() > 0) {
                                Slots slotsCmd = slots.get(0);
                                aiListener.onDoAnswer(question, slotsCmd);

                            } else {
                                aiListener.onDoAnswer(question, "目前不支持此命令控制");
                            }
                        } else {
                            aiListener.onDoAnswer(question, "目前不支持此命令控制");
                        }
                    } else if (service.equals("airControl_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加空调设备");
                    } else if (service.equals("curtain_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加窗帘设备");
                    } else if (service.equals("fan_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加风扇设备");
                    } else if (service.equals("airCleaner_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加空气进化器设备");
                    } else if (service.equals("airCooler_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加空调扇设备");
                    } else if (service.equals("airVent_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加排风扇设备");
                    } else if (service.equals("bathroomMaster_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加浴霸设备");
                    } else if (service.equals("cleaningRobot_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加扫地机器人设备");
                    } else if (service.equals("coffeeMaker_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加咖啡机设备");
                    } else if (service.equals("cookStove_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加灶具设备");
                    } else if (service.equals("cleaningRobot_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加扫地机器人设备");
                    } else if (service.equals("deHumidifier_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加除湿器设备");
                    } else if (service.equals("dishWasher_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加洗碗机设备");
                    } else if (service.equals("electricKettle_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加电水壶设备");
                    } else if (service.equals("footBath_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加足浴盆设备");
                    } else if (service.equals("light_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加灯设备");
                    } else if (service.equals("freezer_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加冰箱设备");
                    } else if (service.equals("humidifier_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加加湿器设备");
                    } else if (service.equals("heater_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加取暖器设备");
                    } else if (service.equals("homeMonitor_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加监控设备");
                    } else if (service.equals("inductionStove_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加电磁炉设备");
                    } else if (service.equals("microwaveOven_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加微波炉设备");
                    } else if (service.equals("oven_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加烤箱设备");
                    } else if (service.equals("racks_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加晾衣杆设备");
                    } else if (service.equals("rangeHood_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加油烟机设备");
                    } else if (service.equals("riceCooker_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加电饭煲设备");
                    } else if (service.equals("slot_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加智能插座设备");
                    } else if (service.equals("switch_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加开关设备");
                    } else if (service.equals("soymilk_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加豆浆机设备");
                    } else if (service.equals("sterilizer_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加消毒柜设备");
                    } else if (service.equals("toaster_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加面包机设备");
                    } else if (service.equals("toiletLid_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加智能座便器设备");
                    } else if (service.equals("towelRack_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加智能毛巾架设备");
                    } else if (service.equals("tv_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加电视设备");
                    } else if (service.equals("underFloorHeating_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加地暖设备");
                    } else if (service.equals("washer_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加洗衣机设备");
                    } else if (service.equals("waterHeater_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加热水器设备");
                    } else if (service.equals("webcam_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加摄像头设备");
                    } else if (service.equals("window_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加窗户设备");
                    } else if (service.equals("waterHeater_smartHome")) {
                        aiListener.onDoAnswer(question, "暂未添加热水器设备");
                    } else if (service.equals("scheduleX")) {
                        aiListener.onDoAnswer(question, text);
                    } else if (service.equals("musicPlayer_smartHome")) {
                        aiListener.onDoAnswer(question, "暂无音乐播放器的控制功能");
                    } else if (service.equals("internetRadio")) {
                        aiListener.onDoAnswer(question, "暂无网络电台节目查询功能");
                    } else if (service.equals("baike")) {
                        JSONObject jsonAnswer = jsonIntent.getJSONObject("answer");
                        String finalText = jsonAnswer.getString("text");
                        aiListener.onDoAnswer(question, finalText);
                    } else if (service.equals("motorViolation")) {
                        JSONObject jsonAnswer = jsonIntent.getJSONObject("answer");
                        String finalText = jsonAnswer.getString("text");
                        aiListener.onDoAnswer(question, finalText);
                    } else if (service.equals("weixin")) {
                        aiListener.onDoAnswer(question, "暂不支持微信");
                    } else if (service.equals("stock")) {
                        Stock stock = analysisRandom(jsonResult.toString(), Stock.class);
                        aiListener.onDoAnswer(question, text, stock);
                    } else if (service.equals("musicX")) {
                        JSONObject jsonAnswer = jsonIntent.getJSONObject("answer");
                        MusicX musicX = GsonUtil.GsonToBean(jsonIntent.toString(), MusicX.class);
                        if (musicX != null) {
                            List<Semantic> semantics = musicX.getSemantic();
                            if (semantics != null && semantics.size() > 0) {
                                Semantic semantic = semantics.get(0);
                                List<SlotsMusic> slotses = semantic.getSlots();
                                if (slotses != null && slotses.size() > 0) {
                                    aiListener.onMusic(question, "没有当前的音乐，为您播放此首歌曲吧");
                                    break;
                                } else {
                                    aiListener.onMusic(question, null);
                                }
                            } else {
                                aiListener.onMusic(question, null);
                            }
                        } else {
                            aiListener.onMusic(question, null);
                        }
                        if (jsonAnswer.has("answer")) {
                            Answer answerMusic = GsonUtil.GsonToBean(jsonAnswer.toString(), Answer.class);
                            question = answerMusic.getQuestion().getQuestion();
                            String finalText = answerMusic.getText();
                            aiListener.onDoAnswer(question, finalText);
                        } else {
                            String finalText = jsonAnswer.getString("text");
                            aiListener.onDoAnswer(question, finalText);
                        }
                    } else if (service.equals("calc")) {
                        aiListener.onDoAnswer(question, text);
                    } else if (service.equals("datetime")) {
                        aiListener.onDoAnswer(question, text);
                    } else if (service.equals("telephone")) {
                        if (cmd != null && cmd.getIntent().equals("DIAL")) {
                            aiListener.onDoDial(question, cmd.getSlots().get(0).getValue());
                        } else if (cmd != null && cmd.getIntent().equals("SET")) {
                            aiListener.onDoDial(question, cmd.getSlots().get(0).getValue());
                        } else {
                            aiListener.onDoAnswer(question, "暂不支持拨打电话，号码纠错功能");
                        }
                    } else if (service.equals("englishEveryday")) {
                        EnglishEveryday englishEveryday = analysisRandom(jsonResult.toString(), EnglishEveryday.class);
                        aiListener.onDoAnswer(question, text, englishEveryday);
                    } else if (service.equals("constellation")) {
                        Constellation constellation = analysisRandom(jsonResult.toString(), Constellation.class);
                        aiListener.onDoAnswer(question, text, constellation);
                    } else if (service.equals("riddle")) {
                        Riddle riddle = analysisRandom(jsonResult.toString(), Riddle.class);
                        aiListener.onDoAnswer(question, text, riddle);
                    } else if (service.equals("wordFinding")) {
                        WordFinding wordFinding = analysisRandom(jsonResult.toString(), WordFinding.class);
                        aiListener.onDoAnswer(question, text, wordFinding);
                    } else if (service.equals("openQA")) {
                        aiListener.onDoAnswer(question, text);
                    } else {
//                        SaveUtils.getInstance().appendJson(question);
                        aiListener.onDoAnswer(question, "等待添加！！！");
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    noResult(question);
                }
                break;
            case AIUIConstant.EVENT_ERROR:
                if (aiListener != null) {
                    aiListener.onError();
                }
                break;
            case AIUIConstant.EVENT_VAD:
                if (AIUIConstant.VAD_BOS == event.arg1) {
//                    Print.i("找到vad_bos");
                } else if (AIUIConstant.VAD_EOS == event.arg1) {
//                    Print.i("找到vad_eos");

                } else {
                }
                break;

            case AIUIConstant.EVENT_START_RECORD:
//                Print.i("开始录音");
                Toast.makeText(activity, "开始录音", Toast.LENGTH_SHORT).show();
                break;
            case AIUIConstant.EVENT_STOP_RECORD:
//                Print.i("停止录音");
                break;
            case AIUIConstant.EVENT_STATE:     // 状态事件
                int mAIUIState = event.arg1;
                if (AIUIConstant.STATE_IDLE == mAIUIState) {
//                    Print.e("STATE_IDLE");
                } else if (AIUIConstant.STATE_READY == mAIUIState) {
//                    Print.e("STATE_READY");
                    if (aiListener != null) {
                        aiListener.onAIUIDowm();
                    }
                } else if (AIUIConstant.STATE_WORKING == mAIUIState) {
//                    Print.e("STATE_WORKING");
                }
                break;
            case AIUIConstant.EVENT_CMD_RETURN: {
                if (AIUIConstant.CMD_UPLOAD_LEXICON == event.arg1) {
                    //某条CMD命令对应的返回事件。对于除CMD_GET_STATE外的有返回的命令，都会返回该事件，用arg1标识对应的CMD命令，arg2为返回值，0表示成功，info字段为描述信息
                }
            }
            break;
            default:
                break;
        }
    }

    private <T> T analysisRandom(String result, Class<T> cls) {
        if (result == null) {
            return null;
        }
        List<T> tList = GsonUtil.GsonToArrayList(result, cls);
        int random = new Random().nextInt(tList.size());
        T t = tList.get(random);
        return t;
    }

    private void noResult(String question) {
//        SaveUtils.getInstance().appendJson(question);
        noResultCount++;
        if (noResultCount > 5) {
            noResultCount = 0;
            String finalText = resFoFinal(R.array.no_result);
            String otherText = null;
            int random = new Random().nextInt(4);
            if (random == 0) {
//                 otherText = resFoFinal(R.array.other_misic);
                //暂时用笑话代替
                otherText = resFoFinal(R.array.other_joke);
            } else if (random == 1) {
//                otherText = resFoFinal(R.array.other_stiry);
                //暂时用笑话代替
                otherText = resFoFinal(R.array.other_joke);
            } else if (random == 2) {
                otherText = resFoFinal(R.array.other_joke);
            } else if (random == 3) {
                otherText = resFoFinal(R.array.other_poetry);
            }
            aiListener.onNoAnswer(question, finalText, otherText);
        } else {
            // 当听不懂的时候 传入的参数为空 重新进入监听状态
//            String finalText = resFoFinal(R.array.no_result);
            aiListener.onDoAnswer(question, "");
        }
    }


    private String resFoFinal(int id) {
        String[] arrResult = activity.getResources().getStringArray(id);
        return arrResult[new Random().nextInt(arrResult.length)];
    }

    private AiListener aiListener;

    public interface AiListener {

        void onDoAnswer(String question, String finalText);

        void onDoAnswer(String question, String text, News news);

        void onDoAnswer(String question, String text, Cookbook cookbook);

        void onDoAnswer(String question, Poetry poetry);

        void onDoAnswer(String question, String finalText, Joke joke);

        void onDoAnswer(String question, String finalText, Story story);

        void onDoAnswer(String question, String finalText, List<Train> trains, Train train);

        void onDoAnswer(String question, String finalText, List<Flight> flights, Flight flight);

        void onNoAnswer(String question, String finalText, String otherText);

        void onDoAnswer(String question, String finalText, Radio radio);

        void onMusic(String question, String finalText);

        void onTranslation(String question, String value);

        void onDoAnswer(String question, Slots slotsCmd);

        void onDoAnswer(String question, String finalText, EnglishEveryday englishEveryday);

        void onDoAnswer(String question, String finalText, Constellation constellation);

        void onDoAnswer(String question, String finalText, Stock stock);

        void onDoAnswer(String question, String finalText, Riddle riddle);

        void onDoAnswer(String question, String finalText, WordFinding wordFinding);

        void onDoDial(String question, String value);

        void onError();

        void onAIUIDowm();
    }
}
