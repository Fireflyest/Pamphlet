package org.fireflyest.pamphlet.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftgui.button.ButtonAction;
import org.fireflyest.craftgui.event.ViewPlaceEvent;
import org.fireflyest.pamphlet.bean.Diary;
import org.fireflyest.pamphlet.bean.Steve;
import org.fireflyest.pamphlet.data.Config;
import org.fireflyest.pamphlet.data.Language;
import org.fireflyest.pamphlet.service.PamphletService;
import org.fireflyest.util.SerializationUtil;
import org.fireflyest.util.TimeUtils;

public class PlayerEventListener implements Listener {
    
    private final PamphletService service;

    public PlayerEventListener(PamphletService service) {
        this.service = service;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        //  判断玩家数据是否存在
        Steve steve = service.selectSteveByUid(player.getUniqueId());
        // 新玩家插入数据，否则更新旧周目数据
        if (steve == null) {
            service.insertSteve(player.getUniqueId(), player.getName(), Config.SEASON);
        } else if (steve.getSeason() <Config.SEASON) {
            // todo
        }

        // 玩家是否当天已签到，未签发送提示
        String target = player.getUniqueId().toString() + "-" + TimeUtils.getLocalDate();
        Diary diary = service.selectDiaryByTarget(target);
        // 今日没有数据
        if (diary == null) {
            diary = new Diary(target);
            service.insertDiary(target);
        }
        // 判断是否签到
        if (!diary.isSign()) {
            player.sendMessage(Language.SIGN_REMIND);
            // ChatUtils.sendCommandButton(player, "每日签到", "点击打开签到界面", "/activity");
        }
    }

    @EventHandler
    public void onViewPlace(ViewPlaceEvent event) {
        int action = event.getAction();

        String view = event.getViewName();
        String value = event.getValue();

        // 判断是否本插件相关的事件
        if(view == null || !view.startsWith("pamphlet")) return;

        // 获取点击到的物品，一般来说是有物品
        ItemStack placeItem = event.getCursorItem();
        ItemStack item = event.getCurrentItem();
        if(item == null) return;

        // 行为
        if (action == ButtonAction.ACTION_PLUGIN) {
            // value的格式为type num season
            String type = value.split(" ")[0];
            int num = NumberConversions.toInt(value.split(" ")[1]);
            int season = NumberConversions.toInt(value.split(" ")[2]);
            String stack = SerializationUtil.serializeItemStack(placeItem);
            service.insertReward(type, season, num, stack);
        }

    }


    public static void testTime() {
        System.out.println(TimeUtils.getLocalDate());
        System.out.println(NumberConversions.toInt(" 004"));
        
    }

    // public static void main(String[] args) {
    //     testTime();
    // }

}
