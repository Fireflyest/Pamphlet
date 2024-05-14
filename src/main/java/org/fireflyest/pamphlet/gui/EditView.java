package org.fireflyest.pamphlet.gui;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.fireflyest.craftgui.api.View;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.pamphlet.Pamphlet;
import org.fireflyest.pamphlet.service.PamphletService;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class EditView implements View<EditPage> {

    private final Map<String, EditPage> pageMap = new HashMap<>();

    private final PamphletService service;
    private final ViewGuide guide;
    private String lastContent = "";

    public EditView(PamphletService service, ViewGuide guide) {
        this.service = service;
        this.guide = guide;

        // 输入框监听
        ProtocolLibrary.getProtocolManager().addPacketListener(
            new PacketAdapter(Pamphlet.getPlugin(),
                    ListenerPriority.NORMAL,
                    PacketType.Play.Client.ITEM_NAME) {

                @Override
                public void onPacketReceiving(PacketEvent event) {
                    if (event.getPacketType() != PacketType.Play.Client.ITEM_NAME) {
                        return;
                    }
                    // 获取数据包
                    String playerName = event.getPlayer().getName();
                    PacketContainer packet = event.getPacket();

                    // 不是正在编辑
                    if (guide.unUsed(playerName)) {
                        return;
                    }
                    // 判断是否对应界面
                    ViewPage page = guide.getUsingPage(playerName);
                    if (! (page instanceof EditPage)) {
                        return;
                    }
                    EditPage editPage = ((EditPage) page);
                    String content = packet.getStrings().read(0);

                    if ("".equals(content) || lastContent.equals(content) || EditPage.TIP_TEXT.equals(content)) {
                        return;
                    }
                    lastContent = content;
                    editPage.updateContent(content);

                    guide.refreshPage(playerName);
                }
            }
        );
    }

    /**
     * target应为奖励id
     */
    @Override
    @Nullable
    public EditPage getFirstPage(@Nullable String target) {
        return pageMap.computeIfAbsent(target, k -> new EditPage(target, service, guide));
    }

    @Override
    public void removePage(@Nullable String target) {
        // 
    }
    
}
