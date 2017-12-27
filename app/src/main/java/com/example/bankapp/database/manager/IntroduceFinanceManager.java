package com.example.bankapp.database.manager;

import com.example.bankapp.database.LocalMoneyServiceDao;
import com.example.bankapp.database.LocalMoneyVideoDao;
import com.example.bankapp.database.base.BaseManager;
import com.example.bankapp.modle.LocalMoneyService;
import com.example.bankapp.modle.LocalMoneyVideo;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * Created by dell on 2017/12/21.
 */

public class IntroduceFinanceManager extends BaseManager<LocalMoneyVideo, Long> {
    @Override
    public AbstractDao<LocalMoneyVideo, Long> getAbstractDao() {
        return daoSession.getLocalMoneyVideoDao();
    }

    public List<LocalMoneyVideo> queryIntroduceByQuestion(String videoShart) {
        Query<LocalMoneyVideo> build = null;
        try {
            build = getAbstractDao().queryBuilder()
                    .where(LocalMoneyVideoDao.Properties.VideoName.like("%" + videoShart + "%"))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return build.list();
    }
}
