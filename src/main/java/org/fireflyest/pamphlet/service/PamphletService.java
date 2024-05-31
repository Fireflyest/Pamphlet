package org.fireflyest.pamphlet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftdatabase.annotation.Auto;
import org.fireflyest.craftdatabase.annotation.Service;
import org.fireflyest.craftdatabase.sql.SQLService;
import org.fireflyest.pamphlet.bean.Diary;
import org.fireflyest.pamphlet.bean.Reward;
import org.fireflyest.pamphlet.bean.Season;
import org.fireflyest.pamphlet.bean.Steve;
import org.fireflyest.pamphlet.dao.DiaryDao;
import org.fireflyest.pamphlet.dao.RewardDao;
import org.fireflyest.pamphlet.dao.SeasonDao;
import org.fireflyest.pamphlet.dao.SteveDao;
import org.fireflyest.util.SerializationUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    @Auto
    public SeasonDao seasonDao;
    
    // *****************************************
    public Diary selectDiaryByTarget(String target) {
        return diaryDao.selectDiaryByTarget(target);
    }

    public long selectSeasonPlaytime(String uid, int season) {
        return diaryDao.selectSeasonPlaytime(uid, season);
    }

    public long insertDiary(String target, int season) {
        return diaryDao.insertDiary(target, season);
    }

    public long updateDiarySign(String target) {
        return diaryDao.updateDiarySign(target);
    }

    public long updateDiaryPlaytimeAdd(String target, long playtime) {
        return diaryDao.updateDiaryPlaytimeAdd(target, playtime);
    }

    public long updateDiaryPlaytimeQuota(String target, String quota) {
        return diaryDao.updateDiaryPlaytimeQuota(target, quota);
    }

    // *****************************************
    public Reward[] selectRewardByType(String type, int season) {
        return rewardDao.selectRewardByType(type, season);
    }

    public Reward selectRewardRandom(String type, String symbol, long num, int season) {
        return rewardDao.selectRewardRandom(type, symbol, num, season);
    }

    public Reward selectRewardById(int id) {
        return rewardDao.selectRewardById(id);
    }

    public long insertReward(String type, int season, int num, String item) {
        return rewardDao.insertReward(type, season, num, item);
    }

    public long updateRewardCommands(long id, List<String> commandList) {
        Gson gson = new Gson();
        return rewardDao.updateRewardCommands(id, gson.toJson(commandList));
    }

    public long addRewardCommand(long id, String command) {
        String commands = rewardDao.selectRewardCommands(id);
        Gson gson = new Gson();
        List<String> commandsList = commands == null ? new ArrayList<>() : gson.fromJson(commands, new TypeToken<List<String>>() {}.getType());
        commandsList.add(command);
        return this.updateRewardCommands(id, commandsList);
    }

    public long removeRewardCommand(long id, String command) {
        String commands = rewardDao.selectRewardCommands(id);
        if (commands == null) {
            return 0;
        }
        Gson gson = new Gson();
        List<String> commandsList = gson.fromJson(commands, new TypeToken<List<String>>() {}.getType());
        commandsList.remove(command);
        return this.updateRewardCommands(id, commandsList);
    }

    public long updateRewardName(long id, String name) {
        return rewardDao.updateRewardName(id, name);
    }

    public long updateRewardNum(long id, long num) {
        return rewardDao.updateRewardNum(id, num);
    }

    public long deleteReward(long id) {
        return rewardDao.deleteReward(id);
    }

    // *****************************************
    public Steve selectSteveByUid(UUID uid) {
        return steveDao.selectSteveByUid(uid.toString());
    }

    public int selectSteveSignedByUid(UUID uid) {
        return steveDao.selectSteveSignedByUid(uid.toString());
    }

    public int selectSteveSeriesByUid(UUID uid) {
        return steveDao.selectSteveSignedByUid(uid.toString());
    }

    public int selectSteveSeasonByUid(UUID uid) {
        return steveDao.selectSteveSeasonByUid(uid.toString());
    }

    public long insertSteve(UUID uid, String name, int season) {
        return steveDao.insertSteve(uid.toString(), name, season);
    }

    public long updateSteveSignedAdd(UUID uid) {
        return steveDao.updateSteveSignedAdd(uid.toString());
    }

    public long updateSteveSeriesAdd(UUID uid) {
        return steveDao.updateSteveSeriesAdd(uid.toString());
    }

    public long updateSteveSeriesReset(UUID uid) {
        return steveDao.updateSteveSeriesReset(uid.toString());
    }

    public long updateSteveExpAdd(UUID uid, int exp) {
        return steveDao.updateSteveExpAdd(uid.toString(), exp);
    }

    public long updateSteveExpReset(UUID uid) {
        return steveDao.updateSteveExpReset(uid.toString());
    }

    public long updateSeasonPlaytimeQuota(UUID uid, String quota) {
        return steveDao.updateSeasonPlaytimeQuota(uid.toString(), quota);
    }

    public long deleteSteveByUid(UUID uid) {
        return steveDao.deleteSteveByUid(uid.toString());
    }

    // *****************************************
    public Season selectSeasonById(int id) {
        return seasonDao.selectSeasonById(id);
    }
    
    public int[] selectSeasonIds() {
        return seasonDao.selectSeasonIds();
    }

    public long insertSeason(String name, ItemStack item) {
        return seasonDao.insertSeason(name, SerializationUtil.serialize(item));
    }

    public long updateSeasonDesc(int id, String desc) {
        return seasonDao.updateSeasonDesc(id, desc);
    }

    public long updateSeasonOutset(int id, long outset) {
        return seasonDao.updateSeasonOutset(id, outset);
    }

    public long updateSeasonPlayersAdd(int id) {
        return seasonDao.updateSeasonPlayersAdd(id);
    }

    public long updateSeasonAdvanceAdd(int id) {
        return seasonDao.updateSeasonAdvanceAdd(id);
    }

}
