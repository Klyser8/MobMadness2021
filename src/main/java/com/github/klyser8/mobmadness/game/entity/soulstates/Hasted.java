package com.github.klyser8.mobmadness.game.entity.soulstates;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.entity.Soul;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

public class Hasted extends SoulState {

    private final Sound hasteSound;
    private double originalSpeed;

    public Hasted(MobMadness plugin, Game game, Soul soul, int level, int duration) {
        super(plugin, game, SoulStateType.HASTED, soul, level, duration);
        hasteSound = new Sound("entity.phantom.flap", 0.3f, 2.0f);
        runSoulState(1, 5);
    }

    @Override
    public void startSoulState() {
        originalSpeed = soul.getLivingEntity().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
        double speed = soul.getLivingEntity().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
        soul.getLivingEntity().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).
                setBaseValue(speed * (1.0 + (level / 10.0)));
    }

    @Override
    protected void applySoulStateLoop() {

    }

    @Override
    public void playSoulStateParticles() {
        World world = soul.getLivingEntity().getWorld();
        LivingEntity entity = soul.getLivingEntity();
        if (entity.isOnGround()) {
            world.spawnParticle(Particle.REDSTONE,
                    entity.getLocation(), level,
                    entity.getBoundingBox().getWidthX() / 2.5,
                    0.1,
                    entity.getBoundingBox().getWidthZ() / 2.5,
                    0, new Particle.DustTransition(Color.AQUA, Color.LIME, 1.0f));
        } else {
            world.spawnParticle(Particle.REDSTONE,
                    entity.getLocation(), level,
                    entity.getBoundingBox().getWidthX() / 2.5,
                    entity.getBoundingBox().getHeight() / 5.0,
                    entity.getBoundingBox().getWidthZ() / 2.5,
                    0, new Particle.DustTransition(Color.AQUA, Color.LIME, 1.0f));
        }
    }

    @Override
    public void playSoulStateSounds() {
        hasteSound.play(soul.getLivingEntity().getEyeLocation(), SoundCategory.AMBIENT);
    }

    @Override
    public void endSoulState() {
        super.endSoulState();
        soul.getLivingEntity().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).
                setBaseValue(originalSpeed);
    }
}
