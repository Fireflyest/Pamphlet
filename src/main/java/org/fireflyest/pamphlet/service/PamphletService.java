package org.fireflyest.pamphlet.service;

import java.util.List;
import java.util.UUID;

import org.fireflyest.craftdatabase.annotation.Auto;
import org.fireflyest.craftdatabase.annotation.Service;
import org.fireflyest.craftdatabase.sql.SQLService;
import org.fireflyest.pamphlet.bean.Diary;
import org.fireflyest.pamphlet.bean.Reward;
import org.fireflyest.pamphlet.bean.Steve;
import org.fireflyest.pamphlet.dao.DiaryDao;
import org.fireflyest.pamphlet.dao.RewardDao;
import org.fireflyest.pamphlet.dao.SteveDao;

import com.google.gson.Gson;

@Service
public class PamphletService extends SQLService {

    public PamphletService(String url) {
        super(url);
    }

    @Auto
    public DiaryDao diaryDao;

    @Auto
    public RewardDao rewardDao;

    @Auto
    public SteveDao steveDao;
    
    // *****************************************
    public Diary selectDiaryByTarget(String target) {
        return diaryDao.selectDiaryByTarget(target);
    }

    public long insertDiary(String target) {
        return diaryDao.insertDiary(target);
    }


    // *****************************************
    public Reward[] selectRewardByType(String type, int season) {
        return rewardDao.selectRewardByType(type, season);
    }

    public long insertReward(String type, int season, int num, String item) {
        return rewardDao.insertReward(type, season, num, item);
    }

    public long updateRewardCommands(long id, List<String> commandList) {
        Gson gson = new Gson();
        return rewardDao.updateRewardCommands(id, gson.toJson(commandList));
    }

    // *****************************************
    public Steve selectSteveByUid(UUID uid) {
        return steveDao.selectSteveByUid(uid.toString());
    }

    public long insertSteve(UUID uid, String name, int season) {
        return steveDao.insertSteve(uid.toString(), name, season);
    }

}
