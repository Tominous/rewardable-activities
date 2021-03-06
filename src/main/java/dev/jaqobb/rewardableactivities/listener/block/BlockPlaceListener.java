/*
 * MIT License
 *
 * Copyright (c) 2020 Jakub Zagórski (jaqobb)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.jaqobb.rewardableactivities.listener.block;

import com.cryptomorin.xseries.XMaterial;
import dev.jaqobb.rewardableactivities.RewardableActivitiesConstants;
import dev.jaqobb.rewardableactivities.RewardableActivitiesPlugin;
import dev.jaqobb.rewardableactivities.data.RewardableActivity;
import dev.jaqobb.rewardableactivities.data.RewardableActivityReward;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public final class BlockPlaceListener implements Listener {

    private final RewardableActivitiesPlugin plugin;

    public BlockPlaceListener(final RewardableActivitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (this.plugin.isBlockPlaceOwnershipCheckEnabled()) {
            this.plugin.setMetadata(block, RewardableActivitiesConstants.PLACED_BY_PLAYER_KEY, true);
        }
        RewardableActivity rewardableActivity = this.plugin.getRepository().getBlockPlaceRewardableActivity(XMaterial.matchXMaterial(block.getType()));
        if (rewardableActivity == null) {
            return;
        }
        RewardableActivityReward rewardableActivityReward = rewardableActivity.getReward(player);
        if (rewardableActivityReward != null) {
            rewardableActivityReward.reward(this.plugin.getEconomy(), player);
        }
    }
}
