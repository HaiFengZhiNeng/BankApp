package com.example.bankapp.database.manager;

import com.example.bankapp.database.LocalMoneyServiceDao;
import com.example.bankapp.database.base.BaseManager;
import com.example.bankapp.modle.LocalMoneyService;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * Created by dell on 2017/12/21.
 */

public class IntroduceManager extends BaseManager<LocalMoneyService, Long> {
    @Override
    public AbstractDao<LocalMoneyService, Long> getAbstractDao() {
        return daoSession.getLocalMoneyServiceDao();
    }

    public List<LocalMoneyService> queryIntroduceByQuestion(String videoShart) {
        Query<LocalMoneyService> build = null;
        try {
            build = getAbstractDao().queryBuilder()
                    .where(LocalMoneyServiceDao.Properties.IntroduceQuestion.like("%" + videoShart + "%"))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return build.list();
    }
}
