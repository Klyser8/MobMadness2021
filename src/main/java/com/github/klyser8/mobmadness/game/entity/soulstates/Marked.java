package com.github.klyser8.mobmadness.game.entity.soulstates;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.entity.Soul;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Marked extends SoulState {

    private static final double BASE_ARROW_MULTIPLIER = 2.5;

    private final Sound triggerSound;

    public Marked(MobMadness plugin, Game game, Soul soul, int level, int duration) {
        super(plugin, game, SoulStateType.MARKED, soul, level, duration);
        triggerSound = new Sound("entity.llama.spit", 2.0f, 1.0f);
        runSoulState(5, 0);
    }

    @Override
    protected void startSoulState() {

    }

    @Override
    protected void applySoulStateLoop() {

    }

    @Override
    protected void playSoulStateParticles() {
        World world = soul.getLivingEntity().getWorld();
        world.spawnParticle(Particle.SNEEZE, soul.getLivingEntity().getEyeLocation(), 5,
                0.25, 0.5 ,0.25, 0.025);
    }

    @Override
    protected void playSoulStateSounds() {

    }

    /**
     * Provided the appropriate event, amplifies the damage an arrow has dealt.
     *
     * @param event a reference to the event which dealt damage to the entity.
     */
    public void handleDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow)) return;
        event.setDamage(event.getDamage() * (BASE_ARROW_MULTIPLIER + (BASE_ARROW_MULTIPLIER / 10 * level - 1)));
        triggerSound.play(soul.getLivingEntity().getLocation(), SoundCategory.PLAYERS);
        //If the mark is triggered, the soul state should end.
        currentDuration = 1;
    }
}
