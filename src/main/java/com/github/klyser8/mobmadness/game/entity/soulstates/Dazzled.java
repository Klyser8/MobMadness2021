package com.github.klyser8.mobmadness.game.entity.soulstates;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.entity.beast.Beast;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Monster;
import org.bukkit.metadata.FixedMetadataValue;

public class Dazzled extends SoulState {

    private final Sound dazzleSound;
    private final Beast beast;

    public Dazzled(MobMadness plugin, Game game, Beast beast, int duration) {
        super(plugin, game, SoulStateType.DAZZLED, beast, 1, duration);
        this.beast = beast;
        dazzleSound = new Sound("block.amethyst_cluster.step", 0.3f, 2.0f);
        runSoulState(1, 1);
    }

    @Override
    protected void startSoulState() {
        beast.getMob().setTarget(null);
        beast.getMob().setMetadata(Beast.IGNORE_PLAYERS_TAG, new FixedMetadataValue(plugin, true));
    }

    @Override
    public void applySoulStateLoop() {
        beast.getMob().getPathfinder().moveTo(beast.getSpawnLoc());
    }

    @Override
    public void playSoulStateParticles() {
        World world = beast.getMob().getWorld();
        Monster mob = (Monster) beast.getMob();
        mob.getBoundingBox().getWidthX();
        world.spawnParticle(Particle.ELECTRIC_SPARK, mob.getEyeLocation(), level,
                mob.getBoundingBox().getWidthX() / 2.5,
                mob.getBoundingBox().getHeight() / 6,
                mob.getBoundingBox().getWidthZ() / 2.5,
                0);
    }

    @Override
    public void playSoulStateSounds() {
        dazzleSound.play(beast.getMob().getEyeLocation(), SoundCategory.AMBIENT);
    }

    @Override
    public void endSoulState() {
        super.endSoulState();
        beast.getMob().removeMetadata(Beast.IGNORE_PLAYERS_TAG, plugin);
    }
}
