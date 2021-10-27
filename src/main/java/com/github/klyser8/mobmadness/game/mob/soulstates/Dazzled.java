package com.github.klyser8.mobmadness.game.mob.soulstates;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.mob.MadEntity;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import static com.github.klyser8.mobmadness.util.MadUtils.getRandomMadMob;

//TODO fix creepers exploding even under this soul state, and spiders jumping towards players in this state
public class Dazzled extends SoulState {

    private final Sound dazzleSound;
//    public static final String DAZZLED_TAG = "mm.soulstate.dazzled";

    public Dazzled(MobMadness plugin, GameData gameData, SoulStateType type, MadEntity entity, int duration) {
        super(plugin, gameData, type, entity, 1, duration);
//        dazzleSound = new Sound("block.amethyst_block.chime", 1.0f, 1.25f);
        dazzleSound = new Sound("block.amethyst_cluster.step", 0.3f, 2.0f);
//        entity.getMob().setMetadata(DAZZLED_TAG, new FixedMetadataValue(plugin, true));
        new BukkitRunnable() {
            @Override
            public void run() {
                runSoulState();
                playSoulStateParticles();
                playSoulStateSounds();
                currentDuration--;
                if (currentDuration == 0 || entity.getMob().isDead()) {
                    cancel();
                    entity.getCurrentSoulStates().remove(getType());
//                    entity.getMob().removeMetadata(DAZZLED_TAG, plugin);
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @Override
    public void runSoulState() {
        Monster mob = (Monster) entity.getMob();
        MadEntity madMob = gameData.getMobMap().get(mob);
        if (madMob == null) return;
        mob.getPathfinder().moveTo(madMob.getSpawnLoc());
//        MadEntity madMob = getRandomMadMob(gameData);
//        if (madMob == null) madMob.getMob().getPathfinder().moveTo(madMob)
//        mob.getPathfinder().moveTo(getRandomMadMob(gameData).getMob());
        if (mob instanceof Creeper) {
            Creeper creeper = (Creeper) mob;
            creeper.setIgnited(false);
        }
    }

    @Override
    public void playSoulStateParticles() {
        World world = entity.getMob().getWorld();
        Monster mob = (Monster) entity.getMob();
        mob.getBoundingBox().getWidthX();
        world.spawnParticle(Particle.ELECTRIC_SPARK, mob.getEyeLocation(), level,
                mob.getBoundingBox().getWidthX() / 2.5,
                mob.getBoundingBox().getHeight() / 6,
                mob.getBoundingBox().getWidthZ() / 2.5,
                0);
    }

    @Override
    public void playSoulStateSounds() {
        dazzleSound.play(entity.getMob().getEyeLocation(), SoundCategory.HOSTILE);
    }
}
