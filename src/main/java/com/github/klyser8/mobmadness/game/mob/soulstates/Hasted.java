package com.github.klyser8.mobmadness.game.mob.soulstates;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.mob.MadEntity;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Monster;

public class Hasted extends SoulState {

    private final Sound hasteSound;
    private double originalSpeed;

    public Hasted(MobMadness plugin, GameData gameData, MadEntity entity, int level, int duration) {
        super(plugin, gameData, SoulStateType.HASTED, entity, level, duration);
        hasteSound = new Sound("entity.phantom.flap", 0.3f, 2.0f);
        runSoulState(1, 5);
    }

    @Override
    public void startSoulState() {
        originalSpeed = entity.getMob().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
        double speed = entity.getMob().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
        System.out.println("Speed before: " + speed);
        entity.getMob().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).
                setBaseValue(speed * (1.0 + (level / 10.0)));
        System.out.println("Speed after: " + entity.getMob().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
    }

    @Override
    protected void applySoulStateLoop() {

    }

    @Override
    public void playSoulStateParticles() {
        World world = entity.getMob().getWorld();
        Monster mob = (Monster) entity.getMob();
        if (mob.isOnGround()) {
            world.spawnParticle(Particle.REDSTONE,
                    mob.getLocation(), level,
                    mob.getBoundingBox().getWidthX() / 2.5,
                    0.1,
                    mob.getBoundingBox().getWidthZ() / 2.5,
                    0, new Particle.DustTransition(Color.AQUA, Color.LIME, 1.0f));
        } else {
            world.spawnParticle(Particle.REDSTONE,
                    mob.getLocation(), level,
                    mob.getBoundingBox().getWidthX() / 2.5,
                    mob.getBoundingBox().getHeight() / 5.0,
                    mob.getBoundingBox().getWidthZ() / 2.5,
                    0, new Particle.DustTransition(Color.AQUA, Color.LIME, 1.0f));
        }
    }

    @Override
    public void playSoulStateSounds() {
        hasteSound.play(entity.getMob().getEyeLocation(), SoundCategory.AMBIENT);
    }

    @Override
    public void endSoulState() {
        super.endSoulState();
        entity.getMob().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).
                setBaseValue(originalSpeed);
        System.out.println("Speed at the end: " + entity.getMob().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
    }
}
