package com.github.klyser8.mobmadness.game.mob.soulstates;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.mob.Beast;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Monster;
import org.bukkit.metadata.FixedMetadataValue;

//TODO fix creepers exploding even under this soul state, and spiders jumping towards players in this state
public class Dazzled extends SoulState {

    private final Sound dazzleSound;
//    public static final String DAZZLED_TAG = "mm.soulstate.dazzled";

    public Dazzled(MobMadness plugin, GameData gameData, Beast beast, int duration) {
        super(plugin, gameData, SoulStateType.DAZZLED, beast, 1, duration);
//        dazzleSound = new Sound("block.amethyst_block.chime", 1.0f, 1.25f);
        dazzleSound = new Sound("block.amethyst_cluster.step", 0.3f, 2.0f);
//        entity.getMob().setMetadata(DAZZLED_TAG, new FixedMetadataValue(plugin, true));
        runSoulState(1, 1);
    }

    @Override
    protected void startSoulState() {
        beast.getMob().setTarget(null);
        beast.getMob().setMetadata(Beast.IGNORE_PLAYERS_TAG, new FixedMetadataValue(plugin, true));
    }

    @Override
    public void applySoulStateLoop() {
        Monster mob = (Monster) beast.getMob();
        Beast madMob = gameData.getMobMap().get(mob);
        if (madMob == null) return;
        mob.getPathfinder().moveTo(madMob.getSpawnLoc());
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
