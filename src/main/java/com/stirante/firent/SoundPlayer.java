package com.stirante.firent;

import org.bukkit.Location;
import org.bukkit.Sound;

import java.util.ArrayList;

public class SoundPlayer {

    private final Location location;
    private final Sound sound;
    private final ArrayList<SoundAction> actions = new ArrayList<>();

    private SoundPlayer(Location location, Sound sound) {
        this.location = location;
        this.sound = sound;
    }

    public static SoundPlayer createNew(Location location, Sound sound) {
        return new SoundPlayer(location, sound);
    }

    public SoundPlayer addNote(Tone tone) {
        actions.add(new SoundAction(tone, ActionType.PLAY, 0));
        return this;
    }

    public SoundPlayer addPause(int duration) {
        actions.add(new SoundAction(null, ActionType.WAIT, duration));
        return this;
    }

    public boolean isFinished() {
        return actions.size() == 0;
    }

    public void update() {
        if (!isFinished()) {
            if (actions.get(0).update()) {
                actions.remove(0);
            }
        }
    }

    public void play() {
        MagicWorld.getWorld(location.getWorld()).addSound(this);
    }

    private enum ActionType {
        WAIT,
        PLAY
    }

    public enum Tone {
        F_SHARP1(0.5f),
        G1(0.53f),
        G_SHARP1(0.561f),
        A1(0.595f),
        A_SHARP1(0.63f),
        B1(0.667f),
        C2(0.707f),
        C_SHARP2(0.749f),
        D2(0.794f),
        D_SHARP2(0.841f),
        E2(0.891f),
        F2(0.944f),
        F_SHARP2(1f),
        G2(1.059f),
        G_SHARP2(1.122f),
        A2(1.189f),
        A_SHARP2(1.26f),
        B2(1.335f),
        C3(1.414f),
        C_SHARP3(1.498f),
        D3(1.587f),
        D_SHARP3(1.682f),
        E3(1.782f),
        F3(1.888f),
        F_SHARP3(2f);

        private final float v;

        Tone(float v) {
            this.v = v;
        }

        public float getPitch() {
            return v;
        }
    }

    private class SoundAction {
        private Tone tone;
        private ActionType type;
        private int duration;

        public SoundAction(Tone tone, ActionType type, int duration) {
            this.tone = tone;
            this.type = type;
            this.duration = duration;
        }

        private boolean update() {
            if (type == ActionType.WAIT) {
                duration--;
                if (duration <= 0) {
                    return true;
                }
            }
            else if (type == ActionType.PLAY) {
                location.getWorld().playSound(location, sound, 1.0f, tone.getPitch());
                return true;
            }
            return false;
        }

    }

}
